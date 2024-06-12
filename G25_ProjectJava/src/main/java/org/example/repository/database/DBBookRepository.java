package org.example.repository.database;

import org.example.domain.Book;
import org.example.repository.BookRepository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DBBookRepository implements BookRepository {
    private List<Book> repo;
    private long nextBookId;

    public DBBookRepository(){
        String sql = "Select * FROM books";
        try (
            Connection conn = DatabaseConnection.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet results = preparedStatement.executeQuery();
            ) {
            repo = new ArrayList<>();
            int count = 0;
            while (results.next()){
                count++;
                String id = results.getString(1);
                String title = results.getString(2);
                String author = results.getString(3);
                String description = results.getString(4);
                int availableCopies = results.getInt(5);
                repo.add(new Book(id,title,author,description,availableCopies));
            }
            if (count == 0){     // ไม่มีข้อมูลใน Database
                nextBookId = 1;
            }
            else {
                Book lastBook = repo.get(count-1);
                String numberStr = lastBook.getId().replace("B","");
                nextBookId = (Integer.parseInt(numberStr)) + 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Book get(String bookId) {
        return repo.stream().filter(b -> b.getId().equals(bookId)).findFirst().orElse(null);
    }

    @Override
    public Book create(String title, String author, String description, int numberOfCopies) {
        long checkDuplicateTitle = repo.stream().filter(b -> b.gettitle().equals(title)).count();
        if (checkDuplicateTitle > 0) return null;
        String id = String.format("B%04d", nextBookId);
        Book book = new Book(id,title,author,description,numberOfCopies);
        repo.add(book);
        nextBookId++;
        String sql = "INSERT INTO books (id,title,author,description,availableCopies) VALUES (?,?,?,?,?)";
        try (
            Connection conn = DatabaseConnection.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ){
            preparedStatement.setString(1,id);
            preparedStatement.setString(2,title);
            preparedStatement.setString(3,author);
            preparedStatement.setString(4,description);
            preparedStatement.setInt(5,numberOfCopies);
            int result = preparedStatement.executeUpdate();
            if (result == 0) return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        String sql = "UPDATE books SET title=?,author=?,description=?,availableCopies=? WHERE id=?";
        try (
            Connection conn = DatabaseConnection.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ){
            preparedStatement.setString(5,book.getId());
            preparedStatement.setString(1,title);
            preparedStatement.setString(2,author);
            preparedStatement.setString(3,description);
            preparedStatement.setInt(4,numberOfCopies);
            int result = preparedStatement.executeUpdate();
            if (result == 0) return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public Boolean remove(Book book) {
        boolean removeStatus = repo.remove(book);
        if (removeStatus) {
            String sql = "DELETE FROM books WHERE id=?";
            try (
                Connection conn = DatabaseConnection.connect();
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                ){
                preparedStatement.setString(1,book.getId());
                int result = preparedStatement.executeUpdate();
                if (result == 0) return false;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return removeStatus;
    }


    @Override
    public Stream<Book> stream() {
        return repo.stream();
    }
}
