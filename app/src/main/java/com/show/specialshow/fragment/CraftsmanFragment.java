package com.show.specialshow.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.show.specialshow.adapter.CraftsmanAdapter;
import com.show.specialshow.contstant.ConstantValue;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.ShopVisitorListMess;
import com.show.specialshow.model.ShowVisitorList;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class CraftsmanFragment extends BaseSearch implements AMapLocationListener {
    //相关控件
    private TextView craftsman_nodata_tv;
    private UserMessage user;
    // 定位相关
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    // 当前定位坐标(起点)
    private double mLat=0.0d;//纬度
    private double mLon=0.0d;//经度
    private List<ShopVisitorListMess> mList = new ArrayList<>();

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
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(null==aMapLocation){
            UIHelper.ToastMessage(mContext,"获取当前位置失败");
            return;
        }
        mLat=aMapLocation.getLatitude();
        mLon=aMapLocation.getLongitude();
        locationClient.stopLocation();
        initListView();
        search_result_lv.setPullLoadEnable(true);
    }

    @Override
    protected void getData() {
        RequestParams params = TXApplication.getParams();
        String url= URLs.SPACE_STAFFLIST;
        user = TXApplication.getUserMess();
        params.addBodyParameter("uid", user.getUid());
        params.addBodyParameter("num", "" + ConstantValue.PAGE_SIZE);
        params.addBodyParameter("index", pageIndex + "");
        if(0.0d==mLat||0.0d==mLon){
            InitLocation();
            return;
        }else{
            params.addBodyParameter("longitude",mLon+"");//经度
            params.addBodyParameter("latitude",mLat+"");//纬度
        }
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if(null!=dialog){
                    dialog.dismiss();
                }
                MessageResult result =MessageResult.parse(responseInfo.result);
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
                    if(null==showVisitorList){
                        changeListView(0);
                        return;
                    }
                    List<ShopVisitorListMess> list = showVisitorList
                            .getList();
                    if (null == list) {
                        changeListView(0);
                        return;
                    }
                    int size = list.size();
                    totalRecord = showVisitorList.getTotal();
                    if (search_result_lv.getState() == XListView.LOAD_REFRESH) {
                        mList.clear();
                    }
                    mList.addAll(list);
                    for (int i = 0; i < mList.size(); i++) {
                        for (int j = mList.size() - 1; j > i; j--) {
                            if (mList.get(j).getUser_id()
                                    .equals(mList.get(i).getUser_id())) {
                                mList.remove(j);
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
                if(null!=dialog){
                    dialog.dismiss();
                }
                onError(getResources().getString(R.string.net_work_error));
            }
        });
    }

    @Override
    public void initData() {
        InitLocation();
        craftsman_nodata_tv= (TextView) findViewById(R.id.craftsman_nodata_tv);
        adapter=new CraftsmanAdapter(mList,mContext);
    }

    @Override
    public void initView() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void fillView() {

    }

    @Override
    public void onClick(View v) {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
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
        loadIng("加载中",true);

    }
}
