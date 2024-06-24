package org.example.repository.file;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

import org.example.domain.Book;
import org.example.repository.BookRepository;

public class FileBookRepository implements BookRepository{
    private final String filename = FilePATH.path + "\\G25BookFile.txt";
    private Map<String,Book> repo;
    private long nextBookId;

    public FileBookRepository(){
        File file = new File(filename);
        if (file.exists()){
            try (
                    InputStream fis = new FileInputStream(filename);
                    InputStream bi = new BufferedInputStream(fis);
                    ObjectInputStream oi = new ObjectInputStream(bi);
            ) {
                nextBookId = oi.readLong();
                repo = (Map<String,Book>)oi.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            nextBookId = 1;
            this.repo = new HashMap<>();
        }
    }

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
        try (
                OutputStream fo = new FileOutputStream(filename);
                OutputStream bo = new BufferedOutputStream(fo);
                ObjectOutputStream oo = new ObjectOutputStream(bo);
        ) {
            oo.writeLong(nextBookId);
            oo.writeObject(repo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return book;
    }

    @Override
    public Boolean update(Book book, String title, String author, String description, int numberOfCopies) {
        if (book == null) return false;
        book.setTitle(title);
        book.setAuthor(author);
        book.setDescription(description);
        book.setAvailableCopies(numberOfCopies);
        try (
                OutputStream fo = new FileOutputStream(filename);
                OutputStream bo = new BufferedOutputStream(fo);
                ObjectOutputStream oo = new ObjectOutputStream(bo);
        ) {
            oo.writeLong(nextBookId);
            oo.writeObject(repo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Boolean remove(Book book) {
        boolean removeStatus = repo.remove(book.getId(),book);
        if (!removeStatus) return false;
        try (
                OutputStream fo = new FileOutputStream(filename);
                OutputStream bo = new BufferedOutputStream(fo);
                ObjectOutputStream oo = new ObjectOutputStream(bo);
        ) {
            oo.writeLong(nextBookId);
            oo.writeObject(repo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return removeStatus;
    }


    @Override
    public Stream<Book> stream() {
        return repo.values().stream();
    }

}

