package br.net.dac.manager.Application.Services.Manager;

import java.util.List;

import br.net.dac.manager.Application.Services.Manager.Events.RemoveManagerEvent;
import br.net.dac.manager.Application.Services.Manager.Events.SaveManagerEvent;
import br.net.dac.manager.Application.Services.Manager.Events.UpdateManagerEvent;
import br.net.dac.manager.Application.Services.Manager.GetAllManagers.ManagerResult;

public interface IManagerService {
    void saveManager(SaveManagerEvent event);
    void updateManager(UpdateManagerEvent event);
    void removeManager(RemoveManagerEvent event);
    List<ManagerResult> getAllManagers();
}
