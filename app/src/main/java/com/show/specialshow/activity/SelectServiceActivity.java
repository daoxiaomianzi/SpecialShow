package com.show.specialshow.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.model.ShopServiceMess;
import com.show.specialshow.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

public class SelectServiceActivity extends BaseActivity {
	private List<ShopServiceMess> shopServiceMesses=new ArrayList<ShopServiceMess>();//服务集合数据
	//相关控件
	private ListView lv_select_service;

	@SuppressWarnings("unchecked")
	@Override
	public void initData() {
		shopServiceMesses=(List<ShopServiceMess>) getIntent().getSerializableExtra("servicelist");
		setContentView(R.layout.activity_select_service);
	}

	@Override
	public void initView() {
		lv_select_service=(ListView) findViewById(R.id.lv_select_service);
	}

	@Override
	public void fillView() {
		head_title_tv.setText("选择服务");
		lv_select_service.setAdapter(new SelectServiceAdapter());
	}

	@Override
	public void setListener() {
		/*lv_select_service.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					 int position, long id) {
				UIHelper.ToastLogMessage(mContext, "点我干嘛于");
			}
		});*/
	}

	@Override
	public void onClick(View v) {
	}
	
	class SelectServiceAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return shopServiceMesses==null?0:shopServiceMesses.size();
		}

		@Override
		public Object getItem(int position) {
			return shopServiceMesses==null?null:shopServiceMesses.get(position);
		}

		@Override
		public long getItemId(int position) {
			return shopServiceMesses==null?0:position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if(null==convertView){
				vh=new ViewHolder();
				convertView=View.inflate(mContext, R.layout.activity_select_service_item, null); 
				vh.select_service_name=(TextView) convertView.findViewById(R.id.select_service_name);
				vh.select_service_now_price=(TextView) convertView.findViewById(R.id.select_service_cheap_price);
				vh.select_service_old_price=(TextView) convertView.findViewById(R.id.select_service_old_price);
				vh.rl_selectc_service=(RelativeLayout) convertView.findViewById(R.id.rl_selectc_service);
				convertView.setTag(vh);
			}else{
				vh=(ViewHolder) convertView.getTag();
			}
			vh.select_service_name.setText(shopServiceMesses.get(position).getService_list_title());
			vh.select_service_now_price.setText(shopServiceMesses.get(position).getService_list_price_now());
			vh.select_service_old_price.setText(shopServiceMesses.get(position).getService_list_price_old());
			vh.rl_selectc_service.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent();
					intent.putExtra("select_service", shopServiceMesses.get(position));
					UIHelper.setResult(mContext, -1, intent);
				}
			});
			return convertView;
		}
		class ViewHolder{
			TextView select_service_name;
			TextView select_service_old_price;
			TextView select_service_now_price;
			RelativeLayout rl_selectc_service;
		}
		
	}


}
