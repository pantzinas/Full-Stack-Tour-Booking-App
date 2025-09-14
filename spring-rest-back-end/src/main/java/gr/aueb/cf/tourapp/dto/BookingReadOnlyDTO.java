package gr.aueb.cf.tourapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingReadOnlyDTO {
    private Long id;
    private LocalDate bookingDate;
    private GuideReadOnlyDTO guideReadOnlyDTO;
    private TourReadOnlyDTO tourReadOnlyDTO;
    private CustomerReadOnlyDTO customerReadOnlyDTO;
}
