package com.example.soundarchive.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "track")
public class TrackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "publish_date")
    private Timestamp publishDate;

    @Column(name = "duration")
    private Double duration;

    @Column(name = "picture")
    private String picture;

    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecordEntity> records;

    @ManyToOne
    @JoinColumn(name="artist_id", nullable=false)
    private ArtistEntity artist;

    @ManyToOne
    @JoinColumn(name="genre_id", nullable=false)
    private GenreEntity genre;
}
