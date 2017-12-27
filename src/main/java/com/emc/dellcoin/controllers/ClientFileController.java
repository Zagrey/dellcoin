package com.emc.dellcoin.controllers;

import com.emc.dellcoin.model.ClientFile;
import com.emc.dellcoin.model.Contract;
import com.emc.dellcoin.service.ClientFileSliceService;
import com.emc.dellcoin.service.ClientFileService;
import com.emc.dellcoin.service.ContractService;
import com.emc.dellcoin.service.ServerFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

@RestController
public class ClientFileController {
    @Autowired
    private ClientFileService clientFileService;
    @Autowired
    private ServerFileService serverFileService;
    @Autowired
    private ClientFileSliceService clientFileSliceService;
    @Autowired
    private ContractService contractService;

    private static final Logger log = LoggerFactory.getLogger(ClientFileController.class);

    @RequestMapping(
            value = "/",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Integer>> getRoot() {
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/clientFiles",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ClientFile>> getClientFiles() {

        log.info("getClientFiles: start");
        Collection<ClientFile> clientFiles = clientFileService.findAll();

        log.info("getClientFiles: end");
        return new ResponseEntity<>(clientFiles, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/clientFile",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientFile> createClientFile(
            @RequestBody ClientFile clientFile) {

        log.info("createClientFile: before sent");
        ClientFile savedClientFile = clientFileService.create(clientFile);
        log.info("createClientFile: after sent");

        // insert into contract
        if (savedClientFile != null) {
            Random rTmp = new Random();
            long clientSeed = rTmp.nextLong();
            long clientOrigSum = 0;
            int clientCheckCount = savedClientFile.getContent().length() / 2; // we check 1/2 bytes in orig file

            Random r = new Random(clientSeed);

//            List<Integer> idxArray = new ArrayList<>();

            for (int i = 0; i < clientCheckCount; i++) {
                int idx = r.nextInt(savedClientFile.getContent().length());
//                idxArray.add(idx);
                System.out.println("Client: file size: " + savedClientFile.getContent().length() + ", check idx: " + idx + ", char: " + savedClientFile.getContent().charAt(idx));

                clientOrigSum += savedClientFile.getContent().charAt(idx);
            }

            Contract c = new Contract();
            // update client related data
            c.setHash(savedClientFile.getHash());
            c.setClientOrigSum(clientOrigSum);
            c.setClientSeed(clientSeed);
            c.setClientCheckCount(clientCheckCount);
            c.setSize(savedClientFile.getContent().length());
            contractService.create(c);
        }

        log.info("createSlices: before sent");
        clientFileSliceService.create(clientFile);
        log.info("createSlices: after sent");
        return new ResponseEntity<>(savedClientFile, HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/decode/{hash}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientFile> decodeFile(@PathVariable("hash") String hash) {

        Contract contract = contractService.findOne(hash);
        ClientFile clientFile = clientFileService.findOneByHash(hash);

        Random clientRandom = new Random(contract.getClientSeed());
        long serverSum = 0;
        for (int i = 0; i < contract.getClientCheckCount(); i++) {
            serverSum += clientFile.getContent().charAt(clientRandom.nextInt(clientFile.getContent().length()));
        }

        contract.setServerSum(serverSum);

        if (contract.isValid()) {

            Random serverRandom = new Random(contract.getServerSeed());

            StringBuilder stringBuffer = new StringBuilder();
            for (int i = 0; i < clientFile.getContent().length(); i++) {
                stringBuffer.append((char) (clientFile.getContent().charAt(i) - serverRandom.nextInt(1000)));
            }

            clientFile.setContent(stringBuffer.toString());
            clientFileService.update(clientFile);
            serverFileService.delete(clientFile.getHash());

            return new ResponseEntity<>(clientFile, HttpStatus.OK);
        }

        return new ResponseEntity<>(clientFile, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(
            value = "/clientFiles/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientFile> updateClientFile(
            @RequestBody ClientFile clientFile) {

        ClientFile updatedClientFile = clientFileService.update(clientFile);
        if (updatedClientFile == null) {
            return new ResponseEntity<>(
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(updatedClientFile, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/clientFiles/{id}",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ClientFile> deleteClientFiles(@PathVariable("id") Long id,
                                                        @RequestBody ClientFile clientFile) {

        log.info("deleteClientFiles: before sent");
        clientFileService.delete(id);
        log.info("deleteClientFiles: after sent");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public void corsHeaders(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with");
        response.addHeader("Access-Control-Max-Age", "3600");
    }
}
