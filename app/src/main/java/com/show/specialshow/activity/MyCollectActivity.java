package com.show.specialshow.activity;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.BaseSearchActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.adapter.MyCollectAdapter;
import com.show.specialshow.contstant.ConstantValue;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.ShopLaneList;
import com.show.specialshow.model.ShopListMess;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.utils.DensityUtil;
import com.show.specialshow.utils.ImmersedStatusbarUtils;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.xlistview.XListView;

public class MyCollectActivity extends BaseSearchActivity implements AMapLocationListener{
	private TextView my_collect_nodata_tv;
	private List<ShopListMess> mList=new ArrayList<>();
	// 定位相关
	private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = null;
	// 当前定位坐标(起点)
	private double mLat=0.0d;//纬度
	private double mLon=0.0d;//经度

	@Override
	protected void getData() {
		UserMessage userMessage=TXApplication.getUserMess();
		RequestParams params=TXApplication.getParams();
		String url=URLs.USER_COLLECTLIST;
		params.addBodyParameter("uid", userMessage.getUid());
		params.addBodyParameter("page",pageIndex+"");
		params.addBodyParameter("num", ConstantValue.PAGE_SIZE+"");
		if(0.0d==mLat||0.0d==mLon){
			UIHelper.ToastMessage(mContext,"获取位置失败,请重试!");
		}else{
			params.addBodyParameter("longitude",mLon+"");//经度
			params.addBodyParameter("latitude",mLat+"");//纬度
		}
		TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				if(dialog!=null){
					dialog.dismiss();
				}
				changeListView(0);
				UIHelper.ToastMessage(mContext, R.string.net_work_error);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if(dialog!=null){
					dialog.dismiss();
				}
				MessageResult result=MessageResult.parse(responseInfo.result);
				if(null==result){
					changeListView(0);
					return;
				}
				switch (result.getSuccess()) {
				case 1:
					ShopLaneList shopLaneList = ShopLaneList.parse(result.getData());
					List<ShopListMess> list=shopLaneList.getList();
					if (null == list) {
						search_result_lv.setVisibility(View.VISIBLE);
						my_collect_nodata_tv
								.setVisibility(View.VISIBLE);
					changeListView(0);
					search_result_lv.setPullLoadEnable(false);
						return;
					}
					int size = list.size();
					totalRecord = shopLaneList.getTotal();
					if(totalRecord>ConstantValue.PAGE_SIZE){
						search_result_lv.setPullLoadEnable(true);
					}else{
						search_result_lv.setPullLoadEnable(false);
					}
					if (search_result_lv.getState() == XListView.LOAD_REFRESH) {
						mList.clear();
					}
					mList.addAll(list);
					for (int i = 0; i < mList.size(); i++) {
						for (int j = mList.size() - 1; j > i; j--) {
							if (mList.get(j).getShop_id()
									.equals(mList.get(i).getShop_id())) {
								mList.remove(j);
							}
						}
					}
					if (mList == null || mList.isEmpty()) {
						search_result_lv.setVisibility(View.VISIBLE);
						my_collect_nodata_tv
								.setVisibility(View.VISIBLE);
						search_result_lv.setPullLoadEnable(false);
					} else {
						search_result_lv.setVisibility(View.VISIBLE);
						my_collect_nodata_tv.setVisibility(View.GONE);
						search_result_lv.setPullLoadEnable(false);
					}
					localRecord = mList.size();
					changeListView(size);
//					 UIHelper.ToastLogMessage(mContext,
//					 result.getMessage());
					break;
				default:
					changeListView(0);
					mList.clear();
					search_result_lv.setVisibility(View.VISIBLE);
					my_collect_nodata_tv
							.setVisibility(View.VISIBLE);
					search_result_lv.setPullLoadEnable(false);
//					UIHelper.ToastLogMessage(mContext,
//							result.getMessage());
					break;
				}
			}
		});
	}

	@Override
	public void initData() {
		setContentView(R.layout.activity_my_collect);
//		View head = findViewById(R.id.head_rl);
//		ImmersedStatusbarUtils.initAfterSetContentView(mContext, head);
	}

	@Override
	public void initView() {
		my_collect_nodata_tv=(TextView) findViewById(R.id.my_collect_nodata_tv);
		search_result_lv=(XListView) findViewById(R.id.search_result_lv);
		adapter=new MyCollectAdapter(mList, mContext,1);
	}

	@Override
	public void fillView() {
		head_title_tv.setText("我的收藏");
		InitLocation();
	}

	@Override
	public void setListener() {
		
	}

	@Override
	public void onClick(View v) {
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		if(mLat==0.0d||mLon==0.0d){
		}else{
			search_result_lv.setState(XListView.LOAD_REFRESH);
			getData();
		}

	}

	/**
	 * 初始化地图定位
	 * @param
	 */
	private void InitLocation() {
		locationClient = new AMapLocationClient(mContext.getApplicationContext());
		locationOption = new AMapLocationClientOption();
		// 设置定位模式为低功耗模式
		locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
		// 设置定位监听
		locationClient.setLocationListener(this);
		// 设置是否需要显示地址信息
		locationOption.setNeedAddress(true);
		/**
		 * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
		 * 注意：只有在高精度模式下的单次定位有效，其他方式无效
		 */
		locationOption.setGpsFirst(true);
		// 设置是否开启缓存
		locationOption.setLocationCacheEnable(true);
		//设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
		locationOption.setOnceLocationLatest(true);
		locationClient.setLocationOption(locationOption);
		locationClient.startLocation();
		loadIng("加载中",true);
	}

	/**
	 * 高德地图定位成功回调
	 * @param aMapLocation
     */
	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
		if (aMapLocation == null) {
			UIHelper.ToastMessage(mContext,"获取当前位置失败");
			return;
		}
		mLat=aMapLocation.getLatitude();
		mLon=aMapLocation.getLongitude();
		locationClient.stopLocation();
		initListView();
		search_result_lv.setPullLoadEnable(true);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != locationClient) {
			/**
			 * 如果AMapLocationClient是在当前Activity实例化的，
			 * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
			 */
			locationClient.onDestroy();
			locationClient = null;
			locationOption = null;
		}
	}
}
