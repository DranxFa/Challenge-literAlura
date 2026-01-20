package com.andromeda.literalura.DTO;

import java.util.List;

public record BookResponse(
        String title,
        List<String> authors,
        List<String> languages,
        Integer downloadCount
) {
}
