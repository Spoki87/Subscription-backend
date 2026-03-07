package com.pawlak.subscription.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class PaginationMetadata {
    private final long totalElements;
    private final int totalPages;
    private final int currentPage;
    private final int pageSize;

    public static PaginationMetadata from(Page<?> page) {
        return new PaginationMetadata(
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }
}

