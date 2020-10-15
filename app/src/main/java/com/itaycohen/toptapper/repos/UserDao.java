package com.itaycohen.toptapper.repos;

import com.itaycohen.toptapper.models.UserModel;
import com.itaycohen.toptapper.models.room_helpers.DatabaseUtils;
import com.itaycohen.toptapper.models.room_helpers.UserAndRecords;
import com.itaycohen.toptapper.ui.models.Level;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

@Dao
public abstract class UserDao {
    @Query("SELECT * FROM users")
    public abstract List<UserModel> getAll();

    @Query("SELECT * FROM users WHERE id IN (:userIds)")
    public abstract List<UserModel> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM users WHERE user_name LIKE :first")
    public abstract UserModel findByName(String first);

    @Insert
    public abstract void insertAll(UserModel... users);

    @Delete
    public abstract void delete(UserModel user);

    @Transaction
    @RawQuery
    public abstract LiveData<UserAndRecords> getUsersAndRecords(SupportSQLiteQuery query);

    LiveData<UserAndRecords> getUsersAndRecordsOrderBy(Level level) {
        String column = DatabaseUtils.getColumnForLevel(level);
        String query = "SELECT * FROM users ORDER BY " +column+ " DESC";
        return getUsersAndRecords(new SimpleSQLiteQuery(query));
    }
}
