package com.show.specialshow.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.show.specialshow.R;
import com.show.specialshow.view.PhotoView;
import com.show.specialshow.view.PhotoViewAttacher.OnPhotoTapListener;
@SuppressLint("ValidFragment")
public class DynamicImageDetailFragment extends BaseFragment{
	private String mImageUrl;
	private PhotoView mImageView;
	private ProgressBar mProgressBar;
	
	public DynamicImageDetailFragment(String url){
		this.mImageUrl=url;
	}

	public DynamicImageDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v=inflater.inflate(R.layout.image_detail_fragment, container,false);
		mImageView=(PhotoView) v.findViewById(R.id.image);
		mProgressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}
	

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		ImageLoader.getInstance().displayImage(mImageUrl, mImageView, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				mProgressBar.setVisibility(View.VISIBLE);
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
				mProgressBar.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				mProgressBar.setVisibility(View.GONE);
				mImageView.setImageBitmap(loadedImage);
			}
		});
	}

	@Override
	public void setListener() {
		mImageView.setOnPhotoTapListener(new OnPhotoTapListener() {
			
			@Override
			public void onPhotoTap(View view, float x, float y) {
				getActivity().finish();
				
			}
		});
	}

	@Override
	public void fillView() {
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
