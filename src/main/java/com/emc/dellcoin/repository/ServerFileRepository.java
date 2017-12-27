package com.emc.dellcoin.repository;

import com.emc.dellcoin.model.ServerFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerFileRepository extends JpaRepository<ServerFile, Long> {

    void deleteByHash(String h);
}
