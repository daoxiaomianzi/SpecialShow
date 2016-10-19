package com.show.specialshow;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.show.specialshow.utils.UIHelper;


@SuppressLint("JavascriptInterface")
public class BaseWebActivity extends BaseActivity {
	protected WebView content;
	protected String path;
	protected Dialog dialog;
	protected ProgressBar detail_load_progress_sb;
	protected String html;
	private Handler mHandler = new Handler();

	@Override
	public void initData() {
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_base_web);
		content = (WebView) findViewById(R.id.detail_content_wv);
		detail_load_progress_sb = (ProgressBar) findViewById(R.id.detail_load_progress_sb);
		showLoadingDialog("加载中");
	}

	@Override
	public void setListener() {

	}

	@Override
	public void fillView() {
	}

	@SuppressLint("SetJavaScriptEnabled")
	protected void loadDetail() {

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

				if (progress == 100) {
					hideLoadingDialog();
					detail_load_progress_sb.setVisibility(View.GONE);
				}
			}

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				//				return super.onJsAlert(view, url, message, result);
				createAffirmDialog(message, DIALOG_SINGLE_STPE,true);
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

	protected void hideLoadingDialog() {
		dialog.dismiss();
	}

	private void showLoadingDialog(String info) {
		dialog = UIHelper.createProgressDialog(this, info,true);
		dialog.show();
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
			content.getSettings().setDefaultZoom(ZoomDensity.FAR);
		} else if (scale == 160) {
			content.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
		} else {
			content.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
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
		content.getSettings().setSupportZoom(false);
		content.getSettings().setBuiltInZoomControls(false);
		content.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// content.setInitialScale(150);
		content.setHorizontalScrollbarOverlay(true);

		content.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				detail_load_progress_sb.setProgress(progress);

				if (progress == 100) {
					hideLoadingDialog();
					detail_load_progress_sb.setVisibility(View.GONE);
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
				hideLoadingDialog();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				hideLoadingDialog();

			}
		});
	}

	// protected void initShareData();
	final class DemoJavaScriptInterface {

		DemoJavaScriptInterface() {
		}

		@JavascriptInterface
		public void commiteUserInfo() {

			mHandler.post(new Runnable() {
				public void run() {
					UIHelper.ToastLogMessage(mContext, "JS控制本地操作");
				}
			});

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
