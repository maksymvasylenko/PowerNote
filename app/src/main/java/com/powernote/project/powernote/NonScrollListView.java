package com.powernote.project.powernote;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Maks on 13.10.2017.
 */

public class NonScrollListView extends ListView {

    public NonScrollListView(Context context) {
        super(context);
    }
    public NonScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public NonScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
        ViewGroup.LayoutParams params = getLayoutParams();
        int nuberOfItems = getCount();
        Log.e("nuberOfItems", "" + nuberOfItems + ":" + getDividerHeight());
        Log.e("height", "" + getHeight());
        Log.e("full height", "" + getMeasuredHeight());
        int h = getMeasuredHeight();
        Log.e("added full height", "" + h);
        params.height = h;
    }
}