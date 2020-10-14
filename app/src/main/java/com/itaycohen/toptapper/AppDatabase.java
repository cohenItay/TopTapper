package com.itaycohen.toptapper;

import com.itaycohen.toptapper.models.UserModel;
import com.itaycohen.toptapper.repos.RecordsDao;
import com.itaycohen.toptapper.repos.UserDao;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public static AppDatabase getInstance() {
        if (instance == null){
            instance = Room.databaseBuilder(App.getAppContext(),
                    AppDatabase.class, "app_database").build();
        }
        return instance;
    }


    public abstract UserDao userDao();
    public abstract RecordsDao recordsDao();
}

