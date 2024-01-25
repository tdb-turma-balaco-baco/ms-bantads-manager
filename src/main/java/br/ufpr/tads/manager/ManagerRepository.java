package br.ufpr.tads.manager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    
    Manager findTop1ByOrderByTotalAccountsAsc();

    Manager findTop1ByCpfNotOrderByTotalAccountsAsc(@Param("cpf") String cpf);

    Manager findTop1ByOrderByTotalAccountsDesc();

    Manager findOneByCpf(@Param("cpf") String cpf);

    void deleteByCpf(@Param("cpf") String cpf);
}
