package br.ufpr.tads.manager.events;

public record UpdateManagerEvent(Long managerId, String name, String email, String cpf, String phone) {
}
