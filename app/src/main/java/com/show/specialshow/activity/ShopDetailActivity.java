package com.show.specialshow.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.ShopComcardStaPicsMess;
import com.show.specialshow.model.ShopComcardStaUserMess;
import com.show.specialshow.model.ShopCommendcardStatusMess;
import com.show.specialshow.model.ShopDetailsMess;
import com.show.specialshow.model.ShopInfoImgMess;
import com.show.specialshow.model.ShopInfoMess;
import com.show.specialshow.model.ShopListMess;
import com.show.specialshow.model.ShopListTagsMess;
import com.show.specialshow.model.ShopPeopleMess;
import com.show.specialshow.model.ShopReviewMess;
import com.show.specialshow.model.ShopServiceMess;
import com.show.specialshow.model.ShopShopMess;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.utils.DensityUtil;
import com.show.specialshow.utils.ImageLoderutils;
import com.show.specialshow.utils.RoundImageView;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.view.MyListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShopDetailActivity extends BaseActivity {
	private String shop_id;//商户id
	//相关控件
		private ImageView stores_iv;//图片
		private TextView stores_details_name;//秀坊名
		private GridView stores_details_label_gv;//标签
		private TextView stores_details_show_card_num;//秀卡数
		private TextView stores_details_ceng_card_num;//蹭卡数
		private TextView stores_details_moods_num;//人气
		private TextView tv_stores_details_address_name;
		private TextView tv_stores_details_phone_name;
		private TextView tv_stores_details_opening_time_name;
		private TextView show_stores_details_introduction;
		private ListView stores_details_service_lv;//服务列表
		private GridView stores_details_crafstm_gv;
		private LinearLayout ll_stores_details_content;
		private TextView tv_stores_details_related_services;
		private TextView tv_stores_details_craftsman_people;
		private RadioButton stores_details_showcard;//秀卡
		private RadioButton stores_details_cengcard;//蹭卡
		private RadioButton stores_details_review;//点评
		private TextView collect;//收藏
		private ViewPager viewPager;
//		private ViewPagerFragmentAdapter adapter;
		private MyListView shop_detail_card_lv;
		//数据实例
		private ShopDetailsMess shopDetailsMess;
		private ShopInfoMess shopInfoMess;
		private ShopShopMess shopShopMess;
		private ShopInfoImgMess shopInfoImgMess;
		private List<ShopServiceMess> shopServiceMesses=new ArrayList<ShopServiceMess>();
		private List<ShopPeopleMess> shopPeopleMesses=new ArrayList<ShopPeopleMess>();
		private List<ShopListTagsMess> mTagsMesses=new ArrayList<ShopListTagsMess>();//标签数据
		private List<ShopCommendcardStatusMess> mCommendcardStatusMesses;//秀卡动态数据
		private ShopListMess mShopListMess;//接受商户列表数据
		private LinearLayout shop_detail_content;
		private int index = 0;
		private MyListener listener = new MyListener();
		private ListView stores_details_shopcard_lv;
		private ListView stores_details_review_lv;//点评列表lv
		private static final int SHOW_CARD=1;
		private static final int CENG_CARD=2;
		private static final int REVIEW=3;
		public static final int  SHOP_SHOW_CARD=4;
		public static final int  SHOP_CENG_CARD=5;
		public static final int  SHOP_REVIEW=6;
		private CommendCardAdapter adapter;
		private CommendCardAdapter adapterReview;
		private List<ShopReviewMess> mReviewMesses;//点评数据
		private ShopComcardStaUserMess mComcardStaUserMess;
		private List<ShopComcardStaPicsMess> mComcardStaPicsMess;//图片

	@SuppressLint("NewApi")
	@Override
	public void initData() {
		shop_id=getIntent().getExtras().getString("shop_id", "");
		setContentView(R.layout.activity_shop_detail);
	}

	@Override
	public void initView() {
		stores_details_shopcard_lv=(ListView) findViewById(R.id.stores_details_shopcard_lv);
		stores_details_review_lv=(ListView) findViewById(R.id.stores_details_review_lv);
//		viewPager=(ViewPager) findViewById(R.id.viewPager);
//		shop_detail_content=(LinearLayout) findViewById(R.id.shop_detail_content);
		tv_stores_details_address_name=(TextView) findViewById(R.id.tv_stores_details_address_name);
		tv_stores_details_phone_name=(TextView) findViewById(R.id.tv_stores_details_phone_name);
		tv_stores_details_opening_time_name=(TextView) findViewById(R.id.tv_stores_details_opening_time_name);
		show_stores_details_introduction=(TextView) findViewById(R.id.show_stores_details_introduction);
		stores_details_service_lv=(ListView) findViewById(R.id.stores_details_service_lv);
		stores_details_crafstm_gv=(GridView) findViewById(R.id.stores_details_crafstm_gv);
		ll_stores_details_content=(LinearLayout) findViewById(R.id.ll_stores_details_content);
		tv_stores_details_related_services=(TextView) findViewById(R.id.tv_stores_details_related_services);
		tv_stores_details_craftsman_people=(TextView) findViewById(R.id.tv_stores_details_craftsman_people);
		stores_details_label_gv=(GridView) findViewById(R.id.stores_details_label_gv);
		stores_details_show_card_num=(TextView) findViewById(R.id.stores_details_show_card_num);
		stores_details_ceng_card_num=(TextView) findViewById(R.id.stores_details_ceng_card_num);
		stores_details_moods_num=(TextView) findViewById(R.id.stores_details_moods_num);
		stores_iv=(ImageView) findViewById(R.id.stores_iv);
		stores_details_name=(TextView) findViewById(R.id.stores_details_name);
		stores_details_showcard=(RadioButton) findViewById(R.id.stores_details_showcard);
		stores_details_cengcard=(RadioButton) findViewById(R.id.stores_details_cengcard);
		stores_details_review=(RadioButton) findViewById(R.id.stores_details_review);
		collect=(TextView) findViewById(R.id.collect);
	}

	@Override
	public void fillView() {
//		adapter=new ViewPagerFragmentAdapter(getSupportFragmentManager());
//		adapter.addFragment(ShopShowCardFragment.newInstance("秀卡", shop_id));
//		adapter.addFragment(new Content2Fragment());
//		adapter.addFragment(new Content2Fragment());
//		viewPager.setOffscreenPageLimit(3);
//		viewPager.setOnPageChangeListener(listener);
//		viewPager.setAdapter(adapter);
//		getViewData();
		getShopCard(SHOW_CARD, "秀卡", true);
	}

	@Override
	public void setListener() {
		
	}

	@Override
	public void onClick(View v) {
		Bundle bundle=new Bundle();
		switch (v.getId()) {
		case R.id.rl_stores_details_address://地址
			
			break;
		case R.id.rl_stores_details_phone://电话
			createAffirmDialog("拔打电话？",DIALOG_DEFAULT_STPE,true);
			break;
		case R.id.contest_confirm_tv:
			affirmDialog.cancel();
			UIHelper.showTel(mContext, tv_stores_details_phone_name.getText().toString());
			break;
		case R.id.contest_cancel_tv:
			affirmDialog.cancel();
			break;
		case R.id.rl_stores_details_related_services://相关服务
			bundle.putSerializable(CraftsmandetailsActivity.SERVICE_LIST, (Serializable) shopServiceMesses);
			bundle.putString("shoptitle", mShopListMess.getTitle());
			bundle.putString("shopt_id", mShopListMess.getShop_id());
			bundle.putSerializable(CraftsmandetailsActivity.PEOPLE_LIST, (Serializable) shopPeopleMesses);
			UIHelper.startActivity(mContext, ServiceListActivity.class,bundle);
			break;
		case R.id.rl_stores_details_craftsman_people://手艺人
			bundle.putString("shopt_id", mShopListMess.getShop_id());
			bundle.putSerializable(CraftsmandetailsActivity.SERVICE_LIST, (Serializable) shopServiceMesses);
			bundle.putSerializable(CraftsmandetailsActivity.PEOPLE_LIST, (Serializable) shopPeopleMesses);
			UIHelper.startActivity(mContext, CraftsmanActivity.class,bundle);
			break;
		case R.id.stores_details_showcard://秀卡
			chaneTab(SHOW_CARD);
			break;
		case R.id.stores_details_cengcard://蹭卡
			chaneTab(CENG_CARD);
			break;
		case R.id.stores_details_review://点评
			chaneTab(REVIEW);
			break;
		case R.id.stores_details_showcard_hover://秀卡
			chaneTab(SHOW_CARD);
			break;
		case R.id.stores_details_cengcard_hover://蹭卡
			chaneTab(CENG_CARD);
			break;
		case R.id.stores_details_review_hover://点评
			chaneTab(REVIEW);
			break;
		case R.id.rl_bottom_show_card://要秀卡
			bundle.putInt("send_type", SHOP_SHOW_CARD);
			bundle.putString("shoptitle", mShopListMess.getTitle());
			bundle.putString("shop_id", mShopListMess.getShop_id());
			UIHelper.startActivity(mContext, SendCardActivity.class, bundle);
			break;
		case R.id.rl_bottom_ceng_card://要蹭卡
			bundle.putInt("send_type", SHOP_CENG_CARD);
			bundle.putString("shoptitle", mShopListMess.getTitle());
			bundle.putString("shop_id", mShopListMess.getShop_id());
			UIHelper.startActivity(mContext, SendCardActivity.class, bundle);
			break;
		case R.id.rl_bottom_review://要点评
			bundle.putInt("send_type", SHOP_REVIEW);
			bundle.putString("shop_id", mShopListMess.getShop_id());
			UIHelper.startActivity(mContext, SendCardActivity.class, bundle);
			break;
		case R.id.rl_bottom_collection://收藏
//			collect();
			break;

		default:
			break;
		}
	}
	/**
	 * 秀卡，蹭卡，点评的切换
	 */
	private void chaneTab(int index){
		switch (index) {
		case SHOW_CARD:
			stores_details_showcard.setChecked(true);
			stores_details_cengcard.setChecked(false);
			stores_details_review.setChecked(false);
			getShopCard(SHOW_CARD, "秀卡",false);
			break;
		case CENG_CARD:
			stores_details_showcard.setChecked(false);
			stores_details_cengcard.setChecked(true);
			stores_details_review.setChecked(false);
//			cengCount++;
			getShopCard(CENG_CARD, "蹭卡",false);
			break;
		case REVIEW:
			stores_details_showcard.setChecked(false);
			stores_details_cengcard.setChecked(false);
			stores_details_review.setChecked(true);
//			reviewCount++;
			getShopCard(REVIEW, "点评",false);
			break;

		default:
			break;
		}
	}
	/**
	 * 获取秀卡数据
	 */
	private void getShopCard(final int index,String type,final boolean isfrist){
		RequestParams params=TXApplication.getParams();
		String url=URLs.SHOP_CARD;
		params.addBodyParameter("shop_id", shop_id);
		params.addBodyParameter("type", type);
		TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();
				
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				UIHelper.ToastMessage(mContext, R.string.net_work_error);
//				dialog.dismiss();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
//				dialog.dismiss();
				MessageResult result=MessageResult.parse(responseInfo.result);
				if(null==result){
					UIHelper.ToastMessage(mContext, "数据加载出错，请重试!");
					return;
				}
				if(1==result.getSuccess()){
					if(SHOW_CARD==index){
						mCommendcardStatusMesses=ShopCommendcardStatusMess.parse(result.getData());
						if(mCommendcardStatusMesses!=null){
							stores_details_showcard.setText("秀卡"+"("+mCommendcardStatusMesses.get(0).getX_count()+")");
							stores_details_review.setText("点评"+"("+mCommendcardStatusMesses.get(0).getD_count()+")");
							stores_details_cengcard.setText("蹭卡"+"("+mCommendcardStatusMesses.get(0).getC_count()+")");
//							stores_details_showcard_hover.setText("秀卡"+"("+mCommendcardStatusMesses.get(0).getX_count()+")");
//							stores_details_review_hover.setText("点评"+"("+mCommendcardStatusMesses.get(0).getD_count()+")");
//							stores_details_cengcard_hover.setText("蹭卡"+"("+mCommendcardStatusMesses.get(0).getC_count()+")");
						}
						if(isfrist){
							adapter=new CommendCardAdapter(SHOW_CARD);
							stores_details_shopcard_lv.setAdapter(adapter);
//							adapter.notifyDataSetChanged();
							adapterReview=new CommendCardAdapter(REVIEW);
							stores_details_review_lv.setAdapter(adapterReview);
						}else{
							stores_details_review_lv.setVisibility(View.GONE);
							stores_details_shopcard_lv.setVisibility(View.VISIBLE);
//							stores_details_shopcard_lv.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						}
//						hvoverView(stores_details_shopcard_lv,true);
					}else if(CENG_CARD==index){
						mCommendcardStatusMesses=ShopCommendcardStatusMess.parse(result.getData());
//						adapter=new CommendCardAdapter(CENG_CARD);
						if(mCommendcardStatusMesses!=null){
							stores_details_showcard.setText("秀卡"+"("+mCommendcardStatusMesses.get(0).getX_count()+")");
							stores_details_review.setText("点评"+"("+mCommendcardStatusMesses.get(0).getD_count()+")");
							stores_details_cengcard.setText("蹭卡"+"("+mCommendcardStatusMesses.get(0).getC_count()+")");
//							stores_details_showcard_hover.setText("秀卡"+"("+mCommendcardStatusMesses.get(0).getX_count()+")");
//							stores_details_review_hover.setText("点评"+"("+mCommendcardStatusMesses.get(0).getD_count()+")");
//							stores_details_cengcard_hover.setText("蹭卡"+"("+mCommendcardStatusMesses.get(0).getC_count()+")");
						}
						stores_details_review_lv.setVisibility(View.GONE);
						stores_details_shopcard_lv.setVisibility(View.VISIBLE);
						adapter.notifyDataSetChanged();
//						hvoverView(stores_details_shopcard_lv,true);
					}else if(REVIEW==index){
						mReviewMesses=ShopReviewMess.parse(result.getData());
						if(mReviewMesses!=null){
							stores_details_showcard.setText("秀卡"+"("+mReviewMesses.get(0).getX_count()+")");
							stores_details_cengcard.setText("蹭卡"+"("+mReviewMesses.get(0).getC_count()+")");
							stores_details_review.setText("点评"+"("+mReviewMesses.get(0).getD_count()+")");
//							stores_details_showcard_hover.setText("秀卡"+"("+mReviewMesses.get(0).getX_count()+")");
//							stores_details_cengcard_hover.setText("蹭卡"+"("+mReviewMesses.get(0).getC_count()+")");
//							stores_details_review_hover.setText("点评"+"("+mReviewMesses.get(0).getD_count()+")");
						}
							stores_details_review_lv.setVisibility(View.VISIBLE);
							stores_details_shopcard_lv.setVisibility(View.GONE);
							adapterReview.notifyDataSetChanged();
//							hvoverView(stores_details_review_lv,false);
					}
//					stores_details_shopcard_lv.setFocusable(false);
//					setListViewHeightBasedOnChildren(stores_details_shopcard_lv);
				}
			}
		});
		
	}
	private void getViewData(){
		UserMessage user=TXApplication.getUserMess();
		RequestParams params=TXApplication.getParams();
		String url=URLs.SHOP_DETAILS;
		params.addBodyParameter("shop_id", shop_id);
		params.addBodyParameter("uid", user.getUid());
		TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();
				loadIng("加载中", false);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				UIHelper.ToastMessage(mContext, R.string.net_work_error);
				dialog.dismiss();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				dialog.dismiss();
//				resetViewPagerHeight(0);
				MessageResult result=MessageResult.parse(responseInfo.result);
				if(null==result){
					UIHelper.ToastMessage(mContext, "数据加载出错，请重试!");
					return;
				}
				if(1==result.getSuccess()){
					if(result.getData()!=null){
					shopDetailsMess = ShopDetailsMess.parse(result.getData());
					shopInfoMess=ShopInfoMess.parse(shopDetailsMess.getShop_info());
					shopShopMess=ShopShopMess.parse(shopDetailsMess.getShow_shop());
					shopServiceMesses=ShopServiceMess.parse(shopDetailsMess.getShop_service());
					shopPeopleMesses=ShopPeopleMess.parse(shopDetailsMess.getShop_people());
					mShopListMess=ShopListMess.getparse(shopDetailsMess.getPackage_shop());
					}else{
						UIHelper.ToastLogMessage(mContext, result.getMessage());
					}
				}else{
					UIHelper.ToastLogMessage(mContext, result.getMessage());
				}
			}
		});
	}
	public class MyListener implements OnPageChangeListener {
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}
		
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			
		}
		
		@Override
		public void onPageSelected(int position) {
			// 页面切换后重置ViewPager高度
//			resetViewPagerHeight(position);
			switch (position) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			}
		}
	}
	/**
	 * 重新设置viewPager高度
	 * 
	 * @param position
	 */
	public void resetViewPagerHeight(int position) {
		View child = viewPager.getChildAt(position);
		if (child != null) {
			child.measure(0, 0);
			int h = child.getMeasuredHeight();
			LinearLayout.LayoutParams params = (LayoutParams) viewPager
					.getLayoutParams();
			params.height = h + 50;
			viewPager.setLayoutParams(params);
		}
	}
	/**
	 * 秀卡和蹭卡，点评列表适配器
	 */
	class CommendCardAdapter extends BaseAdapter{
		private int index;
		
		public CommendCardAdapter(int index) {
			super();
			this.index = index;
		}

		@Override
		public int getCount() {
			if(REVIEW==index){
				return null==mReviewMesses?0:mReviewMesses.size();
			}
			return null==mCommendcardStatusMesses?0:mCommendcardStatusMesses.size();
		}

		@Override
		public Object getItem(int position) {
			if(REVIEW==index){
				return null==mReviewMesses?null:mReviewMesses.get(position);
			}
			return null==mCommendcardStatusMesses?null:mCommendcardStatusMesses.get(position);
		}

		@Override
		public long getItemId(int position) {
			if(REVIEW==index){
				return null==mReviewMesses?0:position;
			}
			return null==mCommendcardStatusMesses?0:position;
		}

		@SuppressLint("NewApi")
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder1 vh=null;
			if(null==convertView){
				switch (index) {
				case REVIEW:
					vh=new ViewHolder1();
					convertView=View.inflate(mContext,R.layout.item_shop_review, null);
					vh.roundImage_commend=(RoundImageView) convertView.findViewById(R.id.roundImage_shop_review);
					vh.shop_commend_name=(TextView) convertView.findViewById(R.id.shop_review_name);
					vh.shop_commend_createtime=(TextView) convertView.findViewById(R.id.shop_review_createtime);
					vh.shop_commend_chat=(TextView) convertView.findViewById(R.id.shop_review_chat);
					vh.shop_commend_gv=(GridView) convertView.findViewById(R.id.shop_review_gv);
					vh.shop_commend_content=(TextView) convertView.findViewById(R.id.shop_review_content);
				
					convertView.setTag(vh);
					break;

				default:
					vh=new ViewHolder1();
					convertView=View.inflate(mContext,R.layout.item_shop_comcard, null);
					vh.roundImage_commend=(RoundImageView) convertView.findViewById(R.id.roundImage_shop_comcard);
					vh.shop_commend_name=(TextView) convertView.findViewById(R.id.shop_comcard_name);
					vh.shop_commend_createtime=(TextView) convertView.findViewById(R.id.shop_comcard_createtime);
					vh.shop_commend_chat=(TextView) convertView.findViewById(R.id.shop_comcard_chat);
					vh.shop_commend_gv=(GridView) convertView.findViewById(R.id.shop_comcard_gv);
					vh.shop_commend_content=(TextView) convertView.findViewById(R.id.shop_comcard_content);
					convertView.setTag(vh);
					break;
				}
			}else{
				switch (index) {
				case REVIEW:
					vh=(ViewHolder1) convertView.getTag();
					break;

				default:
					vh=(ViewHolder1) convertView.getTag();
					break;
				}
			}
			switch (index) {
			case REVIEW:
				ImageLoderutils imLoderutils=new ImageLoderutils(mContext);
				imLoderutils.display(vh.roundImage_commend,mReviewMesses.get(position).getComment_icon());
				vh.shop_commend_name.setText(mReviewMesses.get(position).getComment_name());
				vh.shop_commend_createtime.setText(mReviewMesses.get(position).getComment_time());
				vh.shop_commend_content.setText(mReviewMesses.get(position).getComment_total());
				mComcardStaPicsMess=ShopComcardStaPicsMess.parse(mReviewMesses.get(position).getStatus_pics());
		/*		if(mComcardStaPicsMess!=null){
					switch (mComcardStaPicsMess.size()) {
					case 4:
						vh.shop_commend_gv.setNumColumns(2);
						break;
//					case 1:
//						vh.shop_commend_gv.setNumColumns(1);
//						break;
					default:
						vh.shop_commend_gv.setNumColumns(3);
						break;
					}
				}*/
//				gv_width=vh.shop_commend_gv.getColumnWidth();
				vh.shop_commend_gv.setAdapter(new CommendgvAdapter(mComcardStaPicsMess));
				vh.shop_commend_gv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Bundle bundle=new Bundle();
						bundle.putSerializable(ImagePagerActivity.EXTRA_IMAGE_URLS,(Serializable) ShopComcardStaPicsMess.parse(mReviewMesses.get(position).getStatus_pics()));
						bundle.putInt(ImagePagerActivity.EXTRA_IMAGE_INDEX, arg2);
						UIHelper.startActivity(mContext, ImagePagerActivity.class,bundle);
					}
				});
				break;

			default:
				mComcardStaUserMess=ShopComcardStaUserMess.parse(mCommendcardStatusMesses.get(position).getStatus_user());
				ImageLoderutils imageLoderutils=new ImageLoderutils(mContext);
				imageLoderutils.display(vh.roundImage_commend,mComcardStaUserMess.getUser_icon());
				vh.shop_commend_name.setText(mComcardStaUserMess.getUser_name());
				vh.shop_commend_createtime.setText(mComcardStaUserMess.getUser_statusCreateTime());
				vh.shop_commend_content.setText(mCommendcardStatusMesses.get(position).getStatus_content());
				mComcardStaPicsMess=ShopComcardStaPicsMess.parse(mCommendcardStatusMesses.get(position).getStatus_pics());
