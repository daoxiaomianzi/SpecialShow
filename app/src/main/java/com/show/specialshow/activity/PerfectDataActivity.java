package com.show.specialshow.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.utils.AppManager;
import com.show.specialshow.utils.RoundImageView;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.UIHelper;

import org.apache.commons.lang3.StringUtils;

import java.lang.ref.WeakReference;

public class PerfectDataActivity extends BaseActivity {
    private static final int PERFECT_PHOTO_CARMERA = 1;
    private static final int PERFECT_PHOTO_PICK = 2;
    private static final int PERFECT_PHOTO_CUT = 3;
    //相关控件
    private RoundImageView riv_perfect_data;
    private EditText et_perfect_data_nickname;
    private int from_mode=0;

    @Override
    public void initData() {
        from_mode=getIntent().getIntExtra("from_mode",0);
        setContentView(R.layout.activity_perfect_data);


    }

    @Override
    public void initView() {
        riv_perfect_data= (RoundImageView) findViewById(R.id.riv_perfect_data);
        et_perfect_data_nickname= (EditText) findViewById(R.id.et_perfect_data_nickname);
    }

    @Override
    public void fillView() {
        head_title_tv.setText("完善个人资料");
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.riv_perfect_data://头像
                showSelectDialog(PERFECT_PHOTO_PICK,PERFECT_PHOTO_CARMERA);
                break;
            case R.id.bt_perfect_data_submit://提交
                submit();
                break;
        }

    }

    /**
     * 完善信息
     */
    private void submit() {
        if(null==tempFile){
            UIHelper.ToastMessage(mContext,"请上传头像");
        }else{
            if(StringUtils.isEmpty(et_perfect_data_nickname.getText().toString().trim())){
                UIHelper.ToastMessage(mContext,"请输入昵称");
            }else{
                RequestParams params = TXApplication.getParams();
                String url = URLs.SPACE_SAVEINFO;
                UserMessage user=TXApplication.getUserMess();
                params.addBodyParameter("uid", user.getUid());

                params.addBodyParameter("nickname", et_perfect_data_nickname.getText().toString().trim());
                params.addBodyParameter("type", "1");
                params.addBodyParameter("icon", tempFile);
                TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException error, String msg) {
                        UIHelper.ToastMessage(mContext, R.string.net_work_error);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        MessageResult result = MessageResult.parse(responseInfo.result);
                        if (null == result) {
                            UIHelper.ToastMessage(mContext, "提交失败");
                            return;
                        }
                        if (1 == result.getSuccess()) {
                            UserMessage userMessage = UserMessage.parse(result.getData());
                            SPUtils.put(mContext,"icon",userMessage.getIcon());
                            SPUtils.put(mContext,"nickname",userMessage.getNickname());
                            boolean f=userMessage.isIchange();
                            SPUtils.put(mContext,"ichange",userMessage.isIchange());
                            if(0==from_mode){
                                AppManager.getAppManager().finishActivity(3);

                            }else{
                                finish();
                            }
                            UIHelper.ToastMessage(mContext,"提交成功");
                        } else {
                            UIHelper.ToastMessage(mContext, result.getMessage());
                        }
                    }
                });
            }
        }
    }

    @Override
    public void goBack(View v) {
        super.goBack(v);
        if(0==from_mode){
            TXApplication.quitLogin();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case PERFECT_PHOTO_CARMERA:
                    startPhotoZoom(Uri.fromFile(tempFile), 300, PERFECT_PHOTO_CUT);
                    break;
                case PERFECT_PHOTO_PICK:
                    if (null != data) {
                        startPhotoZoom(data.getData(), 300, PERFECT_PHOTO_CUT);
                    }
                    break;
                case PERFECT_PHOTO_CUT:
                    if (null != data) {
                        Message message=new Message();
                        Bundle bundle=new Bundle();
                        bundle.putBundle("data",data.getExtras());
                        message.setData(bundle);//bundle传值，耗时，效率低
                        message.what=1;//标志是哪个线程传数据
                        basicHandler.sendMessage(message);//发送message信息
                    }
                    break;
                default:
                    break;
            }
        }
    }
    private BasicHandler basicHandler = new BasicHandler(this);

    class BasicHandler extends Handler {
        WeakReference<PerfectDataActivity> mActivity;

        BasicHandler(PerfectDataActivity activity) {
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            if(1==msg.what){
                Bundle data = msg.getData().getBundle("data");
                setPicToView(data,riv_perfect_data);
            }
        }
    }
}
