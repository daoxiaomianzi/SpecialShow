package com.show.specialshow.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.model.MessageNoticeMess;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.UserNumMess;
import com.show.specialshow.receiver.MyReceiver;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.CreateQRImage;
import com.show.specialshow.utils.DensityUtil;
import com.show.specialshow.utils.ImageLoderutils;
import com.show.specialshow.utils.ImmersedStatusbarUtils;
import com.show.specialshow.utils.OnTabActivityResultListener;
import com.show.specialshow.utils.RoundImageView;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.view.Two_dimensionDialog;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;

public class MyActivity extends BaseActivity implements OnTabActivityResultListener {
    // 相关控件
    private ScrollView my_scroll;
    private RoundImageView roundImage_one_border;
    private TextView my_name;
    private TextView tv_apply_merchant;
    private int height;//高度
    private RelativeLayout rl_craftsman;
    private LinearLayout my_craftsman_bottom_ll;
    private View vi_craftsman;
    private TextView focus_on_num;//关注数
    private TextView fans_num;//粉丝数
    private TextView friends_num;//好友数
    private TextView tv_unReadMess;//未读消息数
    private TextView coupons_num;//优惠劵数量
    private TextView my_reservation_num;//我的预约数量
    private ImageView my_two_code_split;
    private RelativeLayout rl_my_agency;
    //
    private static final int BAS_INFORMATION = 0x000001;
    private static final int UNREADMESS = 0x000002;


    /**
     * 获取数据
     */
    private void getData() {
        RequestParams params = TXApplication.getParams();
        String url = URLs.USER_USERNUM;
        params.addBodyParameter("uid", TXApplication.getUserMess().getUid());
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                MessageResult result = MessageResult.parse(responseInfo.result);
                if (null == result) {
                    return;
                }
                if (1 == result.getSuccess()) {
                    UserNumMess userNumMess = UserNumMess.parse(result.getData());
                    if (null != userNumMess) {
                        String couponNum = userNumMess.getCouponNum() + "";
                        String str = MessageFormat.format("优惠劵（{0}）", couponNum);
                        SpannableString style = new SpannableString(str);
                        style.setSpan(new ForegroundColorSpan(Color.RED), 3,
                                5 + couponNum.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        coupons_num.setText(style);
                        String appointmentNum = userNumMess.getAppointmentNum() + "";
                        String str1 = MessageFormat.format("我的预约（{0}）", appointmentNum);
                        SpannableString style1 = new SpannableString(str1);
                        style1.setSpan(new ForegroundColorSpan(Color.RED), 4,
                                6 + appointmentNum.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        my_reservation_num.setText(style1);
                        focus_on_num.setText(userNumMess.getAttentionNum() + "");
                        fans_num.setText(userNumMess.getFansNum() + "");
                        friends_num.setText(userNumMess.getFriendNum() + "");
                        if (2 == userNumMess.getIsMerchant()) {
                            tv_apply_merchant.setVisibility(View.GONE);
                        } else if (1 == userNumMess.getIsMerchant()) {
                            tv_apply_merchant.setVisibility(View.VISIBLE);
                        }
                        int user_biaoshi = userNumMess.getUserBiaoshi();
                        if (1 == user_biaoshi) {
                            vi_craftsman.setVisibility(View.GONE);
                            rl_craftsman.setVisibility(View.GONE);
                            my_craftsman_bottom_ll.setVisibility(View.GONE);
                        } else if (2 == user_biaoshi) {
                            vi_craftsman.setVisibility(View.VISIBLE);
                            rl_craftsman.setVisibility(View.VISIBLE);
                            my_craftsman_bottom_ll.setVisibility(View.VISIBLE);
                        } else {
                            vi_craftsman.setVisibility(View.GONE);
                            rl_craftsman.setVisibility(View.GONE);
                            my_craftsman_bottom_ll.setVisibility(View.GONE);
                        }
//                        if (1 == userNumMess.getIsAgent()) {
//                            my_two_code_split.setVisibility(View.VISIBLE);
//                            rl_my_agency.setVisibility(View.VISIBLE);
//                        } else {
//                            my_two_code_split.setVisibility(View.GONE);
//                            rl_my_agency.setVisibility(View.GONE);
//                        }
                    }

                } else {
                    UIHelper.ToastMessage(mContext, R.string.net_server_error);
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                UIHelper.ToastMessage(mContext, R.string.net_server_error);
            }
        });
    }

