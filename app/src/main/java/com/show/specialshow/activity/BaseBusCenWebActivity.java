package com.show.specialshow.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.utils.UIHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BaseBusCenWebActivity extends BaseActivity {
    private final static int FILECHOOSER_RESULTCODE = 1;// 表单的结果回调</span>
    private Uri imageUri;
    protected WebView content;
    private RelativeLayout web_all;
    protected String path;
    protected Dialog dialog;
    protected ProgressBar detail_load_progress_sb;
    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    protected Handler mHandler = new Handler();
    protected JsResult jsResult;
    private boolean isDoubleShow = false;
    // private ValueCallback<Uri> mFilePathCallback;
    // private ValueCallback<Uri[]> mFilePathCallbackArray;
    // private ImagePick ip;
    // private static final int PHOTO_CARMERA = 1;
    // private static final int PHOTO_PICK = 2;
    // private static final int PHOTO_CUT = 3;

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_base_web_bus_center);
        content = (WebView) findViewById(R.id.detail_content_wv);
        web_all = (RelativeLayout) findViewById(R.id.web_all);
        detail_load_progress_sb = (ProgressBar) findViewById(R.id.detail_load_progress_sb);
        showLoadingDialog("加载中");
    }

    @Override
    public void fillView() {

    }

    @Override
    public void setListener() {

    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    protected void loadDetail() {

        content.getSettings().setJavaScriptEnabled(true);
        content.getSettings().setBuiltInZoomControls(true);
        content.getSettings().setSupportZoom(true);
        // content.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        content.getSettings().setLoadWithOverviewMode(true);
        content.getSettings().setUseWideViewPort(true);
        content.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        content.loadUrl(path);
        content.addJavascriptInterface(mContext, "app");
        // content.addJavascriptInterface(new DemoJavaScriptInterface(), "app");
        content.getSettings().setSaveFormData(false);

        // 清除缓存
        content.clearCache(true);
        // 清除历史记录
        content.clearHistory();
        content.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                take();
                return true;
            }

            // // file upload callback (Android 2.2 (API level 8) -- Android 2.3
            // (API level 10)) (hidden method)
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                take();
            }

            // // file upload callback (Android 3.0 (API level 11) -- Android
            // 4.0 (API level 15)) (hidden method)
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType) {
                mUploadMessage = uploadMsg;
                take();
            }

            // // file upload callback (Android 4.1 (API level 16) -- Android
            // 4.3 (API level 18)) (hidden method)
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                take();
            }

            // public void openFileChooser(ValueCallback<Uri> filePathCallback)
            // {
            // handle(filePathCallback);
            // }
            //
            // public void openFileChooser(ValueCallback filePathCallback,
            // String acceptType) {
            // handle(filePathCallback);
            // }
            //
            // public void openFileChooser(ValueCallback<Uri> filePathCallback,
            // String acceptType, String capture) {
            // handle(filePathCallback);
            // }

            // for Lollipop
            // @Override
            // public boolean onShowFileChooser(WebView webView,
            // ValueCallback<Uri[]> filePathCallback, FileChooserParams
            // fileChooserParams) {
            // // Double check that we don't have any existing callbacks
            // if (mFilePathCallbackArray != null) {
            // mFilePathCallbackArray.onReceiveValue(null);
            // }
            // mFilePathCallbackArray = filePathCallback;
            // showSelectDialog(PHOTO_PICK,PHOTO_CARMERA);
            // return true;
            // }

            /**
             * // * 处理5.0以下系统回调 // * @param filePathCallback //
             */
            // private void handle(ValueCallback<Uri> filePathCallback) {
            // if (filePathCallback != null) {
            // mFilePathCallback.onReceiveValue(null);
            // }
            // mFilePathCallback = filePathCallback;
            // showSelectDialog(PHOTO_PICK,PHOTO_CARMERA);
            // }
            public void onProgressChanged(WebView view, int progress) {
                detail_load_progress_sb.setVisibility(View.VISIBLE);

                detail_load_progress_sb.setProgress(progress);

                if (progress == 100) {
                    head_title_tv.setText(view.getTitle());
                    hideLoadingDialog();
                    detail_load_progress_sb.setVisibility(View.GONE);
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
//				return super.onJsAlert(view, url, message, result);
                createAffirmDialog(message, DIALOG_SINGLE_STPE, true);
                result.confirm();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                jsResult = result;
                isDoubleShow = true;
                createAffirmDialog(message, DIALOG_DOUBLE_STPE, true);
                return true;
            }
        });
        content.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 设置点击网页里面的链接还是在当前的webview里跳转
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, android.net.http.SslError error) {
                // 设置webview处理https请求
                handler.proceed();
            }

            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // 加载页面报错时的处理
                // isLoadFull = false;
                super.onReceivedError(view, errorCode, description, failingUrl);
                UIHelper.ToastMessage(mContext, R.string.net_work_error);
                web_all.setVisibility(View.VISIBLE);
                content.setVisibility(View.GONE);
                hideLoadingDialog();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoadingDialog();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL)
                return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data
                    .getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                Log.e("result", result + "");
                if (result == null) {
                    // mUploadMessage.onReceiveValue(imageUri);
                    mUploadMessage.onReceiveValue(imageUri);
                    mUploadMessage = null;

                    Log.e("imageUri", imageUri + "");
                } else {
                    mUploadMessage.onReceiveValue(result);
                    mUploadMessage = null;
                }

            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode,
                                        Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE
                || mUploadCallbackAboveL == null) {
            return;
        }

        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();

                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        if (results != null) {
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        } else {
            results = new Uri[]{imageUri};
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }

        return;
    }

    // /**
    // * 处理WebView的回调
    // * @param uri
    // */
    // private void handleCallback(Uri uri) {
    // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    // if (mFilePathCallbackArray != null) {
    // if (uri != null) {
    // mFilePathCallbackArray.onReceiveValue(new Uri[]{uri});
    // } else {
    // mFilePathCallbackArray.onReceiveValue(null);
    // }
    // mFilePathCallbackArray = null;
    // }
    // } else {
    // if (mFilePathCallback != null) {
    // if (uri != null) {
    // mFilePathCallback.onReceiveValue(uri);
    // } else {
    // mFilePathCallback.onReceiveValue(null);
    // }
    // mFilePathCallback = null;
    // }
    // }
    // }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contest_confirm_tv:
                if (isDoubleShow) {
                    jsResult.confirm();
                }
                affirmDialog.dismiss();
                break;
            case R.id.contest_cancel_tv:
                if (isDoubleShow) {
                    jsResult.cancel();
                }
                affirmDialog.dismiss();
                break;

            default:
                break;
        }
    }

    protected void hideLoadingDialog() {
        dialog.dismiss();
    }

    private void showLoadingDialog(String info) {
        dialog = UIHelper.createProgressDialog(this, info, true);
        dialog.show();
    }

    private void take() {
        File imageStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyApp");
        // Create the storage directory if it does not exist
        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }
        File file = new File(imageStorageDir + File.separator + "IMG_"
                + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageUri = Uri.fromFile(file);

        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(
                captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName,
                    res.activityInfo.name));
            i.setPackage(packageName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntents.add(i);

        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i, "图片选择");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                cameraIntents.toArray(new Parcelable[]{}));
        mContext.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && content.canGoBack()) {
            content.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void goBack(View v) {
        switch (v.getId()) {
            case R.id.head_left_tv:
                if (content.canGoBack()) {
                    content.goBack();
                } else {
                    mContext.finish();
                }
                break;
            case R.id.head_close_tv:
                mContext.finish();
                break;

            default:
                break;
        }
    }
}
