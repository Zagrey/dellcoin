package com.emc.dellcoin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ClientFile2Peer {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    private long file;
    private long peer;
}
