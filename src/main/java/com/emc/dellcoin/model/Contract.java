package com.emc.dellcoin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Contract {
    @Id
    String hash;
    long clientSeed;       // seed for indexes calculation
    long clientCheckCount; // how many chars (indexes) in file need to check.
    long clientOrigSum;    // client sum by indexes.
    long serverSeed;       // server seed - key for client
    long serverSum;        // server sum by indexes
    long size;             // file size
    String status;         // just for demonstration

    @Transient
    public boolean isValid() {

        Random serverRandom = new Random(serverSeed);
        Random clientRandom = new Random(clientSeed);

        List<Integer> sb = new ArrayList<>();
        long randSum = 0;

        // server random sequence for the file
        for (int i = 0; i < size; i++) {
            sb.add(serverRandom.nextInt(1000));
        }

        // server random sum on client indexes
        for (int i = 0; i < clientCheckCount; i++) {
            randSum += sb.get(clientRandom.nextInt((int) size));
        }

        if (clientOrigSum == serverSum - randSum) {
            return true;
        }

        return false;
    }
}
