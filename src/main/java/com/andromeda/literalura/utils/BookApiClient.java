package com.andromeda.literalura.utils;

import com.andromeda.literalura.DTO.BookDTO;
import com.andromeda.literalura.DTO.JsonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Component
public class BookApiClient {

    private static final String BASE_URL = "https://gutendex.com/books/?search=";
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public BookApiClient(ObjectMapper objectMapper) {
        this.client = HttpClient.newHttpClient();;
        this.objectMapper = objectMapper;
    }

    public Optional<BookDTO> findBookByTitle(String title) {
        try{
            URI uri = URI.create(BASE_URL + title.replace(" ", "+").toLowerCase());
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

                JsonResponse jsonResponse = objectMapper.readValue(response.body(), JsonResponse.class);

                return jsonResponse.results().stream().findFirst();
                
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Error consultando API", e);
            }
    }
}


