package br.net.dac.manager.Application.Services.Manager;

import java.util.List;

import br.net.dac.manager.Application.Services.Manager.Events.ChangeManagerClients;
import br.net.dac.manager.Application.Services.Manager.Events.RemoveManagerEvent;
import br.net.dac.manager.Application.Services.Manager.Events.SaveManagerEvent;
import br.net.dac.manager.Application.Services.Manager.Events.SelectMaxClientEvent;
import br.net.dac.manager.Application.Services.Manager.Events.SelectMinClientEvent;
import br.net.dac.manager.Application.Services.Manager.Events.UpdateManagerEvent;
import br.net.dac.manager.Application.Services.Manager.GetAllManagers.ManagerResult;

public interface IManagerService {
    void saveManager(SaveManagerEvent event);
    void updateManager(UpdateManagerEvent event);
    void removeManager(RemoveManagerEvent event);
    List<ManagerResult> getAllManagers();
    void selectManagerMinClient(SelectMinClientEvent event);
    void selectManagerMaxClient(SelectMaxClientEvent event);
    void changeManagerClients(ChangeManagerClients event);
}
