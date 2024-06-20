package br.ufpr.tads.msbantadsmanager.api.rest.dto;

import br.ufpr.tads.msbantadsmanager.core.domain.model.Manager;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

public record CreateManagerRequest(
    @NotEmpty String name,
    @NotEmpty @Email String email,
    @NotEmpty @CPF String cpf,
    @NotEmpty
        @Length(min = 11, max = 11)
        @Pattern(regexp = "^\\d{11}$", message = "field must be made of numbers")
        String phoneNumber) {

  public Manager toModel() {
    return Manager.create(
        new br.ufpr.tads.msbantadsmanager.core.domain.vo.CPF(this.cpf()),
        this.email(),
        this.name(),
        this.phoneNumber());
  }
}
