package com.show.specialshow.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.utils.ActionSheetDialog;
import com.show.specialshow.utils.FileUtils;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.view.CustomProgressDialog;
import com.show.specialshow.view.PhotoViewAttacher;
import com.show.specialshow.view.PhotoViewAttacher.OnPhotoTapListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageDetailFragment extends BaseFragment {
	private String mImageUrl;
	private ImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;

	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);
		mAttacher = new PhotoViewAttacher(mImageView);
		
		mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
			
			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				getActivity().finish();
			}
		});
		mAttacher.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				showSelectDialog();
				return true;
			}
		});

		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

	/**
	 * 选择弹框
	 */
	private  void showSelectDialog() {
		new ActionSheetDialog(getActivity())
				.builder()
				.setCancelable(true)
				.setCanceledOnTouchOutside(true)
				.addSheetItem("保存图片", ActionSheetDialog.SheetItemColor.Black,
						new ActionSheetDialog.OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								savePhoto();
							}
						}).show();
	}

	/**
	 * 保存图片
	 * @param
     */
	private void savePhoto(){
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
	 	String savePath="/TX_SAVE_PHOTO/"+sdf.format(date) + ".png";
		File file=new File(FileUtils.SDPATH,savePath);
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		HttpUtils httpUtils= new HttpUtils();
		httpUtils.download(mImageUrl, FileUtils.SDPATH+savePath, new RequestCallBack<File>() {
			@Override
			public void onStart() {
				loadIng("正在保存",false);
			}

			@Override
			public void onSuccess(ResponseInfo<File> responseInfo) {
				if(null!=dialog){
					dialog.dismiss();
				}
				if(200==responseInfo.statusCode){
					UIHelper.ToastMessage(getActivity(),"保存成功，在sd下的TX_SAVE_PHOTO文件夹下查看");
				}else{
					UIHelper.ToastMessage(getActivity(),"保存失败");
				}
			}

			@Override
			public void onFailure(HttpException e, String s) {
				if(null!=dialog){
					dialog.dismiss();
				}
				UIHelper.ToastMessage(getActivity(),"保存失败");
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		ImageLoader.getInstance().displayImage(mImageUrl, mImageView, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {
				case IO_ERROR:
					message = "下载错误";
					break;
				case DECODING_ERROR:
					message = "图片无法显示";
					break;
				case NETWORK_DENIED:
					message = "网络有问题，无法下载";
					break;
				case OUT_OF_MEMORY:
					message = "图片太大无法显示";
					break;
				case UNKNOWN:
					message = "未知的错误";
					break;
				}
				Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
				progressBar.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				progressBar.setVisibility(View.GONE);
				mAttacher.update();
			}
		});

		
		
		
	}

	@Override
	public void initData() {

	}

	@Override
	public void initView() {

	}

	@Override
	public void setListener() {

	}

	@Override
	public void fillView() {

	}

	@Override
	public void onClick(View v) {

	}

}
