package br.ufpr.tads.msbantadsmanager.manager;

import br.ufpr.tads.msbantadsmanager.manager.inbound.CreateManager;
import br.ufpr.tads.msbantadsmanager.manager.outbound.ManagerResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerService {
    private final ManagerRepository repository;

    public List<ManagerResponse> findAllManagers() {
        log.debug("[retrieving] findAllManagers");
        return this.repository.findAll().stream().map(ManagerResponse::of).toList();
    }

    public ManagerResponse findManagerByCpf(@Valid @NonNull @Length(min = 11, max = 11) String cpf) {
        log.debug("[retrieving] findManagerByCpf '{}'", cpf);

        var entity = this.repository
                .findManagerByCpf(cpf)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "manager not found by cpf: " + cpf)
                );

        return ManagerResponse.of(entity);
    }

    public ManagerResponse findManagerByEmail(@Valid @NonNull @Email String email) {
        log.debug("[retrieving] findManagerByEmail '{}'", email);

        var entity = this.repository
                .findManagerByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "manager not found by email: " + email)
                );

        return ManagerResponse.of(entity);
    }

    public ManagerResponse findManagerById(@Valid @NonNull @Positive Long id) {
        log.debug("[retrieving] findManagerById '{}'", id);

        var entity = this.repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "manager ' " + id + "' not found")
                );

        return ManagerResponse.of(entity);
    }

    @Transactional
    public Long create(@Valid @NonNull CreateManager createManager) {
        log.debug("[creating] {}", createManager);

        boolean managerAlreadyExists = this.repository.countAllByEmailOrCpf(createManager.email(), createManager.cpf()) > 0;
        if (managerAlreadyExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }


        var entity = new Manager(createManager);
        return this.repository.save(entity).getId();
    }

    @Transactional
    public Manager update(Manager manager) {
        log.debug("[updating] '{}': {}", manager.getId(), manager);
        return this.repository.save(manager);
    }
}
