package br.ufpr.tads.msbantadsmanager.core.domain.vo;

import java.util.UUID;
import org.springframework.util.Assert;

public record ManagerId(UUID id) {
  public ManagerId {
    Assert.notNull(id, "id cannot be null");
  }

  public ManagerId() {
    this(UUID.randomUUID());
  }
}
