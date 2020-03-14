package com.example.agriculture;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Random;

class Utils {

    public static final Random RANDOM=new Random();
    public static float convertDpToPixel(float dp, Context context){
        Resources resources=context.getResources();
        DisplayMetrics metrics=resources.getDisplayMetrics();
        float px=dp=((float)metrics.densityDpi/DisplayMetrics.DENSITY_DEFAULT);
        return px;

    }
    public static int randInt(int min,int max){
        int random=RANDOM.nextInt((max-min)+1)+min;
        return random;
    }
}
































