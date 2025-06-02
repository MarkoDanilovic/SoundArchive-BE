package com.example.soundarchive.service;

import com.example.soundarchive.dao.impl.ArtistDAOImpl;
import com.example.soundarchive.mapper.ObjectUtilMapper;
import com.example.soundarchive.model.dto.ArtistDTO;
import com.example.soundarchive.model.dto.pagination.ListPagedResultDTO;
import com.example.soundarchive.model.entity.ArtistEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistService {

    private  final ArtistDAOImpl artistDAO;

    private final ObjectUtilMapper mapper;

    @Autowired
    public ArtistService(ArtistDAOImpl artistDAO, ObjectUtilMapper mapper) {
        this.artistDAO = artistDAO;
        this.mapper = mapper;
    }

    public ArtistDTO findById(Integer id) {

        ArtistEntity artistEntity = artistDAO.findById(id);

        return mapper.map(artistEntity, ArtistDTO.class);
    }

    public List<ArtistDTO> findByName(String name) {

        List<ArtistEntity> artistEntities = artistDAO.findByName(name);

        return mapper.mapList(artistEntities, ArtistDTO.class);
    }

//    public List<ArtistDTO> findAll() {
//
//        List<ArtistEntity> artistEntities = artistDAO.findAll();
//
//        return mapper.mapList(artistEntities, ArtistDTO.class);
//    }

    public ListPagedResultDTO<ArtistDTO> findAll(Pageable pageable, String name) {

        ListPagedResultDTO<ArtistEntity> listPagedResultReturned = artistDAO.findAll(pageable, name);

        return new ListPagedResultDTO<>(listPagedResultReturned.getTotal(),
                mapper.mapList(listPagedResultReturned.getList(), ArtistDTO.class));
    }

    public ArtistDTO save(ArtistDTO artistDTO) {

        ArtistEntity artistEntity = mapper.map(artistDTO, ArtistEntity.class);

        artistEntity = artistDAO.save(artistEntity);

        return mapper.map(artistEntity, ArtistDTO.class);
    }

    public ArtistDTO update(ArtistDTO artistDTO) {

        ArtistEntity artistEntity = mapper.map(artistDTO, ArtistEntity.class);

        artistEntity = artistDAO.update(artistEntity);

        return mapper.map(artistEntity, ArtistDTO.class);
    }

    public void delete(Integer id) {

        artistDAO.delete(id);
    }

    public ArtistDTO updatePicture(Integer id, String relativePath) {

        ArtistEntity artistEntity = artistDAO.updatePicture(id, relativePath);

        return mapper.map(artistEntity, ArtistDTO.class);
    }
}
