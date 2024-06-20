package br.ufpr.tads.msbantadsmanager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import br.ufpr.tads.msbantadsmanager.api.rest.ManagerController;
import br.ufpr.tads.msbantadsmanager.infrastructure.persistence.ManagerEntityRepository;
import br.ufpr.tads.msbantadsmanager.core.application.ManagerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class MsBantadsManagerEntityApplicationTests {
  @Container @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

  @Autowired private ManagerController controller;
  @Autowired private ManagerServiceImpl service;
  @Autowired private ManagerEntityRepository repository;

  @Test
  void contextLoads() {
    assertNotNull(controller);
    assertNotNull(service);
    assertNotNull(repository);
  }
}
