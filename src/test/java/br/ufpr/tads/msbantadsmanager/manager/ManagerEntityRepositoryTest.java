package br.ufpr.tads.msbantadsmanager.manager;

import static org.junit.jupiter.api.Assertions.*;

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
    var createManager =
        new CreateManagerRequest(
            "name",
            "lastName",
            "email@email.com",
            "12312312300",
            "1112341234",
            "admin@admin.com");
    validEntity = ManagerEntity.create(createManager);
  }

  @BeforeEach
  void setUp() {
    this.repository.deleteAll();
  }

  @Test
  @DisplayName("should successfully create a manager with dto CreateManager")
  void success_save() {
    this.repository.save(validEntity);
    assertEquals(1, this.repository.count());
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"invalid", "invalid@email.com"})
  @DisplayName("should not find a manager by invalid email")
  void invalid_findManagerByEmail(String invalidEmail) {
    this.repository.save(validEntity);
    assertTrue(this.repository.findManagerByEmail(invalidEmail).isEmpty());
  }

  @Test
  @DisplayName("should find a manager by email")
  void findManagerByEmail() {
    this.repository.save(validEntity);
    assertTrue(this.repository.findManagerByEmail(validEntity.getEmail()).isPresent());
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"invalid", "99999999999"})
  @DisplayName("should not find a manager by invalid cpf")
  void invalid_findManagerByCpf(String invalidCpf) {
    this.repository.save(validEntity);
    assertTrue(this.repository.findManagerByCpf(invalidCpf).isEmpty());
  }

  @Test
  @DisplayName("should find a manager by cpf")
  void findManagerByCpf() {
    this.repository.save(validEntity);
    assertTrue(this.repository.findManagerByCpf(validEntity.getCpf()).isPresent());
  }

  @Test
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  @DisplayName("should not create a manager with null values")
  void exception_nullValues() {
    var invalidEmail = new ManagerEntity();
    invalidEmail.setCpf("12312312300");
    invalidEmail.setEmail(null);
    invalidEmail.setPhoneNumber("1112341234");
    invalidEmail.setFirstName("name");
    invalidEmail.setLastName("lastName");
    invalidEmail.setCreatedDate(LocalDateTime.now());
    invalidEmail.setLastModifiedDate(LocalDateTime.now());
    assertThrows(DataIntegrityViolationException.class, () -> this.repository.save(invalidEmail));

    var invalidCpf = new ManagerEntity();
    invalidCpf.setCpf(null);
    invalidCpf.setEmail("email@email.com");
    invalidCpf.setPhoneNumber("1112341234");
    invalidCpf.setFirstName("name");
    invalidCpf.setLastName("lastName");
    invalidCpf.setCreatedDate(LocalDateTime.now());
    invalidCpf.setLastModifiedDate(LocalDateTime.now());
    assertThrows(DataIntegrityViolationException.class, () -> this.repository.save(invalidCpf));

    var invalidFirstName = new ManagerEntity();
    invalidFirstName.setCpf("12312312300");
    invalidFirstName.setEmail("email@email.com");
    invalidFirstName.setPhoneNumber("1112341234");
    invalidFirstName.setFirstName(null);
    invalidFirstName.setLastName("lastName");
    invalidFirstName.setCreatedDate(LocalDateTime.now());
    invalidFirstName.setLastModifiedDate(LocalDateTime.now());
    assertThrows(
        DataIntegrityViolationException.class, () -> this.repository.save(invalidFirstName));

    var invalidLastName = new ManagerEntity();
    invalidLastName.setCpf("12312312300");
    invalidLastName.setEmail("email@email.com");
    invalidLastName.setPhoneNumber("1112341234");
    invalidLastName.setFirstName("name");
    invalidLastName.setLastName(null);
    invalidLastName.setCreatedDate(LocalDateTime.now());
    invalidLastName.setLastModifiedDate(LocalDateTime.now());
    assertThrows(
        DataIntegrityViolationException.class, () -> this.repository.save(invalidLastName));

    var invalidPhone = new ManagerEntity();
    invalidPhone.setCpf("12312312300");
    invalidPhone.setEmail("email@email.com");
    invalidPhone.setPhoneNumber(null);
    invalidPhone.setFirstName("name");
    invalidPhone.setLastName("lastName");
    invalidPhone.setCreatedDate(LocalDateTime.now());
    invalidPhone.setLastModifiedDate(LocalDateTime.now());
    assertThrows(DataIntegrityViolationException.class, () -> this.repository.save(invalidPhone));

    assertEquals(0, this.repository.count());
  }
}
