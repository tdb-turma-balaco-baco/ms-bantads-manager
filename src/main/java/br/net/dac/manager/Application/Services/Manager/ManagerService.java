package br.net.dac.manager.Application.Services.Manager;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.net.dac.manager.Application.Abstractions.IMessageSender;
import br.net.dac.manager.Application.Services.Manager.Events.ChangeManagerClients;
import br.net.dac.manager.Application.Services.Manager.Events.RemoveManagerEvent;
import br.net.dac.manager.Application.Services.Manager.Events.SaveManagerEvent;
import br.net.dac.manager.Application.Services.Manager.Events.SelectMaxClientEvent;
import br.net.dac.manager.Application.Services.Manager.Events.SelectMinClientEvent;
import br.net.dac.manager.Application.Services.Manager.Events.UpdateManagerEvent;
import br.net.dac.manager.Application.Services.Manager.GetAllManagers.ManagerResult;
import br.net.dac.manager.Domain.Entities.Manager;
import br.net.dac.manager.Domain.Events.ChangedManagerClientsEvent;
import br.net.dac.manager.Domain.Events.CreatedManagerEvent;
import br.net.dac.manager.Domain.Events.SelectedManagerEvent;
import br.net.dac.manager.Domain.Events.SelectedTopEvent;
import br.net.dac.manager.Domain.Events.UpdatedManagerEvent;
import br.net.dac.manager.Infrastructure.Persistence.Repositories.ManagerRepository;

@Service
public class ManagerService implements IManagerService {

    @Autowired
    ManagerRepository _managerRepository;

    @Autowired
    IMessageSender _messageSender;

    @Override
    public void saveManager(SaveManagerEvent event) {
        boolean existManager = _managerRepository.findOneByCpf(event.getCpf()) != null;

        if(!existManager){
            Manager manager = new Manager(event.getName(),
                                event.getEmail(), 
                                event.getCpf(), 
                                event.getPhone(), 
                                0);

            _managerRepository.saveAndFlush(manager);

            CreatedManagerEvent eventDomain = new CreatedManagerEvent(event.getName(), event.getEmail(), event.getCpf());
            _messageSender.sendMessage(eventDomain);
        }
    }

    @Override
    public void updateManager(UpdateManagerEvent event) {
        Manager manager = _managerRepository.findById(event.getManagerId()).get();

        manager.setEmail(event.getEmail());
        manager.setName(event.getName());
        manager.setPhone(event.getPhone());

        _managerRepository.saveAndFlush(manager);      

        if(manager.getTotalAccounts() > 0)
        {
            UpdatedManagerEvent eventDomain = new UpdatedManagerEvent(event.getName(), event.getCpf());
            _messageSender.sendMessage(eventDomain);
        }
        
    }

    @Override
    public void removeManager(RemoveManagerEvent event) {
        List<Manager> managers = _managerRepository.findAll();
        if(managers.size() > 1){
            _managerRepository.deleteById(event.getManagerId());        
        } 
    }

    @Override
    public List<ManagerResult> getAllManagers() {
        List<Manager> managers = _managerRepository.findAll();
        List<ManagerResult> managerResults = managers.stream().map(m -> new ManagerResult(m.getId(), m.getName(), m.getEmail(), m.getCpf(), m.getPhone())).collect(Collectors.toList());
        return managerResults;
    }

    @Override
    public void selectManagerMinClient(SelectMinClientEvent event) {
        Manager manager = _managerRepository.findTop1ByOrderByTotalAccountsAsc();

        manager.setTotalAccounts(manager.getTotalAccounts() + 1);

        _managerRepository.saveAndFlush(manager);

        SelectedManagerEvent eventDomain = new SelectedManagerEvent(event.getEmail(),
         event.getName(), event.getCpf(), event.getWage(), manager.getName(), manager.getCpf());

         _messageSender.sendMessage(eventDomain);

    }

    @Override
    public void selectManagerMaxClient(SelectMaxClientEvent event) {
        Manager managerMax = _managerRepository.findTop1ByOrderByTotalAccountsDesc();
        if(managerMax != null && managerMax.getTotalAccounts() > 1)
        {
            managerMax.setTotalAccounts(managerMax.getTotalAccounts() - 1);
    
            Manager managerMin = _managerRepository.findTop1ByOrderByTotalAccountsAsc();
            managerMin.setTotalAccounts(managerMin.getTotalAccounts() + 1);
    
            _managerRepository.save(managerMax);
            _managerRepository.save(managerMin);
            _managerRepository.flush();

            SelectedTopEvent eventDomain = new SelectedTopEvent(managerMin.getName(), managerMin.getCpf(), managerMax.getCpf(), false);
            _messageSender.sendMessage(eventDomain);
        }
    }

    @Override
    public void changeManagerClients(ChangeManagerClients event) {
        List<Manager> managers = _managerRepository.findAll();
        if(managers.size() > 1)
        {
            Manager currentManager = _managerRepository.findOneByCpf(event.getCpfRemover());
    
            Manager manager = _managerRepository.findTop1ByCpfNotOrderByTotalAccountsAsc(event.getCpfRemover());
    
            manager.setTotalAccounts(manager.getTotalAccounts() + currentManager.getTotalAccounts());
            currentManager.setTotalAccounts(0);
    
            _managerRepository.save(currentManager);
            _managerRepository.save(manager);
            _managerRepository.flush();
            
    
            ChangedManagerClientsEvent eventDomain = new ChangedManagerClientsEvent(manager.getName(), manager.getCpf(), currentManager.getCpf(), true);
            _messageSender.sendMessage(eventDomain);
        } 

    }
    
}
