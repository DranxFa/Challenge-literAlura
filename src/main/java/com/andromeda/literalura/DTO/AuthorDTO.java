package com.andromeda.literalura.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthorDTO(
        String name,
        @JsonProperty("birth_year") Integer birthYear,
        @JsonProperty("death_year") Integer deathYear
) {
}
