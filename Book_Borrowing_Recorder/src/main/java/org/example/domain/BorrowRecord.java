package org.example.domain;


import java.io.Serializable;
import java.time.LocalDate;

public class BorrowRecord implements Serializable{
    private final String recordId;
    private String customerId;
    private String bookId;
    private LocalDate borrowDate;
    private LocalDate returnDate;

    public BorrowRecord(String recordId, String customerId, String bookId, LocalDate borrowDate, LocalDate returnDate) {
        this.recordId = recordId;
        this.customerId = customerId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public boolean isOverdue() {    // เช็คว่าเกินกำหนดคืนหนังสือหรือยัง (ภายใน 14 วัน นับจากวันยืม)
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

    @Override
    public String toString() {
        return this.recordId + "[CID: "+ this.customerId + ", BID: " + this.bookId + "]";
    }
}