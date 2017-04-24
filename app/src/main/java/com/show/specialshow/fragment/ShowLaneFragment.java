package com.show.specialshow.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.activity.BannerWebActivity;
import com.show.specialshow.activity.CircleDynamicDetailActivity;
import com.show.specialshow.activity.SearchResultActivity;
import com.show.specialshow.activity.StoresDetailsActivity;
import com.show.specialshow.adapter.GridViewAdapter;
import com.show.specialshow.adapter.ShowLaneAdapter;
import com.show.specialshow.adapter.ViewPagerAdapter;
import com.show.specialshow.contstant.ConstantValue;
import com.show.specialshow.model.BannerMess;
import com.show.specialshow.model.CenterButtonMess;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.ShopLaneList;
import com.show.specialshow.model.ShopListMess;
import com.show.specialshow.model.ShopListTagsMess;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.receiver.MyReceiver;
import com.show.specialshow.utils.BannerPointUtils;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.xlistview.XListView;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import cn.bingoogolapple.bgabanner.BGABanner;

public class ShowLaneFragment extends BaseSearch implements AMapLocationListener, BGABanner.Delegate {

    private List<ShopListMess> mList = new ArrayList<>();
    private List<ShopListTagsMess> mTagsMesses = new ArrayList<>();
    private BGABanner mBanner;

    //相关控件
    private TextView show_lang_nodata_tv;
    //    private EditText show_lang_search_et;//搜索框
//    private Button map_btn;//地图按钮
//    private XListView search_result_key;//key关键字搜索结果
    // 定位相关
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    // 当前定位坐标(起点)
    private double mLat = 0.0d;//纬度
    private double mLon = 0.0d;//经度
    //    通过关键字搜出来的数据
//    private List<ShopListMess> keyList = new ArrayList<>();
//    private int pageKeyIndex = 1;
//    private int totalKeyRecord;
//    private int localKeyRecord = 0;
//    private BaseAdapter keyAdapter;
    private String key;
    //banner相关
    private View header_banner;
    //    private ViewPager dynamic_banner;
    private TextView dynamic_banner_describe_tv;
    //    private LinearLayout dynamic_banner_show_adddot;//banner小点
    private int currentItem = 0;
    private ScheduledExecutorService scheduledExecutorService;
    private BannerPointUtils bannerPointUtils;//banner小点工具类
    private BannerPointUtils centerPoint;//中间小点
    private ArrayList<ImageView> images = new ArrayList<>();
    private MyPagerAdapter banner_adapter;
    private ArrayList<ImageView> pointviews = new ArrayList<>();
    //横向按钮相关
    private View center_button;
    private ViewPager mPager;
    private List<View> mPagerList;
    private List<CenterButtonMess> mDatas;
    private LinearLayout mLlDot;
    private ArrayList<ImageView> pointview = new ArrayList<>();

    /**
     * 总的页数
     */
    private int pageCount;
    /**
     * 每一页显示的个数
     */
    private int pageSize = 5;
    /**
     * 当前显示的是第几页
     */
    private int curIndex = 0;

    //banner数据
    private List<BannerMess> bannerList;
    //类型
    private int tag_id;

    private boolean isFristTag = true;


    public static ShowLaneFragment newInstance(String key, int tag_id) {
        final ShowLaneFragment f = new ShowLaneFragment();

        final Bundle args = new Bundle();
        args.putString("key", key);
        args.putInt("tag_id", tag_id);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        key = getArguments() != null ? getArguments().getString("key") : null;
        tag_id = getArguments() != null ? getArguments().getInt("tag_id", 0) : 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_show_lane, container, false);
        search_result_lv = (XListView) view.findViewById(R.id.search_result_lv);
        return view;
    }

    @Override
    public void initData() {
        show_lang_nodata_tv = (TextView) findViewById(R.id.show_lang_nodata_tv);
//        show_lang_search_et = (EditText) findViewById(R.id.show_lang_search_et);
//        map_btn = (Button) findViewById(R.id.map_btn);
//        search_result_key = (XListView) findViewById(R.id.search_result_key);
        adapter = new ShowLaneAdapter(mList, mContext);
    }

//    private Handler handler = new Handler() {
//        public void handleMessage(android.os.Message msg) {
//            dynamic_banner.setCurrentItem(currentItem);
//        }
//    };

//    @Override
//    public void onStart() {
//        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//        // 当Activity显示出来后，每两秒钟切换一次图片显示
//        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 5, 5,
//                TimeUnit.SECONDS);
//        super.onStart();
//    }
//
//    @Override
//    public void onStop() {
//        // 当Activity不可见的时候停止切换
//        scheduledExecutorService.shutdown();
//        super.onStop();
//    }

    /**
     * 换行切换任务
     *
     * @author Administrator
     */
