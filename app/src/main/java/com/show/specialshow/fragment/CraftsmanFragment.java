package com.show.specialshow.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.activity.CircleDynamicDetailActivity;
import com.show.specialshow.activity.CraftsmandetailsActivity;
import com.show.specialshow.activity.ShowerDetailsActivity;
import com.show.specialshow.adapter.ConstellationAdapter;
import com.show.specialshow.adapter.CraftsmanAdapter;
import com.show.specialshow.adapter.GirdDropDownAdapter;
import com.show.specialshow.adapter.ListDropDownAdapter;
import com.show.specialshow.contstant.ConstantValue;
import com.show.specialshow.model.ConditionMess;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.ShopVisitorListMess;
import com.show.specialshow.model.ShowVisitorList;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.receiver.MyReceiver;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.xlistview.XListView;
import com.yyydjk.library.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 */
public class CraftsmanFragment extends BaseSearch implements AMapLocationListener {
    //相关控件
    private TextView craftsman_nodata_tv;
    private UserMessage user;
    private FrameLayout fl_craftsman_content;
    private LinearLayout ll_craftsman_content;
    // 定位相关
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    // 当前定位坐标(起点)
    private double mLat = 0.0d;//纬度
    private double mLon = 0.0d;//经度
    private List<ShopVisitorListMess> mList = new ArrayList<>();
    private DropDownMenu mDropDownMenu;
    private String headers[] = {"全部类型", "综合排序", "从业时间"};
    private List<View> popupViews = new ArrayList<>();

    private GirdDropDownAdapter cityAdapter;
    private ListDropDownAdapter ageAdapter;
    private ListDropDownAdapter constellationAdapter;


