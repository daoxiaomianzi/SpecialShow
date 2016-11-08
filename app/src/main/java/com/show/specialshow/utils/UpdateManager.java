package com.show.specialshow.utils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.listener.OnConfirmListener;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.view.CustomProgressDialog;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

/**
 *
 */
public class UpdateManager {

    private static final int DOWN_NOSDCARD = 0;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static final int DOWN_FAIL = 3;

    private static final int DIALOG_TYPE_LATEST = 0;
    private static final int DIALOG_TYPE_FAIL = 1;
    // 获取当前版本出错
    private static final int DIALOG_TYPE_LOCALHOS = 2;

    private static UpdateManager updateManager;

    private Context mContext;
    // 下载对话框
    private Dialog downloadDialog;
    // '已经是最新' 或者 '无法获取最新版本' 的对话框
    private Dialog latestOrFailDialog;
    // 进度条
    private FlikerProgressBar mProgress;
    // 显示下载数值
    private TextView mProgressText;
    // 查询动画
    private CustomProgressDialog mProDialog;
    // 进度值
    private int progress;
    // 下载线程
    private Thread downLoadThread;
    // 终止标记
    private boolean interceptFlag;
    // //提示语
    // private String updateMsg = "";
    // 返回的安装包url
    private String apkUrl = URLs.DOWN_LOAD;
    // 下载包保存路径
    private String savePath = "";
    // apk保存完整路径
    private String apkFilePath = "";
    // 临时下载文件路径
    private String tmpFilePath = "";
    // 下载文件大小
    private String apkFileSize;
    // 已下载文件大小
    private String tmpFileSize;

    private String targetVersion;

