package com.show.specialshow.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.show.specialshow.R;


public class Two_dimensionDialog extends Dialog {

	public Two_dimensionDialog(Context context, Bitmap bitmap) {
		this(context, R.style.CustomProgressDialog, bitmap);
	}

	public Two_dimensionDialog(Context context, int theme, Bitmap bitmap) {
		super(context, theme);
		this.setContentView(R.layout.view_dialog_two_dimension);
		this.getWindow().getAttributes().gravity = Gravity.CENTER;
		View content_ll = findViewById(R.id.two_dimension_content_rl);
		LayoutParams params = content_ll.getLayoutParams();
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		params.width = (int) (dm.widthPixels * 0.8);
		content_ll.setLayoutParams(params);
		ImageView my_two_dimension_iv =  (ImageView) this.findViewById(R.id.my_two_dimension_iv);
		if (my_two_dimension_iv != null) {
			my_two_dimension_iv.setImageBitmap(bitmap);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (!hasFocus) {
			dismiss();
		}
	}
}
