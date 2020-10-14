package com.itaycohen.toptapper.ui.utils;

import android.content.res.Resources;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import androidx.annotation.ArrayRes;

public class Utils {

    public static <T, C extends  Collection<T>> List<T> getRandomItemsFromCollection(int forAmount, C collection) {
        List<T> copyCollection = new ArrayList<>(collection.size());
        copyCollection.addAll(collection);
        Collections.shuffle(copyCollection);
        return copyCollection.subList(0, forAmount);
    }

    public static <C extends List<Integer>> int[] listToArray(C list) {
        int[] arr = new int[list.size()];
        for (int i=0; i<list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    public static List<Integer> getListOfResourcesFor(Resources res, @ArrayRes int arrayRes) {
        TypedArray typedArray = res.obtainTypedArray(arrayRes);
        ArrayList<Integer> shapesResArr = new ArrayList<>();
        for (int i=0; i<typedArray.length(); i++) {
            int resId = typedArray.getResourceId(i, -1);
            if (resId != -1)
                shapesResArr.add(resId);
        }
        typedArray.recycle();
        return shapesResArr;
    }
}
