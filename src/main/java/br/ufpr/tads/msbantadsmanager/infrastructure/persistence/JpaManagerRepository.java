package br.ufpr.tads.msbantadsmanager.infrastructure.persistence;

import br.ufpr.tads.msbantadsmanager.core.domain.model.Manager;
import br.ufpr.tads.msbantadsmanager.core.port.out.ManagerRepository;
import br.ufpr.tads.msbantadsmanager.infrastructure.persistence.entity.ManagerEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaManagerRepository implements ManagerRepository {
  private final ManagerEntityRepository entityRepository;

  public JpaManagerRepository(ManagerEntityRepository entityRepository) {
    this.entityRepository = entityRepository;
  }

  @Override
  public Optional<Manager> findManagerByEmail(String email) {
    Assert.hasLength(email, "Email is required");
    return entityRepository.findManagerByEmail(email).map(ManagerEntity::getDomainModel);
  }

  @Override
  public Optional<Manager> findManagerByCpf(String cpf) {
    Assert.notNull(cpf, "CPF is required");
    return entityRepository.findManagerByCpf(cpf).map(ManagerEntity::getDomainModel);
  }

  @Override
  public long countAllByEmailOrCpf(String email, String cpf) {
    Assert.hasLength(email, "Email is required");
    Assert.notNull(cpf, "CPF is required");
    return entityRepository.countAllByEmailOrCpf(email, cpf);
  }

  @Override
  public long totalManagers() {
    return entityRepository.count();
  }

  @Override
  public Optional<Manager> findFirstByOrderByAccountsAsc() {
    return entityRepository.findFirstByOrderByAccountsAsc().map(ManagerEntity::getDomainModel);
  }

  @Override
  public Optional<Manager> findFirstByOrderByAccountsDesc() {
    return entityRepository.findFirstByOrderByAccountsDesc().map(ManagerEntity::getDomainModel);
  }

  @Override
  public Optional<Manager> findById(UUID uuid) {
    Assert.notNull(uuid, "uuid must not be null");
    return entityRepository.findById(uuid).map(ManagerEntity::getDomainModel);
  }

  @Override
  public List<Manager> findAll() {
    return entityRepository.findAll().stream().map(ManagerEntity::getDomainModel).toList();
  }

  @Override
  public Manager create(Manager manager) {
    Assert.notNull(manager, "Model must not be null");
    return entityRepository.save(new ManagerEntity(manager)).getDomainModel();
  }

  @Override
  public Manager update(Manager manager) {
    Assert.notNull(manager, "Model must not be null");
    Assert.notNull(manager.getId(), "Id must not be null");
    return entityRepository.save(new ManagerEntity(manager)).getDomainModel();
  }

  @Override
  public void delete(UUID uuid) {
    Assert.notNull(uuid, "uuid must not be null");
    entityRepository.deleteById(uuid);
  }
}
