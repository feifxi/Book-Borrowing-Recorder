package org.example.ui;

import java.util.stream.Stream;

import org.example.domain.Book;
import org.example.domain.BorrowRecord;
import org.example.domain.Customer;
import org.example.service.BookBorrowingRecorder;

public class UIComponent {
    private final BookBorrowingRecorder service;
    private String clearScreen = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
    private String statusMessage = "";
    private String booksListingOption = "ByAllBooks";
    private String booksListingValue = "";
    private String recordsListingOption = "ByAllRecords";
    private String recordsListingValue = "";

    public UIComponent(BookBorrowingRecorder service){
        this.service = service;
    }

    // Goal of this class is to display ui to the stdout and make code more reusable \

    // == UI ================ Option ===============
    // chooseStorage()
    // Login
    // mainMenu()

    // bookHeader() -> Menu , Detail
    // bookFooter()  -> Menu, Detail(id), TitleText(text)
    // listAllBooks() ->  ById, ByTitle(title), ByAuthor(author)
    // listBookDetail() -> (id)

    // customerHeader() -> Menu
    // customerFooter() -> Menu, TitleText(text)
    // listAllcustomers()

    // recordHeader() -> Menu
    // recordFooter() -> Menu, TitleText(text)
    // listAllRecord() -> ByBorrowDate, ByNotReturned, ByCustemer(id)

    // credit
    // StatusMessage


    // Choose Storage
    public void chooseStorage(){
        System.out.print("""
                ==== Choose Storage ====
                1.In-Memory Storage
                2.File Storage
                3.Database Storage
                [Press q to exit]
                ========================
                """);
    }

    // Login
    public void welcome(){
        String s = clearScreen + """
            ==================================================
            |   Welcome to the book borrowing recorder app   |
            ==================================================""";
        System.out.println(s);
    }


    // Main Menu
    public void mainMenu(){
        System.out.print(clearScreen +"""
            ======== Main Menu =========
            |  1.Book Menu             |
            |  2.Customer Menu         |
            |  3.Borrow Book Menu      |
            |  4.Credit                |
            |  [press q to exit]       |
            ============================
            Choose [1|2|3|4] :"""
        );
    }


    // Header
    public void bookHeader(String option,String value){
        if (option.equals("Menu")){
            System.out.println(
                    clearScreen
                            +"========================================== All Books =========================================\n"
                            +"|  ID   |             Title              |              Author            | Number of Copies |\n"
                            +"|--------------------------------------------------------------------------------------------|"
            );
        }
        else if (option.equals("Detail")){
            System.out.println(
                    clearScreen
                            +"===================================== Book ID "+value+" Detail ===================================\n"
                            +"|  ID   |             Title              |              Author            | Number of Copies |\n"
                            +"|--------------------------------------------------------------------------------------------|"
            );
        }
        else {
            System.out.println("There is no this ui component.");
        }
    }


    public void customerHeader(String option,String value){
        if (option.equals("Menu")){
            System.out.println(
                    clearScreen
                            +"=============================== All Customers =============================\n"
                            +"|  ID   |              Name              |              Email             |\n"
                            +"|-------------------------------------------------------------------------|"
            );
        }
        else {
            System.out.println("There is no this ui component.");
        }
    }

    public void recordHeader(String option,String value){
        if (option.equals("Menu")){
            System.out.println(
                    clearScreen
                            +"================================================== All Borrowing Records =================================================\n"
                            +"| Record |           Customer Name        |  CID  |            Book Title          |  BID  |  Borrow Date | Return Date  |\n"
                            +"|------------------------------------------------------------------------------------------------------------------------|"
            );
        }
        else {
            System.out.println("There is no this ui component.");
        }
    }


