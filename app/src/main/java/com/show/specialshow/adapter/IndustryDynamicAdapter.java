package com.show.specialshow.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.show.specialshow.R;
import com.show.specialshow.model.TeShowActivitiesMess;

import java.util.List;

/**
 * Created by xuyong on 2016/10/18.
 */

public class IndustryDynamicAdapter extends BaseAdapter {
    private Context mContext;

    public IndustryDynamicAdapter(Context mContext, List<TeShowActivitiesMess> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    private List<TeShowActivitiesMess> mList;

    @Override
    public int getCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null == mList ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return null == mList ? 0 : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_industry_dynamic, null);
            viewHolder.iv_item_industry_dynamic_img =
                    (ImageView) convertView.findViewById(R.id.iv_item_industry_dynamic_img);
            viewHolder.tv_item_industry_dynamic_title =
                    (TextView) convertView.findViewById(R.id.tv_item_industry_dynamic_title);
            viewHolder.tv_item_industry_dynamic_intro =
                    (TextView) convertView.findViewById(R.id.tv_item_industry_dynamic_intro);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_item_industry_dynamic_title.setText(mList.get(position).getPost_title());
        ImageLoader.getInstance().displayImage(mList.get(position).getPost_smeta()
                , viewHolder.iv_item_industry_dynamic_img);
        viewHolder.tv_item_industry_dynamic_intro.setText(
                mList.get(position).getPost_excerpt()
        );
        return convertView;
    }

    class ViewHolder {
        ImageView iv_item_industry_dynamic_img;
        TextView tv_item_industry_dynamic_title;
        TextView tv_item_industry_dynamic_intro;
    }
}
