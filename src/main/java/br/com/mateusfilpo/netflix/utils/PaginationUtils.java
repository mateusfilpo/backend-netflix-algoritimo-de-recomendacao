package br.com.mateusfilpo.netflix.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PaginationUtils {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;

    public static PageRequest buildPageRequest(Integer pageNumber, Integer pageSize, String sortBy) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if (pageSize == null || pageSize < 1) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }

        Sort sort = Sort.by(Sort.Order.asc(sortBy));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }
}
