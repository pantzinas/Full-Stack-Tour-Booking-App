package gr.aueb.cf.tourapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReadOnlyDTO {
    private String firstname;
    private String lastname;
    private String vat;
}
