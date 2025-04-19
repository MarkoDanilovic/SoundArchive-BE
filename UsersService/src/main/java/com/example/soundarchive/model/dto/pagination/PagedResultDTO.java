package com.example.soundarchive.model.dto.pagination;

import com.google.gson.Gson;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PagedResultDTO<T> {

    private Integer currentPage;

    private Integer pagesCount;

    private Integer pageSize;

    private Integer totalCount;

    private List<T> items;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
