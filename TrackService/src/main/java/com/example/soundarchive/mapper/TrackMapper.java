package com.example.soundarchive.mapper;

import com.example.soundarchive.model.dto.ArtistDTO;
import com.example.soundarchive.model.dto.GenreDTO;
import com.example.soundarchive.model.dto.RecordDTO;
import com.example.soundarchive.model.dto.TrackDTO;
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

        TrackDTO trackDTO = mapper.map(trackEntity, TrackDTO.class);

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

        TrackEntity trackEntity = mapper.map(trackDTO, TrackEntity.class);
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
}
