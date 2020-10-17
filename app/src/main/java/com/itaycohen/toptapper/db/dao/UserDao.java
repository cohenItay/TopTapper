package com.itaycohen.toptapper.db.dao;

import com.itaycohen.toptapper.db.room_helpers.UserAndRecords;
import com.itaycohen.toptapper.models.UserModel;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public abstract class UserDao {
    @Query("SELECT * FROM users")
    public abstract List<UserModel> getAll();

    @Query("SELECT * FROM users WHERE user_name IN (:userNames)")
    public abstract List<UserModel> loadAllByIds(String[] userNames);

    @Query("SELECT * FROM users WHERE user_name LIKE :userName")
    public abstract Flowable<UserModel> findById(String userName);

    @Insert
    public abstract Completable insertAll(UserModel... users);

    @Delete
    public abstract void delete(UserModel user);

    @Transaction
    @Query("SELECT * FROM users")
    public abstract LiveData<List<UserAndRecords>> getUsersAndRecords();
}
