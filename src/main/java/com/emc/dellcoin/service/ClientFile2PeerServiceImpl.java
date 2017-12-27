package com.emc.dellcoin.service;

import com.emc.dellcoin.model.ClientFile2Peer;
import com.emc.dellcoin.repository.ClientFile2PeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;


@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ClientFile2PeerServiceImpl implements ClientFile2PeerService {
    @Autowired
    private ClientFile2PeerRepository clientFile2PeerRepository;

    @Override
    public Collection<ClientFile2Peer> findAll() {

        return clientFile2PeerRepository.findAll();
    }

    @Override
    public ClientFile2Peer findOne(Long id) {

        return clientFile2PeerRepository.findOne(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ClientFile2Peer create(ClientFile2Peer clientFile2Peer) {
        if (clientFile2Peer.getId() != 0) {
            return null;
        }

        return clientFile2PeerRepository.save(clientFile2Peer);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(long id) {

        clientFile2PeerRepository.delete(id);

    }

}