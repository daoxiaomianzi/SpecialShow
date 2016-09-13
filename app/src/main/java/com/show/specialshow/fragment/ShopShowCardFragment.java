package com.show.specialshow.fragment;

import java.io.Serializable;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.activity.ImagePagerActivity;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.ShopComcardStaPicsMess;
import com.show.specialshow.model.ShopComcardStaUserMess;
import com.show.specialshow.model.ShopCommendcardStatusMess;
import com.show.specialshow.utils.DensityUtil;
import com.show.specialshow.utils.ImageLoderutils;
import com.show.specialshow.utils.RoundImageView;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.xlistview.XListView;

public class ShopShowCardFragment extends BaseSearch {
	private String mType;
	private String shop_id;
	private List<ShopCommendcardStatusMess> mCommendcardStatusMesses;//秀卡动态数据
	private List<ShopComcardStaPicsMess> mComcardStaPicsMess;//图片
	private ShopComcardStaUserMess mComcardStaUserMess;

	public static ShopShowCardFragment newInstance(String type,String shop_id) {
		final ShopShowCardFragment f = new ShopShowCardFragment();

		final Bundle args = new Bundle();
		args.putString("type", type);
		args.putString("shop_id", shop_id);
		f.setArguments(args);

		return f;
	}
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mType=getArguments()!=null?getArguments().getString("type", ""):null;
		shop_id=getArguments()!=null?getArguments().getString("shop_id", ""):null;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		view = inflater.inflate(R.layout.fragment_show_lane,
				container, false);
		search_result_lv = (XListView) view.findViewById(R.id.search_result_lv);
		return view;
	}

	@Override
	public void initData() {
		adapter=new CommendCardAdapter();
	}

	@Override
	public void initView() {
		initListView();
		search_result_lv.setPullLoadEnable(false);
	}

	@Override
	public void setListener() {

	}

	@Override
	public void fillView() {

	}

	@Override
	public void onClick(View v) {

	}
	/**
	 * 获取秀卡数据
	 */
	private void getShopCard(){
		RequestParams params=TXApplication.getParams();
		String url=URLs.SHOP_CARD;
		params.addBodyParameter("shop_id", shop_id);
		params.addBodyParameter("type", mType);
		TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();
				
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				UIHelper.ToastMessage(mContext, R.string.net_work_error);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				MessageResult result=MessageResult.parse(responseInfo.result);
				if(null==result){
					UIHelper.ToastMessage(mContext, "数据加载出错，请重试!");
					return;
				}
				if(1==result.getSuccess()){
/*					if(SHOW_CARD==index){
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
							adapter=new CommendCardAdapter(SHOW_CARD);
							stores_details_shopcard_lv.setAdapter(adapter);
							adapterReview=new CommendCardAdapter(REVIEW);
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
					}*/
//					stores_details_shopcard_lv.setFocusable(false);
//					setListViewHeightBasedOnChildren(stores_details_shopcard_lv);
				}
			}
		});
		
	}
	@Override
	protected void getData() {
		RequestParams params=TXApplication.getParams();
		String url=URLs.SHOP_CARD;
		params.addBodyParameter("shop_id", shop_id);
		params.addBodyParameter("type", mType);
		TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();
				
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				UIHelper.ToastMessage(mContext, R.string.net_work_error);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				MessageResult result=MessageResult.parse(responseInfo.result);
				if(null==result){
					UIHelper.ToastMessage(mContext, "数据加载出错，请重试!");
					return;
				}
				if(1==result.getSuccess()){
					mCommendcardStatusMesses=ShopCommendcardStatusMess.parse(result.getData());
/*					if(SHOW_CARD==index){
						if(mCommendcardStatusMesses!=null){
							stores_details_showcard.setText("秀卡"+"("+mCommendcardStatusMesses.get(0).getX_count()+")");
							stores_details_review.setText("点评"+"("+mCommendcardStatusMesses.get(0).getD_count()+")");
							stores_details_cengcard.setText("蹭卡"+"("+mCommendcardStatusMesses.get(0).getC_count()+")");
							stores_details_showcard_hover.setText("秀卡"+"("+mCommendcardStatusMesses.get(0).getX_count()+")");
							stores_details_review_hover.setText("点评"+"("+mCommendcardStatusMesses.get(0).getD_count()+")");
							stores_details_cengcard_hover.setText("蹭卡"+"("+mCommendcardStatusMesses.get(0).getC_count()+")");
						}
						if(isfrist){
							adapter=new CommendCardAdapter(SHOW_CARD);
							stores_details_shopcard_lv.setAdapter(adapter);
							adapterReview=new CommendCardAdapter(REVIEW);
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
					}*/
//					stores_details_shopcard_lv.setFocusable(false);
//					setListViewHeightBasedOnChildren(stores_details_shopcard_lv);
				}
			}
		});
	}
	/**
	 * 秀卡和蹭卡，点评列表适配器
	 */
	class CommendCardAdapter extends BaseAdapter{
		
		
		@Override
		public int getCount() {
			return null==mCommendcardStatusMesses?0:mCommendcardStatusMesses.size();
		}

		@Override
		public Object getItem(int position) {
			return null==mCommendcardStatusMesses?null:mCommendcardStatusMesses.get(position);
		}

		@Override
		public long getItemId(int position) {
			return null==mCommendcardStatusMesses?0:position;
		}

		@SuppressLint("NewApi")
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder vh=null;
			if(null==convertView){
					vh=new ViewHolder();
					convertView=View.inflate(mContext,R.layout.item_shop_comcard, null);
					vh.roundImage_commend=(RoundImageView) convertView.findViewById(R.id.roundImage_shop_comcard);
					vh.shop_commend_name=(TextView) convertView.findViewById(R.id.shop_comcard_name);
					vh.shop_commend_createtime=(TextView) convertView.findViewById(R.id.shop_comcard_createtime);
					vh.shop_commend_chat=(TextView) convertView.findViewById(R.id.shop_comcard_chat);
					vh.shop_commend_gv=(GridView) convertView.findViewById(R.id.shop_comcard_gv);
					vh.shop_commend_content=(TextView) convertView.findViewById(R.id.shop_comcard_content);
					convertView.setTag(vh);
			}else{
					vh=(ViewHolder) convertView.getTag();
			}

				mComcardStaUserMess=ShopComcardStaUserMess.parse(mCommendcardStatusMesses.get(position).getStatus_user());
				ImageLoderutils imageLoderutils=new ImageLoderutils(mContext);
				imageLoderutils.display(vh.roundImage_commend,mComcardStaUserMess.getUser_icon());
				vh.shop_commend_name.setText(mComcardStaUserMess.getUser_name());
				vh.shop_commend_createtime.setText(mComcardStaUserMess.getUser_statusCreateTime());
				vh.shop_commend_content.setText(mCommendcardStatusMesses.get(position).getStatus_content());
				mComcardStaPicsMess=ShopComcardStaPicsMess.parse(mCommendcardStatusMesses.get(position).getStatus_pics());
//				if(mComcardStaPicsMess!=null&&mComcardStaPicsMess.size()>0){
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
					vh.shop_commend_gv.setVisibility(View.VISIBLE);	
				vh.shop_commend_gv.setAdapter(new CommendgvAdapter());	
				vh.shop_commend_gv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Bundle bundle=new Bundle();
						bundle.putSerializable(ImagePagerActivity.EXTRA_IMAGE_URLS,(Serializable) ShopComcardStaPicsMess.parse(mCommendcardStatusMesses.get(position).getStatus_pics()));
						bundle.putInt(ImagePagerActivity.EXTRA_IMAGE_INDEX, arg2);
						UIHelper.startActivity(getActivity(), ImagePagerActivity.class,bundle);
					}
				});
