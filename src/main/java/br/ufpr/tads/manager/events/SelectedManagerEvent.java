package br.ufpr.tads.manager.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SelectedManagerEvent extends DomainEvent {
    private String email;
    private String name;
    private String cpf;
    private Double wage;
    private String managerName;
    private String managerCpf;
}
