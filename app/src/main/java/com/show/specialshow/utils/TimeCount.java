package com.show.specialshow.utils;



import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.Button;

import com.show.specialshow.R;
import com.show.specialshow.contstant.ConstantValue;

public class TimeCount extends CountDownTimer {
	
	private Button SMS_code;
	private int index;
	

	public TimeCount(long millisInFuture, long countDownInterval,Button SMS_code,int index) {
		super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		this.SMS_code=SMS_code;
		this.index=index;
	}

	@Override
	public void onTick(long millisUntilFinished) {//计时过程显示
		// TODO Auto-generated method stub
		SMS_code.setEnabled(false);
		if(index==1){
			SMS_code.setTextColor(Color.GRAY);
			SMS_code.setText("重新发送("+millisUntilFinished/ConstantValue.countDownInterval+")");
		}else if(index==2){
			SMS_code.setText(millisUntilFinished/ConstantValue.countDownInterval+"秒");
			SMS_code.setTextColor(Color.GRAY);
		}
	}

	@Override
	public void onFinish() {
		SMS_code.setEnabled(true);
		if(index==1){
			SMS_code.setText(R.string.get_sms_code);
		}else if(index==2){
			SMS_code.setText(R.string.get_sms_code);
			SMS_code.setTextColor(Color.rgb(234,86,57));
		}
	}

}
