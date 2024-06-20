package br.ufpr.tads.msbantadsmanager.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.ufpr.tads.msbantadsmanager.api.rest.ManagerController;
import br.ufpr.tads.msbantadsmanager.api.rest.dto.CreateManagerRequest;
import br.ufpr.tads.msbantadsmanager.api.rest.dto.ManagerResponse;
import br.ufpr.tads.msbantadsmanager.core.domain.model.Manager;
import br.ufpr.tads.msbantadsmanager.core.domain.vo.CPF;
import br.ufpr.tads.msbantadsmanager.infrastructure.persistence.ManagerEntityRepository;
import br.ufpr.tads.msbantadsmanager.infrastructure.persistence.entity.ManagerEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class ManagerEntityControllerIntegrationTest {
  @ServiceConnection @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

  private static final String URL = ManagerController.URL;
  public static final CreateManagerRequest CREATE_MANAGER_REQUEST =
      new CreateManagerRequest("fullName", "manager@email.com", "12312312311", "12345678901");
  public static final ManagerEntity MANAGER_ENTITY =
      new ManagerEntity(
          Manager.create(new CPF("12312312311"), "manager@email.com", "fullName", "12345678901"));

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private ManagerEntityRepository repository;

  @BeforeEach
  void setUp() {
    this.repository.deleteAll();
  }

  @ParameterizedTest
  @NullAndEmptySource
  @DisplayName("should not create a new manager without name")
  void return400_createWithoutFirstName(String emptyFirstName) throws Exception {
    var request =
        new CreateManagerRequest(emptyFirstName, "manager@email.com", "12312312311", "11999999999");
    var json = objectMapper.writeValueAsString(request);

    this.mockMvc
        .perform(post(URL).content(json).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());

    assertEquals(0, this.repository.count());
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"123", "not numbers", "1234567890", "123456789012"})
  @DisplayName("should not create a new manager with invalid cpf")
  void return400_createWithInvalidCpf(String invalidCpf) throws Exception {
    var request = new CreateManagerRequest("name", "manager@email.com", invalidCpf, "11999999999");
    var json = objectMapper.writeValueAsString(request);

    this.mockMvc
        .perform(post(URL).content(json).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());

    assertEquals(0, this.repository.count());
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"123", "not email", "123@.com", ".com@.br"})
  @DisplayName("should not create a new manager with invalid email")
  void return400_createWithInvalidEmail(String invalidEmail) throws Exception {
    var request = new CreateManagerRequest("name", invalidEmail, "12345678901", "11999999999");
    var json = objectMapper.writeValueAsString(request);

    this.mockMvc
        .perform(post(URL).content(json).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());

    assertEquals(0, this.repository.count());
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"123", "123456789012", "not numbers"})
  @DisplayName("should not create a new manager with invalid phoneNumber")
  void return400_createWithInvalidPhoneNumber(String invalidPhone) throws Exception {
    var request =
        new CreateManagerRequest("name", "manager@email.com", "12345678901", invalidPhone);
    var json = objectMapper.writeValueAsString(request);

    this.mockMvc
        .perform(post(URL).content(json).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());

    assertEquals(0, this.repository.count());
  }

  @ParameterizedTest
  @CsvSource(
      value = {
        "manager@email.com,12312312311,manager@email.com,12312312311",
        "manager@email.com,12312312311,manager@email.com,99999999999",
        "manager@email.com,12312312311,different@email.com,12312312311",
      })
  @DisplayName("should not create a duplicate manager")
  void return409_createDuplicate(String createEmail, String createCpf, String email, String cpf)
      throws Exception {
    var request = new CreateManagerRequest("name", createEmail, createCpf, "12345678901");
    var json = objectMapper.writeValueAsString(request);

    repository.save(
        new ManagerEntity(Manager.create(new CPF(cpf), email, "anotherName", "12345678901")));

    this.mockMvc
        .perform(post(URL).content(json).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isConflict());

    assertEquals(1, this.repository.count());
  }

  @Test
  @DisplayName("should create a new manager successfully")
  void return201_createSuccessfully() throws Exception {
    var json = objectMapper.writeValueAsString(CREATE_MANAGER_REQUEST);

    var result =
        mockMvc
            .perform(post(URL).content(json).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn();

    assertEquals(1, this.repository.count());

    var saved =
        repository
            .findManagerByEmail(CREATE_MANAGER_REQUEST.email())
            .orElseThrow(AssertionError::new);
    assertEquals(result.getResponse().getHeader("Location"), URL + "/" + saved.getId());
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(longs = {0L, -1L, -999L})
  @DisplayName("should not find a manager with invalid id")
  void return400_findManagerByInvalidId(Long invalidId) throws Exception {
    repository.save(MANAGER_ENTITY);

    mockMvc.perform(get(URL + "/" + invalidId)).andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("should not find a manager with nonexistent id")
  void return404_findManagerByNonExistentId() throws Exception {
    repository.save(MANAGER_ENTITY);

    UUID newRandomUUID = UUID.randomUUID();
    mockMvc
        .perform(get(URL + "/" + newRandomUUID))
        .andDo(print())
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("should find a manager by id")
  void return200_findManagerById() throws Exception {
    var saved = repository.save(MANAGER_ENTITY);
    var json = objectMapper.writeValueAsString(new ManagerResponse(saved.getDomainModel()));

    var result =
        mockMvc
            .perform(get(URL + "/" + saved.getId()))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    assertEquals(result.getResponse().getContentAsString(), json);
  }

  @Test
  @DisplayName("should not find a manager with nonexistent email and cpf")
  void return404_findManagerByNonExistentEmailOrCpf() throws Exception {
    repository.save(MANAGER_ENTITY);

    mockMvc
        .perform(get(URL).param("email", "another@email.com"))
        .andDo(print())
        .andExpect(status().isNoContent());

    mockMvc
        .perform(get(URL).param("cpf", "12345678900"))
        .andDo(print())
        .andExpect(status().isNoContent());
  }

  @ParameterizedTest
  @EmptySource
  @ValueSource(strings = {"1234567890", "123456789012", "not email", "123@.com", ".com@.br"})
  @DisplayName("should not find a manager with invalid email or cpf")
  void return400_findManagerByInvalidEmailOrCpf(String invalidValue) throws Exception {
    repository.save(MANAGER_ENTITY);

    mockMvc
        .perform(get(URL).param("email", invalidValue))
        .andDo(print())
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(get(URL).param("cpf", invalidValue))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("should find a manager by email and cpf")
  void return200_findManagerByEmailAndCpf() throws Exception {
    var saved = repository.save(MANAGER_ENTITY);
    var json = objectMapper.writeValueAsString(new ManagerResponse(saved.getDomainModel()));

    var emailResult =
        mockMvc
            .perform(get(URL).param("email", CREATE_MANAGER_REQUEST.email()))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    assertEquals(emailResult.getResponse().getContentAsString(), json);

    var cpfResult =
        mockMvc
            .perform(get(URL).param("cpf", CREATE_MANAGER_REQUEST.cpf()))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    assertEquals(cpfResult.getResponse().getContentAsString(), json);
  }

  @Test
  @DisplayName("should find all managers or return empty list")
  void return200_findAllManagers() throws Exception {
    var emptyResult =
        mockMvc.perform(get(URL)).andDo(print()).andExpect(status().isOk()).andReturn();
    assertEquals("[]", emptyResult.getResponse().getContentAsString());

    for (int i = 0; i < 3; i++) {
      var entity =
          new ManagerEntity(
              Manager.create(
                  new CPF("1234567890" + i),
                  "manager" + i + "@email.com",
                  "randomName",
                  "12345678901"));

      repository.save(entity);
    }

    var result = mockMvc.perform(get(URL)).andDo(print()).andExpect(status().isOk()).andReturn();
    assertNotEquals(result.getResponse().getContentAsString(), "[]");
  }
}
