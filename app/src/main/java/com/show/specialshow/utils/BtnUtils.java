package com.show.specialshow.utils;

/**
 * 
 * Created by wwt on 2015/2/13.
 * 
 */
public class BtnUtils {

	public String TAG = BtnUtils.class.getSimpleName();

	private static BtnUtils instance;

	public static BtnUtils getInstance() {
		if (instance == null) {
			instance = new BtnUtils();
		}
		return instance;
	}

	private static long lastClickTime = 0;

	/**
	 * 防止连续点击
	 * 
	 * @return
	 * 
	 */
	public boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime > 500) {
			lastClickTime = time;
			return true;
		} else {
			lastClickTime = time;
			return false;
		}
	}

}
