package com.show.specialshow.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.show.specialshow.R;
import com.show.specialshow.model.TeShowActivitiesMess;

import java.util.List;

/**
 * Created by xuyong on 2016/10/18.
 */

public class OfficialEventAdapter extends BaseAdapter {
    private Context mContext;

    public OfficialEventAdapter(Context mContext, List<TeShowActivitiesMess> mList) {
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
            convertView=View.inflate(mContext, R.layout.item_official_event,null);
            viewHolder.iv_official_event= (ImageView)
                    convertView.findViewById(R.id.iv_official_event);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(mList.get(position).getPost_smeta(),
                viewHolder.iv_official_event);
        return convertView;
    }

    class ViewHolder{
        ImageView iv_official_event;
    }
}
