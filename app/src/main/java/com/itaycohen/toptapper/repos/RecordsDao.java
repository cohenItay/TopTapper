package com.itaycohen.toptapper.repos;

import com.itaycohen.toptapper.models.Records;
import com.itaycohen.toptapper.models.UserModel;
import com.itaycohen.toptapper.models.room_helpers.DatabaseUtils;
import com.itaycohen.toptapper.models.room_helpers.UserAndRecords;
import com.itaycohen.toptapper.ui.models.Level;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public abstract class RecordsDao {

    @Delete
    public abstract void delete(Records records);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Completable insertOrUpdateAll(Records... records);

    @RawQuery
    public abstract Completable updateRecordForColumn(SupportSQLiteQuery query);

    public Completable updateRecordForLevel(UserModel user, Level level, int record) {
        String column = DatabaseUtils.getColumnForLevel(level);
        String query = "UPDATE records SET "+column+" = "+record+" WHERE user_id = "+user.id;
        return updateRecordForColumn(new SimpleSQLiteQuery(query));
    }

    @RawQuery
    public abstract Flowable<Integer> getBestRecordForLevel(SupportSQLiteQuery query);

    public Flowable<Integer> getBestRecordForLevel(Level level) {
        String column = DatabaseUtils.getColumnForLevel(level);
        String query = "SELECT MAX("+column+") FROM records";
        return getBestRecordForLevel(new SimpleSQLiteQuery(query));
    }


}
