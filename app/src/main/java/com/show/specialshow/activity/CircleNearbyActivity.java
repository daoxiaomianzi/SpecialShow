package com.show.specialshow.activity;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.fragment.CraftsmanFragment;
import com.show.specialshow.fragment.ShowLaneFragment;
import com.show.specialshow.fragment.ShowVisitorFragment;

public class CircleNearbyActivity extends BaseActivity {

    private ViewPager nearby_page_vp;
    /**
     * tab显示文本框
     */
    private TextView nearby_tab_show_lane_tv;
    //    private TextView nearby_tab_show_visitor_tv;
    private TextView nearby_tab_craftsman_tv;

    /**
     * tab选择的引导线
     */
    private LinearLayout nearby_tab_line_llt;
    private int currentIndex;

    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private NearbyFragmentAdapter mAdapter;
    /**
     * fragment
     */
    private ShowLaneFragment mShowLaneFg;
    //    private ShowVisitorFragment mShowVisitorFg;//秀客
    private CraftsmanFragment mCraftsmanFg;//手艺人

    @Override
    public void initData() {
        mAdapter = new NearbyFragmentAdapter(getSupportFragmentManager());
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_circle_nearby);
        nearby_page_vp = (ViewPager) findViewById(R.id.nearby_page_vp);
        nearby_tab_show_lane_tv = (TextView) findViewById(R.id.nearby_tab_show_lane_tv);
//        nearby_tab_show_visitor_tv = (TextView) findViewById(R.id.nearby_tab_show_visitor_tv);
        nearby_tab_craftsman_tv = (TextView) findViewById(R.id.nearby_tab_craftsman_tv);
        nearby_tab_line_llt = (LinearLayout) findViewById(R.id.nearby_tab_line_llt);
        initTabLineWidth();
        mShowLaneFg = new ShowLaneFragment();
//        mShowVisitorFg = new ShowVisitorFragment();
        mCraftsmanFg = new CraftsmanFragment();
        mFragmentList.add(mShowLaneFg);
//        mFragmentList.add(mShowVisitorFg);
        mFragmentList.add(mCraftsmanFg);
    }

    @Override
    public void fillView() {
        nearby_page_vp.setAdapter(mAdapter);
        nearby_page_vp.setCurrentItem(0);
        nearby_page_vp.setOffscreenPageLimit(1);
        resetTextView(0);
    }

    @Override
    public void setListener() {
        nearby_page_vp.setOnPageChangeListener(new OnPageChangeListener() {

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
                LinearLayout.LayoutParams lp = (LayoutParams) nearby_tab_line_llt
                        .getLayoutParams();
                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (offset
                            * (TXApplication.WINDOW_WIDTH * 1.0 / 2) + currentIndex
                            * (TXApplication.WINDOW_WIDTH / 2));

                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (TXApplication.WINDOW_WIDTH * 1.0 / 2) + currentIndex
                            * (TXApplication.WINDOW_WIDTH / 2));

                } else if (currentIndex == 1 && position == 1) // 1->2
                {
                    lp.leftMargin = (int) (offset
                            * (TXApplication.WINDOW_WIDTH * 1.0 / 2) + currentIndex
                            * (TXApplication.WINDOW_WIDTH / 2));
//                } else if (currentIndex == 2 && position == 2) {
//                    lp.leftMargin = (int) (offset
//                            * (TXApplication.WINDOW_WIDTH * 1.0 / 3) + currentIndex
//                            * (TXApplication.WINDOW_WIDTH / 3));
//                } else if (currentIndex == 2 && position == 1) {
//                    lp.leftMargin = (int) (-(1 - offset)
//                            * (TXApplication.WINDOW_WIDTH * 1.0 / 3) + currentIndex
//                            * (TXApplication.WINDOW_WIDTH / 3));
                }
                nearby_tab_line_llt.setLayoutParams(lp);
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
                nearby_tab_show_lane_tv.setSelected(true);
//                nearby_tab_show_visitor_tv.setSelected(false);
                nearby_tab_craftsman_tv.setSelected(false);
                break;
//            case 0:
//                nearby_tab_show_lane_tv.setSelected(false);
////                nearby_tab_show_visitor_tv.setSelected(true);
//                nearby_tab_craftsman_tv.setSelected(false);
//                break;
            case 1:
                nearby_tab_show_lane_tv.setSelected(false);
//                nearby_tab_show_visitor_tv.setSelected(false);
                nearby_tab_craftsman_tv.setSelected(true);
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nearby_tab_show_lane_tv:
                nearby_page_vp.setCurrentItem(0);
                break;
//            case R.id.nearby_tab_show_visitor_tv:
//                nearby_page_vp.setCurrentItem(0);
//                break;
            case R.id.nearby_tab_craftsman_tv:
                nearby_page_vp.setCurrentItem(1);
                break;
            default:
                break;
        }
    }

    private void initTabLineWidth() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) nearby_tab_line_llt
                .getLayoutParams();
        lp.width = TXApplication.WINDOW_WIDTH / 2;
        nearby_tab_line_llt.setLayoutParams(lp);
    }


    public class NearbyFragmentAdapter extends FragmentPagerAdapter {

        public NearbyFragmentAdapter(FragmentManager fm) {
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
