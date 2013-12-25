package com.git.programmerr47.testhflbjcrhjggkth.utils;

import android.content.Context;
import android.os.Build;
import android.view.ViewConfiguration;

public class AndroidUtils {

    public static boolean isThereASettingsButton(Context context) {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB_MR2 || (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2) && ViewConfiguration.get(context).hasPermanentMenuKey();
    }

}
