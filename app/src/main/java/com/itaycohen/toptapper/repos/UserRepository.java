package com.itaycohen.toptapper.repos;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.itaycohen.toptapper.App;
import com.itaycohen.toptapper.db.AppDatabase;
import com.itaycohen.toptapper.db.dao.UserDao;
import com.itaycohen.toptapper.models.UserModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserRepository {

    private static UserRepository instance;
    private static final String USER_FILE_NAME = "userFffs";
    private static final String USER_MODEL_KEY = "UsermOdeKe";
    private UserModel userModel;
    private SharedPreferences sharedPrefs ;
    private UserDao userDao = AppDatabase.getInstance().userDao();

    public static UserRepository getInstance() {
        if (instance == null)
            instance = new UserRepository();
        return instance;
    }

    public void init(Context appContext) {
        sharedPrefs = appContext.getSharedPreferences(USER_FILE_NAME, Context.MODE_PRIVATE);
        String activeUserName = sharedPrefs.getString(USER_MODEL_KEY, "");
        assert activeUserName != null;
        if (!activeUserName.isEmpty()) {
            Disposable disposable = userDao.findById(activeUserName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(user -> this.userModel = user);
        }
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void onNewUserSet(UserModel userModel) {
        Disposable disposable = userDao.insertAll(userModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.i("tag", "persistNewRecord: The new record has been saved");
                    sharedPrefs.edit().putString(USER_MODEL_KEY, userModel.userName).apply();
                });
        this.userModel = userModel;
    }
}
