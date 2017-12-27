package com.emc.dellcoin.service;

import com.emc.dellcoin.model.ServerFile;
import com.emc.dellcoin.repository.ServerFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import static com.emc.dellcoin.common.Utils.bytesToHex;


@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ServerFileServiceImpl implements ServerFileService {
    @Autowired
    private ServerFileRepository serverFileRepository;

    @Override
    public Collection<ServerFile> findAll() {

        return serverFileRepository.findAll();
    }

    @Override
    public ServerFile findOne(Long id) {

        return serverFileRepository.findOne(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ServerFile create(ServerFile serverFile) {

        serverFile.setId(0);

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (digest != null) {
            serverFile.setHash(bytesToHex(digest.digest(serverFile.getContent().getBytes(StandardCharsets.UTF_8))));
        }

        return serverFileRepository.save(serverFile);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ServerFile update(ServerFile serverFile) {

        ServerFile serverFileToUpdate = findOne(serverFile.getId());
        if (serverFileToUpdate == null) {
            return null;
        }

        serverFileToUpdate.setContent(serverFile.getContent());
        return serverFileRepository.save(serverFileToUpdate);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long id) {
        serverFileRepository.delete(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String h) {
        serverFileRepository.deleteByHash(h);
    }
}