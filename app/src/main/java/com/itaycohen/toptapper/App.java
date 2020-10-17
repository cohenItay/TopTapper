package com.itaycohen.toptapper;

import android.app.Application;
import android.content.Context;

import com.itaycohen.toptapper.repos.UserRepository;

public class App extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        UserRepository.getInstance().init(appContext);
    }

    public static Context getAppContext() {
        return appContext;
    }
}
