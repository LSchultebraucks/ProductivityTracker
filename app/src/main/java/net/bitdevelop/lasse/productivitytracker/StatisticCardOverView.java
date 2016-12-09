package net.bitdevelop.lasse.productivitytracker;

import android.graphics.drawable.Drawable;

public class StatisticCardOverView {

    private String mName;
    private Drawable mDrawable;
    private Class mClass;


    public Class getActivity() {
        return mClass;
    }

    public void setClass(Class aClass) {
        mClass = aClass;
    }

    public StatisticCardOverView(String name, Drawable drawable, Class aClass) {
        mName = name;
        mDrawable = drawable;
        mClass = aClass;

    }

    public StatisticCardOverView(String name, Drawable drawable) {
        mName = name;
        mDrawable = drawable;

    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
    }
}
