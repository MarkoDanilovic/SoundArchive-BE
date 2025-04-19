package com.example.soundarchive.service;

import com.example.soundarchive.dao.impl.WishlistDAOImpl;
import com.example.soundarchive.mapper.ObjectUtilMapper;
import com.example.soundarchive.mapper.TrackMapper;
import com.example.soundarchive.model.WishlistId;
import com.example.soundarchive.model.dto.TrackDTO;
import com.example.soundarchive.model.dto.WishlistDTO;
import com.example.soundarchive.model.dto.pagination.ListPagedResultDTO;
import com.example.soundarchive.model.entity.TrackEntity;
import com.example.soundarchive.model.entity.WishlistEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

    private final WishlistDAOImpl wishlistDAO;

    private final TrackMapper trackMapper;

    private final ObjectUtilMapper mapper;

    @Autowired
    public WishlistService(WishlistDAOImpl wishlistDAO, TrackMapper trackMapper, ObjectUtilMapper mapper) {
        this.wishlistDAO = wishlistDAO;
        this.trackMapper = trackMapper;
        this.mapper = mapper;
    }

    public ListPagedResultDTO<TrackDTO> findByUserId(Pageable pageable, Integer userId) {

        ListPagedResultDTO<TrackEntity> listPagedResultReturned = wishlistDAO.findByUserId(pageable, userId);

        return new ListPagedResultDTO<>(listPagedResultReturned.getTotal(),
                trackMapper.mapDTOs(listPagedResultReturned.getList()));
    }

    public WishlistDTO save(WishlistDTO wishlistDTO) {

        WishlistId wishlistId = mapper.map(wishlistDTO, WishlistId.class);

        WishlistEntity wishlistEntity = new WishlistEntity();
        wishlistEntity.setId(wishlistId);

        wishlistDAO.save(wishlistEntity);

        return wishlistDTO;
    }

    public void delete(Integer userId, Integer trackId) {

        WishlistId wishlistId = new WishlistId(userId, trackId);

        wishlistDAO.delete(wishlistId);
    }

    public void deleteByUserId(Integer userId) {

        wishlistDAO.deleteByUserId(userId);
    }
}