//				}else{
//					vh.shop_commend_gv.setVisibility(View.GONE);	
//				}
			
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

		@Override
		public int getCount() {
			return null==mComcardStaPicsMess?0:mComcardStaPicsMess.size();
		}

		@Override
		public Object getItem(int position) {     
			return null==mComcardStaPicsMess?null:mComcardStaPicsMess.get(position);
		}

		@Override
		public long getItemId(int position) {
			return null==mComcardStaPicsMess?0:position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if(null==convertView){
				vh=new ViewHolder();
				convertView=View.inflate(mContext, R.layout.activity_my_work_item, null);
				vh.shop_commend_iv=(ImageView) convertView.findViewById(R.id.my_work_item_iv);
			
				convertView.setTag(vh);
			}else{
				vh=(ViewHolder) convertView.getTag();
			}
			if(mComcardStaPicsMess!=null){
				switch (mComcardStaPicsMess.size()) {
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
			vh.shop_commend_iv.setTag(mComcardStaPicsMess.get(position).getThumbnail_pic());
			if(vh.shop_commend_iv.getTag()!=null&&vh.shop_commend_iv.getTag().equals(mComcardStaPicsMess.get(position).getThumbnail_pic())){
			ImageAware imageAware = new ImageViewAware(vh.shop_commend_iv, false);
	/*		ImageLoderutils imageLoderutils=new ImageLoderutils(mContext);
			imageLoderutils.display(vh.shop_commend_iv,mComcardStaPicsMess.get(position).getThumbnail_pic());*/
			ImageLoader.getInstance().displayImage(mComcardStaPicsMess.get(position).getThumbnail_pic(),imageAware);
			}
			return convertView;
		}
		class ViewHolder{
			ImageView shop_commend_iv;
		}
		
	}
}
