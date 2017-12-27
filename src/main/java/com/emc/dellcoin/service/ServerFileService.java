package com.emc.dellcoin.service;

import com.emc.dellcoin.model.ServerFile;
import java.util.Collection;


public interface ServerFileService {

    Collection<ServerFile> findAll();

    ServerFile findOne(Long id);

    ServerFile create(ServerFile f);

    ServerFile update(ServerFile f);

    void delete(Long id);
    void delete(String h);
}