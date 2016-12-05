package com.show.specialshow.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.RedCoupon;
import com.show.specialshow.utils.DateUtil;
import com.show.specialshow.utils.UIHelper;

import java.util.List;

public class RedCouponSwipeAdapter extends BaseSwipeAdapter {
    private List<RedCoupon> mlist_coupon;
    private Context mContext;
    OnItemViewClickListener listener;

    public RedCouponSwipeAdapter(List<RedCoupon> mlist_coupon, Context mContext) {
        super();
        this.mlist_coupon = mlist_coupon;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (mlist_coupon != null) {
            return mlist_coupon.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mlist_coupon != null) {
            return mlist_coupon.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (mlist_coupon != null) {
            return position;
        }
        return 0;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.red_coupon_swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View convertView;
        convertView = View.inflate(mContext,
                R.layout.view_red_coupon_swipe_item, null);
        return convertView;
    }

    protected void deleteCoupon(final int deletePosition, final SwipeLayout swipeLayout) {
        RequestParams params = TXApplication.getParams();
        String url = URLs.DELETE_INVALID_COUPON;
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException error, String msg) {
                swipeLayout.close();
                UIHelper.ToastMessage(mContext, R.string.net_work_error);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                MessageResult result = MessageResult.parse(responseInfo.result);
                if (result == null) {
                    swipeLayout.close();
                    return;
                }
                if (1 == result.getSuccess()) {
                    swipeLayout.close(false);
                    mlist_coupon.remove(deletePosition);
                    notifyDataSetChanged();
                    UIHelper.ToastMessage(mContext, result.getData());
                } else {
                    swipeLayout.close();
                    UIHelper.ToastMessage(mContext, result.getData());
                }
            }
        });
    }

    @Override
    public void fillValues(int position, View convertView) {
        final int deletePosition = position;
        final SwipeLayout swipeLayout = (SwipeLayout) convertView
                .findViewById(getSwipeLayoutResourceId(position));
        ViewHolder holder;
        if (mlist_coupon.get(position).getIs_use() == 0
                && mlist_coupon.get(position).getEnddate() > DateUtil.weeHours()) {
            swipeLayout.setSwipeEnabled(false);
        } else {
            swipeLayout.setSwipeEnabled(true);
        }
        holder = new ViewHolder();
        holder.setPosition(position);
        holder.red_coupon_type_bg_item = (RelativeLayout) convertView
                .findViewById(R.id.red_coupon_type_bg_item);
        holder.red_coupon_type_item = (TextView) convertView
                .findViewById(R.id.red_coupon_type_item);
        holder.red_coupon_value_tv = (TextView) convertView
                .findViewById(R.id.red_coupon_value_tv);
        holder.red_coupon_usefull_life_tv = (TextView) convertView
                .findViewById(R.id.red_coupon_usefull_life_tv);
        holder.red_coupon_use_sill_tv = (TextView) convertView
                .findViewById(R.id.red_coupon_use_sill_tv);
        holder.red_coupon_validity_date_tv = (TextView) convertView
                .findViewById(R.id.red_coupon_validity_date_tv);
        holder.red_coupon_buy_btn = (TextView) convertView
                .findViewById(R.id.red_coupon_buy_btn);
        holder.red_coupon_all = (LinearLayout) convertView.findViewById(R.id.red_coupon_all);
        listener = new OnItemViewClickListener(holder);
        holder.red_coupon_all.setOnClickListener(listener);
        convertView.findViewById(R.id.trash).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        deleteCoupon(deletePosition, swipeLayout);
                    }
                });
        bindData(holder);
    }

    private void bindData(ViewHolder holder) {
        RedCoupon coupon_item = mlist_coupon.get(holder.getPosition());
        holder.red_coupon_buy_btn.setText("立即\n使用");
        holder.red_coupon_type_item.setText("优惠劵");
        holder.red_coupon_value_tv.setText("" + coupon_item.getNum()
                + "元");
        switch (coupon_item.getType()) {
            case 0:
                holder.red_coupon_usefull_life_tv.setText("全场通用");
                break;
            case 1:
                holder.red_coupon_usefull_life_tv.setText(coupon_item.getUse_merchant()
                        + "全场通用");
                break;
            case 2:
                holder.red_coupon_usefull_life_tv.setText("仅限" + coupon_item.getUse_merchant() +
                        coupon_item.getUse_service() + "使用");
                break;
            default:
                break;
        }
        holder.red_coupon_use_sill_tv.setText("使用起点:\t≥"
                + coupon_item.getUse_money() + "元可用");
        holder.red_coupon_validity_date_tv
                .setText("有效期至:\t"
                        + DateUtil.getOnlytoDayByMilli(coupon_item
                        .getEnddate() * 1000));
        if (coupon_item.getIs_use() == 0 &&
                coupon_item.getEnddate() > DateUtil.weeHours()) {
            holder.red_coupon_value_tv.setTextColor(mContext.getResources()
                    .getColor(R.color.novice_label_text));
            holder.red_coupon_use_sill_tv.setTextColor(mContext.getResources()
                    .getColor(R.color.color_656565));
            holder.red_coupon_validity_date_tv
                    .setTextColor(mContext.getResources().getColor(
                            R.color.color_656565));
            holder.red_coupon_buy_btn
                    .setBackgroundResource(R.drawable.bg_rad_coupon_buy_useable);
            holder.red_coupon_type_bg_item
                    .setBackgroundResource(R.drawable.icon_red_coupon_available_title);
        } else {
            if (coupon_item.getIs_use() == 0) {
                holder.red_coupon_buy_btn.setText("已过期");
            } else {
                holder.red_coupon_buy_btn.setText("已使用");
            }
            holder.red_coupon_value_tv.setTextColor(mContext.getResources()
                    .getColor(R.color.color_cccccc));
            holder.red_coupon_use_sill_tv.setTextColor(mContext.getResources()
                    .getColor(R.color.color_cccccc));
            holder.red_coupon_validity_date_tv
                    .setTextColor(mContext.getResources().getColor(
                            R.color.color_cccccc));
            holder.red_coupon_buy_btn
                    .setBackgroundResource(R.drawable.bg_coupon_unuserable_buy);
            holder.red_coupon_type_bg_item
                    .setBackgroundResource(R.drawable.icon_coupon_unuserable);
        }
    }

    class OnItemViewClickListener implements OnClickListener {
        ViewHolder holder;

        public OnItemViewClickListener(ViewHolder holder) {
            super();
            this.holder = holder;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            switch (v.getId()) {
                case R.id.red_coupon_buy_btn:
//				RedCoupon coupon_item = mlist_coupon.get(holder.getPosition());
//				if(coupon_item.getIs_use() == 0
//						&& coupon_item.getEnddate() * 1000 > System
//						.currentTimeMillis()){
//					bundle.putInt("registerBack", 1);
//					UIHelper.startActivity((Activity) mContext, MainActivity.class, bundle);
//				}
                    break;
                case R.id.red_coupon_all:
//                    RedCoupon coupon_item = mlist_coupon.get(holder.getPosition());
//
//                    if (coupon_item.getIs_use() == 0
//                            && coupon_item.getEnddate() > DateUtil.weeHours()) {
//
//                        bundle.putInt("registerBack", 1);
//                        UIHelper.startActivity((Activity) mContext, MainActivity.class, bundle);
//                    }
                    break;

            }
        }
    }

    static class ViewHolder {
        RelativeLayout red_coupon_type_bg_item;
        TextView red_coupon_type_item;
        TextView red_coupon_value_tv;
        TextView red_coupon_usefull_life_tv;
        TextView red_coupon_use_sill_tv;
        TextView red_coupon_validity_date_tv;
        TextView red_coupon_buy_btn;
        LinearLayout red_coupon_all;

        private int position;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

    }
}





