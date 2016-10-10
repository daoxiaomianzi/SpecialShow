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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
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
import com.show.specialshow.adapter.TagsMessAdapter;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.ShopListTagsMess;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.utils.AppManager;
import com.show.specialshow.utils.DateUtil;
import com.show.specialshow.utils.ImageLoderutils;
import com.show.specialshow.utils.RoundImageView;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.wheelcity.AddressData;
import com.show.specialshow.wheelcity.OnWheelChangedListener;
import com.show.specialshow.wheelcity.WheelView;
import com.show.specialshow.wheelcity.adapters.AbstractWheelTextAdapter;
import com.show.specialshow.wheelcity.adapters.ArrayWheelAdapter;
import com.show.specialshow.wheelview.JudgeDate;
import com.show.specialshow.wheelview.ScreenInfo;
import com.show.specialshow.wheelview.WheelMain;

import org.apache.commons.lang3.StringUtils;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SuppressLint("SimpleDateFormat")
public class BasicInformationActivity extends BaseActivity {
    // 相关控件
    private static RoundImageView basic_infor_head_iv;// 头像
    private EditText basic_infor_nickname_et;// 昵称
    private TextView basic_info_sex_name;// 性别
    private TextView basic_info_birthday_data;// 生日
    private TextView basic_info_often_in_address;// 常居地
    private EditText basic_info_introduce_hint;// 签名
    private GridView basic_info_interested_sign_gv;//标签
    private TextView basic_info_interested_sign_tv;
    private LinearLayout basic_information_all;//整个视图
    //
    private static final int PHOTO_CARMERA = 1;
    private static final int PHOTO_PICK = 2;
    private static final int PHOTO_CUT = 3;
    private String[] sexArry = new String[]{"男", "女"};// 性别选择
    private WheelMain wheelMain;
    private String cityTxt;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private PopupWindow pw;
    private TextView tv_ok;
    private TextView tv_cancel;
    private UserMessage user;
    private Dialog dialog;//
    private BasicHandler basicHandler = new BasicHandler(this);
    private List<ShopListTagsMess> mTagsMesses = new ArrayList<>();// 标签数据
    private List<String> mLabels=new ArrayList<>();
    private int basic_mode = 0;
    public static final String MY_BASIC = "我的资料";

     class BasicHandler extends Handler {
        WeakReference<BasicInformationActivity> mActivity;

        BasicHandler(BasicInformationActivity activity) {
            mActivity = new WeakReference<BasicInformationActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (1 == msg.what) {
                Bundle data = msg.getData().getBundle("data");
                setPicToView(data, basic_infor_head_iv);
            }
        }
    }

    @Override
    public void initData() {
        basic_mode=getIntent().getIntExtra("basic_mode",0);
        setContentView(R.layout.activity_basic_information);
    }

    @Override
    public void initView() {
        basic_information_all = (LinearLayout) findViewById(R.id.basic_information_all);
        basic_infor_head_iv = (RoundImageView) findViewById(R.id.basic_infor_head_iv);
        basic_infor_nickname_et = (EditText) findViewById(R.id.basic_infor_nickname_et);
        basic_info_sex_name = (TextView) findViewById(R.id.basic_info_sex_name);
        basic_info_birthday_data = (TextView) findViewById(R.id.basic_info_birthday_data);
        basic_info_often_in_address = (TextView) findViewById(R.id.basic_info_often_in_address);
        basic_info_interested_sign_gv = (GridView) findViewById(R.id.basic_info_interested_sign_gv);
        basic_info_interested_sign_tv = (TextView) findViewById(R.id.basic_info_interested_sign_tv);
        basic_info_introduce_hint = (EditText) findViewById(R.id.basic_info_introduce_hint);
    }

    @Override
    public void fillView() {
        user = TXApplication.getUserMess();
        getData();
        head_right_tv.setText("保存");
        head_right_tv.setVisibility(View.VISIBLE);
        head_title_tv.setText(R.string.basic_information);
    }

