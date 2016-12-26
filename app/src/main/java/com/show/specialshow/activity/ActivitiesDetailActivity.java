package com.show.specialshow.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.TeShowActivitiesMess;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.ShareServiceFactory;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.view.SignUpDialog;
import com.umeng.comm.core.beans.ShareContent;

import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

public class ActivitiesDetailActivity extends BaseActivity {
    //相关控件
    private WebView content;
    private TextView tv_activities_detail_sign_up;
    @ViewInject(R.id.iv_item_activities_img)
    private ImageView iv_activities_detail_img;
    @ViewInject(R.id.tv_item_activities_progress)
    TextView tv_detail_activities_progress;//活动状态
    @ViewInject(R.id.tv_item_activities_title)
    TextView tv_detail_activities_title;//标题
    @ViewInject(R.id.tv_item_activities_slogan)
    TextView tv_detail_activities_slogan;//副标题
    @ViewInject(R.id.tv_item_activities_is_free)
    TextView tv_detail_activities_is_free;//收费价格
    @ViewInject(R.id.tv_item_activities_time)
    TextView tv_detail_activities_time;//时间
    @ViewInject(R.id.tv_item_activities_address)
    TextView tv_detail_activities_address;//地点
    @ViewInject(R.id.tv_item_activities_excerpt)
    TextView tv_detail_activities_excerpt;//活动简介
    private EditText et_name;
    private EditText et_iphone;
    private View view;
    private SignUpDialog signUpDialog;

    //数据
    private String html;
    private TeShowActivitiesMess activitiesMess;//活动信息
    private boolean isApply = false;
    public static final String APPLY_ACTION_NAME = "发送广播到特秀活动页";


    @Override
    public void initData() {
        activitiesMess = (TeShowActivitiesMess) getIntent().getSerializableExtra("activities_data");
        if (null != activitiesMess) {
            html = activitiesMess.getPost_content();
        }
        setContentView(R.layout.activity_activities_detail);
        ViewUtils.inject(mContext);
    }

    @Override
    public void initView() {
        content = (WebView)
                findViewById(R.id.wv_activities_details_content);
        tv_activities_detail_sign_up = (TextView)
                findViewById(R.id.tv_activities_detail_sign_up);

    }

    @Override
    public void fillView() {
        head_title_tv.setText("详情");
        head_right_tv.setVisibility(View.VISIBLE);
        Drawable rightDrawable = getResources()
                .getDrawable(R.drawable.icon_share);
        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
                rightDrawable.getMinimumHeight());
        head_right_tv.setCompoundDrawables(null, null, rightDrawable, null);
        if (activitiesMess != null) {
            if (1 == activitiesMess.getIsEnter()) {
                tv_activities_detail_sign_up.setEnabled(false);
                tv_activities_detail_sign_up.setBackgroundResource(R.color.gray);
                tv_activities_detail_sign_up.setText("已报名");
            }
            if (activitiesMess.isPost_isprogress()) {
                tv_detail_activities_progress.setText("正在进行");
            } else {
                tv_activities_detail_sign_up.setVisibility(View.GONE);
                tv_detail_activities_progress.setText("已结束");
                tv_detail_activities_progress.setBackgroundResource(R.color.gray);
            }
            ImageLoader.getInstance().displayImage(activitiesMess.getPost_smeta(), iv_activities_detail_img);
            tv_detail_activities_title.setText(activitiesMess.getPost_title());
            tv_detail_activities_slogan.setText(activitiesMess.getPost_slogan());
            if (0 == activitiesMess.getPost_expense()) {
                tv_detail_activities_is_free.setText("免费");
            } else {
                tv_detail_activities_is_free.setText(activitiesMess.getPost_expense() + "元");
            }
            tv_detail_activities_time.setText(MessageFormat.format("时间: {0}", activitiesMess.getPost_active_time()));
            tv_detail_activities_address.setText(MessageFormat.format("地址: {0}", activitiesMess.getPost_place()));
            tv_detail_activities_excerpt.setText(activitiesMess.getPost_excerpt());

        }
        loadNativeDetail();
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {
        if (!BtnUtils.getInstance().isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_activities_detail_sign_up://报名
                if (TXApplication.login) {
                    signUpDialog = new SignUpDialog(mContext);
                    view = signUpDialog.signUpDialog();
                } else {
                    UIHelper.ToastMessage(mContext, "请先登录");
                    Bundle bundle = new Bundle();
                    bundle.putInt(LoginActivity.FROM_LOGIN, LoginActivity.FROM_OTHER);
                    UIHelper.startActivity(mContext, LoginActivity.class, bundle);
                }

                break;
            case R.id.apply_sign_up:
                et_name = (EditText) view.findViewById(R.id.et_name);
                et_iphone = (EditText) view.findViewById(R.id.et_iphone);
                if (StringUtils.isEmpty(et_name.getText().toString().trim())) {
                    UIHelper.ToastMessage(mContext, "请填写姓名");
                } else {
                    if (StringUtils.isEmpty(et_iphone.getText().toString().toString())) {
                        UIHelper.ToastMessage(mContext, "请填写手机号");
                    } else {
                        applyActivities();
                    }
                }
                break;
            case R.id.head_right_tv:
                share();
                break;
            default:
                break;
        }
    }

