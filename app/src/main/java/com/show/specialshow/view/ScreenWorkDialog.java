package com.show.specialshow.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;


import com.show.specialshow.R;
import com.show.specialshow.adapter.GirdDropDownAdapter;
import com.show.specialshow.model.ConditionMess;
import com.show.specialshow.utils.UIHelper;

import java.util.List;

/**
 * Created by xuyong on 2017/2/21.
 */

public class ScreenWorkDialog extends Dialog {
    GirdDropDownAdapter cityAdapter;
    ListView cityView;

    public GirdDropDownAdapter getCityAdapter() {
        return cityAdapter;
    }

    public void setCityAdapter(GirdDropDownAdapter cityAdapter) {
        this.cityAdapter = cityAdapter;
    }

    public ListView getCityView() {
        return cityView;
    }

    public void setCityView(ListView cityView) {
        this.cityView = cityView;
    }

    public ScreenWorkDialog(Context context, List<ConditionMess> stafftypeList) {
        this(context, R.style.Theme_dialog, stafftypeList);
    }


    public ScreenWorkDialog(Context context, int theme, List<ConditionMess> stafftypeList) {
        super(context, theme);
        cityView = new ListView(context);
        cityAdapter = new GirdDropDownAdapter(context, stafftypeList);
        cityView.setDividerHeight(0);
        cityView.setAdapter(cityAdapter);
        setContentView(cityView);
        this.getWindow().getAttributes().gravity = Gravity.TOP;
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.getDecorView().setPadding(0, UIHelper.Dp2Px(context, 45), 0, 0);
        dialogWindow.setAttributes(lp);
    }
}
