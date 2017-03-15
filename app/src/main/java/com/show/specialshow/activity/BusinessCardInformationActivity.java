package com.show.specialshow.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.model.BusinessCardMess;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.utils.ImageLoderutils;
import com.show.specialshow.utils.RoundImageView;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.wheelcity.adapters.AbstractWheelTextAdapter;
import com.show.specialshow.wheelnum.OnWheelScrollListener;
import com.show.specialshow.wheelnum.WheelView;
import com.show.specialshow.wheelnum.adapter.NumericWheelAdapter;

import org.apache.commons.lang3.StringUtils;

import java.lang.ref.WeakReference;

public class BusinessCardInformationActivity extends BaseActivity {
    private static final int BUSINESS_PHOTO_CARMERA = 1;
    private static final int BUSINESS_PHOTO_PICK = 2;
    private static final int BUSINESS_PHOTO_CUT = 3;
    // 相关控件
    private static RoundImageView business_card_head_iv;
    private TextView show_fang_name;// 秀坊名
    private EditText business_card_design_et;// 手艺人名称
    private EditText business_card_title_et;// 手艺人头衔
    private TextView business_card_working_number;// 从业时间
    private TextView business_card_working_year;//
    private EditText business_card_good_at_et;// 擅长
    private EditText basic_info_introduce_hint;// 简介
    private TextView tv_ok;
    private TextView tv_cancel;
    private PopupWindow pw;
    private LinearLayout all_business_card;
    private WheelView peoplenum;
    private BusinessCardMess info;

    @SuppressLint("NewApi")
    @Override
    public void initData() {
        setContentView(R.layout.activity_business_card_information);
    }

    @Override
    public void initView() {
        business_card_head_iv = (RoundImageView) findViewById(R.id.business_card_head_iv);
        show_fang_name = (TextView) findViewById(R.id.show_fang_name);
        business_card_design_et = (EditText) findViewById(R.id.business_card_design_et);
        business_card_title_et = (EditText) findViewById(R.id.business_card_title_et);
        business_card_working_number = (TextView) findViewById(R.id.business_card_working_number);
        business_card_working_year = (TextView) findViewById(R.id.business_card_working_year);
        business_card_good_at_et = (EditText) findViewById(R.id.business_card_good_at_et);
        basic_info_introduce_hint = (EditText) findViewById(R.id.basic_info_introduce_hint);
        all_business_card = (LinearLayout) findViewById(R.id.all_business_card);
    }

