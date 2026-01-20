package com.andromeda.literalura.DTO;

import java.util.List;

public record AuthorResponse(
        String name,
        Integer birthYear,
        Integer deathYear,
        List<String> books
) {
}
