package gr.aueb.cf.tourapp.dto;

import gr.aueb.cf.tourapp.core.enums.GenderType;
import gr.aueb.cf.tourapp.core.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInsertDTO {

    @Email(message = "Invalid Username")
    private String username;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$", message = "Invalid Password")
    private String password;

    @NotEmpty(message = "Firstname cannot be empty")
    private String firstname;

    @NotEmpty(message = "Lastname cannot be empty")
    private String lastname;

    @Email(message = "Invalid Email")
    private String email;

    @NotEmpty(message = "VAT number cannot be empty")
    @Pattern(regexp = "\\d{9}")
    private String vat;

    @NotNull(message = "Date of birth cannot be null")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender type cannot be null")
    private GenderType genderType;

    @NotEmpty(message = "Nationality cannot be empty")
    private String nationality;

    @NotNull(message = "Role cannot be null")
    private Role role;

    @NotNull(message = "Is active cannot be null")
    private Boolean isActive;
}
