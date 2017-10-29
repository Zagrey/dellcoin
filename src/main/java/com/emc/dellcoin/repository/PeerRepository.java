package com.emc.dellcoin.repository;

import com.emc.dellcoin.model.Peer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeerRepository extends JpaRepository<Peer, Long> {

}
