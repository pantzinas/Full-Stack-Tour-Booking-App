package gr.aueb.cf.tourapp.core.filters;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Setter
@NoArgsConstructor
public abstract class GenericFilters {
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String DEFAULT_SORT_COLUMN = "id";
    private static final Sort.Direction DEFAULT_DIRECTION = Sort.Direction.ASC;

    private int page;
    private int pageSize;
    private Sort.Direction sortDirection;
    private String sortBy;

    public int getPage() {
        return Math.max(page, 0);
    }

    public int getPageSize() {
        return pageSize < 0 ? DEFAULT_PAGE_SIZE : pageSize;
    }

    public Sort.Direction getSortDirection() {
        return sortDirection == null ? DEFAULT_DIRECTION : sortDirection;
    }

    public String getSortBy() {
        return (sortBy == null || sortBy.isBlank()) ? DEFAULT_SORT_COLUMN : sortBy;
    }

    public Sort getSort() {
        return Sort.by(getSortDirection(), getSortBy());
    }

    public Pageable getPageable() {
        return PageRequest.of(getPage(), getPageSize(), getSort());
    }
}
