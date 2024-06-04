package main;

import java.time.LocalDate;

import domain.Book;
import domain.Customer;
import repository.memory.InMemoryBookRepository;
import repository.memory.InMemoryBorrowRecordRepository;
import repository.memory.InMemoryCustomerRepository;
import service.LibraryService;
import ui.LibraryUI;


public class App {
    public static void main(String[] args) {
        var app = new LibraryUI();
        app.start();
        
        // test();
        // testDate();  กลับมาดูว่ามี method อะไร
    }


    static void test(){
            LibraryService service = new LibraryService(
                new InMemoryBookRepository(), 
                new InMemoryCustomerRepository(), 
                new InMemoryBorrowRecordRepository()
                );

            // Create
            System.out.println("= Create & Get = \n");
            Book b1 = service.addNewBook("Monkey King1", "fei suchai", "helloworld",5);
            Book b2 = service.addNewBook("Monkey King1", "fei suchai", "helloworld",15);
            service.addNewBook("Monkey King2", "fei suchai", "helloworld",3);
            Customer c1 = service.addNewCustomer("jeno theadven", "theAdven@gmail.com");
            Customer c2 = service.addNewCustomer("jeno theadven", "gta555@gmail.com");
            service.addNewCustomer("oshimi ruksiam", "thechimi@gmail.com");
            System.out.println("b1 : " + b1);
            System.out.println("b2 : " + b2);
            System.out.println("b2 : " + c1);
            System.out.println("b2 : " + c2);
            // Get All
            service.getBooks().forEach(x -> System.out.println(x.getId() + "[Title: "+x.gettitle()+", Author: "+x.getAuthor()+", Avaliable: "+x.getAvailableCopies() + "]"));
            service.getCustomers().forEach(x -> System.out.println(x.getId()+"["+"Name: "+x.getName()+", Email: "+x.getEmail()+ "]"));
            // Get by ID
            System.out.println("Get ID B0001: " + service.getBookById("B0001"));
            System.out.println("Get ID C0002: " + service.getCustomerById("C0002"));
            // Check Record
            service.getRecords().forEach(x -> System.out.println(x.getBookId() + " = " + x.getCustomerId()));
            
            System.out.println("\n= Update =\n");
            // Update
            boolean bo1 = service.updateBook("B0001", "Monkey Lopburi","jeno","hehehe");
            boolean bo2 = service.updateBook("B000x", "Monkey Chengmai","jenox","hehehe");
            boolean bo3 = service.updateBook("B0001", 100);
            boolean bo4 = service.updateBook("B000x", 5000);
            boolean co1 = service.updateCustomer("C0001", "ter noob", "ter.gmail.com");
            boolean co2 = service.updateCustomer("C000x", "pj kuy", "pj.gmail.com");
            
            service.getBooks().forEach(x -> System.out.println(x.getId() + "[Title: "+x.gettitle()+", Author: "+x.getAuthor()+", Avaliable: "+x.getAvailableCopies() + "]"));
            service.getCustomers().forEach(x -> System.out.println(x.getId()+"["+"Name: "+x.getName()+", Email: "+x.getEmail()+ "]"));

            System.out.println("book1 : "+bo1);
            System.out.println("book2 : "+bo2);
            System.out.println("book3 : "+bo3);
            System.out.println("book4 : "+bo4);
            System.out.println("Cus1 : "+co1);
            System.out.println("Cus2 : "+co2);
            
            
            // Remove
            System.out.println("\n= Remove =\n");
            
            boolean ro1 = service.removeBook("B0002"); 
            boolean ro2 = service.removeBook("B000x");
            boolean ro3 = service.removeCustomer("C0002");
            boolean ro4 = service.removeCustomer("C000x");
            service.getBooks().forEach(x -> System.out.println(x.getId() + "[Title: "+x.gettitle()+", Author: "+x.getAuthor()+", Avaliable: "+x.getAvailableCopies() + "]"));
            service.getCustomers().forEach(x -> System.out.println(x.getId()+"["+"Name: "+x.getName()+", Email: "+x.getEmail()+ "]"));

            System.out.println("removeB1 : "+ro1);
            System.out.println("removeB2 : "+ro2);
            System.out.println("removeCus1 : "+ro3);
            System.out.println("removeCus2 : "+ro4);
            
            // Remove All
            System.out.println("\n= Remove All =\n");
            
            service.addNewBook("Monkey1", "fei", "f",5);
            service.addNewBook("Monkey2", "fei", "f",5);
            service.addNewBook("Monkey3", "fei", "f",5);
            service.getBooks().forEach(x -> System.out.println(x.getId() + "[Title: "+x.gettitle()+", Author: "+x.getAuthor()+", Avaliable: "+x.getAvailableCopies() + "]"));

            System.out.println(" After Remove All");
            service.removeAllBook();
            service.getBooks().forEach(x -> System.out.println(x.getId() + "[Title: "+x.gettitle()+", Author: "+x.getAuthor()+", Avaliable: "+x.getAvailableCopies() + "]"));


            // Fuction : Borrow & Return
            System.out.println("\n== Fuction : Borrow & Return ==\n");

            service = new LibraryService(
                new InMemoryBookRepository(), 
                new InMemoryCustomerRepository(), 
                new InMemoryBorrowRecordRepository()
                );

            service.addNewBook("Monkey King1", "fei suchai", "helloworld",3);
            service.addNewBook("Monkey King2", "fei suchai", "helloworld",1);
            service.addNewCustomer("jeno theadven", "gta555@gmail.com");
            service.addNewCustomer("ocshimi riksiam", "chimi555@gmail.com");
            
            // List ALl
            System.out.println("=== List All Book & Customer ===");
            service.getBooks().forEach(x -> System.out.println(x.getId() + "[Title: "+x.gettitle()+", Author: "+x.getAuthor()+", Avaliable: "+x.getAvailableCopies() + "]"));
            service.getCustomers().forEach(x -> System.out.println(x.getId()+"["+"Name: "+x.getName()+", Email: "+x.getEmail()+ "]"));

            boolean brw1 = service.borrowBook("C0001", "B0002");
            boolean brw2 = service.borrowBook("C000x", "B0001");    // ไม่มี customer หรือ book
            boolean brw3 = service.borrowBook("C0001", "B0002");    // ไม่ใหยืมหนังสือซ้ำเว้ยจนกว่าจะคืน
            boolean brw4 = service.borrowBook("C0002", "B0001");    
            boolean brw5 = service.borrowBook("C0002", "B0002");    // หนังสือหมด 
            boolean brw6 = service.borrowBook("C0001", "B0001");     


            System.out.println("=== List Record ===");

            service.getRecords()
            .forEach(x -> System.out.println(
                x.getRecordId()
                +"[CustomerId: "
                +x.getCustomerId() 
                + "- bookID: " 
                + x.getBookId() +", Borrow: "
                + x.getBorrowDate() 
                + ", Return: " +
                x.getReturnDate()+"]")
                );


            System.out.println("=== List All Book & Customer After ===");
            service.getBooks().forEach(x -> System.out.println(x.getId() + "[Title: "+x.gettitle()+", Author: "+x.getAuthor()+", Avaliable: "+x.getAvailableCopies() + "]"));
            service.getCustomers().forEach(x -> System.out.println(x.getId()+"["+"Name: "+x.getName()+", Email: "+x.getEmail()+ "]"));
            
            System.out.println("brw1 :"+ brw1);
            System.out.println("brw2 :"+ brw2);
            System.out.println("brw3 :"+ brw3);
            System.out.println("brw4 :"+ brw4);
            System.out.println("brw5 :"+ brw5);
            System.out.println("brw5 :"+ brw6);

            boolean rt1 = service.returnBook("C0001", "B0002");
            boolean rt2 = service.returnBook("C0002", "B0001");
            boolean rt3 = service.returnBook("C0001", "B0003"); // Book or customer ไม่มีการยืม
            

            System.out.println("\n === List Record After Runturned ===\n"); 

            service.getRecords()
            .forEach(x -> System.out.println(
                x.getRecordId()
                +"[CustomerId: "
                +x.getCustomerId() 
                + "- bookID: " 
                + x.getBookId() +", Borrow: "
                + x.getBorrowDate() 
                + ", Return: " +
                x.getReturnDate()+"]")
                );

            System.out.println("rt1 :"+rt1);
            System.out.println("rt2 :"+rt2);
            System.out.println("rt3 :"+rt3);
    }   

    static void testDate(){
        System.out.println(LocalDate.now());

    }
}
