package ui;

import java.util.Scanner;

import domain.Book;
import domain.Customer;
import repository.memory.InMemoryBookRepository;
import repository.memory.InMemoryBorrowRecordRepository;
import repository.memory.InMemoryCustomerRepository;
import service.LibraryService;

public class LibraryUI {
    private final LibraryService service;
    private final Scanner sc = new Scanner(System.in);
    private UIComponent uiComponent;

    public LibraryUI(boolean useDatabase){
        if (useDatabase){
            service = new LibraryService(       // Use Database  //////////////////////////////////////////////////////////
                new InMemoryBookRepository(), 
                new InMemoryCustomerRepository(), 
                new InMemoryBorrowRecordRepository()
            );
        }
        else {      // useDatabase is false would you File storage instead 
            service = new LibraryService(       ////////////////////////////////////////////////////////////////////////
                new InMemoryBookRepository(), 
                new InMemoryCustomerRepository(), 
                new InMemoryBorrowRecordRepository()); 
                }
        this.uiComponent = new UIComponent(service);
    }

    public LibraryUI(){     // No augument constructor would use in-memory storage
        service = new LibraryService(
                new InMemoryBookRepository(), 
                new InMemoryCustomerRepository(), 
                new InMemoryBorrowRecordRepository()
                );
        this.uiComponent = new UIComponent(service);
    }


    public void start(){
        // Mock Data
        service.addNewBook("Monkey King", "fei zeedzad","Hello wordldddddddddd",2);
        service.addNewBook("The Snake1", "jeno theadven","Hello worlddddddddddd",5);
        service.addNewBook("The Snake2", "jeno theadven","Hello worlddddddddddd",10);
        service.addNewBook("The Snake3", "jeno theadven","Hello worlddddddddddd",8);
        service.addNewCustomer("ter", "ternoob@gmail.com");
        service.addNewCustomer("ice", "pjkuy@gmail.com");
        service.addNewCustomer("shokun", "shoku@gmail.com");
        service.borrowBook("C0001","B0001");
        service.borrowBook("C0002","B0002");
        service.borrowBook("C0003", "B0002");
        service.borrowBook("C0003", "B0003");
        service.getRecords().findFirst().get().setOverDueTesting();
        service.returnBook("C0003", "B0003");
        
        loginUI();
    }

    public void loginUI(){
        var cons = System.console();
        uiComponent.setStatusMessage("*Please fill the password to continue");
        while (true) {
            uiComponent.login();
            uiComponent.showStatusMessage();
            System.out.print("Password [press q to exit]:");
            String password =  new String(cons.readPassword());
            if (password.equalsIgnoreCase("q")) break;
            else if (password.isBlank()){
                uiComponent.setStatusMessage("*Please fill the password");
            }
            else if (service.checkPassword(password)){
                mainMenuUI();
            }
            else {
                uiComponent.setStatusMessage("*Password is incorrect");
            }
        }
    }


    public void mainMenuUI(){
        while (true) {
            uiComponent.mainMenu();
            String choose = sc.nextLine();
            if (choose.isBlank()) continue;
            else if (choose.equalsIgnoreCase("q")) break;
            else if (choose.equals("1")) bookMenuUI();  
            else if (choose.equals("2")) customerMenuUI();
            else if (choose.equals("3")) borrowRecordUI(); 
            else if (choose.equals("4")){
                // Just display credit screen
                uiComponent.credit();
                sc.nextLine();
                uiComponent.javaGradeAforSure();
                sc.nextLine();
            }
        }
    }


