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
public class RecordId  implements Serializable {

    private Integer trackId;

    private Integer mediumId;
}
