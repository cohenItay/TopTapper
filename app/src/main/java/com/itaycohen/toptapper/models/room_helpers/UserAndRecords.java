package com.itaycohen.toptapper.models.room_helpers;

import com.itaycohen.toptapper.models.Records;
import com.itaycohen.toptapper.models.UserModel;

import androidx.room.Embedded;
import androidx.room.Relation;

public class UserAndRecords {
    @Embedded
    public UserModel userModel;
    @Relation(
            parentColumn = "id",
            entityColumn = "user_id"
    )
    public Records library;

}
