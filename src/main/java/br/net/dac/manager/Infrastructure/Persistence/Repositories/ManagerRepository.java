package br.net.dac.manager.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.net.dac.manager.Domain.Entities.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    
}
