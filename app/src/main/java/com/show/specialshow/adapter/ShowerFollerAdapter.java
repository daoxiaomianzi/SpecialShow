package com.show.specialshow.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.model.ShowerFollowerCountMess;
import com.show.specialshow.utils.DensityUtil;
import com.show.specialshow.utils.ImageLoderutils;

public class ShowerFollerAdapter extends BaseAdapter {
	private Context context;
	private List<ShowerFollowerCountMess> showerFollerMess;
	private int flag;
	
	public ShowerFollerAdapter(Context context,
			List<ShowerFollowerCountMess> showerFollerMess,int flag) {
		super();
		this.context = context;
		this.showerFollerMess = showerFollerMess;
		this.flag=flag;
	}

	@Override
	public int getCount() {
		if(1==flag){
			return null==showerFollerMess?0:(showerFollerMess.size()<5?showerFollerMess.size():5);
		}else if(2==flag){
			return null==showerFollerMess?0:showerFollerMess.size();
		}else if(3==flag){
			return null==showerFollerMess?0:(showerFollerMess.size()<5?showerFollerMess.size():5);
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null==showerFollerMess?null:showerFollerMess.get(position);
	}

	@Override
	public long getItemId(int position) {
		return null==showerFollerMess?0:position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(null==convertView){
			vh=new ViewHolder();
			convertView=View.inflate(context, R.layout.item_shower_foller, null);
			vh.shower_foller_icon=(ImageView) convertView.findViewById(R.id.item_shower_foller_head_pro);
			vh.shower_foller_nickname=(TextView) convertView.findViewById(R.id.item_shower_foller_name);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
//		ImageLoderutils imageLoderutils=new ImageLoderutils(context);
		switch (flag) {
		case 1:
			vh.shower_foller_icon.setLayoutParams(new LayoutParams((TXApplication.WINDOW_WIDTH-DensityUtil.dip2px(context, 140))/5, (TXApplication.WINDOW_WIDTH-DensityUtil.dip2px(context, 140))/5));
//			imageLoderutils.display(vh.shower_foller_icon, showerFollerMess.get(position).getFav_icon());
			ImageLoader.getInstance().displayImage(showerFollerMess.get(position).getFav_icon(), vh.shower_foller_icon);
			vh.shower_foller_nickname.setVisibility(View.GONE);
			break;
		case 3:
			vh.shower_foller_icon.setLayoutParams(new LayoutParams(DensityUtil.dip2px(context, 40), DensityUtil.dip2px(context, 40)));
//			imageLoderutils.display(vh.shower_foller_icon, showerFollerMess.get(position).getFav_icon());
			ImageLoader.getInstance().displayImage(showerFollerMess.get(position).getFav_icon(), vh.shower_foller_icon);
			vh.shower_foller_nickname.setVisibility(View.GONE);
			break;
		case 2:
			vh.shower_foller_icon.setLayoutParams(new LayoutParams((TXApplication.WINDOW_WIDTH-DensityUtil.dip2px(context, 150))/3, (TXApplication.WINDOW_WIDTH-DensityUtil.dip2px(context, 150))/3));
//			imageLoderutils.display(vh.shower_foller_icon, showerFollerMess.get(position).getFav_icon());
			ImageLoader.getInstance().displayImage(showerFollerMess.get(position).getFav_icon(), vh.shower_foller_icon);
			vh.shower_foller_nickname.setText(showerFollerMess.get(position).getFav_name());
			break;

		default:
			break;
		}
		return convertView;
	}
	class ViewHolder{
		ImageView shower_foller_icon;
		TextView shower_foller_nickname;
	}
}
