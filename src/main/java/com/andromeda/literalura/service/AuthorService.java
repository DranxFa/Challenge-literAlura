package com.andromeda.literalura.service;
import com.andromeda.literalura.DTO.AuthorResponse;
import com.andromeda.literalura.entity.Author;
import com.andromeda.literalura.entity.Book;
import com.andromeda.literalura.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional(readOnly = true)
    public List<AuthorResponse> listAuthorsRegistered(){
        return authorRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<AuthorResponse> listAuthorsAliveByYear(Integer year){
        return authorRepository.findAuthorsAliveInYear(year).stream().map(this::toResponse).toList();
    }

    private AuthorResponse toResponse(Author author){
        return new AuthorResponse(
                author.getName(),
                author.getBirthYear(),
                author.getDeathYear(),
                author.getBooks().stream().map(Book::getTitle).toList()
        );
    }
}
