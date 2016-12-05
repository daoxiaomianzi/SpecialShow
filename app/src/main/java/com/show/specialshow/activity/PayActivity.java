package com.show.specialshow.activity;

import android.view.View;
import android.widget.TextView;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.view.PayRadioGroup;
import com.show.specialshow.view.PayRadioPurified;

//相关控件
public class PayActivity extends BaseActivity {
    private PayRadioGroup genderGroup;
    private TextView pay_confirm;
    private TextView pay_shop_title;
    private PayRadioPurified payRadioPurified;
    private TextView tv_pay_amount;
    //数据
    private String shop_title;
    private String pay_amount;//支付金额
    private int isToShop;


    @Override
    public void initData() {
        isToShop = getIntent().getIntExtra("isToShop", 0);
        shop_title = getIntent().getStringExtra("shop_title");
        pay_amount = getIntent().getStringExtra("pay_amount");
        setContentView(R.layout.activity_pay);

    }

    @Override
    public void initView() {
        genderGroup = (PayRadioGroup) findViewById(R.id.genderGroup);
        pay_confirm = (TextView) findViewById(R.id.pay_confirm);
        pay_shop_title = (TextView) findViewById(R.id.pay_shop_title);
        payRadioPurified = (PayRadioPurified) findViewById(R.id.p2);
        tv_pay_amount = (TextView) findViewById(R.id.tv_pay_amount);
    }

    @Override
    public void fillView() {
        head_title_tv.setText("特秀收银台");
        switch (isToShop) {
            case 0:
                pay_shop_title.setText(shop_title);
                break;
            case 1:
                pay_shop_title.setText(shop_title + "商家到店付款");
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
                payRadioPurified = (PayRadioPurified) findViewById(checkedId);
                for (int i = 0; i < group.getChildCount(); i++) {
                    ((PayRadioPurified) group.getChildAt(i)).setChangeImg(checkedId);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_confirm:
                UIHelper.ToastMessage(mContext, payRadioPurified.getTextTitle());
                break;
        }

    }
}
