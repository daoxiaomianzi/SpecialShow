package com.show.specialshow.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.show.specialshow.BaseSearchActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.adapter.CircleDynamicAdapter;
import com.show.specialshow.contstant.ConstantValue;
import com.show.specialshow.model.BannerMess;
import com.show.specialshow.model.CircleDynamicItem;
import com.show.specialshow.model.CircleDynamicList;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.receiver.MyReceiver;
import com.show.specialshow.utils.BannerPointUtils;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.xlistview.XListView;

import org.apache.commons.lang3.StringUtils;

public class CircleDynamicActivity extends BaseSearchActivity {
    private List<CircleDynamicItem> mList = new ArrayList<CircleDynamicItem>();
    private CircleDynamicList mDynamicList;
//
//    private View header_banner;
//    private ViewPager dynamic_banner;
//    private MyPagerAdapter banner_adapter;
//    private ArrayList<ImageView> images = new ArrayList<ImageView>();
//    private TextView dynamic_banner_describe_tv;
//    private LinearLayout dynamic_banner_show_adddot;//banner小点
//    private ScheduledExecutorService scheduledExecutorService;
//    private int currentItem = 0;
//    private BannerPointUtils bannerPointUtils;//banner小点工具类
//    private ArrayList<ImageView> pointviews = new ArrayList<>();
//
//    //banner数据
//    private List<BannerMess> bannerList;
//
//    private Handler handler = new Handler() {
//        public void handleMessage(android.os.Message msg) {
//            dynamic_banner.setCurrentItem(currentItem);
//        }
//    };

    @Override
    protected void getData() {
//        if (bannerList == null) {
//            loadBanner();
//        }
        RequestParams params = TXApplication.getParams();
        String url = URLs.GET_CIRCLE_DYNAMIC;
        String uid = TXApplication.filename.getString("uid", "");
        params.addBodyParameter("pageSize", "" + ConstantValue.PAGE_SIZE);
        params.addBodyParameter("pageNow", "" + pageIndex);
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("address", SPUtils.get(mContext, "city", "上海").toString());
        TXApplication.post(null, mContext, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        changeListView(0);
                        UIHelper.ToastMessage(mContext, R.string.net_work_error);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        MessageResult result = MessageResult
                                .parse(responseInfo.result);
                        if (result == null) {
                            return;
                        }
                        switch (result.getSuccess()) {
                            case 1:
                                if (search_result_lv.getState() != XListView.LOAD_MORE) {
                                    String oldCity = SPUtils.get(mContext, "oldCity", "上海").toString();
                                    String city = SPUtils.get(mContext, "city", "上海").toString();
                                    if (!oldCity.equals(city)) {
                                        if (null != mList) {
                                            mList.clear();
                                        }
                                    }
                                }
                                String info = result.getData();
                                CircleDynamicList circleDynamicList = CircleDynamicList
                                        .parse(info);
                                List<CircleDynamicItem> list = circleDynamicList
                                        .getList();
                                if (null == list) {
                                    changeListView(0);
                                    return;
                                }
                                int size = list.size();
                                totalRecord = circleDynamicList.getTotal();
                                if (search_result_lv.getState() == XListView.LOAD_REFRESH) {
                                    if (null != mList) {
                                        mList.clear();
                                    }
                                }
                                mList.addAll(list);
//							for (int i = 0; i < mList.size(); i++) {
//								for (int j = mList.size() - 1; j > i; j--) {
//									if (mList.get(j).getIdStr()
//											.equals(mList.get(i).getIdStr())) {
//										mList.remove(j);
//									}
//								}
//							}
                                for (int i = mList.size() - 1; i > 0; i--) {
                                    for (int j = i - 1; j >= 0; j--) {
                                        if (mList.get(j).getIdStr().equals(mList.get(i).getIdStr())) {
                                            mList.remove(j);
                                            break;
                                        }
                                    }
                                }
                                localRecord = mList.size();
                                changeListView(size);
                                // UIHelper.ToastLogMessage(mContext,
                                // result.getMessage());
                                break;
                            default:
                                changeListView(0);
                                UIHelper.ToastLogMessage(mContext,
                                        result.getMessage());
                                break;
                        }
                    }
                });
    }
//
//    @Override
//    protected void onStart() {
//        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//        // 当Activity显示出来后，每两秒钟切换一次图片显示
//        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 5, 5,
//                TimeUnit.SECONDS);
//        super.onStart();
//    }

    @Override
    public void initData() {
        adapter = new CircleDynamicAdapter(mContext, mList);
    }

