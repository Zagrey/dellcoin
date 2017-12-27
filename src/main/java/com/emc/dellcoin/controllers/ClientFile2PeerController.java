package com.emc.dellcoin.controllers;

import com.emc.dellcoin.model.ClientFile2Peer;
import com.emc.dellcoin.service.ClientFile2PeerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class ClientFile2PeerController {
    @Autowired
    private ClientFile2PeerService clientFile2PeerService;

    private static final Logger log = LoggerFactory.getLogger(ClientFile2PeerController.class);


    @RequestMapping(
            value = "/clientFiles2Peers",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ClientFile2Peer>> getClientFile2Peers() {

        log.info("getClientFile2Peers: start");
        Collection<ClientFile2Peer> clientFiles2Peer = clientFile2PeerService.findAll();

        log.info("getClientFile2Peers: end");
        return new ResponseEntity<>(clientFiles2Peer, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/clientFiles2Peer",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientFile2Peer> create(
            @RequestBody ClientFile2Peer clientFile2Peer) {

        log.info("createClientFile: before sent");
        ClientFile2Peer savedClientFile2Peer = clientFile2PeerService.create(clientFile2Peer);
        log.info("createClientFile: after sent");
        
        return new ResponseEntity<>(savedClientFile2Peer, HttpStatus.CREATED);
    }



    @RequestMapping(
            value = "/clientFiles2Peer/{id}",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ClientFile2Peer> delete(@PathVariable("id") Long id) {

        log.info("deleteClientFiles: before sent");
        clientFile2PeerService.delete(id);
        log.info("deleteClientFiles: after sent");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    
}
