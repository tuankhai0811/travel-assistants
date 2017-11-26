package com.tuankhai.travelassistants.customTab;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.content.ContextCompat;

import com.tuankhai.travelassistants.R;

import java.util.List;

/**
 * Created by khai.it on 25/11/2017.
 */

public class CustomTabActivityHelper implements ServiceConnectionCallback {

    private CustomTabsSession mCustomTabsSession;
    private CustomTabsClient mCustomTabsClient;
    private CustomTabsServiceConnection mConnection;
    private CustomTabConnectionCallback mConnectionCallback;

    /**
     * Interface to handle cases where the chrome custom tab cannot be opened.
     */
    public interface CustomTabFallback {
        void openUri(Context context, Uri uri);
    }

    /**
     * Interface to handle connection callbacks to the custom tab. We'll use this to handle UI changes
     * when the connection is either connected or disconnected.
     */
    public interface CustomTabConnectionCallback {
        void onCustomTabConnected();

        void onCustomTabDisconnected();
    }


    /**
     * Utility method for opening a custom tab
     *
     * @param activity         Host activity
     * @param customTabsIntent custom tabs intent
     * @param uri              uri to open
     * @param fallback         fallback to handle case where custom tab cannot be opened
     */
    public static void openCustomTab(Context context, CustomTabsIntent customTabsIntent,
                                     Uri uri, CustomTabFallback fallback) {
        String packageName = CustomTabsHelper.getPackageNameToUse(context);

        if (packageName == null) {
            // no package name, means there's no chromium browser.
            // Trigger fallback
            if (fallback != null) {
                fallback.openUri(context, uri);
            }
        } else {
            // set package name to use
            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.launchUrl(context, uri);
        }
    }

    public static void openCustomTab(Context context, Uri uri) {
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
        // set toolbar colors
        intentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

        // add menu items
//        intentBuilder.addMenuItem(getString(R.string.title_menu_1),
//                createPendingIntent(ChromeTabActionBroadcastReceiver.ACTION_MENU_ITEM_1));
//        intentBuilder.addMenuItem(getString(R.string.title_menu_2),
//                createPendingIntent(ChromeTabActionBroadcastReceiver.ACTION_MENU_ITEM_2));

        // set action button
//        intentBuilder.setActionButton(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), "Action Button",
//                createPendingIntent(ChromeTabActionBroadcastReceiver.ACTION_ACTION_BUTTON));

        // set start and exit animations
//        intentBuilder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
//        intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        // build custom tabs intent
        CustomTabsIntent customTabsIntent = intentBuilder.build();

        // call helper to open custom tab
        CustomTabActivityHelper.openCustomTab(context, customTabsIntent, uri, new CustomTabActivityHelper.CustomTabFallback() {
            @Override
            public void openUri(Context context, Uri uri) {

            }
        });
    }


    /**
     * Binds the activity to the custom tabs service.
     *
     * @param activity activity to be "bound" to the service
     */
    public void bindCustomTabsService(Activity activity) {
        if (mCustomTabsClient != null) return;

        String packageName = CustomTabsHelper.getPackageNameToUse(activity);
        if (packageName == null) return;

        mConnection = new ServiceConnection(this);
        CustomTabsClient.bindCustomTabsService(activity, packageName, mConnection);
    }

    /**
     * Unbinds the activity from the custom tabs service
     *
     * @param activity
     */
    public void unbindCustomTabsService(Activity activity) {
        if (mConnection == null) return;
        activity.unbindService(mConnection);
        mCustomTabsClient = null;
        mCustomTabsSession = null;
        mConnection = null;
    }

    /**
     * Creates or retrieves an exiting CustomTabsSession.
     *
     * @return a CustomTabsSession.
     */
    public CustomTabsSession getSession() {
        if (mCustomTabsClient == null) {
            mCustomTabsSession = null;
        } else if (mCustomTabsSession == null) {
            mCustomTabsSession = mCustomTabsClient.newSession(null);
        }
        return mCustomTabsSession;
    }

    /**
     * Register a Callback to be called when connected or disconnected from the Custom Tabs Service.
     *
     * @param connectionCallback
     */
    public void setConnectionCallback(CustomTabConnectionCallback connectionCallback) {
        this.mConnectionCallback = connectionCallback;
    }

    /**
     * @return true if call to mayLaunchUrl was accepted.
     * @see {@link CustomTabsSession#mayLaunchUrl(Uri, Bundle, List)}.
     */
    public boolean mayLaunchUrl(Uri uri, Bundle extras, List<Bundle> otherLikelyBundles) {
        if (mCustomTabsClient == null) return false;

        CustomTabsSession session = getSession();
        if (session == null) return false;

        return session.mayLaunchUrl(uri, extras, otherLikelyBundles);
    }

    @Override
    public void onServiceConnected(CustomTabsClient client) {
        mCustomTabsClient = client;
        mCustomTabsClient.warmup(0L);
        if (mConnectionCallback != null) {
            mConnectionCallback.onCustomTabConnected();
        }
    }

    @Override
    public void onServiceDisconnected() {
        mCustomTabsClient = null;
        mConnection = null;
        if (mConnectionCallback != null) {
            mConnectionCallback.onCustomTabDisconnected();
        }
    }
}

