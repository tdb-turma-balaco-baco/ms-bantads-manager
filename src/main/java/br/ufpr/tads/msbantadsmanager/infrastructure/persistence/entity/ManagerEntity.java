package br.ufpr.tads.msbantadsmanager.infrastructure.persistence.entity;

import br.ufpr.tads.msbantadsmanager.core.domain.vo.CPF;
import br.ufpr.tads.msbantadsmanager.core.domain.model.Manager;
import br.ufpr.tads.msbantadsmanager.core.domain.vo.ManagerId;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "managers")
public class ManagerEntity extends BaseEntity<Manager, UUID, LocalDateTime> {
  @Column(unique = true, nullable = false, length = 11)
  private String cpf;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, length = 11)
  private String phoneNumber;

  @Column private int accounts = 0;
  @Column private boolean isActive = true;

  public ManagerEntity() {}

  public ManagerEntity(Manager domainModel) {
    this.cpf = domainModel.getCpf().value();
    this.email = domainModel.getEmail();
    this.name = domainModel.getName();
    this.phoneNumber = domainModel.getPhoneNumber();
    this.accounts = domainModel.getAccounts();

    setCreatedDate(LocalDateTime.now());
    setCreatedBy("system");
  }

  @Override
  public Manager getDomainModel() {
    return new Manager(
        new ManagerId(this.getId()), new CPF(cpf), email, name, phoneNumber, accounts);
  }
}
