package com.show.specialshow.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.lidroid.xutils.bitmap.core.BitmapSize;

public class XUtilsImageLoader {// 框架里面设置了缓存和异步操作，不用单独设置线程池和缓存机制（也可以自定义缓存路径）
	private BitmapUtils bitmapUtils;
	private Context mContext;
	private boolean isRedPage = false;

	public boolean isRedPage() {
		return isRedPage;
	}

	public void setRedPage(boolean isRedPage) {
		this.isRedPage = isRedPage;
		if(isRedPage){
			bitmapUtils.clearCache();
		}
	}

	public XUtilsImageLoader(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		bitmapUtils = new BitmapUtils(mContext);
		// TODO
		if (!isRedPage) {
//			bitmapUtils
//					.configDefaultLoadingImage(R.drawable.img_loading_banner);// 默认背景图片
//			bitmapUtils
//					.configDefaultLoadFailedImage(R.drawable.img_loading_banner);// 加载失败图片
		}
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
	}

	/**
	 * 
	 * @author sunglasses
	 * @category 图片回调函数
	 */
	public class CustomBitmapLoadCallBack extends
			DefaultBitmapLoadCallBack<ImageView> {

		@Override
		public void onLoading(ImageView container, String uri,
				BitmapDisplayConfig config, long total, long current) {
		}

		@Override
		public void onLoadCompleted(ImageView container, String uri,
				Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
			// super.onLoadCompleted(container, uri, bitmap, config, from);
			fadeInDisplay(container, bitmap);
		}

		@Override
		public void onLoadFailed(ImageView container, String uri,
				Drawable drawable) {
		}
	}

	private static final ColorDrawable TRANSPARENT_DRAWABLE = new ColorDrawable(
			android.R.color.transparent);

	/**
	 * @author sunglasses
	 * @category 图片加载效果
	 * @param imageView
	 * @param bitmap
	 */
	private void fadeInDisplay(ImageView imageView, Bitmap bitmap) {// 目前流行的渐变效果
		if (isRedPage) {
			TranslateAnimation ta = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
					0, Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, -0.5f);
			ta.setFillAfter(true);
			ta.setDuration(2000);
			imageView.setAnimation(ta);

			WindowManager wm = (WindowManager) mContext
					.getSystemService(Context.WINDOW_SERVICE);
			int width = wm.getDefaultDisplay().getWidth();
			if (width > 720) {
				Matrix matrix = new Matrix();
				matrix.postScale(2.0f, 2.0f); // 长和宽放大缩小的比例
				Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				imageView.setImageBitmap(resizeBmp);
			} else {
				imageView.setImageBitmap(bitmap);
			}
			return;
		}
//		try {
//			bitmap=revitionImageSize(bitmap);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		final TransitionDrawable transitionDrawable = new TransitionDrawable(
				new Drawable[] { TRANSPARENT_DRAWABLE,
						new BitmapDrawable(imageView.getResources(), bitmap) });
		imageView.setImageDrawable(transitionDrawable);
		transitionDrawable.startTransition(500);
	}

	public void display(ImageView container, String url) {// 外部接口函数
		if (isRedPage) {
			bitmapUtils.configMemoryCacheEnabled(false);
//			bitmapUtils.clearCache();
			WindowManager wm = (WindowManager) mContext
					.getSystemService(Context.WINDOW_SERVICE);
			int width = wm.getDefaultDisplay().getWidth();
			BitmapDisplayConfig config = new BitmapDisplayConfig();
			config.setBitmapMaxSize(new BitmapSize(93 * 2, 117 * 2));
			if (width > 720) {
				bitmapUtils.display(container, url, config,
						new CustomBitmapLoadCallBack());
				return;
			}
		}
//		BitmapDisplayConfig config = new BitmapDisplayConfig();
//		config.setBitmapMaxSize(new BitmapSize(700, 445));
		bitmapUtils.display(container, url, new CustomBitmapLoadCallBack());
	}
	
	private  Bitmap revitionImageSize(Bitmap bmp) throws IOException {
//		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
//				new ));
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
		InputStream in = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000)) {
//				in = new BufferedInputStream(
//						new FileInputStream(new File(path)));
				in = new ByteArrayInputStream(baos.toByteArray());
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}
}
