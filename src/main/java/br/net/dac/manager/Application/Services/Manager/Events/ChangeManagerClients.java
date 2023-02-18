package br.net.dac.manager.Application.Services.Manager.Events;

public class ChangeManagerClients {
    private String cpfRemover;

    public ChangeManagerClients(String cpfRemover) {
        this.cpfRemover = cpfRemover;
    }

    
    public ChangeManagerClients() {
    }


    public String getCpfRemover() {
        return cpfRemover;
    }

    public void setCpfRemover(String cpfRemover) {
        this.cpfRemover = cpfRemover;
    }

    
}


