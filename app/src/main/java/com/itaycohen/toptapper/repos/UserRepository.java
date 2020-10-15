package com.itaycohen.toptapper.repos;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.itaycohen.toptapper.App;
import com.itaycohen.toptapper.GsonContainer;
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
    private final Gson mGson;
    private UserModel userModel;
    private SharedPreferences sharedPrefs ;
    private UserDao userDao = AppDatabase.getInstance().userDao();

    public static UserRepository getInstance() {
        if (instance == null)
            instance = new UserRepository(App.getAppContext(), GsonContainer.get());
        return instance;
    }

    private UserRepository(Context appContext, Gson gson) {
        sharedPrefs = appContext.getSharedPreferences(USER_FILE_NAME, Context.MODE_PRIVATE);
        this.mGson = gson;
        String userJson = sharedPrefs.getString(USER_MODEL_KEY, null);
        if (userJson != null) {
            try {
                userModel = gson.fromJson(userJson, UserModel.class);
            } catch (JsonSyntaxException ignored) { }
        }
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void onNewUserSet(UserModel userModel) {
        String userJson = mGson.toJson(userModel);
        sharedPrefs.edit().putString(USER_MODEL_KEY, userJson).apply();

        Disposable disposable = userDao.insertAll(userModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.i("tag", "persistNewRecord: The new record has been saved"));
        this.userModel = userModel;
    }
}
