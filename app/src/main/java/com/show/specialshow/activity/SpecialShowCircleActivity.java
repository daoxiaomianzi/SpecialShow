package com.show.specialshow.activity;

import android.annotation.TargetApi;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.easemob.chatuidemo.activity.MainHxActivity;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.utils.ImmersedStatusbarUtils;
import com.show.specialshow.utils.OnTabActivityResultListener;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.ShareServiceFactory;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.view.ActionItem;
import com.show.specialshow.view.TitlePopup;
import com.umeng.comm.core.beans.ShareContent;

public class SpecialShowCircleActivity extends BaseActivity implements OnTabActivityResultListener/*,AMapLocationListener*/ {

    private TabHost tabHost;
    private LocalActivityManager localActivityManager;

    private RadioGroup show_circle_head_menu_rg;
    private RadioButton show_circle_head_dynamic_rb;
    //    private RadioButton show_circle_head_nearby_rb;
    private static final int SELECT_ADDRESS = 0x000002;//选择城市
    //	private TextView special_show_address;//城市
//    private ImageView circle_red_small;//消息通知小红点

    // 定位相关
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private int index = 0;

    // 添加pop相关
    private TitlePopup titlePopup;

    @Override
    public void initData() {
    }

    @Override
    public void initView() {
        initLocView();
//		InitLocation();/
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
//		special_show_address=(TextView) findViewById(R.id.special_show_address);
//        circle_red_small = (ImageView) findViewById(R.id.circle_red_small);
        show_circle_head_menu_rg = (RadioGroup) findViewById(R.id.show_circle_head_menu_rg);
        show_circle_head_dynamic_rb = (RadioButton) findViewById(R.id.show_circle_head_dynamic_rb);
//        show_circle_head_nearby_rb = (RadioButton) findViewById(R.id.show_circle_head_nearby_rb);
        tabHost = (TabHost) findViewById(R.id.myTabHost);
        localActivityManager = new LocalActivityManager(this, true);
        localActivityManager.dispatchResume();
        tabHost.setup(localActivityManager);
        Intent circleDynamicIntent = new Intent(mContext,
                CircleDynamicActivity.class);
//        Intent circleNearbyIntent = new Intent(mContext,
//                ShowVisitorActivity.class);
        Intent chatIntent = new Intent(mContext, MainHxActivity.class);
        tabHost.addTab(buildTabSpec("dynamic", R.string.circle_dynamic,
                R.drawable.bg_main_redio_button_left_selecter,
                circleDynamicIntent));
//        tabHost.addTab(buildTabSpec("nearby", R.string.circle_nearby,
//                R.drawable.bg_main_redio_button_center_selecter,
//                circleNearbyIntent));
        tabHost.addTab(buildTabSpec("chat", R.string.chat,
                R.drawable.bg_main_redio_button_right_selecter,
                chatIntent));
//		special_show_address.setText(SPUtils.get(mContext,"city","上海").toString());
//		SPUtils.put(mContext,"oldCity",special_show_address.getText().toString().trim());
        onListener();
    }

    @Override
    public void fillView() {
        inint();
    }

    @Override
    public void setListener() {

    }

