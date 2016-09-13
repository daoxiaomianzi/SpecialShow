package com.show.specialshow.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.model.ShopPeopleMess;
import com.show.specialshow.model.ShopServiceMess;
import com.show.specialshow.utils.ImageLoderutils;
import com.show.specialshow.utils.UIHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServiceListActivity extends BaseActivity {
	private ListView service_list_lv;//列表控件
	private String shoptitle;
	private String shop_id;
	private List<ShopServiceMess> servicelist=new ArrayList<ShopServiceMess>();
	private List<ShopPeopleMess> shopPeopleMesses;
	
	private Bundle bundle=new Bundle();

	@SuppressWarnings("unchecked")
	@Override
	public void initData() {
		shoptitle=getIntent().getStringExtra("shoptitle");
		shop_id=getIntent().getStringExtra("shop_id");
		servicelist=(List<ShopServiceMess>) getIntent().getSerializableExtra(CraftsmandetailsActivity.SERVICE_LIST);
		shopPeopleMesses=(List<ShopPeopleMess>) getIntent().getSerializableExtra(CraftsmandetailsActivity.PEOPLE_LIST);
		setContentView(R.layout.activity_service_list);
	}

	@Override
	public void initView() {
		service_list_lv=(ListView) findViewById(R.id.service_list_lv);
		service_list_lv.setAdapter(new ServiceListAdapter());
	}

	@Override
	public void fillView() {
		head_title_tv.setText("服务列表");
	}

	@Override
	public void setListener() {
		
	}

	@Override
	public void onClick(View v) {
		
	}
	/**
	 * 服务列表适配器
	 * @author admin
	 *
	 */
	class ServiceListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return servicelist==null?0:servicelist.size();
		}

		@Override
		public Object getItem(int position) {
			return servicelist==null?null:servicelist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return servicelist==null?0:position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if(convertView==null){
				vh=new ViewHolder();
				convertView=View.inflate(mContext, R.layout.activity_services_item, null);
				vh.service_item_iv=(ImageView) convertView.findViewById(R.id.service_item_iv);
				vh.service_items_name=(TextView) convertView.findViewById(R.id.service_items_name);
				vh.service_items_cheap_price=(TextView) convertView.findViewById(R.id.service_items_cheap_price);
				vh.service_items_price=(TextView) convertView.findViewById(R.id.service_items_price);
				vh.service_item_reservation=(Button) convertView.findViewById(R.id.service_item_reservation);
				vh.service_item_rl=(RelativeLayout) convertView.findViewById(R.id.service_item_rl);
				convertView.setTag(vh);
			}else{
				vh=(ViewHolder) convertView.getTag();
			}
			ImageLoderutils imageLoderutils=new ImageLoderutils(mContext);
			imageLoderutils.display(vh.service_item_iv, servicelist.get(position).getService_list_icon());
			vh.service_items_name.setText(servicelist.get(position).getService_list_title());
			vh.service_items_cheap_price.setText(servicelist.get(position).getService_list_price_now());
			vh.service_items_price.setText(servicelist.get(position).getService_list_price_old());
			vh.service_item_rl.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					bundle.putString("shoptitle", shoptitle);
					bundle.putString("shop_id", shop_id);
				bundle.putSerializable("serviceDes", servicelist.get(position));
				bundle.putSerializable(CraftsmandetailsActivity.SERVICE_LIST, (Serializable) servicelist);
				bundle.putSerializable(CraftsmandetailsActivity.PEOPLE_LIST, (Serializable) shopPeopleMesses);
				UIHelper.startActivity(mContext, ServiceIntroduceActivity.class,bundle);
				}
			});
			vh.service_item_reservation.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Bundle bundle=new Bundle();
					bundle.putString("shop_id", shop_id);
					bundle.putSerializable(CraftsmandetailsActivity.SERVICE_LIST, (Serializable) servicelist);
					bundle.putSerializable("serviceDes", servicelist.get(position));
					bundle.putSerializable(CraftsmandetailsActivity.PEOPLE_LIST, (Serializable) shopPeopleMesses);
					UIHelper.startActivity(mContext, OrderActivity.class,bundle);
				}
			});
			return convertView;
		}
		class ViewHolder{
			ImageView service_item_iv;
			TextView service_items_name;
			TextView service_items_cheap_price;
			TextView service_items_price;
			Button service_item_reservation;
			RelativeLayout service_item_rl;
		}
		
	}


}
