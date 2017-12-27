package com.emc.dellcoin.service;

import com.emc.dellcoin.model.Contract;
import java.util.Collection;


public interface ContractService {

    Collection<Contract> findAll();

    Contract findOne(String id);

    Contract create(Contract f);

    Contract updateClient(Contract f);

    Contract updateServer(Contract f);

    void delete(Contract id);
}