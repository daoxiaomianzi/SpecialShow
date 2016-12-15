package com.show.specialshow.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.BaseSearchActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.adapter.RedCouponSwipeAdapter;
import com.show.specialshow.contstant.ConstantValue;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.RedCoupon;
import com.show.specialshow.model.RedCouponList;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.DensityUtil;
import com.show.specialshow.utils.MD5Utils;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.xlistview.XListView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class MyDiscountCouponActivity extends BaseSearchActivity {

    private RedCoupon mSelectCoupon;
    private List<RedCoupon> mlist_coupon = new ArrayList<RedCoupon>();
    private TextView red_coupon_no_data;
    private TextView no_use_coupon;//
    private DecimalFormat df = new DecimalFormat("0.00");
    private int isSelect = 0;//0：全部优惠劵，1：查找符合条件的优惠劵
    private String shop_id;//商户id
    private String service_id;//服务id
    private String service_price;//服务总费用

    @Override
    public void initData() {
        isSelect = getIntent().getIntExtra("isSelect", 0);
        shop_id = getIntent().getStringExtra("shop_id");
        service_id = getIntent().getStringExtra("service_id");
        service_price = getIntent().getStringExtra("service_price");
        adapter = new RedCouponSwipeAdapter(mlist_coupon, mContext,isSelect);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_select_red_coupon);
        search_result_lv = (XListView) findViewById(R.id.search_result_lv);
        red_coupon_no_data = (TextView) findViewById(R.id.red_coupon_no_data);
        no_use_coupon = (TextView) findViewById(R.id.no_use_coupon);
    }

    @Override
    public void fillView() {
        head_title_tv.setText("优惠券");
        head_left_tv.setVisibility(View.VISIBLE);
        if (0 == isSelect) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) no_use_coupon.getLayoutParams();
            params.height = 1;
            no_use_coupon.setLayoutParams(params);
        }
        initListView();
        search_result_lv.setPullLoadEnable(false);
        search_result_lv.setDividerHeight(DensityUtil.dip2px(mContext, 10));
        search_result_lv.setBackgroundResource(R.color.app_bg);

    }

    @Override
    public void setListener() {
        search_result_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (!BtnUtils.getInstance().isFastDoubleClick()) {
                    return;
                }
                if (1 == isSelect) {
                    mSelectCoupon = mlist_coupon.get(position - 1);
                    Intent data = new Intent();
                    data.putExtra("select_red_coupon", mSelectCoupon);
                    UIHelper.setResult(mContext, -1, data);
                }
            }
        });
    }

    @Override
    protected void getData() {
        RequestParams params = TXApplication.getParams();
        String url = URLs.COUPON;
        String uid = (String) SPUtils.get(mContext, "uid", "");
        params.addBodyParameter("pageSize", ConstantValue.PAGE_SIZE + "");
        params.addBodyParameter("pageNow", pageIndex + "");
        params.addBodyParameter("user_id", uid);
        params.addBodyParameter(ConstantValue.sign,
                MD5Utils.getMd5Str(uid + ConstantValue.SIGN));
        switch (isSelect) {
            case 0:
                break;
            case 1:
                params.addBodyParameter("match_merchant_id", shop_id);
                params.addBodyParameter("match_service_id", service_id);
                params.addBodyParameter("match_service_price", service_price);
                break;
            default:
                break;
        }
        TXApplication.post(null, mContext, url, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        loadIng("加载中...", true);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        onError(getResources().getString(
                                R.string.net_work_error));
                        dialog.dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        MessageResult result = MessageResult
                                .parse(responseInfo.result);
                        if (result == null) {
                            if (null != dialog) {
                                dialog.dismiss();
                            }
                            onError(getResources().getString(R.string.net_server_error));
                            return;
                        }
                        if (result.getSuccess() == 1) {
                            if (null != dialog) {
                                dialog.dismiss();
                            }
                            String info = result.getData();
                            RedCouponList redCouponList = RedCouponList.parse(info);
                            List<RedCoupon> list = redCouponList.getList();
                            if (null == redCouponList || null == list || list.isEmpty()) {
                                changeListView(0);
                                search_result_lv.setVisibility(View.VISIBLE);
                                search_result_lv.setPullLoadEnable(false);
                                red_coupon_no_data.setVisibility(View.VISIBLE);
                                red_coupon_no_data.setText("暂无优惠劵");
                                return;
                            }
                            int size = list.size();
                            totalRecord = redCouponList.getTotal();
                            if (totalRecord > ConstantValue.PAGE_SIZE) {
                                search_result_lv.setPullLoadEnable(true);
                            } else {
                                search_result_lv.setPullLoadEnable(false);
                            }
                            if (search_result_lv.getState() == XListView.LOAD_REFRESH) {
                                mlist_coupon.clear();
                            }
                            mlist_coupon.addAll(list);
                            if (mlist_coupon == null || mlist_coupon.isEmpty()) {
                                String no_data_show = "暂无优惠券";
                                search_result_lv.setVisibility(View.VISIBLE);
                                red_coupon_no_data.setVisibility(View.VISIBLE);
                                red_coupon_no_data.setText(no_data_show);
                                search_result_lv.setPullLoadEnable(false);
                            } else {
                                search_result_lv.setVisibility(View.VISIBLE);
                                red_coupon_no_data.setVisibility(View.GONE);
                            }
                            localRecord = mlist_coupon.size();
                            changeListView(size);
                        } else {
                            changeListView(0);
                            if (null != dialog) {
                                dialog.dismiss();
                            }
                        }
                    }
                });
    }

    public void onClick(View v) {
        if (!BtnUtils.getInstance().isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.no_use_coupon://不使用优惠劵
                Intent data = new Intent();
                UIHelper.setResult(mContext, -1, data);
                break;
        }
    }
}
