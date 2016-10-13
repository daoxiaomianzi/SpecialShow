package com.show.specialshow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.easemob.EMCallBack;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.utils.SPUtils;
import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.constants.Constants;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.impl.CommunitySDKImpl;
import com.umeng.comm.core.login.LoginListener;
import com.umeng.comm.core.utils.Log;
import com.umeng.common.ui.util.BroadcastUtils;
import com.umeng.message.PushAgent;
import com.umeng.message.UHandler;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;

import org.json.JSONObject;

public class TXApplication extends MultiDexApplication {
	public static int WINDOW_WIDTH = 0;
	public static int WINDOW_HEIGHT = 0;
	public static TXApplication mInstance;
	public static HttpUtils httpUtils;
	public static SharedPreferences appConfig;
	public static SharedPreferences filename;
	public static final String APP_CONFIG = "app_config";
//	 * 保存在手机里面的文件名
	public static final String FILE_NAME = "share_data";
	public static String device_number = "";
	private static int currentVersionCode;
	private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
	public static boolean isShowToast = true;
	public static boolean login = false; // 登录状态
	
	public static Context applicationContext;
	public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();
	
	// login user name
	public final String PREF_USERNAME = "username";
	
	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";

	public WindowManager.LayoutParams getMywmParams() {
		return wmParams;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		initData();
		DisplayImageOptions defaultOptions = new DisplayImageOptions
				.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher) 
				.showImageOnFail(R.drawable.ic_launcher) 
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.delayBeforeLoading(100)
				.resetViewBeforeLoading(false)
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration
				.Builder(getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions)
				.memoryCacheSize(2*1024*1024)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
//				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(new BaseImageDownloader(getApplicationContext(),5*1000,30*1000))
				.denyCacheImageMultipleSizesInMemory()
				.memoryCacheExtraOptions(480, 800)
				.discCacheSize(10 * 1024 * 1024)//
				.discCacheFileCount(50)//缓存一百张图片
				.writeDebugLogs()
				.threadPoolSize(3)//线程池内加载的数量 
.threadPriority(Thread.NORM_PRIORITY - 2)
				.build();
		ImageLoader.getInstance().init(config);
	}
	private void initUmengSet() {
		CommunitySDK mCommSDK = CommunityFactory.getCommSDK(this);
		PlatformConfig.setWeixin("wx02754205cfcac490", "d6ebf1ecaf2029455bf6d58610b77eae");
        //豆瓣RENREN平台目前只能在服务器端配置
        //新浪微博
//        PlatformConfig.setSinaWeibo("275392174", "d96fb6b323c60a42ed9f74bfab1b4f7a");
        PlatformConfig.setQQZone("1105582014", "4I0F1bO5cMqymzeF");
        PushAgent.getInstance(this).setDebugMode(true);
        PushAgent.getInstance(this).setMessageHandler(new UmengMessageHandler() {
            @Override
            public void dealWithNotificationMessage(Context arg0, UMessage msg) {
                // 调用父类方法,这里会在通知栏弹出提示信息
                super.dealWithNotificationMessage(arg0, msg);
                Log.e("", "### 自行处理推送消息");
            }
        });
        PushAgent.getInstance(this).setNotificationClickHandler(new UHandler() {
            @Override
            public void handleMessage(Context context, UMessage uMessage) {
                com.umeng.comm.core.utils.Log.d("notifi", "getting message");
                try {
                    JSONObject jsonObject = uMessage.getRaw();
                    String feedid = "";
                    if (jsonObject != null) {
                        com.umeng.comm.core.utils.Log.d("json", jsonObject.toString());
                        JSONObject extra = uMessage.getRaw().optJSONObject("extra");
                        feedid = extra.optString(Constants.FEED_ID);
                    }
                    Class myclass = Class.forName(uMessage.activity);
                    Intent intent = new Intent(context, myclass);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.FEED_ID, feedid);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    com.umeng.comm.core.utils.Log.d("class", e.getMessage());
                }
            }
        });
	}
	/**
	 * 初始化数据
	 */
	private void initData() {
		mInstance = this;
		applicationContext=this;
		device_number = getDeviceId(mInstance);
		initHxStart();
		initUmengSet();
		initConfig();
		initDisplayMetrics();
		initHttpUtil();
		initState();
	}


	private void initHxStart(){
		 /**
         * this function will initialize the HuanXin SDK
         * 
         * @return boolean true if caller can continue to call HuanXin related APIs after calling onInit, otherwise false.
         * 
         * 环信初始化SDK帮助函数
         * 返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
         * 
         * for example:
         * 例子：
         * 
         * public class DemoHXSDKHelper extends HXSDKHelper
         * 
         * HXHelper = new DemoHXSDKHelper();
         * if(HXHelper.onInit(context)){
         *     // do HuanXin related work
         * }
         */
        hxSDKHelper.onInit(applicationContext);
	}
	
	
	public static TXApplication getInstance() {
		return mInstance;
	}
 

	/**
	 * 获取当前登陆用户名
	 *
	 * @return
	 */
	public String getUserName() {
	    return hxSDKHelper.getHXId();
	}

	/**
	 * 获取密码
	 *
	 * @return
	 */
	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	/**
	 * 设置用户名
	 *
	 * @param //user
	 */
	public void setUserName(String username) {
	    hxSDKHelper.setHXId(username);
	}

	/**
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
	 * 内部的自动登录需要的密码，已经加密存储了
	 *
	 * @param pwd
	 */
	public void setPassword(String pwd) {
	    hxSDKHelper.setPassword(pwd);
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout(final boolean isGCM,final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
	    hxSDKHelper.logout(isGCM,emCallBack);
	}
	/**
	 * 退出友盟微社区
	 */
	public void umengLogout(){
		CommunitySDKImpl.getInstance().logout(applicationContext, new LoginListener() {
			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(int stCode, CommUser userInfo) {
				BroadcastUtils.sendUserLogoutBroadcast(applicationContext);
			}
		});
	}
	/**
	 * 初始化配置信息
	 */
	private void initConfig() {
		appConfig = getSharedPreferences(APP_CONFIG, MODE_PRIVATE);
		filename = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
	}
	/**
	 * 判断当前版本是不是最新
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNew(Context context) {
		int oldVersionCode = TXApplication.appConfig.getInt("versionCode", 0);
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			currentVersionCode = info.versionCode;
			return oldVersionCode < currentVersionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 将当前版本号存入本地
	 */
	public static void setNew(){
		appConfig.edit().putInt("versionCode",currentVersionCode).commit();
	}
	/**
	 * 初始化屏幕尺寸
	 */
	private void initDisplayMetrics() {
		DisplayMetrics dm = getResources().getDisplayMetrics();
		WINDOW_WIDTH = dm.widthPixels;
		WINDOW_HEIGHT = dm.heightPixels;
	}
	/**
	 * 初始化网络访问工具类
	 */
	private void initHttpUtil() {
		httpUtils = new HttpUtils(6000);
		httpUtils.configDefaultHttpCacheExpiry(0);

	}
	public static RequestParams getParams() {
		RequestParams params = new RequestParams();
		return params;
	}
	/**
	 * 检测网络是否可用
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}
	public interface NoNetWorkHandler {
		void handleOnNoNetWork();
	}
	/**
	 *初始化账户状态
	 */
	private void initState() {
		login=(Boolean) SPUtils.get(getApplicationContext(), "loginSuccess", false);
	}
	// 获取手机的设备号
	public String getDeviceId(Context context) {
		String deviceId = ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		return deviceId;
	}
	/**
	 * 统一Post接口
	 */
	public static <T> void post(NoNetWorkHandler handler, Context context,
			String url, RequestParams params, RequestCallBack<T> callBack) {
		httpUtils.send(HttpRequest.HttpMethod.POST, url, params, callBack);
	}
	/**
	 * 登录成功保存用户信息
	 */
	public static void loginSuccess(UserMessage user){
		filename.edit().putString("nickname", user.getNickname()).commit();
		filename.edit().putString("icon", user.getIcon()).commit();
		filename.edit().putString("uid", user.getUid()).commit();
		filename.edit().putString("phone", user.getPhone()).commit();
		filename.edit().putBoolean("loginSuccess", user.isLogin()).commit();
		filename.edit().putInt("user_biaoshi", user.getUser_biaoshi()).commit();
		filename.edit().putBoolean("ichange",user.isIchange()).commit();
		login=true;
	}
	/**
	 * 获取用户信息
	 */
	public static UserMessage getUserMess(){
		UserMessage user=new UserMessage();
		user.setNickname(filename.getString("nickname", ""));
		user.setIcon(filename.getString("icon", ""));
		user.setUid(filename.getString("uid", ""));
		user.setPhone(filename.getString("phone", ""));
		user.setLogin(filename.getBoolean("loginSuccess", false));
		user.setUser_biaoshi(filename.getInt("user_biaoshi", 1));
		user.setIchange(filename.getBoolean("ichange",true));
		return user;
	}
	/**
	 * 退出时清除用户信息
	 */
	public static void quitLogin(){
		filename.edit().clear().commit();
		login=false;
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
}
