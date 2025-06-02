package com.example.soundarchive.mapper;

import com.example.soundarchive.model.RecordId;
import com.example.soundarchive.model.dto.*;
import com.example.soundarchive.model.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TrackMapper {

    private final ObjectUtilMapper mapper;

    private final RecordMapper recordMapper;

    @Autowired
    public TrackMapper(ObjectUtilMapper mapper, RecordMapper recordMapper) {
        this.mapper = mapper;
        this.recordMapper = recordMapper;
    }

    public TrackDTO mapDTO(TrackEntity trackEntity) {//, List<MediumEntity> mediumEntities) {

        //TrackDTO trackDTO = mapper.map(trackEntity, TrackDTO.class);
        TrackDTO trackDTO = new TrackDTO();
        trackDTO.setId(trackEntity.getId());
        trackDTO.setName(trackEntity.getName());
        trackDTO.setPublishDate(trackEntity.getPublishDate());
        trackDTO.setDuration(trackEntity.getDuration());
        if(trackDTO.getPicture() != null && !trackDTO.getPicture().isEmpty()) {
            trackDTO.setPicture(trackDTO.getPicture());
        }
        else {
            trackDTO.setPicture("");
        }

        ArtistDTO artistDTO = mapper.map(trackEntity.getArtist(), ArtistDTO.class);
        trackDTO.setArtist(artistDTO);

        GenreDTO genreDTO = mapper.map(trackEntity.getGenre(), GenreDTO.class);
        trackDTO.setGenre(genreDTO);

        List<RecordDTO> recordDTOs = recordMapper.mapDTOs(trackEntity.getRecords());//, mediumEntities);
        trackDTO.setRecords(recordDTOs);
        //kako upisuje record i da li treba odvojeno kod upisa track-a ili ce automatski ako ima referencu na entitet

        return trackDTO;
    }

    public TrackEntity mapEntity(TrackDTO trackDTO) {

        //TrackEntity trackEntity = mapper.map(trackDTO, TrackEntity.class);
        TrackEntity trackEntity = new TrackEntity();
        if(trackDTO.getId() != null && !trackDTO.getId().equals(0)) {
            trackEntity.setId(trackDTO.getId());
        }
        trackEntity.setName(trackDTO.getName());
        trackEntity.setPublishDate(trackDTO.getPublishDate());
        trackEntity.setDuration(trackDTO.getDuration());
        if(trackDTO.getPicture() != null && !trackDTO.getPicture().isEmpty()) {
            trackEntity.setPicture(trackDTO.getPicture());
        } else {
            trackEntity.setPicture("");
        }

        trackEntity.setArtist(mapper.map(trackDTO.getArtist(), ArtistEntity.class));
        trackEntity.setGenre(mapper.map(trackDTO.getGenre(), GenreEntity.class));
        trackEntity.setRecords(recordMapper.mapEntities(trackDTO.getRecords(), trackDTO.getId()));

        return trackEntity;
    }

    public List<TrackDTO> mapDTOs(List<TrackEntity> trackEntities) {

        List<TrackDTO> trackDTOs = new ArrayList<>();

        for (TrackEntity trackEntity : trackEntities) {

            TrackDTO trackDTO = mapper.map(trackEntity, TrackDTO.class);
            trackDTO.setArtist(mapper.map(trackEntity.getArtist(), ArtistDTO.class));
            trackDTO.setGenre(mapper.map(trackEntity.getGenre(), GenreDTO.class));
            trackDTO.setRecords(recordMapper.mapDTOs(trackEntity.getRecords()));

            trackDTOs.add(trackDTO);
        }

        return trackDTOs;
    }

    //vidi da li moze da se u entity ubace pod objekti i on automatski da ih upise, vidi i listu recorda da li moze

    //track id je dupli u recordu, pitanje dal treba oba, i sta onda sa id-jevima ako mora da se izbaci


    //mozda da radi cascade unos, ali samo za admine, jer ne bi trebalo artist da moze da unese recorde
    //pa tipa da izadje adminima unos za track, pa oni odobre i unesu u bazu i recorde?

    //biderictional?, cascade persist oko 39:40 lesson 6

    //pri kraju kaze da join table nije orm way i ne bi trebalo on da record da pravim kao entitet
    //ali pitanje onda sta sa dodatnim vrednostima

    //svuda preporucuje _ za nazive kolona u bazi, i jel ce biti jednine za tabele

    //JPA/Hibernate Fundamentals 2023 - Lesson 7 - Many-to-many relationships
    //stigao do 30. min


    public void mapUpdate(TrackEntity trackEntity, UpdateTrackDTO updateTrackDTO) {

        trackEntity.setName(updateTrackDTO.getName());
        trackEntity.setPublishDate(updateTrackDTO.getPublishDate());
        trackEntity.setDuration(updateTrackDTO.getDuration());
        if(updateTrackDTO.getPicture() != null && !updateTrackDTO.getPicture().isEmpty()) {
            trackEntity.setPicture(updateTrackDTO.getPicture());
        }
        trackEntity.setGenre(mapper.map(updateTrackDTO.getGenre(), GenreEntity.class));

        trackEntity.getRecords().clear();
        if(updateTrackDTO.getRecords() != null) {
            for (RecordDTO recordDTO : updateTrackDTO.getRecords()) {
                RecordEntity recordEntity = new RecordEntity();

                RecordId recordId = new RecordId();
                recordId.setTrackId(trackEntity.getId());
                recordId.setMediumId(recordDTO.getMedium().getId());
                recordEntity.setRecordId(recordId);

                recordEntity.setTrack(trackEntity);

                MediumEntity mediumEntity = mapper.map(recordDTO.getMedium(), MediumEntity.class);
                recordEntity.setMedium(mediumEntity);

                recordEntity.setQuantity(recordDTO.getQuantity());
                recordEntity.setPrice(recordDTO.getPrice());

                trackEntity.getRecords().add(recordEntity);
            }
        }
    }
}