//    private void loadBanner() {
//        RequestParams params = TXApplication.getParams();
//        String url = URLs.LOGIN_BANNER;
//        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
//
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                MessageResult result = MessageResult.parse(responseInfo.result);
//                if (null == result) {
//                    return;
//                }
//                if (1 == result.getSuccess()) {
//                    String info = result.getData();
//                    if (null != info) {
//                        if (bannerList != null) {
//                            bannerList.clear();
//                        }
//                        bannerList = BannerMess.parse(info);
//                        if (null != bannerList) {
//                            initBanner();
//                        }
//                    }
//                } else {
//                    return;
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//
//            }
//        });
//    }
//
//    private void initBanner() {
//        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
//                LayoutParams.WRAP_CONTENT);
//        if (images != null) {
//            images.clear();
//        }
//        for (int i = 0; i < bannerList.size(); i++) {
//            ImageView imageView = new ImageView(mContext);
//            imageView.setLayoutParams(params);
//            imageView.setScaleType(ScaleType.FIT_XY);
//            final BannerMess bannerMess = bannerList.get(i);
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (StringUtils.isEmpty(bannerMess.getUrl())) {
//                        return;
//                    }
//                    Bundle bundle = new Bundle();
//                    bundle.putString("banner_path", bannerMess.getUrl());
//                    UIHelper.startActivity(mContext, BannerWebActivity.class, bundle);
//                }
//            });
//            ImageLoader.getInstance().displayImage(bannerList.get(i).getImagePath(), imageView);
//            images.add(imageView);
//        }
//        banner_adapter = new MyPagerAdapter();
//        dynamic_banner.setCurrentItem(300);
//        dynamic_banner.setAdapter(banner_adapter);
//        bannerPointUtils = new BannerPointUtils(mContext, dynamic_banner_show_adddot, pointviews
//        );
//        if (null != bannerList) {
//            bannerPointUtils.initPoint(bannerList.size());
//            bannerPointUtils.draw_Point(0);
//        }
//    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_circle_dynamic);
        search_result_lv = (XListView) findViewById(R.id.search_result_lv);
//        header_banner = View.inflate(mContext,
//                R.layout.view_dynamic_banner_page, null);
//        dynamic_banner = (ViewPager) header_banner
//                .findViewById(R.id.dynamic_banner_show_vp);
//        dynamic_banner_show_adddot = (LinearLayout) header_banner.findViewById(R.id.dynamic_banner_show_adddot);
//        dynamic_banner_describe_tv = (TextView) header_banner
//                .findViewById(R.id.dynamic_banner_describe_tv);
//        search_result_lv.addHeaderView(header_banner);

    }

    @Override
    public void fillView() {
        initListView();
        registerBoradcastReceiver();
//		registerDetailBoradcastReceiver();
        registerSendBoradcastReceiver();
    }

    public void registerDetailBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(CircleDynamicDetailActivity.ACTION_NAME_DETAIL);
        // 注册广播
        mContext.registerReceiver(mDetailBroadcastReceiver, myIntentFilter);
    }

    private MyReceiver mDetailBroadcastReceiver = new MyReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(CircleDynamicDetailActivity.ACTION_NAME_DETAIL)) {
                CircleDynamicAdapter.cache_attention.clear();
//				if (mList != null) {
//					mList.clear();
//				}
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
                CircleDynamicAdapter.cache_attention.clear();
                if (mList != null) {
                    mList.clear();
                }
                pageIndex = 1;
                search_result_lv.setState(XListView.LOAD_REFRESH);
                getData();
            }
        }

    };

    public void registerSendBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(SendCardActivity.SEND_ACTION_NAME);
        // 注册广播
        mContext.registerReceiver(mSendBroadcastReceiver, myIntentFilter);
    }

    private MyReceiver mSendBroadcastReceiver = new MyReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(SendCardActivity.SEND_ACTION_NAME)) {
                CircleDynamicAdapter.cache_attention.clear();
                if (mList != null) {
                    mList.clear();
                }
                pageIndex = 1;
                search_result_lv.setState(XListView.LOAD_REFRESH);
                getData();
            }
        }

    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(mBroadcastReceiver);
//		mContext.unregisterReceiver(mDetailBroadcastReceiver);
        mContext.unregisterReceiver(mSendBroadcastReceiver);
    }
//
//    @Override
//    protected void onStop() {
//        // 当Activity不可见的时候停止切换
//        scheduledExecutorService.shutdown();
//        super.onStop();
//    }

    @Override
    public void setListener() {
        search_result_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (!BtnUtils.getInstance().isFastDoubleClick()) {
                    return;
                }
                Bundle bundle = new Bundle();
                CircleDynamicItem item = mList.get(position - 1);
                bundle.putString("idStr", item.getIdStr());
                UIHelper.startActivity(mContext,
                        CircleDynamicDetailActivity.class, bundle);
            }
        });
//        dynamic_banner.setOnPageChangeListener(new OnPageChangeListener() {
//
//            @Override
//            public void onPageSelected(int position) {
//                currentItem = position;
//                bannerPointUtils.draw_Point(position % images.size());
//            }
//
//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int arg0) {
//
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }
//
//    /**
//     * 换行切换任务
//     *
//     * @author Administrator
//     */
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
//
//    private class MyPagerAdapter extends PagerAdapter {
//        @Override
//        public int getCount() {
//            return Integer.MAX_VALUE;
//            // return images.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(View arg0, Object arg1) {
//            return arg0 == arg1;
//        }
//
//        // 添加数据
//        @Override
//        public Object instantiateItem(ViewGroup viewPager, int position) {
//            ImageView imageView;
//            imageView = images.get(position % images.size());
//
//            ViewGroup parent = (ViewGroup) imageView.getParent();
//            if (parent != null)
//                viewPager.removeView(imageView);
//            viewPager.addView(imageView);
//            return imageView;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup viewPager, int position, Object object) {
//        }
//    }

}
