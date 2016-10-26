package com.show.specialshow.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.model.ShopPeopleMess;
import com.show.specialshow.utils.DensityUtil;
import com.show.specialshow.utils.ImageLoderutils;
import com.show.specialshow.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

public class SelectCraftsmanActivity extends BaseActivity {
	private GridView select_craftsman_gv;//
	private List<ShopPeopleMess> shopPeopleMesses=new ArrayList<ShopPeopleMess>();

	@SuppressWarnings("unchecked")
	@Override
	public void initData() {
		shopPeopleMesses=(List<ShopPeopleMess>) getIntent().getSerializableExtra("peopleList");
		setContentView(R.layout.activity_craftsman);
	}

	@Override
	public void initView() {
		select_craftsman_gv=(GridView) findViewById(R.id.craftsman_gv);
		select_craftsman_gv.setNumColumns(4);
		select_craftsman_gv.setAdapter(new CraftsmanAdapter());
	}

	@Override
	public void fillView() {
		head_title_tv.setText("选择手艺人");
	}

	@Override
	public void setListener() {
		
	}

	@Override
	public void onClick(View v) {
		
	}
	class CraftsmanAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return shopPeopleMesses==null?0:(shopPeopleMesses.size()>4?4:shopPeopleMesses.size());
		}

		@Override
		public Object getItem(int position) {
			return shopPeopleMesses==null?null:shopPeopleMesses.get(position);
		}

		@Override
		public long getItemId(int position) {
			return shopPeopleMesses==null?0:position;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHoldergv vhgv;
			if(convertView==null){
				vhgv=new ViewHoldergv();
				convertView=View.inflate(mContext, R.layout.activity_craftsman_item, null);
				vhgv.craftsm_head_pro=(ImageView) convertView.findViewById(R.id.craftsm_head_pro);
				vhgv.craftsm_job=(TextView) convertView.findViewById(R.id.craftsm_job);
				vhgv.craftsm_name=(TextView) convertView.findViewById(R.id.craftsm_name);
				vhgv.craftsm_moods=(TextView) convertView.findViewById(R.id.craftsm_moods);
				vhgv.craftsm_rl=(LinearLayout) convertView.findViewById(R.id.craftsm_rl);
				convertView.setTag(vhgv);
			}else{
				vhgv=(ViewHoldergv) convertView.getTag();
			}
			vhgv.craftsm_head_pro.setLayoutParams(new LinearLayout.LayoutParams((TXApplication.WINDOW_WIDTH- DensityUtil.dip2px(mContext,90))/4, (TXApplication.WINDOW_WIDTH-DensityUtil.dip2px(mContext,90))/4));
			ImageLoderutils imageLoderutils=new ImageLoderutils(mContext);
			imageLoderutils.display(vhgv.craftsm_head_pro, shopPeopleMesses.get(position).getChoice_artisans_icon());
			vhgv.craftsm_name.setText(shopPeopleMesses.get(position).getChoice_artisans_name());
			vhgv.craftsm_job.setText(shopPeopleMesses.get(position).getChoice_artisans_job());
			vhgv.craftsm_moods.setText(shopPeopleMesses.get(position).getChoice_artisans_hot()+"人气");
			vhgv.craftsm_rl.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent data =new Intent();
					data.putExtra("select_craftsman", shopPeopleMesses.get(position));
					UIHelper.setResult(mContext, -1, data);
				}
			});
			return convertView;
		}
		class ViewHoldergv{
			ImageView craftsm_head_pro;
			TextView craftsm_name;//手艺人名字
			TextView craftsm_job;//手艺人职位
			TextView craftsm_moods;//手艺人人气
			LinearLayout craftsm_rl;
		}
		
	}



}