//    private class ScrollTask implements Runnable {
//
//        public void run() {
//            synchronized (header_banner) {
//                // System.out.println("currentItem: " + currentItem);
//                // currentItem = (currentItem + 1) % images.size();
//                currentItem = currentItem + 1;
//                // handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
//                handler.sendEmptyMessage(-1);
//            }
//        }
//
//    }
    @Override
    public void initView() {
        if (StringUtils.isEmpty(key) && 0 == tag_id) {
            header_banner = View.inflate(mContext,
                    R.layout.view_banner, null);
            mBanner = (BGABanner) header_banner.findViewById(R.id.banner);
            mBanner.setAdapter(new BGABanner.Adapter() {
                @Override
                public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
                    BannerMess bannerMess = (BannerMess) model;
                    ImageLoader.getInstance().displayImage(bannerMess.getImagePath(), (ImageView) view);

                }
            });
//            dynamic_banner = (ViewPager) header_banner
//                    .findViewById(R.id.dynamic_banner_show_vp);
//            dynamic_banner_show_adddot = (LinearLayout) header_banner.findViewById(R.id.dynamic_banner_show_adddot);
//            dynamic_banner_describe_tv = (TextView) header_banner
//                    .findViewById(R.id.dynamic_banner_describe_tv);
            search_result_lv.addHeaderView(header_banner);
            center_button = View.inflate(mContext, R.layout.view_switch_class, null);
            mPager = (ViewPager) center_button.findViewById(R.id.viewpager);
            mLlDot = (LinearLayout) center_button.findViewById(R.id.ll_dot);
        }
        registerBoradcastReceiver();
        InitLocation();
    }

    private void permissionLocation() {
        Acp.getInstance(mContext).request(new AcpOptions.Builder()
                        .setPermissions(android.Manifest.permission.ACCESS_COARSE_LOCATION
                        )
//                /*以下为自定义提示语、按钮文字
                        .setRationalMessage("定位功能需要您授权，否则App将不能正常使用")
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        InitLocation();
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        UIHelper.ToastMessage(mContext, "定位功能取消授权");
                    }
                });
    }

    /**
     * 初始化数据源
     */
    private void initCenterView() {
        //总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(mDatas.size() * 1.0 / pageSize);
        mPagerList = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            //每个页面都是inflate出一个新实例
            GridView gridView = (GridView) View.inflate(mContext, R.layout.gridview, null);
            gridView.setAdapter(new GridViewAdapter(mContext, mDatas, i, pageSize));
            mPagerList.add(gridView);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int pos = position + curIndex * pageSize;
                    if (!BtnUtils.getInstance().isFastDoubleClick()) {
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("tag", mDatas.get(pos).getTag());
                    bundle.putInt("tag_id", mDatas.get(pos).getTag_id());
                    bundle.putSerializable("mData", (Serializable) mDatas);
                    UIHelper.startActivity(getActivity(), SearchResultActivity.class, bundle);
                }
            });
        }
        //设置适配器
        mPager.setAdapter(new ViewPagerAdapter(mPagerList));
        centerPoint = new BannerPointUtils(mContext, mLlDot, pointview
        );
        if (0 != pageCount) {
            centerPoint.initPoint(pageCount);
            centerPoint.draw_Point(0);
        }
        search_result_lv.addHeaderView(center_button);
    }

    @Override
    public void setListener() {
        if (StringUtils.isEmpty(key) && 0 == tag_id) {
            mBanner.setDelegate(this);
//            dynamic_banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//                @Override
//                public void onPageSelected(int position) {
//                    currentItem = position;
//                    bannerPointUtils.draw_Point(position % images.size());
//                }
//
//                @Override
//                public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//                }
//
//                @Override
//                public void onPageScrollStateChanged(int arg0) {
//
//                }
//            });
            mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    curIndex = position;
                    centerPoint.draw_Point(position % mPagerList.size());
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int arg0) {

                }
            });
        }
        search_result_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (!BtnUtils.getInstance().isFastDoubleClick()) {
                    return;
                }
                Bundle bundle = new Bundle();
                if (StringUtils.isEmpty(key) && 0 == tag_id) {
                    bundle.putString("shop_id", mList.get(position - 3).getShop_id());
                } else {
                    bundle.putString("shop_id", mList.get(position - 1).getShop_id());
                }
                UIHelper.startActivity((Activity) mContext, StoresDetailsActivity.class, bundle);
            }
        });

