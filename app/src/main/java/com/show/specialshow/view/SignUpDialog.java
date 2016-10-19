package com.show.specialshow.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.utils.DensityUtil;

/**
 * Created by xuyong on 2016/10/19.
 */

public class SignUpDialog {
    private EditText et_name;
    private EditText et_iphoen;
    private Context context;

    public SignUpDialog(EditText et_name, Context context, EditText et_iphoen) {
        this.et_name = et_name;
        this.context = context;
        this.et_iphoen = et_iphoen;
    }

    public void signUpDialog(){
        final Dialog dialog=new Dialog(context,R.style.Theme_dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_dialog_sign_up, null);
        et_name= (EditText) view.findViewById(R.id.et_name);
        et_iphoen =(EditText) view.findViewById(R.id.et_iphone);
        dialog.setContentView(view);
                Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER|Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width=WindowManager.LayoutParams.MATCH_PARENT;
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
//        dialogWindow.getDecorView().setPadding(0,0,0,0);
        dialogWindow.setAttributes(lp);
        dialog.setCancelable(true);
        dialog.show();

    }

}
