package br.net.dac.manager.Application.Services.Manager;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.net.dac.manager.Application.Abstractions.IMessageSender;
import br.net.dac.manager.Application.Services.Manager.Events.RemoveManagerEvent;
import br.net.dac.manager.Application.Services.Manager.Events.SaveManagerEvent;
import br.net.dac.manager.Application.Services.Manager.Events.UpdateManagerEvent;
import br.net.dac.manager.Application.Services.Manager.GetAllManagers.ManagerResult;
import br.net.dac.manager.Domain.Entities.Manager;
import br.net.dac.manager.Domain.Events.CreatedManagerEvent;
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
        Manager manager = new Manager(event.getName(),
                            event.getEmail(), 
                            event.getCpf(), 
                            event.getPhone(), 
                            0);

        _managerRepository.saveAndFlush(manager);

        CreatedManagerEvent eventDomain = new CreatedManagerEvent(event.getName(), event.getCpf());
        _messageSender.sendMessage(eventDomain);
    }

    @Override
    public void updateManager(UpdateManagerEvent event) {
        Manager manager = _managerRepository.findById(event.getManagerId()).get();

        manager.setEmail(event.getEmail());
        manager.setName(event.getName());
        manager.setPhone(event.getPhone());

        _managerRepository.saveAndFlush(manager);      
        
        UpdatedManagerEvent eventDomain = new UpdatedManagerEvent(event.getName(), event.getCpf());
        _messageSender.sendMessage(eventDomain);
        
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
    
}
