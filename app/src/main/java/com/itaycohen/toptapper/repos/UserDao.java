package com.itaycohen.toptapper.repos;

import com.itaycohen.toptapper.models.UserModel;
import com.itaycohen.toptapper.models.room_helpers.UserAndRecords;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    List<UserModel> getAll();

    @Query("SELECT * FROM users WHERE id IN (:userIds)")
    List<UserModel> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM users WHERE user_name LIKE :first")
    UserModel findByName(String first, String last);

    @Insert
    void insertAll(UserModel... users);

    @Delete
    void delete(UserModel user);

    @Transaction
    @Query("SELECT * FROM users")
    public List<UserAndRecords> getUsersAndRecords();
}
