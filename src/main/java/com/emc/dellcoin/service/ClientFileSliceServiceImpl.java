package com.emc.dellcoin.service;

import com.emc.dellcoin.model.ClientFileSlice;
import com.emc.dellcoin.model.ClientFile;
import com.emc.dellcoin.repository.ClientFileSliceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.emc.dellcoin.common.Utils.bytesToHex;


@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ClientFileSliceServiceImpl implements ClientFileSliceService {
    @Autowired
    private ClientFileSliceRepository clientFileSliceRepository;

    @Override
    public Collection<ClientFileSlice> findAll() {

        return clientFileSliceRepository.findAll();
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public List<ClientFileSlice> create(ClientFile clientFile) {
        if (clientFile.getId() == 0) {
            return null;
        }

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        List<ClientFileSlice> clientFileSliceList = new ArrayList<>();

        int fileSize = clientFile.getContent().length();

        if (digest != null) {
            clientFile.setHash(bytesToHex(digest.digest(clientFile.getContent().getBytes(StandardCharsets.UTF_8))));

            clientFileSliceList.add(new ClientFileSlice(0, clientFile.getId(), 0, fileSize/2, bytesToHex(digest.digest(clientFile.getContent().substring(0, fileSize/2).getBytes(StandardCharsets.UTF_8)))));
            clientFileSliceList.add(new ClientFileSlice(0, clientFile.getId(), fileSize/2, fileSize - 1, bytesToHex(digest.digest(clientFile.getContent().substring(fileSize/2, fileSize - 1).getBytes(StandardCharsets.UTF_8)))));
            clientFileSliceList.add(new ClientFileSlice(0, clientFile.getId(), 1, fileSize - 2, bytesToHex(digest.digest(clientFile.getContent().substring(1, fileSize - 2).getBytes(StandardCharsets.UTF_8)))));

            clientFileSliceRepository.save(clientFileSliceList);
        }

        return clientFileSliceList;
    }



    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(Long id) {

        clientFileSliceRepository.delete(id);

    }

}