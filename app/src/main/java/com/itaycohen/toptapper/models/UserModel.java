package com.itaycohen.toptapper.models;

import android.view.View;

import java.util.Objects;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserModel {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_name")
    public String userName;

    @ColumnInfo(name = "avatar_res")
    @DrawableRes
    public int avatarRes;

    public UserModel(
            @NonNull String userName,
            int avatarRes
    ) {
        Objects.requireNonNull(userName);
        if (avatarRes == View.NO_ID)
            throw new IllegalArgumentException("Must be a valid drawable resource");

        this.userName = userName;
        this.avatarRes = avatarRes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel userModel = (UserModel) o;
        return userName.equals(userModel.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }

    public static class Builder {

        private String userName;
        @DrawableRes
        private int avatarRes = View.NO_ID;

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder setAvatarRes(int avatarRes) {
            this.avatarRes = avatarRes;
            return this;
        }

        public UserModel build() {
            return new UserModel(userName, avatarRes);
        }

        public String getUserName() {
            return userName;
        }

        public int getAvatarRes() {
            return avatarRes;
        }
    }
}
