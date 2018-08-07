package com.udacity.baketime.baketime.misc;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;


public class MiscFunctions {

    public static final String RECIPE_DETAILS = "RECIPE_DETAILS";
    public static final String RECIPE_NAME = "recipe_name";
    public static final String URL_NOT_FOUND = "url_not_found";
    public static final String VIDEO_URL = "url_not_found";
    public static final String IS_TWO_PANE = "is_it_TwoPane";
    public static final String NUMBER_OF_STEPS = "is_it_TwoPane";
    public static final String EXTRA_WIDGET_DATA = "extra_data";
    public static final String WHICH_ACTIVITY = "which_activity";
    public static final String JSON_DATA = "json_data";


    public static final Integer PROGRESS_UNDEFINED = 0;
    public static final Integer PROGRESS_LOADING = 1;
    public static final Integer PROGRESS_SUCCESSFULL = 2;
    public static final Integer PROGRESS_FAILED = 3;

    public static final int MAIN_ACTIVITY = 1;
    public static final String INTENT_FROM_WIDGET = "data_from_widget";


    public static String CapitalizeString(String string) {
        StringBuilder sb = new StringBuilder(string);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }


    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static int convertDpIntoPx(Context mContext, float yourdpmeasure) {
        if (mContext == null) {
            return 0;
        }
        Resources r = mContext.getResources();
        return (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, yourdpmeasure, r.getDisplayMetrics());
    }


//    public static int calculateNoOfColumns(Context context) {
//        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
//        return (int) (dpWidth / 180);
//    }
//
//    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
//            throws Throwable {
//        Bitmap bitmap = null;
//        MediaMetadataRetriever mediaMetadataRetriever = null;
//        try {
//            mediaMetadataRetriever = new MediaMetadataRetriever();
//            if (Build.VERSION.SDK_INT >= 14)
//                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
//            else
//                mediaMetadataRetriever.setDataSource(videoPath);
//
//            bitmap = mediaMetadataRetriever.getFrameAtTime(10, MediaMetadataRetriever.OPTION_CLOSEST);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new Throwable(
//                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
//                            + e.getMessage());
//
//        } finally {
//            if (mediaMetadataRetriever != null) {
//                mediaMetadataRetriever.release();
//            }
//        }
//        return bitmap;
//    }
}
