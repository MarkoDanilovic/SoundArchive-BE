package com.example.soundarchive.util.pagination;

import com.example.soundarchive.util.constants.Constants;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableHelper {

    public static Pageable createPageRequest(int page, int size, String sortBy, String order) {
        Sort sort = order.equals(Constants.SORT_ORDER_ASC) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(page, size, sort);
    }

    public static Pageable createPageRequest(int page, int size) {
        return PageRequest.of(page, size);
    }

    public static Pageable createPageable(Integer page, Integer size, String sortBy, String order,
                                          int defaultPage, int defaultSize) {

        int pageNumber = (page != null && page >= 0) ? page : defaultPage;
        int pageSize = (size != null && size > 0) ? size : defaultSize;

        if(sortBy != null && order != null) {
            return createPageRequest(pageNumber, pageSize, sortBy, order);
        }

        return createPageRequest(pageNumber, pageSize);

    }
}
