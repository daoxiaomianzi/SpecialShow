package com.show.specialshow.view;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.show.specialshow.R;




public class CustomProgressDialog extends Dialog {

	public CustomProgressDialog(Context context, String strMessage) {
		this(context, R.style.CustomProgressDialog, strMessage);
	}

	public CustomProgressDialog(Context context, int theme, String strMessage) {
		super(context, theme);
		this.setContentView(R.layout.view_dialog_progress_new);
		this.getWindow().getAttributes().gravity = Gravity.CENTER;
		View content_ll = findViewById(R.id.custom_content_rl);
		LayoutParams params = content_ll.getLayoutParams();
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		params.width = (int) (dm.widthPixels * 0.4);
		content_ll.setLayoutParams(params);
		TextView tvMsg = (TextView) this.findViewById(R.id.id_tv_loadingmsg);
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (!hasFocus) {
//			dismiss();
		}
	}
}
