package com.show.specialshow.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.model.ShopComcardStaPicsMess;
import com.show.specialshow.utils.DensityUtil;

public class MyWorkAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ShopComcardStaPicsMess> myWorkPhoto;
	private int width;
	private int flag;
	
	public MyWorkAdapter(Context context, ArrayList<ShopComcardStaPicsMess> myWorkPhoto,int flag) {
		super();
		this.context = context;
		this.myWorkPhoto = myWorkPhoto;
		this.flag=flag;
	}

	@Override
	public int getCount() {
		return myWorkPhoto==null?0:myWorkPhoto.size();
	}

	@Override
	public Object getItem(int position) {
		return myWorkPhoto==null?null:myWorkPhoto.get(position);
	}

	@Override
	public long getItemId(int position) {
		return myWorkPhoto==null?0:position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(convertView==null){
			vh=new ViewHolder();
			convertView=View.inflate(context,R.layout.activity_work_item , null);
			vh.work_item_iv=(ImageView) convertView.findViewById(R.id.work_item_iv);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		width=TXApplication.WINDOW_WIDTH;
		switch (flag) {
		case 1:
			vh.work_item_iv.setLayoutParams(new LayoutParams((width-DensityUtil.dip2px(context, 10))/2, (width-DensityUtil.dip2px(context, 10))/2));
			ImageLoader.getInstance().displayImage(myWorkPhoto.get(position).getBig_pic(), vh.work_item_iv);
			break;
		case 2:
			vh.work_item_iv.setLayoutParams(new LayoutParams((width-DensityUtil.dip2px(context, 40))/3, (width-DensityUtil.dip2px(context, 40))/3));
			ImageLoader.getInstance().displayImage(myWorkPhoto.get(position).getBig_pic(), vh.work_item_iv);
			break;
		default:
			break;
		}
		return convertView;
	}
	
	class ViewHolder{
		ImageView work_item_iv;
	}

}
