package com.show.specialshow.activity;

import android.graphics.Color;
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
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.MD5Utils;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.UIHelper;


public class SetTradingPasswordActivity extends BaseActivity {
	
	private Button set_trading_password;

	// 输入框控件
	private EditText input_set_trading_password;
	private EditText input_again_set_trading_password;
	//交易密码，再次交易密码；
	private String setTradingPassword,againSetTradingPassword;

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_set_trading_password);
		input_again_set_trading_password = (EditText) findViewById(R.id.input_again_set_trading_password);
		input_set_trading_password = (EditText) findViewById(R.id.input_set_trading_password);
		set_trading_password = (Button) findViewById(R.id.set_trading_confirm);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		changeEditText();
	}

	@Override
	public void fillView() {
		// TODO Auto-generated method stub
		if(TXApplication.setTradingpassword){
			head_title_tv.setText("修改支付密码");
		}else{
			head_title_tv.setText("设置支付密码");

		}
//		head_title_tv.setTextColor(Color.WHITE);
//		head_left_tv.setVisibility(View.VISIBLE);
//		Drawable rightDrawable = getResources().getDrawable(R.drawable.back_arrow);
//        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
//        head_left_tv.setCompoundDrawables(null, null, rightDrawable, null);

	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	public void onClick(View view) {
		if(!BtnUtils.getInstance().isFastDoubleClick()){
			return;
		}
		switch (view.getId()) {
		case R.id.set_trading_confirm:
			setTradingPassword();
			break;
		case R.id.contest_confirm_tv:
			affirmDialog.cancel();
			break;

		default:
			break;
		}
	}
	/**
	 *设置交易密码
	 */
	private void setTradingPassword(){
		setTradingPassword=input_set_trading_password.getText().toString().trim();
		againSetTradingPassword=input_again_set_trading_password.getText().toString().trim();
		String uid=(String) SPUtils.get(mContext, "uid", "");
		if(setTradingPassword.equals(againSetTradingPassword)){

			RequestParams params= TXApplication.getParams();
			params.addBodyParameter("uid", uid);
			params.addBodyParameter("pin_pass1", MD5Utils.getMd5Str(setTradingPassword));
			params.addBodyParameter("pin_pass2", MD5Utils.getMd5Str(againSetTradingPassword));

			TXApplication.post(null, mContext, URLs.LOGIN_SETPINPASS, params, new RequestCallBack<String>() {
				@Override
				public void onStart() {
					super.onStart();
					set_trading_password.setEnabled(false);
					loadIng("正在设置...",true);
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					// TODO Auto-generated method stub
					set_trading_password.setEnabled(true);
					if(dialog!=null){
						dialog.dismiss();
					}
					UIHelper.ToastMessage(mContext, R.string.net_work_error);
				}

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					set_trading_password.setEnabled(true);
					if(dialog!=null){
						dialog.dismiss();
					}
//					Log.i("设置交易密码", responseInfo.result);
					MessageResult result=MessageResult.parse(responseInfo.result);
					if(result==null){
						return;
					}
					if(result.getSuccess()==1){
						UIHelper.ToastMessage(mContext, "支付密码设置成功");
						SPUtils.put(mContext, "setTradingSuccess", true);
						TXApplication.setTradingpassword=true;
						finish();
					}else{
						createAffirmDialog(result.getMessage(),DIALOG_SINGLE_STPE,true);
					}
				}
			});
		}else{
			createAffirmDialog("两次密码不一致",DIALOG_SINGLE_STPE,true);
		}
		
		
	}
	

	/**
	 * 输入框的变化判断
	 */
	private void changeEditText() {

		input_set_trading_password.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(input_set_trading_password.getText()
						.toString().trim())) {
					flag2 = true;
				} else {
					flag2 = false;
				}
				isflag();
			}
		});

		input_again_set_trading_password.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(input_again_set_trading_password.getText().toString()
						.trim())) {
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

		if (!flag2||!flag3) {
			set_trading_password.setEnabled(false);
			// modify_confirm.setTextColor(Color.GRAY);
			
		} else {
			set_trading_password.setEnabled(true);
			set_trading_password.setTextColor(Color.WHITE);
		}
	}

}
