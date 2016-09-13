package com.show.specialshow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.show.specialshow.utils.ActionSheetDialog;
import com.show.specialshow.utils.ActionSheetDialog.OnSheetItemClickListener;
import com.show.specialshow.utils.ActionSheetDialog.SheetItemColor;
import com.show.specialshow.utils.AppManager;
import com.show.specialshow.utils.FileUtils;
import com.show.specialshow.view.CustomProgressDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseActivity extends FragmentActivity {
	public static final int DIALOG_DEFAULT_STPE = 0;
	public static final int DIALOG_SINGLE_STPE=1;
	public static final int DIALOG_DOUBLE_STPE=2;//默认的取消与确定
	public static final int DIALOG_AGREE_CANCEL=10;//同意好友与拒绝好友
	public Activity mContext;
	protected TextView head_left_tv;
	protected TextView head_title_tv;
	protected TextView head_right_tv;
	protected RelativeLayout head_rl;
	protected ImageView view_top;
	protected Dialog affirmDialog;
	protected CustomProgressDialog dialog;
	public boolean flag1 = false, flag2 = false, flag3 = false;
	public boolean flag4 = false;
	public int activityFlag=0;
	public TextView contest_confirm_tv;
	// 创建一个以当前系统时间为名称的文件，防止重复
	protected static File tempFile; /*= new File(
			Environment.getExternalStorageDirectory(), getPhotoFileName());*/

	// 使用系统当前日期加以调整作为照片的名称
	@SuppressLint("SimpleDateFormat")
	protected static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("'PNG'_yyyyMMdd_HHmmss");
		return "/TX_PHOTO/"+sdf.format(date) + ".png";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
		mContext = this;
		initData();
		if(activityFlag==0){
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				getWindow().addFlags(
						WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			}
		}
		initView();
		setListener();
		initCommonBar();
		fillView();
	}

	/**
	 * 点返回键调用方法
	 * 
	 * @param
	 */
	public void goBack(View v) {
		finish();
	}

	/**
	 * 触摸事件屏蔽软键盘
	 */
	@Override
	public boolean onTouchEvent(android.view.MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		View currentFocus = this.getCurrentFocus();
		if (null != currentFocus) {
			IBinder windowToken = currentFocus.getWindowToken();
			return imm.hideSoftInputFromWindow(windowToken, 0);
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 初始化数据
	 */
	public abstract void initData();

	/**
	 * 初始化视图
	 */
	public abstract void initView();

	/**
	 * 设置控件显示内容
	 */
	public abstract void fillView();

	public abstract void setListener();

	private void initCommonBar() {
		head_left_tv = (TextView) findViewById(R.id.head_left_tv);
		head_title_tv = (TextView) findViewById(R.id.head_title_tv);
		head_right_tv = (TextView) findViewById(R.id.head_right_tv);
		head_rl = (RelativeLayout) findViewById(R.id.head_rl);
	}

	public abstract void onClick(View v);

	public void createAffirmDialog(String content,int dialogStyle,String tips,boolean isCancel) {
		affirmDialog = new Dialog(mContext, R.style.Theme_dialog);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.view_contest_dialog, null);
		View content_ll = view.findViewById(R.id.content_ll);
		TextView contest_cancel_tv = (TextView) view
				.findViewById(R.id.contest_cancel_tv);
		TextView confirm_dialog_tips=(TextView) view.findViewById(R.id.confirm_dialog_tips);
		ImageView split_iv_vertical = (ImageView) view
				.findViewById(R.id.split_iv_vertical);
		TextView contest_confirm_tv = (TextView) view
				.findViewById(R.id.contest_confirm_tv);

		// TextView contest_confirm_tv=(TextView)
		// view.findViewById(R.id.contest_confirm_tv);
		TextView confirm_dialog_content_tv = (TextView) view
				.findViewById(R.id.confirm_dialog_content_tv);
		switch (dialogStyle) {
			case DIALOG_DEFAULT_STPE:
				contest_cancel_tv.setText("否");
				contest_confirm_tv.setText("是");
				break;
			case DIALOG_SINGLE_STPE:
				contest_cancel_tv.setVisibility(View.GONE);
				split_iv_vertical.setVisibility(View.GONE);
				break;
			case DIALOG_AGREE_CANCEL:
				contest_cancel_tv.setText("拒绝");
				contest_confirm_tv.setText("同意");
				break;
			default:
				break;
		}
		confirm_dialog_content_tv.setText(content);
		confirm_dialog_tips.setText(tips);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				(int) (TXApplication.WINDOW_WIDTH * 0.7),
				LayoutParams.WRAP_CONTENT);
		content_ll.setLayoutParams(params);
		affirmDialog.setContentView(view);
		affirmDialog.setCancelable(isCancel);
		affirmDialog.show();
	}

	public void createAffirmDialog(String content, int dialogStyle,boolean isCancel) {
		affirmDialog = new Dialog(mContext, R.style.Theme_dialog);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.view_contest_dialog, null);
		View content_ll = view.findViewById(R.id.content_ll);
		TextView contest_cancel_tv = (TextView) view
				.findViewById(R.id.contest_cancel_tv);
		ImageView split_iv_vertical = (ImageView) view
				.findViewById(R.id.split_iv_vertical);
		contest_confirm_tv = (TextView) view
				.findViewById(R.id.contest_confirm_tv);
		TextView confirm_dialog_content_tv = (TextView) view
				.findViewById(R.id.confirm_dialog_content_tv);
		switch (dialogStyle) {
		case DIALOG_DEFAULT_STPE:
			contest_cancel_tv.setText("否");
			contest_confirm_tv.setText("是");
			break;
		case DIALOG_SINGLE_STPE:
			contest_cancel_tv.setVisibility(View.GONE);
			split_iv_vertical.setVisibility(View.GONE);
			break;
		case DIALOG_AGREE_CANCEL:
			contest_cancel_tv.setText("拒绝");
			contest_confirm_tv.setText("同意");
			break;
		default:
			break;
		}
		confirm_dialog_content_tv.setText(content);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				(int) (TXApplication.WINDOW_WIDTH * 0.7),
				LayoutParams.WRAP_CONTENT);
		content_ll.setLayoutParams(params);
		affirmDialog.setContentView(view);
		affirmDialog.setCancelable(isCancel);
		affirmDialog.show();
	}
	public void loadIng(String tips, boolean canBack) {
		if (mContext.isFinishing()) {
			return;
		}
		if (dialog != null) {
			dialog.cancel();
		}
		dialog = new CustomProgressDialog(mContext, tips);
		dialog.setCancelable(canBack);
		dialog.show();
	}
	/**
	 * 头像选择弹框
	 */
	protected void showSelectDialog(final int pickcode, final int cameracode) {
		new ActionSheetDialog(mContext)
				.builder()
				.setCancelable(true)
				.setCanceledOnTouchOutside(false)
				.addSheetItem("从手机中选择", SheetItemColor.Black,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								startPick(pickcode);
							}
						})
				.addSheetItem("拍照", SheetItemColor.Black,
						new OnSheetItemClickListener() {

							@Override
							public void onClick(int which) {
								startCamera(cameracode);
							}
						}).show();
	}

	// 调用系统相机
	protected void startCamera(int cameracode) {
		// 调用系统的拍照功能
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra("camerasensortype", 2); // 调用前置摄像头
		intent.putExtra("autofocus", true); // 自动对焦
		intent.putExtra("fullScreen", false); // 全屏
		intent.putExtra("showActionIcons", false);
		// 指定调用相机拍照后照片的存储路径
		tempFile=new File(FileUtils.SDPATH,getPhotoFileName());
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
		startActivityForResult(intent, cameracode);
	}

	// 调用系统相册
	protected void startPick(int pickcode) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(intent, pickcode);
	}

	// 调用系统裁剪
	protected void startPhotoZoom(Uri uri, int size, int cutcode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以裁剪
		intent.putExtra("crop", true);
		// aspectX,aspectY是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX,outputY是裁剪图片的宽高
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		// 设置是否返回数据
		intent.putExtra("return-data", true);
		startActivityForResult(intent, cutcode);
	}

	// 将裁剪后的图片显示在ImageView上
	protected static void setPicToView(Bundle bundle, ImageView imageview) {
		if (null != bundle) {
			final Bitmap bmp = bundle.getParcelable("data");
			imageview.setImageBitmap(bmp);
			saveCropPic(bmp);
		}
	}

	// 把裁剪后的图片保存到sdcard上
	private static void saveCropPic(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileOutputStream fis = null;
		bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
		tempFile=new File(FileUtils.SDPATH,getPhotoFileName());
		try {
			fis = new FileOutputStream(tempFile);
			fis.write(baos.toByteArray());
			fis.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != baos) {
					baos.close();
				}
				if (null != fis) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
