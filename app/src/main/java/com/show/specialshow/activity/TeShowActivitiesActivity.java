package com.show.specialshow.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.fragment.CraftsmanFragment;
import com.show.specialshow.fragment.IndustryDynamicFragment;
import com.show.specialshow.fragment.OfficialEventFragment;
import com.show.specialshow.fragment.ShowLaneFragment;
import com.show.specialshow.fragment.ShowVisitorFragment;
import com.show.specialshow.fragment.TeShowActivitiesFragment;
import com.show.specialshow.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class TeShowActivitiesActivity extends BaseActivity {

    private ViewPager te_show_vp;
    /**
     * tab显示文本框
     */
    private TextView te_show_activity_tv;
    private TextView te_show_official_event_tv;
    private TextView te_show_industry_dynamic_tv;

    /**
     * tab选择的引导线
     */
    private LinearLayout te_show_line_llt;
    private int currentIndex;

    private List<Fragment> mFragmentList = new ArrayList<>();
    private TeShowFragmentAdapter mAdapter;
    /**
     * fragment
     */
    private TeShowActivitiesFragment activitiesFragment;
    private OfficialEventFragment eventFragment;
    private IndustryDynamicFragment dynamicFragment;//手艺人

    @Override
    public void initData() {
        mAdapter = new TeShowFragmentAdapter(getSupportFragmentManager());
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_te_show_activities);
        te_show_vp = (ViewPager) findViewById(R.id.te_show_vp);
        te_show_activity_tv = (TextView) findViewById(R.id.te_show_activity_tv);
        te_show_official_event_tv = (TextView) findViewById(R.id.te_show_official_event_tv);
        te_show_industry_dynamic_tv = (TextView) findViewById(R.id.te_show_industry_dynamic_tv);
        te_show_line_llt = (LinearLayout) findViewById(R.id.te_show_line_llt);
        initTabLineWidth();
        activitiesFragment = TeShowActivitiesFragment.newInstance();
        eventFragment = OfficialEventFragment.newInstance();
        dynamicFragment = IndustryDynamicFragment.newInstance();
        mFragmentList.add(activitiesFragment);
        mFragmentList.add(eventFragment);
        mFragmentList.add(dynamicFragment);
    }

    @Override
    public void fillView() {
        te_show_vp.setAdapter(mAdapter);
        te_show_vp.setCurrentItem(0);
        resetTextView(0);
    }

    @Override
    public void setListener() {
        te_show_vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
             */
            @Override
            public void onPageScrollStateChanged(int state) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrolled(int position, float offset,
                                       int offsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) te_show_line_llt
                        .getLayoutParams();
                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (offset
                            * ((TXApplication.WINDOW_WIDTH * 1.0-DensityUtil.dip2px(mContext
                    ,120))/ 3) + currentIndex
                            * ((TXApplication.WINDOW_WIDTH * 1.0-DensityUtil.dip2px(mContext
                            ,120)) / 3))+DensityUtil.dip2px(mContext,75);

                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * ((TXApplication.WINDOW_WIDTH * 1.0-DensityUtil.dip2px(mContext
                            ,120)) / 3) + currentIndex
                            * ((TXApplication.WINDOW_WIDTH * 1.0-DensityUtil.dip2px(mContext
                            ,120)) / 3))+DensityUtil.dip2px(mContext,75);

                } else if (currentIndex == 1 && position == 1) // 1->2
                {
                    lp.leftMargin = (int) (offset
                            * ((TXApplication.WINDOW_WIDTH * 1.0-DensityUtil.dip2px(mContext
                            ,120)) / 3) + currentIndex
                            * ((TXApplication.WINDOW_WIDTH * 1.0-DensityUtil.dip2px(mContext
                            ,120)) / 3))+DensityUtil.dip2px(mContext,75);
                } else if (currentIndex == 2 && position == 2) {
                    lp.leftMargin = (int) (offset
                            * ((TXApplication.WINDOW_WIDTH * 1.0-DensityUtil.dip2px(mContext
                            ,120)) / 3) + currentIndex
                            * ((TXApplication.WINDOW_WIDTH * 1.0-DensityUtil.dip2px(mContext
                            ,120)) / 3))+DensityUtil.dip2px(mContext,75);
                } else if (currentIndex == 2 && position == 1) {
                    lp.leftMargin = (int) (-(1 - offset)
                            * ((TXApplication.WINDOW_WIDTH * 1.0-DensityUtil.dip2px(mContext
                            ,120)) / 3) + currentIndex
                            * ((TXApplication.WINDOW_WIDTH * 1.0-DensityUtil.dip2px(mContext
                            ,120)) / 3))+DensityUtil.dip2px(mContext,75);
                }
                te_show_line_llt.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                resetTextView(position);
                currentIndex = position;
            }

        });
    }

    protected void resetTextView(int position) {
        switch (position) {
            case 0:
                te_show_activity_tv.setSelected(true);
                te_show_official_event_tv.setSelected(false);
                te_show_industry_dynamic_tv.setSelected(false);
                break;
            case 1:
                te_show_activity_tv.setSelected(false);
                te_show_official_event_tv.setSelected(true);
                te_show_industry_dynamic_tv.setSelected(false);
                break;
            case 2:
                te_show_activity_tv.setSelected(false);
                te_show_official_event_tv.setSelected(false);
                te_show_industry_dynamic_tv.setSelected(true);
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.te_show_activity_tv:
                te_show_vp.setCurrentItem(0);
                break;
            case R.id.te_show_official_event_tv:
                te_show_vp.setCurrentItem(1);
                break;
            case R.id.te_show_industry_dynamic_tv:
                te_show_vp.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    private void initTabLineWidth() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) te_show_line_llt
                .getLayoutParams();
//        lp.width = (TXApplication.WINDOW_WIDTH- DensityUtil.dip2px(mContext,50))/3;
        te_show_line_llt.setLayoutParams(lp);
    }


    public class TeShowFragmentAdapter extends FragmentPagerAdapter {

        public TeShowFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (mFragmentList != null) {
                return mFragmentList.get(position);
            }
            return null;
        }

        @Override
        public int getCount() {
            if (mFragmentList != null) {
                return mFragmentList.size();
            }
            return 0;
        }

    }
}