    @Override
    public void fillView() {
        head_title_tv.setText(R.string.business_card_information);
        getData();
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rll_business_card_head:// 名片头像
                showSelectDialog(BUSINESS_PHOTO_PICK, BUSINESS_PHOTO_CARMERA);
                break;
            case R.id.rll_working_time:// 选择从业时间
                showSelectWorkingTime();
                break;
            case R.id.business_modify_tv://修改
                modifyBusiness();
                break;
            case R.id.rll_show_fang://所属秀坊
                Bundle bundle = new Bundle();
                bundle.putString("shop_id", info.getShop_id());
                UIHelper.startActivity(mContext, StoresDetailsActivity.class, bundle);
                break;

            default:
                break;
        }
    }

    /**
     * 修改名片信息
     */
    private void modifyBusiness() {
        UserMessage user = TXApplication.getUserMess();
        RequestParams params = TXApplication.getParams();
        String url = URLs.USER_CARDSAVE;
        params.addBodyParameter("staffid", info.getStaffid());
        params.addBodyParameter("staffname", business_card_design_et.getText().toString().trim());
        params.addBodyParameter("rank", business_card_title_et.getText().toString().trim());
        params.addBodyParameter("worktime", business_card_working_number.getText().toString().trim());
        params.addBodyParameter("goodat", business_card_good_at_et.getText().toString().trim());
        params.addBodyParameter("introduction", basic_info_introduce_hint.getText().toString().trim());
        if (null == tempFile) {
            params.addBodyParameter("type", "0");
        } else {
            params.addBodyParameter("type", "1");
            params.addBodyParameter("icon", tempFile);
        }
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                dialog = UIHelper.createProgressDialog(mContext, "更新中", true);
                dialog.show();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                UIHelper.ToastMessage(mContext, R.string.net_work_error);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                MessageResult result = MessageResult.parse(responseInfo.result);
                if (null == result) {
                    UIHelper.ToastMessage(mContext, "更新失败");
                    return;
                }
                if (1 == result.getSuccess()) {
                    UIHelper.ToastMessage(mContext, result.getMessage());
                    finish();
                } else {
                    UIHelper.ToastMessage(mContext, result.getMessage());
                }
            }
        });
    }

    /*
     * 从业时间
     */
    @SuppressLint("NewApi")
    private void showSelectWorkingTime() {

        pw = UIHelper.showPopwindow(beforShowPopWindow(getPeopleNumPick()), mContext,
                all_business_card);
        tv_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                business_card_working_number.setText((peoplenum.getCurrentItem() + 1) + "");
                pw.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
    }

    /**
     * 选择从业时间
     *
     * @return
     */
    private View getPeopleNumPick() {
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_commendpicker, null);
        peoplenum = (WheelView) view.findViewById(R.id.select_commed);
        NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(
                this, 1, 20);
        numericWheelAdapter1.setLabel("");
        peoplenum.setViewAdapter(numericWheelAdapter1);
        peoplenum.setCyclic(true);
        peoplenum.addScrollingListener(scrollListener);
        peoplenum.setVisibleItems(7);
        peoplenum.setCurrentItem(Integer.valueOf(business_card_working_number.getText()
                .toString().trim().equals("") ? "1" : business_card_working_number
                .getText().toString().trim()) - 1);
        return view;
    }

    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {

        }
    };

    /**
     * 加载popWindow之前的布局
     */
    private View beforShowPopWindow(View view) {
        LinearLayout popLayout = (LinearLayout) LayoutInflater.from(mContext)
                .inflate(R.layout.pop_window, null);
        LinearLayout ll_pop = (LinearLayout) popLayout
                .findViewById(R.id.ll_pop);
        tv_cancel = (TextView) popLayout.findViewById(R.id.tv_cancel);
        tv_ok = (TextView) popLayout.findViewById(R.id.tv_ok);
        ll_pop.addView(view);
        return popLayout;
    }

    /**
     * Adapter for countries
     */
    private class CountryAdapter extends AbstractWheelTextAdapter {
        // Countries names
        private String countries[];

        /**
         * Constructor
         */
        protected CountryAdapter(Context context, String countries[]) {
            super(context, R.layout.wheelcity_country_layout, NO_RESOURCE);
            setItemTextResource(R.id.wheelcity_country_name);
            this.countries = countries;
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return countries.length;
        }

        @Override
        protected CharSequence getItemText(int index) {
            return countries[index];
        }
    }

    private Dialog dialog = null;

    private void getData() {
        UserMessage user = TXApplication.getUserMess();
        RequestParams params = TXApplication.getParams();
        String url = URLs.USER_CARDINFO;
        params.addBodyParameter("uid", user.getUid());
        TXApplication.post(null, mContext, url, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        dialog = UIHelper.createProgressDialog(mContext, "加载中", true);
                        dialog.show();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        dialog.dismiss();
                        UIHelper.ToastMessage(mContext, R.string.net_work_error);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        MessageResult result = MessageResult
                                .parse(responseInfo.result);
                        if (null == result) {
                            dialog.dismiss();
                            UIHelper.ToastMessage(mContext, "加载失败");
                            return;
                        }
                        if (1 == result.getSuccess()) {
                            info = BusinessCardMess
                                    .parse(result.getData());
                            if (null != info) {
                                upDataView(info);
                            }
                        } else {
                            dialog.dismiss();
                        }
                    }
                });
    }

    /**
     * 加载视图
     *
     * @param info
     */
    protected void upDataView(BusinessCardMess info) {
        String imgheadurl = info.getPic();
        if (!StringUtils.isEmpty(imgheadurl)) {
            ImageLoderutils imgload = new ImageLoderutils(mContext);
            imgload.display(business_card_head_iv, imgheadurl);
        }
        show_fang_name.setText(info.getShop());
        business_card_design_et.setText(info.getStaffname());
        business_card_title_et.setText(info.getRank());
        business_card_working_number.setText(info.getWorktime());
        business_card_good_at_et.setText(info.getGoodat());
        basic_info_introduce_hint.setText(info.getIntroduction());
        dialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case BUSINESS_PHOTO_CARMERA:
                    startPhotoZoom(Uri.fromFile(tempFile), 300, BUSINESS_PHOTO_CUT);
                    break;
                case BUSINESS_PHOTO_PICK:
                    if (null != data) {
                        startPhotoZoom(data.getData(), 300, BUSINESS_PHOTO_CUT);
                    }
                    break;
                case BUSINESS_PHOTO_CUT:
                    if (null != data) {
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putBundle("data", data.getExtras());
                        message.setData(bundle);//bundle传值，耗时，效率低
                        message.what = 1;//标志是哪个线程传数据
                        basicHandler.sendMessage(message);//发送message信息
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private BasicHandler basicHandler = new BasicHandler(this);

    class BasicHandler extends Handler {
        WeakReference<BusinessCardInformationActivity> mActivity;

        BasicHandler(BusinessCardInformationActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (1 == msg.what) {
                Bundle data = msg.getData().getBundle("data");
                setPicToView(data, business_card_head_iv);
            }
        }
    }

}
