package com.show.specialshow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.show.specialshow.model.RedCoupon;
import com.show.specialshow.model.RedCouponList;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.MD5Utils;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.view.PayRadioGroup;
import com.show.specialshow.view.PayRadioPurified;

import java.text.MessageFormat;

//相关控件
public class PayActivity extends BaseActivity {
    private PayRadioGroup genderGroup;
    private TextView pay_confirm;
    private TextView pay_shop_title;
    private PayRadioPurified payRadioPurified;
    private TextView tv_pay_amount;
    private TextView pay_red_coupon_tv;
    //数据
    private String shop_title;
    private String pay_amount;//支付金额
    private String shop_id;//商户id
    private String service_id;//服务id
    private int isToShop;
    private LinearLayout ll_pay_coupon;
    public static final int REQUEST_CODE_SELECT_RED_COUPON_PAY = 0X3EC;

    private RedCoupon redCoupon;
    private int totalCoupon;

    @Override
    public void initData() {
        isToShop = getIntent().getIntExtra("isToShop", 0);
        shop_title = getIntent().getStringExtra("shop_title");
        pay_amount = getIntent().getStringExtra("pay_amount");
        shop_id = getIntent().getStringExtra("shop_id");
        service_id = getIntent().getStringExtra("service_id");
        setContentView(R.layout.activity_pay);

    }

    @Override
    public void initView() {
        genderGroup = (PayRadioGroup) findViewById(R.id.genderGroup);
        pay_confirm = (TextView) findViewById(R.id.pay_confirm);
        pay_shop_title = (TextView) findViewById(R.id.pay_shop_title);
//        payRadioPurified = (PayRadioPurified) findViewById(R.id.p2);
        tv_pay_amount = (TextView) findViewById(R.id.tv_pay_amount);
        ll_pay_coupon = (LinearLayout) findViewById(R.id.ll_pay_coupon);
        pay_red_coupon_tv = (TextView) findViewById(R.id.pay_red_coupon_tv);
    }

    @Override
    public void fillView() {
        head_title_tv.setText("特秀收银台");
        switch (isToShop) {
            case 0:
                pay_shop_title.setText(shop_title);
                getCoupon();
                break;
            case 1:
                pay_shop_title.setText(shop_title + "商家到店付款");
                ll_pay_coupon.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        tv_pay_amount.setText("¥ " + pay_amount);
        pay_confirm.setText("确认支付" + pay_amount + "元");
        ((PayRadioPurified) genderGroup.getChildAt(0)).setTextDesc("您的可用积分为20000分，本次扣除"
                + pay_amount + "分");
        ((PayRadioPurified) genderGroup.getChildAt(1)).setTextDesc("本次消费可获得" + pay_amount + "积分");
        ((PayRadioPurified) genderGroup.getChildAt(2)).setTextDesc("本次消费可获得" + pay_amount + "积分");
    }

    @Override
    public void setListener() {
        genderGroup.setOnCheckedChangeListener(new PayRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(PayRadioGroup group, int checkedId) {
//                payRadioPurified = (PayRadioPurified) findViewById(checkedId);
//                for (int i = 0; i < group.getChildCount(); i++) {
//                    ((PayRadioPurified) group.getChildAt(i)).setChangeImg(checkedId);
//                }
                UIHelper.ToastMessage(mContext, "暂未开通");
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!BtnUtils.getInstance().isFastDoubleClick()) {
            return;
        }
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.pay_confirm:
                if (null != payRadioPurified) {
                    UIHelper.ToastMessage(mContext, payRadioPurified.getTextTitle());
                }
                break;
            case R.id.ll_pay_coupon://选择优惠劵
                bundle.putInt("isSelect", 1);
                bundle.putString("shop_id", shop_id);
                bundle.putString("service_id", service_id);
                UIHelper.startActivityForResult(mContext, MyDiscountCouponActivity.class, REQUEST_CODE_SELECT_RED_COUPON_PAY, bundle);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_SELECT_RED_COUPON_PAY:
                redCoupon = (RedCoupon) data
                        .getSerializableExtra("select_red_coupon");
                addRedCouponShow();
                break;

            default:
                break;
        }
    }

    private void addRedCouponShow() {
        if (null != redCoupon) {
            double coupon_amount = Double.valueOf(pay_amount) - Double.valueOf(redCoupon.getNum());
            pay_confirm.setText("确认支付" + coupon_amount + "元");
            pay_red_coupon_tv.setText(MessageFormat.format("{0}元优惠劵", redCoupon.getNum()));
        } else {
            pay_confirm.setText("确认支付" + pay_amount + "元");
            pay_red_coupon_tv.setText(totalCoupon + "张可用");
        }
    }

    protected void getCoupon() {
        RequestParams params = TXApplication.getParams();
        String url = URLs.COUPON;
        String uid = (String) SPUtils.get(mContext, "uid", "");
        params.addBodyParameter("pageSize", ConstantValue.PAGE_SIZE + "");
        params.addBodyParameter("pageNow", 1 + "");
        params.addBodyParameter("user_id", uid);
        params.addBodyParameter(ConstantValue.sign,
                MD5Utils.getMd5Str(uid + ConstantValue.SIGN));
        params.addBodyParameter("match_merchant_id", shop_id);
        params.addBodyParameter("match_service_id", service_id);
        TXApplication.post(null, mContext, url, params,
                new RequestCallBack<String>() {


                    @Override
                    public void onFailure(HttpException error, String msg) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        MessageResult result = MessageResult
                                .parse(responseInfo.result);
                        if (result == null) {
                            return;
                        }
                        if (result.getSuccess() == 1) {
                            String info = result.getData();
                            RedCouponList redCouponList = RedCouponList.parse(info);
                            if (null == redCouponList) {
                                return;
                            }
                            totalCoupon = redCouponList.getTotal();
                            pay_red_coupon_tv.setText(totalCoupon + "张可用");
                        }
                    }
                });
    }
}
