package com.tuankhai.travelassistants.utils;

import com.tuankhai.travelassistants.webservice.main.RequestService;

/**
 * Created by Khai on 30/08/2017.
 */

public class AppContansts {
    public static final String SHAREDPRE_FILE = "com.tuankhai.travelassistants.sharedpre";
    public static final String SHAREDPRE_ALLPROVINCE = "com.tuankhai.travelassistants.sharedpre.allprovince";
    public static final String SHAREDPRE_SLIDERPLACE = "com.tuankhai.travelassistants.sharedpre.sliderplace";

    public static final String INTENT_TYPE = "com.tuankhai.travelassistants.intent.type";
    public static final String INTENT_NAME = "com.tuankhai.travelassistants.intent.name";
    public static final String INTENT_DATA = "com.tuankhai.travelassistants.intent.data";
    public static final String INTENT_DATA1 = "com.tuankhai.travelassistants.intent.data1";
    public static final String INTENT_DATA2 = "com.tuankhai.travelassistants.intent.data2";
    public static final String INTENT_DATA3 = "com.tuankhai.travelassistants.intent.data3";
    public static final String INTENT_DATA_LAT = "com.tuankhai.travelassistants.intent.lat";
    public static final String INTENT_DATA_LNG = "com.tuankhai.travelassistants.intent.lng";
    public static final String INTENT_IMAGE = "com.tuankhai.travelassistants.intent.image";

    public static final int INTENT_TYPE_NORMAL = 0;
    public static final int INTENT_TYPE_PROVINCE = 1;
    public static final int INTENT_TYPE_SEA = 2;
    public static final int INTENT_TYPE_ATTRACTIONS = 3;
    public static final int INTENT_TYPE_CULTURAL = 4;
    public static final int INTENT_TYPE_ENTERTAINMENT = 5;
    public static final int INTENT_TYPE_SPRING = 6;
    public static final int INTENT_TYPE_SUMMER = 7;
    public static final int INTENT_TYPE_AUTUMN = 8;
    public static final int INTENT_TYPE_WINNER = 9;
    public static final int INTENT_TYPE_FAVORITE = 10;
    public static final int INTENT_TYPE_ATM = 11;
    public static final int INTENT_TYPE_RESTAURANT = 12;
    public static final int INTENT_TYPE_HOTEL = 13;
    public static final String KEY_SEARCH_FRAGMENT = "com.tuankhai.travelassistants.fragment.searchplace";
    public static final String KEY_PLACE_FRAGMENT = "com.tuankhai.travelassistants.fragment.place";
    public static final int PERMISSIONS_REQUEST_LOCATION = 543;
    public static final int REQUEST_CHECK_SETTINGS = 2000;
    public static final int PLAY_SERVICES_REQUEST = 1000;

    public static String URL_IMAGE = RequestService.BASE_URL + "TravelAssistants/public/image/";
    public static String IMAGE_EXTENSION = ".jpg";
    public static String IMAGE_RATIO_3_4 = "_3_4" + IMAGE_EXTENSION;
    public static String IMAGE_RATIO_4_3 = "_4_3" + IMAGE_EXTENSION;
    public static String IMAGE_RATIO_16_9 = "_16_9" + IMAGE_EXTENSION;

    public static int RC_REAUTHORIZE = 111;
    public static final int RC_AUTHORIZE_CONTACTS = 112;
    public static int REQUEST_LOCATION = 113;
}
