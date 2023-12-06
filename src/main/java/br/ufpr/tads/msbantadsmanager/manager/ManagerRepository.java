package br.ufpr.tads.msbantadsmanager.manager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Optional<Manager> findManagerByEmail(@NonNull String email);
    Optional<Manager> findManagerByCpf(@NonNull String cpf);
    int countAllByEmailOrCpf(String email, String cpf);
}
