package com.emc.dellcoin.service;

import com.emc.dellcoin.model.Peer;
import com.emc.dellcoin.repository.PeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PeerServiceImpl implements PeerService {
    @Autowired
    private PeerRepository peerRepository;

    @Override
    public List<Peer> findAll() {
        List<Peer> peers = peerRepository.findAll();
        return peers;
    }

    @Override
    public Peer findOne(Long id) {

        Peer peer = peerRepository.findOne(id);

        return peer;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Peer create(Peer peer) {

        // Ensure the entity object to be created does NOT exist in the
        // repository. Prevent the default behavior of save() which will update
        // an existing entity if the entity matching the supplied id exists.
        if (peer.getId() != null) {
            // Cannot create Peer with specified ID value
            return null;
        }

        Peer savedGreeting = peerRepository.save(peer);
        if (savedGreeting.getId() == 7) {
            throw new RuntimeException();
        }

        return savedGreeting;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Peer update(Peer Peer) {

        // Ensure the entity object to be updated exists in the repository to
        // prevent the default behavior of save() which will persist a new
        // entity if the entity matching the id does not exist
        Peer greetingToUpdate = findOne(Peer.getId());
        if (greetingToUpdate == null) {
            // Cannot update Peer that hasn't been persisted
            return null;
        }

        greetingToUpdate.setAddress(Peer.getAddress());
        Peer updatedGreeting = peerRepository.save(greetingToUpdate);

        return updatedGreeting;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(Long id) {

        peerRepository.delete(id);

    }

}