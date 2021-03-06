package com.itaycohen.toptapper.db;

import com.itaycohen.toptapper.App;
import com.itaycohen.toptapper.models.Records;
import com.itaycohen.toptapper.models.UserModel;
import com.itaycohen.toptapper.db.dao.RecordsDao;
import com.itaycohen.toptapper.db.dao.UserDao;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserModel.class, Records.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public static AppDatabase getInstance() {
        if (instance == null){
            instance = Room.databaseBuilder(App.getAppContext(),
                    AppDatabase.class, "app_database")
                    .build();
        }
        return instance;
    }


    public abstract UserDao userDao();
    public abstract RecordsDao recordsDao();
}

