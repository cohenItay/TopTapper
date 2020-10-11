package com.itaycohen.toptapper.repos;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.itaycohen.toptapper.App;
import com.itaycohen.toptapper.GsonContainer;
import com.itaycohen.toptapper.models.UserModel;

public class UserRepository {

    private static UserRepository instance;
    private static final String USER_FILE_NAME = "userFffs";
    private static final String USER_MODEL_KEY = "UsermOdeKe";
    private final Gson mGson;
    private UserModel userModel;
    private SharedPreferences sharedPrefs ;

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

    public void setUserModel(UserModel userModel) {
        String userJson = mGson.toJson(userModel);
        sharedPrefs.edit().putString(USER_MODEL_KEY, userJson).apply();
        this.userModel = userModel;
    }
}
