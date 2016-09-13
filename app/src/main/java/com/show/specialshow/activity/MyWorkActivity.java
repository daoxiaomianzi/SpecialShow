package com.show.specialshow.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.adapter.MyWorkAdapter;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.ShopComcardStaPicsMess;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.utils.DensityUtil;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.view.PullToRefreshView;
import com.show.specialshow.view.PullToRefreshView.OnHeaderRefreshListener;

public class MyWorkActivity extends BaseActivity {
	private PullToRefreshView my_work_pull_refresh_view;
	private GridView my_work_gv;
	private RelativeLayout rll_nodata_tv;
	private List<ShopComcardStaPicsMess> mPicsMesses = new ArrayList<ShopComcardStaPicsMess>();
	private MyWorkAdapter maAdapter;

	@Override
	public void initData() {
		setContentView(R.layout.activity_my_work);
	}

	@Override
	public void initView() {
		my_work_pull_refresh_view=(PullToRefreshView) findViewById(R.id.my_work_pull_refresh_view);
		my_work_gv=(GridView) findViewById(R.id.my_work_gv);
		rll_nodata_tv=(RelativeLayout) findViewById(R.id.rll_nodata_tv);
		rll_nodata_tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, TXApplication.WINDOW_HEIGHT-DensityUtil.dip2px(mContext, 60)));
		
	}

	@Override
	public void fillView() {
		head_title_tv.setText("我的作品");
		Drawable rightDrawable = getResources()
				.getDrawable(R.drawable.icon_camera);
		rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
				rightDrawable.getMinimumHeight());
		head_right_tv.setCompoundDrawables(null, null, rightDrawable, null);
		head_right_tv.setPadding(DensityUtil.dip2px(mContext, 20), 0, DensityUtil.dip2px(mContext, 17), 0);
		head_right_tv.setVisibility(View.VISIBLE);
	}

	@Override
	public void setListener() {
	my_work_pull_refresh_view.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				getData();
			}
		});
//	my_work_pull_refresh_view.setOnFooterRefreshListener(new OnFooterRefreshListener() {
//		
//		@Override
//		public void onFooterRefresh(PullToRefreshView view) {
//			my_work_pull_refresh_view.onFooterRefreshComplete();
//		}
//	});
		my_work_pull_refresh_view.startInit();
		my_work_gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putSerializable(
						ImagePagerActivity.EXTRA_IMAGE_URLS,
						(ArrayList<ShopComcardStaPicsMess>)mPicsMesses
						);
				bundle.putInt(
						ImagePagerActivity.EXTRA_IMAGE_INDEX,
						position);
				UIHelper.startActivity(mContext,
						ImagePagerActivity.class, bundle);
			}
		});
		my_work_gv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				createAffirmDialog("确认删除？删除后不可恢复", 2,true);
				contest_confirm_tv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						affirmDialog.dismiss();
						photoDel(position);
					}
				});
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_right_tv:
			UIHelper.startActivity(mContext, UploadWorkActivity.class);
			break;
		case R.id.contest_cancel_tv:
			affirmDialog.dismiss();
			break;

		default:
			break;
		}
		
	}
	/**
	 * 删除照片
	 */
	private void photoDel(final int position){
		RequestParams params=TXApplication.getParams();
		UserMessage user=TXApplication.getUserMess();
		String url=URLs.USER_PRODUCTIONDEL;
		params.addBodyParameter("staffid", user.getUid());
		params.addBodyParameter("index", position+"");
		TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				UIHelper.ToastMessage(mContext, R.string.net_work_error);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				MessageResult result=MessageResult.parse(responseInfo.result);
				if(null==result){
					return;
				}
				if(1==result.getSuccess()){
					mPicsMesses.remove(position);
					maAdapter.notifyDataSetChanged();
					if(mPicsMesses==null||mPicsMesses.isEmpty()){
						rll_nodata_tv.setVisibility(View.VISIBLE);
					}else{
						rll_nodata_tv.setVisibility(View.GONE);
					}
					UIHelper.ToastMessage(mContext, result.getMessage());
				}else{
					UIHelper.ToastMessage(mContext, result.getMessage());
				}
			}
		});
	}
	/**
	 *获取数据
	 */
	private void getData(){
		RequestParams params=TXApplication.getParams();
		UserMessage user=TXApplication.getUserMess();
		String url=URLs.USER_PRODUCTION;
		params.addBodyParameter("staffid", user.getUid());
		TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				my_work_pull_refresh_view.onHeaderRefreshComplete(UIHelper.getLastUpdateTime());
				UIHelper.ToastMessage(mContext, R.string.net_work_error);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				MessageResult result=MessageResult.parse(responseInfo.result);
				if(null==result){
					return;
				}
				if(1==result.getSuccess()){
					mPicsMesses=ShopComcardStaPicsMess.parse(result.getData());
					if(mPicsMesses==null||mPicsMesses.isEmpty()){
						rll_nodata_tv.setVisibility(View.VISIBLE);
					}else{
						rll_nodata_tv.setVisibility(View.GONE);
					}
					my_work_pull_refresh_view.onHeaderRefreshComplete(UIHelper.getLastUpdateTime());
					upDataView();
				}else{
					my_work_pull_refresh_view.onHeaderRefreshComplete(UIHelper.getLastUpdateTime());
				}
			}
		});
	}
/**
 * 加载视图
 */
	protected void upDataView() {
		maAdapter=new MyWorkAdapter(mContext, (ArrayList<ShopComcardStaPicsMess>) mPicsMesses,2);
		my_work_gv.setAdapter(maAdapter);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		getData();
	}

}
