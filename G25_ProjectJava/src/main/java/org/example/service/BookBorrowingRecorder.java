package org.example.service;


import java.util.stream.Stream;

import org.example.domain.Book;
import org.example.domain.BorrowRecord;
import org.example.domain.Customer;
import org.example.repository.BookRepository;
import org.example.repository.BorrowRecordRepository;
import org.example.repository.CustomerRepository;


public class BookBorrowingRecorder {
    private final BookRepository bookRepo;
    private final CustomerRepository customerRepo;
    private final BorrowRecordRepository recordRepo;

    public BookBorrowingRecorder(BookRepository bookRepo, CustomerRepository customerRepo,BorrowRecordRepository recordRepo){
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
    public Customer getCustomerByName(String customerName){
        if (customerName == null || customerName.isBlank()) return null;
        return getCustomers().filter(c -> c.getName().equals(customerName)).findFirst().orElse(null);
    }

    public Stream<Customer> getCustomers(){
        return customerRepo.stream();
    }
    public BorrowRecord getRecordById(String recordId){
        if (recordId == null || recordId.isBlank()) return null;
        return recordRepo.get(recordId);
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
        removeRecord(removeBook);   // ลบ record ที่เกี่ยวข้อง (ถ้ามี) 
        return bookRepo.remove(removeBook); 
    }
    public boolean removeAllBook(){
        return bookRepo.removeAll();
    }

    public boolean removeCustomer(String customerId){
        Customer removeCustomer = getCustomerById(customerId);
        if (removeCustomer == null) return false;
        removeRecord(removeCustomer);   // ลบ record ที่เกี่ยวข้อง (ถ้ามี)
        return customerRepo.remove(removeCustomer);
    }   

    public boolean removeRecord(Book book){
        long bookCount = recordRepo.stream().filter(r -> r.getBookId().equals(book.getId())).count();
        if (bookCount == 0) return false;   
        BorrowRecord[] records = recordRepo.stream().filter(r -> r.getBookId().equals(book.getId())).toArray(BorrowRecord[]::new);
        for (BorrowRecord record : records) {
            recordRepo.remove(record);
        }
        return true;
    }

    public boolean removeRecord(Customer customer){
        long bookCount = recordRepo.stream().filter(r -> r.getCustomerId().equals(customer.getId())).count();
        if (bookCount == 0) return false;   
        BorrowRecord[] records = recordRepo.stream().filter(r -> r.getCustomerId().equals(customer.getId())).toArray(BorrowRecord[]::new);
        for (BorrowRecord record : records) {
            recordRepo.remove(record);
        }
        return true;  
    }

    // Borrow and Return Book Fucntion
    public boolean borrowBook(String customerId, String bookId){
        Book book = getBookById(bookId);
        Customer customer = getCustomerById(customerId);
        if (book == null || customer == null || !book.isAvailable()) return false;  // เช็คจาก book และ customer ที่มี
        // สร้าง Record ใหม่
        BorrowRecord record = recordRepo.create(customerId, bookId);
        if (record == null) return false;
        // ลดจน. book
        book.reducedAvailableBook();
        updateBook(book.getId(),book.getAvailableCopies()); // Update in storage
        return true;
    }

    public boolean returnBook(String customerId, String bookId){
        BorrowRecord borrowRecord = recordRepo.stream()   // หา record การยืมของ book และ customer คู่นั้น
                .filter(br-> (br.getReturnDate().equals("Not Returned") || br.getReturnDate().equals("Overdue"))
                        && br.getCustomerId().equals(customerId) && br.getBookId().equals(bookId))
                .findFirst().orElse(null);
        if (borrowRecord == null) return false;     // ไม่มีบันทึกการยืมของคู่นี้
        // เปลี่ยนสถานะของบันทึกเป็น คืนแล้ว
        recordRepo.returnBook(borrowRecord);
        // เพิ่มจน. book
        Book book = getBookById(bookId);
        book.increaseAvailableBook();
        updateBook(book.getId(),book.getAvailableCopies());  // Update in storage
        return true;
    }

    // Password and Login
    public boolean checkPassword(String password){
        return password.equals("555");  // Password : 555
    }
}

