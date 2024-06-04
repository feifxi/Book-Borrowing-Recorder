package service;

import java.util.stream.Stream;

import domain.Book;
import domain.BorrowRecord;
import domain.Customer;
import repository.BookRepository;
import repository.BorrowRecordRepository;
import repository.CustomerRepository;


public class LibraryService {
    private final BookRepository bookRepo;
    private final CustomerRepository customerRepo;
    private final BorrowRecordRepository recordRepo;

    public LibraryService(BookRepository bookRepo, CustomerRepository customerRepo,BorrowRecordRepository recordRepo){
        this.bookRepo = bookRepo;
        this.customerRepo = customerRepo;
        this.recordRepo = recordRepo;
    }   
    // Get
    public Book getBookById(String bookId){
        if (bookId == null || bookId.isBlank()) return null;
        return bookRepo.get(bookId);
    }
    public Book getBookByTitle(String title){
        return getBooks().filter(b -> b.gettitle().equals(title)).findFirst().orElse(null);
    }
    public BorrowRecord getRecordById(String recordId){
        if (recordId == null || recordId.isBlank()) return null;
        return recordRepo.get(recordId);
    }
    public Stream<Book> getBooks(){
        return bookRepo.stream();
    }
    public Stream<Book> getBooksByTitle(String title){
        return bookRepo.stream().filter(b -> b.gettitle().contains(title));
    }
    public Stream<Book> getBooksByAuthor(String author){
        return bookRepo.stream().filter(b -> b.getAuthor().contains(author));
    }

    public Customer getCustomerById(String customerId){
        if (customerId == null || customerId.isBlank()) return null;
        return customerRepo.get(customerId);
    }

    public Stream<Customer> getCustomers(){
        return customerRepo.stream();
    }

    public Stream<BorrowRecord> getRecords(){
        return recordRepo.stream();
    }
    
    
    // Create
    public Book addNewBook(String title, String author, String description, int numberOfCopies){
        if (title == null || author == null ||  description == null
        || title.isBlank() || author.isBlank() || description.isBlank() 
        || numberOfCopies < 0) return null;
        return bookRepo.create(title, author, description, numberOfCopies);
    }

    public Customer addNewCustomer(String name, String email){
        if (name == null || email == null || name.isBlank() || email.isBlank()) return null;
        return customerRepo.create(name, email);
    }


    // Update
    public boolean updateBook(String bookId,String title, String author, String description){
        Book book = getBookById(bookId);
        if (book == null || title == null || author == null ||  description == null
        || title.isBlank() || author.isBlank() || description.isBlank()) return false;
        return bookRepo.update(book, title, author, description, book.getAvailableCopies());
    }   
    public boolean updateBook(String bookId,int numberOfCopies){
        Book book = getBookById(bookId);
        if (book == null || numberOfCopies < 0) return false;
        return bookRepo.update(book, book.gettitle(), book.getAuthor(), book.getDescription(), numberOfCopies);
    }   
    
    public boolean updateCustomer(String customerId,String name, String email){
        Customer customer = getCustomerById(customerId);
        if (customer == null || name == null || email == null || name.isBlank() || email.isBlank() ) return false;
        return customerRepo.update(customer, name, email);
    }   


    // Remove
    public boolean removeBook(String bookId){
        Book removeBook = getBookById(bookId);
        if (removeBook == null) return false;
        return bookRepo.remove(removeBook);
    }
    public boolean removeAllBook(){
        return bookRepo.removeAll();
    }

    public boolean removeCustomer(String customerId){
        Customer removeCustomer = getCustomerById(customerId);
        if (removeCustomer == null) return false;
        return customerRepo.remove(removeCustomer);
    }


    // Borrow and Return Book Fucntion
    public boolean borrowBook(String customerId, String bookId){
        Book book = getBookById(bookId);
        Customer customer = getCustomerById(customerId);
        if (book == null || customer == null || !book.isAvailable()) return false;  // เช็คจาก book และ customer ที่มี
        BorrowRecord record = recordRepo.create(customerId, bookId);
        if (record == null) return false;
        book.reducedAvailableBook();    // ลดจน. book
        return true;
    }

    public boolean returnBook(String customerId, String bookId){
        BorrowRecord borrowRecord = recordRepo.stream()   // เช็คจาก record ที่มีเพราะ book และ customer อาจจะมีแต่ไม่ได้ยืมกัน
        .filter(br-> (br.getReturnDate().equals("Not Returned") || br.getReturnDate().equals("Overdue")) 
        && br.getCustomerId().equals(customerId) && br.getBookId().equals(bookId))
        .findFirst().orElse(null);
        if (borrowRecord == null) return false;
        borrowRecord.setReturnDate();
        Book book = getBookById(bookId);
        book.increaseAvailableBook();   // เพิ่มจน. book
        return true;
    }

    // Password and Login  
    public boolean checkPassword(String password){
        return password.equals("555");  // Password : 555
    }
}