//        show_lang_search_et.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int start, int count,
//                                          int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (StringUtils.isEmpty(editable.toString().trim())) {
////                    map_btn.setBackgroundResource(R.drawable.icon_map);
////                    map_btn.setText("");
//                    map_btn.setVisibility(View.GONE);
//                    if (null != keyList) {
//                        keyList.clear();
//                    }
//                    search_result_key.setVisibility(View.GONE);
//                    search_result_lv.setVisibility(View.VISIBLE);
//                } else {
//                    map_btn.setVisibility(View.VISIBLE);
//                    map_btn.setBackgroundColor(Color.WHITE);
//                    map_btn.setText(R.string.cancel);
//                }
//            }
//        });
//        map_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clickMapBtn();
//            }
//        });
//        show_lang_search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int keyCode, KeyEvent keyEvent) {
//                if (keyCode == EditorInfo.IME_ACTION_SEARCH) {
//                    // 先隐藏键盘
//                    UIHelper.isVisable(mContext, show_lang_search_et);
//                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
//                    search();
//                }
//                return false;
//            }
//        });
    }

//    /**
//     * 搜索结果
//     */
//    private void search() {
//        if (StringUtils.isEmpty(show_lang_search_et.getText().toString().trim())) {
//            UIHelper.ToastMessage(mContext, "请输入关键字");
//        } else {
//            keyAdapter = new ShowLaneAdapter(keyList, mContext);
//            initKeyListView();
//        }
//    }

//    /**
//     * 通过关键字key搜索的秀坊列表
//     */
//    private void getKeyData() {
//        String key = show_lang_search_et.getText().toString().trim();//秀坊关键字
//        RequestParams params = TXApplication.getParams();
//        String url = URLs.SHOP_SHOPLIST;
//        UserMessage user = TXApplication.getUserMess();
//        params.addBodyParameter("uid", user.getUid());
//        params.addBodyParameter("num", "" + ConstantValue.PAGE_SIZE);
//        params.addBodyParameter("key", key);
//        params.addBodyParameter("index", pageIndex + "");
//        if (0.0d == mLat || 0.0d == mLon) {
//            UIHelper.ToastMessage(mContext, "获取位置失败,请重试!");
//        } else {
//            params.addBodyParameter("longitude", mLon + "");//经度
//            params.addBodyParameter("latitude", mLat + "");//纬度
//        }
//        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                onKeyError(getResources().getString(R.string.net_server_error));
//            }
//
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                MessageResult result = MessageResult.parse(responseInfo.result);
//                if (null == result) {
//                    onKeyError(getResources().getString(R.string.no_data_search));
//                    return;
//                }
//                if (1 == result.getSuccess()) {
//                    ShopLaneList shopLaneList = ShopLaneList.parse(result.getData());
//                    List<ShopListMess> List = shopLaneList.getList();
//                    if (null == List) {
//                        UIHelper.ToastMessage(mContext, "无搜索结果");
//                        changeKeyListView(0);
//                        return;
//                    }
//                    int size = List.size();
//                    totalKeyRecord = shopLaneList.getTotal();
//                    if (totalKeyRecord <= ConstantValue.PAGE_SIZE) {
//                        search_result_key.setPullLoadEnable(false);
//                    }
//                    if (search_result_key.getState() == XListView.LOAD_REFRESH) {
//                        keyList.clear();
//                    }
//                    keyList.addAll(List);
//                    for (int i = 0; i < keyList.size(); i++) {
//                        for (int j = keyList.size() - 1; j > i; j--) {
//                            if (keyList.get(j).getShop_id()
//                                    .equals(keyList.get(i).getShop_id())) {
//                                keyList.remove(j);
//                            }
//                        }
//                    }
//                    if (keyList == null || keyList.isEmpty()) {
//                        search_result_key.setVisibility(View.VISIBLE);
//                        search_result_lv.setVisibility(View.GONE);
//                        show_lang_nodata_tv.setVisibility(View.VISIBLE);
//                    } else {
//                        search_result_key.setVisibility(View.VISIBLE);
//                        search_result_lv.setVisibility(View.GONE);
//                        show_lang_nodata_tv.setVisibility(View.GONE);
//                    }
//                    localKeyRecord = keyList.size();
////					totalRecord = -1;
//                    changeKeyListView(size);
//                } else {
//                    changeKeyListView(0);
//                    UIHelper.ToastMessage(mContext, result.getMessage());
//                }
//            }
//        });
//    }

    @Override
    public void fillView() {

    }

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

