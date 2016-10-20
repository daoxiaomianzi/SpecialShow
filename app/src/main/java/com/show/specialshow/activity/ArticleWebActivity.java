package com.show.specialshow.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.utils.UIHelper;

public class ArticleWebActivity extends BaseActivity {
    protected WebView content;
    protected String path;
    protected Dialog dialog;
    protected ProgressBar detail_load_progress_sb;

    @Override
    public void initData() {
        path = getIntent().getStringExtra("status_url");
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_base_web);
        content = (WebView) findViewById(R.id.detail_content_wv);
        detail_load_progress_sb = (ProgressBar) findViewById(R.id.detail_load_progress_sb);
        showLoadingDialog("加载中");
    }

    @Override
    public void fillView() {
        head_title_tv.setText("加载中...");
        loadDetail();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadDetail() {

        content.getSettings().setJavaScriptEnabled(true);
        content.getSettings().setBuiltInZoomControls(true);
        content.getSettings().setSupportZoom(true);
//		content.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
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
            public void onProgressChanged(WebView view, int progress) {
                detail_load_progress_sb.setProgress(progress);
                head_title_tv.setText("加载中...");
                if (progress == 100) {
                    hideLoadingDialog();
                    head_title_tv.setText(view.getTitle());
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
        });
        content.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 设置点击网页里面的链接还是在当前的webview里跳转
                view.loadUrl(url);
                return false;
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
                hideLoadingDialog();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoadingDialog();

            }
        });
    }

    private void hideLoadingDialog() {
        if (null != dialog) {
            dialog.dismiss();
        }
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

    }

    private void showLoadingDialog(String info) {
        dialog = UIHelper.createProgressDialog(this, info, true);
        dialog.show();
    }
}
