package br.ufpr.tads.manager.events;

public class SelectedTopEvent extends BaseSelectManager {
    public SelectedTopEvent(String managerName, String managerCpf, String oldManagerCpf, boolean swapAll) {
        super(managerName, managerCpf, oldManagerCpf, swapAll);
    }
}
