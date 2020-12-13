package com.rerecir.ticksserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class TicksServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicksServerApplication.class, args);
    }

}
