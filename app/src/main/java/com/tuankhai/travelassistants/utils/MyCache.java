package com.tuankhai.travelassistants.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Khai on 22/09/2017.
 */

public class MyCache {
    private static MyCache myCache = new MyCache();
    private static LruCache<String, Bitmap> imgCache;

    public static String bg_place_global_4_3 = "bg_place_global_4_3";
    public static String icon_user_logo_review = "icon_user_logo";

//    private static LruCache<String, BaseFragment> fragmentCache;

    private MyCache() {
        imgCache = new LruCache<String, Bitmap>(Utils.getMaxSizeCache() / 16) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return super.sizeOf(key, value) / 1024;
            }
        };
//        fragmentCache = new LruCache<String, BaseFragment>(Utils.getMaxSizeCache() / 4) {
//            @Override
//            protected int sizeOf(String key, BaseFragment value) {
//                return super.sizeOf(key, value) / 1014;
//            }
//        };
    }

    public static MyCache getInstance() {
        return myCache;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            imgCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return imgCache.get(key);
    }

//    public void addFragmentToMemoryCache(String key, BaseFragment fragment) {
//        if (getFragmentFromMemCache(key) == null) {
//            fragmentCache.put(key, fragment);
//        }
//    }
//
//    public BaseFragment getFragmentFromMemCache(String key) {
//        return fragmentCache.get(key);
//    }
}
