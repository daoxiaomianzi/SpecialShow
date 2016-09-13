package com.show.specialshow.ninegridlayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.activity.CraftsmanActivity;
import com.show.specialshow.activity.CraftsmandetailsActivity;
import com.show.specialshow.activity.ImagePagerActivity;
import com.show.specialshow.activity.LoginActivity;
import com.show.specialshow.activity.OrderActivity;
import com.show.specialshow.activity.SendCardActivity;
import com.show.specialshow.activity.ServiceIntroduceActivity;
import com.show.specialshow.activity.ServiceListActivity;
import com.show.specialshow.adapter.TagsMessAdapter;
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
import com.show.specialshow.utils.ImmersedStatusbarUtils;
import com.show.specialshow.utils.RoundImageView;
import com.show.specialshow.utils.UIHelper;

public class StoresDetailsNineActivity extends BaseActivity{
	//数据实例
	private ShopDetailsMess shopDetailsMess;
	private ShopInfoMess shopInfoMess;
	private ShopShopMess shopShopMess;
	private ShopInfoImgMess shopInfoImgMess;
	private List<ShopServiceMess> shopServiceMesses=new ArrayList<ShopServiceMess>();
	private List<ShopPeopleMess> shopPeopleMesses=new ArrayList<ShopPeopleMess>();
	public static final int SHOW_CARD=1;
	public static final int CENG_CARD=2;
	public static final int REVIEW=3;
	public static final int  SHOP_SHOW_CARD=4;
	public static final int  SHOP_CENG_CARD=5;
	public static final int  SHOP_REVIEW=6;
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
	private RadioButton stores_details_showcard_hover;//秀卡
	private RadioButton stores_details_cengcard_hover;//蹭卡
	private RadioButton stores_details_review_hover;//点评
	private ListView stores_details_shopcard_lv;
	private ListView stores_details_review_lv;//点评列表lv
	private ShopListMess mShopListMess;//接受商户列表数据
	private List<ShopListTagsMess> mTagsMesses=new ArrayList<ShopListTagsMess>();//标签数据
	private List<ShopCommendcardStatusMess> mCommendcardStatusMesses;//秀卡动态数据
	private ShopComcardStaUserMess mComcardStaUserMess;
	private List<ShopReviewMess> mReviewMesses;//点评数据
	private NineStoreDetailsAdapter adapter;
	private NineStoreDetailsAdapter adapterReview;
	private List<ShopComcardStaPicsMess> mComcardStaPicsMess;//图片

	private String shop_id;//商户id
	private TextView collect;//收藏
	private LinearLayout stores_details_bottom_ll;
	View stores_details;
	View stores_details_head2;
	private LinearLayout stores_details_hover_ll;
	@SuppressLint("NewApi")
	@Override
	public void initData() {
//		mShopListMess=(ShopListMess) getIntent().getSerializableExtra("shopList");
		shop_id=getIntent().getExtras().getString("shop_id", "");
		setContentView(R.layout.activity_shop_details);
		View head=findViewById(R.id.head_rl);
		 ImmersedStatusbarUtils.initAfterSetContentView(mContext, head);
		stores_details_shopcard_lv=(ListView) findViewById(R.id.stores_details_shopcard_lv);
		stores_details_review_lv=(ListView) findViewById(R.id.stores_details_review_lv);
		stores_details_review_lv.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
		stores_details_shopcard_lv.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
		stores_details=View.inflate(mContext, R.layout.activity_stores_details, null);
		stores_details_head2=View.inflate(mContext, R.layout.activity_stores_details_head2, null);
		stores_details_shopcard_lv.addHeaderView(stores_details);
		stores_details_shopcard_lv.addHeaderView(stores_details_head2);
		stores_details_review_lv.addHeaderView(stores_details);
		stores_details_review_lv.addHeaderView(stores_details_head2);
	}

