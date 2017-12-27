package com.emc.dellcoin.service;

import com.emc.dellcoin.model.ClientFile2Peer;

import java.util.Collection;


public interface ClientFile2PeerService {

    Collection<ClientFile2Peer> findAll();

    ClientFile2Peer findOne(Long id);

    ClientFile2Peer create(ClientFile2Peer f);

    void delete(long id);

}