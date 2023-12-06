package br.ufpr.tads.msbantadsmanager.manager;

import br.ufpr.tads.msbantadsmanager.manager.inbound.CreateManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ManagerControllerIntegrationTest {
    private static final String URL = ManagerController.URL;

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
        this.repository.save(new Manager(saved));

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
        var request = new CreateManager(
                "firstName",
                "lastName",
                "manager@email.com",
                "12312312311",
                "12345678901",
                "admin@email.com"
        );
        var json = objectMapper.writeValueAsString(request);

        var result = this.mockMvc
                .perform(post(URL)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        assertEquals(1, this.repository.count());

        var saved = this.repository.findManagerByEmail(request.email()).orElseThrow(AssertionError::new);
        assertEquals(result.getResponse().getHeader("Location"), URL + "/" + saved.getId());
    }
}