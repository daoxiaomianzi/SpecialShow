package com.show.specialshow.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.utils.UIHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SelectLabelActivity extends BaseActivity {
	//圆点控件
	private ImageView circle_red_small1;
	private ImageView circle_red_small2;
	private ImageView circle_red_small3;
	private ImageView circle_red_small4;
	private ImageView circle_red_small5;
	private ImageView circle_red_small6;
	private ImageView circle_red_small7;
	private ImageView circle_red_small8;
	private ImageView circle_red_small9;
	private List<String> labelList=new ArrayList<>();
	@Override
	public void initData() {
		setContentView(R.layout.activity_select_label);

	}

	@Override
	public void initView() {
		circle_red_small1= (ImageView) findViewById(R.id.circle_red_small1);
		circle_red_small2= (ImageView) findViewById(R.id.circle_red_small2);
		circle_red_small3= (ImageView) findViewById(R.id.circle_red_small3);
		circle_red_small4= (ImageView) findViewById(R.id.circle_red_small4);
		circle_red_small5= (ImageView) findViewById(R.id.circle_red_small5);
		circle_red_small6= (ImageView) findViewById(R.id.circle_red_small6);
		circle_red_small7= (ImageView) findViewById(R.id.circle_red_small7);
		circle_red_small8= (ImageView) findViewById(R.id.circle_red_small8);
		circle_red_small9= (ImageView) findViewById(R.id.circle_red_small9);
	}

	@Override
	public void fillView() {
		head_title_tv.setText("选择标签");
		head_right_tv.setText("确定");
		head_right_tv.setVisibility(View.VISIBLE);
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_right_tv:
			Intent data = new Intent();
			data.putStringArrayListExtra("labels", (ArrayList<String>) labelList);
			UIHelper.setResult(mContext, RESULT_OK, data);
			break;
			case R.id.rll_select_label1:
			visibleChage(circle_red_small1,"美发");

				break;
			case R.id.rll_select_label2:
				visibleChage(circle_red_small2,"美容");

				break;
			case R.id.rll_select_label3:
				visibleChage(circle_red_small3,"美甲");

				break;
			case R.id.rll_select_label4:
				visibleChage(circle_red_small4,"瑜伽");

				break;
			case R.id.rll_select_label5:
				visibleChage(circle_red_small5,"美睫");

				break;
			case R.id.rll_select_label6:
				visibleChage(circle_red_small6,"美体");

				break;
			case R.id.rll_select_label7:
				visibleChage(circle_red_small7,"纹绣");

				break;
			case R.id.rll_select_label8:
				visibleChage(circle_red_small8,"养生");

				break;
			case R.id.rll_select_label9:
				visibleChage(circle_red_small9,"光电");
				break;

		default:
			break;
		}
	}
	/**
	 * 处理点击
	 */
	private void visibleChage(ImageView imageView,String label){
		if(imageView.getVisibility()==View.VISIBLE){
			imageView.setVisibility(View.GONE);
			labelList.remove(label);
		}else{
			if(labelList.size()>2){
				UIHelper.ToastMessage(mContext,"最多只能选三项");
			}else{
				labelList.add(label);
				imageView.setVisibility(View.VISIBLE);
			}
		}
	}
}
