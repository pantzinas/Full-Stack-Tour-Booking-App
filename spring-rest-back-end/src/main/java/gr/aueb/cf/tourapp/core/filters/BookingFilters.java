package gr.aueb.cf.tourapp.core.filters;

import jakarta.annotation.Nullable;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BookingFilters extends GenericFilters {

    @Nullable
    private LocalDate bookingDate;

    @Nullable
    private Double price;

    @Nullable
    private String tourCategory;

    @Nullable
    private Long guideId;

    @Nullable
    private Long customerId;

    @Nullable
    private Boolean customerIsActive;
}
