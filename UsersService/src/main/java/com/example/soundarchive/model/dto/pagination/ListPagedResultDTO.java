package com.example.soundarchive.model.dto.pagination;

import com.google.gson.Gson;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ListPagedResultDTO<T> {

    private int total;

    private List<T> list;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
