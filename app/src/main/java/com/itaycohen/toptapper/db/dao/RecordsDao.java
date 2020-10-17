package com.itaycohen.toptapper.db.dao;

import com.itaycohen.toptapper.models.Records;
import com.itaycohen.toptapper.models.UserModel;
import com.itaycohen.toptapper.ui.models.Level;

import java.util.List;
import java.util.concurrent.Callable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public abstract class RecordsDao {

    @Delete
    public abstract void delete(Records records);

    @Query("SELECT * FROM records")
    public abstract List<Records> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long[] insertOrUpdateAll(Records... records);

    @Query("SELECT * FROM records WHERE owner_name =:ownerName")
    public abstract Records getRecords(String ownerName);

    @Query("SELECT beginner FROM records WHERE owner_name =:ownerName")
    public abstract Flowable<Integer> getBestRecordForBeginner(String ownerName);

    @Query("SELECT MAX(intermediate) FROM records WHERE owner_name =:ownerName")
    public abstract Flowable<Integer> getBestRecordForIntermediate(String ownerName);

    @Query("SELECT MAX(expert) FROM records WHERE owner_name =:ownerName")
    public abstract Flowable<Integer> getBestRecordForExpert(String ownerName);




    public Completable insertOrUpdateRecordForLevel(UserModel user, Level level, int record) {
        return Completable.fromCallable((Callable<long[]>) () -> {
            Records records = getRecords(user.userName);
            records = records == null ? new Records(user.userName) : records;
            switch (level) {
                case BASIC:
                    records.beginnerModeRecord = record;
                    break;
                case INTERMEDIATE:
                    records.intermediateModeRecord = record;
                    break;
                case EXPERT:
                    records.expertModeRecord = record;
                    break;
            }
            return insertOrUpdateAll(records);
        });
    }

    public Flowable<Integer> getBestRecordForLevel(Level level, String ownerName) {
        Flowable<Integer> flowable;
        switch (level) {
            case BASIC:
                flowable = getBestRecordForBeginner(ownerName);
                break;
            case INTERMEDIATE:
                flowable = getBestRecordForIntermediate(ownerName);
                break;
            case EXPERT:
                flowable = getBestRecordForExpert(ownerName);
                break;
            default:
                throw new IllegalArgumentException("Must be a level");
        }
        return flowable;
    }
}
