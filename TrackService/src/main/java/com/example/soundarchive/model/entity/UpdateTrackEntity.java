package com.example.soundarchive.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "track")
public class UpdateTrackEntity {
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

}
