package gr.aueb.cf.tourapp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourInsertDTO {

    @NotEmpty(message = "Tour category cannot be empty")
    private String category;

    @NotNull(message = "Price cannot be null")
    private Double price;
}
