package gr.aueb.cf.tourapp.core.filters;

import jakarta.annotation.Nullable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class GuideFilters extends GenericFilters {

    @Nullable
    private String uuid;

    @Nullable
    private String userLastname;

    @Nullable
    private String userVat;

    @Nullable
    private Boolean isActive;

    @Nullable
    private String tourCategory;
}
