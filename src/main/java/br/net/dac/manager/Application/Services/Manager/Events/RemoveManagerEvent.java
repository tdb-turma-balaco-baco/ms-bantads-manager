package br.net.dac.manager.Application.Services.Manager.Events;

public class RemoveManagerEvent {
    public Long managerId;

    public RemoveManagerEvent() {
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }
    
}