    @Override
    public void initData() {
        setContentView(R.layout.activity_my);
        View head = findViewById(R.id.head_rl);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ImmersedStatusbarUtils.initAfterSetContentView(mContext, head);
        }
        registerBoradcastReceiver();
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(BasicInformationActivity.MY_BASIC);
        // 注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private MyReceiver mBroadcastReceiver = new MyReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BasicInformationActivity.MY_BASIC)) {
                updataview();
            }
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void initView() {
        focus_on_num = (TextView) findViewById(R.id.focus_on_num);
        fans_num = (TextView) findViewById(R.id.fans_num);
        friends_num = (TextView) findViewById(R.id.friends_num);
        my_scroll = (ScrollView) findViewById(R.id.my_scroll);
        roundImage_one_border = (RoundImageView) findViewById(R.id.roundImage_one_border);
        my_name = (TextView) findViewById(R.id.my_name);
        rl_craftsman = (RelativeLayout) findViewById(R.id.rl_craftsman);
        my_craftsman_bottom_ll = (LinearLayout) findViewById(R.id.my_craftsman_bottom_ll);
        vi_craftsman = findViewById(R.id.vi_craftsman);
        tv_apply_merchant = (TextView) findViewById(R.id.tv_apply_merchant);
        tv_unReadMess = (TextView) findViewById(R.id.tv_unReadMess);
        my_reservation_num = (TextView) findViewById(R.id.my_reservation_num);
        coupons_num = (TextView) findViewById(R.id.coupons_num);
        my_two_code_split = (ImageView) findViewById(R.id.my_two_code_split);
        rl_my_agency = (RelativeLayout) findViewById(R.id.rl_my_agency);
    }

    @Override
    public void fillView() {
        head_title_tv.setText(R.string.my);
        head_title_tv.setVisibility(View.GONE);
        UIHelper.leftDrawable(R.color.white, mContext, head_left_tv);
        head_left_tv.setText("签到");
        head_right_tv.setVisibility(View.VISIBLE);
        Drawable rightDrawable = getResources()
                .getDrawable(R.drawable.icon_set);
        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
                rightDrawable.getMinimumHeight());
        head_right_tv.setCompoundDrawables(null, null, rightDrawable, null);
        head_right_tv.setPadding(DensityUtil.dip2px(mContext, 20), 0, DensityUtil.dip2px(mContext, 17), 0);
