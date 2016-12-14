package com.show.specialshow.receiver;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.show.specialshow.activity.AppStartActivity;
import com.show.specialshow.activity.JpushWebView;
import com.show.specialshow.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

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
//        	processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

            //打开自定义的Activity
//        	Intent i = new Intent(context, BannerWebActivity.class);
//        	i.putExtras(bundle);
//        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//        	context.startActivity(i);
            if (isForeground(context)) {
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);//获取附加字段,是一个json数组
                String content = bundle.getString(JPushInterface.EXTRA_ALERT);

                try {
                    JSONObject json = new JSONObject(extras);
                    //进行json.getString("Article")操作时,要保证这里的Article与服务器上的Article一模一样,不然回报空指针异常
                    String articleUrl = json.getString("url");//获取附加字段Article,对应的值为articleUrl
                    Log.d("articleUrl:", articleUrl);
//                    if(StringUtils.isEmpty(articleUrl)){
//                    	UIHelper.showConfirmDialog(context, content, null, null, false);
//                    }else{
                    /**
                     * 点击通知栏通知, 打开网页地址
                     * 把articleUrl通过startActivity发送给WebViewActivity
                     */
                    Intent intent_WebView = new Intent(context, JpushWebView.class);
                    //必须要写,不然出错,因为这是一个从非activity的类跳转到一个activity,需要一个flag来说明,这个flag就是Intent.FLAG_ACTIVITY_NEW_TASK
                    intent_WebView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle bundle_WebView = new Bundle();
                    bundle_WebView.putString("url", articleUrl);//文章url
                    intent_WebView.putExtras(bundle_WebView);
                    context.startActivity(intent_WebView);//打开WebViewActivity
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Intent intent_WebView = new Intent(context, MainActivity.class);
                    //必须要写,不然出错,因为这是一个从非activity的类跳转到一个activity,需要一个flag来说明,这个flag就是Intent.FLAG_ACTIVITY_NEW_TASK
                    intent_WebView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bundle_WebView = new Bundle();
                    bundle_WebView.putString("content", content);//文章url
                    intent_WebView.putExtras(bundle_WebView);
                    context.startActivity(intent_WebView);//打开WebViewActivity
                }
            } else {
//            	UIHelper.ToastMessage(context, "2");
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);//获取附加字段,是一个json数组
                String content = bundle.getString(JPushInterface.EXTRA_ALERT);

                try {
                    JSONObject json = new JSONObject(extras);
                    //进行json.getString("Article")操作时,要保证这里的Article与服务器上的Article一模一样,不然回报空指针异常
                    String articleUrl = json.getString("url");//获取附加字段Article,对应的值为articleUrl
                    Log.d("articleUrl:", articleUrl);
//        if(StringUtils.isEmpty(articleUrl)){
//        	UIHelper.showConfirmDialog(context, content, null, null, false);
//        }else{
                    /**
                     * 点击通知栏通知, 打开网页地址
                     * 把articleUrl通过startActivity发送给WebViewActivity
                     */
                    Intent intent_WebView = new Intent(context, AppStartActivity.class);
                    //必须要写,不然出错,因为这是一个从非activity的类跳转到一个activity,需要一个flag来说明,这个flag就是Intent.FLAG_ACTIVITY_NEW_TASK
                    intent_WebView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bundle_WebView = new Bundle();
                    bundle_WebView.putString("url", articleUrl);//文章url
                    bundle_WebView.putInt("jpushFlag", 1);//文章url
                    intent_WebView.putExtras(bundle_WebView);
                    context.startActivity(intent_WebView);//打开WebViewActivity
//        }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Intent intent_WebView = new Intent(context, AppStartActivity.class);
                    //必须要写,不然出错,因为这是一个从非activity的类跳转到一个activity,需要一个flag来说明,这个flag就是Intent.FLAG_ACTIVITY_NEW_TASK
                    intent_WebView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bundle_WebView = new Bundle();
                    bundle_WebView.putString("content", content);//文章url
                    intent_WebView.putExtras(bundle_WebView);
                    context.startActivity(intent_WebView);//打开WebViewActivity
                }
            }

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
//        	UIHelper.ToastMessage(context, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
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


}
