package com.show.specialshow.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import com.show.specialshow.model.MyBookingMess;
import com.show.specialshow.model.RedCoupon;
import com.show.specialshow.model.RedCouponList;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.MD5Utils;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.view.PayRadioGroup;
import com.show.specialshow.view.PayRadioPurified;

import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

//相关控件
public class PayActivity extends BaseActivity {
    private PayRadioGroup genderGroup;
    private TextView pay_confirm;
    private TextView pay_shop_title;
    private PayRadioPurified payRadioPurified;
    private TextView tv_pay_amount;
    private TextView pay_red_coupon_tv;
    //支付密码弹框视图
    private EditText payPassword_et;
    private Dialog payPasswordDialog;
    //数据
    private String shop_title;
    private String pay_amount;//支付金额
    private String shop_id;//商户id
    private String service_id;//服务id
    private MyBookingMess myBookingMess;
    //
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
        myBookingMess = (MyBookingMess) getIntent().getSerializableExtra("payMess");
        if (null != myBookingMess) {
            shop_title = myBookingMess.getShop_name();
            shop_id = myBookingMess.getShop_id();
            service_id = myBookingMess.getService_id();
            pay_amount = myBookingMess.getService_price();
        }
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
                } else {
                    payPassWordDialog();
                }
                break;
            case R.id.ll_pay_coupon://选择优惠劵
                bundle.putInt("isSelect", 1);
                bundle.putString("shop_id", shop_id);
                bundle.putString("service_id", service_id);
                bundle.putSerializable("service_price", pay_amount);
                UIHelper.startActivityForResult(mContext, MyDiscountCouponActivity.class, REQUEST_CODE_SELECT_RED_COUPON_PAY, bundle);
                break;
            case R.id.pay_password_confirm_tv://支付密码弹框确定
                if (null != payPasswordDialog) {
                    if (StringUtils.isEmpty(payPassword_et.getText().toString().trim())) {
                        UIHelper.ToastMessage(mContext, "请输入支付密码");
                    } else {
                        payPasswordDialog.dismiss();
//                        Double buy_money = Double.valueOf(novice_area_buy_input_amount
//                                .getText().toString().trim());
//                        canPay(buy_money);
                    }
                }
                break;
            case R.id.pay_password_cancel_tv://支付密码弹框取消
                if (null != payPasswordDialog) {
                    payPasswordDialog.dismiss();
                }
                break;
            case R.id.forget_pay_password://忘记支付密码
                UIHelper.startActivity(mContext, SetTradingPasswordActivity.class);
                break;
            default:
                break;
        }

    }

    private void payPassWordDialog() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View payPasswordView = inflater.inflate(R.layout.view_pay_password_dialog, null);
        payPassword_et = (EditText) payPasswordView.findViewById(R.id.pay_password);
        TextView payPassword_tv = (TextView) payPasswordView.findViewById(R.id.pay_password_dialog_content_tv);
        String coupon;
        String str;
        SpannableString style;
        String payNum = String.valueOf(coupon_amount);
        if (redCoupon != null) {
            coupon = String.valueOf(redCoupon.getNum());
            str = "您本次支付金额是" + payNum + "元，使用了一张" + coupon +
                    "元的优惠劵";
            style = new SpannableString(str);
            style.setSpan(new ForegroundColorSpan(Color.RED), 8,
                    8 + payNum.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            style.setSpan(new ForegroundColorSpan(Color.RED), 15 + payNum.length(),
                    15 + payNum.length() + coupon.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            str = "您本次支付金额是" + pay_amount + "元";
            style = new SpannableString(str);
            style.setSpan(new ForegroundColorSpan(Color.RED), 8,
                    8 + pay_amount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        payPassword_tv.setText(style);
        payPasswordDialog = UIHelper.showPayPasswordDialog(mContext, payPasswordView);
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

    double coupon_amount;

    private void addRedCouponShow() {
        if (null != redCoupon) {
            coupon_amount = Double.valueOf(pay_amount) - Double.valueOf(redCoupon.getNum());
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
        params.addBodyParameter("match_service_price", pay_amount);
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
