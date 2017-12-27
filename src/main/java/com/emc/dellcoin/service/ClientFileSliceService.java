package com.emc.dellcoin.service;

import com.emc.dellcoin.model.ClientFile;
import com.emc.dellcoin.model.ClientFileSlice;

import java.util.Collection;
import java.util.List;


public interface ClientFileSliceService {

    Collection<ClientFileSlice> findAll();

    List<ClientFileSlice> create(ClientFile slice);

    void delete(Long id);

}