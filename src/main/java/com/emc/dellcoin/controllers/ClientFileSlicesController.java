package com.emc.dellcoin.controllers;

import com.emc.dellcoin.model.ClientFile;
import com.emc.dellcoin.model.ClientFileSlice;
import com.emc.dellcoin.service.ClientFileSliceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
public class ClientFileSlicesController {
    @Autowired
    private ClientFileSliceService clientFileSliceService;
    private static final Logger log = LoggerFactory.getLogger(ClientFileSlicesController.class);


    @RequestMapping(
            value = "/slices",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ClientFileSlice>> getAll() {

        log.info("getSlices: start");
        Collection<ClientFileSlice> clientFileSlices = clientFileSliceService.findAll();
        log.info("getSlices: end");
        return new ResponseEntity<>(clientFileSlices, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/slice",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientFileSlice>> create(
            @RequestBody ClientFile clientFile) {

        log.info("createSlices: before sent");
        List<ClientFileSlice> savedSlices = clientFileSliceService.create(clientFile);
        log.info("createSlices: after sent");
        return new ResponseEntity<>(savedSlices, HttpStatus.CREATED);
    }


}
