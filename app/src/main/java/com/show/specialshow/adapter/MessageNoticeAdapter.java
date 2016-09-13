package com.show.specialshow.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.show.specialshow.R;
import com.show.specialshow.model.MessageNoticeMess;

import java.util.List;

/**
 * Created by xuyong on 16/8/9.
 */
public class MessageNoticeAdapter extends BaseAdapter {
    private Context context;
    private List<MessageNoticeMess> mlist;

    public MessageNoticeAdapter(Context context, List<MessageNoticeMess> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    @Override
    public int getCount() {
        return null == mlist ? 0 : mlist.size();
    }

    @Override
    public Object getItem(int i) {
        return null == mlist ? null : mlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return null == mlist ? 0 : i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.activity_message_notice_item, null);
            viewHolder.message_notice_item_time = (TextView) convertView.findViewById(R.id.message_notice_item_time);
            viewHolder.message_notice_item_content = (TextView) convertView.findViewById(R.id.message_notice_item_content);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.message_notice_item_time.setText(mlist.get(position).getTime());
        viewHolder.message_notice_item_content.setText(mlist.get(position).getTitle());
        return convertView;
    }

    class ViewHolder {
        TextView message_notice_item_time;//消息时间
        TextView message_notice_item_content;//消息内容
    }
}
