package com.show.specialshow.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.easemob.EMCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.DemoApplication;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.activity.MainHxActivity;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.utils.AppManager;
import com.show.specialshow.utils.IsMatcher;
import com.show.specialshow.utils.MD5Utils;
import com.show.specialshow.utils.UIHelper;

public class LoginActivity extends BaseActivity implements AMapLocationListener {
	private EditText login_phonenumber;
	private EditText login_password;
	private Button login_login;
	public 	static final String FROM_LOGIN="from_login";
	public static final int FROM_OTHER=0;
	public static final int FROM_MY=1;
	public static final int FROM_MAIN=2;
	private int from_login;
	private String currentUsername;//环信登录用户名
	private String currentPassword;//环信登录密码

	// 定位相关
	private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = null;
	// 当前定位坐标(起点)
	private double mLat=0.0d;//纬度
	private double mLon=0.0d;//经度

	@Override
	public void initData() {
		activityFlag=1;
		from_login=getIntent().getIntExtra(FROM_LOGIN, 0);
		setContentView(R.layout.activity_login);
	}

	@Override
	public void initView() {
		login_phonenumber=(EditText) findViewById(R.id.login_phonenumber);
		login_password=(EditText) findViewById(R.id.login_password);
		login_login=(Button) findViewById(R.id.login_login);
	}

	@Override
	public void fillView() {
		InitLocation();
		isCanLogin();
	}

	@Override
	public void setListener() {
		
	}

	@Override
	public void onClick(View v) {
		Bundle bundle=new Bundle();
		switch (v.getId()) {
		case R.id.login_login:
			login_login.setEnabled(false);
			login();
			break;
		case R.id.login_getback_password:
			bundle.putInt(FROM_LOGIN, from_login);
			UIHelper.startActivity(mContext, GetbackPassActivity.class,bundle);
			break;
		case R.id.head_right_tv:
			UIHelper.startActivity(mContext, RegisterActivity.class);
			break;
		case R.id.contest_confirm_tv:
			affirmDialog.cancel();
			break;
		case R.id.login_immade_register://立即注册
			bundle.putInt(FROM_LOGIN, from_login);
			UIHelper.startActivity(mContext, RegisterActivity.class,bundle);
			break;
		default:
			break;
		}
	}
	/**
	 * 登录
	 */
	private void login() {
		if(IsMatcher.isMobileNO(login_phonenumber.getText().toString())){
			RequestParams params=TXApplication.getParams();
			String url=URLs.LOGIN_URL;
			final String phone=login_phonenumber.getText().toString().trim();
			String password=login_password.getText().toString().trim();
			params.addBodyParameter("phone", phone);
			params.addBodyParameter("password", MD5Utils.getMd5Str(password));
			if(0.0d==mLat||0.0d==mLon){
				InitLocation();
				login_login.setEnabled(true);
				return;
			}else{
				params.addBodyParameter("x",mLon+"");//经度
				params.addBodyParameter("y",mLat+"");//纬度
			}
			TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException error, String mag) {
					UIHelper.ToastMessage(mContext, R.string.net_work_error);
					login_login.setEnabled(true);
				}
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					MessageResult result=MessageResult.parse(responseInfo.result);
					if(null==result){
						return;
					}
					if(1==result.getSuccess()){
						UserMessage info=UserMessage.parse(result.getData());
						loginHX(info);
					}else{
						createAffirmDialog(result.getMessage(),DIALOG_SINGLE_STPE,true);
						login_login.setEnabled(true);
					}
				}
			});
		}else{
			UIHelper.ToastMessage(mContext, "请输入正确的手机号");
			login_login.setEnabled(true);
		}
	}
	
	/**
	 * 环信登录
	 * 
	 * @param //view
	 */
	public void loginHX(final UserMessage info) {
		if (!CommonUtils.isNetWorkConnected(this)) {
			Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
			login_login.setEnabled(true);
			return;
		}
		final String phone=login_phonenumber.getText().toString().trim();
		currentUsername =info.getUid();
		String password=login_password.getText().toString().trim();
		currentPassword =MD5Utils.getMd5Str(MD5Utils.getMd5Str(password));

		if (TextUtils.isEmpty(currentUsername)) {
			Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
			login_login.setEnabled(true);
			return;
		}
		if (TextUtils.isEmpty(currentPassword)) {
			Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
			login_login.setEnabled(true);
			return;
		}
		// 调用sdk登陆方法登陆聊天服务器
		EMChatManager.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

			@Override
			public void onSuccess() {
				runOnUiThread(new Runnable() {
					public void run() {
						// 登陆成功，保存用户名密码
						TXApplication.getInstance().setUserName(currentUsername);
						TXApplication.getInstance().setPassword(currentPassword);
						info.setPhone(phone);
						info.setLogin(true);
						TXApplication.loginSuccess(info);
						login_login.setEnabled(true);
						UIHelper.ToastMessage(mContext, "登录成功");
						loginSuccess();
				try {
					//  第一次登录或者之前logout后再登录，加载所有本地群和回话
				    EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();
					// 处理好友和群组
					initializeContacts();
				} catch (Exception e) {
					e.printStackTrace();
					// 取好友或者群聊失败，不让进入主页面
					runOnUiThread(new Runnable() {
						public void run() {
							DemoHXSDKHelper.getInstance().logout(true,null);
							UIHelper.ToastMessage(mContext, R.string.net_work_error);
							login_login.setEnabled(true);
						}
					});
					return;
				}
					}
				});
			}

			@Override
			public void onProgress(int progress, String status) {
			}

			@Override
			public void onError(final int code, final String message) {
				runOnUiThread(new Runnable() {
					public void run() {
						UIHelper.ToastMessage(mContext, getString(R.string.Login_failed) + message);
						login_login.setEnabled(true);
					}
				});
			}
		});
	}
	

	private void initializeContacts() {
		Map<String, User> userlist = new HashMap<String, User>();
		// 存入内存
		((DemoHXSDKHelper)HXSDKHelper.getInstance()).setContactList(userlist);
		// 存入db
		UserDao dao = new UserDao(LoginActivity.this);
		List<User> users = new ArrayList<User>(userlist.values());
		dao.saveContactList(users);
	}
	/**
	 * 登录成功的操作
	 */
	private void loginSuccess(){
		if(FROM_MY==from_login){
			Bundle bundle =new Bundle();
			bundle.putInt("index",0);
			UIHelper.startActivity(mContext, MainActivity.class,bundle);
			AppManager.getAppManager().finishActivity(2);
		}else if(FROM_OTHER==from_login){
			finish();
		}else if(FROM_MAIN==from_login){
			Intent data=new Intent();
			UIHelper.setResult(mContext,RESULT_OK,data);
		}
	}

	/**
	 * 监听输入框的变化
	 */
	private void isCanLogin(){
		login_phonenumber.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(!TextUtils.isEmpty(s.toString())){
					flag1=true;
				}else{
					flag1=false;
				}
				isflag();
			}
		});
		login_password.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(!TextUtils.isEmpty(s.toString())){
					flag2=true;
				}else{
					flag2=false;
				}
				isflag();
			}
		});
	}
	// 登录按钮的变化
	private void isflag() {

		if (!flag1 || !flag2) {
			login_login.setEnabled(false);
		} else {
			login_login.setEnabled(true);
		}
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
	}
	/**
	 * 高德定位回调
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
	}
	@Override
	public void onDestroy() {
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
