package br.ufpr.tads.msbantadsmanager.api.rest.dto;

import br.ufpr.tads.msbantadsmanager.core.domain.model.Manager;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import java.util.function.Predicate;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public record UpdateManagerRequest(
    String name,
    @Email String email,
    @Length(min = 11, max = 11)
        @Pattern(regexp = "^\\d{11}$", message = "field must be made of numbers")
        String phoneNumber) {
  private static final Predicate<UpdateManagerRequest> validate =
      request ->
          StringUtils.hasLength(request.name)
              || StringUtils.hasLength(request.email)
              || StringUtils.hasLength(request.phoneNumber);

  public Manager updateModel(Manager model) {
    Assert.isTrue(validate.test(this), "Request must be valid");
    Assert.notNull(model, "manager model must not be null");

    if (StringUtils.hasLength(name)) {
      model.setName(name);
    }

    if (StringUtils.hasLength(email)) {
      model.setEmail(email);
    }

    if (StringUtils.hasLength(phoneNumber)) {
      model.setPhoneNumber(phoneNumber);
    }

    return model;
  }
}
