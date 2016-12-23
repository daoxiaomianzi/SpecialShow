package com.show.specialshow.activity;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.db.DatabaseUtil;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.NearMapMess;
import com.show.specialshow.utils.ActionSheetDialog;
import com.show.specialshow.utils.UIHelper;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class NearbyShowFangMapActivity extends BaseActivity implements AMapLocationListener, AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener, AMap.OnMapLoadedListener,
        View.OnClickListener, AMap.InfoWindowAdapter, LocationSource, AMap.OnCameraChangeListener {
    //数据相关
    private List<NearMapMess> mapMessList = new ArrayList<>();
    //相关控件
    private TextureMapView mMapView;//高德地图页
    private EditText near_show_et;//输入框
    // 定位相关
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    boolean isFirstLoc = true; // 是否首次定位
    private double mLat = 0.0d;//纬度
    private double mLon = 0.0d;//经度
    private double locationLat = 0.0d;//定位的纬度
    private double locationLot = 0.0d;//定位的经度
    //覆盖物相关
    private Marker marker;
    //加载的布局
    private View view;
    // 初始化全局 bitmap 信息，不用时及时 recycle
    private BitmapDescriptor bdA;

    private List<Marker> markerList = new ArrayList<>();
    private TextView map_shop;//店铺名字
    //    private TextView map_address;//店铺地址
    //sqlite数据库
    private DatabaseUtil mDBUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//                getWindow().setFormat(PixelFormat.TRANSLUCENT);

        super.onCreate(savedInstanceState);
        near_show_et = (EditText) findViewById(R.id.near_show_et);
        // 地图初始化
        mMapView = (TextureMapView) findViewById(R.id.bmapView);
        mMapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mMapView.getMap();
            setUpMap();
        }
        near_show_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (StringUtils.isEmpty(editable.toString().trim())) {
                    if (null != aMap) {
                        aMap.clear();
                        if (mapMessList != null) {
                            addMarkersToMap(mapMessList);
                        }
                    }
                }
            }
        });

    }

    /**
     * 获取数据
     */
    private void getData() {
        mDBUtil.deleteAll();
        RequestParams params = TXApplication.getParams();
        String url = URLs.SHOP_NEARSHOP;
        if (0.0d == mLat || 0.0d == mLon) {
            UIHelper.ToastMessage(mContext, "获取位置失败,请重试!");
        } else {
            params.addBodyParameter("longitude", mLon + "");//经度
            params.addBodyParameter("latitude", mLat + "");//纬度
//            params.addBodyParameter("longitude", "121.530701");//经度
//            params.addBodyParameter("latitude", "31.234841");//纬度
        }
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                loadIng("加载中...", true);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (null != dialog) {
                    dialog.dismiss();
                }
                MessageResult result = MessageResult.parse(responseInfo.result);
                if (null == result) {
                    return;
                }
                if (1 == result.getSuccess()) {
                    String info = result.getData();
                    if (null != info) {
                        List<NearMapMess> mapList = NearMapMess.parse(info);
                        if (null != mapList && !mapList.isEmpty()) {
                            mapMessList.addAll(mapList);
                        }
                        for (int i = mapMessList.size() - 1; i > 0; i--) {
                            for (int j = i - 1; j >= 0; j--) {
                                if (mapMessList.get(j).getShop_id().equals(mapMessList.get(i).getShop_id())) {
                                    mapMessList.remove(j);
                                    break;
                                }
                            }
                        }
                        if (mapMessList.isEmpty() || mapMessList == null) {
                            UIHelper.ToastMessage(mContext, "附近没有秀坊");
                            return;
                        } else {
                            insertData();
                            if (null != aMap) {
                                aMap.clear();
                                if (null != markerList) {
                                    markerList.clear();
                                }
                            }
                            addMarkersToMap(mapMessList);// 往地图上添加marker
                        }
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (null != dialog) {
                    dialog.dismiss();
                }
                UIHelper.ToastMessage(mContext, R.string.net_work_error);
            }
        });

    }

    /**
     * 插入数据进数据库
     */
    private void insertData() {
        for (int i = 0; i < mapMessList.size(); i++) {
            mDBUtil.Insert(mapMessList.get(i));
        }
    }


    @Override
    public void initData() {
        mDBUtil = new DatabaseUtil(mContext);
        setContentView(R.layout.activity_nearby_show_fang_map);

    }

    @Override
    public void initView() {
    }

    @Override
    public void fillView() {
    }

    @Override
    public void setListener() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contest_confirm_tv:
                affirmDialog.dismiss();
                break;
            case R.id.contest_cancel_tv:
                affirmDialog.dismiss();
                break;
            case R.id.btn_search://搜索
                if (StringUtils.isEmpty(near_show_et.getText().toString().trim())) {
                    UIHelper.ToastMessage(mContext, "请输入关键字");
                } else {
                    searchResult();
                }
                break;