	@Override
	public void initView() {
		stores_details_hover_ll=(LinearLayout) findViewById(R.id.stores_details_hover_ll);
		stores_details_showcard_hover=(RadioButton) findViewById(R.id.stores_details_showcard_hover);
		stores_details_cengcard_hover=(RadioButton) findViewById(R.id.stores_details_cengcard_hover);
		stores_details_review_hover=(RadioButton) findViewById(R.id.stores_details_review_hover);
		tv_stores_details_address_name=(TextView) stores_details.findViewById(R.id.tv_stores_details_address_name);
		tv_stores_details_phone_name=(TextView) stores_details.findViewById(R.id.tv_stores_details_phone_name);
		tv_stores_details_opening_time_name=(TextView) stores_details.findViewById(R.id.tv_stores_details_opening_time_name);
		show_stores_details_introduction=(TextView) stores_details.findViewById(R.id.show_stores_details_introduction);
		stores_details_service_lv=(ListView) stores_details.findViewById(R.id.stores_details_service_lv);
		stores_details_crafstm_gv=(GridView) stores_details.findViewById(R.id.stores_details_crafstm_gv);
		ll_stores_details_content=(LinearLayout) stores_details.findViewById(R.id.ll_stores_details_content);
		tv_stores_details_related_services=(TextView) stores_details.findViewById(R.id.tv_stores_details_related_services);
		tv_stores_details_craftsman_people=(TextView) stores_details.findViewById(R.id.tv_stores_details_craftsman_people);
		stores_details_label_gv=(GridView) stores_details.findViewById(R.id.stores_details_label_gv);
		stores_details_show_card_num=(TextView) stores_details.findViewById(R.id.stores_details_show_card_num);
		stores_details_ceng_card_num=(TextView) stores_details.findViewById(R.id.stores_details_ceng_card_num);
		stores_details_moods_num=(TextView) stores_details.findViewById(R.id.stores_details_moods_num);
		stores_iv=(ImageView) stores_details.findViewById(R.id.stores_iv);
		stores_details_name=(TextView) stores_details.findViewById(R.id.stores_details_name);
		stores_details_showcard=(RadioButton) stores_details_head2.findViewById(R.id.stores_details_showcard);
		stores_details_cengcard=(RadioButton) stores_details_head2.findViewById(R.id.stores_details_cengcard);
		stores_details_review=(RadioButton) stores_details_head2.findViewById(R.id.stores_details_review);
		collect=(TextView) findViewById(R.id.collect);
		stores_details_bottom_ll=(LinearLayout) findViewById(R.id.stores_details_bottom_ll);
		stores_details_bottom_ll.setVisibility(View.GONE);
		stores_details_shopcard_lv.setVisibility(View.GONE);
	}