    public void bookMenuUI(){
        while (true) {
            // Display
            uiComponent.bookHeader("Menu", null);
            uiComponent.listAllBooks();
            uiComponent.bookFooter("Menu", null);
            System.out.print("Choose [1-6] :");
            // Input
            String choose = sc.nextLine();
            if (choose.isBlank()) continue;     
            else if (choose.equalsIgnoreCase("q")) break; 
            else if (choose.equals("1")){     // Add Book
                addOrUpdateBookUI("AddBook",null);
            }
            else if (choose.equals("2")) {   // View Book                       
                while (true) {
                    // Display
                    uiComponent.bookHeader("Menu", null);
                    uiComponent.listAllBooks();
                    uiComponent.bookFooter("TitleText", "View book detail");
                    uiComponent.showStatusMessage();
                    System.out.print("Book ID [q to Quit]:");
                    // Input
                    String id = sc.nextLine();
                    if (id.equalsIgnoreCase("q")) break;
                    else if (id.isBlank()) {     
                        uiComponent.setStatusMessage("*Please fill the input");
                        continue;
                    }
                    Book book = service.getBookById(id); 
                    if (book == null) {
                        uiComponent.setStatusMessage("*There is no book ID " + id + " please try again"); 
                        continue;
                    }
                    else {     
                        viewBookUI(id);
                        break;
                    }
                } 
            }
            else if (choose.equals("3")) {     // Remove all book
                while (true) {
                    var cons = System.console();
                    // Display
                    uiComponent.bookHeader("Menu", null);
                    uiComponent.listAllBooks();
                    uiComponent.bookFooter("TitleText", "Remove all books");
                    uiComponent.showStatusMessage();
                    System.out.print("Password [q to Quit]:");
                    // Input
                    String password =  new String(cons.readPassword());
                    if (password.equalsIgnoreCase("q")) break;
                    else if (service.checkPassword(password)){
                        service.removeAllBook();
                        uiComponent.setStatusMessage("Remove all book sucessfully");
                    }
                    else {     
                        uiComponent.setStatusMessage("Password is incorrect");
                    }
                } 
            }
            else if (choose.equals("4")) {      // List all book
                uiComponent.setBooksListingOption("AllBook");
            }
            else if (choose.equals("5")) {      // List book by title
                while (true) {
                    // Display
                    uiComponent.bookHeader("Menu", null);
                    uiComponent.listAllBooks();
                    uiComponent.bookFooter("TitleText", "List books by title");
                    uiComponent.showStatusMessage();
                    System.out.print("Book title [q to Quit]:");
                    // Input
                    String title = sc.nextLine();
                    if (title.equalsIgnoreCase("q")) break;
                    else if (title.isBlank()) {    
                        uiComponent.setStatusMessage("Please fill the input");
                        continue;
                    }
                    else {   
                        uiComponent.setBooksListingOption("ByTitle");  
                        uiComponent.setBooksListingValue(title);
                    }
                } 
            } 
            else if (choose.equals("6")) {      // List book by author
                while (true) {
                    // Display
                    uiComponent.bookHeader("Menu", null);
                    uiComponent.listAllBooks();
                    uiComponent.bookFooter("TitleText", "List books by author");
                    uiComponent.showStatusMessage();
                    System.out.print("Book author [q to Quit]:");
                    String author = sc.nextLine();
                    // Input
                    if (author.equalsIgnoreCase("q")) break;
                    else if (author.isBlank()) {     
                        uiComponent.setStatusMessage("Please fill the input");
                        continue;
                    }
                    else {     
                        uiComponent.setBooksListingOption("ByAuthor");  
                        uiComponent.setBooksListingValue(author);
                    }
                } 
            } 
        }
    }

    
    public void addOrUpdateBookUI(String option, String id){
        String title="", author="", description="";
        int numberOfCopies = 0;
        String footerTitleText = "";
        if (option.equals("AddBook")) footerTitleText = "Add new book";
        else if (option.equals("UpdateBook")) footerTitleText = "Update book ID "+id;
        uiComponent.setBooksListingOption("ById");
        while (true) {
            while (true) {     // get Title
                // Display
                if (option.equals("AddBook")){
                    uiComponent.bookHeader("Menu", null);
                    uiComponent.listAllBooks();
                }       
                else if (option.equals("UpdateBook")){
                    uiComponent.bookHeader("Detail", id);
                    uiComponent.listBookDetail(id);
                }
                else System.out.println("Some thing went wrong in AddOrUpdate method..");
                uiComponent.bookFooter("TitleText", footerTitleText);
                uiComponent.showStatusMessage();
                System.out.print("Book title [q to Quit]:");
                // Input
                title = sc.nextLine();
                if (title.equalsIgnoreCase("q")) return;
                else if (title.isBlank()) {   
                    uiComponent.setStatusMessage("*Please fill the input");
                    continue;
                };  
                Book book = service.getBookByTitle(title);  
                if (book != null) { // check duplicate book title
                    uiComponent.setStatusMessage("*There is already have book title name \""+title+"\""+ " in storage*");
                    continue;
                }
                else if (title.length() > 30) { 
                    uiComponent.setStatusMessage("*Title length should not more than 30 character");
                    continue;
                }
                else break;
            }
            while (true) {    // get Author
                // Display
                if (option.equals("AddBook")){
                    uiComponent.bookHeader("Menu", null);
                    uiComponent.listAllBooks();
                }       
                else if (option.equals("UpdateBook")){
                    uiComponent.bookHeader("Detail", id);
                    uiComponent.listBookDetail(id);
                }
                else System.out.println("Some thing went wrong in AddOrUpdate method..");
                uiComponent.bookFooter("TitleText", footerTitleText);
                uiComponent.showStatusMessage();
                System.out.print("Book author [q to Quit]:");
                // Input
                author = sc.nextLine();
                if (author.equalsIgnoreCase("q")) return;
                else if (author.isBlank()) {    
                    uiComponent.setStatusMessage("*Please fill the input");
                    continue;
                }
                else if (author.length() > 30) { 
                    uiComponent.setStatusMessage("*Author length should not more than 30 character");
                    continue;
                }
                else break;
            }
            while (true) {    // get Description
                // Display
                if (option.equals("AddBook")){
                    uiComponent.bookHeader("Menu", null);
                    uiComponent.listAllBooks();
                }       
                else if (option.equals("UpdateBook")){
                    uiComponent.bookHeader("Detail", id);
                    uiComponent.listBookDetail(id);
                }
                else System.out.println("Some thing went wrong in AddOrUpdate method..");
                uiComponent.bookFooter("TitleText", footerTitleText);
                uiComponent.showStatusMessage();
                System.out.print("Book description [q to Quit]:");
                // Input
                description = sc.nextLine();
                if (description.equalsIgnoreCase("q")) return;
                else if (description.isBlank()) {   
                    uiComponent.setStatusMessage("*Please fill the input"); 
                    continue;
                }
                else if (description.length() > 85) { 
                    uiComponent.setStatusMessage("*Description length should not more than 85 character");
                    continue;
                }
                else break;
            }
            while (option.equals("AddBook")) {    // get Number Of Copies
                // Display
                if (option.equals("AddBook")){
                    uiComponent.bookHeader("Menu", null);
                    uiComponent.listAllBooks();
                }       
                else if (option.equals("UpdateBook")){
                    uiComponent.bookHeader("Detail", id);
                    uiComponent.listBookDetail(id);
                }
                else System.out.println("Some thing went wrong in AddOrUpdate method..");
                uiComponent.bookFooter("TitleText", footerTitleText);
                uiComponent.showStatusMessage();
                System.out.print("Book number of copies [q to Quit]:");
                // Input
                String strNumberOfCopies = sc.nextLine();
                if (strNumberOfCopies.equalsIgnoreCase("q")) return;
                else if (strNumberOfCopies.isBlank()) {    
                    uiComponent.setStatusMessage("*Please fill the input");
                    continue;
                }
                Scanner numberSC = new Scanner(strNumberOfCopies);
                if (numberSC.hasNext("^(?:\\d|[1-9]\\d|[1-9]\\d{2})$")){//////////////////////////////////////////////////////////////////
                    numberOfCopies = numberSC.nextInt();
                    break;
                }                    
                else uiComponent.setStatusMessage("*Number of copies should be integer between 0 - 999");
            }
            
            // Add new book 
            if (option.equals("AddBook")){
                Book newBook = service.addNewBook(title, author, description, numberOfCopies);
                if (newBook != null) uiComponent.setStatusMessage("*Add new book successfully!");
                else uiComponent.setStatusMessage("*Fail to add new book please try again");
            }
            // Update book 
            else if (option.equals("UpdateBook")){
                boolean updateStatus = service.updateBook(id,title, author, description);
                if (updateStatus) uiComponent.setStatusMessage("*Update book successfully!");
                else uiComponent.setStatusMessage("*Fail to update book please try again");
            }
            // Display status and ask if need to add new book or update book again 
            while (true) {
                // Dislplay
                if (option.equals("AddBook")){
                    uiComponent.bookHeader("Menu", null);
                    uiComponent.listAllBooks();
                }
                else if (option.equals("UpdateBook")){
                    uiComponent.bookHeader("Detail", id);
                    uiComponent.listBookDetail(id);
                }
                uiComponent.bookFooter("TitleText", footerTitleText);
                uiComponent.showStatusMessage(); 
                // Input
                System.out.print(footerTitleText + " again press y [q to quit]:");
                String choose = sc.nextLine();
                if (choose.equalsIgnoreCase("q")) return;
                else if (choose.equalsIgnoreCase("y")) break;
            }
        }
    }

