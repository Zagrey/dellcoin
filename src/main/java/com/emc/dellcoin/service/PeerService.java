package com.emc.dellcoin.service;

import com.emc.dellcoin.model.Peer;

import java.util.Collection;


public interface PeerService {

    Collection<Peer> findAll();

    Peer findOne(Long id);

    Peer create(Peer greeting);

    Peer update(Peer greeting);

    void delete(Long id);

}