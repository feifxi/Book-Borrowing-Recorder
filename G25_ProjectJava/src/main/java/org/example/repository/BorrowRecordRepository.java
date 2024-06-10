package org.example.repository;

import java.util.stream.Stream;
import org.example.domain.BorrowRecord;


public interface BorrowRecordRepository {
    public BorrowRecord get(String recordId);
    public BorrowRecord create(String customerId, String bookId);
    public boolean remove(BorrowRecord record);
    public boolean returnBook(BorrowRecord record);
    public Stream<BorrowRecord> stream();
}
