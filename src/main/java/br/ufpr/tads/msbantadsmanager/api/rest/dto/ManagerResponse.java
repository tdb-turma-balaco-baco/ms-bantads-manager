package br.ufpr.tads.msbantadsmanager.api.rest.dto;

import br.ufpr.tads.msbantadsmanager.core.domain.model.Manager;
import java.util.UUID;

public record ManagerResponse(UUID id, String name, String email, String cpf, String phoneNumber) {
  public ManagerResponse(Manager manager) {
    this(
        manager.getId().id(),
        manager.getName(),
        manager.getEmail(),
        manager.getCpf().value(),
        manager.getPhoneNumber());
  }
}
