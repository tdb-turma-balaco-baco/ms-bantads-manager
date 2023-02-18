package br.net.dac.manager.Application.Services.Manager.Events;

public class SelectMaxClientEvent {
    private String cpf;
    
    public SelectMaxClientEvent(String cpf) {
        this.cpf = cpf;
    }

    public SelectMaxClientEvent() {
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    
}
