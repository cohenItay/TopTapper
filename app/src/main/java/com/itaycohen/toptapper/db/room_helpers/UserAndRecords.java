package com.itaycohen.toptapper.db.room_helpers;

import com.itaycohen.toptapper.models.Records;
import com.itaycohen.toptapper.models.UserModel;

import androidx.room.Embedded;
import androidx.room.Relation;

public class UserAndRecords {
    @Embedded
    public UserModel userModel;
    @Relation(
            parentColumn = "user_name",
            entityColumn = "owner_name"
    )
    public Records records;

}
