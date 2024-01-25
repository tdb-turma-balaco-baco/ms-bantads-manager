package br.ufpr.tads.manager;

import br.ufpr.tads.manager.events.*;
import br.ufpr.tads.manager.infra.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final ManagerRepository repository;
    private final MessageSender sender;

    public void saveManager(SaveManagerEvent event) {
        boolean existManager = repository.findOneByCpf(event.cpf()) != null;

        if (!existManager) {
            Manager manager = new Manager(
                    null,
                    event.name(),
                    event.email(),
                    event.cpf(),
                    event.phone(),
                    0
            );

            repository.saveAndFlush(manager);

            CreatedManagerEvent eventDomain = new CreatedManagerEvent(event.name(), event.email(), event.cpf());
            sender.sendMessage(eventDomain);
        }
    }

    public void updateManager(UpdateManagerEvent event) {
        Manager manager = repository.findById(event.managerId()).get();

        manager.setEmail(event.email());
        manager.setName(event.name());
        manager.setPhone(event.phone());

        repository.saveAndFlush(manager);

        if (manager.getTotalAccounts() > 0) {
            UpdatedManagerEvent eventDomain = new UpdatedManagerEvent(event.name(), event.cpf());
            sender.sendMessage(eventDomain);
        }

    }

    public void removeManager(RemoveManagerEvent event) {
        List<Manager> managers = repository.findAll();
        if (managers.size() > 1) {
            repository.deleteByCpf(event.cpf());
        }
    }

    public List<ManagerResult> getAllManagers() {
        List<Manager> managers = repository.findAll();
        return managers
                .stream()
                .map(m -> new ManagerResult(m.getId(), m.getName(), m.getEmail(), m.getCpf(), m.getPhone(), m.getTotalAccounts()))
                .toList();
    }

    public void selectManagerMinClient(SelectMinClientEvent event) {
        Manager manager = repository.findTop1ByOrderByTotalAccountsAsc();

        manager.setTotalAccounts(manager.getTotalAccounts() + 1);

        repository.saveAndFlush(manager);

        SelectedManagerEvent eventDomain = new SelectedManagerEvent(event.email(),
                event.name(), event.cpf(), event.wage(), manager.getName(), manager.getCpf());

        sender.sendMessage(eventDomain);

    }

    public void selectManagerMaxClient(SelectMaxClientEvent event) {
        Manager managerMax = repository.findTop1ByOrderByTotalAccountsDesc();
        if (managerMax != null && managerMax.getTotalAccounts() > 1) {
            managerMax.setTotalAccounts(managerMax.getTotalAccounts() - 1);

            Manager managerMin = repository.findTop1ByOrderByTotalAccountsAsc();
            managerMin.setTotalAccounts(managerMin.getTotalAccounts() + 1);

            repository.save(managerMax);
            repository.save(managerMin);
            repository.flush();

            SelectedTopEvent eventDomain = new SelectedTopEvent(managerMin.getName(), managerMin.getCpf(), managerMax.getCpf(), false);
            sender.sendMessage(eventDomain);
        }
    }

    public void changeManagerClients(ChangeManagerClients event) {
        List<Manager> managers = repository.findAll();
        if (managers.size() > 1) {
            Manager currentManager = repository.findOneByCpf(event.cpfRemover());

            Manager manager = repository.findTop1ByCpfNotOrderByTotalAccountsAsc(event.cpfRemover());

            manager.setTotalAccounts(manager.getTotalAccounts() + currentManager.getTotalAccounts());
            currentManager.setTotalAccounts(0);

            repository.save(currentManager);
            repository.save(manager);
            repository.flush();

            ChangedManagerClientsEvent eventDomain = new ChangedManagerClientsEvent(manager.getName(), manager.getCpf(), currentManager.getCpf(), true);
            sender.sendMessage(eventDomain);
        }
    }
}
