package com.tuankhai.travelassistants.customTab;

import android.support.customtabs.CustomTabsClient;

/**
 * Created by khai.it on 25/11/2017.
 */

public interface ServiceConnectionCallback {
    /**
     * Called when the service is connected.
     * @param client a CustomTabsClient
     */
    void onServiceConnected(CustomTabsClient client);

    /**
     * Called when the service is disconnected.
     */
    void onServiceDisconnected();
}