package com.example.soundarchive.dao;

import com.example.soundarchive.model.WishlistId;
import com.example.soundarchive.model.dto.pagination.ListPagedResultDTO;
import com.example.soundarchive.model.entity.TrackEntity;
import com.example.soundarchive.model.entity.WishlistEntity;
import org.springframework.data.domain.Pageable;

public interface WishlistDAO {


    ListPagedResultDTO<TrackEntity> findByUserId(Pageable pageable, Integer userId);

    WishlistEntity save(WishlistEntity wishlistEntity);

    void delete(WishlistId wishlistId);

    void deleteByUserId(Integer userId);

    Boolean checkWishlist(Integer userId, Integer trackId);
}