    @Override
    public void setListener() {
        basic_info_interested_sign_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle =new Bundle();
                UIHelper.startActivityForResult(mContext,
                        SelectLabelActivity.class,SendCardActivity.TAKE_LABEL, bundle);
            }
        });
    }

    /**
     * 获取基本资料
     */
    private void getData() {
        RequestParams params = TXApplication.getParams();
        String url = URLs.SPACE_INDEX;
        params.addBodyParameter("uid", user.getUid());
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                dialog = UIHelper.createProgressDialog(mContext, "加载中", true);
                dialog.show();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
//				dialog.dismiss();
                UIHelper.ToastMessage(mContext, R.string.net_work_error);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                MessageResult result = MessageResult.parse(responseInfo.result);
                if (result == null) {
                    dialog.dismiss();
                    UIHelper.ToastMessage(mContext, R.string.net_work_error);
                    return;
                }
                if (1 == result.getSuccess()) {
                    UserMessage info = UserMessage.parse(result.getData());
                    if (null != info) {
                        upDataView(info);
                    }
                }
            }
        });
    }

    /**
     * 加载视图
     *
     * @param info
     */
    private void upDataView(UserMessage info) {
        dialog.dismiss();
        String imgheadurl = info.getIcon();
        if (!StringUtils.isEmpty(imgheadurl)) {
            ImageLoderutils imgload = new ImageLoderutils(mContext);
            imgload.display(basic_infor_head_iv, imgheadurl);
        }
        basic_infor_nickname_et.setHint(info.getNickname());
        basic_info_sex_name.setText(info.getSex());
        basic_info_birthday_data.setText(info.getBirthday());
        basic_info_often_in_address.setText(info.getAddress());
        basic_info_introduce_hint.setText(info.getSign_name());
        mTagsMesses = ShopListTagsMess.parse(info.getTag());
        if (null == mTagsMesses || mTagsMesses.isEmpty()) {
            basic_info_interested_sign_tv.setVisibility(View.VISIBLE);
        } else {
            basic_info_interested_sign_tv.setVisibility(View.GONE);

            basic_info_interested_sign_gv.setAdapter(new TagsMessAdapter(
                    mTagsMesses, mContext));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rll_basic_infor_head:// 头像
                showSelectDialog(PHOTO_PICK, PHOTO_CARMERA);
                break;
            case R.id.rll_sex:// 性别
                showSexChooseDialog();
                break;
            case R.id.rll_birthday:// 生日
                showBirthdayChooseDialog();
                break;
            case R.id.rll_often_in:// 常居地
                showAddressChooseDialog();
                break;
            case R.id.head_right_tv://保存资料
                saveInfo();
                break;
            case R.id.basic_info_interested_sign_ll://标签
                Bundle bundle =new Bundle();
                UIHelper.startActivityForResult(mContext,
                        SelectLabelActivity.class,SendCardActivity.TAKE_LABEL, bundle);
                break;
            default:
                break;
        }
    }

    /**
     * 更新用户资料
     */
    private void saveInfo() {
        RequestParams params = TXApplication.getParams();
        String url = URLs.SPACE_SAVEINFO;
        params.addBodyParameter("uid", user.getUid());
        if (StringUtils.isEmpty(basic_infor_nickname_et.getText().toString().trim())) {
            params.addBodyParameter("nickname", basic_infor_nickname_et.getHint().toString().trim());
        } else {
            params.addBodyParameter("nickname", basic_infor_nickname_et.getText().toString().trim());
        }
        params.addBodyParameter("sex", basic_info_sex_name.getText().toString().trim());
        params.addBodyParameter("birthday", basic_info_birthday_data.getText().toString().trim());
        params.addBodyParameter("address", basic_info_often_in_address.getText().toString().trim());
        params.addBodyParameter("sign_name", basic_info_introduce_hint.getText().toString().trim());
        if (null == tempFile) {
            params.addBodyParameter("type", "0");
        } else {
            params.addBodyParameter("type", "1");
            params.addBodyParameter("icon", tempFile);
        }
        if(mTagsMesses!=null){
        for (int i = 0; i < mTagsMesses.size(); i++) {
            params.addBodyParameter("tag[]", mTagsMesses.get(i).getTag_name());
        }
        }

        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException error, String msg) {
                UIHelper.ToastMessage(mContext, R.string.net_work_error);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                MessageResult result = MessageResult.parse(responseInfo.result);
                if (null == result) {
                    UIHelper.ToastMessage(mContext, "修改失败");
                    return;
                }
                if (1 == result.getSuccess()) {
                    UserMessage userMessage = UserMessage.parse(result.getData());
                    saveInfoSuccess(userMessage);
                } else {
                    UIHelper.ToastMessage(mContext, result.getMessage());
                }
            }
        });
    }

    /**
     * 更新资料成功后
     *
     * @param userMessage
     */
    private void saveInfoSuccess(UserMessage userMessage) {
        userMessage.setPhone(user.getPhone());
        userMessage.setLogin(true);
        userMessage.setUser_biaoshi(user.getUser_biaoshi());
        TXApplication.loginSuccess(userMessage);
        if(0==basic_mode){
            Intent data = new Intent();
//		data.putExtra(MyActivity.BAS_INFOR, );
            UIHelper.setResult(mContext, RESULT_OK, data);
            UIHelper.ToastMessage(mContext, "修改成功");
        }else if(1==basic_mode){
            AppManager.getAppManager().finishActivity(2);
            Intent mIntent = new Intent(MY_BASIC);
            mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
            // 发送广播
            sendBroadcast(mIntent);
        }

    }

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
     * 地址选择
     */
    private void showAddressChooseDialog() {
//		final MyAlertDialog addDialog = new MyAlertDialog(mContext)
//				.builder()
//				.setTitle("选择地址")
//				.setView(view)
//				.setNegativeButton("取消", new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//					}
//				});
//		addDialog.setPositiveButton("保存", new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				basic_info_often_in_address.setText(cityTxt);
//			}
//		});
//		addDialog.show();
        View view = dialogm();
        pw = UIHelper.showPopwindow(beforShowPopWindow(view), mContext, basic_information_all);
        tv_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                basic_info_often_in_address.setText(cityTxt);
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
     * 获取地址数据
     *
     * @return
     */
    private View dialogm() {
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.wheelcity_cities_layout, null);
        final WheelView country = (WheelView) contentView
                .findViewById(R.id.wheelcity_country);
        country.setVisibleItems(6);
        country.setViewAdapter(new CountryAdapter(mContext, AddressData.PROVINCES));

        final String cities[][] = AddressData.CITIES;
        final String ccities[][][] = AddressData.COUNTIES;
        final WheelView city = (WheelView) contentView
                .findViewById(R.id.wheelcity_city);
        city.setVisibleItems(6);

        // 地区选择
        final WheelView ccity = (WheelView) contentView
                .findViewById(R.id.wheelcity_ccity);
        ccity.setVisibleItems(6);// 不限城市

        country.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateCities(city, cities, newValue);
                cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                ;
            }
        });

        city.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updatecCities(ccity, ccities, country.getCurrentItem(),
                        newValue);
                cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                        + "-"
                        + AddressData.CITIES[country.getCurrentItem()][city
                        .getCurrentItem()]
                ;
            }
        });

        ccity.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                        + "-"
                        + AddressData.CITIES[country.getCurrentItem()][city
                        .getCurrentItem()]
                        + "-"
                        + AddressData.COUNTIES[country.getCurrentItem()][city
                        .getCurrentItem()][ccity.getCurrentItem()];
            }
        });

        country.setCurrentItem(1);// 设置北京
        city.setCurrentItem(1);
        ccity.setCurrentItem(1);
        return contentView;
    }

    /**
     * Updates the city wheel
     */
    private void updateCities(WheelView city, String cities[][], int index) {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
                cities[index]);
        adapter.setTextSize(18);
        city.setViewAdapter(adapter);
        city.setCurrentItem(0);
    }

    /**
     * Updates the ccity wheel
     */
    private void updatecCities(WheelView city, String ccities[][][], int index,
                               int index2) {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
                ccities[index][index2]);
        adapter.setTextSize(18);
        city.setViewAdapter(adapter);
        city.setCurrentItem(0);
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

    /**
     * 生日选择
     */
    private void showBirthdayChooseDialog() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View datepickerview = inflater.inflate(R.layout.datepicker,
                null);
        ScreenInfo screenInfo = new ScreenInfo(mContext);
        wheelMain = new WheelMain(datepickerview);
        wheelMain.screenheight = screenInfo.getHeight();
        String time = basic_info_birthday_data.getText().toString();
        final Calendar calendar = Calendar.getInstance();
        if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
            try {
                calendar.setTime(dateFormat.parse(time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        wheelMain.initDateTimePicker(year, month, day);
        pw = UIHelper.showPopwindow(beforShowPopWindow(datepickerview), mContext, basic_information_all);
        tv_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (DateUtil.stringToLong(wheelMain.getTime(), "yyyy-MM-dd") > System.currentTimeMillis()) {
                        UIHelper.ToastMessage(mContext, "选择时间超过当前时间,请重新选择");
                    } else {
                        basic_info_birthday_data.setText(wheelMain.getTime());
                        pw.dismiss();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        tv_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
//		final MyAlertDialog dialog=new MyAlertDialog(mContext)
//				.builder()
//				.setTitle("选择日期")
//				.setView(datepickerview)
//				.setNegativeButton("取消", new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//
//					}
//				});
//		dialog.setPositiveButton("保存", new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				basic_info_birthday_data.setText(wheelMain.getTime());
//			}
//		});
//		dialog.show();
    }


    /**
     * 性别选择框
     */
    private void showSexChooseDialog() {
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);// 自定义对话框
//		int index="男".equals(basic_info_sex_name.getText().toString())?0:1;
//		builder.setSingleChoiceItems(sexArry, index,
//				new DialogInterface.OnClickListener() {// 2默认的选中
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
//						// showToast(which+"");
//						basic_info_sex_name.setText(sexArry[which]);
//						dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
//					}
//				});
//		builder.show();// 让弹出框显示
        View sexView = LayoutInflater.from(this).inflate(
                R.layout.wheel_sexs_layout, null);
        final WheelView sex = (WheelView) sexView
                .findViewById(R.id.wheel_sexs);
        sex.setVisibleItems(5);
        sex.setViewAdapter(new CountryAdapter(mContext, sexArry));
        sex.setCurrentItem(0);
        sex.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
            }
        });
        pw = UIHelper.showPopwindow(beforShowPopWindow(sexView), mContext, basic_information_all);
        tv_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                basic_info_sex_name.setText(sexArry[sex.getCurrentItem()]);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case PHOTO_CARMERA:
                    startPhotoZoom(Uri.fromFile(tempFile), 300, PHOTO_CUT);
                    break;
                case PHOTO_PICK:
                    if (null != data) {
                        startPhotoZoom(data.getData(), 300, PHOTO_CUT);
                    }
                    break;
                case PHOTO_CUT:
                    if (null != data) {
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putBundle("data", data.getExtras());
                        message.setData(bundle);//bundle传值，耗时，效率低
                        message.what = 1;//标志是哪个线程传数据
                        basicHandler.sendMessage(message);//发送message信息
                    }
                    break;
                case SendCardActivity.TAKE_LABEL:
                    mLabels=data.getStringArrayListExtra("labels");
                    if(null==mLabels||mLabels.isEmpty()||mLabels.size()==0){
                        if(null!=mTagsMesses){
                            mTagsMesses.clear();
                        }else{
                            mTagsMesses=new ArrayList<>();
                        }
                        basic_info_interested_sign_tv.setVisibility(View.VISIBLE);
                        basic_info_interested_sign_gv.setVisibility(View.GONE);
                        return;
                    }else{
                        if(null!=mTagsMesses){
                            mTagsMesses.clear();
                        }else{
                            mTagsMesses=new ArrayList<>();
                        }
                        for (int i = 0; i <mLabels.size() ; i++) {
                         ShopListTagsMess shopListTagsMess=new ShopListTagsMess();
                            shopListTagsMess.setTag_name(mLabels.get(i));
                            mTagsMesses.add(shopListTagsMess);
                        }
                        basic_info_interested_sign_tv.setVisibility(View.GONE);
                        basic_info_interested_sign_gv.setVisibility(View.VISIBLE);
                        basic_info_interested_sign_gv.setAdapter(new TagsMessAdapter(mTagsMesses,mContext));
                    }
                    break;
                default:
                    break;
            }
        }
    }


}
