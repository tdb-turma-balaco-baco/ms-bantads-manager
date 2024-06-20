package br.ufpr.tads.msbantadsmanager.core.application;

import br.ufpr.tads.msbantadsmanager.core.domain.vo.CPF;
import br.ufpr.tads.msbantadsmanager.core.domain.model.Manager;
import br.ufpr.tads.msbantadsmanager.core.domain.vo.ManagerId;
import br.ufpr.tads.msbantadsmanager.core.port.in.ManagerService;
import br.ufpr.tads.msbantadsmanager.core.port.out.ManagerRepository;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class ManagerServiceImpl implements ManagerService {
  private final Logger log = LoggerFactory.getLogger(ManagerServiceImpl.class);
  private final ManagerRepository repository;

  public ManagerServiceImpl(ManagerRepository repository) {
    this.repository = repository;
  }

  @Override
  public Optional<Manager> findManagerByEmail(String email) {
    Assert.hasLength(email, "Email is required");
    return repository.findManagerByEmail(email);
  }

  @Override
  public Optional<Manager> findManagerByCpf(CPF cpf) {
    log.debug("[retrieving] find manager by cpf '{}'", cpf);
    Assert.notNull(cpf, "cpf cannot be null");
    return repository.findManagerByCpf(cpf.value());
  }

  @Override
  public Optional<Manager> findManagerWithLeastClients() {
    return repository.findFirstByOrderByAccountsAsc();
  }

  @Override
  public Optional<Manager> findManagerWithMostClients() {
    return repository.findFirstByOrderByAccountsDesc();
  }

  @Override
  public Optional<Manager> findById(ManagerId managerId) {
    Assert.notNull(managerId, "managerId cannot be null");
    return repository.findById(managerId.id());
  }

  @Override
  public List<Manager> findAll() {
    return repository.findAll();
  }

  @Override
  @Transactional
  public Manager create(Manager entity) {
    if (entity.getId() != null || managerExists(entity.getEmail(), entity.getCpf())) {
      throw new IllegalStateException("this manager already exists");
    }

    return repository.create(entity);
  }

  @Override
  @Transactional
  public Manager update(Manager model) {
    Assert.notNull(model.getId(), "id cannot be null");
    return repository.update(model);
  }

  @Override
  @Transactional
  public void delete(ManagerId managerId) {
    Assert.notNull(managerId, "managerId cannot be null");

    if (repository.totalManagers() <= 1L) {
      throw new IllegalStateException("there must be at least one manager");
    }

    repository.delete(managerId.id());
  }

  private boolean managerExists(String email, CPF cpf) {
    return repository.countAllByEmailOrCpf(email, cpf.value()) > 0L;
  }
}