//            case R.id.near_show_right_tv:
//                UIHelper.startActivity(mContext, NearShowActivity.class);
//                break;
            case R.id.near_show_back:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
                if (isOpen) {
                    UIHelper.isVisable(mContext, near_show_et);
                }
                finish();
                break;
        }
    }

    /**
     * 搜索结果
     */
    private void searchResult() {
        List<NearMapMess> searchList = mDBUtil.queryByname(
                near_show_et.getText().toString().trim());
        if (searchList.size() != 0) {
            if (null != aMap) {
                aMap.clear();
                if (null != markerList) {
                    markerList.clear();
                }
                mapMessList = searchList;
                addMarkersToMap(searchList);
                LatLng latLng = new LatLng(mapMessList.get(0).getLatitude(),
                        mapMessList.get(0).getLongitude());
                //设置中心点和缩放比例
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
            }
        } else {
            UIHelper.ToastMessage(mContext, "没有搜索到秀坊");
        }
    }

    /**
     * 弹出对话框,选择是去店铺还是导航
     */
    private void switchDialog(final double mLat2, final double mLon2, final String shop_id, String address) {
        new ActionSheetDialog(mContext)
                .builder()
                .setTitle("前往\n" + address)
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("店铺详情", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Bundle bundle = new Bundle();
                                bundle.putString("shop_id", shop_id);
                                UIHelper.startActivity(mContext, StoresDetailsActivity.class, bundle);
                            }
                        })
                .addSheetItem("前往", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                isShowDialog(mLat2, mLon2);
                            }
                        }).show();
    }

    /**
     * 弹出对话框，选择出行导航方式
     */
    private void isShowDialog(final double mLat2, final double mLon2) {
        new ActionSheetDialog(mContext)
                .builder()
                .setTitle("导航\n请选择出行方式")
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("步行导航", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Bundle bundle = new Bundle();
                                bundle.putDouble("mLat1", locationLat);
                                bundle.putDouble("mLon1", locationLot);
                                bundle.putDouble("mLat2", mLat2);
                                bundle.putDouble("mLon2", mLon2);
                                UIHelper.startActivity(mContext, WalkNaviActivity.class, bundle);
                            }
                        })
                .addSheetItem("驾车导航", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Bundle bundle = new Bundle();
                                bundle.putDouble("mLat1", locationLat);
                                bundle.putDouble("mLon1", locationLot);
                                bundle.putDouble("mLat2", mLat2);
                                bundle.putDouble("mLon2", mLon2);
                                UIHelper.startActivity(mContext, NavigationActivity.class, bundle);
                            }
                        }).show();
    }

    /**
     * 高德地图定位回调
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        // map view 销毁后不在处理新接收的位置
        if (aMapLocation == null || mMapView == null) {
            UIHelper.ToastMessage(mContext, "获取当前位置失败");
            return;
        }
        if (isFirstLoc) {
            isFirstLoc = false;
            mLat = aMapLocation.getLatitude();
            mLon = aMapLocation.getLongitude();
            locationLat = aMapLocation.getLatitude();
            locationLot = aMapLocation.getLongitude();
            LatLng latLng = new LatLng(mLat, mLon);
            //设置中心点和缩放比例
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
            if (null != mListener) {
                mListener.onLocationChanged(aMapLocation);
            }
            getData();
        }
    }

    /**
     * 自定义infowinfow窗口
     */
    public void render(Marker marker, View view) {
        String title = marker.getTitle();
        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (title != null) {
            SpannableString titleText = new SpannableString(title);
//            titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
//                    titleText.length(), 0);
//            titleUi.setTextSize(15);
            titleUi.setText(titleText);

        } else {
            titleUi.setText("");
        }
        String snippet = marker.getSnippet();
        TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
        if (snippet != null) {
            SpannableString snippetText = new SpannableString(snippet);
//            snippetText.setSpan(new ForegroundColorSpan(Color.GREEN), 0,
//                    snippetText.length(), 0);
//            snippetUi.setTextSize(20);
            snippetUi.setText(snippetText);
        } else {
            snippetUi.setText("");
        }
    }

    /**
     * 监听自定义infowindow窗口的infowindow事件回调
     */
    @Override
    public View getInfoWindow(Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.custom_info_window, null);
        render(marker, view);
        return view;
    }

    /**
     * 监听自定义infowindow窗口的infocontents事件回调
     */
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    /**
     * 监听点击infowindow窗口事件回调
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        for (int i = 0; i < markerList.size(); i++) {
            if (marker.equals(markerList.get(i))) {
                LatLng ll = marker.getPosition();
                switchDialog(ll.latitude, ll.longitude, mapMessList.get(i).getShop_id(), mapMessList.get(i).getAddress());
            }
        }
    }

    /**
     * 监听amap地图加载成功事件回调
     */
    @Override
    public void onMapLoaded() {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
    }

    /**
     * 监听点击marker时事件回调
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
//        for (int i = 0; i < markerList.size(); i++) {
//            if (marker .equals( markerList.get(i))) {
//                LatLng ll = marker.getPosition();
//                switchDialog(ll.latitude,ll.longitude,mapMessList.get(i).getShop_id(),mapMessList.get(i).getAddress());
//            }
//        }
        return false;
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
    }

    private void setUpMap() {
        aMap.setOnCameraChangeListener(this);//设置获取当前屏幕中心点的经纬度
        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
    }

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap(List<NearMapMess> mapMessList) {

        for (int i = 0; i < mapMessList.size(); i++) {
//            view = getLayoutInflater().from(mContext).inflate(R.layout.map_button, null);
//            map_shop = (TextView) view.findViewById(R.id.map_shop);
////            map_address = (TextView) view.findViewById(R.id.map_address);
//            map_shop.setText(mapMessList.get(i).getTitle());
////            map_address.setText(mapMessList.get(i).getAddress());
            bdA = BitmapDescriptorFactory.
                    fromResource(R.drawable.icon_nearby_marke);
//                    .fromView(view);
            LatLng ll = new LatLng(mapMessList.get(i).getLatitude(), mapMessList.get(i).getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(ll);
            marker = aMap.addMarker(markerOptions.title(mapMessList.get(i).getTitle())
                    .snippet(mapMessList.get(i).getAddress()).icon(bdA).draggable(true));
            markerList.add(marker);
        }
        for (int i = markerList.size() - 1; i > 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                if (markerList.get(j).equals(markerList.get(i))) {
                    markerList.remove(j);
                    break;
                }
            }
        }
        marker.showInfoWindow();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
        deactivate();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        if (null != locationClient) {
            locationClient.stopLocation();
        }
        // 关闭定位图层
        if (null != mMapView) {
            mMapView.onDestroy();
        }
        if (null != bdA) {
            bdA.recycle();
        }
        mMapView = null;
        super.onDestroy();

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (null == locationClient) {
            InitLocation();
        }

    }

    @Override
    public void deactivate() {
        mListener = null;
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        locationClient = null;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        LatLng latLng = cameraPosition.target;
        LatLng latLngLocation = new LatLng(mLat, mLon);
        if (mLat != 0.0d && mLon != 0.0d) {
            float distance = AMapUtils.calculateLineDistance(latLngLocation, latLng);
            if (distance > 4000) {
                mLon = latLng.longitude;
                mLat = latLng.latitude;
                if (aMap != null) {
                    aMap.clear();
                }
                getData();
            }
        }
    }


}