	@Override
	public void fillView() {
		head_title_tv.setVisibility(View.GONE);
		getViewData();
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
			collect();
			break;

		default:
			break;
		}
	}
	/**
	 * 商户的收藏与取消收藏
	 */
	private void collect(){
		Bundle bundle=new Bundle();
		UserMessage user=TXApplication.getUserMess();
		if(user.isLogin()){
			RequestParams params=TXApplication.getParams();
			String url=URLs.SHOP_COLLECT;
			params.addBodyParameter("uid",user.getUid());
			params.addBodyParameter("shop_id", shop_id);
			TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
				@Override
				public void onStart() {
					super.onStart();
//					UIHelper.createProgressDialog(mContext, "").show();
				}
				@Override
				public void onFailure(HttpException error, String msg) {
//					UIHelper.createProgressDialog(mContext, "").dismiss();
					UIHelper.ToastMessage(mContext, R.string.net_work_error);
				}

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
//					UIHelper.createProgressDialog(mContext, "").cancel();
					MessageResult result=MessageResult.parse(responseInfo.result);
					if(null==result){
						return;
					}
					if(1==result.getSuccess()){
						collect.setText("取消收藏");
					}else if(2==result.getSuccess()){
						collect.setText("收藏");
					}
					UIHelper.ToastMessage(mContext, result.getMessage());
				}
			});
		}else{
			UIHelper.startActivity(mContext, LoginActivity.class,bundle);
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
//				dialog.dismiss();
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
					getData();
					}else{
						UIHelper.ToastLogMessage(mContext, result.getMessage());
					}
				}else{
					UIHelper.ToastLogMessage(mContext, result.getMessage());
				}
			}
		});
	}
	/**
	 * 获取秀卡数据
	 */
	private void getShopCard(final int index,String type,final boolean isfrist){
		RequestParams params=TXApplication.getParams();
		String url=URLs.SHOP_CARD;
		params.addBodyParameter("shop_id", mShopListMess.getShop_id());
		params.addBodyParameter("type", type);
		TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();
				
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				UIHelper.ToastMessage(mContext, R.string.net_work_error);
				dialog.dismiss();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				dialog.dismiss();
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
							stores_details_showcard_hover.setText("秀卡"+"("+mCommendcardStatusMesses.get(0).getX_count()+")");
							stores_details_review_hover.setText("点评"+"("+mCommendcardStatusMesses.get(0).getD_count()+")");
							stores_details_cengcard_hover.setText("蹭卡"+"("+mCommendcardStatusMesses.get(0).getC_count()+")");
						}
						if(isfrist){
							adapter=new NineStoreDetailsAdapter(SHOW_CARD);
							stores_details_shopcard_lv.setAdapter(adapter);
							adapterReview=new NineStoreDetailsAdapter(REVIEW);
							stores_details_review_lv.setAdapter(adapterReview);
							updataview();
						}else{
							stores_details_review_lv.setVisibility(View.GONE);
							stores_details_shopcard_lv.setVisibility(View.VISIBLE);
//							stores_details_shopcard_lv.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						}
						hvoverView(stores_details_shopcard_lv,true);
					}else if(CENG_CARD==index){
						mCommendcardStatusMesses=ShopCommendcardStatusMess.parse(result.getData());
//						adapter=new CommendCardAdapter(CENG_CARD);
						if(mCommendcardStatusMesses!=null){
							stores_details_showcard.setText("秀卡"+"("+mCommendcardStatusMesses.get(0).getX_count()+")");
							stores_details_review.setText("点评"+"("+mCommendcardStatusMesses.get(0).getD_count()+")");
							stores_details_cengcard.setText("蹭卡"+"("+mCommendcardStatusMesses.get(0).getC_count()+")");
							stores_details_showcard_hover.setText("秀卡"+"("+mCommendcardStatusMesses.get(0).getX_count()+")");
							stores_details_review_hover.setText("点评"+"("+mCommendcardStatusMesses.get(0).getD_count()+")");
							stores_details_cengcard_hover.setText("蹭卡"+"("+mCommendcardStatusMesses.get(0).getC_count()+")");
						}
						stores_details_review_lv.setVisibility(View.GONE);
						stores_details_shopcard_lv.setVisibility(View.VISIBLE);
						adapter.notifyDataSetChanged();
						hvoverView(stores_details_shopcard_lv,true);
					}else if(REVIEW==index){
						mReviewMesses=ShopReviewMess.parse(result.getData());
						if(mReviewMesses!=null){
							stores_details_showcard.setText("秀卡"+"("+mReviewMesses.get(0).getX_count()+")");
							stores_details_cengcard.setText("蹭卡"+"("+mReviewMesses.get(0).getC_count()+")");
							stores_details_review.setText("点评"+"("+mReviewMesses.get(0).getD_count()+")");
							stores_details_showcard_hover.setText("秀卡"+"("+mReviewMesses.get(0).getX_count()+")");
							stores_details_cengcard_hover.setText("蹭卡"+"("+mReviewMesses.get(0).getC_count()+")");
							stores_details_review_hover.setText("点评"+"("+mReviewMesses.get(0).getD_count()+")");
						}
							stores_details_review_lv.setVisibility(View.VISIBLE);
							stores_details_shopcard_lv.setVisibility(View.GONE);
							adapterReview.notifyDataSetChanged();
							hvoverView(stores_details_review_lv,false);
					}
