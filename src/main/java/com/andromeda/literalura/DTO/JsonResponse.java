package com.andromeda.literalura.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JsonResponse(
        Integer count,
        List<BookDTO> results
) {
}
