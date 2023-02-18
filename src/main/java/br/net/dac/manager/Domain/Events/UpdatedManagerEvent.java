package br.net.dac.manager.Domain.Events;

public class UpdatedManagerEvent extends DomainEvent {
    private String name;
    private String cpf;

    public UpdatedManagerEvent(String name, String cpf) {
        this.name = name;
        this.cpf = cpf;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
