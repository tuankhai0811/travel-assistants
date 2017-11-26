package com.tuankhai.travelassistants.webservice;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.view.Window;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.utils.Utils;

/**
 * Created by Khai on 26/10/2017.
 */

public class UtilsService {

    private static Dialog dialog;

    public static Dialog getInstance(Context context){
        if (dialog == null){
            dialog = new Dialog(context.getApplicationContext(), Utils.getAnimDialog(context));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.content_dialog_progress_webservice);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        return dialog;
    }

    public boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
