package com.show.specialshow.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.show.specialshow.contstant.ConstantValue;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.utils.AppManager;
import com.show.specialshow.utils.IsMatcher;
import com.show.specialshow.utils.JpushUtils;
import com.show.specialshow.utils.MD5Utils;
import com.show.specialshow.utils.TimeCount;
import com.show.specialshow.utils.UIHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends BaseActivity implements AMapLocationListener {
    //相关控件
    private EditText register_phonenumber;//输入手机号
    private EditText register_code;//输入验证码
    private EditText register_password;//输入密码
    private Button register_sms_code;//获取验证码
    private Button register_register;//注册
    // 计时
    private TimeCount time;
    private int from_login;//从哪里来的
    private String currentUsername;//环信登录用户名
    private String currentPassword;//环信登录密码
    // 定位相关
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    // 当前定位坐标(起点)
    private double mLat = 0.0d;//纬度
    private double mLon = 0.0d;//经度
    private String currentCity;


    @Override
    public void initData() {
        activityFlag = 1;
        from_login = getIntent().getIntExtra(LoginActivity.FROM_LOGIN, 0);
        setContentView(R.layout.activity_register);
    }

    @Override
    public void initView() {
        register_phonenumber = (EditText) findViewById(R.id.register_phonenumber);
        register_code = (EditText) findViewById(R.id.register_code);
        register_password = (EditText) findViewById(R.id.register_password);
        register_sms_code = (Button) findViewById(R.id.register_sms_code);
        register_register = (Button) findViewById(R.id.register_register);
        time = new TimeCount(ConstantValue.millisInFuture,
                ConstantValue.countDownInterval, register_sms_code, 1);
    }

    @Override
    public void fillView() {
        isCanRegister();
        InitLocation();
        head_title_tv.setText(R.string.register);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_register:
                register_register.setEnabled(false);
                register();
                break;
            case R.id.register_sms_code:
                isRegister();
                break;
            case R.id.contest_confirm_tv:
                affirmDialog.cancel();
                break;
            case R.id.register_login_ll://登录
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 注册
     */
    private void register() {
        RequestParams params = TXApplication.getParams();
        String url = URLs.REGISTER_URL;
        final String phone = register_phonenumber.getText().toString().trim();
        String code = register_code.getText().toString().trim();
        String password = register_password.getText().toString().trim();
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("code", code);
        params.addBodyParameter("password", MD5Utils.getMd5Str(password));
        if (0.0d == mLat || 0.0d == mLon) {
            InitLocation();
            register_register.setEnabled(true);
            return;
        } else {
            params.addBodyParameter("x", mLon + "");//经度
            params.addBodyParameter("y", mLat + "");//纬度
            params.addBodyParameter("current_city", currentCity);
        }
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException error, String msg) {
                UIHelper.ToastMessage(mContext, R.string.net_work_error);
                register_register.setEnabled(true);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                MessageResult result = MessageResult.parse(responseInfo.result);
                if (null == result) {
                    return;
                }
                if (1 == result.getSuccess()) {
                    UserMessage info = UserMessage.parse(result.getData());
                    loginHX(info);
                } else {
                    createAffirmDialog(result.getMessage(), DIALOG_SINGLE_STPE, true);
                    register_register.setEnabled(true);
                }
            }
        });
    }

    /**
     * 环信登录
     *
     * @param //view
     */
    public void loginHX(final UserMessage info) {
        if (!CommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            register_register.setEnabled(true);
            return;
        }
        final String phone = register_phonenumber.getText().toString().trim();
        currentUsername = info.getUid();
        String password = register_password.getText().toString().trim();
        currentPassword = MD5Utils.getMd5Str(MD5Utils.getMd5Str(password));

        if (TextUtils.isEmpty(currentUsername)) {
            Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
            register_register.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            register_register.setEnabled(true);
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
                        //调用JPush API设置Alias
                        JpushUtils jpushUtils = new JpushUtils(mContext);
                        jpushUtils.mHandler.sendMessage(jpushUtils.mHandler.
                                obtainMessage(JpushUtils.MSG_SET_ALIAS, info.getUid()));
                        register_register.setEnabled(true);
                        UIHelper.ToastMessage(mContext, "注册成功");
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
                                    register_register.setEnabled(true);
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
                        register_register.setEnabled(true);
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
        UserDao dao = new UserDao(mContext);
        List<User> users = new ArrayList<User>(userlist.values());
        dao.saveContactList(users);
    }

    /**
     * 注册成功的操作
     */
    private void loginSuccess() {
        Bundle bundle = new Bundle();
        if (LoginActivity.FROM_MY == from_login) {
            bundle.putInt("index", 0);
            UIHelper.startActivity(mContext, MainActivity.class, bundle);
            AppManager.getAppManager().finishActivity(3);
        } else if (LoginActivity.FROM_OTHER == from_login) {
            bundle.putInt("from_mode", 0);
            UIHelper.startActivity(mContext, PerfectDataActivity.class, bundle);
//            AppManager.getAppManager().finishActivity(2);
        }
    }

    /**
     * 判断手机号是否注册过
     */
    private void isRegister() {
        if ((!TextUtils.isEmpty(register_phonenumber.getText().toString())) &&
                IsMatcher.isMobileNO(register_phonenumber.getText().toString())) {
            getCode();
        } else {
            UIHelper.ToastLogMessage(mContext, "请输入正确的11位手机号");
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        RequestParams params = TXApplication.getParams();
        String url = URLs.SMS_CODE_URL;
        params.addBodyParameter("phone", register_phonenumber.getText().toString().trim());
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException error, String msg) {
                UIHelper.ToastMessage(mContext, R.string.net_work_error);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                MessageResult result = MessageResult.parse(responseInfo.result);
                if (null == result) {
                    return;
                }
                if (1 == result.getSuccess()) {
                    time.start();
//					UIHelper.ToastMessage(mContext, result.getMessage());
                } else {
                    createAffirmDialog(result.getMessage(), DIALOG_SINGLE_STPE, true);
                }
            }
        });
    }

    /**
     * 判断是否可点击注册
     */
    private void isCanRegister() {
        // 文本监听
        register_phonenumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
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

        register_code.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

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
        register_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (!TextUtils.isEmpty(s.toString())) {
                    flag3 = true;
                } else {
                    flag3 = false;
                }
                isflag();
            }
        });
    }

    // 判断
    private void isflag() {
        if (!flag1 || !flag2 || !flag3) {
            register_register.setEnabled(false);
        } else {
            register_register.setEnabled(true);
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
