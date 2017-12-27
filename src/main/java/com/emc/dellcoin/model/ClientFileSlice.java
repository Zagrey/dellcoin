package com.emc.dellcoin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ClientFileSlice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long fileId; // id original file
    private int startIndex;
    private int endIndex;
    private String hash;

//    private ClientFile clientFileByFileid;
//
//    @ManyToOne
//    @JoinColumn(name = "fileid", referencedColumnName = "id", foreignKey = )
//    public ClientFile getClientFileByFileid() {
//        return clientFileByFileid;
//    }
//
//    public void setClientFileByFileid(ClientFile clientFileByFileid) {
//        this.clientFileByFileid = clientFileByFileid;
//    }
}
