package com.emc.dellcoin.repository;

import com.emc.dellcoin.model.File;
import com.emc.dellcoin.model.Peer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

}
