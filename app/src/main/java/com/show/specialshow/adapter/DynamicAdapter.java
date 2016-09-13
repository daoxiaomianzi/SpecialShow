package com.show.specialshow.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.show.specialshow.model.CircleDynamicItem;

public class DynamicAdapter extends BaseAdapter{
	private Context mContext;
	private List<CircleDynamicItem> mList;
	
	public DynamicAdapter(Context context,List<CircleDynamicItem> list){
		mContext=context;
		mList=list;
	}

	@Override
	public int getCount() {
		if(mList!=null){
			return mList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(mList!=null){
			return mList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		if(mList!=null){
			return position;
		}
		return 0;
	}

	
	@Override
	public int getViewTypeCount() {
		return super.getViewTypeCount();
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
