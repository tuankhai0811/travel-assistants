package com.tuankhai.travelassistants.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.DetailPlaceActivity;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.webservice.DTO.DetailPlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.PlaceDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;
import com.tuankhai.travelassistants.webservice.request.GetDetailPlaceRequest;

/**
 * Created by tuank on 05/04/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private String place = "";
    private String title = "";
    private int id = 123;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {
            Log.e("MessagingService", "receive: firebase");
            sendNotificationFirebase(remoteMessage);
        } else {
            Log.e("MessagingService", "receive: api");
            sendNotification(remoteMessage);
        }
    }

    private void sendNotificationFirebase(RemoteMessage remoteMessage) {
        this.place = remoteMessage.getData().get("id");
        this.title = remoteMessage.getData().get("title");
        getDetailPlace(place);
    }

    private void getDetailPlace(final String place) {
        new RequestService().load(
                new GetDetailPlaceRequest(place),
                false,
                new MyCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        super.onSuccess(response);
                        DetailPlaceDTO placeDTO = (DetailPlaceDTO) response;
                        if (placeDTO.isSuccess()) {
                            pushNotification(placeDTO.place);
                        }
                    }
                },
                DetailPlaceDTO.class
        );
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        this.place = remoteMessage.getData().get("id");
        this.title = remoteMessage.getData().get("title");
        getDetailPlace(place);
    }

    private void pushNotification(PlaceDTO.Place data) {
        Intent intent = new Intent(getApplicationContext(), DetailPlaceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra(AppContansts.INTENT_DATA, data);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setContentTitle(title.isEmpty() ? "Bạn đã đến đây chưa?" : title)
                .setContentText(data.long_name)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notificationBuilder.build());
    }
}