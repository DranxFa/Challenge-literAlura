package com.andromeda.literalura.service;
import com.andromeda.literalura.DTO.AuthorDTO;
import com.andromeda.literalura.DTO.BookResponse;
import com.andromeda.literalura.entity.Author;
import com.andromeda.literalura.entity.Book;
import com.andromeda.literalura.repository.AuthorRepository;
import com.andromeda.literalura.repository.BookRepository;
import com.andromeda.literalura.utils.BookApiClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookApiClient bookApiClient;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, BookApiClient bookApiClient) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookApiClient = bookApiClient;
    }

    @Transactional
    public BookResponse searchAndSaveBook(String title){

        var bookDTO = bookApiClient.findBookByTitle(title)
                .orElseThrow( () -> new IllegalArgumentException("Libro no encontrado"));

        bookRepository.findByTitle(bookDTO.title())
                .ifPresent( b -> {
                    throw new IllegalStateException("Libro ya registrado");
                } );

        Book bookEntity = new Book();
        bookEntity.setTitle(bookDTO.title());
        bookEntity.setLanguages(bookDTO.languages());
        bookEntity.setDownloadCount(bookDTO.downloadCount());

        var authors = bookDTO.authors().stream()
                .map( this::getOrCreateAuthor )
                .toList();

        bookEntity.setAuthors(authors);

        return toResponse(bookRepository.save(bookEntity));
    }

    @Transactional(readOnly = true)
    public List<BookResponse> listBooksRegistered(){
        return bookRepository.findAll().stream().map( this::toResponse ).toList();
    }

    @Transactional(readOnly = true)
    public List<BookResponse> listBooksByLanguage(String language){
        return bookRepository.findByLanguage(language).stream().map( this::toResponse ).toList();
    }

    private Author getOrCreateAuthor(AuthorDTO dto) {
        return authorRepository
                .findByName(dto.name())
                .orElseGet(() -> authorRepository.save(
                        new Author(dto.name(), dto.birthYear(), dto.deathYear())
                ));
    }

    private BookResponse toResponse(Book book){
        return new BookResponse(
                book.getTitle(),
                book.getAuthors().stream().map(Author::getName).toList(),
                List.copyOf(book.getLanguages()),
                book.getDownloadCount()
        );
    }
}
