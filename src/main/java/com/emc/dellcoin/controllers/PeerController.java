package com.emc.dellcoin.controllers;

import com.emc.dellcoin.model.Peer;
import com.emc.dellcoin.service.PeerService;
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
public class PeerController {
    @Autowired
    private PeerService peerService;
    private static final Logger log = LoggerFactory.getLogger(PeerController.class);

    @RequestMapping(
            value = "/peers",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Peer>> getAll() {

        log.info("getPeers: start");
        List<Peer> peers = peerService.findAll();
        log.info("getPeers: end");
        return new ResponseEntity<>(peers, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/peer",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Peer> create(
            @RequestBody Peer peer) {

        log.info("createPeer: before sent");
        Peer savedPeer = peerService.create(peer);
        log.info("createPeer: after sent");
        return new ResponseEntity<>(savedPeer, HttpStatus.CREATED);
    }

    @RequestMapping(
            value = "/peers/{id}",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Peer> delete(@PathVariable("id") Long id,
                                               @RequestBody Peer peer) {

        log.info("deleteGreeting: before sent");
        peerService.delete(id);
        log.info("deleteGreeting: after sent");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
