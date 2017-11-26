package com.tuankhai.travelassistants.customTab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by khai.it on 25/11/2017.
 */

public class ChromeTabActionBroadcastReceiver extends BroadcastReceiver {
    public static final String KEY_ACTION_SOURCE = "com.tuankhai.travelassistants.ACTION_SOURCE";

    public static final int ACTION_MENU_ITEM_1 = 1;
    public static final int ACTION_MENU_ITEM_2 = 2;
    public static final int ACTION_ACTION_BUTTON = 3;


    @Override
    public void onReceive(Context context, Intent intent) {
        String data = intent.getDataString();

        if (data != null) {
            String toastText = getToastText(context, intent.getIntExtra(KEY_ACTION_SOURCE, -1), data);

            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
        }
    }


    private String getToastText(Context context, int actionSource, String message) {
//        switch (actionSource) {
//            case ACTION_MENU_ITEM_1:
//                return context.getString(R.string.toast_menu_1);
//            case ACTION_MENU_ITEM_2:
//                return context.getString(R.string.toast_menu_2);
//            case ACTION_ACTION_BUTTON:
//                return context.getString(R.string.text_action_button);
//            default:
//                return context.getString(R.string.unknown_action);
//        }
        return "";
    }
}