//					stores_details_shopcard_lv.setFocusable(false);
//					setListViewHeightBasedOnChildren(stores_details_shopcard_lv);
				}
			}
		});
		
	}
	private void hvoverView(ListView listview,final boolean flag) {
		listview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem >=1) {
					if(flag){
						stores_details_review_lv.setVisibility(View.GONE);
					}
					stores_details_hover_ll.setVisibility(View.VISIBLE);
					stores_details_showcard_hover.setChecked(stores_details_showcard.isChecked());
					stores_details_cengcard_hover.setChecked(stores_details_cengcard.isChecked());
					stores_details_review_hover.setChecked(stores_details_review.isChecked());
				} else {
					stores_details_hover_ll.setVisibility(View.GONE);
				}
			}
		});
	}
	/**
	 * 加载页面数据
	 */
	private void updataview() {
		if(mShopListMess!=null){
			head_title_tv.setVisibility(View.VISIBLE);
			head_title_tv.setText(mShopListMess.getTitle());
//			ImageLoderutils imageLoderutils=new ImageLoderutils(mContext);
//			imageLoderutils.display(stores_iv, mShopListMess.getPic_urls());
			ImageLoader.getInstance().displayImage(mShopListMess.getPic_urls(), stores_iv);
			stores_details_name.setText(mShopListMess.getTitle());
			stores_details_show_card_num.setText(mShopListMess.getShowCard());
			stores_details_ceng_card_num.setText(mShopListMess.getNeedCard());
			stores_details_moods_num.setText(mShopListMess.getHot());
			if(1==mShopListMess.getNotice()){
				collect.setText("取消收藏");
			}else if(2==mShopListMess.getNotice()){
				collect.setText("收藏");
			}
			mTagsMesses=ShopListTagsMess.parse(mShopListMess.getTags());
		}
		if(mTagsMesses!=null){
			stores_details_label_gv.setAdapter(new TagsMessAdapter(mTagsMesses, mContext));
		}
		if(shopInfoMess!=null){
			tv_stores_details_address_name.setText(shopInfoMess.getShop_info_address());
			tv_stores_details_phone_name.setText(shopInfoMess.getShop_info_phoneNum());
			tv_stores_details_opening_time_name.setText(shopInfoMess.getShop_info_time());
		}else{
			tv_stores_details_address_name.setVisibility(View.GONE);
			tv_stores_details_phone_name.setVisibility(View.GONE);
			tv_stores_details_opening_time_name.setVisibility(View.GONE);
		}
		if(shopShopMess!=null){
			show_stores_details_introduction.setText(shopShopMess.getShow_shop_introduce());
		}else{
			show_stores_details_introduction.setVisibility(View.GONE);
		}
		if(shopServiceMesses!=null){
			tv_stores_details_related_services.setText("相关服务"+"("+shopServiceMesses.size()+")");
		}else{
			tv_stores_details_related_services.setText("相关服务(0)");
		}
		if(shopPeopleMesses!=null){
			tv_stores_details_craftsman_people.setText("手艺人"+"("+shopPeopleMesses.size()+")");
		}else{
			tv_stores_details_craftsman_people.setText("手艺人(0)");
		}
		stores_details_service_lv.setAdapter(new ServiceAdapter((ArrayList<ShopServiceMess>) shopServiceMesses));
		setListViewHeightBasedOnChildren(stores_details_service_lv);
		stores_details_crafstm_gv.setAdapter(new ArtisansAdapter());
		stores_details_shopcard_lv.setVisibility(View.VISIBLE);
		stores_details_bottom_ll.setVisibility(View.VISIBLE);
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
			ViewHolder vh=null;
			if(null==convertView){
				switch (index) {
				case REVIEW:
					vh=new ViewHolder();
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
					vh=new ViewHolder();
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
					vh=(ViewHolder) convertView.getTag();
					break;

				default:
					vh=(ViewHolder) convertView.getTag();
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
				if(mComcardStaPicsMess!=null){
					switch (mComcardStaPicsMess.size()) {
					case 4:
						vh.shop_commend_gv.setNumColumns(2);
						break;
					case 1:
						vh.shop_commend_gv.setNumColumns(1);
						break;
					default:
						vh.shop_commend_gv.setNumColumns(3);
						break;
					}
				}
//				if(mComcardStaPicsMess!=null&&mComcardStaPicsMess.size()>0){
					vh.shop_commend_gv.setVisibility(View.VISIBLE);	
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
//				}else{
//					vh.shop_commend_gv.setVisibility(View.GONE);	
////				}
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
				if(mComcardStaPicsMess!=null){
					switch (mComcardStaPicsMess.size()) {
					case 4:
						vh.shop_commend_gv.setNumColumns(2);
						break;
					case 1:
						vh.shop_commend_gv.setNumColumns(1);
						break;
					default:
						vh.shop_commend_gv.setNumColumns(3);
						break;
					}
				}
				if(mComcardStaPicsMess!=null&&mComcardStaPicsMess.size()>0){
					vh.shop_commend_gv.setVisibility(View.VISIBLE);	
				vh.shop_commend_gv.setAdapter(new CommendgvAdapter(mComcardStaPicsMess));	
				vh.shop_commend_gv.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
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
				}else{
					vh.shop_commend_gv.setVisibility(View.GONE);	
				}
				break;
			}
			
			return convertView;
		}
		 class ViewHolder{
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
			ViewHolder vh;
			if(null==convertView){
				vh=new ViewHolder();
				convertView=View.inflate(mContext, R.layout.activity_my_work_item, null);
				vh.shop_commend_iv=(ImageView) convertView.findViewById(R.id.my_work_item_iv);
			
				vh.shop_commend_iv.setTag(staPicsMesses.get(position).getThumbnail_pic());
				convertView.setTag(vh);
			}else{
				vh=(ViewHolder) convertView.getTag();
			}
			if(staPicsMesses!=null){
				switch (staPicsMesses.size()) {
				case 4:
					vh.shop_commend_iv.setLayoutParams(new LayoutParams((TXApplication.WINDOW_WIDTH-DensityUtil.dip2px(mContext, 62))/2,(TXApplication.WINDOW_WIDTH-DensityUtil.dip2px(mContext, 62))/2));
					break;
				case 1:
					vh.shop_commend_iv.setLayoutParams(new LayoutParams((TXApplication.WINDOW_WIDTH-DensityUtil.dip2px(mContext, 52))/1,(int) ((TXApplication.WINDOW_WIDTH-DensityUtil.dip2px(mContext, 52))/1.5)));
					break;
				default:
					vh.shop_commend_iv.setLayoutParams(new LayoutParams((TXApplication.WINDOW_WIDTH-DensityUtil.dip2px(mContext, 72))/3,(TXApplication.WINDOW_WIDTH-DensityUtil.dip2px(mContext, 72))/3));
					break;
				}
			}
			if(vh.shop_commend_iv.getTag()!=null&&vh.shop_commend_iv.getTag().equals(staPicsMesses.get(position).getThumbnail_pic())){
			ImageAware imageAware = new ImageViewAware(vh.shop_commend_iv, false);
	/*		ImageLoderutils imageLoderutils=new ImageLoderutils(mContext);
			imageLoderutils.display(vh.shop_commend_iv,mComcardStaPicsMess.get(position).getThumbnail_pic());*/
			ImageLoader.getInstance().displayImage(staPicsMesses.get(position).getThumbnail_pic(),imageAware);
			}
//			Log.i("hhhh", mComcardStaPicsMess.get(position).getThumbnail_pic());
			return convertView;
		}
		
	}
	static class ViewHolder{
		ImageView shop_commend_iv;
	}
	/**
	 * 手艺人列表适配器
	 */
	class ArtisansAdapter extends BaseAdapter{

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
			vhgv.craftsm_head_pro.setLayoutParams(new LayoutParams((TXApplication.WINDOW_WIDTH-DensityUtil.dip2px(mContext, 80))/4, (TXApplication.WINDOW_WIDTH-DensityUtil.dip2px(mContext, 80))/4));
			ImageLoderutils imageLoderutils=new ImageLoderutils(mContext);
			imageLoderutils.display(vhgv.craftsm_head_pro, shopPeopleMesses.get(position).getChoice_artisans_icon());
			vhgv.craftsm_name.setText(shopPeopleMesses.get(position).getChoice_artisans_name());
			vhgv.craftsm_job.setText(shopPeopleMesses.get(position).getChoice_artisans_job());
			vhgv.craftsm_moods.setText(shopPeopleMesses.get(position).getChoice_artisans_hot()+"人气");
			vhgv.craftsm_rl.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Bundle bundle=new Bundle();
					bundle.putString("shopt_id", mShopListMess.getShop_id());
					bundle.putSerializable(CraftsmandetailsActivity.PEOPLE_LIST,(Serializable) shopPeopleMesses);
					bundle.putInt(CraftsmandetailsActivity.WHERR_FROM, CraftsmandetailsActivity.SHOP_FROM);
					bundle.putSerializable(CraftsmandetailsActivity.SERVICE_LIST, (Serializable) shopServiceMesses);
					bundle.putString("user_id", shopPeopleMesses.get(position).getChoice_artisans_id());
					bundle.putSerializable(CraftsmandetailsActivity.PEOPLE_DES, shopPeopleMesses.get(position));
					UIHelper.startActivity(mContext, CraftsmandetailsActivity.class,bundle);
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
	/**
	 * 相关服务列表适配器
	 * @author admin
	 *
	 */
	class ServiceAdapter extends BaseAdapter{
		private ArrayList<ShopServiceMess> servicelist;

		public ServiceAdapter(ArrayList<ShopServiceMess> servicelist) {
			super();
			this.servicelist = servicelist;
		}

		@Override
		public int getCount() {
			return servicelist==null?0:(servicelist.size()>2?2:servicelist.size());
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
					Bundle bundle=new Bundle();
					bundle.putString("shopt_id", mShopListMess.getShop_id());
					bundle.putSerializable("serviceDes", shopServiceMesses.get(position));
					bundle.putString("shoptitle", mShopListMess.getTitle());
					bundle.putSerializable(CraftsmandetailsActivity.SERVICE_LIST, (Serializable) shopServiceMesses);
					bundle.putSerializable(CraftsmandetailsActivity.PEOPLE_LIST, (Serializable) shopPeopleMesses);
					UIHelper.startActivity(mContext, ServiceIntroduceActivity.class,bundle);
				}
			});
			vh.service_item_reservation.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Bundle bundle=new Bundle();
					bundle.putString("shopt_id", mShopListMess.getShop_id());
					bundle.putSerializable(CraftsmandetailsActivity.SERVICE_LIST, (Serializable) shopServiceMesses);
					bundle.putSerializable("serviceDes", shopServiceMesses.get(position));
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
	/**
	 * 为了解决ListView在ScrollView中只能显示一行数据的问题
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	protected void getData() {
		getShopCard(SHOW_CARD, "秀卡",true);
	}
	 class NineStoreDetailsAdapter extends BaseAdapter {
		private int index;

		public NineStoreDetailsAdapter(int index) {
			this.index = index;
		}

		@Override
		public int getCount() {
			if(REVIEW==index){
				return null==mReviewMesses?0:mReviewMesses.size();
			}
			return mCommendcardStatusMesses == null ? 0 : mCommendcardStatusMesses
					.size();
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
			return mCommendcardStatusMesses == null ? 0 : position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder=null;
			List<ShopComcardStaPicsMess> itemList = null;
//			Log.i("dddd", itemList.size() + "");
			if (convertView == null) {
				switch (index) {
				case StoresDetailsNineActivity.REVIEW:
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.item_review_nine, parent, false);
					viewHolder = new ViewHolder();
					viewHolder.ivMore = (NineGridlayout) convertView
							.findViewById(R.id.iv_ngrid_layout_review);
					viewHolder.ivOne = (CustomImageView) convertView
							.findViewById(R.id.iv_oneimage_review);
					viewHolder.roundImage_commend=(RoundImageView) convertView.findViewById(R.id.roundImage_shop_review);
					viewHolder.shop_commend_name=(TextView) convertView.findViewById(R.id.shop_review_name);
					viewHolder.shop_commend_createtime=(TextView) convertView.findViewById(R.id.shop_review_createtime);
					viewHolder.shop_commend_chat=(TextView) convertView.findViewById(R.id.shop_review_chat);
					viewHolder.shop_commend_content=(TextView) convertView.findViewById(R.id.shop_review_content);
					convertView.setTag(viewHolder);
					break;

				default:
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.item_comemdcard_ninegridlayout, parent, false);
					viewHolder = new ViewHolder();
					viewHolder.ivMore = (NineGridlayout) convertView
							.findViewById(R.id.iv_ngrid_layout);
					viewHolder.ivOne = (CustomImageView) convertView
							.findViewById(R.id.iv_oneimage);
					viewHolder.roundImage_commend=(RoundImageView) convertView.findViewById(R.id.roundImage_shop_comcard);
					viewHolder.shop_commend_name=(TextView) convertView.findViewById(R.id.shop_comcard_name);
					viewHolder.shop_commend_createtime=(TextView) convertView.findViewById(R.id.shop_comcard_createtime);
					viewHolder.shop_commend_chat=(TextView) convertView.findViewById(R.id.shop_comcard_chat);
					viewHolder.shop_commend_content=(TextView) convertView.findViewById(R.id.shop_comcard_content);
					convertView.setTag(viewHolder);
					break;
				}
				
			} else {
				switch (index) {
				case StoresDetailsNineActivity.REVIEW:
					viewHolder = (ViewHolder) convertView.getTag();
					break;
				default:
					viewHolder = (ViewHolder) convertView.getTag();
					break;
				}
			}
			switch (index) {
			case StoresDetailsNineActivity.REVIEW:
				ImageLoderutils imLoderutils=new ImageLoderutils(mContext);
				imLoderutils.display(viewHolder.roundImage_commend,mReviewMesses.get(position).getComment_icon());
				viewHolder.shop_commend_name.setText(mReviewMesses.get(position).getComment_name());
				viewHolder.shop_commend_createtime.setText(mReviewMesses.get(position).getComment_time());
				viewHolder.shop_commend_content.setText(mReviewMesses.get(position).getComment_total());
				itemList=ShopComcardStaPicsMess.parse(mReviewMesses.get(position).getStatus_pics());
				break;
			default:
				ShopComcardStaUserMess mComcardStaUserMess=ShopComcardStaUserMess.parse(mCommendcardStatusMesses.get(position).getStatus_user());
				ImageLoderutils imageLoderutils=new ImageLoderutils(mContext);
				imageLoderutils.display(viewHolder.roundImage_commend,mComcardStaUserMess.getUser_icon());
				viewHolder.shop_commend_name.setText(mComcardStaUserMess.getUser_name());
				viewHolder.shop_commend_createtime.setText(mComcardStaUserMess.getUser_statusCreateTime());
				viewHolder.shop_commend_content.setText(mCommendcardStatusMesses.get(position).getStatus_content());
				itemList = ShopComcardStaPicsMess
						.parse(mCommendcardStatusMesses.get(position).getStatus_pics());
				break;
			}
			if (itemList == null || itemList.isEmpty()) {
				viewHolder.ivMore.setVisibility(View.GONE);
				viewHolder.ivOne.setVisibility(View.GONE);
			} else if (itemList.size() == 1) {
				viewHolder.ivMore.setVisibility(View.GONE);
				viewHolder.ivOne.setVisibility(View.VISIBLE);

				handlerOneImage(viewHolder, itemList.get(0).getThumbnail_pic());
			} else {
				viewHolder.ivMore.setVisibility(View.VISIBLE);
				viewHolder.ivOne.setVisibility(View.GONE);

				viewHolder.ivMore.setImagesData(itemList,60);
			}

			return convertView;
		}

		private void handlerOneImage(ViewHolder viewHolder, String url) {
			viewHolder.ivOne.setClickable(true);
			viewHolder.ivOne
					.setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
//			viewHolder.ivOne.setImageUrl(url);
			 ImageLoderutils xUtilsImageLoader = new ImageLoderutils(mContext);
			 xUtilsImageLoader.setChange(true);
			 xUtilsImageLoader.setLeftAndRight(60);
			 xUtilsImageLoader.display(viewHolder.ivOne, url);

		}

		class ViewHolder {
			public NineGridlayout ivMore;
			public CustomImageView ivOne;
			RoundImageView roundImage_commend;
			TextView shop_commend_name;
			TextView shop_commend_createtime;
			TextView shop_commend_chat;
			TextView shop_commend_content;
		}
	}

}
