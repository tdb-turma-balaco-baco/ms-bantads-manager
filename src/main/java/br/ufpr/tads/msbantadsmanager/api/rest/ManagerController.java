package br.ufpr.tads.msbantadsmanager.api.rest;

import br.ufpr.tads.msbantadsmanager.api.rest.dto.ManagerResponse;
import br.ufpr.tads.msbantadsmanager.core.domain.model.Manager;
import br.ufpr.tads.msbantadsmanager.core.domain.vo.CPF;
import br.ufpr.tads.msbantadsmanager.core.domain.vo.ManagerId;
import br.ufpr.tads.msbantadsmanager.api.rest.dto.CreateManagerRequest;
import br.ufpr.tads.msbantadsmanager.api.rest.dto.UpdateManagerRequest;
import br.ufpr.tads.msbantadsmanager.core.port.in.ManagerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(ManagerController.URL)
public class ManagerController {
  public static final String URL = "/api/manager";

  private final Logger log = LoggerFactory.getLogger(ManagerController.class);
  private final ManagerService service;

  public ManagerController(ManagerService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<?> findManager(
      @RequestParam(required = false) @Valid @Email @Length(min = 6) String email,
      @RequestParam(required = false) @Valid @org.hibernate.validator.constraints.br.CPF
          String cpf) {
    log.debug("[request] findManager(email: {} , cpf: {})", email, cpf);

    if (cpf != null) {
      log.debug("[request] findManagerByCpf '{}'", cpf);
      return buildFindManagerByAttributeResponse(service.findManagerByCpf(new CPF(cpf)));
    }

    if (email != null) {
      log.debug("[request] findManagerByEmail '{}'", email);
      return buildFindManagerByAttributeResponse(service.findManagerByEmail(email));
    }

    List<ManagerResponse> managers = service.findAll().stream().map(ManagerResponse::new).toList();
    return ResponseEntity.ok(managers);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ManagerResponse> findManagerById(@PathVariable @Valid @NotNull UUID id) {
    log.debug("[request] findManagerById '{}'", id);
    return buildFindManagerByAttributeResponse(service.findById(new ManagerId(id)));
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody @Valid @NotNull CreateManagerRequest request) {
    log.debug("[request] create {}", request);
    var managerId = service.create(request.toModel()).getId();
    var createdManagerURI = URI.create(URL + "/" + managerId.id());

    return ResponseEntity.created(createdManagerURI).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(
      @PathVariable @Valid @NotNull UUID id,
      @RequestBody @Valid @NotNull UpdateManagerRequest request) {
    log.debug("[request] update {}", request);

    var manager =
        service
            .findById(new ManagerId(id))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY));

    service.update(request.updateModel(manager));
    return ResponseEntity.noContent().build();
  }

  private ResponseEntity<ManagerResponse> buildFindManagerByAttributeResponse(
      Optional<Manager> managerOptional) {
    return managerOptional
        .map(manager -> ResponseEntity.ok(new ManagerResponse(manager)))
        .orElseGet(() -> ResponseEntity.noContent().build());
  }
}
