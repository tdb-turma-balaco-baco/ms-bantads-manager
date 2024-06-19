package br.ufpr.tads.msbantadsmanager;

import br.ufpr.tads.msbantadsmanager.manager.ManagerController;
import br.ufpr.tads.msbantadsmanager.manager.ManagerRepository;
import br.ufpr.tads.msbantadsmanager.manager.ManagerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
class MsBantadsManagerApplicationTests {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private ManagerController controller;
    @Autowired
    private ManagerService service;
    @Autowired
    private ManagerRepository repository;

    @Test
    void contextLoads() {
        assertNotNull(controller);
        assertNotNull(service);
        assertNotNull(repository);
    }

}
