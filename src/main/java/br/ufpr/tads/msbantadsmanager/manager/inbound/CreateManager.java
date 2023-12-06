package br.ufpr.tads.msbantadsmanager.manager.inbound;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record CreateManager(@NotEmpty String firstName,
                            @NotEmpty String lastName,
                            @NotEmpty @Email String email,
                            @NotEmpty
                            @Length(min = 11, max = 11)
                            @Pattern(regexp = "^\\d{11}$", message = "field must be made of numbers")
                            String cpf,
                            @NotEmpty
                            @Length(min = 11, max = 11)
                            @Pattern(regexp = "^\\d{11}$", message = "field must be made of numbers")
                            String phone,
                            @NotEmpty String createdBy) {
}
