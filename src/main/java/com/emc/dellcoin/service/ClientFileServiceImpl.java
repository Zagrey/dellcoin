package com.emc.dellcoin.service;

import com.emc.dellcoin.model.ClientFile;
import com.emc.dellcoin.repository.ClientFileRepository;
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
public class ClientFileServiceImpl implements ClientFileService {
    @Autowired
    private ClientFileRepository clientFileRepository;

    @Override
    public Collection<ClientFile> findAll() {

        Collection<ClientFile> clientFiles = clientFileRepository.findAll();

        return clientFiles;
    }

    @Override
    public ClientFile findOne(Long id) {

        ClientFile clientFile = clientFileRepository.findOne(id);

        return clientFile;
    }

    @Override
    public ClientFile findOneByHash(String h) {

        ClientFile clientFile = clientFileRepository.getOneByHash(h);

        return clientFile;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ClientFile create(ClientFile clientFile) {
        if (clientFile.getId() != 0) {
            return null;
        }

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (digest != null) {
            clientFile.setHash(bytesToHex(digest.digest(clientFile.getContent().getBytes(StandardCharsets.UTF_8))));
        }

        return clientFileRepository.save(clientFile);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ClientFile update(ClientFile clientFile) {
        ClientFile clientFileToUpdate = findOne(clientFile.getId());
        if (clientFileToUpdate == null) {
            return null;
        }

        clientFileToUpdate.setContent(clientFile.getContent());

        return clientFileRepository.save(clientFileToUpdate);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(Long id) {

        clientFileRepository.delete(id);

    }

}