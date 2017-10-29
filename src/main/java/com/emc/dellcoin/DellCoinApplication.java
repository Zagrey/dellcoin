package com.emc.dellcoin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class DellCoinApplication {

    public static void main(String[] args) {
        SpringApplication.run(DellCoinApplication.class, args);
    }


}