    public void viewBookUI(String id){
        while (true) {
            // Display
            uiComponent.bookHeader("Detail", id);
            uiComponent.listBookDetail(id);
            uiComponent.bookFooter("Detail", id);
            System.out.print("Choose [1|2|3] :");
            // Input
            String choose = sc.nextLine();
            if (choose.equalsIgnoreCase("q")) break;
            else if (choose.isBlank()) continue;
            else if (choose.equals("1")){
                addOrUpdateBookUI("UpdateBook", id);
            }
            else if (choose.equals("2")){
                while (true) {
                    uiComponent.bookHeader("Detail", id);
                    uiComponent.listBookDetail(id);
                    uiComponent.bookFooter("TitleText", "Update number of copies");
                    uiComponent.showStatusMessage();
                    System.out.print("Number [q to quit]:");
                    String strNumberOfCopies = sc.nextLine();
                    if (strNumberOfCopies.equalsIgnoreCase("q")) break;
                    else if (strNumberOfCopies.isBlank()) {    
                        uiComponent.setStatusMessage("*Please fill the input");
                        continue;
                    }
                    Scanner numberSC = new Scanner(strNumberOfCopies);
                    if (numberSC.hasNext("^(?:\\d|[1-9]\\d|[1-9]\\d{2})$")){/////////////////////////////////////////////////////////////////////
                        int numberOfCopies = numberSC.nextInt();
                        service.updateBook(id, numberOfCopies);
                        uiComponent.setStatusMessage("*Update number of copies successfully!");
                    }                    
                    else uiComponent.setStatusMessage("*Number of copies should be integer between 0 - 999");
                }
            }
            else if (choose.equals("3")){
                while (true) {
                    uiComponent.bookHeader("Detail", id);
                    uiComponent.listBookDetail(id);
                    uiComponent.bookFooter("TitleText", "Remove book ID "+ id);
                    System.out.print("Are you sure? - yes[y] / No[n] :");
                    choose = sc.nextLine();
                    if (choose.equalsIgnoreCase("y")) {
                        service.removeBook(id);
                        uiComponent.setStatusMessage("Remove book sucessfully!");
                        uiComponent.showStatusMessage();
                        System.out.print("[Press anything to go back to book Menu]");
                        sc.nextLine();
                        return;
                    }
                    else if (choose.equalsIgnoreCase("n")) break;
                }
            }
        }
    }

    

