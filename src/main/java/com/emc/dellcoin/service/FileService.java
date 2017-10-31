package com.emc.dellcoin.service;

import com.emc.dellcoin.model.File;

import java.util.Collection;


public interface FileService {

    Collection<File> findAll();

    File findOne(Long id);

    File create(File f);

    File update(File f);

    void delete(Long id);

}