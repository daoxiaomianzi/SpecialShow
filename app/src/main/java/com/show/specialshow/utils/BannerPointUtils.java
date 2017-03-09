package com.show.specialshow.utils;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.show.specialshow.R;

import java.util.ArrayList;

/**
 * Created by xuyong on 16/8/11.
 */
public class BannerPointUtils {
    private Context mContext;
    private LinearLayout ll_point;
    private ArrayList<ImageView> pointviews = new ArrayList<>();

    public BannerPointUtils(Context mContext, LinearLayout ll_point, ArrayList<ImageView> pointviews) {
        this.mContext = mContext;
        this.ll_point = ll_point;
        this.pointviews = pointviews;
    }
    public void initPoint(int size) {
        ImageView imageView;
        if(ll_point!=null){
            ll_point.removeAllViews();
        }
        if(pointviews!=null){
            pointviews.clear();
        }
        for (int i = 0; i < size; i++) {
            imageView = new ImageView(mContext);
            imageView.setBackgroundResource(R.drawable.dot_normal);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(DensityUtil.dip2px(mContext,7),
                            DensityUtil.dip2px(mContext,7)));
            layoutParams.leftMargin = 10;
            layoutParams.rightMargin = 10;
            ll_point.addView(imageView, layoutParams);
            pointviews.add(imageView);
        }
    }

    public void draw_Point(int index) {
        for (int i = 0; i < pointviews.size(); i++) {
            pointviews.get(i).setImageResource(R.drawable.dot_normal);
        }
        if (pointviews.size() > index) {
            pointviews.get(index).setImageResource(R.drawable.dot_selected);
        }
    }
}
