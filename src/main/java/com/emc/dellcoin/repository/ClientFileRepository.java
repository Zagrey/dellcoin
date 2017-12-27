package com.emc.dellcoin.repository;

import com.emc.dellcoin.model.ClientFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientFileRepository extends JpaRepository<ClientFile, Long> {

    ClientFile getOneByHash(String h);
}