    /**
     * 分享回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShareServiceFactory.getShareService().onActivityResult(mContext, requestCode, resultCode, data);
    }

    /**
     * 分享
     */
    private void share() {
        ShareContent shareItem = new ShareContent();
        shareItem.mText = activitiesMess.getPost_excerpt();
        shareItem.mTargetUrl = activitiesMess.getPost_share_url();
        shareItem.mTitle = activitiesMess.getPost_title();
        ShareServiceFactory.getShareService().share(this, shareItem);
    }

    @Override
    public void goBack(View v) {
        if (isApply) {
            Intent intent = new Intent(APPLY_ACTION_NAME);
            sendBroadcast(intent);
        }
        super.goBack(v);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isApply) {
            Intent intent = new Intent(APPLY_ACTION_NAME);
            sendBroadcast(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 提交活动报名
     */
    private void applyActivities() {
        RequestParams params = TXApplication.getParams();
        String url = URLs.POSTS_ENTER;
        params.addBodyParameter("uid", (String) SPUtils.get(mContext, "uid", ""));
        params.addBodyParameter("object_id", activitiesMess.getPost_id() + "");
        params.addBodyParameter("name", et_name.getText().toString().trim());
        params.addBodyParameter("phone", et_iphone.getText().toString().trim());
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                loadIng("报名中...", true);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                MessageResult result = MessageResult.parse(responseInfo.result);
                if (null == result) {
                    return;
                }
                if (1 == result.getSuccess()) {
                    if (null != signUpDialog && null != signUpDialog.getDialog()) {
                        signUpDialog.getDialog().dismiss();
                    }
                    isApply = true;
                    UIHelper.ToastMessage(mContext, result.getMessage());
                    tv_activities_detail_sign_up.setText("已报名");
                    tv_activities_detail_sign_up.setBackgroundResource(R.color.gray);
                    tv_activities_detail_sign_up.setEnabled(false);
                } else {
                    UIHelper.ToastMessage(mContext, result.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                UIHelper.ToastMessage(mContext, R.string.net_work_error);
            }
        });

    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void loadNativeDetail() {
        content.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        content.getSettings().setJavaScriptEnabled(true);
        content.setWebChromeClient(new WebChromeClient());
        content.addJavascriptInterface(mContext, "app");
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int scale = dm.densityDpi;
        if (scale == 240) { //
            content.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (scale == 160) {
            content.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (scale == 120) {
            content.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }

        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        if (width > 650) {
            content.setInitialScale(190);
        } else if (width > 520) {
            content.setInitialScale(160);
        } else if (width > 450) {
            content.setInitialScale(140);
        } else if (width > 300) {
            content.setInitialScale(120);
        } else {
            content.setInitialScale(100);
        }

        content.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        content.setHorizontalScrollBarEnabled(false);
        content.getSettings().setDomStorageEnabled(true);
//        content.getSettings().setUseWideViewPort(true);
//        content.getSettings().setLoadWithOverviewMode(true);
        content.getSettings().setSupportZoom(false);
        content.getSettings().setBuiltInZoomControls(false);
        content.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // content.setInitialScale(150);
        content.setHorizontalScrollbarOverlay(true);

        content.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

                if (progress == 100) {
//                    hideLoadingDialog();
//                    detail_load_progress_sb.setVisibility(View.GONE);
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                // TODO Auto-generated method stub
                return super.onJsAlert(view, url, message, result);
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
//                hideLoadingDialog();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                hideLoadingDialog();

            }
        });
    }
}
