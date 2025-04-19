package com.example.soundarchive.model.entity;

import com.example.soundarchive.model.RecordId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "record")
public class RecordEntity {

    @EmbeddedId
    private RecordId recordId;

    @ManyToOne
    @MapsId("trackId")
    @JoinColumn(name="track_id", nullable=false)
    private TrackEntity track;

    @ManyToOne
    @MapsId("mediumId")
    @JoinColumn(name="medium_id", nullable=false)
    private MediumEntity medium;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Double price;

}