    private List<ConditionMess> filterList;
    private List<ConditionMess> stafftypeList;
    private List<ConditionMess> orderingList;
    private int filter = 0;
    private int staffType = 0;
    private int ordering = 0;
    //第一次加载
    private boolean isFirst = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_craftsman, container, false);
        search_result_lv = (XListView) view.findViewById(R.id.search_result_lv);
        return view;
    }

    /**
     * 高德地图定位成功回调
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (null == aMapLocation) {
            UIHelper.ToastMessage(mContext, "获取当前位置失败");
            return;
        }
        mLat = aMapLocation.getLatitude();
        mLon = aMapLocation.getLongitude();
        locationClient.stopLocation();
        initListView();
        registerBoradcastReceiver();
        registerShowerBoradcastReceiver();
        search_result_lv.setPullLoadEnable(true);
    }

    @Override
    protected void getData() {
        RequestParams params = TXApplication.getParams();
        String url = URLs.SPACE_STAFFLIST;
        user = TXApplication.getUserMess();
        params.addBodyParameter("filter", filter + "");
        params.addBodyParameter("stafftype", staffType + "");
        params.addBodyParameter("ordering", ordering + "");
        params.addBodyParameter("uid", user.getUid());
        params.addBodyParameter("num", "" + ConstantValue.PAGE_SIZE);
        params.addBodyParameter("index", pageIndex + "");
        params.addBodyParameter("current_city", SPUtils.get(mContext, "city", "上海").toString());
        if (0.0d == mLat || 0.0d == mLon) {
            InitLocation();
            return;
        } else {
            params.addBodyParameter("longitude", mLon + "");//经度
            params.addBodyParameter("latitude", mLat + "");//纬度
        }
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (null != dialog) {
                    dialog.dismiss();
                }
                MessageResult result = MessageResult.parse(responseInfo.result);
                if (null == result) {
                    onError(getResources().getString(
                            R.string.net_work_error));
                    return;
                }
                if (1 == result.getSuccess()) {
                    // mList=ShopVisitorListMess.parse(result.getData());
                    // totalRecord = -1;
                    // changeListView(0);
                    ShowVisitorList showVisitorList = ShowVisitorList
                            .parse(result.getData());
                    List<ShopVisitorListMess> list = showVisitorList
                            .getList();
                    filterList = showVisitorList.getFilter();
                    stafftypeList = showVisitorList.getStafftype();
                    orderingList = showVisitorList.getOrdering();
                    if (null != filterList && null != stafftypeList
                            && orderingList != null && isFirst) {
                        dropDownView();
                        isFirst = false;
                    }
                    if (null == showVisitorList || null == list) {
                        changeListView(0);
                        search_result_lv.setVisibility(View.VISIBLE);
                        craftsman_nodata_tv
                                .setVisibility(View.VISIBLE);
                        search_result_lv.setPullLoadEnable(false);
                        return;
                    }
                    int size = list.size();
                    totalRecord = showVisitorList.getTotal();
                    if (search_result_lv.getState() == XListView.LOAD_REFRESH) {
                        mList.clear();
                    }
                    mList.addAll(list);
//                    for (int i = 0; i < mList.size(); i++) {
//                        for (int j = mList.size() - 1; j > i; j--) {
//                            if (mList.get(j).getUser_id()
//                                    .equals(mList.get(i).getUser_id())) {
//                                mList.remove(j);
//                            }
//                        }
//                    }
                    for (int i = mList.size() - 1; i > 0; i--) {
                        for (int j = i - 1; j >= 0; j--) {
                            if (mList.get(j).getUser_id().equals(mList.get(i).getUser_id())) {
                                mList.remove(j);
                                break;
                            }
                        }
                    }
                    if (mList == null || mList.isEmpty()) {
                        search_result_lv.setVisibility(View.VISIBLE);
                        craftsman_nodata_tv
                                .setVisibility(View.VISIBLE);
                        search_result_lv.setPullLoadEnable(false);
                    } else {
                        search_result_lv.setVisibility(View.VISIBLE);
                        craftsman_nodata_tv.setVisibility(View.GONE);
                        search_result_lv.setPullLoadEnable(true);
                    }
                    localRecord = mList.size();
                    changeListView(size);
                } else {
                    changeListView(0);
                    UIHelper.ToastMessage(mContext, result.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (null != dialog) {
                    dialog.dismiss();
                }
                onError(getResources().getString(R.string.net_work_error));
            }
        });
    }

    @Override
    public void initData() {
        mDropDownMenu = (DropDownMenu) findViewById(R.id.dropDownMenu);
        craftsman_nodata_tv = (TextView) findViewById(R.id.craftsman_nodata_tv);
        fl_craftsman_content = (FrameLayout) findViewById(R.id.fl_craftsman_content);
        ll_craftsman_content = (LinearLayout) findViewById(R.id.ll_craftsman_content);
        ll_craftsman_content.removeView(fl_craftsman_content);
        adapter = new CraftsmanAdapter(mList, mContext);
        InitLocation();
    }

    @Override
    public void initView() {
    }

    private void dropDownView() {
        //init city menu
        final ListView cityView = new ListView(mContext);
        cityAdapter = new GirdDropDownAdapter(mContext, stafftypeList);
        cityView.setDividerHeight(0);
        cityView.setAdapter(cityAdapter);

        //init age menu
        final ListView ageView = new ListView(mContext);
        ageView.setDividerHeight(0);
        ageAdapter = new ListDropDownAdapter(mContext, orderingList);
        ageView.setAdapter(ageAdapter);


        //init constellation
        final ListView constellationView = new ListView(mContext);
        constellationView.setDividerHeight(0);
        constellationAdapter = new ListDropDownAdapter(mContext, filterList);
        constellationView.setAdapter(constellationAdapter);

        //init popupViews
        popupViews.add(cityView);
        popupViews.add(ageView);
        popupViews.add(constellationView);

        //add item click event
        cityView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[0] : stafftypeList.get(position).getName());
                mDropDownMenu.closeMenu();
                staffType = stafftypeList.get(position).getKey();
                notifyView();
            }
        });

        ageView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ageAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[1] : orderingList.get(position).getName());
                mDropDownMenu.closeMenu();
                ordering = orderingList.get(position).getKey();
                notifyView();
            }
        });

        constellationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                constellationAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[2] : filterList.get(position).getName());
                mDropDownMenu.closeMenu();
                filter = filterList.get(position).getKey();
                notifyView();
            }
        });
        //init dropdownview
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, fl_craftsman_content);
    }

    private void notifyView() {
        search_result_lv.setState(XListView.LOAD_REFRESH);
        pageIndex = 1;
        mList.clear();
        adapter.notifyDataSetChanged();
        getData();
    }

    @Override
    public void setListener() {
        search_result_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (!BtnUtils.getInstance().isFastDoubleClick()) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("user_id", mList.get(position - 1)
                        .getUser_id());
                UIHelper.startActivity((Activity) mContext,
                        CraftsmandetailsActivity.class, bundle);
            }
        });
    }

    @Override
    public void fillView() {
    }

    public void registerShowerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ShowerDetailsActivity.ACTION_NAME);
        // 注册广播
        getActivity().registerReceiver(mShowerBroadcastReceiver, myIntentFilter);
    }

    private MyReceiver mShowerBroadcastReceiver = new MyReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ShowerDetailsActivity.ACTION_NAME)) {
                getData();
            }
        }

    };

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(CircleDynamicDetailActivity.ACTION_NAME);
        // 注册广播
        mContext.registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private MyReceiver mBroadcastReceiver = new MyReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(CircleDynamicDetailActivity.ACTION_NAME)) {
                if (null != mList) {
                    mList.clear();
                }
                pageIndex = 1;
                search_result_lv.setState(XListView.LOAD_REFRESH);
                getData();
            }
        }

    };

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mShowerBroadcastReceiver);
        getActivity().unregisterReceiver(mBroadcastReceiver);
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    /**
     * 初始化地图定位
     *
     * @param
     */
    private void InitLocation() {
        locationClient = new AMapLocationClient(mContext.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为低功耗模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        // 设置定位监听
        locationClient.setLocationListener(this);
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(true);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(true);
        locationClient.setLocationOption(locationOption);
        locationClient.startLocation();
        loadIng("加载中...", true);

    }


}
