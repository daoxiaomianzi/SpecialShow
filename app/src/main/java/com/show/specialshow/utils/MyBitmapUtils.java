package com.show.specialshow.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class MyBitmapUtils {
	private static BitmapFactory.Options options;
	static {
		options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.ARGB_8888;
		options.inPurgeable = true;// 允许可清除
		options.inInputShareable = true;// 以上options的两个属性必须联合使用才会有效果
	}

	public static Bitmap createNormalBitmap(Context context, int id) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				id, options);
		return bitmap;
	}

	public static Bitmap showBitmap(Activity activity, int viewId, int bitMapId) {
		ImageView imageView = (ImageView) activity.findViewById(viewId);
		return showBitmap(activity, imageView, bitMapId);
	}

	public static Bitmap showBitmap(Activity activity, ImageView imageView,
			int bitMapId) {
		Bitmap bitmap = createNormalBitmap(activity, bitMapId);
		imageView.setImageBitmap(bitmap);
		return bitmap;
	}

	public static void recycleBitmap(Bitmap... bitmaps) {
		for (Bitmap bitmap : bitmaps)
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
			}
	}
}