    // Footer
    public void bookFooter(String option, String value){
        if (option.equals("Menu")){
            System.out.println(
                    "==============================================================================================\n"
                            +"============== Book Menu =================\n"
                            +"1.Add new book      4.List all books [default] \n"
                            +"2.View book detail  5.List books by Title\n"
                            +"3.Remove all Books  6.List books by Author\n"
                            +"[press q to exit]\n"
                            +"=========================================="
            );
        }
        else if (option.equals("TitleText")) {
            System.out.println(
                    "==============================================================================================\n"
                            +"==== "+value+" ===="
            );
        }
        else if (option.equals("Detail")) {
            System.out.println(
                    "==============================================================================================\n"
                            +"=========== Option ===========\n"
                            +"1.Update book information\n"
                            +"2.Update book number of copies\n"
                            +"3.Remove book from storage\n"
                            +"[press q to exit]\n"
                            +"=============================="
            );
        }
        else {
            System.out.println("There is no this ui component.");
        }
    }

    public void customerFooter(String option,String value){
        if (option.equals("Menu")){
            System.out.println(
                    "===========================================================================\n"
                            +"=== Customer Menu ===\n"
                            +"1.Add new customer\n"
                            +"2.Update customer\n"
                            +"3.Remove customer\n"
                            +"[press q to exit]\n"
                            +"====================="
            );
        }
        else if (option.equals("TitleText")) {
            System.out.println(
                    "===========================================================================\n"
                            +"==== "+value+" ===="
            );
        }
        else {
            System.out.println("There is no this ui component.");
        }
    }

    public void recordFooter(String option,String value){
        if (option.equals("Menu")){
            System.out.println(
                    "==========================================================================================================================\n"
                            +"======= Borrowing Record Menu ========\n"
                            +"1.Borrow book\n"
                            +"2.Return book\n"
                            +"3.List all records [default]\n"
                            +"4.List all records that haven't been returned\n"
                            +"5.List all records of customer \n"
                            +"[press q to exit]\n"
                            +"======================================"
            );
        }
        else if (option.equals("TitleText")) {
            System.out.println(
                    "==========================================================================================================================\n"
                            +"========== "+value+" =========="
            );
        }
        else {
            System.out.println("There is no this ui component.");
        }
    }


    // List all books
    public void listAllBooks(){
        Stream<Book> stream;
        Stream<Book> checkStream;
        String message;
        if (booksListingOption.equals("ByAllBooks")) {
            stream = service.getBooks();
            checkStream = service.getBooks();
            message = "There is no book in stock";
        }
        else if (booksListingOption.equals("ByTitle")) {
            stream = service.getBooksByTitle(booksListingValue);
            checkStream = service.getBooksByTitle(booksListingValue);
            message = "There is no book in stock with title \"" + booksListingValue + "\"";
        }
        else if (booksListingOption.equals("ByAuthor")) {
            stream = service.getBooksByAuthor(booksListingValue);
            checkStream = service.getBooksByAuthor(booksListingValue);
            message = "There is no book in stock with author \"" + booksListingValue + "\"";
        }
        else {
            stream = service.getBooks();    // default
            checkStream = service.getBooks();
            message = "There is no book in stock";
        };
        Book someBook = checkStream.findFirst().orElse(null);
        if (someBook == null){
            System.out.println("| "+String.format("%-90s",message) +" |");
        }
        stream.forEach(book -> {
            System.out.println(
                    "| " + book.getId() + " | "
                            + String.format("%-30s",book.gettitle()) + " | "
                            + String.format("%-30s",book.getAuthor()) + " | "
                            + String.format("%-16s",book.getAvailableCopies()) + " | "
            );
        });
    }

    public void setBooksListingOption(String booksListingOption) {
        this.booksListingOption = booksListingOption;
    }
    public void setBooksListingValue(String booksListingValue) {
        this.booksListingValue = booksListingValue;
    }


