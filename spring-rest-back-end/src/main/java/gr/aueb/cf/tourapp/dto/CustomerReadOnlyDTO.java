package gr.aueb.cf.tourapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerReadOnlyDTO {
    private Long id;
    private String uuid;
    private Boolean isActive;
    private UserReadOnlyDTO userReadOnlyDTO;
}
