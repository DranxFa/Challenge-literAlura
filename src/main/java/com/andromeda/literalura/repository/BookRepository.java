package com.andromeda.literalura.repository;

import com.andromeda.literalura.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitle(String title);

    @Query("SELECT b FROM Book b JOIN b.languages l WHERE l = :language")
    List<Book> findByLanguage(String language);
}
