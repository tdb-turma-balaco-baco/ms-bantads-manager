package br.ufpr.tads.msbantadsmanager.manager;

import br.ufpr.tads.msbantadsmanager.manager.inbound.CreateManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ManagerRepositoryTest {
    @Autowired
    private ManagerRepository repository;
    private static Manager validEntity;

    @BeforeAll
    static void beforeAll() {
        var createManager = new CreateManager(
                "firstName",
                "lastName",
                "12312312300",
                "email@email.com",
                "1112341234",
                "admin@admin.com");
        validEntity = new Manager(createManager);
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
        var invalidEmail = new Manager();
        invalidEmail.setCpf("12312312300");
        invalidEmail.setEmail(null);
        invalidEmail.setPhone("1112341234");
        invalidEmail.setFirstName("firstName");
        invalidEmail.setLastName("lastName");
        invalidEmail.setCreatedAt(LocalDateTime.now());
        invalidEmail.setUpdatedAt(LocalDateTime.now());
        assertThrows(DataIntegrityViolationException.class, () -> this.repository.save(invalidEmail));

        var invalidCpf = new Manager();
        invalidCpf.setCpf(null);
        invalidCpf.setEmail("email@email.com");
        invalidCpf.setPhone("1112341234");
        invalidCpf.setFirstName("firstName");
        invalidCpf.setLastName("lastName");
        invalidCpf.setCreatedAt(LocalDateTime.now());
        invalidCpf.setUpdatedAt(LocalDateTime.now());
        assertThrows(DataIntegrityViolationException.class, () -> this.repository.save(invalidCpf));

        var invalidFirstName = new Manager();
        invalidFirstName.setCpf("12312312300");
        invalidFirstName.setEmail("email@email.com");
        invalidFirstName.setPhone("1112341234");
        invalidFirstName.setFirstName(null);
        invalidFirstName.setLastName("lastName");
        invalidFirstName.setCreatedAt(LocalDateTime.now());
        invalidFirstName.setUpdatedAt(LocalDateTime.now());
        assertThrows(DataIntegrityViolationException.class, () -> this.repository.save(invalidFirstName));

        var invalidLastName = new Manager();
        invalidLastName.setCpf("12312312300");
        invalidLastName.setEmail("email@email.com");
        invalidLastName.setPhone("1112341234");
        invalidLastName.setFirstName("firstName");
        invalidLastName.setLastName(null);
        invalidLastName.setCreatedAt(LocalDateTime.now());
        invalidLastName.setUpdatedAt(LocalDateTime.now());
        assertThrows(DataIntegrityViolationException.class, () -> this.repository.save(invalidLastName));

        var invalidPhone = new Manager();
        invalidPhone.setCpf("12312312300");
        invalidPhone.setEmail("email@email.com");
        invalidPhone.setPhone(null);
        invalidPhone.setFirstName("firstName");
        invalidPhone.setLastName("lastName");
        invalidPhone.setCreatedAt(LocalDateTime.now());
        invalidPhone.setUpdatedAt(LocalDateTime.now());
        assertThrows(DataIntegrityViolationException.class, () -> this.repository.save(invalidPhone));

        assertEquals(0, this.repository.count());
    }
}