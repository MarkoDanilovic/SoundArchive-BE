package com.example.soundarchive.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "artist")
public class ArtistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "artist_name")
    private String artistName;

    @Column(name = "birthday")
    private Timestamp birthday;

    @Column(name = "country")
    private String country;

    @Column(name = "picture")
    private String picture;

    @OneToMany(mappedBy = "artist")
    private List<TrackEntity> tracks;
}
