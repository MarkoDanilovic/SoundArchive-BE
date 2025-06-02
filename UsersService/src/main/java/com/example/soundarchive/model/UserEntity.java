package com.example.soundarchive.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name = "\"user\"")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "dateOfBirth")
    private String dateOfBirth;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "displayName")
    private String displayName;
    @Column(name = "description", length = 1000)
    private String description;
    @Column(name = "socialMediaLink")
    private String socialMediaLink;
    @Column(name = "phoneNumber")
    private String phoneNumber;
    @Column(name = "address")
    private String address;
    @Column(name = "city")
    private String city;
    @Column(name = "country")
    private String country;
    @Column(name = "creditCardNumber")
    private String creditCardNumber;

    @Column(name = "username", unique = true)
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "permissionLevel")
    private Integer permissionLevel;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "artistId")
    private Integer artistId;

}
