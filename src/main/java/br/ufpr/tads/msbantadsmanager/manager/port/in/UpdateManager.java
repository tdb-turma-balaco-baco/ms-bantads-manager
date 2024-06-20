package br.ufpr.tads.msbantadsmanager.manager.port.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record UpdateManager(
    String firstName,
    String lastName,
    @Email String email,
    @Length(min = 11, max = 11)
        @Pattern(regexp = "^\\d{11}$", message = "field must be made of numbers")
        String phone,
    String lastModifiedBy) {
}
