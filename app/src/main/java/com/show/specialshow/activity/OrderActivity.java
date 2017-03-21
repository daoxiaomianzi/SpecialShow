package com.show.specialshow.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.model.CraftsmanIntroduceMess;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.MyBookingMess;
import com.show.specialshow.model.ShopPeopleMess;
import com.show.specialshow.model.ShopServiceMess;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.ImmersedStatusbarUtils;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.view.DateTimePicker;
import com.show.specialshow.view.DateTimePicker.OnDateTimeChangedListener;
import com.show.specialshow.view.DateTimePickerDialog;
import com.show.specialshow.view.DateTimePickerDialog.OnDateTimeSetListener;
import com.show.specialshow.wheelnum.OnWheelScrollListener;
import com.show.specialshow.wheelnum.WheelView;
import com.show.specialshow.wheelnum.adapter.NumericWheelAdapter;
import com.show.specialshow.widget.DatePicker;
import com.show.specialshow.widget.TimePicker;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrderActivity extends BaseActivity {
    private LayoutInflater inflater = null;
    private static final int SELECT_SERVICE = 0x000001;
    private static final int SELECT_CRAFTSMAN = 0x000002;
    private ShopServiceMess shopServiceMess;// 服务的信息
    private ShopPeopleMess shopPeopleMess;// 手艺人信息
    private CraftsmanIntroduceMess craftsmanIntroduceMess;// 手艺人信息
    private List<ShopPeopleMess> shopPeopleMesses = new ArrayList<>();
    private List<ShopServiceMess> shopServiceMesses = new ArrayList<>();
    private String shop_id;
    private String staff_id;//手艺人id
    private String service_name;//服务名字
    private String service_id;//服务id
    private String service_price_now;
    private String service_price_old;
    private String service_price_discount;
    // 相关控件
    private RelativeLayout rl_order_service;
    private TextView order_service_name;// 服务名
    private TextView tv_order_switch_service;// 选择服务
    private TextView order_service_cheap_price;// 当前的价格
    private TextView order_service_price;// 原始价格
    private TextView order_service_price_num;
    private TextView order_people_num_tv;
    private TextView order_time;// 预约时间
    private TextView order_people_num;// 预约人数
    private TextView order_craftseman_people;// 预约的手艺人
    private EditText order_contact;// 联系方式
    private EditText order_name;//输入姓名
    private TextView order_remark;// 备注
    private View order_service_vi;
    private View timepickerview;
    private PopupWindow mPopupWindow;
    private PopupWindow pw;
    private View view;
    private WheelView peoplenum;// 滚动的人数
    private Calendar calendar;
    private String selectDate, selectTime;
    private RelativeLayout ll_all;// 整个视图
    private RelativeLayout rl_order_craftsman_people;// 选择手艺人
    private ImageView iv_order_craftsman_people_right;
    private TextView free_order;// 预约
    // 选择时间与当前时间，用于判断用户选择的是否是以前的时间
    private int currentHour, currentMinute, currentDay, selectHour,
            selectMinute, selectDay;
    private DatePicker dp_test;
    private TimePicker tp_test;
    private TextView tv_time_ok, tv_time_cancel; // 确定、取消button
    private DateTimePicker mDateTimePicker;
    private Calendar mDate = Calendar.getInstance();
    private boolean mIs24HourView;
    private int whree_from;
    private UserMessage user;
    private MessageResult result;
    private MyBookingMess bookingMess;

    @SuppressWarnings("unchecked")
    @Override
    public void initData() {
        whree_from = getIntent().getExtras().getInt(
                CraftsmandetailsActivity.WHERR_FROM);
        if (3 == whree_from) {
            bookingMess = (MyBookingMess) getIntent().getSerializableExtra("orderMess");
            if (null != bookingMess) {
                shop_id = bookingMess.getShop_id();
                staff_id = bookingMess.getStaff_id();
                service_id = bookingMess.getService_id();
                service_name = bookingMess.getService_name();
            }
        } else if (4 == whree_from) {
            shop_id = getIntent().getStringExtra("shop_id");
            service_id = getIntent().getStringExtra("service_id");
            service_name = getIntent().getStringExtra("service_name");
            service_price_now = getIntent().getStringExtra("service_price_now");
            service_price_old = getIntent().getStringExtra("service_price_old");
            service_price_discount = getIntent().getStringExtra("service_price_discount");
            shopPeopleMesses = ShopPeopleMess.parse(getIntent().getStringExtra("staff_list"));
        } else {
            if (2 == whree_from) {
                craftsmanIntroduceMess = (CraftsmanIntroduceMess) getIntent()
                        .getSerializableExtra(CraftsmandetailsActivity.PEOPLE_DES);
                if (null != craftsmanIntroduceMess) {
                    shop_id = craftsmanIntroduceMess.getCratsman_introduce_shopId();
                    staff_id = craftsmanIntroduceMess.getCratsman_introduce_id();
                }
            } else {
                shopPeopleMess = (ShopPeopleMess) getIntent().getSerializableExtra(
                        CraftsmandetailsActivity.PEOPLE_DES);
                shop_id = getIntent().getStringExtra("shop_id");
                if (null != shopPeopleMess) {
                    staff_id = shopPeopleMess.getChoice_artisans_staffid();
                }
            }
            shopServiceMesses = (List<ShopServiceMess>) getIntent()
                    .getSerializableExtra(CraftsmandetailsActivity.SERVICE_LIST);
            shopServiceMess = (ShopServiceMess) getIntent().getSerializableExtra(
                    "serviceDes");
            if (null != shopServiceMess) {
                service_name = shopServiceMess.getService_list_title();
                service_id = shopServiceMess.getService_list_id();
            }
            shopPeopleMesses = (List<ShopPeopleMess>) getIntent()
                    .getSerializableExtra(CraftsmandetailsActivity.PEOPLE_LIST);
        }

        setContentView(R.layout.activity_order);
        View head = findViewById(R.id.order_head);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ImmersedStatusbarUtils.initAfterSetContentView(mContext, head);
            head.setFitsSystemWindows(false);
        }
    }

    @Override
    public void initView() {
        free_order = (TextView) findViewById(R.id.free_order);
        order_name = (EditText) findViewById(R.id.order_name);
        iv_order_craftsman_people_right = (ImageView) findViewById(R.id.iv_order_craftsman_people_right);
        rl_order_craftsman_people = (RelativeLayout) findViewById(R.id.rl_order_craftsman_people);
        rl_order_service = (RelativeLayout) findViewById(R.id.rl_order_service);
        tv_order_switch_service = (TextView) findViewById(R.id.tv_order_switch_service);
        order_service_name = (TextView) findViewById(R.id.order_service_name);
        order_service_cheap_price = (TextView) findViewById(R.id.order_service_cheap_price);
        order_service_price = (TextView) findViewById(R.id.order_service_price);
        order_service_price_num = (TextView) findViewById(R.id.order_service_price_num);
        order_time = (TextView) findViewById(R.id.order_time);
        order_people_num = (TextView) findViewById(R.id.order_people_num);
        order_people_num_tv = (TextView) findViewById(R.id.order_people_num_tv);
        order_craftseman_people = (TextView) findViewById(R.id.order_craftseman_people);
        order_contact = (EditText) findViewById(R.id.order_contact);
        order_remark = (TextView) findViewById(R.id.order_remark);
        order_service_vi = findViewById(R.id.order_service_vi);
        ll_all = (RelativeLayout) findViewById(R.id.ll_all);
        user = TXApplication.getUserMess();
    }

    @Override
    public void fillView() {
        head_title_tv.setText("预约");
        order_contact.setText(user.getPhone());
        updataService();
        updataCraftsman();
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
            case R.id.rl_order_switch_service:// 选择服务
                bundle.putSerializable("servicelist",
                        (Serializable) shopServiceMesses);
                UIHelper.startActivityForResult(mContext,
                        SelectServiceActivity.class, SELECT_SERVICE, bundle);
                ;
                break;
            case R.id.rl_order_craftsman_people:// 选择手艺人
                bundle.putSerializable("peopleList",
                        (Serializable) shopPeopleMesses);
                UIHelper.startActivityForResult(mContext,
                        SelectCraftsmanActivity.class, SELECT_CRAFTSMAN, bundle);
                break;
            case R.id.rl_order_people_num:
                showPopupWindow();
                break;
            case R.id.rl_order_time://选择时间
                showTimePop(System.currentTimeMillis());
                // setReminder();
                break;
            case R.id.free_order:
                isOrder();
                break;
            case R.id.contest_confirm_tv:
                affirmDialog.cancel();
                if (1 == result.getSuccess()) {
                    finish();
                }
                break;

            default:
                break;
        }
    }

    /**
     * 是否可以进行预约服务
     */
    private void isOrder() {
        if (3 == whree_from) {
            if (null == bookingMess) {
                UIHelper.showConfirmDialog(mContext, "请选择服务", null, null, true);
            } else {
                commonJudge();
            }
        } else if (4 == whree_from) {
            commonJudge();
        } else {
            if (shopServiceMess == null) {
                UIHelper.showConfirmDialog(mContext, "请选择服务", null, null, true);
            } else {
                commonJudge();
            }
        }

    }

    private void commonJudge() {
        if ("选择时间".equals(order_time.getText().toString().trim())) {
            UIHelper.showConfirmDialog(mContext, "请选择时间", null, null, true);
        } else {
            if ("选择人数".equals(order_people_num.getText().toString().trim())) {
                UIHelper.showConfirmDialog(mContext, "请选择人数", null, null,
                        true);
            } else {
//                    if (("选择手艺人".equals(order_craftseman_people.getText()
//                            .toString().trim())
//                            || TextUtils.isEmpty(order_craftseman_people
//                            .getText().toString().trim()))) {
//                        UIHelper.showConfirmDialog(mContext, "请选择手艺人", null, null, true);
//                    } else {
                if (TextUtils.isEmpty(order_contact.getText().toString().trim())) {
                    UIHelper.showConfirmDialog(mContext, "请填写您的手机号", null, null, true);
                } else {
                    if (TextUtils.isEmpty(order_name.getText().toString().trim())) {
                        UIHelper.showConfirmDialog(mContext, "请填写您的姓名", null, null, true);
                    } else {
                        order();
                    }
                }

//                    }
            }
        }
    }

    private Dialog dialog;

    /**
     * 预约服务
     */
    private void order() {
        RequestParams params = TXApplication.getParams();
        String url = URLs.APPOINMENT_ADD;
        params.addBodyParameter("number", order_people_num.getText().toString().trim());
        params.addBodyParameter("staffid", staff_id);
        params.addBodyParameter("mid", shop_id);
        params.addBodyParameter("appointmenttime", order_time.getText().toString().trim());
        params.addBodyParameter("phone", order_contact.getText().toString().trim());
        params.addBodyParameter("name", order_name.getText().toString().trim());
        params.addBodyParameter("remark", order_remark.getText().toString().trim());
        params.addBodyParameter("servicename", service_name);
        params.addBodyParameter("serviceid", service_id);
        params.addBodyParameter("uid", user.getUid());
        if (4 == whree_from) {
            params.addBodyParameter("discount", "1");
            params.addBodyParameter("service_price_now", service_price_now);
        }
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                free_order.setSelected(false);
                dialog = UIHelper.createProgressDialog(mContext, "发送中", true);
                dialog.show();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                UIHelper.ToastMessage(mContext, R.string.net_work_error);
                free_order.setSelected(true);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                result = MessageResult.parse(responseInfo.result);
                if (null == result) {
                    dialog.dismiss();
//				createAffirmDialog("预约失败", DIALOG_SINGLE_STPE);
                    UIHelper.ToastMessage(mContext, "预约失败");
                    free_order.setSelected(true);
                    return;
                }
                if (1 == result.getSuccess()) {
                    dialog.dismiss();
                    UIHelper.ToastMessage(mContext, result.getMessage());
                    free_order.setSelected(true);
                    UIHelper.startActivity(mContext, OrderSuccessActivity.class);
                } else {
                    dialog.dismiss();
//					createAffirmDialog(result.getMessage(), DIALOG_SINGLE_STPE);
                    UIHelper.ToastMessage(mContext, result.getMessage());
                    free_order.setSelected(true);
                }
            }
        });
    }

    private void setReminder() {
        DateTimePickerDialog d = new DateTimePickerDialog(this,
                System.currentTimeMillis());
        d.setOnDateTimeSetListener(new OnDateTimeSetListener() {
            @Override
            public void OnDateTimeSet(AlertDialog dialog, long date) {
                // txt.setText("您输入的日期是：" + getStringDate(date));
            }
        });
        d.show();
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     */
    @SuppressLint("SimpleDateFormat")
    public static String getStringDate(Long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE (MM-dd) HH:mm");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 初始化选择服务时间pop窗口
     */
    private void showTimePop(long date) {
        mDateTimePicker = new DateTimePicker(mContext);
        mDateTimePicker
                .setOnDateTimeChangedListener(new OnDateTimeChangedListener() {
                    public void onDateTimeChanged(DateTimePicker view,
                                                  int year, int month, int dayOfMonth, int hourOfDay,
                                                  int minute) {
                        mDate.set(Calendar.YEAR, year);
                        mDate.set(Calendar.MONTH, month);
                        mDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        mDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        mDate.set(Calendar.MINUTE, minute);
                    }
                });
        mDate.setTimeInMillis(date);
        mDate.set(Calendar.SECOND, 0);
        mDateTimePicker.setCurrentDate(mDate.getTimeInMillis());
        mIs24HourView = DateFormat.is24HourFormat(this);// 判断是不是24小时
        LinearLayout popLayout = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.pop_window, null);

        LinearLayout ll_pop = (LinearLayout) popLayout
                .findViewById(R.id.ll_pop);

        TextView tv_cancel = (TextView) popLayout.findViewById(R.id.tv_cancel);
        TextView tv_ok = (TextView) popLayout.findViewById(R.id.tv_ok);

        ll_pop.addView(mDateTimePicker);

        pw = new PopupWindow(popLayout,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        pw.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(getResources().getColor(
                R.color.white));
        pw.setBackgroundDrawable(dw);
        pw.setInputMethodMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pw.setOutsideTouchable(true);
        pw.setFocusable(true);
        // 设置popWindow的显示和消失动画
        pw.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        pw.showAtLocation(ll_all, Gravity.BOTTOM, 0, 0);
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        tv_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mDate.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
                    UIHelper.ToastMessage(mContext, "不能选择过去的时间,请重新选择");
                } else {
                    order_time.setText(getStringDate(mDate.getTimeInMillis()));
                    pw.dismiss();

                }
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
     * 初始化选择人数popupWindow窗口
     */
    private void showPopupWindow() {

        LinearLayout popLayout = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.pop_window, null);

        LinearLayout ll_pop = (LinearLayout) popLayout
                .findViewById(R.id.ll_pop);

        TextView tv_cancel = (TextView) popLayout.findViewById(R.id.tv_cancel);
        TextView tv_ok = (TextView) popLayout.findViewById(R.id.tv_ok);

        ll_pop.addView(getPeopleNumPick());

        mPopupWindow = new PopupWindow(popLayout,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        mPopupWindow.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(getResources().getColor(
                R.color.white));
        mPopupWindow.setBackgroundDrawable(dw);
        mPopupWindow
                .setInputMethodMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        // 设置popWindow的显示和消失动画
        mPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        mPopupWindow.showAtLocation(ll_all, Gravity.BOTTOM, 0, 0);
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        tv_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                order_people_num.setText((peoplenum.getCurrentItem() + 1) + "");
                order_people_num_tv.setText("人");
                mPopupWindow.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

    }

    /**
     * 选择人数
     *
     * @return
     */
    private View getPeopleNumPick() {
        inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_commendpicker, null);
        peoplenum = (WheelView) view.findViewById(R.id.select_commed);
        NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(
                this, 1, 20);
        numericWheelAdapter1.setLabel("");
        peoplenum.setViewAdapter(numericWheelAdapter1);
        peoplenum.setCyclic(true);
        peoplenum.addScrollingListener(scrollListener);
        peoplenum.setVisibleItems(7);
        peoplenum.setCurrentItem(Integer.valueOf(order_people_num.getText()
                .toString().trim().equals("选择人数") ? "1" : order_people_num
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
     * 加载手艺人数据
     */
    private void updataCraftsman() {
        if (null != shopPeopleMess) {
            order_craftseman_people.setText(shopPeopleMess
                    .getChoice_artisans_job()
                    + shopPeopleMess.getChoice_artisans_name());
        }
        if (null != craftsmanIntroduceMess) {
            order_craftseman_people
                    .setText((craftsmanIntroduceMess
                            .getCratsman_introduce_job() == null ? ""
                            : craftsmanIntroduceMess
                            .getCratsman_introduce_job())
                            + (craftsmanIntroduceMess
                            .getCratsman_introduce_name() == null ? ""
                            : craftsmanIntroduceMess
                            .getCratsman_introduce_name()));
            rl_order_craftsman_people.setClickable(false);
            iv_order_craftsman_people_right.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) order_craftseman_people.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            order_craftseman_people.setPadding(0, 0, UIHelper.Dp2Px(mContext, 16), 0);
        }
        if (3 == whree_from && null != bookingMess) {
            order_craftseman_people
                    .setText((bookingMess
                            .getStaff_name() == null ? ""
                            : bookingMess
                            .getStaff_name()));
            rl_order_craftsman_people.setClickable(false);
            iv_order_craftsman_people_right.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) order_craftseman_people.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            order_craftseman_people.setPadding(0, 0, UIHelper.Dp2Px(mContext, 16), 0);
            order_people_num.setText(bookingMess.getPeople_num());
            order_people_num_tv.setText("人");
            order_contact.setText(bookingMess.getPeople_mobile());
            order_name.setText(bookingMess.getPeople_name());
            order_remark.setText(bookingMess.getPeople_remark());
        }

    }

    /**
     * 加载服务数据
     */
    private void updataService() {
        if (3 == whree_from && null != bookingMess) {
            rl_order_service.setVisibility(View.VISIBLE);
            order_service_vi.setVisibility(View.VISIBLE);
            order_service_name.setText(bookingMess.getService_name());
            order_service_cheap_price.setText("¥ " + bookingMess
                    .getService_price());
            order_service_price.setVisibility(View.GONE);
            findViewById(R.id.rll_order_service_price).setVisibility(View.GONE);
            order_service_price_num.setVisibility(View.GONE);
            findViewById(R.id.rl_order_switch_service).setVisibility(View.GONE);
            iv_order_craftsman_people_right.setVisibility(View.INVISIBLE);
        } else if (4 == whree_from) {
            rl_order_service.setVisibility(View.VISIBLE);
            order_service_vi.setVisibility(View.VISIBLE);
            order_service_name.setText(service_name);
            order_service_cheap_price.setText(service_price_now);
            order_service_price.setText(service_price_old);
//            findViewById(R.id.rll_order_service_price).setVisibility(View.GONE);
            order_service_price_num.setText(service_price_discount);
            findViewById(R.id.rl_order_switch_service).setVisibility(View.GONE);
//            iv_order_craftsman_people_right.setVisibility(View.INVISIBLE);
        } else {
            if (shopServiceMess == null) {
                rl_order_service.setVisibility(View.GONE);
                order_service_vi.setVisibility(View.GONE);
            } else {
                rl_order_service.setVisibility(View.VISIBLE);
                order_service_vi.setVisibility(View.VISIBLE);
                order_service_name.setText(shopServiceMess.getService_list_title());
                order_service_cheap_price.setText(shopServiceMess
                        .getService_list_price_now());
                order_service_price.setText(shopServiceMess
                        .getService_list_price_old());
                order_service_price_num.setText(shopServiceMess
                        .getService_list_discount());
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case SELECT_SERVICE:
                    shopServiceMess = (ShopServiceMess) data
                            .getSerializableExtra("select_service");
                    service_name = shopServiceMess.getService_list_title();
                    service_id = shopServiceMess.getService_list_id();
                    updataService();
                    break;
                case SELECT_CRAFTSMAN:
                    shopPeopleMess = (ShopPeopleMess) data
                            .getSerializableExtra("select_craftsman");
                    staff_id = shopPeopleMess.getChoice_artisans_staffid();
                    updataCraftsman();
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * 初始化选择服务时间pop窗口
     */
    private void showTimePop() {
        Calendar calendar = Calendar.getInstance();
        View view = View.inflate(mContext, R.layout.dialog_select_time, null);
        selectDate = calendar.get(Calendar.YEAR) + "年"
                + calendar.get(Calendar.MONTH) + "月"
                + calendar.get(Calendar.DAY_OF_MONTH) + "日"
                + DatePicker.getDayOfWeekCN(calendar.get(Calendar.DAY_OF_WEEK));
        // 选择时间与当前时间的初始化，用于判断用户选择的是否是以前的时间，如果是，弹出toss提示不能选择过去的时间
        selectDay = currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        selectMinute = currentMinute = calendar.get(Calendar.MINUTE);
        selectHour = currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        selectTime = currentHour
                + "点"
                + ((currentMinute < 10) ? ("0" + currentMinute) : currentMinute)
                + "分";
        dp_test = (DatePicker) view.findViewById(R.id.dp_test);
        tp_test = (TimePicker) view.findViewById(R.id.tp_test);
        tv_time_ok = (TextView) view.findViewById(R.id.tv_time_ok);
        tv_time_cancel = (TextView) view.findViewById(R.id.tv_time_cancel);
        // 设置滑动改变监听器
        dp_test.setOnChangeListener(dp_onchanghelistener);
        tp_test.setOnChangeListener(tp_onchanghelistener);
        pw = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // //设置这2个使得点击pop以外区域可以去除pop
        pw.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(getResources().getColor(
                R.color.white));
        pw.setBackgroundDrawable(dw);

        // 出现在布局底端
        pw.showAtLocation(ll_all, Gravity.BOTTOM, 0, 0);

        // 点击确定
        tv_time_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (selectDay == currentDay) { // 在当前日期情况下可能出现选中过去时间的情况
                    if (selectHour < currentHour) {
                        Toast.makeText(getApplicationContext(),
                                "不能选择过去的时间\n        请重新选择", 0).show();
                    } else if ((selectHour == currentHour)
                            && (selectMinute < currentMinute)) {
                        Toast.makeText(getApplicationContext(),
                                "不能选择过去的时间\n        请重新选择", 0).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                selectDate + selectTime, 0).show();
                        pw.dismiss();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            selectDate + selectTime, 0).show();
                    pw.dismiss();
                }
            }
        });

        // 点击取消
        tv_time_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                pw.dismiss();
            }
        });
    }

    // listeners
    DatePicker.OnChangeListener dp_onchanghelistener = new DatePicker.OnChangeListener() {
        @Override
        public void onChange(int year, int month, int day, int day_of_week) {
            selectDay = day;
            selectDate = year + "年" + month + "月" + day + "日"
                    + DatePicker.getDayOfWeekCN(day_of_week);
        }
    };
    TimePicker.OnChangeListener tp_onchanghelistener = new TimePicker.OnChangeListener() {
        @Override
        public void onChange(int hour, int minute) {
            selectTime = hour + "点" + ((minute < 10) ? ("0" + minute) : minute)
                    + "分";
            selectHour = hour;
            selectMinute = minute;
        }
    };
}