    // private Update mUpdate;
    TXApplication application;
    private boolean isMust;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    mProgressText.setText(tmpFileSize + "/" + apkFileSize);
                    break;
                case DOWN_OVER:
                    TXApplication.appConfig.edit().putBoolean("isFirst", true)
                            .commit();
                    downloadDialog.dismiss();
                    installApk();
                    break;
                case DOWN_NOSDCARD:
                    downloadDialog.dismiss();
                    Toast.makeText(mContext, "无法下载安装文件，请检查SD卡是否挂载", Toast.LENGTH_LONG).show();
                    break;
                case DOWN_FAIL:
                    downloadDialog.dismiss();
                    Toast.makeText(mContext, "无法下载安装文件，请检查网络或稍后重试", Toast.LENGTH_LONG).show();
                    break;
            }
        }

        ;
    };

    public static UpdateManager getUpdateManager() {
        if (updateManager == null) {
            updateManager = new UpdateManager();
        }
        updateManager.interceptFlag = false;
        return updateManager;
    }

    /**
     * 检查App更新
     *
     * @param context
     * @param isShowMsg 是否显示提示消息
     */
    public void checkAppUpdate(final Context context, final boolean isShowMsg) {
        this.mContext = context;
        application = (TXApplication) mContext.getApplicationContext();
        if (isShowMsg) {
            if (mProDialog == null) {
                mProDialog = new CustomProgressDialog(mContext, "正在检测最新版本...");
                mProDialog.setOnCancelListener(new OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });
                mProDialog.show();
            } else if (mProDialog.isShowing()
                    || (latestOrFailDialog != null && latestOrFailDialog
                    .isShowing())) {
                return;
            }
        }

        String url = URLs.GET_CURRENT_VERSION;
        RequestParams params = TXApplication.getParams();
        TXApplication.post(null, context, url, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            // 进度条对话框不显示 - 检测结果也不显示
                            if (mProDialog != null && !mProDialog.isShowing()) {
                                return;
                            }
                            // 关闭并释放释放进度条对话框
                            if (isShowMsg && mProDialog != null) {
                                mProDialog.dismiss();
                                mProDialog = null;
                            }
                            MessageResult result = MessageResult
                                    .parse(responseInfo.result);

                            targetVersion = result.getData();
                            String my_version_name = getCurrentVersion().versionName;
                            String[] verify = targetVersion.split("\\.");
                            String[] my_verify = my_version_name.split("\\.");

                            if (targetVersion.equals(my_version_name)) {
                                return;
                            }
                            int verify_int = Integer.valueOf(verify[0]) * 100
                                    + Integer.valueOf(verify[1]) * 10
                                    + Integer.valueOf(verify[2]);
                            int my_verify_int = Integer.valueOf(my_verify[0])
                                    * 100 + Integer.valueOf(my_verify[1]) * 10
                                    + Integer.valueOf(my_verify[2]);
                            if (my_verify_int > verify_int) {
                                return;
                            }
                            int verify_code_one = Integer.valueOf(verify[0]);
                            int my_verify_code_one = Integer.valueOf(my_verify[0]);
                            int verify_code = Integer.valueOf(verify[1]);
                            int my_verify_code = Integer.valueOf(my_verify[1]);
                            if (verify_code_one > my_verify_code_one || verify_code > my_verify_code) {
                                // 必须要更新
                                isMust = true;
                            } else {
                                // 可不更新
                                isMust = false;
                            }
                            showNoticeDialog();
                            // if (TextUtils.isEmpty(result.getData())) {
                            // if (isShowMsg){
                            // showLatestOrFailDialog(DIALOG_TYPE_LATEST);
                            // }
                            // return;
                            // }
                            // mUpdate = Update.parse(result.getData());
                            // PackageInfo pi=getCurrentVersion();
                            // if(pi==null){
                            // showLatestOrFailDialog(DIALOG_TYPE_LOCALHOS);
                            // return;
                            // }
                            // // 判断本地使用版本和服务器版本
                            // if(pi.versionCode==mUpdate.getVersion()){
                            // showLatestOrFailDialog(DIALOG_TYPE_LATEST);
                            // return;
                            // }
                            // apkUrl = mUpdate.getFilePath();
                            // showNoticeDialog();
                        } catch (Exception e) {
                            UIHelper.ToastMessage(mContext, e.getMessage());
                            showLatestOrFailDialog(DIALOG_TYPE_FAIL);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        // showLatestOrFailDialog(DIALOG_TYPE_FAIL);
                    }
                });
    }

    /**
     * 显示'已经是最新'或者'无法获取版本信息'对话框
     */
    private void showLatestOrFailDialog(int dialogType) {
        if (latestOrFailDialog != null) {
            // 关闭并释放之前的对话框
            latestOrFailDialog.dismiss();
            latestOrFailDialog = null;
        }
        Builder builder = new Builder(mContext);
        builder.setTitle("系统提示");
        if (dialogType == DIALOG_TYPE_LATEST) {
            builder.setMessage("您当前已是最新版本!");
        } else if (dialogType == DIALOG_TYPE_FAIL) {
            builder.setMessage("无法获取版本更新信息");
        } else if (dialogType == DIALOG_TYPE_LOCALHOS) {
            builder.setMessage("无法获取用户当前使用版本信息");
        }
        if (mProDialog != null) {
            mProDialog.dismiss();
        }
        builder.setPositiveButton("确定", null);
        latestOrFailDialog = builder.create();
        latestOrFailDialog.show();
    }

    /**
     * 获取当前客户端版本信息
     */
    private PackageInfo getCurrentVersion() {
        try {
            PackageInfo pack = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0);
            return pack;
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    /**
     * 显示版本更新通知对话框
     */
    private void showNoticeDialog() {
        UIHelper.showConfirmDialog(mContext, isMust ? "发现重要更新，继续使用请更新"
                : "发现新版本,是否更新?", new OnConfirmListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm)
                    showDownloadDialog();
                else {
                    if (isMust) {
                        ((Activity) mContext).finish();
                    }
                }
            }
        }, null, isMust);
    }

    /**
     * 显示下载对话框
     */
    private void showDownloadDialog() {
        downloadDialog = new Dialog(mContext, R.style.Theme_dialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.view_update_progress, null);
        TextView cancel = (TextView) view
                .findViewById(R.id.waybill_dialog_cancel_tv);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                downloadDialog.dismiss();
                interceptFlag = true;
                if (isMust) {
                    ((Activity) mContext).finish();
                }
            }
        });
        downloadDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadDialog.dismiss();
                interceptFlag = true;
            }
        });
        mProgress = (FlikerProgressBar) view.findViewById(R.id.update_progress);
        mProgressText = (TextView) view.findViewById(R.id.update_progress_text);
        downloadDialog.setContentView(view);
        downloadDialog.setCancelable(false);
        downloadDialog.show();

        downloadApk();
    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                String apkName = "gl_" + "特秀美妆" + targetVersion + ".apk";
                String tmpApk = "gl_" + "特秀美妆" + targetVersion + ".tmp";
                // 判断是否挂载了SD卡

                if (Environment.MEDIA_MOUNTED.equals(Environment
                        .getExternalStorageState())) {
                    savePath = Environment.getExternalStorageDirectory()
                            .getAbsolutePath()
                            + File.separator
                            + "SpecialShow"
                            + File.separator + "apk" + File.separator;
                    System.out.println(savePath);
                    File file = new File(savePath);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    apkFilePath = savePath + apkName;
                    tmpFilePath = savePath + tmpApk;
                }

                // 没有挂载SD卡，无法下载文件
                if (apkFilePath == null || apkFilePath == "") {
                    mHandler.sendEmptyMessage(DOWN_NOSDCARD);
                    return;
                }

                File ApkFile = new File(apkFilePath);

                // 是否已下载更新文件
                if (ApkFile.exists()) {
                    downloadDialog.dismiss();
                    installApk();
                    return;
                }

                // 输出临时下载文件
                File tmpFile = new File(tmpFilePath);
                FileOutputStream fos = new FileOutputStream(tmpFile);

                URL url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                // 显示文件大小格式：2个小数点显示
                DecimalFormat df = new DecimalFormat("0.00");
                // 进度条下面显示的总文件大小
                apkFileSize = df.format((float) length / 1024 / 1024) + "MB";

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    // 进度条下面显示的当前下载文件大小
                    tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
                    // 当前进度值
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成 - 将临时下载文件转成APK文件
                        if (tmpFile.renameTo(ApkFile)) {
                            // 通知安装
                            mHandler.sendEmptyMessage(DOWN_OVER);
                        }
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消就停止下载

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                UIHelper.ToastMessage(mContext, e.getMessage());
                mHandler.sendEmptyMessage(DOWN_FAIL);
            } catch (IOException e) {
                e.printStackTrace();
                UIHelper.ToastMessage(mContext, e.getMessage());
                mHandler.sendEmptyMessage(DOWN_FAIL);
            }
        }
    };

    /**
     * 下载apk
     *
     * @param ／／url
     */
    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 安装apk
     *
     * @param ／／url
     */
    private void installApk() {
        File apkfile = new File(apkFilePath);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
    }

    public String getChannelNumber(Context context) {
        String channel = "";
        try {
            ApplicationInfo appInfo = context
                    .getApplicationContext()
                    .getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            channel = appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return channel;
    }

}