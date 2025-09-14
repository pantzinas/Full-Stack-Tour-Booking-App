package gr.aueb.cf.tourapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponseDTO {

    private String firstname;
    private String lastname;
    private String token;
}
