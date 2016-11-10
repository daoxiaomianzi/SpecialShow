package com.show.specialshow.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.model.BannerMess;
import com.show.specialshow.model.ShopPeopleMess;
import com.show.specialshow.model.ShopServiceMess;
import com.show.specialshow.utils.BannerPointUtils;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.UIHelper;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServiceIntroduceActivity extends BaseActivity {
    private ShopServiceMess shopServiceMess;
    private ArrayList<ImageView> images = new ArrayList<ImageView>();
    private String shoptitle;
    private String shop_id;
    private List<ShopPeopleMess> shopPeopleMesses;//手艺人集合数据
    private List<ShopServiceMess> shopServiceMesses;//服务集合
    private int currentItem = 0;
    private MyPagerAdapter banner_adapter;
    private ImageView service_des_banner_show_iv;
    //相关控件
    private ViewPager service_des_banner;
    private TextView servicde_des_name;//服务名称
    private TextView service_des_cheap_price;//当前价格
    private TextView service_des_price;//原始价格
    private TextView service_des_tv;//服务说明
    private ScheduledExecutorService scheduledExecutorService;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            service_des_banner.setCurrentItem(currentItem);
        }

        ;
    };

    @SuppressWarnings("unchecked")
    @Override
    public void initData() {
        shopServiceMesses = (List<ShopServiceMess>) getIntent().getSerializableExtra(CraftsmandetailsActivity.SERVICE_LIST);
        shopPeopleMesses = (List<ShopPeopleMess>) getIntent().getSerializableExtra(CraftsmandetailsActivity.PEOPLE_LIST);
        shoptitle = getIntent().getStringExtra("shoptitle");
        shop_id = getIntent().getStringExtra("shop_id");
        shopServiceMess = (ShopServiceMess) getIntent().getSerializableExtra("serviceDes");
        setContentView(R.layout.activity_service_introduce);
    }

    @Override
    public void initView() {
        service_des_banner_show_iv = (ImageView) findViewById(R.id.service_des_banner_show_iv);
        service_des_banner = (ViewPager) findViewById(R.id.service_des_banner_show_vp);
        servicde_des_name = (TextView) findViewById(R.id.servicde_des_name);
        service_des_cheap_price = (TextView) findViewById(R.id.service_des_cheap_price);
        service_des_price = (TextView) findViewById(R.id.service_des_price);
        service_des_tv = (TextView) findViewById(R.id.service_des_tv);
    }

    @Override
    public void fillView() {
        head_title_tv.setText(shoptitle);
        servicde_des_name.setText(shopServiceMess.getService_list_title());
        service_des_cheap_price.setText(shopServiceMess.getService_list_price_now());
        service_des_price.setText(shopServiceMess.getService_list_price_old());
        service_des_tv.setText(shopServiceMess.getService_list_des());
        ImageLoader.getInstance().displayImage(shopServiceMess.getService_list_icon(), service_des_banner_show_iv);
//		loadBanner();
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
            case R.id.service_des_appoint_immed:
                bundle.putString("shop_id", shop_id);
                bundle.putSerializable("serviceDes", shopServiceMess);
                bundle.putSerializable(CraftsmandetailsActivity.SERVICE_LIST, (Serializable) shopServiceMesses);
                bundle.putSerializable(CraftsmandetailsActivity.PEOPLE_LIST, (Serializable) shopPeopleMesses);
                UIHelper.startActivity(mContext, OrderActivity.class, bundle);
                break;
            case R.id.service_des_reservation:
                bundle.putString("shop_id", shop_id);
                bundle.putSerializable("serviceDes", shopServiceMess);
                bundle.putSerializable(CraftsmandetailsActivity.SERVICE_LIST, (Serializable) shopServiceMesses);
                bundle.putSerializable(CraftsmandetailsActivity.PEOPLE_LIST, (Serializable) shopPeopleMesses);
                UIHelper.startActivity(mContext, OrderActivity.class, bundle);
                break;
            case R.id.service_des_banner_show_iv:
                bundle.putString(OneImageShowActivity.ONE_IMAGE_URL,
                        shopServiceMess.getService_list_icon());
                UIHelper.startActivity(mContext, OneImageShowActivity.class, bundle);
                break;

            default:
                break;
        }

    }

    @Override
    protected void onStart() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒钟切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 5, 5,
                TimeUnit.SECONDS);
        super.onStart();
    }

    @Override
    protected void onStop() {
        // 当Activity不可见的时候停止切换
        scheduledExecutorService.shutdown();
        super.onStop();
    }

    /**
     * 换行切换任务
     *
     * @author Administrator
     */
    private class ScrollTask implements Runnable {

        public void run() {
            synchronized (service_des_banner) {
                // System.out.println("currentItem: " + currentItem);
                // currentItem = (currentItem + 1) % images.size();
                currentItem = currentItem + 1;
                // handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
                handler.sendEmptyMessage(-1);
            }
        }

    }

    private void loadBanner() {
        initBanner();
    }

    private void initBanner() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < 1; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ScaleType.FIT_XY);
            ImageLoader.getInstance().displayImage(shopServiceMess.getService_list_icon(), imageView);
            images.add(imageView);
        }
        banner_adapter = new MyPagerAdapter();
        service_des_banner.setCurrentItem(300);//滑屏速度
        service_des_banner.setAdapter(banner_adapter);
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
