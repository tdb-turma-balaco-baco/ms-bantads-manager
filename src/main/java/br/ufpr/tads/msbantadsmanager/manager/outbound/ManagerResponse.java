package br.ufpr.tads.msbantadsmanager.manager.outbound;

import br.ufpr.tads.msbantadsmanager.manager.Manager;
import jakarta.validation.constraints.NotNull;

public record ManagerResponse(Long managerId,
                              String firstName,
                              String lastName,
                              String email,
                              String cpf,
                              String phone) {
    public static ManagerResponse of(@NotNull Manager manager) {
        return new ManagerResponse(
                manager.getId(),
                manager.getFirstName(),
                manager.getLastName(),
                manager.getEmail(),
                manager.getCpf(),
                manager.getPhone()
        );
    }
}
