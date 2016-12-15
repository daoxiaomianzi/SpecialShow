package com.show.specialshow.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.easemob.chatuidemo.DemoHXSDKHelper;
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
import com.show.specialshow.utils.JpushUtils;
import com.show.specialshow.utils.MD5Utils;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.UIHelper;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.beans.Source;
import com.umeng.comm.core.constants.Constants;
import com.umeng.comm.core.login.LoginListener;
import com.umeng.comm.core.utils.ResFinder;
import com.umeng.community.login.CustomDialog;
import com.umeng.community.login.DefaultLoginActivity;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoginActivity extends BaseActivity implements AMapLocationListener {
    private EditText login_phonenumber;
    private EditText login_password;
    private Button login_login;
    public static final String FROM_LOGIN = "from_login";
    public static final int FROM_OTHER = 0;
    public static final int FROM_MY = 1;
    public static final int FROM_MAIN = 2;
    private int from_login;
    private String currentUsername;//环信登录用户名
    private String currentPassword;//环信登录密码


    // 定位相关
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    // 当前定位坐标(起点)
    private double mLat = 0.0d;//纬度
    private double mLon = 0.0d;//经度
    private String currentCity;
    //三方登陆相关
    private UMShareAPI mShareAPI = null;
    String uid = "";

    @Override
    public void initData() {
        activityFlag = 1;
        from_login = getIntent().getIntExtra(FROM_LOGIN, 0);
        setContentView(R.layout.activity_login);
        this.mShareAPI = UMShareAPI.get(this);

    }

    @Override
    public void initView() {
        login_phonenumber = (EditText) findViewById(R.id.login_phonenumber);
        login_password = (EditText) findViewById(R.id.login_password);
        login_login = (Button) findViewById(R.id.login_login);
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
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.login_login:
                login_login.setEnabled(false);
                login();
                break;
            case R.id.login_getback_password:
                bundle.putInt(FROM_LOGIN, from_login);
                UIHelper.startActivity(mContext, GetbackPassActivity.class, bundle);
                break;
            case R.id.head_right_tv:
                UIHelper.startActivity(mContext, RegisterActivity.class);
                break;
            case R.id.contest_confirm_tv:
                affirmDialog.cancel();
                break;
            case R.id.login_immade_register://立即注册
                bundle.putInt(FROM_LOGIN, from_login);
                UIHelper.startActivity(mContext, RegisterActivity.class, bundle);
                break;
            case R.id.qq_platform_btn://qq登陆
                loginThree(mContext, SHARE_MEDIA.QQ);
                break;
            case R.id.weixin_platform_btn://微信登陆
                loginThree(mContext, SHARE_MEDIA.WEIXIN);
                break;
            default:
                break;
        }
    }

    private void loginThree(final Context context, final SHARE_MEDIA platform) {
        this.mShareAPI.doOauthVerify(this, platform, new UMAuthListener() {
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                LoginActivity.this.uid = map.get("uid");
                if (TextUtils.isEmpty(LoginActivity.this.uid)) {
                    LoginActivity.this.uid = map.get("openid");
                }

                if (share_media == SHARE_MEDIA.WEIXIN) {
                    Constants.WX_UID = map.get("unionid");
                }

                LoginActivity.this.fetchLoginedUserInfo(context, platform, DefaultLoginActivity.mLoginListener);
            }

            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                if (DefaultLoginActivity.mLoginListener != null) {
                    Bundle data = new Bundle();
                    data.putString("msg", throwable.getMessage());
                    DefaultLoginActivity.mLoginListener.onComplete(0, new CommUser());
                }

            }

            public void onCancel(SHARE_MEDIA share_media, int i) {
                if (DefaultLoginActivity.mLoginListener != null) {
                    DefaultLoginActivity.mLoginListener.onComplete('鱀', new CommUser());
                }

            }
        });
    }

    private void fetchLoginedUserInfo(final Context context, final SHARE_MEDIA platform, final LoginListener listener) {
        final CustomDialog progressDialog = new CustomDialog(this, ResFinder.getString("umeng_socialize_load_userinfo"));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOwnerActivity(this);
        SocializeUtils.safeShowDialog(progressDialog);
        this.mShareAPI.getPlatformInfo((Activity) context, platform, new UMAuthListener() {
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Log.i("login", "logged in");
                LoginActivity.this.showMapInfo(map);
                SocializeUtils.safeCloseDialog(progressDialog);
//				LoginActivity.this.finish();
                QQWexinLogin(map, platform);
                if (listener != null) {
                    Log.i("login", "logged in");
                    CommUser user = LoginActivity.this.createNewUser(map, platform);
                    listener.onComplete(200, user);
                }

            }

            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                String msg = ResContainer.getString(context, "umeng_socialize_fetch_info_failed");
                UIHelper.ToastMessage(mContext, msg + ":" + throwable.getClass());
                SocializeUtils.safeCloseDialog(progressDialog);
            }

            public void onCancel(SHARE_MEDIA share_media, int i) {
                Log.i("loggin", "log in cancel");
            }
        });
    }

    private void showMapInfo(Map<String, String> info) {
        Set entries = info.entrySet();
        Iterator var3 = entries.iterator();

        while (var3.hasNext()) {
            Map.Entry entry = (Map.Entry) var3.next();
            Log.i("", "###" + entry.getKey() + "=" + entry.getValue());
        }

    }

    private CommUser createNewUser(Map<String, String> info, SHARE_MEDIA platform) {
        com.umeng.socialize.utils.Log.d("userinfo", info.toString());
        CommUser currentUser = new CommUser();
        if (info != null && info.size() != 0) {
            currentUser.id = String.valueOf(info.get("uid"));
            if (platform == SHARE_MEDIA.SINA) {
                currentUser.source = Source.SINA;
                currentUser.iconUrl = this.getString(info, "profile_image_url");
            } else if (platform == SHARE_MEDIA.WEIXIN) {
                currentUser.source = Source.WEIXIN;
                currentUser.id = this.uid;
                currentUser.iconUrl = this.getString(info, "headimgurl");
            } else if (platform == SHARE_MEDIA.QQ) {
                currentUser.source = Source.QQ;
                currentUser.id = this.uid;
                currentUser.iconUrl = this.getString(info, "icon_url");
            }

            Log.i("xxxxxx", "info = " + info);
            Log.i("", "### login source : " + currentUser.source == null ? "selfAccount" : currentUser.source.toString());
            currentUser.name = this.getString(info, "screen_name");
            if (TextUtils.isEmpty(currentUser.name)) {
                currentUser.name = this.getString(info, "nickname");
            }

            if (TextUtils.isEmpty(currentUser.name)) {
                currentUser.name = this.getString(info, "name");
            }

            Log.i("xxxxxxxx", "info=" + info);
            currentUser.gender = this.getGender(info);
            return currentUser;
        } else {
            return currentUser;
        }
    }

    private String getString(Map<String, String> info, String key) {
        return info.containsKey(key) ? (String) info.get(key) : "";
    }

    private CommUser.Gender getGender(Map<String, String> info) {
        if (!info.containsKey("sex") && !info.containsKey("gender")) {
            return CommUser.Gender.MALE;
        } else {
            String gender = null;
            if (info.containsKey("sex")) {
                gender = ((String) info.get("sex")).toString();
            } else {
                gender = ((String) info.get("gender")).toString();
            }

            return !gender.equals("1") && !"男".equals(gender) ? (!gender.equals("0") && !"女".equals(gender) ? CommUser.Gender.MALE : CommUser.Gender.FEMALE) : CommUser.Gender.MALE;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * QQ和微信登陆
     */
    private void QQWexinLogin(final Map<String, String> info, SHARE_MEDIA platform) {
        RequestParams params = TXApplication.getParams();

        String url = URLs.QQ_LOGIN;
        if (info != null && info.size() != 0) {
            if (0.0d == mLat || 0.0d == mLon) {
                InitLocation();
                UIHelper.ToastMessage(mContext, "出错了,请重试!");
                return;
            } else {
                params.addBodyParameter("x", mLon + "");//经度
                params.addBodyParameter("y", mLat + "");//纬度
                params.addBodyParameter("current_city", currentCity);
            }
            params.addBodyParameter("openid", getString(info, "openid"));
            params.addBodyParameter("province", getString(info, "province"));
            params.addBodyParameter("city", getString(info, "city"));

            if (platform == SHARE_MEDIA.WEIXIN) {
                params.addBodyParameter("profile_image_url", getString(info, "headimgurl"));
                params.addBodyParameter("screen_name", getString(info, "nickname"));
                String sex = getString(info, "sex");
                if ("1".equals(sex)) {
                    sex = "男";
                } else if ("0".equals(sex)) {
                    sex = "女";
                } else {
                    sex = "";
                }
                params.addBodyParameter("gender", sex);
            } else if (platform == SHARE_MEDIA.QQ) {
                params.addBodyParameter("profile_image_url", getString(info, "profile_image_url"));
                params.addBodyParameter("screen_name", getString(info, "screen_name"));
                params.addBodyParameter("gender", getString(info, "gender"));
            }
            TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
                @Override
                public void onStart() {
                    loadIng("登录中...", false);
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    MessageResult result = MessageResult.parse(responseInfo.result);
                    if (null == result) {
                        return;
                    }
                    if (1 == result.getSuccess()) {
                        UserMessage userInfo = UserMessage.parse(result.getData());
                        loginHX(userInfo, true, getString(info, "openid"));
                    } else {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        UIHelper.ToastMessage(mContext, R.string.net_work_error);
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    UIHelper.ToastMessage(mContext, R.string.net_work_error);
                }
            });
        } else {
            UIHelper.ToastMessage(mContext, "获取信息失败");
        }
    }


    /**
     * 登录
     */
    private void login() {
        if (IsMatcher.isMobileNO(login_phonenumber.getText().toString())) {
            RequestParams params = TXApplication.getParams();
            String url = URLs.LOGIN_URL;
            final String phone = login_phonenumber.getText().toString().trim();
            String password = login_password.getText().toString().trim();
            params.addBodyParameter("phone", phone);
            params.addBodyParameter("password", MD5Utils.getMd5Str(password));
            if (0.0d == mLat || 0.0d == mLon) {
                InitLocation();
                login_login.setEnabled(true);
                return;
            } else {
                params.addBodyParameter("x", mLon + "");//经度
                params.addBodyParameter("y", mLat + "");//纬度
                params.addBodyParameter("current_city", currentCity);
            }
            TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
                @Override
                public void onStart() {
                    super.onStart();
                    loadIng("登录中...", false);
                }

                @Override
                public void onFailure(HttpException error, String mag) {
                    UIHelper.ToastMessage(mContext, R.string.net_work_error);
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    login_login.setEnabled(true);
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    MessageResult result = MessageResult.parse(responseInfo.result);
                    if (null == result) {
                        return;
                    }
                    if (1 == result.getSuccess()) {
                        UserMessage info = UserMessage.parse(result.getData());
                        loginHX(info, false, "");
                    } else {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        createAffirmDialog(result.getMessage(), DIALOG_SINGLE_STPE, true);
                        login_login.setEnabled(true);
                    }
                }
            });
        } else {
            UIHelper.ToastMessage(mContext, "请输入正确的手机号");
            login_login.setEnabled(true);
        }
    }

    /**
     * 环信登录
     *
     * @param //view
     */
    public void loginHX(final UserMessage info, final boolean isThreePath, String openId) {
        if (!CommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            if (dialog != null) {
                dialog.dismiss();
            }
            if (!isThreePath) {
                login_login.setEnabled(true);
            }
            return;
        }
        if (!isThreePath) {
            String password = login_password.getText().toString().trim();
            currentPassword = MD5Utils.getMd5Str(MD5Utils.getMd5Str(password));
        } else {
            currentPassword = MD5Utils.getMd5Str(openId);
        }
        final String phone = login_phonenumber.getText().toString().trim();
        currentUsername = info.getUid();

        if (TextUtils.isEmpty(currentUsername)) {
            Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
            if (dialog != null) {
                dialog.dismiss();
            }
            if (!isThreePath) {
                login_login.setEnabled(true);
            }
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            if (dialog != null) {
                dialog.dismiss();
            }
            if (!isThreePath) {
                login_login.setEnabled(true);
            }
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
                        if (!(boolean) SPUtils.get(mContext, "setAlias", false)) {
                            //调用JPush API设置Alias
                            JpushUtils jpushUtils = new JpushUtils(mContext);
                            jpushUtils.mHandler.sendMessage(jpushUtils.mHandler.
                                    obtainMessage(JpushUtils.MSG_SET_ALIAS, info.getUid()));
                        }

                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        if (!isThreePath) {
                            login_login.setEnabled(true);
                        }
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
                                    DemoHXSDKHelper.getInstance().logout(true, null);
                                    UIHelper.ToastMessage(mContext, R.string.net_work_error);
                                    if (dialog != null) {
                                        dialog.dismiss();
                                    }
                                    if (!isThreePath) {
                                        login_login.setEnabled(true);
                                    }
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
                        UIHelper.ToastMessage(mContext, getString(R.string.Login_failed));
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        if (!isThreePath) {
                            login_login.setEnabled(true);
                        }
                    }
                });
            }
        });
    }


    private void initializeContacts() {
        Map<String, User> userlist = new HashMap<String, User>();
        // 存入内存
        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(userlist);
        // 存入db
        UserDao dao = new UserDao(LoginActivity.this);
        List<User> users = new ArrayList<User>(userlist.values());
        dao.saveContactList(users);
    }

    /**
     * 登录成功的操作
     */
    private void loginSuccess() {

        if (FROM_MY == from_login) {
            Bundle bundle = new Bundle();
            bundle.putInt("index", 0);
            UIHelper.startActivity(mContext, MainActivity.class, bundle);
            AppManager.getAppManager().finishActivity(2);
        } else if (FROM_OTHER == from_login) {
            finish();
        } else if (FROM_MAIN == from_login) {
            Intent data = new Intent();
            UIHelper.setResult(mContext, RESULT_OK, data);
        }
    }

    /**
     * 监听输入框的变化
     */
    private void isCanLogin() {
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
                if (!TextUtils.isEmpty(s.toString())) {
                    flag1 = true;
                } else {
                    flag1 = false;
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
                if (!TextUtils.isEmpty(s.toString())) {
                    flag2 = true;
                } else {
                    flag2 = false;
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
     *
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
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (null == aMapLocation) {
            UIHelper.ToastMessage(mContext, "获取当前位置失败");
            return;
        }
        mLat = aMapLocation.getLatitude();
        mLon = aMapLocation.getLongitude();
        currentCity = aMapLocation.getCity().substring(0, aMapLocation.getCity().length() - 1);

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
