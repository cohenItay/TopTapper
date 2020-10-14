package com.itaycohen.toptapper.models;

import java.util.Objects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "records")
public class Records {

    @ColumnInfo(name = "user_id")
    private String userId;

    @ColumnInfo(name = "beginner")
    private int beginnerModeRecord;

    @ColumnInfo(name = "intermediate")
    private int intermediateModeRecord;

    @ColumnInfo(name = "expert")
    private int expertModeRecord;

    public Records(String userId, int beginnerModeRecord, int intermediateModeRecord, int expertModeRecord) {
        this.userId = userId;
        this.beginnerModeRecord = beginnerModeRecord;
        this.intermediateModeRecord = intermediateModeRecord;
        this.expertModeRecord = expertModeRecord;
    }

    public int getBeginnerModeRecord() {
        return beginnerModeRecord;
    }

    public int getIntermediateModeRecord() {
        return intermediateModeRecord;
    }

    public int getExpertModeRecord() {
        return expertModeRecord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Records records = (Records) o;
        return beginnerModeRecord == records.beginnerModeRecord &&
                intermediateModeRecord == records.intermediateModeRecord &&
                expertModeRecord == records.expertModeRecord;
    }

    @Override
    public int hashCode() {
        return Objects.hash(beginnerModeRecord, intermediateModeRecord, expertModeRecord);
    }
}
