package com.show.specialshow.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.MyBookingMess;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.UIHelper;

public class ReserDetailsActivity extends BaseActivity {
    //相关控件
    private TextView reser_details_shop_name;//预约门店名称
    private TextView reser_details_service_name;//服务名称
    private TextView reser_details_staff_name;//手艺人名字
    private TextView reser_details_appoinment_time;//预约时间
    private TextView reser_details_appoinment_peoplenum;//人数
    private TextView reser_details_appoinment_prices;//价格
    private TextView reser_details_appoinment_phone;//预约手机号
    private TextView reser_details_appoinment_name;//预约姓名
    private TextView reser_details_appoinment_remark;//预约备注
    private TextView reser_details_appoinment_addtime;//提交时间
    private TextView reser_details_marking_rtv;//预约状态
    private Button reser_details_cancel;//取消
    private Button reser_details_delete;//删除
    private MyBookingMess myBooking;
    private int action = 0;

    private static final int PAY = 0x000003;//支付


    @SuppressWarnings("unchecked")
    @Override
    public void initData() {
        myBooking = (MyBookingMess) getIntent().getSerializableExtra(MyBookingActivity.RESER_DETAILS);
        setContentView(R.layout.activity_reser_details);
    }

    @Override
    public void initView() {
        reser_details_shop_name = (TextView) findViewById(R.id.reser_details_shop_name);
        reser_details_service_name = (TextView) findViewById(R.id.reser_details_service_name);
        reser_details_staff_name = (TextView) findViewById(R.id.reser_details_staff_name);
        reser_details_appoinment_time = (TextView) findViewById(R.id.reser_details_appoinment_time);
        reser_details_appoinment_peoplenum = (TextView) findViewById(R.id.reser_details_appoinment_peoplenum);
        reser_details_appoinment_phone = (TextView) findViewById(R.id.reser_details_appoinment_phone);
        reser_details_appoinment_name = (TextView) findViewById(R.id.reser_details_appoinment_name);
        reser_details_appoinment_remark = (TextView) findViewById(R.id.reser_details_appoinment_remark);
        reser_details_appoinment_addtime = (TextView) findViewById(R.id.reser_details_appoinment_addtime);
        reser_details_cancel = (Button) findViewById(R.id.reser_details_cancel);
        reser_details_marking_rtv = (TextView) findViewById(R.id.reser_details_marking_rtv);
        reser_details_delete = (Button) findViewById(R.id.reser_details_delete);
        reser_details_appoinment_prices = (TextView) findViewById(R.id.reser_details_appoinment_prices);
    }

    @Override
    public void fillView() {
        head_title_tv.setText("预约详情");
        upDataView();
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {
        if (!BtnUtils.getInstance().isFastDoubleClick()) {
            return;
        }
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.reser_details_cancel://取消
                createAffirmDialog("您确定要取消该服务吗", 2, true);
                action = 1;
                break;
            case R.id.reser_details_delete://删除
                if (1 == myBooking.getStatus()) {
                    bundle.putInt("isToShop", 0);
                    bundle.putSerializable("payMess", myBooking);
                    UIHelper.startActivityForResult(mContext, PayActivity.class, PAY, bundle);
                    return;
                } else if (3 == myBooking.getStatus()) {
                    bundle.putInt(CraftsmandetailsActivity.WHERR_FROM, CraftsmandetailsActivity.ORDER_DETAIL_FROM);
                    bundle.putSerializable("orderMess", myBooking);
                    UIHelper.startActivity(mContext, OrderActivity.class, bundle);
                    return;
                }
                createAffirmDialog("您确定要删除该服务吗", 2, true);
                action = 2;
                break;
            case R.id.contest_confirm_tv:
                if (1 == action) {
                    cancel();
                } else if (2 == action) {
                    detele();
                }
                break;
            case R.id.contest_cancel_tv:
                affirmDialog.cancel();
                break;

            default:
                break;
        }
    }

    /**
     * 取消预约
     */
    private void cancel() {
        RequestParams params = TXApplication.getParams();
        String url = URLs.APPOINMENT_APPOINTREMOVE;
        params.addBodyParameter("appointment_id", myBooking.getAppointment_id());
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
                    affirmDialog.cancel();
                    UIHelper.ToastMessage(mContext, result.getMessage());
                    finish();
                } else {
                    UIHelper.ToastMessage(mContext, result.getMessage());
                }
            }
        });
    }

    /**
     * 删除预约
     */
    private void detele() {
        RequestParams params = TXApplication.getParams();
        String url = URLs.APPOINMENT_APPOINTDEL;
        params.addBodyParameter("appointment_id", myBooking.getAppointment_id());
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
                    affirmDialog.cancel();
                    UIHelper.ToastMessage(mContext, result.getMessage());
                    finish();
                } else {
                    UIHelper.ToastMessage(mContext, result.getMessage());
                }
            }
        });
    }

    /**
     * 加载视图
     */
    private void upDataView() {
        reser_details_appoinment_prices.setText(myBooking.getService_price());
        reser_details_shop_name.setText(myBooking.getShop_name());
        reser_details_service_name.setText(myBooking.getService_name());
        reser_details_staff_name.setText(myBooking.getStaff_name());
        reser_details_appoinment_time.setText(myBooking.getAppointment_time());
        reser_details_appoinment_peoplenum.setText(myBooking.getPeople_num());
        reser_details_appoinment_phone.setText(myBooking.getPeople_mobile());
        reser_details_appoinment_name.setText(myBooking.getPeople_name());
        reser_details_appoinment_remark.setText(myBooking.getPeople_remark());
        reser_details_appoinment_addtime.setText(myBooking.getAdd_time());
        switch (myBooking.getStatus()) {
            case 0:
                reser_details_cancel.setSelected(true);
                reser_details_delete.setSelected(true);
                reser_details_marking_rtv.setBackgroundResource(R.drawable.icon_to_accept);
                break;
            case 1:
                reser_details_cancel.setSelected(true);
                reser_details_cancel.setEnabled(true);
                reser_details_delete.setSelected(true);
                reser_details_delete.setEnabled(true);
                reser_details_delete.setText("支付");
                reser_details_marking_rtv.setBackgroundResource(R.drawable.icon_confirmed);
                break;
            case 3:
                reser_details_cancel.setSelected(false);
                reser_details_cancel.setEnabled(false);
                reser_details_cancel.setVisibility(View.GONE);
                reser_details_delete.setText("再来一单");
                reser_details_delete.setTextColor(Color.WHITE);
                reser_details_delete.setBackgroundResource(R.drawable.bg_send_selector);
                reser_details_marking_rtv.setBackgroundResource(R.drawable.icon_service_pay);
                break;
            case 2:
                reser_details_cancel.setSelected(false);
                reser_details_cancel.setEnabled(false);
                reser_details_delete.setSelected(true);
                reser_details_delete.setEnabled(true);
                reser_details_marking_rtv.setBackgroundResource(R.drawable.icon_cancelled);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case PAY:
                    myBooking.setStatus(3);
                    reser_details_cancel.setVisibility(View.GONE);
                    reser_details_delete.setText("再来一单");
                    reser_details_delete.setTextColor(Color.WHITE);
                    reser_details_delete.setBackgroundResource(R.drawable.bg_send_selector);
                    reser_details_marking_rtv.setBackgroundResource(R.drawable.icon_service_pay);
                    break;
            }
        }
    }
}
