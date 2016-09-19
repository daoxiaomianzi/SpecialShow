package com.show.specialshow.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.activity.CircleDynamicDetailActivity;
import com.show.specialshow.activity.NearbyShowFangMapActivity;
import com.show.specialshow.adapter.CircleDynamicAdapter;
import com.show.specialshow.adapter.ShowLaneAdapter;
import com.show.specialshow.contstant.ConstantValue;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.ShopLaneList;
import com.show.specialshow.model.ShopListMess;
import com.show.specialshow.model.ShopListTagsMess;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.receiver.MyReceiver;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.xlistview.XListView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ShowLaneFragment extends BaseSearch implements AMapLocationListener{

	private List<ShopListMess> mList = new ArrayList<ShopListMess>();
	private List<ShopListTagsMess> mTagsMesses=new ArrayList<ShopListTagsMess>();
	//相关控件
	private TextView show_lang_nodata_tv;
	private EditText show_lang_search_et;//搜索框
	private Button map_btn;//地图按钮
	private XListView search_result_key;//key关键字搜索结果
	// 定位相关
	private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = null;
	// 当前定位坐标(起点)
	private double mLat=0.0d;//纬度
	private double mLon=0.0d;//经度
	//通过关键字搜出来的数据
	private List<ShopListMess> keyList = new ArrayList<>();
	private int pageKeyIndex=1;
	private int totalKeyRecord;
	private int localKeyRecord = 0;
	private BaseAdapter keyAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		view = inflater.inflate(R.layout.fragment_show_lane, container, false);
		search_result_lv = (XListView) view.findViewById(R.id.search_result_lv);
		return view;
	}

	@Override
	public void initData() {
		InitLocation();
		show_lang_nodata_tv=(TextView) findViewById(R.id.show_lang_nodata_tv);
		show_lang_search_et= (EditText) findViewById(R.id.show_lang_search_et);
		map_btn= (Button) findViewById(R.id.map_btn);
		search_result_key= (XListView) findViewById(R.id.search_result_key);
		adapter=new ShowLaneAdapter(mList,mContext);
	}

	@Override
	public void initView() {

	}

	@Override
	public void setListener() {
		show_lang_search_et.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int start, int count,
										  int after) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				if(StringUtils.isEmpty(editable.toString().trim())){
					map_btn.setBackgroundResource(R.drawable.icon_map);
					map_btn.setText("");
					if(null!=keyList){
						keyList.clear();
					}
					search_result_key.setVisibility(View.GONE);
					search_result_lv.setVisibility(View.VISIBLE);
				}else{
					map_btn.setBackgroundColor(Color.WHITE);
					map_btn.setText(R.string.cancel);
				}
			}
		});
		map_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				clickMapBtn();
			}
		});
		show_lang_search_et.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					// 先隐藏键盘
					UIHelper.isVisable(mContext,show_lang_search_et);
					//进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
					search();
				}
				return false;
			}
		});
	}

	/**
	 * 搜索结果
	 */
	private void search(){
		if(StringUtils.isEmpty(show_lang_search_et.getText().toString().trim())){
			UIHelper.ToastMessage(mContext,"请输入关键字");
		}else{
			keyAdapter = new ShowLaneAdapter(keyList,mContext);
			initKeyListView();
		}
	}

	/**
	 * 通过关键字key搜索的秀坊列表
	 */
	private void getKeyData(){
		String key = show_lang_search_et.getText().toString().trim();//秀坊关键字
		RequestParams params=TXApplication.getParams();
		String url=URLs.SHOP_SHOPLIST;
		UserMessage user=TXApplication.getUserMess();
		params.addBodyParameter("uid", user.getUid());
		params.addBodyParameter("num",""+ ConstantValue.PAGE_SIZE);
		params.addBodyParameter("key",key);
		params.addBodyParameter("index",pageIndex+"");
		if(0.0d==mLat||0.0d==mLon){
			UIHelper.ToastMessage(mContext,"获取位置失败,请重试!");
		}else{
			params.addBodyParameter("longitude",mLon+"");//经度
			params.addBodyParameter("latitude",mLat+"");//纬度
		}
		TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				onKeyError(getResources().getString(R.string.net_server_error));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				MessageResult result = MessageResult.parse(responseInfo.result);
				if(null==result){
					onKeyError(getResources().getString(R.string.no_data_search));
					return;
				}
				if(1==result.getSuccess()){
					ShopLaneList shopLaneList = ShopLaneList.parse(result.getData());
					List<ShopListMess> List=shopLaneList.getList();
					if(null==List){
						UIHelper.ToastMessage(mContext,"无搜索结果");
						changeKeyListView(0);
						return;
					}
					int size= List.size();
					totalKeyRecord=shopLaneList.getTotal();
					if(totalKeyRecord<=ConstantValue.PAGE_SIZE){
						search_result_key.setPullLoadEnable(false);
					}
					if (search_result_key.getState() == XListView.LOAD_REFRESH) {
						keyList.clear();
					}
					keyList.addAll(List);
					for (int i = 0; i < keyList.size(); i++) {
						for (int j = keyList.size() - 1; j > i; j--) {
							if (keyList.get(j).getShop_id()
									.equals(keyList.get(i).getShop_id())) {
								keyList.remove(j);
							}
						}
					}
					if(keyList==null||keyList.isEmpty()){
						search_result_key.setVisibility(View.VISIBLE);
						search_result_lv.setVisibility(View.GONE);
						show_lang_nodata_tv.setVisibility(View.VISIBLE);
					}else{
						search_result_key.setVisibility(View.VISIBLE);
						search_result_lv.setVisibility(View.GONE);
						show_lang_nodata_tv.setVisibility(View.GONE);
					}
					localKeyRecord=keyList.size();
//					totalRecord = -1;
					changeKeyListView(size);
				}else{
					changeKeyListView(0);
					UIHelper.ToastMessage(mContext, result.getMessage());
				}
			}
		});
	}

	@Override
	public void fillView() {

	}
	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(CircleDynamicDetailActivity.ACTION_NAME);
		// 注册广播
		mContext.registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	private MyReceiver mBroadcastReceiver = new MyReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(CircleDynamicDetailActivity.ACTION_NAME)) {
				if(null!=mList){
					mList.clear();
				}
				pageIndex=1;
				search_result_lv.setState(XListView.LOAD_REFRESH);
				getData();
			}
		}

	};

	@Override
	public void onClick(View v) {

	}
	/**
	 地图和取消的时候做不同的操作
	 */
	private void clickMapBtn(){
		if(StringUtils.isEmpty(map_btn.getText().toString().trim())){//当为地图按钮时
			UIHelper.startActivity(getActivity(), NearbyShowFangMapActivity.class);
		}else{//当为取消按钮时
			show_lang_search_et.setText("");
			UIHelper.isVisable(mContext,show_lang_search_et);
			if(null!=keyList){
				keyList.clear();
			}
			search_result_key.setVisibility(View.GONE);
			search_result_lv.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void getData() {
		RequestParams params=TXApplication.getParams();
		String url=URLs.SHOP_SHOPLIST;
		UserMessage user=TXApplication.getUserMess();
		params.addBodyParameter("uid", user.getUid());
		params.addBodyParameter("num",""+ ConstantValue.PAGE_SIZE);
		params.addBodyParameter("city", SPUtils.get(mContext,"city","上海").toString());
		params.addBodyParameter("index",pageIndex+"");
		if(0.0d==mLat||0.0d==mLon){
			InitLocation();
			return;
		}else{
			params.addBodyParameter("longitude",mLon+"");//经度
			params.addBodyParameter("latitude",mLat+"");//纬度
		}
		TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				if(null!=dialog){
					dialog.dismiss();
				}
				onError(getResources().getString(R.string.net_work_error));
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				MessageResult result = MessageResult.parse(responseInfo.result);
				if(null==result){
					if(null!=dialog){
						dialog.dismiss();
					}
					onError(getResources().getString(R.string.net_server_error));
					return;
				}
				if(1==result.getSuccess()){
					if(null!=dialog){
						dialog.dismiss();
					}
					ShopLaneList shopLaneList = ShopLaneList.parse(result.getData());
					List<ShopListMess> List=shopLaneList.getList();
					if(null==List){
						changeListView(0);
						search_result_key.setVisibility(View.GONE);
						search_result_lv.setVisibility(View.VISIBLE);
						show_lang_nodata_tv.setVisibility(View.VISIBLE);
						search_result_lv.setPullLoadEnable(false);
						return;
					}
					int size= List.size();
					totalRecord=shopLaneList.getTotal();
					if(totalRecord>ConstantValue.PAGE_SIZE){
						search_result_lv.setPullLoadEnable(true);
					}else{
						search_result_lv.setPullLoadEnable(false);
					}
					if (search_result_lv.getState() == XListView.LOAD_REFRESH) {
						mList.clear();
					}
					mList.addAll(List);
					for (int i = 0; i < mList.size(); i++) {
						for (int j = mList.size() - 1; j > i; j--) {
							if (mList.get(j).getShop_id()
									.equals(mList.get(i).getShop_id())) {
								mList.remove(j);
							}
						}
					}
					if(mList==null||mList.isEmpty()){
						search_result_key.setVisibility(View.GONE);
						search_result_lv.setVisibility(View.VISIBLE);
						show_lang_nodata_tv.setVisibility(View.VISIBLE);
						search_result_lv.setPullLoadEnable(false);
					}else{
						search_result_key.setVisibility(View.GONE);
						search_result_lv.setVisibility(View.VISIBLE);
						show_lang_nodata_tv.setVisibility(View.GONE);
					}
					localRecord=mList.size();
//					totalRecord = -1;
					changeListView(size);
				}else{
					if(null!=dialog){
						dialog.dismiss();
					}
					changeListView(0);
					UIHelper.ToastMessage(mContext, result.getMessage());
				}
			}
		});
	}
	/**
	 * 初始化地图定位
	 * @param
	 */
	private void InitLocation() {
		locationClient = new AMapLocationClient(mContext.getApplicationContext());
		locationOption = new AMapLocationClientOption();
		// 设置定位模式为高精度模式
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
	 * 高德地图定位回调
	 * @param aMapLocation
     */
	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
		if(null==aMapLocation){
			UIHelper.ToastMessage(mContext,"获取当前位置失败");
			return;
		}
			mLat=aMapLocation.getLatitude();
			mLon=aMapLocation.getLongitude();
		locationClient.stopLocation();
		initListView();
		registerBoradcastReceiver();
		search_result_lv.setPullLoadEnable(true);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(mBroadcastReceiver);
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

	private void initKeyListView() {
		search_result_key.setDividerHeight(15);
		search_result_key.setPullLoadEnable(true);
		search_result_key.setPullRefreshEnable(false);
		search_result_key.setXListViewListener(new XListView.IXListViewListener() {

			@Override
			public void onRefresh() {
				pageKeyIndex = 1;
				getKeyData();
			}

			@Override
			public void onLoadMore() {
				pageKeyIndex++;
				getKeyData();
			}

			@Override
			public void onInit() {
				getKeyData();

			}
		});
		search_result_key.setAdapter(keyAdapter);
	}

	private void changeKeyListView(int size) {
		stopLoad();
		if (size < ConstantValue.PAGE_SIZE || localKeyRecord == totalKeyRecord) {
			search_result_key.loadFull();
		}
		if(size==0&&localKeyRecord==0){
			search_result_key.loadEmpty();
		}
		if (search_result_key.getState() == XListView.LOAD_INIT)
			search_result_key.setSelectionFromTop(0, 0);
		keyAdapter.notifyDataSetChanged();
	}

	private  void onKeyError(String prompt) {
		UIHelper.ToastMessage(mContext, prompt);
		stopKeyLoad();
		search_result_key.onError();
	}

	private void stopKeyLoad() {
		switch (search_result_key.getState()) {
			case XListView.LOAD_INIT:
				search_result_key.stopInit();
				break;
			case XListView.LOAD_REFRESH:
				search_result_key.stopRefresh();
				break;
			case XListView.LOAD_MORE:
				search_result_key.stopLoadMore();
				break;
		}
	}
}
