package com.emc.dellcoin.controllers;

import com.emc.dellcoin.model.File;
import com.emc.dellcoin.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

@RestController
public class FileController {
    @Autowired
    private FileService fileService;
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @RequestMapping(
            value = "/files",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<File>> getAll() {

        log.info("getFiles: start");
        Collection<File> greetings = fileService.findAll();


        log.info("getFiles: end");
        return new ResponseEntity<>(greetings, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/file",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<File> create(
            @RequestBody File file) {

        log.info("createFile: before sent");
        File savedFile = fileService.create(file);
        log.info("createFile: after sent");
        return new ResponseEntity<>(savedFile, HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/files/{id}",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<File> delete(@PathVariable("id") Long id,
                                               @RequestBody File file) {

        log.info("deleteGreeting: before sent");
        fileService.delete(id);
        log.info("deleteGreeting: after sent");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value= "/**", method=RequestMethod.OPTIONS)
    public void corsHeaders(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with");
        response.addHeader("Access-Control-Max-Age", "3600");
    }
}
