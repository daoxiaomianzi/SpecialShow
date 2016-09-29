package com.show.specialshow.activity;

import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.BaseSearchActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.adapter.ShowVisitorAdapter;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.ShopVisitorListMess;
import com.show.specialshow.model.ShowVisitorList;
import com.show.specialshow.utils.DensityUtil;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 好友,关注,粉丝列表页
 */

public class CommenNumActivity extends BaseSearchActivity implements AMapLocationListener{


    private List<ShopVisitorListMess> mList = new ArrayList<ShopVisitorListMess>();

    private String type;//类型
    private String noData;
    //相关控件
    private TextView commen_num_nodata_tv;
    // 定位相关
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    // 当前定位坐标(起点)
    private double mLat=0.0d;//纬度
    private double mLon=0.0d;//经度

    @Override
    protected void getData() {
        RequestParams params = TXApplication.getParams();
        String url= URLs.USER_USERNUM;
        params.addBodyParameter("uid",TXApplication.getUserMess().getUid());
        params.addBodyParameter("page",pageIndex+"");
        if(0.0d==mLat||0.0d==mLon){
            UIHelper.ToastMessage(mContext,"获取位置失败,请重试!");
        }else{
            params.addBodyParameter("longitude",mLon+"");//经度
            params.addBodyParameter("latitude",mLat+"");//纬度
        }
        params.addBodyParameter("type",type);
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                MessageResult result = MessageResult.parse(responseInfo.result);
                if(null!=dialog){
                    dialog.dismiss();
                }
                if(null==result){
                    return;
                }
                if(1==result.getSuccess()){
                    ShowVisitorList showVisitorList = ShowVisitorList.parse(result.getData());
                    List<ShopVisitorListMess> list = showVisitorList.getList();
                    if(null==showVisitorList){
                        search_result_lv.setVisibility(View.VISIBLE);
                        commen_num_nodata_tv.setVisibility(View.VISIBLE);
                        commen_num_nodata_tv.setText(noData);
                        changeListView(0);
                        return;
                    }
                    if(null!=list){
                        int size= list.size();

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
                        for(int i = mList.size() - 1; i > 0; i--) {
                            for(int j = i - 1; j >= 0; j--) {
                                if(mList.get(j).getUser_id().equals(mList.get(i).getUser_id())) {
                                    mList.remove(j);
                                    break;
                                }
                            }
                        }
                    if (mList == null || mList.isEmpty()) {
                        search_result_lv.setVisibility(View.VISIBLE);
                        commen_num_nodata_tv
                                .setVisibility(View.VISIBLE);
                        commen_num_nodata_tv.setText(noData);
                        search_result_lv.setPullLoadEnable(false);
                    } else {
                        search_result_lv.setVisibility(View.VISIBLE);
                        commen_num_nodata_tv.setVisibility(View.GONE);
                    }
                    localRecord = mList.size();
                    changeListView(size);
                    }else{
                        if(null!=mList){
                            mList.clear();
                        }
                        search_result_lv.setVisibility(View.VISIBLE);
                        commen_num_nodata_tv
                                .setVisibility(View.VISIBLE);
                        commen_num_nodata_tv.setText(noData);
                        search_result_lv.setPullLoadEnable(false);
                        changeListView(0);
                    }

                }else{
                    changeListView(0);
                    UIHelper.ToastMessage(mContext,R.string.net_work_error);
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if(null!=dialog){
                    dialog.dismiss();
                }
                UIHelper.ToastMessage(mContext,R.string.net_work_error);
            }
        });
    }

    @Override
    public void initData() {
        type=getIntent().getStringExtra("type");
        setContentView(R.layout.activity_commen_num);
    }

    @Override
    public void initView() {
        search_result_lv= (XListView) findViewById(R.id.search_result_lv);
        commen_num_nodata_tv= (TextView) findViewById(R.id.commen_num_nodata_tv);
        adapter=new ShowVisitorAdapter(mList,mContext);
    }

    @Override
    public void fillView() {
        if("attention".equals(type)){
            head_title_tv.setText("我的关注");
            noData="暂无关注";
        }else if("fans".equals(type)){
            head_title_tv.setText("我的粉丝");
            noData="暂无粉丝";
        }else if("friend".equals(type)){
            head_title_tv.setText("我的好友");
            noData="暂无好友";
        }
        InitLocation();

    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

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

    /**
     * 高德地图定位回调
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            UIHelper.ToastMessage(mContext,"获取当前位置失败");
            return;
        }
        mLat=aMapLocation.getLatitude();
        mLon=aMapLocation.getLongitude();
        locationClient.stopLocation();
        initListView();
        search_result_lv.setPullLoadEnable(true);
        search_result_lv.setDividerHeight(DensityUtil.dip2px(mContext,8));
    }
    @Override
    protected void onDestroy() {
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

}
