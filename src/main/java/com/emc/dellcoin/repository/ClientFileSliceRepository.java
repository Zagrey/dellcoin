package com.emc.dellcoin.repository;

import com.emc.dellcoin.model.ClientFileSlice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientFileSliceRepository extends JpaRepository<ClientFileSlice, Long> {

}
