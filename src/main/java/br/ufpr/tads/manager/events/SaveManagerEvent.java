package br.ufpr.tads.manager.events;

public record SaveManagerEvent(String name, String email, String cpf, String phone) {
}
