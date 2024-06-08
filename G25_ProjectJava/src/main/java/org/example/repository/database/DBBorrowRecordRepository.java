package org.example.repository.database;

import org.example.domain.BorrowRecord;
import org.example.repository.BorrowRecordRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DBBorrowRecordRepository implements BorrowRecordRepository {
    private List<BorrowRecord> repo;
    private long nextRecordId;

    public DBBorrowRecordRepository(){
        try {
            Connection conn = DatabaseConnection.conenct();
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM borrowrecords");
            ResultSet results = preparedStatement.executeQuery();
            repo = new ArrayList<>();
            int count = 0;
            while (results.next()){
                count++;
                String id = results.getString(1);
                String cid = results.getString(2);
                String bid = results.getString(3);
                LocalDate borrowDate = results.getDate(4).toLocalDate();
                LocalDate returnDate = results.getDate(5) != null ? results.getDate(5).toLocalDate() : null;
                repo.add(new BorrowRecord(id,cid,bid,borrowDate,returnDate));
            }
            if (count == 0){        // ไม่มีข้อมูลใน Database
                nextRecordId = 1;
            }
            else {
                BorrowRecord lastRecord = repo.get(count-1);
                String numberStr = lastRecord.getRecordId().replace("BR","");
                nextRecordId = (Integer.parseInt(numberStr)) + 1;
            }
            conn.close();   //////////////////////////////////////////////////////////////////////////////////////
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        BorrowRecord borrowRecord = new BorrowRecord(id,customerId,bookId);
        repo.add(borrowRecord);
        nextRecordId++;
        try {
            Connection conn = DatabaseConnection.conenct();
            String sql = "INSERT INTO borrowrecords (id,cid,bid,borrowDate,returnDate) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,id);
            preparedStatement.setString(2,customerId);
            preparedStatement.setString(3,bookId);
            preparedStatement.setDate(4,Date.valueOf(LocalDate.now()));
            preparedStatement.setDate(5,null);
            int result = preparedStatement.executeUpdate();
            if (result == 0) return null;
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return borrowRecord;
    }

    @Override
    public boolean remove(BorrowRecord record) {
        boolean removeStatus = repo.remove(record);
        if (removeStatus) {
            try {
                Connection conn = DatabaseConnection.conenct();
                String sql = "DELETE FROM borrowrecords WHERE id=?";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, record.getRecordId());
                int result = preparedStatement.executeUpdate();
                if (result == 0) return false;
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return removeStatus;
    }
    
    @Override
    public boolean returnBook(BorrowRecord record) {
        record.setReturnDate();
        try {
            Connection conn = DatabaseConnection.conenct();
            String sql = "UPDATE borrowrecords SET returnDate=? WHERE id=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(2, record.getRecordId());
            preparedStatement.setDate(1, Date.valueOf(LocalDate.now()));
            int result = preparedStatement.executeUpdate();
            if (result == 0) return false;
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public Stream<BorrowRecord> stream() {
        return repo.stream();
    }

    
}
