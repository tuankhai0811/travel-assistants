package com.tuankhai.travelassistants.webservice;

import android.app.Activity;
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

    public static Dialog createDialog(Activity activity) {
        Dialog dialog = new Dialog(activity, Utils.getAnimDialog(activity));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.content_dialog_progress_webservice);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

    public boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
