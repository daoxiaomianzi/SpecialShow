package com.show.specialshow.activity;

import android.annotation.TargetApi;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.utils.ImmersedStatusbarUtils;
import com.show.specialshow.utils.OnTabActivityResultListener;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class SpecialShowCircleActivity extends BaseActivity implements OnTabActivityResultListener,AMapLocationListener {

	private TabHost tabHost;
	private LocalActivityManager localActivityManager;

	private RadioGroup show_circle_head_menu_rg;
	private RadioButton show_circle_head_dynamic_rb;
	private RadioButton show_circle_head_nearby_rb;
	private static final int SELECT_ADDRESS=0x000002;//选择城市
	private static final int MESSAGE_NOTICE=0x000003;//消息
	private TextView special_show_address;//城市
	private ImageView circle_red_small;//消息通知小红点

	// 定位相关
	private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = null;

	@Override
	public void initData() {
	}

	@Override
	public void initView() {
		initLocView();
		InitLocation();

	}

	@SuppressWarnings("deprecation")
	public void initLocView() {
		setContentView(R.layout.activity_special_show_circle);
		View head = findViewById(R.id.special_show_circle_head_rll);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			ImmersedStatusbarUtils.initAfterSetContentView(mContext, head);
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		special_show_address=(TextView) findViewById(R.id.special_show_address);
		circle_red_small= (ImageView) findViewById(R.id.circle_red_small);
		show_circle_head_menu_rg = (RadioGroup) findViewById(R.id.show_circle_head_menu_rg);
		tabHost = (TabHost) findViewById(R.id.myTabHost);
		localActivityManager = new LocalActivityManager(this, true);
		localActivityManager.dispatchResume();
		tabHost.setup(localActivityManager);
		Intent circleDynamicIntent = new Intent(mContext,
				CircleDynamicActivity.class);
		Intent circleNearbyIntent = new Intent(mContext,
				CircleNearbyActivity.class);
		tabHost.addTab(buildTabSpec("dynamic", R.string.circle_dynamic,
				R.drawable.bg_main_redio_button_left_selecter,
				circleDynamicIntent));
		tabHost.addTab(buildTabSpec("nearby", R.string.circle_nearby,
				R.drawable.bg_main_redio_button_right_selecter,
				circleNearbyIntent));
		special_show_address.setText(SPUtils.get(mContext,"city","上海").toString());
		SPUtils.put(mContext,"oldCity",special_show_address.getText().toString().trim());
		onListener();
	}

	@Override
	public void fillView() {

	}

	@Override
	public void setListener() {

	}

	/**
	 * 判断是否有未读消息
	 */
	private void isMessage(){
		RequestParams params=TXApplication.getParams();
		String url= URLs.USER_ISMESSAGE;
		params.addBodyParameter("uid",TXApplication.getUserMess().getUid());
		TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				MessageResult result = MessageResult.parse(responseInfo.result);
				if(null==result){
					return;
				}
				if(1==result.getSuccess()){
					try {
						JSONObject obj = new JSONObject(result.getData());
						int count = obj.getInt("count");
						if(count>0){
							circle_red_small.setVisibility(View.VISIBLE);
						}else{
							circle_red_small.setVisibility(View.GONE);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else{
					UIHelper.ToastMessage(mContext,R.string.net_work_error);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				UIHelper.ToastMessage(mContext,R.string.net_work_error);
			}
		});
	}

	private void onListener() {
		show_circle_head_menu_rg
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.show_circle_head_dynamic_rb:
							tabHost.setCurrentTabByTag("dynamic");
							break;
						case R.id.show_circle_head_nearby_rb:
							tabHost.setCurrentTabByTag("nearby");
							break;
						default:
							break;
						}
					}
				});
	}
	@Override
	protected void onResume() {
		super.onResume();
		if(TXApplication.login){
			isMessage();
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.special_show_address://选择城市
			getParent().startActivityForResult(new Intent(mContext, SwitchCityActivity.class), SELECT_ADDRESS);
			break;
			case R.id.message_notice://消息通知
				if(TXApplication.login){
//					UIHelper.startActivity(mContext,MessageNoticeActivity.class);
					getParent().startActivityForResult(new Intent(mContext,MessageNoticeActivity.class),MESSAGE_NOTICE);
				}else{
//					Bundle bundle=new Bundle();
//					bundle.putInt(LoginActivity.FROM_LOGIN,LoginActivity.FROM_MAIN);
//					UIHelper.startActivity(mContext,LoginActivity.class,bundle);
					Intent intent=new Intent(mContext,LoginActivity.class);
					intent.putExtra(LoginActivity.FROM_LOGIN,LoginActivity.FROM_MAIN);
					getParent().startActivityForResult(intent,MESSAGE_NOTICE);
				}
				break;
			case R.id.contest_confirm_tv://确定
				special_show_address.setText(currentCity);
				SPUtils.put(mContext,"city",currentCity);
				Intent mIntent = new Intent(CircleDynamicDetailActivity.ACTION_NAME);
				// 发送广播
				sendBroadcast(mIntent);
				affirmDialog.dismiss();
				break;
			case R.id.contest_cancel_tv://取消
				affirmDialog.dismiss();
				break;

		default:
			break;
		}
	}


	/**
	 * 公用初始化Tab
	 */
	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,
			final Intent content) {
		return tabHost
				.newTabSpec(tag)
				.setIndicator(getString(resLabel),
						getResources().getDrawable(resIcon))
				.setContent(content);
	}

	@Override
	public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case SELECT_ADDRESS:
			SPUtils.put(mContext,"oldCity",special_show_address.getText().toString().trim());
			String city=data.getStringExtra("select_city");
			special_show_address.setText(city);
			Intent mIntent = new Intent(CircleDynamicDetailActivity.ACTION_NAME);
			// 发送广播
			sendBroadcast(mIntent);
			break;
			case MESSAGE_NOTICE:
				if(TXApplication.login){
					isMessage();
				}
				break;

		default:
			break;
		}
	}
	//当前定位城市
	private String currentCity;

	/**
	 * 高德地图定位回调
	 * @param aMapLocation
     */
	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
		if(aMapLocation==null){
			UIHelper.ToastMessage(mContext,"获取当前位置失败");
		}else{
			if(null!=aMapLocation.getCity()){
				currentCity = aMapLocation.getCity().substring(0,aMapLocation.getCity().length()-1);
				if(SPUtils.get(mContext,"city","上海").toString().equals(currentCity)){
					SPUtils.put(mContext,"city",currentCity);
					SPUtils.put(mContext,"send_city",currentCity);
				}else{
					createAffirmDialog("当前定位城市为"+currentCity+",是否切换?",DIALOG_DOUBLE_STPE,true);
				}
			}
			locationClient.stopLocation();
		}

		special_show_address.setText(SPUtils.get(mContext,"city","上海").toString());
		SPUtils.put(mContext,"oldCity",special_show_address.getText().toString().trim());
//		initLocView();
	}

	private void InitLocation() {
		locationClient = new AMapLocationClient(this.getApplicationContext());
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
		special_show_address.setText("定位中");
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
