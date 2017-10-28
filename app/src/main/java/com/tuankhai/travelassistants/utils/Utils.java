package com.tuankhai.travelassistants.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.webservice.DTO.PlaceDTO;
import com.tuankhai.travelassistants.webservice.DTO.ProvinceDTO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Khai on 31/08/2017.
 */

public class Utils {

    public static <T> T readValue(byte[] data, Class<T> valueType) throws IOException {
        return getObjectMapper().readValue(data, valueType);
    }

    public static <T> T readValue(String data, Class<T> valueType) throws IOException {
        return getObjectMapper().readValue(data, valueType);
    }

    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        objectMapper.disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return objectMapper;
    }

    public static int getMaxSizeCache() {
        return (int) (Runtime.getRuntime().maxMemory() / 1024);
    }

    public static void saveAllProvince(Context context, ProvinceDTO data) {
        SharedPreferences preferences = context.getSharedPreferences(AppContansts.SHAREDPRE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString(AppContansts.SHAREDPRE_ALLPROVINCE, json);
        editor.commit();
    }

    public static ProvinceDTO getAllProvince(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(AppContansts.SHAREDPRE_FILE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(AppContansts.SHAREDPRE_ALLPROVINCE, "");
        if (Utils.isEmptyString(json)) return null;
        return gson.fromJson(json, ProvinceDTO.class);
    }

    public static long getLastTimeUpdate(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(AppContansts.SHAREDPRE_LASTTIME, Context.MODE_PRIVATE);
        return preferences.getLong(AppContansts.SHAREDPRE_LASTTIME, 0);
    }

    public static void saveLastTimeUpdate(Context context, long time) {
        SharedPreferences preferences = context.getSharedPreferences(AppContansts.SHAREDPRE_LASTTIME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(AppContansts.SHAREDPRE_LASTTIME, time);
        editor.commit();
    }

    public static String encodeToBase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static void saveSliderPlace(Context context, PlaceDTO data) {
        SharedPreferences preferences = context.getSharedPreferences(AppContansts.SHAREDPRE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString(AppContansts.SHAREDPRE_SLIDERPLACE, json);
        editor.commit();
    }

    public static PlaceDTO getSliderPlace(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(AppContansts.SHAREDPRE_FILE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(AppContansts.SHAREDPRE_SLIDERPLACE, "");
        if (Utils.isEmptyString(json)) return null;
        return gson.fromJson(json, PlaceDTO.class);
    }

    public static boolean isEmptyString(String string) {
        if (string == null || string.trim().equals("")) return true;
        return false;
    }

    public static boolean checkNull(Object object) {
        if (object == null) {
            return false;
        }
        return true;
    }

    public static String checkStringNull(String string) {
        if (string == null) {
            return "";
        }
        return string;
    }

    /**
     * Converting dp to pixel
     */
    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static String getDescriptionTime(String time) {
        long current = new Date().getTime();
        long change = (current - Long.parseLong(time)) / 1000;
        long value = (long) change / 60;
        if (value < 1) {
            return "vài giây trước";
        } else if (value < 2) {
            return "một phút trước";
        } else if (value < 3) {
            return "2 phút trước";
        } else if (value < 4) {
            return "3 phút trước";
        } else if (value < 6) {
            return "5 phút trước";
        } else if (value < 11) {
            return "10 phút trước";
        } else if (value < 31) {
            return "30 phút trước";
        }

        long hours = (long) value / 60;
        if (hours < 1) {
            return "trong giờ trước";
        } else if (hours < 2) {
            return "một giờ trước";
        } else if (hours < 3) {
            return "2 giờ trước";
        } else if (hours < 4) {
            return "3 giờ trước";
        } else if (hours < 6) {
            return "5 giờ trước";
        } else if (hours < 11) {
            return "10 giờ trước";
        }

        long days = (long) hours / 24;
        if (days < 1) {
            return "hôm qua";
        } else if (days < 2) {
            return "một ngày trước";
        } else if (days < 3) {
            return "2 ngày trước";
        } else if (days < 4) {
            return "3 ngày trước";
        }

        long week = (long) days / 7;
        if (week < 1) {
            return "trong tuần trước";
        } else if (week < 2) {
            return "một tuần trước";
        } else if (week < 3) {
            return "2 tuần trước";
        } else if (week < 4) {
            return "3 tuần trước";
        }

        long month = (long) week / 4;
        if (month < 1) {
            return "trong tháng trước";
        } else if (month < 2) {
            return "một tháng trước";
        } else if (month < 3) {
            return "2 tháng trước";
        } else if (month < 4) {
            return "3 tháng trước";
        } else if (month < 6) {
            return "5 tháng trước";
        } else if (month < 11) {
            return "10 tháng trước";
        }

        long year = (long) month / 12;
        if (year < 1) {
            return "trong năm trước";
        } else if (year < 2) {
            return "một năm trước";
        } else if (year < 3) {
            return "2 năm trước";
        }
        return "lâu lắm rồi";
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static void showSuccessToast(Context context, String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.toast_custom_green, null);
        ((ImageView) view.findViewById(R.id.toast_img)).setImageDrawable(context.getResources().getDrawable(R.drawable.ico_success));
        ((TextView) view.findViewById(R.id.toast_txv)).setText(text);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, Utils.dpToPx(context, 80));
        toast.setView(view);
        toast.show();
    }

    public static void showFaildToast(Context context, String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.toast_custom_green, null);
        ((ImageView) view.findViewById(R.id.toast_img)).setImageDrawable(context.getResources().getDrawable(R.drawable.ico_fail));
        ((TextView) view.findViewById(R.id.toast_txv)).setText(text);
        ((TextView) view.findViewById(R.id.toast_txv)).setTextColor(context.getResources().getColor(R.color.random_1));
        view.findViewById(R.id.layout).setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_round_red));

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, Utils.dpToPx(context, 80));
        toast.setView(view);
        toast.show();
    }
}
