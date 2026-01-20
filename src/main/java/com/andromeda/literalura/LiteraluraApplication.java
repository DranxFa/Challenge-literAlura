package com.andromeda.literalura;

import com.andromeda.literalura.DTO.AuthorResponse;
import com.andromeda.literalura.DTO.BookResponse;
import com.andromeda.literalura.service.AuthorService;
import com.andromeda.literalura.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    private final AuthorService authorService;
    private final BookService bookService;

    public LiteraluraApplication(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        String mensaje = """
                ----------------------------------------
                Elija la opción a través de su número:
                1- Buscar libro por Título
                2- Listar libros registrados
                3- Lista Autores registrados
                4- Listar Autores vivos en un determinado año
                5- Listar libros por idioma
                0- Salir
                ----------------------------------------
                """;

        while(true){
            System.out.println(mensaje);
            Integer option = scanner.nextInt();
            scanner.nextLine();
            switch (option){
                case 0 -> {
                    return;
                }
                case 1 -> {
                    System.out.println("Ingrese el título del libro");
                    String title = scanner.nextLine();
                    if (title == null || title.isBlank()) throw new IllegalArgumentException("El titulo no puede estar vacío");
                    showBook(bookService.searchAndSaveBook(title));
                }
                case 2 -> {
                    var books = bookService.listBooksRegistered();
                    books.forEach(this::showBook);
                }
                case 3 -> {
                    var authors = authorService.listAuthorsRegistered();
                    authors.forEach(this::showAuthor);
                }
                case 4 -> {
                    System.out.println("Ingrese el año vivo de autor(es) que desea buscar");
                    Integer year = scanner.nextInt();
                    scanner.nextLine();
                    var authors = authorService.listAuthorsAliveByYear(year);
                    authors.forEach(this::showAuthor);
                }
                case 5 -> {
                    System.out.println("Ingrese el idioma para buscar los libros");
                    System.out.println("""
                            es - español
                            en - inglés
                            fr - francés
                            pt - portugués
                            """);
                    String language = scanner.nextLine();
                    var books = bookService.listBooksByLanguage(language);
                    books.forEach(this::showBook);
                }
            }
        }

    }
    private void showBook(BookResponse bookSaved){
        System.out.println("----- LIBRO -----");
        System.out.println("Título: "+bookSaved.title());
        System.out.println("Autor: "+bookSaved.authors());
        System.out.println("Idioma: " +bookSaved.languages());
        System.out.println("Numero de descargas: "+bookSaved.downloadCount()+ "\n");
    }

    private void showAuthor(AuthorResponse author){
        System.out.println("Autor: "+author.name());
        System.out.println("Fecha de Nacimiento: "+author.birthYear());
        System.out.println("Fecha de Fallecimiento: " +author.deathYear());

//        var titles = author.books().stream().map(Book::getTitle).toList();
        System.out.println("Libros: " + author.books() + "\n");
    }
}
