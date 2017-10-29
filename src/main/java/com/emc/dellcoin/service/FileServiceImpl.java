package com.emc.dellcoin.service;

import com.emc.dellcoin.model.File;
import com.emc.dellcoin.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collection;

import static com.emc.dellcoin.common.Utils.bytesToHex;


@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class FileServiceImpl implements FileService {
    @Autowired
    private FileRepository fileRepository;

    @Override
    public Collection<File> findAll() {

        Collection<File> files = fileRepository.findAll();

        return files;
    }

    @Override
    public File findOne(Long id) {

        File file = fileRepository.findOne(id);

        return file;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public File create(File file) {
        if (file.getId() != null) {
            return null;
        }

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (digest != null) {
            file.setHash(bytesToHex(digest.digest(file.getContent().getBytes(StandardCharsets.UTF_8))));
        }

        return fileRepository.save(file);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public File update(File file) {

        
        File fileToUpdate = findOne(file.getId());
        if (fileToUpdate == null) {
        
            return null;
        }

        fileToUpdate.setContent(file.getContent());

        return fileRepository.save(fileToUpdate);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(Long id) {

        fileRepository.delete(id);

    }

}