package com.show.specialshow.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.listener.OnConfirmListener;

@SuppressLint("SimpleDateFormat")
public class UIHelper {
    private static Toast mToast;

    /**
     * 弹出Toast消息
     */
    public static void ToastMessage(Context cont, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(cont, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void ToastMessage(Context cont, int msg) {
        if (mToast == null) {
            mToast = Toast.makeText(cont, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void ToastMessage(Context cont, String msg, int time) {
        Toast.makeText(cont, msg, time).show();
    }

    public static void ToastLogMessage(Context cont, String msg) {
        if (TXApplication.isShowToast) {
            Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 在文字的左边
     *
     * @param resources
     * @param context
     * @param textView
     */
    public static void leftDrawable(int resources, Context context, TextView textView) {
        Drawable leftDrawable = context.getResources().getDrawable(resources);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(),
                leftDrawable.getMinimumHeight());
        textView.setCompoundDrawables(leftDrawable, null, null, null);
    }

    /**
     * 获得系统的最新时间
     *
     * @return
     */
    public static String getLastUpdateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(System.currentTimeMillis());
    }

    /**
     * 正常启动Activity
     */
    public static void startActivity(Activity activity,
                                     Class<? extends Activity> clazz) {
        Intent intent = new Intent(activity, clazz);
        activity.startActivity(intent);
    }

    /**
     * 携带数据启动Activity
     */
    public static void startActivity(Activity activity,
                                     Class<? extends Activity> clazz, Bundle bundle) {
        Intent intent = new Intent(activity, clazz);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    /**
     * 启动Activity，获取数据
     */
    public static void startActivityForResult(Activity activity,
                                              Class<? extends Activity> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(activity, clazz);
        if (bundle != null)
            intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 启动Fragment，获取数据
     */
    public static void startActivityForResult(Fragment fragment,
                                              Class<? extends Activity> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(fragment.getActivity(), clazz);
        if (bundle != null)
            intent.putExtras(bundle);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 调用系统电话功能
     *
     * @param context
     * @param //phonenumber
     */
    public static void showTel(Activity context, String phone) {
        Uri uri = Uri.parse("tel:" + phone);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(intent);
    }

    public static void setResult(Activity activity, int resultCode, Intent data) {
        activity.setResult(resultCode, data);
        activity.finish();
    }

    public static void scrollToTop(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 得到自定义的progressDialog
     */
    public static Dialog createProgressDialog(Context context, String msg, boolean isCancel) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.progress_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.progress_dialog);// 创建自定义样式dialog
        loadingDialog.setCancelable(isCancel);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
        return loadingDialog;
    }

    /**
     * 确认对话框
     */
    public static void showConfirmDialog(final Context context,
                                         CharSequence msg, final OnConfirmListener onConfirmListener,
                                         OnCancelListener listener, final boolean isMust) {
        final Dialog dialog = new Dialog(context, R.style.Theme_dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_contest_dialog, null);
        TextView content = (TextView) view
                .findViewById(R.id.confirm_dialog_content_tv);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int) (TXApplication.WINDOW_WIDTH * 0.7),
                LayoutParams.WRAP_CONTENT);
        content.setLayoutParams(params);
        content.setText(msg);
        if (listener != null)
            dialog.setOnCancelListener(listener);
        TextView confirm = (TextView) view
                .findViewById(R.id.contest_confirm_tv);
        TextView cancel = (TextView) view.findViewById(R.id.contest_cancel_tv);
        ImageView image = (ImageView) view.findViewById(R.id.split_iv_vertical);
        if (isMust) {
            cancel.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
        }
        confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
                if (onConfirmListener != null)
                    onConfirmListener.onClick(dialog, true);
            }
        });
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
                if (onConfirmListener != null)
                    onConfirmListener.onClick(dialog, false);
            }
        });
        dialog.setContentView(view);
        dialog.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getRepeatCount() == 0) {
                    if (isMust) {
                        ((Activity) context).finish();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * popwindow框
     */
    public static PopupWindow showPopwindow(View popView, Context context, View parentView) {
        PopupWindow pw = new PopupWindow(popView,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        pw.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(
                R.color.white));
        pw.setBackgroundDrawable(dw);
        pw.setInputMethodMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pw.setOutsideTouchable(true);
        // 设置popWindow的显示和消失动画
        pw.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        pw.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
        return pw;
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 输入法软键盘的操作
     */
    public static void isVisable(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    /**
     * 输入支付密码确定框
     */
    public static Dialog showPayPasswordDialog(Context context, View view) {
        Dialog dialog = new Dialog(context, R.style.Theme_dialog);

        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width = (int) (0.7 * TXApplication.WINDOW_WIDTH);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        dialogWindow.getDecorView().setPadding(0,0,0,0);
        dialogWindow.setAttributes(lp);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }
}
