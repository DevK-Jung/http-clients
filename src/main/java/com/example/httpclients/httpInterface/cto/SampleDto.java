package com.example.httpclients.httpInterface.cto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SampleDto {
    private Long id;
    private String userId;

    public SampleDto(String userId) {
        this.userId = userId;
    }
}

