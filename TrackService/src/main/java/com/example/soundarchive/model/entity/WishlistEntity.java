package com.example.soundarchive.model.entity;

import com.example.soundarchive.model.WishlistId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "wishlist")
public class WishlistEntity {

    @EmbeddedId
    private WishlistId id;

    @ManyToOne(optional = true)
    @MapsId("trackId")
    @JoinColumn(name = "track_id", insertable = false, updatable = false)
    private TrackEntity trackEntity;
}