    public void customerMenuUI(){
        while (true) {
            // Display
            uiComponent.customerHeader("Menu", null);
            uiComponent.listAllCustomers();
            uiComponent.customerFooter("Menu", null);
            System.out.print("Choose [1|2|3] :");
            String choose = sc.nextLine();
            // Input
            if (choose.isBlank()) continue;     
            else if (choose.equalsIgnoreCase("q")) break; 
            else if (choose.equals("1")){     // Add Customer
                addOrUpdateCustomerUI("AddCustomer",null);
            }
            else if (choose.equals("2")){     // Update Customer
                // Check If customer is exist first!
                while (true) {
                    // Display
                    uiComponent.customerHeader("Menu", null);
                    uiComponent.listAllCustomers();
                    uiComponent.customerFooter("TitleText", "Update customer");
                    uiComponent.showStatusMessage();
                    System.out.print("Customer ID [q to Quit]:");
                    // Input
                    String id = sc.nextLine();  
                    if (id.equalsIgnoreCase("q")) break;
                    else if (id.isBlank()) {     
                        uiComponent.setStatusMessage("*Please fill the input");
                        continue;
                    }   
                    Customer customer = service.getCustomerById(id);    
                    if (customer == null) {
                        uiComponent.setStatusMessage("*There is no customer ID " + id + " please try again"); 
                        continue;
                    }
                    else {     
                        addOrUpdateCustomerUI("UpdateCustomer", id);
                    }
                }
            }
            else if (choose.equals("3")){       // Remove Customer
                // Check If customer is exist first!
                while (true) {
                    // Display
                    uiComponent.customerHeader("Menu", null);
                    uiComponent.listAllCustomers();
                    uiComponent.customerFooter("TitleText", "Remove customer");
                    uiComponent.showStatusMessage();
                    System.out.print("Customer ID [q to Quit]:");
                    // Input
                    String id = sc.nextLine();  
                    if (id.equalsIgnoreCase("q")) break;
                    else if (id.isBlank()) {     
                        uiComponent.setStatusMessage("*Please fill the input");
                        continue;
                    }   
                    Customer customer = service.getCustomerById(id);    
                    if (customer == null) {
                        uiComponent.setStatusMessage("*There is no customer ID " + id + " please try again"); 
                        continue;
                    }
                    else {     // Are you sure?
                        while (true) {   
                            // Display   
                            uiComponent.customerHeader("Menu", null);
                            uiComponent.listAllCustomers();
                            uiComponent.customerFooter("TitleText", "Remove customer ID "+ id);
                            System.out.print("Are you sure? - yes[y] / No[n] :");
                            // Input
                            choose = sc.nextLine();
                            if (choose.equalsIgnoreCase("y")) { // Yes
                                service.removeCustomer(id);
                                uiComponent.setStatusMessage("Remove customer sucessfully!");
                                break;
                            }
                            else if (choose.equalsIgnoreCase("n")) break;   // No
                        }
                    }
                }
            }
        }
    }


