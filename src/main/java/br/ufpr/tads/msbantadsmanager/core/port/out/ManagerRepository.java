package br.ufpr.tads.msbantadsmanager.core.port.out;

import br.ufpr.tads.msbantadsmanager.core.domain.model.Manager;
import br.ufpr.tads.msbantadsmanager.core.port.CrudOperations;

import java.util.Optional;
import java.util.UUID;

public interface ManagerRepository extends CrudOperations<Manager, UUID> {
  Optional<Manager> findManagerByEmail(String email);

  Optional<Manager> findManagerByCpf(String cpf);

  long countAllByEmailOrCpf(String email, String cpf);

  long totalManagers();

  Optional<Manager> findFirstByOrderByAccountsAsc();

  Optional<Manager> findFirstByOrderByAccountsDesc();
}
