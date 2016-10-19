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

import java.text.MessageFormat;
import java.util.List;

/**
 * Created by xuyong on 2016/10/18.
 */

public class TeShowActAdapter extends BaseAdapter {
    private Context mContext;

    public TeShowActAdapter(Context mContext, List<TeShowActivitiesMess> mList) {
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
            convertView = View.inflate(mContext, R.layout.item_te_show_activities, null);
            viewHolder.iv_item_activities_img = (ImageView)
                    convertView.findViewById(R.id.iv_item_activities_img);
            viewHolder.tv_item_activities_progress = (TextView)
                    convertView.findViewById(R.id.tv_item_activities_progress);
            viewHolder.tv_item_activities_title =
                    (TextView) convertView.findViewById(R.id.tv_item_activities_title);
            viewHolder.tv_item_activities_slogan =
                    (TextView) convertView.findViewById(R.id.tv_item_activities_slogan);
            viewHolder.tv_item_activities_is_free =
                    (TextView) convertView.findViewById(R.id.tv_item_activities_is_free);
            viewHolder.tv_item_activities_time =
                    (TextView) convertView.findViewById(R.id.tv_item_activities_time);
            viewHolder.tv_item_activities_address =
                    (TextView) convertView.findViewById(R.id.tv_item_activities_address);
            viewHolder.tv_item_activities_excerpt = (TextView)
                    convertView.findViewById(R.id.tv_item_activities_excerpt);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        bindData(viewHolder, position);
        return convertView;
    }

    private void bindData(ViewHolder viewHolder, int position) {
        ImageLoader.getInstance().displayImage(mList.get(position).getPost_smeta(), viewHolder.iv_item_activities_img);
        if (mList.get(position).isPost_isprogress()) {
            viewHolder.tv_item_activities_progress.setText("正在进行");
        } else {
            viewHolder.tv_item_activities_progress.setText("已结束");
            viewHolder.tv_item_activities_progress.setBackgroundResource(R.color.gary);
        }
        viewHolder.tv_item_activities_title.setText(mList.get(position).getPost_title());
        viewHolder.tv_item_activities_slogan.setText(mList.get(position).getPost_slogan());
        if (0 == mList.get(position).getPost_expense()) {
            viewHolder.tv_item_activities_is_free.setText("免费");
        } else {
            viewHolder.tv_item_activities_is_free.setText(mList.get(position).getPost_expense() + "元");
        }
        viewHolder.tv_item_activities_time.setText(MessageFormat.format(
                "时间:{0}", mList.get(position).getPost_active_time()
        ));
        viewHolder.tv_item_activities_address.setText(MessageFormat.format(
                "地址:{0}", mList.get(position).getPost_place()
        ));
        viewHolder.tv_item_activities_excerpt.setText(mList.get(position).getPost_excerpt());
    }

    class ViewHolder {
        ImageView iv_item_activities_img;//图片
        TextView tv_item_activities_progress;//活动状态
        TextView tv_item_activities_title;//标题
        TextView tv_item_activities_slogan;//副标题
        TextView tv_item_activities_is_free;//收费价格
        TextView tv_item_activities_time;//时间
        TextView tv_item_activities_address;//地点
        TextView tv_item_activities_excerpt;//活动简介
    }
}