//        height = TXApplication.WINDOW_HEIGHT - DensityUtil.dip2px(mContext, 130);
//        my_scroll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
//                height));
        updataview();
    }

    @Override
    public void goBack(View v) {
        if (!BtnUtils.getInstance().isFastDoubleClick()) {
            return;
        }
        UIHelper.startActivity(mContext, SignInActivity.class);
    }

    @Override
    public void setListener() {

    }

    /**
     * 判断是否有未读消息
     */
    private void isMessage() {
        RequestParams params = TXApplication.getParams();
        String url = URLs.USER_ISMESSAGE;
        params.addBodyParameter("uid", TXApplication.getUserMess().getUid());
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                MessageResult result = MessageResult.parse(responseInfo.result);
                if (null == result) {
                    return;
                }
                if (1 == result.getSuccess()) {
                    try {
                        JSONObject obj = new JSONObject(result.getData());
                        int count = obj.getInt("count");
                        if (count > 0) {
                            tv_unReadMess.setVisibility(View.VISIBLE);
                            tv_unReadMess.setText(count + "");
                        } else {
                            tv_unReadMess.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    UIHelper.ToastMessage(mContext, R.string.net_work_error);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                UIHelper.ToastMessage(mContext, R.string.net_work_error);
            }
        });
    }

    /**
     * 加载数据
     */
    private void updataview() {
        String imgheadurl = (String) SPUtils.get(mContext, "icon", "");
        if (!StringUtils.isEmpty(imgheadurl)) {
            ImageLoderutils imgload = new ImageLoderutils(mContext);
            imgload.display(roundImage_one_border, imgheadurl);
        }
        my_name.setText(SPUtils.get(mContext, "nickname", "点击登录") + "");
    }

    @Override
    protected void onResume() {
        super.onResume();
//		updataview();
        getData();
        isMessage();
    }

    @Override
    public void onClick(View v) {
        if (!BtnUtils.getInstance().isFastDoubleClick()) {
            return;
        }
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.rll_roundImage_one_border://头像
                if (TXApplication.login) {
//				UIHelper.startActivity(mContext, BasicInformationActivity.class);
                    getParent().startActivityForResult(new Intent(mContext, BasicInformationActivity.class), BAS_INFORMATION);
                } else {
                    bundle.putInt(LoginActivity.FROM_LOGIN, LoginActivity.FROM_MY);
                    UIHelper.startActivity(mContext, LoginActivity.class, bundle);
                }
                break;
            case R.id.ll_focus_on://关注
                bundle.putString("type", "attention");
                UIHelper.startActivity(mContext, CommenNumActivity.class, bundle);
                break;
            case R.id.ll_fans://粉丝
                bundle.putString("type", "fans");
                UIHelper.startActivity(mContext, CommenNumActivity.class, bundle);
                break;
            case R.id.ll_friends://好友
                bundle.putString("type", "friend");
                UIHelper.startActivity(mContext, CommenNumActivity.class, bundle);
                break;
            case R.id.rl_latest_dynamic://最新动态
                UIHelper.startActivity(mContext, LatestDynamicActivity.class);
                break;

            case R.id.rl_shop_center://商户中心
                UIHelper.startActivity(mContext, BusinessCenterActivity.class);
                break;
            case R.id.ll_coupons://优惠劵
                bundle.putInt("isSelect", 0);
                UIHelper.startActivity(mContext, MyDiscountCouponActivity.class, bundle);
                break;
            case R.id.ll_my_reservation://我的预约
                UIHelper.startActivity(mContext, MyBookingActivity.class);
                break;
            case R.id.ll_invite_awards://我的邀请
                UIHelper.startActivity(mContext, MyInviteActivity.class);
                break;
            case R.id.rl_my_integral://积分商城
                UIHelper.startActivity(mContext, IntegralmMarketActivity.class);
                break;
            case R.id.rl_my_message://我的消息
                getParent().startActivityForResult(new Intent(mContext,
                        MessageNoticeActivity.class), UNREADMESS);
//                UIHelper.startActivity(mContext, MessageNoticeActivity.class);
                break;
            case R.id.rl_my_two_code://我的专属二维码
                CreateQRImage cqri = new CreateQRImage(mContext);
                Two_dimensionDialog dialog;
                dialog = new Two_dimensionDialog(
                        mContext,
                        cqri.createQRImage("http://m.teshow.com/index.php?g=User&m=Merchant&a=zhuce&uid="
                                + SPUtils.get(mContext, "uid", "")));
                if (dialog != null) {
                    dialog.cancel();
                }
                dialog.show();
                break;
            case R.id.rl_my_agency://邀请成为代理
//                CreateQRImage createQRImage = new CreateQRImage(mContext);
//                Two_dimensionDialog agencyDialog;
//                agencyDialog = new Two_dimensionDialog(
//                        mContext,
//                        createQRImage.createQRImage("http://m.teshow.com/PublicClass/Agent/addAgent?aid="
//                                + SPUtils.get(mContext, "uid", "")));
//                if (agencyDialog != null) {
//                    agencyDialog.cancel();
//                }
//                agencyDialog.show();
                bundle.putString("status_url", URLs.USER_AGENT +
                        SPUtils.get(mContext, "uid", ""));
                UIHelper.startActivity(mContext, CommonActivityActivity.class, bundle);
                break;
            case R.id.rl_craftsman://我的手艺人

                break;
            case R.id.ll_business_card://名片
                UIHelper.startActivity(mContext, BusinessCardInformationActivity.class);
                break;
            case R.id.ll_icon_works://作品
                UIHelper.startActivity(mContext, MyWorkActivity.class);
                break;
            case R.id.ll_home_page://主页
                bundle.putString("user_id", TXApplication.getUserMess().getUid());
                UIHelper.startActivity(mContext, ShowerDetailsActivity.class, bundle);
                break;
            case R.id.head_right_tv:
                UIHelper.startActivity(mContext, SetActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case BAS_INFORMATION:
                updataview();
                break;
            case UNREADMESS:
                isMessage();
                break;

            default:
                break;
        }
    }

}
