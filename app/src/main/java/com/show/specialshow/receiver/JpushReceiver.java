package com.show.specialshow.receiver;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.activity.AppStartActivity;
import com.show.specialshow.activity.JpushWebView;
import com.show.specialshow.activity.MyBookingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JpushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
//            if (isForeground(context)) {
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);//获取附加字段,是一个json数组
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            try {
                JSONObject json = new JSONObject(extras);
                //进行json.getString("Article")操作时,要保证这里的Article与服务器上的Article一模一样,不然回报空指针异常
                String articleUrl = json.getString("url");//获取附加字段Article,对应的值为articleUrl
                int status_code = json.getInt("status_code");
                switch (status_code) {
                    case 442:
                        if (isForeground(context) && TXApplication.login) {
                            Intent intent_WebView = new Intent(context, MyBookingActivity.class);
                            //必须要写,不然出错,因为这是一个从非activity的类跳转到一个activity,需要一个flag来说明,这个flag就是Intent.FLAG_ACTIVITY_NEW_TASK
                            intent_WebView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent_WebView);//打开WebViewActivity
                        } else {
                            Intent intent_WebView = new Intent(context, AppStartActivity.class);
                            //必须要写,不然出错,因为这是一个从非activity的类跳转到一个activity,需要一个flag来说明,这个flag就是Intent.FLAG_ACTIVITY_NEW_TASK
                            intent_WebView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Bundle bundle_WebView = new Bundle();
                            bundle_WebView.putInt("jpushFlag", 2);
                            intent_WebView.putExtras(bundle_WebView);
                            context.startActivity(intent_WebView);//打开WebViewActivity
                        }
                        break;
                    case 441:
                    case 443:
                    default:
                        /**
                         * 点击通知栏通知, 打开网页地址
                         * 把articleUrl通过startActivity发送给WebViewActivity
                         */
                        if (isForeground(context)) {
                            Intent intent_WebView = new Intent(context, JpushWebView.class);
                            //必须要写,不然出错,因为这是一个从非activity的类跳转到一个activity,需要一个flag来说明,这个flag就是Intent.FLAG_ACTIVITY_NEW_TASK
                            intent_WebView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Bundle bundle_WebView = new Bundle();
                            bundle_WebView.putString("url", articleUrl);//url
                            intent_WebView.putExtras(bundle_WebView);
                            context.startActivity(intent_WebView);//打开WebViewActivity
                        } else {
                            Intent intent_WebView = new Intent(context, AppStartActivity.class);
                            //必须要写,不然出错,因为这是一个从非activity的类跳转到一个activity,需要一个flag来说明,这个flag就是Intent.FLAG_ACTIVITY_NEW_TASK
                            intent_WebView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Bundle bundle_WebView = new Bundle();
                            bundle_WebView.putString("url", articleUrl);//url
                            bundle_WebView.putInt("jpushFlag", 1);
                            intent_WebView.putExtras(bundle_WebView);
                            context.startActivity(intent_WebView);//打开WebViewActivity
                        }
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
                if (isForeground(context)) {
                    showUpdateSuccessDialog(context, content);
                } else {
                    startApp(context, AppStartActivity.class, content);
                }
            }

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    private void startApp(Context context, Class clazz, String content) {
        Intent intent_WebView = new Intent(context, clazz);
        //必须要写,不然出错,因为这是一个从非activity的类跳转到一个activity,需要一个flag来说明,这个flag就是Intent.FLAG_ACTIVITY_NEW_TASK
        intent_WebView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle_WebView = new Bundle();
        bundle_WebView.putString("content", content);//内容
        intent_WebView.putExtras(bundle_WebView);
        context.startActivity(intent_WebView);//打开WebViewActivity
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    public boolean isAction(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isForeground(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i("前台", appProcess.processName);
                    return true;
                } else {
                    Log.i("后台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    private void showUpdateSuccessDialog(Context context, String content) {
        final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        WindowManager.LayoutParams para = new WindowManager.LayoutParams();
        para.height = -2;//WRAP_CONTENT
        para.width = -2;//WRAP_CONTENT
        para.format = 1;

        para.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        para.gravity = Gravity.CENTER;

        para.type = WindowManager.LayoutParams.TYPE_TOAST;
        final View contentView = LayoutInflater.from(context).inflate(R.layout.view_contest_dialog, null);
        View content_ll = contentView.findViewById(R.id.content_ll);
        TextView contest_cancel_tv = (TextView) contentView
                .findViewById(R.id.contest_cancel_tv);
        TextView confirm_dialog_tips = (TextView) contentView.findViewById(R.id.confirm_dialog_tips);
        ImageView split_iv_vertical = (ImageView) contentView
                .findViewById(R.id.split_iv_vertical);
        TextView contest_confirm_tv = (TextView) contentView
                .findViewById(R.id.contest_confirm_tv);

        TextView confirm_dialog_content_tv = (TextView) contentView
                .findViewById(R.id.confirm_dialog_content_tv);
        contest_cancel_tv.setVisibility(View.GONE);
        split_iv_vertical.setVisibility(View.GONE);
        confirm_dialog_tips.setText("推送内容");
        confirm_dialog_content_tv.setText(content);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int) (TXApplication.WINDOW_WIDTH * 0.7),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        content_ll.setLayoutParams(params);
        confirm_dialog_content_tv.setLayoutParams(params);
        contest_confirm_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wm.removeView(contentView);
            }
        });
        wm.addView(contentView, para);
    }

    /**
     * 实现自定义推送声音
     *
     * @param context
     * @param bundle
     */

    private void processCustomMessage(Context context, Bundle bundle) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);


        NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        notification.setAutoCancel(true)
                .setContentText(message)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher);
        Notification notify = notification.build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL; // 点击通知后通知栏消失
        // 通知id需要唯一，要不然会覆盖前一条通知
        int notifyId = (int) System.currentTimeMillis();
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        if (!TextUtils.isEmpty(extras)) {
            try {
                JSONObject extraJson = new JSONObject(extras);
                if (null != extraJson && extraJson.length() > 0) {

                    String sound = extraJson.getString("sound");
                    int status_code = extraJson.getInt("status_code");
                    switch (status_code) {
                        case 441:
                            if ("neworder.mp3".equals(sound)) {
                                notification.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.neworder));
                            }
                            break;
                        case 442:
                            if ("makesuccess.mp3".equals(sound)) {
                                notification.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.makesuccess));
                            }
                            break;
                        case 443:
                            if ("paysuccess.mp3".equals(sound)) {
                                notification.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.paysuccess));
                            }
                            break;
                        case 444:
                            if ("cancelorder.mp3".equals(sound)) {
                                notification.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.cancelorder));
                            }
                            break;
                        default:
                            break;
                    }
                    Intent clickIntent = new Intent(context, JpushReceiver.class); //点击通知之后要发送的广播
                    clickIntent.setAction(JPushInterface.ACTION_NOTIFICATION_OPENED);
                    clickIntent.putExtras(bundle);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notifyId, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    notification.setContentIntent(pendingIntent);


                }
            } catch (JSONException e) {

            }

        } else {
            if (isForeground(context)) {
                showUpdateSuccessDialog(context, message);
            } else {
                Intent intent_WebView = new Intent(context, AppStartActivity.class);
                //必须要写,不然出错,因为这是一个从非activity的类跳转到一个activity,需要一个flag来说明,这个flag就是Intent.FLAG_ACTIVITY_NEW_TASK
                intent_WebView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle_WebView = new Bundle();
                bundle_WebView.putString("content", message);//内容
                intent_WebView.putExtras(bundle_WebView);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, notifyId, intent_WebView, 0);
                notification.setContentIntent(pendingIntent);
            }
        }


        notificationManager.notify(notifyId, notification.build());

    }
}
