package org.example.repository.database;

import org.example.domain.BorrowRecord;
import org.example.repository.BorrowRecordRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class DBBorrowRecordRepository implements BorrowRecordRepository {
    private Map<String,BorrowRecord> repo;
    private long nextRecordId;

    public DBBorrowRecordRepository(){
        String sqlAll = "SELECT * FROM borrowrecords";
        String sqlNextID = "SELECT MAX(id) FROM borrowrecords";
        try (
                Connection conn = DatabaseConnection.connect();
                PreparedStatement psAll = conn.prepareStatement(sqlAll);
                PreparedStatement psNextId = conn.prepareStatement(sqlNextID);
                ResultSet rsAll = psAll.executeQuery();
                ResultSet rsNextId = psNextId.executeQuery();
        ) {
            repo = new HashMap<>();
            // get nextId
            rsNextId.next();
            String lastId = rsNextId.getString(1);
            if (lastId != null){
                String idStr = lastId.replace("BR","");
                nextRecordId = (Integer.parseInt(idStr)) + 1;
                while (rsAll.next()){
                    String id = rsAll.getString(1);
                    String cid = rsAll.getString(2);
                    String bid = rsAll.getString(3);
                    LocalDate borrowDate = rsAll.getDate(4).toLocalDate();
                    LocalDate returnDate = rsAll.getDate(5) != null ? rsAll.getDate(5).toLocalDate() : null;
                    repo.put(id,new BorrowRecord(id,cid,bid,borrowDate,returnDate));
                }
            }
            else nextRecordId = 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BorrowRecord get(String recordId) {
        return repo.get(recordId);
    }

    @Override
    public BorrowRecord create(String customerId, String bookId) {
        long isBorrowing = repo.values().stream()
                .filter(br -> (br.getReturnDate().equals("Not Returned") || br.getReturnDate().equals("Overdue"))
                        && br.getCustomerId().equals(customerId) && br.getBookId().equals(bookId)).count();
        if (isBorrowing > 0) return null; // ไม่ให้ยืมหนังสือซ้ำ
        String id = String.format("BR%04d", nextRecordId);
        BorrowRecord borrowRecord = new BorrowRecord(id,customerId,bookId,LocalDate.now(),null);
        repo.put(id,borrowRecord);
        nextRecordId++;
        String sql = "INSERT INTO borrowrecords (id,cid,bid,borrowDate,returnDate) VALUES (?,?,?,?,?)";
        try (
            Connection conn = DatabaseConnection.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ){
            preparedStatement.setString(1,id);
            preparedStatement.setString(2,customerId);
            preparedStatement.setString(3,bookId);
            preparedStatement.setDate(4,Date.valueOf(LocalDate.now()));
            preparedStatement.setDate(5,null);
            int result = preparedStatement.executeUpdate();
            if (result == 0) return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return borrowRecord;
    }

    @Override
    public boolean remove(BorrowRecord record) {
        boolean removeStatus = repo.remove(record.getRecordId(),record);
        if (removeStatus) {
            String sql = "DELETE FROM borrowrecords WHERE id=?";
            try (
                Connection conn = DatabaseConnection.connect();
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                ){
                preparedStatement.setString(1, record.getRecordId());
                int result = preparedStatement.executeUpdate();
                if (result == 0) return false;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return removeStatus;
    }
    
    @Override
    public boolean returnBook(BorrowRecord record) {
        record.setReturnDate();
        String sql = "UPDATE borrowrecords SET returnDate=? WHERE id=?";
        try (
            Connection conn = DatabaseConnection.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ){
            preparedStatement.setString(2, record.getRecordId());
            preparedStatement.setDate(1, Date.valueOf(LocalDate.now()));
            int result = preparedStatement.executeUpdate();
            if (result == 0) return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public Stream<BorrowRecord> stream() {
        return repo.values().stream();
    }

    
}