//				gv_width=vh.shop_commend_gv.getColumnWidth();
			/*	if(mComcardStaPicsMess!=null){
					switch (mComcardStaPicsMess.size()) {
					case 4:
						vh.shop_commend_gv.setNumColumns(2);
						break;
//					case 1:
//						vh.shop_commend_gv.setNumColumns(1);
//						break;
					default:
						vh.shop_commend_gv.setNumColumns(3);
						break;
					}
				}*/
//				if(mComcardStaPicsMess!=null&&mComcardStaPicsMess.size()>0){
					vh.shop_commend_gv.setVisibility(View.VISIBLE);	
				vh.shop_commend_gv.setAdapter(new CommendgvAdapter(mComcardStaPicsMess));	
				vh.shop_commend_gv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Bundle bundle=new Bundle();
						bundle.putSerializable(ImagePagerActivity.EXTRA_IMAGE_URLS,(Serializable) ShopComcardStaPicsMess.parse(mCommendcardStatusMesses.get(position).getStatus_pics()));
						bundle.putInt(ImagePagerActivity.EXTRA_IMAGE_INDEX, arg2);
						UIHelper.startActivity(mContext, ImagePagerActivity.class,bundle);
					}
				});
//				}else{
//					vh.shop_commend_gv.setVisibility(View.GONE);	
//				}
				break;
			}
			
			return convertView;
		}
		 class ViewHolder1{
			RoundImageView roundImage_commend;
			TextView shop_commend_name;
			TextView shop_commend_createtime;
			TextView shop_commend_chat;
			GridView shop_commend_gv;
			TextView shop_commend_content;
		}
		
	}
	/**
	 * 秀卡，蹭卡，点评
	 */
	class CommendgvAdapter extends BaseAdapter{
		private List<ShopComcardStaPicsMess> staPicsMesses;
		

		public CommendgvAdapter(List<ShopComcardStaPicsMess> staPicsMesses) {
			super();
			this.staPicsMesses = staPicsMesses;
		}

		@Override
		public int getCount() {
			return null==staPicsMesses?0:staPicsMesses.size();
		}

		@Override
		public Object getItem(int position) {     
			return null==staPicsMesses?null:staPicsMesses.get(position);
		}

		@Override
		public long getItemId(int position) {
			return null==staPicsMesses?0:position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder vh=null;
			if(null==convertView){
				vh=new ViewHolder();
				convertView=View.inflate(mContext, R.layout.activity_my_work_item, null);
				vh.shop_commend_iv=(ImageView) convertView.findViewById(R.id.my_work_item_iv);
			
				convertView.setTag(vh);
			}else{
				vh=(ViewHolder) convertView.getTag();
			}
//			if(mComcardStaPicsMess!=null){
//				switch (mComcardStaPicsMess.size()) {
//				case 4:
//					vh.shop_commend_iv.setLayoutParams(new LayoutParams((TXApplication.WINDOW_WIDTH-DensityUtil.dip2px(mContext, 62))/2,(TXApplication.WINDOW_WIDTH-DensityUtil.dip2px(mContext, 62))/2));
//					break;
////				case 1:
////					vh.shop_commend_iv.setLayoutParams(new LayoutParams((TXApplication.WINDOW_WIDTH-DensityUtil.dip2px(mContext, 52))/1,(int) ((TXApplication.WINDOW_WIDTH-DensityUtil.dip2px(mContext, 52))/1.5)));
////					break;
//				default:
					vh.shop_commend_iv.setLayoutParams(new LayoutParams((TXApplication.WINDOW_WIDTH-DensityUtil.dip2px(mContext, 72))/3,(TXApplication.WINDOW_WIDTH-DensityUtil.dip2px(mContext, 72))/3));
//					break;
//				}
//			}
			vh.shop_commend_iv.setTag(staPicsMesses.get(position).getThumbnail_pic()+position);
			if(vh.shop_commend_iv.getTag()!=null&&vh.shop_commend_iv.getTag().equals(staPicsMesses.get(position).getThumbnail_pic()+position)){
			ImageAware imageAware = new ImageViewAware(vh.shop_commend_iv, false);
	/*		ImageLoderutils imageLoderutils=new ImageLoderutils(mContext);
			imageLoderutils.display(vh.shop_commend_iv,mComcardStaPicsMess.get(position).getThumbnail_pic());*/
			ImageLoader.getInstance().displayImage(staPicsMesses.get(position).getThumbnail_pic(),imageAware);
			}
			return convertView;
		}
		
	}
	private static class ViewHolder{
		ImageView shop_commend_iv;
		private int position;

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}
	}
//	private ShopShowCardFragment shopShowCardFragment;
//	private ShopShowCardFragment shopCengCardFragment;
//	private void getFragment() {
//		shopShowCardFragment=ShopShowCardFragment.newInstance("秀卡",shop_id);
////		shopCengCardFragment=ShopShowCardFragment.newInstance("蹭卡",shop_id);
//		getSupportFragmentManager()
//		.beginTransaction()
//		// .add(R.id.fragment_container, mine).hide(mine)
//		.add(R.id.shop_detail_content, shopShowCardFragment).hide(shopShowCardFragment)
////		.add(R.id.shop_detail_content, shopCengCardFragment).hide(shopCengCardFragment)
//		.show(shopShowCardFragment).commit();
//	}

}
