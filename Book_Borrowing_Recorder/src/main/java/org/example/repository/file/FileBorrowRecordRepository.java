package org.example.repository.file;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import org.example.domain.BorrowRecord;
import org.example.repository.BorrowRecordRepository;

public class FileBorrowRecordRepository implements BorrowRecordRepository {
    private final String filename = FilePATH.path + "\\G25RecordFile.txt";
    private List<BorrowRecord> repo;
    private long nextRecordId;

    public FileBorrowRecordRepository(){
        File file = new File(filename);
        if (file.exists()){
            try (
                    InputStream fis = new FileInputStream(filename);
                    InputStream bi = new BufferedInputStream(fis);
                    ObjectInputStream oi = new ObjectInputStream(bi);
            ) {
                nextRecordId = oi.readLong();
                repo = (List<BorrowRecord>)oi.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            nextRecordId = 1;
            this.repo = new ArrayList<>();
        }
    }

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
        BorrowRecord borrowRecord = new BorrowRecord(id,customerId,bookId,LocalDate.now(),null);
        repo.add(borrowRecord);
        nextRecordId++;
        try (
                OutputStream fo = new FileOutputStream(filename);
                OutputStream bo = new BufferedOutputStream(fo);
                ObjectOutputStream oo = new ObjectOutputStream(bo);
        ) {
            oo.writeLong(nextRecordId);
            oo.writeObject(repo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return borrowRecord;
    }

    @Override
    public boolean remove(BorrowRecord record) {
        boolean removeStatus = repo.remove(record);
        try (
                OutputStream fo = new FileOutputStream(filename);
                OutputStream bo = new BufferedOutputStream(fo);
                ObjectOutputStream oo = new ObjectOutputStream(bo);
        ) {
            oo.writeLong(nextRecordId);
            oo.writeObject(repo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return removeStatus;
    }

    @Override
    public boolean returnBook(BorrowRecord record) {
        record.setReturnDate();
        try (
                OutputStream fo = new FileOutputStream(filename);
                OutputStream bo = new BufferedOutputStream(fo);
                ObjectOutputStream oo = new ObjectOutputStream(bo);
        ) {
            oo.writeLong(nextRecordId);
            oo.writeObject(repo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    @Override
    public Stream<BorrowRecord> stream() {
        return repo.stream();
    }

}
