package gr.aueb.cf.tourapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInsertDTO {

    @NotNull(message = "Is active cannot be null")
    private Boolean isActive;

    @NotNull(message = "User details cannot be null")
    private UserInsertDTO userInsertDTO;
}
