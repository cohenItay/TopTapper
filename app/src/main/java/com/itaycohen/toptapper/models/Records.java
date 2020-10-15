package com.itaycohen.toptapper.models;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "records")
public class Records {

    @ColumnInfo(name = "user_id")
    @PrimaryKey
    @NonNull
    public String userId;

    @ColumnInfo(name = "beginner")
    public int beginnerModeRecord;

    @ColumnInfo(name = "intermediate")
    public int intermediateModeRecord;

    @ColumnInfo(name = "expert")
    public int expertModeRecord;

    public Records(String userId, int beginnerModeRecord, int intermediateModeRecord, int expertModeRecord) {
        this.userId = userId;
        this.beginnerModeRecord = beginnerModeRecord;
        this.intermediateModeRecord = intermediateModeRecord;
        this.expertModeRecord = expertModeRecord;
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
