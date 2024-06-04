package repository;

import java.util.stream.Stream;
import domain.BorrowRecord;


public interface BorrowRecordRepository {
    public BorrowRecord get(String recordId);
    public BorrowRecord create(String customerId, String bookId);
    public Stream<BorrowRecord> stream(); 
}
