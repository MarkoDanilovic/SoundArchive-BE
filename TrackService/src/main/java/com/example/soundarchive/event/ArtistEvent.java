package com.example.soundarchive.event;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ArtistEvent {

    private Integer id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String email;
    private String password;
    private String socialMediaLink;
    private String artistName;
    private String biography;
    private String imageUrl;
    private String operation;
}