    public void addOrUpdateCustomerUI(String option, String id){
        String name="", email="";
        String footerTitleText = "";
        if (option.equals("AddCustomer")) footerTitleText = "Add new customer";
        else if (option.equals("UpdateCustomer")) footerTitleText = "Update customer ID "+ id;
        while (true) {
            while (true) {     // get Name
                // Display
                uiComponent.customerHeader("Menu", null);
                uiComponent.listAllCustomers();
                uiComponent.customerFooter("TitleText", footerTitleText);
                uiComponent.showStatusMessage();
                System.out.print("Customer name [q to Quit]:");
                // Input
                name = sc.nextLine();
                if (name.equalsIgnoreCase("q")) return;
                else if (name.isBlank()) {   
                    uiComponent.setStatusMessage("*Please fill the input");
                    continue;
                };  
                Book book = service.getBookByTitle(name);  
                if (book != null) { // check duplicate customer name
                    uiComponent.setStatusMessage("*There is already have customer name \""+name+"\""+ " in storage*");
                    continue;
                }
                else if (name.length() > 30) { 
                    uiComponent.setStatusMessage("*Customer name length should not more than 30 character");
                    continue;
                }
                else break;
            }
            while (true) {    // get Email
                // Display
                uiComponent.customerHeader("Menu", null);
                uiComponent.listAllCustomers();
                uiComponent.customerFooter("TitleText", footerTitleText);
                uiComponent.showStatusMessage();
                System.out.print("Customer email [q to Quit]:");
                // Input
                email = sc.nextLine();
                if (email.equalsIgnoreCase("q")) return;
                else if (email.isBlank()) {    
                    uiComponent.setStatusMessage("*Please fill the input");
                    continue;
                }
                else if (email.length() > 30) { 
                    uiComponent.setStatusMessage("*Email length should not more than 30 character");
                    continue;
                }
                else break;
            }
            
            // Add new Customer 
            if (option.equals("AddCustomer")){
                Customer newCustomer = service.addNewCustomer(name,email);
                if (newCustomer != null) uiComponent.setStatusMessage("*Add new customer successfully!");
                else uiComponent.setStatusMessage("*Fail to add new customer please try again");
            }
            // Update Customer 
            else if (option.equals("UpdateCustomer")){
                boolean updateStatus = service.updateCustomer(id,name,email);
                if (updateStatus) uiComponent.setStatusMessage("*Update customer successfully!");
                else uiComponent.setStatusMessage("*Fail to update customer please try again");
            }
            // Display status and ask if need to add new customer or update customer again 
            while (true) {
                // Dislplay
                uiComponent.customerHeader("Menu", null);
                uiComponent.listAllCustomers();
                uiComponent.customerFooter("TitleText", footerTitleText);
                uiComponent.showStatusMessage();
                // Input
                System.out.print(footerTitleText + " again press y [q to quit]:");
                String choose = sc.nextLine();
                if (choose.equalsIgnoreCase("q")) return;
                else if (choose.equalsIgnoreCase("y")) break;
            }
        }
    }