    // List book detail
    public void listBookDetail(String id){
        Book book = service.getBookById(id);
        System.out.println(
                "| " + book.getId() + " | "
                        + String.format("%-30s",book.gettitle()) + " | "
                        + String.format("%-30s",book.getAuthor()) + " | "
                        + String.format("%-16s",book.getAvailableCopies()) + " | \n"
                        + "----------------------------------------------------------------------------------------------\n"
                        + "| Description : " + String.format("%-76s",book.getDescription()) + " | "
        );
    }


    // List all customers
    public void listAllCustomers(){
        Customer someCustomer = service.getCustomers().findFirst().orElse(null);
        if (someCustomer == null){
            System.out.println("| "+String.format("%-71s","There is no customer") +" |");
        }
        service.getCustomers().forEach(c -> {
            System.out.println(
                    "| " + c.getId() + " | "
                            + String.format("%-30s",c.getName()) + " | "
                            + String.format("%-30s",c.getEmail()) + " | "
            );
        });
    }


    // List all records
    public void listAllRecords(){
        Stream<BorrowRecord> stream;
        Stream<BorrowRecord> checkStream;
        String message;
        if (recordsListingOption.equals("ByAllRecords")) {
            stream = service.getRecords();
            checkStream = service.getRecords();
            message = "No book were borrowed";
        }
        else if (recordsListingOption.equals("ByNotReturned")) {
            stream = service.getRecords()
                    .filter(r->r.getReturnDate().equals("Not Returned") || r.getReturnDate().equals("Overdue"));
            checkStream = service.getRecords()
                    .filter(r->r.getReturnDate().equals("Not Returned") || r.getReturnDate().equals("Overdue"));
            message = "No book that haven't returned yet";
        }
        else if (recordsListingOption.equals("ByCustomer")) {
            stream = service.getRecords().filter(r->r.getCustomerId().equals(recordsListingValue));
            checkStream = service.getRecords().filter(r->r.getCustomerId().equals(recordsListingValue));
            message = "There is no book that "+service.getCustomerById(recordsListingValue)+" borrowed.";
        }
        else {
            stream = service.getRecords();  // default
            checkStream = service.getRecords();
            message = "There is no book in stock";
        };
        BorrowRecord someRecord = checkStream.findFirst().orElse(null);
        if (someRecord == null){
            System.out.println("| "+String.format("%-118s",message) +" |");
        }
        stream.forEach(r -> {
            System.out.println(
                    "| " + r.getRecordId() + " | "
                            + String.format("%-30s",service.getCustomerById(r.getCustomerId()).getName()) + " | "
                            + r.getCustomerId() + " | "
                            + String.format("%-30s",service.getBookById(r.getBookId()).gettitle()) + " | "
                            + r.getBookId() + " | "
                            + String.format("%-12s",r.getBorrowDate()) + " | "
                            + String.format("%-12s",r.getReturnDate()) + " | "
            );
        });
    }

    public void setRecordsListingOption(String recordsListingOption) {
        this.recordsListingOption = recordsListingOption;
    }
    public void setRecordsListingValue(String recordsListingValue) {
        this.recordsListingValue = recordsListingValue;
    }


    // Status Message
    public void setStatusMessage(String messageStatus) {
        this.statusMessage = messageStatus + "\n";
    }
    public void showStatusMessage(){
        System.out.print(statusMessage);
        this.statusMessage = "";
    }


    // Credit
    public void credit(){
        String credit = clearScreen + """
            =========== This Project Made by Group 25 IT66 ================
            | Student ID  |        Name            |   Resposibility      |
            |-------------------------------------------------------------|
            | 66130500081 | Sirawich Phasuk        |   Banana Management  |
            | 66130500114 | Noppawut Phomchana     |   Indian Call Center |
            | 66130500117 | Piyachoke Mevised      |   Dancing Engineer   |
            | 66130500125 | Suchai Cholchaipaisal  |   Cheif Monkey Officer, Chairman of the Banana, etc. |
            =============================================================== 

            [Press anything to quit]""";
        System.out.print(credit);
    }
}
