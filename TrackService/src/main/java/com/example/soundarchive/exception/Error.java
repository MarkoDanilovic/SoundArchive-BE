package com.example.soundarchive.exception;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Error {

    private String errorType;
    private String errorMessage;
}
