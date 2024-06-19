package br.ufpr.tads.msbantadsmanager.manager;

import br.ufpr.tads.msbantadsmanager.manager.port.in.CreateManager;
import br.ufpr.tads.msbantadsmanager.manager.port.out.ManagerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class ManagerControllerIntegrationTest {
    @ServiceConnection
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");
    private static final String URL = ManagerController.URL;
    public static final CreateManager CREATE_MANAGER = new CreateManager(
            "firstName",
            "lastName",
            "manager@email.com",
            "12312312311",
            "12345678901",
            "admin@email.com"
    );

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ManagerRepository repository;

    @BeforeEach
    void setUp() {
        this.repository.deleteAll();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("should not create a new manager without firstName")
    void return400_createWithoutFirstName(String emptyFirstName) throws Exception {
        var request = new CreateManager(
                emptyFirstName,
                "lastName",
                "manager@email.com",
                "12312312311",
                "11999999999",
                "admin@email.com"
        );
        var json = objectMapper.writeValueAsString(request);

        this.mockMvc
                .perform(post(URL)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

        assertEquals(0, this.repository.count());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("should not create a new manager without lastName")
    void return400_createWithoutLastName(String emptyLastName) throws Exception {
        var request = new CreateManager(
                "firstName",
                emptyLastName,
                "manager@email.com",
                "12312312311",
                "11999999999",
                "admin@email.com"
        );
        var json = objectMapper.writeValueAsString(request);

        this.mockMvc
                .perform(post(URL)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

        assertEquals(0, this.repository.count());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"123", "not numbers", "1234567890", "123456789012"})
    @DisplayName("should not create a new manager with invalid cpf")
    void return400_createWithInvalidCpf(String invalidCpf) throws Exception {
        var request = new CreateManager(
                "firstName",
                "lastName",
                "manager@email.com",
                invalidCpf,
                "11999999999",
                "admin@email.com"
        );
        var json = objectMapper.writeValueAsString(request);

        this.mockMvc
                .perform(post(URL)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

        assertEquals(0, this.repository.count());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"123", "not email", "123@.com", ".com@.br"})
    @DisplayName("should not create a new manager with invalid email")
    void return400_createWithInvalidEmail(String invalidEmail) throws Exception {
        var request = new CreateManager(
                "firstName",
                "lastName",
                invalidEmail,
                "12345678901",
                "11999999999",
                "admin@email.com"
        );
        var json = objectMapper.writeValueAsString(request);

        this.mockMvc
                .perform(post(URL)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

        assertEquals(0, this.repository.count());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"123", "123456789012", "not numbers"})
    @DisplayName("should not create a new manager with invalid email")
    void return400_createWithInvalidPhone(String invalidPhone) throws Exception {
        var request = new CreateManager(
                "firstName",
                "lastName",
                "manager@email.com",
                "12345678901",
                invalidPhone,
                "admin@email.com"
        );
        var json = objectMapper.writeValueAsString(request);

        this.mockMvc
                .perform(post(URL)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

        assertEquals(0, this.repository.count());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "manager@email.com,12312312311,manager@email.com,12312312311",
            "manager@email.com,12312312311,manager@email.com,99999999999",
            "manager@email.com,12312312311,different@email.com,12312312311",
    })
    @DisplayName("should not create a duplicate manager")
    void return409_createDuplicate(String createEmail,
                                   String createCpf,
                                   String email,
                                   String cpf) throws Exception {
        var request = new CreateManager(
                "firstName",
                "lastName",
                createEmail,
                createCpf,
                "12345678901",
                "admin@email.com"
        );
        var json = objectMapper.writeValueAsString(request);

        var saved = new CreateManager(
                "anotherName",
                "anotherLast",
                email,
                cpf,
                "12345678901",
                "admin@email.com"
        );
        this.repository.save(Manager.create(saved));

        this.mockMvc
                .perform(post(URL)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isConflict());

        assertEquals(1, this.repository.count());
    }

    @Test
    @DisplayName("should create a new manager successfully")
    void return201_createSuccessfully() throws Exception {
        var json = objectMapper.writeValueAsString(CREATE_MANAGER);

        var result = this.mockMvc
                .perform(post(URL)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        assertEquals(1, this.repository.count());

        var saved = this.repository.findManagerByEmail(CREATE_MANAGER.email()).orElseThrow(AssertionError::new);
        assertEquals(result.getResponse().getHeader("Location"), URL + "/" + saved.getId());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0L, -1L, -999L})
    @DisplayName("should not find a manager with invalid id")
    void return400_findManagerByInvalidId(Long invalidId) throws Exception {
        this.repository.save(Manager.create(CREATE_MANAGER));

        this.mockMvc
                .perform(get(URL + "/" + invalidId))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should not find a manager with nonexistent id")
    void return404_findManagerByNonExistentId() throws Exception {
        var saved = this.repository.save(Manager.create(CREATE_MANAGER));
        long invalidId = saved.getId() + 1L;

        this.mockMvc
                .perform(get(URL + "/" + invalidId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should find a manager by id")
    void return200_findManagerById() throws Exception {
        var saved = this.repository.save(Manager.create(CREATE_MANAGER));
        var json = objectMapper.writeValueAsString(ManagerResponse.of(saved));

        var result = this.mockMvc
                .perform(get(URL + "/" + saved.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), json);
    }

    @Test
    @DisplayName("should not find a manager with nonexistent email and cpf")
    void return404_findManagerByNonExistentEmailOrCpf() throws Exception {
        this.repository.save(Manager.create(CREATE_MANAGER));

        this.mockMvc
                .perform(get(URL).param("email", "another@email.com"))
                .andDo(print())
                .andExpect(status().isNotFound());

        this.mockMvc
                .perform(get(URL).param("cpf", "00000000000"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"1234567890", "123456789012", "not email", "123@.com", ".com@.br"})
    @DisplayName("should not find a manager with invalid email or cpf")
    void return400_findManagerByInvalidEmailOrCpf(String invalidValue) throws Exception {
        this.repository.save(Manager.create(CREATE_MANAGER));

        this.mockMvc
                .perform(get(URL).param("email", invalidValue))
                .andDo(print())
                .andExpect(status().isBadRequest());

        this.mockMvc
                .perform(get(URL).param("cpf", invalidValue))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should find a manager by email and cpf")
    void return200_findManagerByEmailAndCpf() throws Exception {
        var saved = this.repository.save(Manager.create(CREATE_MANAGER));
        var json = objectMapper.writeValueAsString(ManagerResponse.of(saved));

        var emailResult = this.mockMvc
                .perform(get(URL).param("email", CREATE_MANAGER.email()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(emailResult.getResponse().getContentAsString(), json);

        var cpfResult = this.mockMvc
                .perform(get(URL).param("cpf", CREATE_MANAGER.cpf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(cpfResult.getResponse().getContentAsString(), json);
    }

    @Test
    @DisplayName("should find all managers or return empty list")
    void return200_findAllManagers() throws Exception {
        var emptyResult = this.mockMvc
                .perform(get(URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("[]", emptyResult.getResponse().getContentAsString());

        for (int i = 0; i < 3; i++) {
            var entity = Manager.create(CREATE_MANAGER);
            entity.setCpf("1234567890" + i);
            entity.setEmail("manager" + i + "@email.com");

            this.repository.save(entity);
        }

        var result = this.mockMvc
                .perform(get(URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertNotEquals(result.getResponse().getContentAsString(), "[]");
    }
}