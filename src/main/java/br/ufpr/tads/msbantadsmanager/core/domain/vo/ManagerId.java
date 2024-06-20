package br.ufpr.tads.msbantadsmanager.core.domain.vo;

import org.springframework.util.Assert;

import java.util.UUID;

public record ManagerId(UUID id) {
  public ManagerId {
    Assert.notNull(id, "id cannot be null");
  }

  public ManagerId() {
    this(UUID.randomUUID());
  }
}
