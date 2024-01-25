package br.ufpr.tads.manager.events;

public record SelectMinClientEvent(String email, String name, String cpf, Double wage) {
}
