package com.emc.dellcoin.repository;

import com.emc.dellcoin.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, String> {

}
