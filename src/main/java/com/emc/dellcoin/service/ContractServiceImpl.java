package com.emc.dellcoin.service;

import com.emc.dellcoin.model.Contract;
import com.emc.dellcoin.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;


@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ContractServiceImpl implements ContractService {
    @Autowired
    private ContractRepository contractRepository;


    @Override
    public Collection<Contract> findAll() {
        return contractRepository.findAll();
    }

    @Override
    public Contract findOne(String id) {
        return contractRepository.findOne(id);
    }

    @Override
    public Contract create(Contract f) {
        return contractRepository.save(f);
    }

    @Override
    public Contract updateClient(Contract f) {

        Contract contractToUpdate = findOne(f.getHash());
        if (contractToUpdate == null) {
            return null;
        }

        contractToUpdate.setClientCheckCount(f.getClientCheckCount());
        contractToUpdate.setClientSeed(f.getClientSeed());
        contractToUpdate.setClientOrigSum(f.getClientOrigSum());

        return contractRepository.save(contractToUpdate);
    }

    @Override
    public Contract updateServer(Contract f) {

        Contract contractToUpdate = findOne(f.getHash());
        if (contractToUpdate == null) {
            return null;
        }

        contractToUpdate.setServerSeed(f.getServerSeed());
        contractToUpdate.setServerSum(f.getServerSum());

        return contractRepository.save(contractToUpdate);
    }

    @Override
    public void delete(Contract id) {

    }
}