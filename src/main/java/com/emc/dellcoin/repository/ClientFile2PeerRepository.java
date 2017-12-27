package com.emc.dellcoin.repository;

import com.emc.dellcoin.model.ClientFile2Peer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientFile2PeerRepository extends JpaRepository<ClientFile2Peer, Long> {

}
