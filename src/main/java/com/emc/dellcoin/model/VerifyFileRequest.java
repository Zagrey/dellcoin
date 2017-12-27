package com.emc.dellcoin.model;

import lombok.Data;

@Data
public class VerifyFileRequest {
    String fileHash;
    int start;
    int end;
}
