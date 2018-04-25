package net.greenpoppy.tahiti.service;

import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;


@SpringBootTest
public class ServiceTestBase {

    protected Instant getInstant(String isoDateTime) {
        return Instant.parse(isoDateTime);
    }
}
