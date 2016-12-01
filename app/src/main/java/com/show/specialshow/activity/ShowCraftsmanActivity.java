package com.show.specialshow.activity;

import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.fragment.CraftsmanFragment;
import com.show.specialshow.utils.ImmersedStatusbarUtils;

public class ShowCraftsmanActivity extends BaseActivity {

    @Override
    public void initData() {
        setContentView(R.layout.activity_show_craftsman);
    }

    @Override
    public void initView() {
        CraftsmanFragment craftsmanFragment = new CraftsmanFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.ll_show_craftsman_content
                , craftsmanFragment).show(craftsmanFragment).commit();
    }

    @Override
    public void fillView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ImmersedStatusbarUtils.initAfterSetContentView(this, head_rl);
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        head_left_tv.setVisibility(View.GONE);
        head_title_tv.setText(R.string.circle_nearby_craftsman);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
