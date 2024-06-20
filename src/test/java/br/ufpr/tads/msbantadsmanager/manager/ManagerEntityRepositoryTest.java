package br.ufpr.tads.msbantadsmanager.manager;

import static org.junit.jupiter.api.Assertions.*;

import br.ufpr.tads.msbantadsmanager.core.domain.model.Manager;
import br.ufpr.tads.msbantadsmanager.core.domain.vo.CPF;
import br.ufpr.tads.msbantadsmanager.infrastructure.persistence.entity.ManagerEntity;
import br.ufpr.tads.msbantadsmanager.api.rest.dto.CreateManagerRequest;
import java.time.LocalDateTime;

import br.ufpr.tads.msbantadsmanager.infrastructure.persistence.ManagerEntityRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class ManagerEntityRepositoryTest {
  @ServiceConnection @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

  @Autowired private ManagerEntityRepository repository;
  private static ManagerEntity validEntity;

  @BeforeAll
  static void beforeAll() {
    validEntity =
        new ManagerEntity(
            Manager.create(new CPF("12312312300"), "email@email.com", "fullName", "1112341234"));
  }

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Test
  @DisplayName("should successfully create a manager")
  void success_save() {
    repository.save(validEntity);
    assertEquals(1, this.repository.count());
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"invalid", "invalid@email.com"})
  @DisplayName("should not find a manager by searching a inexistent email")
  void invalid_findManagerByEmail(String invalidEmail) {
    repository.save(validEntity);
    assertTrue(repository.findManagerByEmail(invalidEmail).isEmpty());
  }

  @Test
  @DisplayName("should find a manager by searching for a existing email")
  void findManagerByEmail() {
    repository.save(validEntity);
    assertTrue(repository.findManagerByEmail(validEntity.getDomainModel().getEmail()).isPresent());
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"invalid", "99999999999"})
  @DisplayName("should not find a manager by searching for a inexistent cpf")
  void invalid_findManagerByCpf(String inexistentCpf) {
    repository.save(validEntity);
    assertTrue(repository.findManagerByCpf(inexistentCpf).isEmpty());
  }

  @Test
  @DisplayName("should find a manager by searching for a existing cpf")
  void findManagerByCpf() {
    this.repository.save(validEntity);
    assertTrue(
        this.repository
            .findManagerByCpf(validEntity.getDomainModel().getCpf().value())
            .isPresent());
  }

  @Test
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  @DisplayName("should not create a manager with null values")
  void exception_nullValues() {
    var nullAttributesEntity = new ManagerEntity();
    assertThrows(
        DataIntegrityViolationException.class, () -> repository.save(nullAttributesEntity));
    assertEquals(0, this.repository.count());
  }
}
