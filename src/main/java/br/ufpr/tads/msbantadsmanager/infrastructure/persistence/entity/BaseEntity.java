package br.ufpr.tads.msbantadsmanager.infrastructure.persistence.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;

import java.time.temporal.TemporalAccessor;
import java.util.Optional;

@MappedSuperclass
public abstract class BaseEntity<U, ID, T extends TemporalAccessor>
    implements Auditable<String, ID, T> {
  @Id private ID id;
  @CreatedBy private String createdBy;
  @CreatedDate private T creationDate;
  @LastModifiedBy private String lastModifiedBy;
  @LastModifiedDate private T lastModifiedDate;

  abstract U getDomainModel();

  @Override
  public ID getId() {
    return id;
  }

  @Override
  public boolean isNew() {
    return id == null;
  }

  @Override
  public void setLastModifiedDate(T lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  @Override
  public Optional<T> getLastModifiedDate() {
    return Optional.ofNullable(lastModifiedDate);
  }

  @Override
  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  @Override
  public Optional<String> getLastModifiedBy() {
    return Optional.ofNullable(lastModifiedBy);
  }

  @Override
  public void setCreatedDate(T creationDate) {
    this.creationDate = creationDate;
  }

  @Override
  public Optional<T> getCreatedDate() {
    return Optional.ofNullable(creationDate);
  }

  @Override
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  @Override
  public Optional<String> getCreatedBy() {
    return Optional.ofNullable(createdBy);
  }
}
