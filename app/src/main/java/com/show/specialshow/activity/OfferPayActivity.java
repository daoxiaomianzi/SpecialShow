package com.show.specialshow.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.utils.UIHelper;

import org.apache.commons.lang3.StringUtils;

public class OfferPayActivity extends BaseActivity {
    //相关控件
    private EditText et_monetary;
    private TextView tv_confirm_pay;



    @Override
    public void initData() {
        setContentView(R.layout.activity_offer_pay);
    }

    @Override
    public void initView() {
        et_monetary= (EditText) findViewById(R.id.et_monetary);
        tv_confirm_pay= (TextView) findViewById(R.id.tv_confirm_pay);
    }

    @Override
    public void fillView() {
        head_title_tv.setText("优惠买单");
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_confirm_pay:
                if (StringUtils.isEmpty(et_monetary.getText().toString().trim())) {
                    UIHelper.ToastMessage(mContext,"请输入金额");
                }else{

                }
                break;
        }
    }
}