package com.itaycohen.toptapper.repos;

import com.itaycohen.toptapper.models.Records;
import com.itaycohen.toptapper.models.UserModel;
import com.itaycohen.toptapper.models.room_helpers.UserAndRecords;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public interface RecordsDao {

    @Delete
    void delete(Records records);

    @Insert
    void insertAll(Records... records);
}
