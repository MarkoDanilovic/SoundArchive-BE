package com.example.soundarchive.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Error {

    private String error;
    private String errorType;
}
