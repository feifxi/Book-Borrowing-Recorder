# INT103 Final Project

This project is the final project of the INT103 Course. It is a Java application designed with a text-based UI and implements multiple data storage methods, including in-memory storage, file storage, and database storage. This project uses a clean architecture design to ensure the separation of concerns.

## Functions

### Book Data Management

This function includes CRUD (Create, Read, Update, Delete) operations for managing book data. Users can:
- **Create**: Add new book records.
- **Read**: View existing book records.
- **Update**: Modify details of existing book records.
- **Delete**: Remove book records from the system.

### Customers Data Management

This function includes CRUD (Create, Read, Update, Delete) operations for managing customer data. Users can:
- **Create**: Add new customer records.
- **Read**: View existing customer records.
- **Update**: Modify details of existing customer records.
- **Delete**: Remove customer records from the system.

### Book Borrowing Recorder

This function records the borrowing activity of books by customers. It includes:
- **Create New Record**: When a customer borrows a book, a new record is created.
- **Record Details**: The borrowing record includes the book ID, customer ID, borrowing date, and return status.
