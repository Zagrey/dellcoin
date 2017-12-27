package com.emc.dellcoin.service;

import com.emc.dellcoin.model.Peer;

import java.util.List;


public interface PeerService {

    List<Peer> findAll();

    Peer findOne(Long id);

    Peer create(Peer greeting);

    Peer update(Peer greeting);

    void delete(Long id);

}