package br.ufpr.tads.msbantadsmanager.core.port.in;

import br.ufpr.tads.msbantadsmanager.core.domain.model.Manager;
import br.ufpr.tads.msbantadsmanager.core.domain.vo.CPF;
import br.ufpr.tads.msbantadsmanager.core.domain.vo.ManagerId;
import br.ufpr.tads.msbantadsmanager.core.port.CrudOperations;
import java.util.Optional;

public interface ManagerService extends CrudOperations<Manager, ManagerId> {
  Optional<Manager> findManagerByEmail(String email);

  Optional<Manager> findManagerByCpf(CPF cpf);

  Optional<Manager> findManagerWithLeastClients();

  Optional<Manager> findManagerWithMostClients();
}
