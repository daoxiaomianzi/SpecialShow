package com.show.specialshow.activity;

import java.io.Serializable;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.model.ShopPeopleMess;
import com.show.specialshow.model.ShopServiceMess;
import com.show.specialshow.utils.ImageLoderutils;
import com.show.specialshow.utils.UIHelper;

public class CraftsmanActivity extends BaseActivity {
	private GridView craftsman_gv;//
	private String shop_id;
	private List<ShopPeopleMess> shopPeopleMesses;//手艺人集合数据
	private List<ShopServiceMess> shopServiceMesses;

	@SuppressWarnings("unchecked")
	@Override
	public void initData() {
		shop_id=getIntent().getStringExtra("shop_id");
		shopServiceMesses=(List<ShopServiceMess>) getIntent().getSerializableExtra(CraftsmandetailsActivity.SERVICE_LIST);
		shopPeopleMesses=(List<ShopPeopleMess>) getIntent().getSerializableExtra(CraftsmandetailsActivity.PEOPLE_LIST);
		setContentView(R.layout.activity_craftsman);
		
	}

	@Override
	public void initView() {
		craftsman_gv=(GridView) findViewById(R.id.craftsman_gv);
		craftsman_gv.setAdapter(new CraftsmanAdapter());
	}

	@Override
	public void fillView() {
		head_title_tv.setText("手艺人");
	}

	@Override
	public void setListener() {
		craftsman_gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				Bundle bundle=new Bundle();
				bundle.putString("shop_id", shop_id);
				bundle.putSerializable(CraftsmandetailsActivity.PEOPLE_LIST,(Serializable) shopPeopleMesses);
				bundle.putInt(CraftsmandetailsActivity.WHERR_FROM, CraftsmandetailsActivity.SHOP_FROM);
				bundle.putSerializable(CraftsmandetailsActivity.SERVICE_LIST, (Serializable) shopServiceMesses);
				bundle.putString("user_id", shopPeopleMesses.get(position).getChoice_artisans_id());
				bundle.putSerializable(CraftsmandetailsActivity.PEOPLE_DES, shopPeopleMesses.get(position));
				UIHelper.startActivity(mContext, CraftsmandetailsActivity.class,bundle);
			}
		});
	}

	@Override
	public void onClick(View v) {
		
	}
	class CraftsmanAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return shopPeopleMesses==null?0:shopPeopleMesses.size();
		}

		@Override
		public Object getItem(int position) {
			return shopPeopleMesses==null?0:shopPeopleMesses.get(position);
		}

		@Override
		public long getItemId(int position) {
			return shopPeopleMesses==null?0:position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if(null==convertView){
				vh=new ViewHolder();
				convertView=View.inflate(mContext, R.layout.activity_craftsman_itemot, null);
				vh.craftsm_head_item_pro=(ImageView) convertView.findViewById(R.id.craftsm_head_item_pro);
				vh.craftsm_item_name=(TextView) convertView.findViewById(R.id.craftsm_item_name);
				vh.craftsm__item_job=(TextView) convertView.findViewById(R.id.craftsm__item_job);
				vh.craftsm_item_moods=(TextView) convertView.findViewById(R.id.craftsm_item_moods);
				convertView.setTag(vh);
			}else{
				vh=(ViewHolder) convertView.getTag();
			}
			vh.craftsm_head_item_pro.setLayoutParams(new LayoutParams((TXApplication.WINDOW_WIDTH-50)/2, (TXApplication.WINDOW_WIDTH-50)/2));
			ImageLoderutils imageLoderutils=new ImageLoderutils(mContext);
			imageLoderutils.display(vh.craftsm_head_item_pro, shopPeopleMesses.get(position).getChoice_artisans_icon());
			vh.craftsm_item_name.setText(shopPeopleMesses.get(position).getChoice_artisans_name());
			vh.craftsm__item_job.setText(shopPeopleMesses.get(position).getChoice_artisans_job());
			vh.craftsm_item_moods.setText(shopPeopleMesses.get(position).getChoice_artisans_hot()+"人气");
			return convertView;
		}
		class ViewHolder{
			ImageView craftsm_head_item_pro;//图片
			TextView craftsm_item_name;//名字
			TextView craftsm__item_job;//职位
			TextView craftsm_item_moods;//人气
		}
		
	}


}
