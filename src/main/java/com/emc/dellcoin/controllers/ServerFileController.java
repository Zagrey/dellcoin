package com.emc.dellcoin.controllers;

import com.emc.dellcoin.common.SimpleRandom;
import com.emc.dellcoin.model.ClientFile;
import com.emc.dellcoin.model.Contract;
import com.emc.dellcoin.model.ServerFile;
import com.emc.dellcoin.model.VerifyFileRequest;
import com.emc.dellcoin.service.ContractService;
import com.emc.dellcoin.service.ServerFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.emc.dellcoin.common.Utils.bytesToHex;

@RestController
public class ServerFileController {
    @Autowired
    private ServerFileService serverFileService;
    @Autowired
    private ContractService contractService;

    private static final Logger log = LoggerFactory.getLogger(ServerFileController.class);


    @RequestMapping(
            value = "/serverFiles",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ServerFile>> getServerFiles() {

        log.info("getServerFiles: start");
        Collection<ServerFile> serverFiles = serverFileService.findAll();

        log.info("getServerFiles: end");
        return new ResponseEntity<>(serverFiles, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/serverFiles/{hash}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientFile> getServerFile(@PathVariable("hash") String hash) {

        Collection<ServerFile> serverFiles = serverFileService.findAll();
        ClientFile cf = new ClientFile();
        cf.setHash(hash);
        ServerFile sf = serverFiles.stream().filter(f -> f.getHash().equals(hash)).findFirst().orElse(null);

        StringBuilder sb = new StringBuilder();

        if (sf == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {

            Random rTmp = new Random();
            int serverSeed = rTmp.nextInt();
            SimpleRandom r = new SimpleRandom(serverSeed);

            String content = sf.getContent();
            int i = 0;
            for (char c : content.toCharArray()) {
                int ri = r.nextInt(1000);
                sb.append((char) (c + ri));
                System.out.println("Server: char: " + c + ", +rand: " + ri);
                i++;
            }

            Contract c = contractService.findOne(hash);
            content = sb.toString();
            cf.setContent(content);

            // update server related data
            c.setHash(hash);
            c.setServerSeed(serverSeed);
            contractService.updateServer(c);

            cf.setServerSeed(serverSeed); // TODO should call from java BE - here
        }

        return new ResponseEntity<ClientFile>(cf, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/serverFiles/verify/{hash}",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> verifyServerFile(@PathVariable("hash") String hash,
                                                                @RequestBody VerifyFileRequest verifyFileRequest) {

        log.info("verifyServerFile: start");
        Map<String, String> resultMap = new HashMap<>();
        Collection<ServerFile> serverFiles = serverFileService.findAll();

        for (ServerFile serverFile : serverFiles) {

            if (serverFile.getHash().equals(hash)) {

                MessageDigest digest = null;
                try {
                    digest = MessageDigest.getInstance("SHA-256");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                if (digest != null) {
                    String sliceHash = bytesToHex(digest.digest(serverFile.getContent().substring(verifyFileRequest.getStart(),
                            verifyFileRequest.getEnd()).getBytes(StandardCharsets.UTF_8)));
                    resultMap.put("hash", sliceHash);
                    resultMap.put("start", String.valueOf(verifyFileRequest.getStart()));
                    resultMap.put("end", String.valueOf(verifyFileRequest.getEnd()));

                    log.info("verifyServerFile: [" + verifyFileRequest.getStart() + " - " + verifyFileRequest.getEnd() + "] :" + sliceHash);
                }
            }
        }

        log.info("verifyServerFile: end");
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/serverFile",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServerFile> createServerFile(
            @RequestBody ServerFile serverFile) {

        log.info("createServerFile: before sent");
        ServerFile savedServerFile = serverFileService.create(serverFile);
        log.info("createServerFile: after sent");

        return new ResponseEntity<>(savedServerFile, HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/serverFiles/{id}",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ServerFile> deleteServerFiles(@PathVariable("id") Long id,
                                                        @RequestBody ServerFile serverFile) {

        log.info("deleteServerFiles: before sent");
        serverFileService.delete(id);
        log.info("deleteServerFiles: after sent");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
