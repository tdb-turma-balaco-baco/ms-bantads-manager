package br.ufpr.tads.msbantadsmanager;

import br.ufpr.tads.msbantadsmanager.manager.ManagerController;
import br.ufpr.tads.msbantadsmanager.manager.ManagerRepository;
import br.ufpr.tads.msbantadsmanager.manager.ManagerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MsBantadsManagerApplicationTests {
	@Autowired private ManagerController controller;
	@Autowired private ManagerService service;
	@Autowired private ManagerRepository repository;

	@Test
	void contextLoads() {
		assertNotNull(controller);
		assertNotNull(service);
		assertNotNull(repository);
	}

}
