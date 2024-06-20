package br.ufpr.tads.msbantadsmanager.manager;

import br.ufpr.tads.msbantadsmanager.manager.port.in.CreateManager;
import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;

@Entity
@Table(name = "managers")
public class Manager implements Serializable, Auditable<String, Long, LocalDateTime> {
  @Serial private static final long serialVersionUID = 5397338376573272269L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "managers_id_seq")
  @SequenceGenerator(name = "managers_id_seq", allocationSize = 1)
  private Long id;

  @Column(unique = true, nullable = false, length = 11)
  private String cpf;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false)
  private String phone;

  @Column private int accounts = 0;

  @Column private boolean isActive = true;

  @CreatedBy @Column private String createdBy;

  @LastModifiedBy @Column private String lastModifiedBy;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime creationDate;

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime lastModifiedDate;

  public static Manager create(CreateManager createManager) {
    var entity = new Manager();
    entity.setFirstName(createManager.firstName());
    entity.setLastName(createManager.lastName());
    entity.setCpf(createManager.cpf());
    entity.setEmail(createManager.email());
    entity.setPhone(createManager.phone());
    entity.setCreatedBy(createManager.createdBy());

    LocalDateTime now = LocalDateTime.now();
    entity.setCreatedDate(now);
    entity.setLastModifiedDate(now);

    return entity;
  }

  public Long getId() {
    return id;
  }

  @Override
  public boolean isNew() {
    return id == null;
  }

  public String getCpf() {
    return cpf;
  }

  public String getEmail() {
    return email;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getPhone() {
    return phone;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  @Override
  public Optional<String> getCreatedBy() {
    return Optional.of(createdBy);
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  @Override
  public Optional<LocalDateTime> getCreatedDate() {
    return Optional.of(creationDate);
  }

  @Override
  public void setCreatedDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }

  @Override
  public Optional<String> getLastModifiedBy() {
    return Optional.of(lastModifiedBy);
  }

  @Override
  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  @Override
  public Optional<LocalDateTime> getLastModifiedDate() {
    return Optional.of(lastModifiedDate);
  }

  @Override
  public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }
}
