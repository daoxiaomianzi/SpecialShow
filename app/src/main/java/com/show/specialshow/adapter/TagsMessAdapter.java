package com.show.specialshow.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.show.specialshow.R;
import com.show.specialshow.model.ShopListTagsMess;

/**
 * 标签适配器
 * @author admin
 */
public class TagsMessAdapter extends BaseAdapter{
	private List<ShopListTagsMess> mTagsMesses=new ArrayList<ShopListTagsMess>();
	private Context mContext;
	

	public TagsMessAdapter(List<ShopListTagsMess> mTagsMesses, Context mContext) {
		super();
		this.mTagsMesses = mTagsMesses;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return mTagsMesses==null?0:mTagsMesses.size();
	}

	@Override
	public Object getItem(int position) {
		return mTagsMesses==null?null:mTagsMesses.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mTagsMesses==null?0:position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViwHoldergv vhgv;
		if(null==convertView){
			vhgv=new ViwHoldergv();
			convertView=View.inflate(mContext, R.layout.item_tabs, null);
			vhgv.show_lang_item_label_tv=(TextView) convertView.findViewById(R.id.show_lang_item_label_tv);
			convertView.setTag(vhgv);
		}else{
			vhgv=(ViwHoldergv) convertView.getTag();
		}
		vhgv.show_lang_item_label_tv.setText("# "+mTagsMesses.get(position).getTag_name());
		return convertView;
	}
	class ViwHoldergv{
		TextView show_lang_item_label_tv;
	}
	
}
