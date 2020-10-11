package com.itaycohen.toptapper;

import com.google.gson.Gson;

public class GsonContainer {

    private static Gson instance;

    public static Gson get() {
        if (instance == null)
            instance = new Gson();
        return instance;
    }
}
