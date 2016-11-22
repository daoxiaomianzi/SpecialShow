package com.show.specialshow.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.adapter.GridAdapter;
import com.show.specialshow.model.Bimp;
import com.show.specialshow.model.ImageItem;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.ShopItem;
import com.show.specialshow.utils.ActionSheetDialog;
import com.show.specialshow.utils.ActionSheetDialog.OnSheetItemClickListener;
import com.show.specialshow.utils.ActionSheetDialog.SheetItemColor;
import com.show.specialshow.utils.FileUtils;
import com.show.specialshow.utils.ImageFactory;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.UIHelper;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SendCardActivity extends BaseActivity {
    private static final String SHOW_CARD_STATUS = "秀卡";
    private static final String BORROW_CARD_STATUS = "蹭卡";
    private static final String SEND_STATE = "动态";
    private static final String SEND_REVIEW = "点评";
    private static final int TAKE_PICTURE = 0x000001;
    private static final int TAKE_STORE = 0x000010;
    static final int TAKE_LABEL = 0X000011;
    private TextView send_card_addtion_slogan_tv;
    private TextView send_card_select_label_tv;
    private EditText send_card_describe_et;
    private TextView send_card_describe_count_tv;
    private TextView send_card_publish_tv;
    private GridView send_card_picture_gv;
    private GridAdapter mAdapter;
    private LinearLayout send_card_addtion_llt;
    private int sendType;
    private String shoptitle;
    private String shopid;
    private String user_id;

    private ImageView send_card_add_img_iv;
    public static Bitmap bimap;

    private ShopItem mShop;
    private String mLabel = "";
    private List<String> mLabels = new ArrayList<>();
    private SendHandle sendHandle = new SendHandle(this);
    public static final String SEND_ACTION_NAME = "发送动态页发送广播到动态页";


    class SendHandle extends Handler {
        WeakReference<SendCardActivity> mActivity;

        SendHandle(SendCardActivity activity) {
            mActivity = new WeakReference<SendCardActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (1 == msg.what) {
                Intent data = (Intent) msg.getData().getSerializable("data");
                int resultCode = msg.getData().getInt("resultCode");
                if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {
                    Bitmap bm = null;
                    if (data != null) {
                        bm = (Bitmap) data.getExtras().get("data");
                    } else {
                        ImageFactory imageFactory = new ImageFactory();
                        bm = imageFactory.ratio(out.getAbsolutePath(), 480f, 800f);
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

    @SuppressLint("NewApi")
    @Override
    public void initData() {
        bimap = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_add_img);
        mAdapter = new GridAdapter(mContext);
        sendType = getIntent().getExtras().getInt("send_type");
        shoptitle = getIntent().getExtras().getString("shoptitle", "");
        shopid = getIntent().getExtras().getString("shop_id", "");
        user_id = getIntent().getExtras().getString("user_id", "");
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_send_card);
        send_card_addtion_llt = (LinearLayout) findViewById(R.id.send_card_addtion_llt);
        send_card_add_img_iv = (ImageView) findViewById(R.id.send_card_add_img_iv);
        send_card_addtion_slogan_tv = (TextView) findViewById(R.id.send_card_addtion_slogan_tv);
        send_card_select_label_tv = (TextView) findViewById(R.id.send_card_select_label_tv);
        send_card_describe_et = (EditText) findViewById(R.id.send_card_describe_et);
        send_card_describe_count_tv = (TextView) findViewById(R.id.send_card_describe_count_tv);
        send_card_publish_tv = (TextView) findViewById(R.id.send_card_publish_tv);
        send_card_picture_gv = (GridView) findViewById(R.id.send_card_picture_gv);
        send_card_picture_gv.setSelector(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
    }

    @Override
    public void fillView() {
        mAdapter.update();
        send_card_picture_gv.setAdapter(mAdapter);
        updataView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.update();
    }

    private void updataView() {
        String title_show = "";
        String append_choose_show = "";
        switch (sendType) {
            case SelectSendTypeActivity.SHOW_CARD_CODE:
                title_show = "要秀卡";
                append_choose_show = MessageFormat.format("可用门店:{0}",
                        (null == mShop) ? "" : mShop.getShop_names());
                break;
            case SelectSendTypeActivity.BORROW_CARD_CODE:
                title_show = "想蹭卡";
                append_choose_show = MessageFormat.format("目标门店:{0}",
                        (null == mShop) ? "" : mShop.getShop_names());
                break;
            case SelectSendTypeActivity.SEND_STATE_CODE:
                title_show = "发状态";
                if (mLabels == null || mLabels.isEmpty() || mLabels.size() == 0) {
                    append_choose_show = "选择内容标签(最多三项)";
                } else {
                    for (int i = 0; i < mLabels.size(); i++) {
                        String str = mLabels.get(i);
                        append_choose_show = append_choose_show + str;
                    }
                }
                break;
            case StoresDetailsActivity.SHOP_SHOW_CARD:
                title_show = "要秀卡";
                append_choose_show = MessageFormat.format("{0}",
                        shoptitle);
                send_card_addtion_llt.setEnabled(false);
                send_card_select_label_tv.setVisibility(View.INVISIBLE);
                break;
            case StoresDetailsActivity.SHOP_CENG_CARD:
                title_show = "要蹭卡";
                append_choose_show = MessageFormat.format("{0}",
                        shoptitle);
                send_card_addtion_llt.setEnabled(false);
                send_card_select_label_tv.setVisibility(View.INVISIBLE);
                break;
            case StoresDetailsActivity.SHOP_REVIEW:
                title_show = "点评";
                send_card_addtion_llt.setVisibility(View.GONE);
                break;
            case CraftsmandetailsActivity.STAFF_REVIEW:
                title_show = "点评";
                send_card_addtion_llt.setVisibility(View.GONE);
        }
        head_title_tv.setText(title_show);
        send_card_addtion_slogan_tv.setText(append_choose_show);
    }

    @Override
    public void setListener() {
        send_card_picture_gv.setOnItemClickListener(new OnItemClickListener() {
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
        send_card_describe_et.addTextChangedListener(new TextWatcher() {

            private int cou = 0;
            int selectionEnd = 0;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                cou = send_card_describe_et.length();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (cou > 500) {
                    UIHelper.ToastMessage(mContext, "最大输入字数不能超过 500 个字");
                    selectionEnd = send_card_describe_et.getSelectionEnd();
                    s.delete(500, selectionEnd);
                    send_card_describe_count_tv.setText("500/500");
                } else {
                    send_card_describe_count_tv.setText(MessageFormat.format(
                            "{0}/500", cou));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_card_add_img_iv:

                break;
            case R.id.send_card_publish_tv://发布
                isPublish();
                break;
            case R.id.send_card_addtion_llt:
                startBySendType();
                break;
            default:
                break;
        }
    }

    private void startBySendType() {
        Bundle bundle = new Bundle();
        switch (sendType) {
            case SelectSendTypeActivity.SHOW_CARD_CODE:
            case SelectSendTypeActivity.BORROW_CARD_CODE:
                UIHelper.startActivityForResult(mContext,
                        SelectStoreActivity.class, TAKE_STORE, bundle);
                break;
            case SelectSendTypeActivity.SEND_STATE_CODE:
                UIHelper.startActivityForResult(mContext,
                        SelectLabelActivity.class, TAKE_LABEL, bundle);
                break;
        }
    }


    /**
     * 窗口
     */
    private Dialog dialog;

    private void sendState() {
        RequestParams params = TXApplication.getParams();
        verifyContent();
        String url = "";
        url = addTextDetailToParams(params, url);
        addImageToParams(params);
        TXApplication.post(null, mContext, url, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        send_card_publish_tv.setSelected(false);
                        dialog = UIHelper.createProgressDialog(mContext, "发布中", true);
                        dialog.setCancelable(false);
                        dialog.show();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        send_card_publish_tv.setSelected(true);
                        dialog.dismiss();
                        UIHelper.ToastMessage(mContext, R.string.net_work_error);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        send_card_publish_tv.setSelected(true);
                        dialog.dismiss();
                        MessageResult result = MessageResult
                                .parse(responseInfo.result);
                        if (result == null) {
                            return;
                        }
                        if (1 == result.getSuccess()) {
                            UIHelper.ToastLogMessage(mContext, result.getMessage());
                            sendSuccess();
                        } else {
                            UIHelper.ToastLogMessage(mContext, result.getMessage());
                        }
                    }
                });
    }

    /**
     * 发送成功
     */
    private void sendSuccess() {
        Bundle bundle = new Bundle();
        switch (sendType) {
            case CraftsmandetailsActivity.STAFF_REVIEW:
                Intent data = new Intent();
                UIHelper.setResult(mContext, RESULT_OK, data);
                break;
            case SelectSendTypeActivity.SHOW_CARD_CODE:
//			UIHelper.startActivity(mContext, MainActivity.class,bundle);
//			AppManager.getAppManager().finishActivity(4);
                startActivity();
                break;
            case SelectSendTypeActivity.BORROW_CARD_CODE:
//			UIHelper.startActivity(mContext, MainActivity.class,bundle);
//			AppManager.getAppManager().finishActivity(4);
                startActivity();
                break;
            case SelectSendTypeActivity.SEND_STATE_CODE:
//			AppManager.getAppManager().finishActivity(4);
//			AppManager.getAppManager().finishActivity(MainActivity.class);
//			UIHelper.startActivity(mContext, MainActivity.class,bundle);
//                startActivity();
                Intent mIntent = new Intent(SendCardActivity.SEND_ACTION_NAME);
//                 发送广播
                sendBroadcast(mIntent);
                finish();
                break;
            case StoresDetailsActivity.SHOP_SHOW_CARD:
                finish();
                break;
            case StoresDetailsActivity.SHOP_CENG_CARD:
                finish();
                break;
            case StoresDetailsActivity.SHOP_REVIEW:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 页面跳转
     */
    private void startActivity() {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void verifyContent() {

    }

    private String addTextDetailToParams(RequestParams params, String url) {
        String status_type = "";
        String shop_id = "";
        String uid = "";
        String status_text = send_card_describe_et.getText().toString().trim();
        uid = TXApplication.filename.getString("uid", "");
        if (CraftsmandetailsActivity.STAFF_REVIEW == sendType) {
            url = URLs.APPOINMENT_POST;
            params.addBodyParameter("uid", uid);
            params.addBodyParameter("staff_id", user_id);
            params.addBodyParameter("content", status_text);
        } else {
            String tag_type = "";
            if (null != mShop) {
                shop_id = mShop.getShop_id();
            }
            if (!StringUtils.isEmpty(mLabel)) {
                tag_type = mLabel;
            }
            switch (sendType) {
                case SelectSendTypeActivity.SHOW_CARD_CODE:
                    status_type = SHOW_CARD_STATUS;
                    url = URLs.SEND_SHOW_CARD;
                    break;
                case SelectSendTypeActivity.BORROW_CARD_CODE:
                    status_type = BORROW_CARD_STATUS;
                    url = URLs.SEND_BORROW_CARD;
                    break;
                case SelectSendTypeActivity.SEND_STATE_CODE:
                    status_type = SEND_STATE;
                    url = URLs.SEND_DYNAMIC;
                    break;
                case StoresDetailsActivity.SHOP_SHOW_CARD:
                    shop_id = shopid;
                    status_type = SHOW_CARD_STATUS;
                    url = URLs.SEND_SHOW_CARD;
                    break;
                case StoresDetailsActivity.SHOP_CENG_CARD:
                    shop_id = shopid;
                    status_type = BORROW_CARD_STATUS;
                    url = URLs.SEND_BORROW_CARD;
                    break;
                case StoresDetailsActivity.SHOP_REVIEW:
                    shop_id = shopid;
                    status_type = SEND_REVIEW;
                    url = URLs.SEND_REVIEW;
                    break;
            }
            params.addBodyParameter("status_type", status_type);
            params.addBodyParameter("shoppid", shop_id);
            params.addBodyParameter("uid", uid);
            params.addBodyParameter("status_text", status_text);
            for (int i = 0; i < mLabels.size(); i++) {
                params.addBodyParameter("tag_type[]", mLabels.get(i));
            }
            params.addBodyParameter("address", SPUtils.get(mContext, "city", "").toString());
        }
        return url;
    }

    /**
     * 是否可发布
     */
    private void isPublish() {
        String shop_id = "";
        String status_text = send_card_describe_et.getText().toString().trim();
        String tag_type = "";
        if (null != mShop) {
            shop_id = mShop.getShop_id();
        }
        if (!StringUtils.isEmpty(mLabel)) {
            tag_type = mLabel;
        }
        switch (sendType) {
            case SelectSendTypeActivity.SHOW_CARD_CODE:
                break;
            case SelectSendTypeActivity.BORROW_CARD_CODE:
                break;
            case SelectSendTypeActivity.SEND_STATE_CODE:
                break;
            case StoresDetailsActivity.SHOP_SHOW_CARD:
                shop_id = shopid;
                break;
            case StoresDetailsActivity.SHOP_CENG_CARD:
                shop_id = shopid;
                break;
            case StoresDetailsActivity.SHOP_REVIEW:
                shop_id = shopid;
                break;
        }
        if (TXApplication.login) {
            if (StringUtils.isEmpty(shop_id) && !(sendType == SelectSendTypeActivity.SEND_STATE_CODE)
                    && !(sendType == CraftsmandetailsActivity.STAFF_REVIEW)) {
                UIHelper.showConfirmDialog(mContext, "请选择店铺", null, null, true);
                return;
            } else {
                if (TextUtils.isEmpty(status_text) && Bimp.tempSelectBitmap.size() == 0) {
                    UIHelper.showConfirmDialog(mContext, "文字和图片不可同时为空", null, null, true);
                    return;
                } else {
                    if ((Boolean) SPUtils.get(mContext, "ichange", true)) {
                        sendState();

                    } else {
                        UIHelper.ToastMessage(mContext, "请先完善资料");
                        Bundle bundle = new Bundle();
                        bundle.putInt("from_mode", 1);
                        UIHelper.startActivity(mContext, PerfectDataActivity.class, bundle);
                    }
                }
            }
        } else {
            UIHelper.ToastMessage(mContext, "请先登录");
            Bundle bundle = new Bundle();
            bundle.putInt(LoginActivity.FROM_LOGIN, LoginActivity.FROM_OTHER);
            UIHelper.startActivity(mContext, LoginActivity.class, bundle);
        }
    }

    private void addImageToParams(RequestParams params) {
        Iterator<ImageItem> iterator = Bimp.tempSelectBitmap.iterator();
        int i = Bimp.tempSelectBitmap.size();
        ImageFactory imageFactory = new ImageFactory();
        while (iterator.hasNext()) {
            i--;
            ImageItem imageItem = iterator.next();
            Bitmap bm = imageFactory.ratio(imageItem.getImagePath(), 480f, 800f);
            String newFilePath = FileUtils.saveBitmap(bm, "/TX_PHOTO/" + String.valueOf(System.currentTimeMillis())
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
        UIHelper.startActivity(mContext, AlbumActivity.class);
        overridePendingTransition(R.anim.activity_translate_in,
                R.anim.activity_translate_out);
    }

    String fileName;
    File out;

    private void startPhoto() {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case TAKE_PICTURE:
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) data);
                bundle.putInt("resultCode", resultCode);
                message.setData(bundle);//bundle传值，耗时，效率低
                message.what = 1;//标志是哪个线程传数据
                sendHandle.sendMessage(message);//发送message信息

                break;
            case TAKE_STORE:
                mShop = (ShopItem) data.getSerializableExtra("select_store");
                updataView();
                break;
            case TAKE_LABEL:
                mLabels = data.getStringArrayListExtra("labels");
                updataView();
//                mLabel = "美发";
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        Bimp.tempSelectBitmap.clear();
        Bimp.onceSelectBitmap.clear();
        Bimp.max = 0;
        super.onDestroy();
    }

}