    private void inint() {
        // 实例化标题栏弹窗
        titlePopup = new TitlePopup(this, RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        titlePopup.setItemOnClickListener(onitemClick);
        // 给标题栏弹窗添加子类
        titlePopup.addAction(new ActionItem(this, R.string.addfriend,
                R.drawable.icon_menu_addfriend));
        titlePopup.addAction(new ActionItem(this, R.string.myfriend,
                R.drawable.icon_menu_myfriend));
        titlePopup.addAction(new ActionItem(this, R.string.invite_friend,
                R.drawable.icon_invite_friends));
        titlePopup.addAction(new ActionItem(this, R.string.nearby_show_visitor, R.drawable
                .icon_nearby_show_vitior));
    }

    private TitlePopup.OnItemOnClickListener onitemClick = new TitlePopup.OnItemOnClickListener() {

        @Override
        public void onItemClick(ActionItem item, int position) {
            Bundle bundle = new Bundle();
            // mLoadingDialog.show();
            switch (position) {
                case 0:// 添加好友
                    UIHelper.startActivity(mContext,
                            AddFriendActivity.class);
                    break;
                case 1:// 我的好友
                    bundle.putInt(MainHxActivity.INDEX, 1);
                    UIHelper.startActivity(mContext,
                            com.easemob.chatuidemo.activity.LoginActivity.class, bundle);
                    break;
                case 2://邀请好友
                    inviteFriends();
                    break;
                case 3://附近秀客
                    UIHelper.startActivity(mContext, ShowVisitorActivity.class);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 邀请好友
     */
    private void inviteFriends() {
        ShareContent shareItem = new ShareContent();
        shareItem.mText = "特秀美妆:美不曾离开，让你的美由此开始";
        shareItem.mTargetUrl = "http://m.teshow.com/index.php?g=User&m=Merchant&a=zhuce&uid=" + TXApplication.getUserMess().getUid();
        shareItem.mTitle = "特秀美妆:美不曾离开，让你的美由此开始";
//                ShareSDKManager.getInstance().getCurrentSDK().share((Activity) mContext, shareItem);
        ShareServiceFactory.getShareService().share(this, shareItem);
    }


//    /**
//     * 判断是否有未读消息
//     */
//    private void isMessage() {
//        RequestParams params = TXApplication.getParams();
//        String url = URLs.USER_ISMESSAGE;
//        params.addBodyParameter("uid", TXApplication.getUserMess().getUid());
//        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
//
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                MessageResult result = MessageResult.parse(responseInfo.result);
//                if (null == result) {
//                    return;
//                }
//                if (1 == result.getSuccess()) {
//                    try {
//                        JSONObject obj = new JSONObject(result.getData());
//                        int count = obj.getInt("count");
//                        if (count > 0) {
//                            circle_red_small.setVisibility(View.VISIBLE);
//                        } else {
//                            circle_red_small.setVisibility(View.GONE);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    UIHelper.ToastMessage(mContext, R.string.net_work_error);
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                UIHelper.ToastMessage(mContext, R.string.net_work_error);
//            }
//        });
//    }

    private void onListener() {
        show_circle_head_menu_rg
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.show_circle_head_dynamic_rb:
                                index = 0;
                                tabHost.setCurrentTabByTag("dynamic");
                                break;
//                            case R.id.show_circle_head_nearby_rb:
//                                index = 1;
//                                tabHost.setCurrentTabByTag("nearby");
//                                break;
                            case R.id.show_circle_head_chat_rb:
                                Bundle bundle = new Bundle();
                                if (TXApplication.login) {
                                    if ((Boolean) SPUtils.get(mContext, "ichange", true)) {
                                        tabHost.setCurrentTabByTag("chat");
                                    } else {
                                        UIHelper.ToastMessage(mContext, "请先完善资料");
                                        bundle.putInt("from_mode", 1);
                                        UIHelper.startActivity(mContext, PerfectDataActivity.class, bundle);
                                        switch (index) {
                                            case 0:
                                                show_circle_head_dynamic_rb.setChecked(true);
                                                break;
                                            case 1:
//                                                show_circle_head_nearby_rb.setChecked(true);
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                } else {
                                    bundle.putInt(LoginActivity.FROM_LOGIN,
                                            LoginActivity.FROM_OTHER);
                                    UIHelper.startActivity(mContext, LoginActivity.class, bundle);
                                    switch (index) {
                                        case 0:
                                            show_circle_head_dynamic_rb.setChecked(true);
                                            break;
                                        case 1:
//                                            show_circle_head_nearby_rb.setChecked(true);
                                            break;
                                        default:
                                            break;
                                    }
                                }
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
//        if (TXApplication.login) {
//            isMessage();
//        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.special_show_address://选择城市
//                getParent().startActivityForResult(new Intent(mContext, SwitchCityActivity.class), SELECT_ADDRESS);
//                break;
            case R.id.send_dynamic://发布动态
                switch (tabHost.getCurrentTab()) {
                    case 1:
                        titlePopup.show(findViewById(R.id.send_dynamic));
                        break;
                    case 0:
                    default:
                        Bundle bundle = new Bundle();
                        bundle.putInt("send_type", SelectSendTypeActivity.SEND_STATE_CODE);
                        UIHelper.startActivity(mContext, SendCardActivity.class, bundle);
                        break;
                }

                break;
            case R.id.contest_confirm_tv://确定
//				special_show_address.setText(currentCity);
//				SPUtils.put(mContext,"city",currentCity);
//				Intent mIntent = new Intent(CircleDynamicDetailActivity.ACTION_NAME);
                // 发送广播
//				sendBroadcast(mIntent);
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
        ShareServiceFactory.getShareService().onActivityResult(mContext, requestCode, resultCode, data);
    }

//	@Override
//	public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
//		if (resultCode != RESULT_OK) {
//			return;
//		}
//		switch (requestCode) {
//		case SELECT_ADDRESS:
//			SPUtils.put(mContext,"oldCity",special_show_address.getText().toString().trim());
//			String city=data.getStringExtra("select_city");
//			special_show_address.setText(city);
//			Intent mIntent = new Intent(CircleDynamicDetailActivity.ACTION_NAME);
//			// 发送广播
//			sendBroadcast(mIntent);
//			break;
//			case MESSAGE_NOTICE:
//				if(TXApplication.login){
//					isMessage();
//				}
//				break;
//
//		default:
//			break;
//		}
//	}	//当前定位城市
//	private String currentCity;
//
//	/**
//	 * 高德地图定位回调
//	 * @param aMapLocation
//	 */
//	@Override
//	public void onLocationChanged(AMapLocation aMapLocation) {
//		if(aMapLocation==null){
//			UIHelper.ToastMessage(mContext,"获取当前位置失败");
//		}else{
//			if(null!=aMapLocation.getCity()){
//				currentCity = aMapLocation.getCity().substring(0,aMapLocation.getCity().length()-1);
//				if(SPUtils.get(mContext,"city","上海").toString().equals(currentCity)){
//					SPUtils.put(mContext,"city",currentCity);
//					SPUtils.put(mContext,"send_city",currentCity);
//				}else{
//					createAffirmDialog("当前定位城市为"+currentCity+",是否切换?",DIALOG_DOUBLE_STPE,true);
//				}
//			}
//			locationClient.stopLocation();
//		}
//
//		special_show_address.setText(SPUtils.get(mContext,"city","上海").toString());
//		SPUtils.put(mContext,"oldCity",special_show_address.getText().toString().trim());
////		initLocView();
//	}
//
//
//	private void InitLocation() {
//		locationClient = new AMapLocationClient(this.getApplicationContext());
//		locationOption = new AMapLocationClientOption();
//		// 设置定位模式为低功耗模式
//		locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//		// 设置定位监听
//		locationClient.setLocationListener(this);
//		// 设置是否需要显示地址信息
//		locationOption.setNeedAddress(true);
//		/**
//		 * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
//		 * 注意：只有在高精度模式下的单次定位有效，其他方式无效
//		 */
//		locationOption.setGpsFirst(true);
//		// 设置是否开启缓存
//		locationOption.setLocationCacheEnable(true);
//		//设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
//		locationOption.setOnceLocationLatest(true);
//		locationClient.setLocationOption(locationOption);
//		locationClient.startLocation();
//		special_show_address.setText("定位中");
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		if (null != locationClient) {
//			/**
//			 * 如果AMapLocationClient是在当前Activity实例化的，
//			 * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
//			 */
//			locationClient.onDestroy();
//			locationClient = null;
//			locationOption = null;
//		}
//	}

}