//    /**
//     * 地图和取消的时候做不同的操作
//     */
//    private void clickMapBtn() {
//        if (StringUtils.isEmpty(map_btn.getText().toString().trim())) {//当为地图按钮时
//            UIHelper.startActivity(getActivity(), NearbyShowFangMapActivity.class);
//        } else {//当为取消按钮时
//            show_lang_search_et.setText("");
//            UIHelper.isVisable(mContext, show_lang_search_et);
//            if (null != keyList) {
//                keyList.clear();
//            }
//            search_result_key.setVisibility(View.GONE);
//            search_result_lv.setVisibility(View.VISIBLE);
//        }
//    }

    @Override
    protected void getData() {
        if (StringUtils.isEmpty(key) && 0 == tag_id) {
            loadBanner();
        }
        RequestParams params = TXApplication.getParams();
        String url = URLs.SHOP_SHOPLIST;
        UserMessage user = TXApplication.getUserMess();
        params.addBodyParameter("uid", user.getUid());
        params.addBodyParameter("num", "" + ConstantValue.PAGE_SIZE);
        if (StringUtils.isEmpty(key)) {
            params.addBodyParameter("city", SPUtils.get(mContext, "city", "上海").toString());
        } else {
            params.addBodyParameter("key", key);
        }
        params.addBodyParameter("index", pageIndex + "");
        params.addBodyParameter("tag_id", tag_id + "");
        if (0.0d == mLat || 0.0d == mLon) {
            InitLocation();
            return;
        } else {
            params.addBodyParameter("longitude", mLon + "");//经度
            params.addBodyParameter("latitude", mLat + "");//纬度
        }
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException error, String msg) {
                if (null != dialog) {
                    dialog.dismiss();
                }
                onError(getResources().getString(R.string.net_work_error));
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                MessageResult result = MessageResult.parse(responseInfo.result);
                if (null == result) {
                    if (null != dialog) {
                        dialog.dismiss();
                    }
                    onError(getResources().getString(R.string.net_server_error));
                    return;
                }
                if (1 == result.getSuccess()) {
                    if (null != dialog) {
                        dialog.dismiss();
                    }
                    ShopLaneList shopLaneList = ShopLaneList.parse(result.getData());
                    List<ShopListMess> List = shopLaneList.getList();
                    if (null == List) {
                        changeListView(0);
//                        search_result_key.setVisibility(View.GONE);
                        search_result_lv.setVisibility(View.VISIBLE);
                        show_lang_nodata_tv.setVisibility(View.VISIBLE);
                        search_result_lv.setPullLoadEnable(false);
                        return;
                    }
                    int size = List.size();
                    totalRecord = shopLaneList.getTotal();
                    if (totalRecord > ConstantValue.PAGE_SIZE) {
                        search_result_lv.setPullLoadEnable(true);
                    } else {
                        search_result_lv.setPullLoadEnable(false);
                    }
                    if (search_result_lv.getState() == XListView.LOAD_REFRESH) {
                        mList.clear();
                    }
                    mList.addAll(List);
                    for (int i = 0; i < mList.size(); i++) {
                        for (int j = mList.size() - 1; j > i; j--) {
                            if (mList.get(j).getShop_id()
                                    .equals(mList.get(i).getShop_id())) {
                                mList.remove(j);
                            }
                        }
                    }
                    if (mList == null || mList.isEmpty()) {
//                        search_result_key.setVisibility(View.GONE);
                        search_result_lv.setVisibility(View.VISIBLE);
                        show_lang_nodata_tv.setVisibility(View.VISIBLE);
                        search_result_lv.setPullLoadEnable(false);
                    } else {
//                        search_result_key.setVisibility(View.GONE);
                        search_result_lv.setVisibility(View.VISIBLE);
                        show_lang_nodata_tv.setVisibility(View.GONE);
                    }
                    localRecord = mList.size();
//					totalRecord = -1;
                    changeListView(size);
                } else {
                    if (null != dialog) {
                        dialog.dismiss();
                    }
                    changeListView(0);
                    UIHelper.ToastMessage(mContext, result.getMessage());
                }
            }
        });
    }

    /**
     * 初始化地图定位
     *
     * @param
     */
    private void InitLocation() {
        locationClient = new AMapLocationClient(mContext.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
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

    /**
     * 高德地图定位回调
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
        search_result_lv.setDividerHeight(0);
        search_result_lv.setBackgroundColor(Color.WHITE);
        search_result_lv.setPullLoadEnable(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    //    private void initKeyListView() {
//        search_result_key.setDividerHeight(15);
//        search_result_key.setPullLoadEnable(true);
//        search_result_key.setPullRefreshEnable(false);
//        search_result_key.setXListViewListener(new XListView.IXListViewListener() {
//
//            @Override
//            public void onRefresh() {
//                pageKeyIndex = 1;
//                getKeyData();
//            }
//
//            @Override
//            public void onLoadMore() {
//                pageKeyIndex++;
//                getKeyData();
//            }
//
//            @Override
//            public void onInit() {
//                getKeyData();
//
//            }
//        });
//        search_result_key.setAdapter(keyAdapter);
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void changeKeyListView(int size) {
//        stopLoad();
//        if (size < ConstantValue.PAGE_SIZE || localKeyRecord == totalKeyRecord) {
//            search_result_key.loadFull();
//        }
//        if (size == 0 && localKeyRecord == 0) {
//            search_result_key.loadEmpty();
//        }
//        if (search_result_key.getState() == XListView.LOAD_INIT)
//            search_result_key.setSelectionFromTop(0, 0);
//        keyAdapter.notifyDataSetChanged();
//    }
//
//    private void onKeyError(String prompt) {
//        UIHelper.ToastMessage(mContext, prompt);
//        stopKeyLoad();
//        search_result_key.onError();
//    }
//
//    private void stopKeyLoad() {
//        switch (search_result_key.getState()) {
//            case XListView.LOAD_INIT:
//                search_result_key.stopInit();
//                break;
//            case XListView.LOAD_REFRESH:
//                search_result_key.stopRefresh();
//                break;
//            case XListView.LOAD_MORE:
//                search_result_key.stopLoadMore();
//                break;
//        }
//    }


    private void loadBanner() {
//        scheduledExecutorService.shutdown();
        RequestParams params = TXApplication.getParams();
        String url = URLs.LOGIN_BANNER;
        String city = SPUtils.get(mContext, "city", "上海").toString();
        params.addBodyParameter("city", city);
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                MessageResult result = MessageResult.parse(responseInfo.result);
                if (null == result) {
                    return;
                }
                if (1 == result.getSuccess()) {
                    String info = result.getData();
                    if (null != info) {
                        if (bannerList != null) {
                            bannerList.clear();
                        }
                        bannerList = BannerMess.parse(info);
                        if (null != bannerList) {
                            List<String> tips = new ArrayList<>();
                            mBanner.setData(R.layout.view_image, bannerList, tips);
                        }
                    }
                    String tags = result.getTaglist();
                    if (isFristTag) {
                        if (null != tags) {
                            if (mDatas != null) {
                                mDatas.clear();
                            }
                            mDatas = CenterButtonMess.parse(tags);
                            if (mDatas != null) {
                                initCenterView();
                                isFristTag = false;
                            }
                        }
                    }
                } else {
                    return;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    private void initBanner() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        if (images != null) {
            images.clear();
        }
        for (int i = 0; i < bannerList.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            final BannerMess bannerMess = bannerList.get(i);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (StringUtils.isEmpty(bannerMess.getUrl())) {
                        return;
                    }
                    if (!BtnUtils.getInstance().isFastDoubleClick()) {
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("banner_path", bannerMess.getUrl());
                    UIHelper.startActivity(getActivity(), BannerWebActivity.class, bundle);
                }
            });
            ImageLoader.getInstance().displayImage(bannerList.get(i).getImagePath(), imageView);
            images.add(imageView);
        }
        banner_adapter = new MyPagerAdapter();
//        dynamic_banner.setCurrentItem(300);
//        dynamic_banner.setAdapter(banner_adapter);
//        bannerPointUtils = new BannerPointUtils(mContext, dynamic_banner_show_adddot, pointviews
//        );
        if (null != bannerList) {
            bannerPointUtils.initPoint(bannerList.size());
            bannerPointUtils.draw_Point(0);
        }
    }

    @Override
    public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {
        BannerMess bannerMess = (BannerMess) model;
        Bundle bundle = new Bundle();
        if (!StringUtils.isEmpty(bannerMess.getUrl())) {
            bundle.putString("banner_path", bannerMess.getUrl());
            UIHelper.startActivity(getActivity(), BannerWebActivity.class, bundle);
        }

    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
            // return images.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        // 添加数据
        @Override
        public Object instantiateItem(ViewGroup viewPager, int position) {
            ImageView imageView;
            imageView = images.get(position % images.size());

            ViewGroup parent = (ViewGroup) imageView.getParent();
            if (parent != null)
                viewPager.removeView(imageView);
            viewPager.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup viewPager, int position, Object object) {
        }
    }
}
