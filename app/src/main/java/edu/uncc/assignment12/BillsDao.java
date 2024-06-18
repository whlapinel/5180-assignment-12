package edu.uncc.assignment12;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BillsDao {
    @Insert
    void insertAll(Bill... bills);

    @Update
    void updateBill(Bill bill);

    @Delete
    void deleteBill(Bill bill);

    @Query("SELECT * FROM bills")
    List<Bill> getAllBills();

    @Query("SELECT * FROM bills WHERE id = :id")
    Bill getBillById(int id);

    @Query("DELETE FROM bills")
    void deleteAll();
}
