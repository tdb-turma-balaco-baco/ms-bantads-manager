package br.ufpr.tads.manager.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CreatedManagerEvent extends DomainEvent {
    private String name;
    private String email;
    private String cpf;
}
