package com.emc.dellcoin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ClientFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 4096)
    private String content;
    private String hash;


    long clientSeed;       // seed for indexes calculation
    long clientCheckCount; // how many chars (indexes) in file need to check.
    long clientOrigSum;    // client sum by indexes.
    long size;             // file size
    @Transient
    long serverSeed;       // server seed - key for client
//    @Transient
//    long serverSum;        // server sum by indexes

//    private List<ClientFileSlice> clientFileSliceById;

//    @OneToMany(mappedBy = "clientFileByFileid")
//    public List<ClientFileSlice> getClientFileSliceById() {
//        return clientFileSliceById;
//    }
//
//    public void setClientFileSliceById(List<ClientFileSlice> clientFileSliceById) {
//        this.clientFileSliceById = clientFileSliceById;
//    }
}
