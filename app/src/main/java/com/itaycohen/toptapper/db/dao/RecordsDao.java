package com.itaycohen.toptapper.db.dao;

import com.itaycohen.toptapper.models.Records;
import com.itaycohen.toptapper.models.UserModel;
import com.itaycohen.toptapper.ui.models.Level;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public abstract class RecordsDao {

    @Delete
    public abstract void delete(Records records);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Completable insertOrUpdateAll(Records... records);


    @Query("SELECT * FROM records WHERE user_id =:id")
    public abstract Flowable<Records> getRecord(int id);



    @Query("UPDATE records SET beginner = :score WHERE user_id =:id")
    public abstract Completable updateBeginnerRecord(int score, int id);

    @Query("UPDATE records SET intermediate = :score WHERE user_id =:id")
    public abstract Completable updateIntermediateRecord(int score, int id);

    @Query("UPDATE records SET expert = :score WHERE user_id =:id")
    public abstract Completable updateExpertRecord(int score, int id);

    @Query("SELECT beginner FROM records WHERE user_id =:id")
    public abstract Flowable<Integer> getBestRecordForBeginner(int id);

    @Query("SELECT MAX(intermediate) FROM records WHERE user_id =:id")
    public abstract Flowable<Integer> getBestRecordForIntermediate(int id);

    @Query("SELECT MAX(expert) FROM records WHERE user_id =:id")
    public abstract Flowable<Integer> getBestRecordForExpert(int id);


    @Query("SELECT beginner FROM records WHERE user_id =:id LIMIT 1")
    public abstract Flowable<Integer> getBestRecordForBeginner2(int id);



    public Completable updateRecordForLevel(UserModel user, Level level, int record) {
        Completable completable;

        switch (level) {
            case BASIC:
                completable = updateBeginnerRecord(record, user.id);
                break;
            case INTERMEDIATE:
                completable = updateIntermediateRecord(record, user.id);
                break;
            case EXPERT:
                completable = updateExpertRecord(record, user.id);
                break;
            default:
                throw new IllegalArgumentException("Must be a level");
        }
        return completable;
    }

    public Flowable<Integer> getBestRecordForLevel(Level level, int id) {
        Flowable<Integer> flowable;
        switch (level) {
            case BASIC:
                flowable = getBestRecordForBeginner(id);
                break;
            case INTERMEDIATE:
                flowable = getBestRecordForIntermediate(id);
                break;
            case EXPERT:
                flowable = getBestRecordForExpert(id);
                break;
            default:
                throw new IllegalArgumentException("Must be a level");
        }
        return flowable;
    }


}
