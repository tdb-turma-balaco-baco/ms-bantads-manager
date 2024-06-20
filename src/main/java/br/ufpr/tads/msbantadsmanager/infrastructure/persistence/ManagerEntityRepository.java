package br.ufpr.tads.msbantadsmanager.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import br.ufpr.tads.msbantadsmanager.infrastructure.persistence.entity.ManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface ManagerEntityRepository extends JpaRepository<ManagerEntity, UUID> {
  Optional<ManagerEntity> findManagerByEmail(String email);

  Optional<ManagerEntity> findManagerByCpf(String cpf);

  long countAllByEmailOrCpf(String email, String cpf);

  Optional<ManagerEntity> findFirstByOrderByAccountsAsc();

  Optional<ManagerEntity> findFirstByOrderByAccountsDesc();
}
