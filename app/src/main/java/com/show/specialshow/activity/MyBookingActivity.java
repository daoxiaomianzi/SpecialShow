package com.show.specialshow.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.BaseSearchActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.MyBookingMess;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.xlistview.XListView;

import java.util.List;

/**
 * 我的预约页
 *
 * @author admin
 */
public class MyBookingActivity extends BaseSearchActivity {
    private List<MyBookingMess> myBookingList;
    private TextView my_booking_nodata_tv;
    public static final String RESER_DETAILS = "reser_details";

    @Override
    public void initData() {
        setContentView(R.layout.activity_my_booking);
    }

    @Override
    public void initView() {
        my_booking_nodata_tv = (TextView) findViewById(R.id.my_booking_nodata_tv);
        search_result_lv = (XListView) findViewById(R.id.search_result_lv);
        search_result_lv.setBackgroundResource(R.color.app_bg);
        adapter = new MyBookingAdapter();
    }

    @Override
    public void fillView() {
        head_title_tv.setText("我的预约");
        initListView();
        search_result_lv.setPullLoadEnable(false);
    }

    @Override
    public void setListener() {
        search_result_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (!BtnUtils.getInstance().isFastDoubleClick()) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(RESER_DETAILS, myBookingList.get(position - 1));
                UIHelper.startActivity(mContext, ReserDetailsActivity.class, bundle);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void getData() {
        UserMessage user = TXApplication.getUserMess();
        RequestParams params = TXApplication.getParams();
        String url = URLs.APPOINMENT_APPOINTLIST;
        params.addBodyParameter("uid", user.getUid());
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException error, String msg) {
                changeListView(0);
                UIHelper.ToastMessage(mContext, R.string.net_work_error);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                MessageResult result = MessageResult.parse(responseInfo.result);
                if (null == result) {
                    changeListView(0);
                    UIHelper.ToastMessage(mContext, "获取数据失败");
                    return;
                }
                if (1 == result.getSuccess()) {
                    myBookingList = MyBookingMess.parse(result.getData());
                    if (myBookingList == null) {
                        my_booking_nodata_tv.setVisibility(View.VISIBLE);
                        search_result_lv.setVisibility(View.VISIBLE);
                    } else {
                        my_booking_nodata_tv.setVisibility(View.GONE);
                        search_result_lv.setVisibility(View.VISIBLE);
                    }
                    changeListView(0);
                } else {
                    UIHelper.ToastMessage(mContext, result.getMessage());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    class MyBookingAdapter extends BaseAdapter {
    /*	private Context mContext;
        private List<MyBookingMess> myBookingList;

		public MyBookingAdapter(Context mContext,List<MyBookingMess> myBookingList) {
			super();
			this.mContext = mContext;
			this.myBookingList = myBookingList;
		}*/

        @Override
        public int getCount() {
            return null == myBookingList ? 0 : myBookingList.size();
        }

        @Override
        public Object getItem(int position) {
            return null == myBookingList ? null : myBookingList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return null == myBookingList ? 0 : position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (null == convertView) {
                vh = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.activity_my_booking_item, null);
                vh.mybooking_shop_name = (TextView) convertView.findViewById(R.id.mybooking_shop_name);
                vh.mybooking_service_name = (TextView) convertView.findViewById(R.id.mybooking_service_name);
                vh.mybooking_staff_name = (TextView) convertView.findViewById(R.id.mybooking_staff_name);
                vh.mybooking_appoinment_time = (TextView) convertView.findViewById(R.id.mybooking_appoinment_time);
                vh.mybooking_add_time = (TextView) convertView.findViewById(R.id.mybooking_add_time);
                vh.mybooking_status = (TextView) convertView.findViewById(R.id.mybooking_status);
                vh.all_mybooking = (RelativeLayout) convertView.findViewById(R.id.all_mybooking);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.mybooking_shop_name.setText(myBookingList.get(position).getShop_name());
            vh.mybooking_service_name.setText(myBookingList.get(position).getService_name());
            vh.mybooking_staff_name.setText(myBookingList.get(position).getStaff_name());
            vh.mybooking_appoinment_time.setText(myBookingList.get(position).getAppointment_time());
            vh.mybooking_add_time.setText(myBookingList.get(position).getAdd_time());
            switch (myBookingList.get(position).getStatus()) {
                case 0:
                    vh.mybooking_status.setBackgroundResource(R.drawable.icon_to_accept);
                    break;
                case 1:
                    vh.mybooking_status.setBackgroundResource(R.drawable.icon_confirmed);
                    break;
                case 3:
                    vh.mybooking_status.setBackgroundResource(R.drawable.icon_confirmed);
                    break;
                case 2:
                    vh.mybooking_status.setBackgroundResource(R.drawable.icon_cancelled);
                    break;

                default:
                    break;
            }
            return convertView;
        }

        class ViewHolder {
            TextView mybooking_shop_name;
            TextView mybooking_service_name;
            TextView mybooking_staff_name;
            TextView mybooking_appoinment_time;
            TextView mybooking_add_time;
            TextView mybooking_status;
            RelativeLayout all_mybooking;
        }
    }
}
