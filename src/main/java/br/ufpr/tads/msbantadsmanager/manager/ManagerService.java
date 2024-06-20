package br.ufpr.tads.msbantadsmanager.manager;

import br.ufpr.tads.msbantadsmanager.manager.port.in.CreateManager;
import br.ufpr.tads.msbantadsmanager.manager.port.in.UpdateManager;
import br.ufpr.tads.msbantadsmanager.manager.port.out.ManagerResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ManagerService {
  private final Logger log = LoggerFactory.getLogger(ManagerService.class);
  private final ManagerRepository repository;

  public ManagerService(ManagerRepository repository) {
    this.repository = repository;
  }

  public List<ManagerResponse> findAllManagers() {
    log.debug("[retrieving] findAllManagers");
    return this.repository.findAll().stream().map(ManagerResponse::of).toList();
  }

  public ManagerResponse findManagerByCpf(@Valid @NotNull @Length(min = 11, max = 11) String cpf) {
    log.debug("[retrieving] findManagerByCpf '{}'", cpf);

    var entity =
        this.repository
            .findManagerByCpf(cpf)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "manager not found by cpf: " + cpf));

    return ManagerResponse.of(entity);
  }

  public ManagerResponse findManagerByEmail(@Valid @NotNull @Email String email) {
    log.debug("[retrieving] findManagerByEmail '{}'", email);

    var entity =
        this.repository
            .findManagerByEmail(email)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "manager not found by email: " + email));

    return ManagerResponse.of(entity);
  }

  public ManagerResponse findManagerById(@Valid @NotNull @Positive Long id) {
    log.debug("[retrieving] findManagerById '{}'", id);

    var entity =
        this.repository
            .findById(id)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "manager ' " + id + "' not found"));

    return ManagerResponse.of(entity);
  }

  public Optional<Manager> findManagerWithLeastClients() {
    log.debug("[retrieving] retrieving manager with least clients");
    return this.repository.findFirstByOrderByAccountsAsc();
  }

  public Optional<Manager> findManagerWithMostClients() {
    log.debug("[retrieving] retrieving manager with most clients");
    return this.repository.findFirstByOrderByAccountsDesc();
  }

  @Transactional
  public Long create(@Valid @NotNull CreateManager createManager) {
    log.debug("[creating] {}", createManager);

    boolean managerAlreadyExists =
        this.repository.countAllByEmailOrCpf(createManager.email(), createManager.cpf()) > 0;

    if (managerAlreadyExists) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }

    var entity = Manager.create(createManager);
    return this.repository.save(entity).getId();
  }

  @Transactional
  public void remove(@Valid @NotNull @Positive String cpf) {
    log.debug("[removing]");

    if (this.repository.count() > 1) {
      log.warn("[removing] there are more than one manager");
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    var manager =
        this.repository
            .findManagerByCpf(cpf)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    this.repository.deleteById(manager.getId());
  }

  @Transactional
  public ManagerResponse update(@NotNull @Positive Long id, @Valid @NotNull UpdateManager dto) {
    log.debug("[updating] manager '{}': {}", id, dto);

    var manager =
        this.repository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    if (dto.firstName() != null && !dto.firstName().isEmpty()) {
      manager.setFirstName(dto.firstName());
    }

    if (dto.lastName() != null && !dto.lastName().isEmpty()) {
      manager.setLastName(dto.lastName());
    }

    if (dto.email() != null && !dto.email().isEmpty()) {
      manager.setEmail(dto.email());
    }

    if (dto.phone() != null && !dto.phone().isEmpty()) {
      manager.setPhone(dto.phone());
    }

    manager.setLastModifiedBy(dto.lastModifiedBy());
    manager.setLastModifiedDate(LocalDateTime.now());

    Manager saved = this.repository.save(manager);
    return ManagerResponse.of(saved);
  }
}
