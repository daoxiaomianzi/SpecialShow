package com.show.specialshow.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.show.specialshow.R;
import com.show.specialshow.model.TeShowActivitiesMess;

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
        return null==mList ? 0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null==mList ? null:mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return null==mList ? 0 :position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(null==convertView){
            viewHolder=new ViewHolder();
            convertView=View.inflate(mContext, R.layout.item_te_show_activities,null);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder{

    }
}
