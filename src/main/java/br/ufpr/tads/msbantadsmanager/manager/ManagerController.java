package br.ufpr.tads.msbantadsmanager.manager;

import br.ufpr.tads.msbantadsmanager.manager.port.in.CreateManager;
import br.ufpr.tads.msbantadsmanager.manager.port.in.UpdateManager;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
      @RequestParam(required = false) @Length(min = 11, max = 11) String cpf) {
    log.debug("[request] findManager(email: {} , cpf: {})", email, cpf);

    if (cpf != null) {
      log.debug("[request] findManagerByCpf '{}'", cpf);
      return ResponseEntity.ok(this.service.findManagerByCpf(cpf));
    }

    if (email != null) {
      log.debug("[request] findManagerByEmail '{}'", email);
      return ResponseEntity.ok(this.service.findManagerByEmail(email));
    }

    return ResponseEntity.ok(this.service.findAllManagers());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findManagerById(@PathVariable @Valid @NotNull @Positive Long id) {
    log.debug("[request] findManagerById '{}'", id);
    return ResponseEntity.ok(this.service.findManagerById(id));
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody @Valid @NotNull CreateManager request) {
    log.debug("[request] create {}", request);
    Long id = this.service.create(request);
    return ResponseEntity.created(URI.create(URL + "/" + id)).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(
      @RequestBody @Valid @NotNull UpdateManager request,
      @PathVariable @Valid @NotNull @Positive Long id) {
    log.debug("[request] update {}", request);
    this.service.update(id, request);
    return ResponseEntity.noContent().build();
  }
}
