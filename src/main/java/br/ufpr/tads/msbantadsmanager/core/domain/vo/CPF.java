package br.ufpr.tads.msbantadsmanager.core.domain.vo;

import org.springframework.util.Assert;

public record CPF(String value) {
  public CPF {
    Assert.hasLength(value, "cpf must not be empty");
    Assert.isTrue(value.length() == 11, "cpf must contain 10 digits");
    Assert.doesNotContain(value, ".", "cpf must not contain dots");
    Assert.doesNotContain(value, "-", "cpf must not contain special characters");
  }
}
