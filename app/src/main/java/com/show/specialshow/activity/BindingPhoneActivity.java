package com.show.specialshow.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.contstant.ConstantValue;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.IsMatcher;
import com.show.specialshow.utils.MD5Utils;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.TimeCount;
import com.show.specialshow.utils.UIHelper;

public class BindingPhoneActivity extends BaseActivity {
    //相关控件
    private EditText binging_phonenumber;//输入手机号
    private EditText binging_code;//输入验证码
    private EditText binging_password;//输入密码
    private Button binging_sms_code;//获取验证码
    private Button binging_complete;//完成
    // 计时
    private TimeCount time;

    @Override
    public void initData() {
        setContentView(R.layout.activity_binding_phone);
    }

    @Override
    public void initView() {
        binging_phonenumber = (EditText) findViewById(R.id.binging_phonenumber);
        binging_code = (EditText) findViewById(R.id.binging_code);
        binging_password = (EditText) findViewById(R.id.binging_password);
        binging_sms_code = (Button) findViewById(R.id.binging_sms_code);
        binging_complete = (Button) findViewById(R.id.binging_complete);
        time = new TimeCount(ConstantValue.millisInFuture,
                ConstantValue.countDownInterval, binging_sms_code, 1);
    }

    @Override
    public void fillView() {
        isComplete();
        head_title_tv.setText("绑定手机");
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
            case R.id.binging_complete:
                binging_complete.setEnabled(false);
                complete();
                break;
            case R.id.binging_sms_code:
                isRegister();
                break;
            case R.id.contest_confirm_tv:
                if (null != affirmDialog) {
                    affirmDialog.dismiss();
                }
                break;
            default:
                break;
        }
    }

    private void complete() {
        RequestParams params = TXApplication.getParams();
        String url = URLs.LOGIN_BINDPHONE;
        String phone = binging_phonenumber.getText().toString().trim();
        String code = binging_code.getText().toString().trim();
        String password = binging_password.getText().toString().trim();
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("code", code);
        params.addBodyParameter("uid", (String) SPUtils.get(mContext, "uid", ""));
        params.addBodyParameter("password", MD5Utils.getMd5Str(password));
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                loadIng("正在绑定...", true);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                binging_complete.setEnabled(true);
                if (null != dialog) {
                    dialog.dismiss();
                }
                MessageResult result = MessageResult.parse(responseInfo.result);
                if (null == result) {
                    return;
                }
                if (1 == result.getSuccess()) {
                    Intent intent = new Intent();
                    UIHelper.setResult(mContext, RESULT_OK, intent);
                } else {
                    UIHelper.ToastMessage(mContext, result.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                binging_complete.setEnabled(true);
                if (null != dialog) {
                    dialog.dismiss();
                }
                UIHelper.ToastMessage(mContext, R.string.net_work_error);
            }
        });

    }

    /**
     * 判断手机号是否注册过
     */
    private void isRegister() {
        if ((!TextUtils.isEmpty(binging_phonenumber.getText().toString())) &&
                IsMatcher.isMobileNO(binging_phonenumber.getText().toString())) {
            getCode();
        } else {
            UIHelper.ToastLogMessage(mContext, "请输入正确的11位手机号");
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        RequestParams params = TXApplication.getParams();
        String url = URLs.SMS_CODE_URL;
        params.addBodyParameter("phone", binging_phonenumber.getText().toString().trim());
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException error, String msg) {
                UIHelper.ToastMessage(mContext, R.string.net_work_error);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                MessageResult result = MessageResult.parse(responseInfo.result);
                if (null == result) {
                    return;
                }
                if (1 == result.getSuccess()) {
                    time.start();
//					UIHelper.ToastMessage(mContext, result.getMessage());
                } else {
                    createAffirmDialog(result.getMessage(), DIALOG_SINGLE_STPE, true);
                }
            }
        });
    }

    /**
     * 判断是否可点击注册
     */
    private void isComplete() {
        // 文本监听
        binging_phonenumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    flag1 = true;
                } else {
                    flag1 = false;
                }
                isflag();
            }
        });

        binging_code.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    flag2 = true;
                } else {
                    flag2 = false;
                }
                isflag();
            }
        });
        binging_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (!TextUtils.isEmpty(s.toString())) {
                    flag3 = true;
                } else {
                    flag3 = false;
                }
                isflag();
            }
        });
    }

    // 判断
    private void isflag() {
        if (!flag1 || !flag2 || !flag3) {
            binging_complete.setEnabled(false);
        } else {
            binging_complete.setEnabled(true);
        }
    }
}
