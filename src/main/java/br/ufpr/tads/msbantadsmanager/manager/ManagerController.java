package br.ufpr.tads.msbantadsmanager.manager;

import br.ufpr.tads.msbantadsmanager.manager.port.in.CreateManager;
import br.ufpr.tads.msbantadsmanager.manager.port.out.ManagerResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(ManagerController.URL)
@RequiredArgsConstructor
public class ManagerController {
    public static final String URL = "/api/manager";

    private final ManagerService service;

    @GetMapping
    public ResponseEntity<?> findManager(@RequestParam(required = false) @Valid @Email @Length(min = 6) String email,
                                         @RequestParam(required = false) @Length(min = 11, max = 11) String cpf) {
        log.debug("[request] findManager(email: {} , cpf: {})", email, cpf);

        if (Objects.nonNull(cpf)) {
            log.debug("[request] findManagerByCpf '{}'", cpf);
            return ResponseEntity.ok(this.service.findManagerByCpf(cpf));
        }

        if (Objects.nonNull(email)) {
            log.debug("[request] findManagerByEmail '{}'", email);
            return ResponseEntity.ok(this.service.findManagerByEmail(email));
        }

        return ResponseEntity.ok(this.service.findAllManagers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagerResponse> findManagerById(@PathVariable @Valid @NonNull @Positive Long id) {
        log.debug("[request] findManagerById '{}'", id);
        return ResponseEntity.ok(this.service.findManagerById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid @NonNull CreateManager request) {
        log.debug("[request] create {}", request);
        Long id = this.service.create(request);
        return ResponseEntity.created(URI.create(URL + "/" + id)).build();
    }
}