    public void borrowRecordUI(){  
        while (true) {
            // Display
            uiComponent.recordHeader("Menu", null);
            uiComponent.listAllRecords();
            uiComponent.recordFooter("Menu", null);
            System.out.print("Choose [1-5]:");
            // Input
            String choose = sc.nextLine();
            if (choose.isBlank()) continue;     
            else if (choose.equalsIgnoreCase("q")) break; 
            else if (choose.equals("1")){     // borrow book
                borrowOrReturnBookUI("BorrowBook");
            }
            else if (choose.equals("2")) {   //  return book    
                borrowOrReturnBookUI("ReturnBook");  
            }
            else if (choose.equals("3")) {      // List all records 
                uiComponent.setRecordsListingOption("ByAllRecords");
            }
            else if (choose.equals("4")) {      // List all records that haven't been returned yet
                uiComponent.setRecordsListingOption("ByNotReturned");
            }
            else if (choose.equals("5")) {      // List records of customer
                listAllRecordsOfCustomer();
            } 
        }
    }

    public void borrowOrReturnBookUI(String option){     
        String customerId,bookId, footerTitleText;        
        if (option.equals("BorrowBook")) footerTitleText = "Borrow book";
        else if (option.equals("ReturnBook")) footerTitleText = "Return Book"; 
        else footerTitleText = "";
        while (true) {  // Get customer ID
            // Display
            uiComponent.recordHeader("Menu", null);
            uiComponent.listAllRecords();
            uiComponent.recordFooter("TitleText", footerTitleText);
            uiComponent.showStatusMessage();
            System.out.print("Customer ID [q to Quit]:");
            // Input
            customerId = sc.nextLine();
            if (customerId.equalsIgnoreCase("q")) return;
            else if (customerId.isBlank()) {     
                uiComponent.setStatusMessage("*Please fill the input");
                continue;
            }
            Customer customer = service.getCustomerById(customerId); 
            if (customer == null) {
                uiComponent.setStatusMessage("*There is no customer ID " + customerId + " please try again"); 
                continue;
            }
            else break;
        }
        while (true) {  // Get book ID
            // Display
            uiComponent.recordHeader("Menu", null);
            uiComponent.listAllRecords();
            uiComponent.recordFooter("TitleText", footerTitleText);
            uiComponent.showStatusMessage();
            System.out.print("Book ID [q to Quit]:");
            // Input
            bookId = sc.nextLine();
            if (bookId.equalsIgnoreCase("q")) return;
            else if (bookId.isBlank()) {     
                uiComponent.setStatusMessage("*Please fill the input");
                continue;
            }
            Book book = service.getBookById(bookId); 
            if (book == null) {
                uiComponent.setStatusMessage("*There is no book ID " + bookId + " please try again"); 
                continue;
            }
            else break;
        }
        
        // Borrow book
        if (option.equals("BorrowBook")){
            boolean borrowStatus = service.borrowBook(customerId, bookId);
            Book book = service.getBookById(bookId);
            if (borrowStatus) uiComponent.setStatusMessage("*Borrow book seccessfully!");
            else if (!service.getBookById(bookId).isAvailable()) uiComponent.setStatusMessage("*Borrow book fail - The books are out of stock");
            else uiComponent.setStatusMessage("*Borrow book fail - this customer ID" + customerId + " is already borrow book ID "+ bookId);
        }
        // Return book
        if (option.equals("ReturnBook")){
            boolean returnStatus = service.returnBook(customerId, bookId);
            if (returnStatus) uiComponent.setStatusMessage("*Return book seccessfully!");
            else uiComponent.setStatusMessage("*There is no borrowing record of customer ID " +customerId+" with book ID "+bookId+" please try again");
        }
        // Display
        uiComponent.recordHeader("Menu", null);
        uiComponent.listAllRecords();
        uiComponent.recordFooter("TitleText", "Return book");
        uiComponent.showStatusMessage();
        System.out.print("[press anything to go back]:");
        sc.nextLine();
    }

    
    public void listAllRecordsOfCustomer(){
        while (true) {
            // Display
            uiComponent.recordHeader("Menu", null);
            uiComponent.listAllRecords();
            uiComponent.recordFooter("TitleText", "List customer records");
            uiComponent.showStatusMessage();
            System.out.print("Customer ID [q to Quit]:");
            // Input
            String id = sc.nextLine();
            if (id.equalsIgnoreCase("q")) break;
            else if (id.isBlank()) {    
                uiComponent.setStatusMessage("Please fill the input");
                continue;
            }
            Customer customer = service.getCustomerById(id);
            if (customer == null) {
                uiComponent.setStatusMessage("*There is no customer ID " + id + " please try again"); 
            }
            else {   
                uiComponent.setRecordsListingOption("ByCustomer");  
                uiComponent.setRecordsListingValue(id);
            }
        } 
    }
} 