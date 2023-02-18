package br.net.dac.manager.Domain.Events;

import br.net.dac.manager.Domain.Events.Common.BaseSelectManager;

public class ChangedManagerClientsEvent extends BaseSelectManager {

    public ChangedManagerClientsEvent(String managerName, String managerCpf, String oldManagerCpf, boolean swapAll) {
        super(managerName, managerCpf, oldManagerCpf, swapAll);
    }
    
}
