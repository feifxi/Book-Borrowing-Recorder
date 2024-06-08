package org.example.repository.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.example.domain.BorrowRecord;
import org.example.repository.BorrowRecordRepository;

public class InMemoryBorrowRecordRepository implements BorrowRecordRepository {
    private List<BorrowRecord> repo;
    private long nextRecordId = 1;

    public InMemoryBorrowRecordRepository(){this.repo = new ArrayList<>();}

    @Override
    public BorrowRecord get(String recordId) {
        return repo.stream().filter(r -> r.getRecordId().equals(recordId)).findFirst().orElse(null);
    }

    @Override
    public BorrowRecord create(String customerId, String bookId) {
        long isBorrowing = repo.stream()
                .filter(br -> (br.getReturnDate().equals("Not Returned") || br.getReturnDate().equals("Overdue"))
                        && br.getCustomerId().equals(customerId) && br.getBookId().equals(bookId)).count();
        if (isBorrowing > 0) return null; // ไม่ให้ยืมหนังสือซ้ำ
        String id = String.format("BR%04d", nextRecordId);
        BorrowRecord borrowRecord = new BorrowRecord(id,customerId,bookId);
        repo.add(borrowRecord);
        nextRecordId++;
        return borrowRecord;
    }

    @Override
    public boolean remove(BorrowRecord record) {
        return repo.remove(record);
    }

    @Override
    public boolean returnBook(BorrowRecord record) {
        record.setReturnDate();
        return true;
    }

    @Override
    public Stream<BorrowRecord> stream() {
        return repo.stream();
    }

    


}

