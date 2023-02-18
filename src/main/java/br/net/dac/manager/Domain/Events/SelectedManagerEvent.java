package br.net.dac.manager.Domain.Events;

import br.net.dac.manager.Domain.Events.Common.DomainEvent;

public class SelectedManagerEvent extends DomainEvent {
    private String email;
    private String name;
    private String cpf;
    private Double wage;
    private String managerName;
    private String managerCpf;
    
    public SelectedManagerEvent(String email, String name, String cpf, Double wage, String managerName,
            String managerCpf) {
        this.email = email;
        this.name = name;
        this.cpf = cpf;
        this.wage = wage;
        this.managerName = managerName;
        this.managerCpf = managerCpf;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
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
    public Double getWage() {
        return wage;
    }
    public void setWage(Double wage) {
        this.wage = wage;
    }
    public String getManagerName() {
        return managerName;
    }
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
    public String getManagerCpf() {
        return managerCpf;
    }
    public void setManagerCpf(String managerCpf) {
        this.managerCpf = managerCpf;
    }

    
}
