package com.show.specialshow.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.lidroid.xutils.bitmap.core.BitmapSize;
import com.show.specialshow.R;
import com.show.specialshow.ninegridlayout.ScreenTools;

public class ImageLoderutils {

	private BitmapUtils bitmapUtils;
	private Context mContext;
	private boolean isChange = false;
	private int leftAndRight;

	public int getLeftAndRight() {
		return leftAndRight;
	}

	public void setLeftAndRight(int leftAndRight) {
		this.leftAndRight = leftAndRight;
	}



	public boolean isChange() {
		return isChange;
	}

	public void setChange(boolean isChange) {
		this.isChange = isChange;
	}

	public ImageLoderutils(Context context) {
		this.mContext = context;
		bitmapUtils = new BitmapUtils(mContext);
		// TODO
		 bitmapUtils.configDefaultLoadingImage(R.drawable.ic_launcher);//默认背景图片
		 bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_launcher);//加载失败图片
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

	/**
	 * @author admin 加载到背景图片回调函数
	 */
	public class backgroundBitmapLoadCallBack extends
			DefaultBitmapLoadCallBack<View> {
		@Override
		public void onLoading(View container, String uri,
				BitmapDisplayConfig config, long total, long current) {
			// TODO Auto-generated method stub
			super.onLoading(container, uri, config, total, current);
		}
		@Override
		public void onPreLoad(View container, String uri,
				BitmapDisplayConfig config) {
			super.onPreLoad(container, uri, config);
		}

		@Override
		public void onLoadCompleted(View container, String uri, Bitmap bitmap,
				BitmapDisplayConfig config, BitmapLoadFrom from) {
			 super.onLoadCompleted(container, uri, bitmap, config, from);
			fadeInDisplayForBack(container, bitmap);
		}

		@Override
		public void onLoadFailed(View container, String uri, Drawable drawable) {
			 super.onLoadFailed(container, uri, drawable);
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
		if(isChange){
			int totalWidth;
			int imageWidth;
			int imageHeight;
			ScreenTools screentools = ScreenTools.instance(mContext);
			totalWidth = screentools.getScreenWidth() - screentools.dip2px(leftAndRight);
			imageWidth = bitmap.getWidth();
			imageHeight =bitmap.getHeight();
			if (imageWidth <= imageHeight) {
				if (imageHeight > totalWidth) {
					imageHeight = totalWidth;
					imageWidth=(int) (imageHeight/(1.3));
				}
			} else {
				if (imageWidth > totalWidth) {
					imageWidth = totalWidth;
					imageHeight=(int) (imageWidth/(1.6));
				}
			}
			ViewGroup.LayoutParams layoutparams = imageView
					.getLayoutParams();
			layoutparams.height = imageHeight;
			layoutparams.width = imageWidth;
			imageView.setLayoutParams(layoutparams);
		}
		final TransitionDrawable transitionDrawable = new TransitionDrawable(
				new Drawable[] { TRANSPARENT_DRAWABLE,
						new BitmapDrawable(imageView.getResources(), bitmap) });
		imageView.setImageDrawable(transitionDrawable);
		transitionDrawable.startTransition(500);
	}

	@SuppressLint("NewApi")
	private void fadeInDisplayForBack(View view, Bitmap bitmap) {// 目前流行的渐变效果
//		final TransitionDrawable transitionDrawable = new TransitionDrawable(
//				new Drawable[] { TRANSPARENT_DRAWABLE,
//						new BitmapDrawable(view.getResources(), bitmap) });
//		view.setBackground(transitionDrawable);
//		transitionDrawable.startTransition(500);
		view.setBackground(new BitmapDrawable(bitmap));
	}

	public void display(ImageView container, String url) {// 外部接口函数
		// bitmapUtils.display(container, url,new CustomBitmapLoadCallBack());

		try {
			// InputStream r=null;
			BitmapDisplayConfig config = new BitmapDisplayConfig();
			// if (bitmapUtils.getBitmapFileFromDiskCache(url) != null) {
			// r = new FileInputStream(
			// bitmapUtils.getBitmapFileFromDiskCache(url));
			// Bitmap b = BitmapFactory.decodeStream(r);
			// int imgWidth = b.getWidth();
			// int imgHeight = b.getHeight();
			// config.setBitmapMaxSize(new BitmapSize(imgWidth*5, imgHeight*5));
			// } else {
			config.setBitmapMaxSize(new BitmapSize(1125 * 3, 6782 * 3));
			// }
			// bitmapUtils.configDownloader(new Downloader() {
			//
			// @Override
			// public long downloadToStream(String arg0, OutputStream arg1,
			// BitmapLoadTask<?> arg2) {
			// Bitmap b = BitmapFactory.decodeStream(arg1);
			// return 0;
			// }
			// });
			// URL urls = new URL(url);
			// HttpURLConnection conn =
			// (HttpURLConnection)urls.openConnection();
			// conn.setConnectTimeout(5000);
			// conn.setRequestMethod("GET");
			// if(conn.getResponseCode() == 200){
			// InputStream inputStream = conn.getInputStream();
			// Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			// }
			bitmapUtils.display(container, url, config,
					new CustomBitmapLoadCallBack());
			// if(r!=null){
			// r.close();
			// }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		// bitmapUtils.display(container, url, config);
	}

	public void displayForBack(View container, String url) {// 外部接口函数
		bitmapUtils.display(container, url, new backgroundBitmapLoadCallBack());
	}
}
