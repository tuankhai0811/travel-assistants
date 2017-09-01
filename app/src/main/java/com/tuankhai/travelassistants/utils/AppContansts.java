package com.tuankhai.travelassistants.utils;

import com.tuankhai.travelassistants.webservice.main.RequestService;

/**
 * Created by Khai on 30/08/2017.
 */

public class AppContansts {
    public static String URL_IMAGE = RequestService.BASE_URL + "TravelAssistants/public/image/";
    public static String IMAGE_EXTENSION = ".jpg";
    public static String IMAGE_RATIO_3_4 = "_3_4" + IMAGE_EXTENSION;
    public static String IMAGE_RATIO_4_3 = "_4_3" + IMAGE_EXTENSION;
    public static String IMAGE_RATIO_16_9 = "_16_9" + IMAGE_EXTENSION;

    public static int RC_REAUTHORIZE = 111;
    public static final int RC_AUTHORIZE_CONTACTS = 112;
    public static int REQUEST_LOCATION = 113;
}
