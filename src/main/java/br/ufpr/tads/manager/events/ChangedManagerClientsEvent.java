package br.ufpr.tads.manager.events;

public class ChangedManagerClientsEvent extends BaseSelectManager {
    public ChangedManagerClientsEvent(String managerName, String managerCpf, String oldManagerCpf, boolean swapAll) {
        super(managerName, managerCpf, oldManagerCpf, swapAll);
    }
}
