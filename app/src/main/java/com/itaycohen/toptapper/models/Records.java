package com.itaycohen.toptapper.models;

import com.itaycohen.toptapper.ui.models.Level;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "records")
public class Records {

    @ColumnInfo(name = "owner_name")
    @PrimaryKey
    @NonNull
    public String ownerName;

    @ColumnInfo(name = "beginner")
    public int beginnerModeRecord;

    @ColumnInfo(name = "intermediate")
    public int intermediateModeRecord;

    @ColumnInfo(name = "expert")
    public int expertModeRecord;

    public Records(@NonNull String ownerName) {
        Objects.requireNonNull(ownerName);
        this.ownerName = ownerName;
    }

    public int getRecordForLevel(Level level) {
        switch (level) {
            case BASIC: return beginnerModeRecord;
            case INTERMEDIATE: return intermediateModeRecord;
            case EXPERT: return expertModeRecord;
            default: return -1;
        }
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
