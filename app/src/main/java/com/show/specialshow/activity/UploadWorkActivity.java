package com.show.specialshow.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.adapter.GridAdapter;
import com.show.specialshow.model.Bimp;
import com.show.specialshow.model.ImageItem;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.utils.ActionSheetDialog;
import com.show.specialshow.utils.ActionSheetDialog.OnSheetItemClickListener;
import com.show.specialshow.utils.ActionSheetDialog.SheetItemColor;
import com.show.specialshow.utils.FileUtils;
import com.show.specialshow.utils.ImageFactory;
import com.show.specialshow.utils.UIHelper;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;

public class UploadWorkActivity extends BaseActivity {
	private GridView upload_work_picture_gv;
	private GridAdapter mAdapter;
	private static final int TAKE_PICTURE = 0x000001;
	@Override
	public void initData() {
		mAdapter=new GridAdapter(mContext);
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_upload_work);
		 upload_work_picture_gv=(GridView) findViewById(R.id.upload_work_picture_gv);
		 upload_work_picture_gv.setSelector(new ColorDrawable(
					android.graphics.Color.TRANSPARENT));
	}

	@Override
	public void fillView() {
		head_title_tv.setText("上传图片");
		head_right_tv.setVisibility(View.VISIBLE);
		head_right_tv.setText("提交");
		Drawable rightDrawable = getResources()
				.getDrawable(R.drawable.icon_submit);
		rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
				rightDrawable.getMinimumHeight());
		head_right_tv.setCompoundDrawables(rightDrawable, null, null, null);
		mAdapter.update();
		upload_work_picture_gv.setAdapter(mAdapter);
	}

	@Override
	public void setListener() {
		upload_work_picture_gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == Bimp.tempSelectBitmap.size()) {
					showSelectDialog();
				} else {
					Bundle bundle = new Bundle();
					bundle.putString("position", "1");
					bundle.putInt("ID", position);
					UIHelper.startActivity(mContext, GalleryActivity.class,
							bundle);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_right_tv://提交
			upLoadWork();
			break;

		default:
			break;
		}
	}
	
	/**
	 * 上传作品
	 */
	private void upLoadWork(){
		RequestParams params =TXApplication.getParams();
		UserMessage user=TXApplication.getUserMess();
		String url=URLs.USER_PRODUCTIONADD;
	params.addBodyParameter("staffid", user.getUid());
	addImageToParams(params);
	TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
		@Override
		public void onStart() {
			super.onStart();
			loadIng("上传中...", false);
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			dialog.dismiss();
 			UIHelper.ToastMessage(mContext, R.string.net_work_error);
		}

		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			MessageResult result =MessageResult.parse(responseInfo.result);
			if(null==result){
				dialog.dismiss();
				return;
			}
			if(1==result.getSuccess()){
				dialog.dismiss();
					UIHelper.ToastMessage(mContext, result.getMessage());
					finish();
			}else{
				dialog.dismiss();
				UIHelper.ToastMessage(mContext, result.getMessage());
			}
		}
	});
		
	}
	/**
	 * 图片参数
	 * @param params
	 */
	private void addImageToParams(RequestParams params) {
		Iterator<ImageItem> iterator = Bimp.tempSelectBitmap.iterator();
		ImageFactory imageFactory=new ImageFactory();
		int i = Bimp.tempSelectBitmap.size();
		while (iterator.hasNext()) {
			i--;
			ImageItem imageItem = (ImageItem) iterator.next();
			Bitmap bm=imageFactory.ratio(imageItem.getImagePath(),480f,800f);
			String newFilePath = FileUtils.saveBitmap(bm,"/TX_PHOTO/"+String.valueOf(System.currentTimeMillis())
					,
					mContext);
			File tempFile = new File(newFilePath);
			params.addBodyParameter("pic" + i, tempFile);
		}
	}

	private void showSelectDialog() {
		new ActionSheetDialog(mContext)
				.builder()
				.setCancelable(true)
				.setCanceledOnTouchOutside(false)
				.addSheetItem("从手机中选择", SheetItemColor.Black,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								startPhotoAlbum();
							}
						})
				.addSheetItem("拍照", SheetItemColor.Black,
						new OnSheetItemClickListener() {

							@Override
							public void onClick(int which) {
								startPhoto();
							}
						}).show();
	}

	private void startPhotoAlbum() {
		Acp.getInstance(this).request(new AcpOptions.Builder()
						.setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE
						)
						.build(),
				new AcpListener() {
					@Override
					public void onGranted() {
						UIHelper.startActivity(mContext, AlbumActivity.class);
						overridePendingTransition(R.anim.activity_translate_in,
								R.anim.activity_translate_out);
					}

					@Override
					public void onDenied(List<String> permissions) {
						UIHelper.ToastMessage(mContext, "读取sd卡功能取消授权");
					}
				});
	}
	String fileName;
	File out;
	private void startPhoto() {
		Acp.getInstance(this).request(new AcpOptions.Builder()
						.setPermissions(Manifest.permission.CAMERA
						)
						.build(),
				new AcpListener() {
					@Override
					public void onGranted() {
						Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						fileName = "/TX_PHOTO/" + String.valueOf(System.currentTimeMillis());
						out = new File(FileUtils.SDPATH, fileName + ".JPEG");
						if (!out.getParentFile().exists()) {
							out.getParentFile().mkdirs();
						}
						Uri uri = Uri.fromFile(out);
						openCameraIntent.
								putExtra(MediaStore.EXTRA_OUTPUT, uri);// 获取拍照后未压缩的原图片，并保存在uri路径中
						startActivityForResult(openCameraIntent, TAKE_PICTURE);
					}

					@Override
					public void onDenied(List<String> permissions) {
						UIHelper.ToastMessage(mContext, "相机功能取消授权");
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case TAKE_PICTURE:
			Message message=new Message();
			Bundle bundle=new Bundle();
			bundle.putSerializable("data", (Serializable) data);
			bundle.putInt("resultCode",resultCode);
			message.setData(bundle);//bundle传值，耗时，效率低
			message.what=1;//标志是哪个线程传数据
			sendHandle.sendMessage(message);//发送message信息
			break;
		default:
			break;
		}
	}
	private SendHandle sendHandle =new SendHandle(this);
	class SendHandle extends Handler {
		WeakReference<UploadWorkActivity> mActivity;

		SendHandle(UploadWorkActivity activity) {
			mActivity = new WeakReference<>(activity);
		}
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(1==msg.what){
				Intent data = (Intent) msg.getData().getSerializable("data");
				int resultCode = msg.getData().getInt("resultCode");
				if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {
					Bitmap bm = null;
					if(data!=null){
						bm= (Bitmap) data.getExtras().get("data");
					}else{
						ImageFactory imageFactory = new ImageFactory();
						bm= imageFactory.ratio(out.getAbsolutePath(),480f,800f);
					}
					String newFilePath = "";
					newFilePath = FileUtils.saveBitmap(bm, fileName, mContext);

					ImageItem takePhoto = new ImageItem();
					if (!StringUtils.isEmpty(newFilePath)) {
						takePhoto.setImagePath(newFilePath);
					}
					takePhoto.setBitmap(bm);
					Bimp.tempSelectBitmap.add(takePhoto);
					mAdapter.update();
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		Bimp.tempSelectBitmap.clear();
		Bimp.onceSelectBitmap.clear();
		Bimp.max = 0;
		super.onDestroy();
	}
	@Override
	protected void onResume() {
		super.onResume();
		mAdapter.update();
	}
}
