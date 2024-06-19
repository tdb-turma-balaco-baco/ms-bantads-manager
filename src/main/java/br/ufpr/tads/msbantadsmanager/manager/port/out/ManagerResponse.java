package br.ufpr.tads.msbantadsmanager.manager.port.out;

import br.ufpr.tads.msbantadsmanager.manager.Manager;

public record ManagerResponse(Long managerId,
                              String firstName,
                              String lastName,
                              String email,
                              String cpf,
                              String phone) {
    public static ManagerResponse of(Manager manager) {
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
