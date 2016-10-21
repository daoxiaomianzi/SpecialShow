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

public class SignUpDialog extends Dialog{

    private Context context;
    private Dialog dialog;

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public SignUpDialog(Context context) {
        super(context);
        this.context = context;
    }

    public View signUpDialog(){
        dialog=new Dialog(context,R.style.Theme_dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_dialog_sign_up, null);
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
        return view;
    }

}
