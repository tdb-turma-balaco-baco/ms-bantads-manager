package br.ufpr.tads.manager.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class BaseSelectManager extends DomainEvent {
    private String managerName;
    private String managerCpf;
    private String oldManagerCpf;
    private boolean swapAll;
}
