package com.emc.dellcoin.service;

import com.emc.dellcoin.model.ClientFileSlice;
import com.emc.dellcoin.model.File;

import java.util.Collection;
import java.util.List;


public interface ClientFileSliceService {

    Collection<ClientFileSlice> findAll();

    List<ClientFileSlice> create(File slice);

    void delete(Long id);

}