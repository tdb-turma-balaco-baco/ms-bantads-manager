package br.ufpr.tads.msbantadsmanager.infrastructure.persistence.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.domain.Persistable;

@MappedSuperclass
public abstract class BaseEntity<U, ID> implements Persistable<ID> {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private ID id;

  abstract U getDomainModel();

  @Override
  public ID getId() {
    return id;
  }

  @Override
  public boolean isNew() {
    return id == null;
  }
}
