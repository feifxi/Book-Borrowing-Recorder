package domain;

import java.time.LocalDate;

public class BorrowRecord {
    private final String recordId;
    private String customerId;
    private String bookId;
    private LocalDate borrowDate;
    private LocalDate returnDate;

    public BorrowRecord(String recordId, String customerId, String bookId) {
        this.recordId = recordId;
        this.customerId = customerId;
        this.bookId = bookId;
        this.borrowDate = LocalDate.now();  
    }


    public boolean isOverdue() {    // เกินกำหนดคืนหรือยัง (ภายใน 14 วัน นับจากวันยืม)
        if (returnDate == null) {
            return borrowDate.plusDays(14).isBefore(LocalDate.now());
        }
        return false;
    }

    public String getRecordId() {
        return recordId;
    }
    public LocalDate getBorrowDate() {
        return borrowDate;
    }
    public String getReturnDate() {
        if (returnDate == null && isOverdue()) return "Overdue";
        else if (returnDate == null) return "Not Returned";
        return returnDate.toString();
    }
    public String getBookId() {
        return bookId;
    }
    public String getCustomerId() {
        return customerId;
    }

    public void setReturnDate() {
        this.returnDate = LocalDate.now();
    }


    
    // Testing overdue method   ////////////////////////////////////////////////////////////////////////////////
    public void setOverDueTesting() {
        this.borrowDate = LocalDate.now().minusDays(15);
    }
}