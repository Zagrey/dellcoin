package com.emc.dellcoin.service;

import com.emc.dellcoin.model.ClientFile;
import java.util.Collection;


public interface ClientFileService {

    Collection<ClientFile> findAll();

    ClientFile findOne(Long id);

    ClientFile findOneByHash(String h);

    ClientFile create(ClientFile f);

    ClientFile update(ClientFile f);

    void delete(Long id);
}