package com.emc.dellcoin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ClientFileSlice {
    @Id
    @GeneratedValue
    private Long id;
    private Long fileId; // id original file
    private int startIndex;
    private int endIndex;
    private String hash;
}
