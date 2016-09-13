package com.show.specialshow.utils;

import java.util.Iterator;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * 
 * 
 * @version 1.0
 * @created 2013-8-9
 */
public class AppManager {
	private static Stack<Activity> activityStack;
	private static AppManager instance;
	public static String appStartName;

	private AppManager() {
	}

	/**
	 * 单一实例
	 */
	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束指定个数Activity（按压入堆栈中的顺序）
	 */
	public void finishActivity(int size) {
		for (int i = 0; i < size; i++) {
			Activity activity = activityStack.lastElement();
			finishActivity(activity);
		}
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		try {
			for (Activity activity : activityStack) {
				if (activity.getClass().equals(cls)) {
					finishActivity(activity);
				}
			}
		} catch (Exception e) {
		}
		
	}

	/**
	 * 返回启动页
	 */
	public void goAppStart(Context context) {
		Iterator<Activity> iterator = activityStack.iterator();
		if (TextUtils.isEmpty(appStartName)) {
			appStartName = (String) SPUtils.get(context, "appStartName", "");
		}
		try {
			Intent appStart = new Intent(context, Class.forName(appStartName));
			appStart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(appStart);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		while (iterator.hasNext()) {
			Activity activity = iterator.next();
			if (StringUtils.isNotEmpty(appStartName)
					&& !activity.getClass().getName().equals(appStartName)) {
				iterator.remove();
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 退出应用程序
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {
		}
	}

	public boolean isRunningForeground(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String currentPackageName = cn.getPackageName();
		return !TextUtils.isEmpty(currentPackageName)
				&& currentPackageName.equals(context.getPackageName());
	}
}
