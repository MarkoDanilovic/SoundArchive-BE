package com.example.soundarchive.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class WishlistId implements Serializable {

    private Integer userId;

    private Integer trackId;
}
