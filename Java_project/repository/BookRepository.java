package repository;
import java.util.stream.Stream;
import domain.Book;

public interface BookRepository {
    public Book get(String bookId);
    public Book create(String title, String author, String description, int numberOfCopies);
    public Boolean update(Book book,String title, String author, String description, int numberOfCopies);
    public Boolean remove(Book book);
    public Boolean removeAll();
    public Stream<Book> stream(); 
}
