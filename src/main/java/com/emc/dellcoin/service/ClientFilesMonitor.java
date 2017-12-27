package com.emc.dellcoin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientFilesMonitor {
    private Logger logger = LoggerFactory.getLogger(ClientFilesMonitor.class);
    @Autowired
    private ClientFileService clientFileService;


//    @Scheduled(cron = "*/5 * * * * *")
//    public void fileMonitor() {
//
//        Collection<ClientFile> clientFiles = clientFileService.findAll();
//        logger.info("File monitor started.");
//
//        for (ClientFile clientFile : clientFiles) {
//            logger.info("Verify file: " + clientFile.getId() + ", hash: " + clientFile.getHash());
//        }
//    }

}