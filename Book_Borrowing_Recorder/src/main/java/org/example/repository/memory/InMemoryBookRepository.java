package org.example.repository.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.example.domain.Book;
import org.example.repository.BookRepository;

public class InMemoryBookRepository implements BookRepository{
    private Map<String,Book> repo;
    private long nextBookId = 1;

    public InMemoryBookRepository(){this.repo = new HashMap<>();}

    @Override
    public Book get(String bookId) {
        return repo.get(bookId);
    }

    @Override
    public Book create(String title, String author, String description, int numberOfCopies) {
        long checkDuplicateTitle = repo.values().stream().filter(b -> b.gettitle().equals(title)).count();
        if (checkDuplicateTitle > 0) return null;
        String id = String.format("B%04d", nextBookId);
        Book book = new Book(id,title,author,description,numberOfCopies);
        repo.put(id,book);
        nextBookId++;
        return book;
    }

    @Override
    public Boolean update(Book book, String title, String author, String description, int numberOfCopies) {
        if (book == null) return false;
        book.setTitle(title);
        book.setAuthor(author);
        book.setDescription(description);
        book.setAvailableCopies(numberOfCopies);
        return true;
    }

    @Override
    public Boolean remove(Book book) {
        return repo.remove(book.getId(),book);
    }


    @Override
    public Stream<Book> stream() {
        return repo.values().stream();
    }

}

