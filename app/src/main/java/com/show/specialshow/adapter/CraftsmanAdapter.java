package com.show.specialshow.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.activity.CraftsmandetailsActivity;
import com.show.specialshow.activity.LoginActivity;
import com.show.specialshow.activity.ShowerDetailsActivity;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.ShopVisitorListMess;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.UIHelper;

import java.util.List;

public class CraftsmanAdapter extends BaseAdapter {
    private List<ShopVisitorListMess> mList;
    private Context mContext;
    OnItemViewClickListener listener;
    private String[] solars = {"白羊", "金牛", "双子", "巨蟹", "狮子", "处女", "天秤", "天蝎", "射手",
            "摩羯", "水瓶", "双鱼"};
    private int[] solarImgs = {R.drawable.icon_baiyang, R.drawable.icon_jinniu
            , R.drawable.icon_shuangzi, R.drawable.icon_juxie, R.drawable.icon_shizi
            , R.drawable.icon_chunv, R.drawable.icon_tianping, R.drawable.icon_tianxie
            , R.drawable.icon_sheshou, R.drawable.icon_mojie, R.drawable.icon_shuiping
            , R.drawable.icon_shuangyu};


    public CraftsmanAdapter(List<ShopVisitorListMess> mList, Context mContext) {
        super();
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mList != null) {
            return mList.size();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        UserMessage user = TXApplication.getUserMess();
        if (null == convertView) {
            vh = new ViewHolder();
            convertView = View.inflate(mContext,
                    R.layout.fragment_craftsman_item, null);
            vh.setPostion(position);
            vh.craftsman_item_iv = (ImageView) convertView
                    .findViewById(R.id.craftsman_item_iv);
            vh.craftsman_item_name = (TextView) convertView
                    .findViewById(R.id.craftsman_item_name);
            vh.craftsman_item_distance = (TextView) convertView
                    .findViewById(R.id.craftsman_item_distance);
            vh.craftsman_item_age = (TextView) convertView
                    .findViewById(R.id.craftsman_item_age);
            vh.craftsman_item_constellation = (TextView) convertView
                    .findViewById(R.id.craftsman_item_constellation);
            vh.craftsman_item_attention_btn = (TextView) convertView
                    .findViewById(R.id.craftsman_item_attention_btn);
            vh.craftsman_item_rll = (RelativeLayout) convertView
                    .findViewById(R.id.craftsman_item_rll);
            vh.craftsman_item_address = (TextView) convertView.findViewById(R.id.craftsman_item_address);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
            vh.setPostion(position);
        }
        ImageLoader.getInstance().displayImage(mList
                .get(position).getUser_icon(), vh.craftsman_item_iv);
        vh.craftsman_item_name.setText(mList.get(position)
                .getUser_name());
        vh.craftsman_item_address.setText(mList.get(position).getShop_name());
        vh.craftsman_item_age.setText(mList.get(position).getUser_age());
        if ("男".equals(mList.get(position).getUser_sex())) {
            vh.craftsman_item_age.setVisibility(View.VISIBLE);
            UIHelper.leftDrawable(
                    R.drawable.icon_sex_boy, mContext,
                    vh.craftsman_item_age);
            vh.craftsman_item_age.setBackgroundResource(R.drawable.bg_sex_man);
        } else if ("女".equals(user.getSex())) {
            vh.craftsman_item_age.setVisibility(View.VISIBLE);
            UIHelper.leftDrawable(
                    R.drawable.icon_sex_girl, mContext,
                    vh.craftsman_item_age);
            vh.craftsman_item_age.setBackgroundResource(R.drawable.bg_sex_womam);
        } else {
            if (mList.get(position).getUser_age() == null) {
                vh.craftsman_item_age.setVisibility(View.GONE);

            } else {
                vh.craftsman_item_age.setVisibility(View.VISIBLE);
                UIHelper.leftDrawable(
                        R.color.transparent, mContext,
                        vh.craftsman_item_age);
                vh.craftsman_item_age.setBackgroundResource(R.drawable.bg_sex_man);
            }
        }
        if (0 == mList.get(position).getUser_xz()) {
            vh.craftsman_item_constellation.setVisibility(View.GONE);
        } else {
            bindXzData(vh.craftsman_item_constellation, solarImgs[mList.get(position).getUser_xz() - 1],
                    solars[mList.get(position).getUser_xz() - 1]);
        }
        vh.craftsman_item_distance.setText(mList.get(position)
                .getDistance());
        if (user.getUid().equals(mList.get(position).getUser_id())) {
            vh.craftsman_item_attention_btn.setVisibility(View.GONE);
        } else {
            vh.craftsman_item_attention_btn.setVisibility(View.VISIBLE);
        }
        switch (mList.get(position).getAttention()) {
            case 2:
                vh.craftsman_item_attention_btn.setText("+关注");
                vh.craftsman_item_attention_btn.setSelected(true);
                break;
            case 1:
                vh.craftsman_item_attention_btn.setText("已关注");
                vh.craftsman_item_attention_btn.setSelected(false);
                break;
            default:
                break;
        }
        listener = new OnItemViewClickListener(vh);
//        vh.craftsman_item_rll.setOnClickListener(listener);
        vh.craftsman_item_attention_btn.setOnClickListener(listener);
        return convertView;
    }

    private void bindXzData(TextView textView, int res, String string) {
        textView.setVisibility(View.VISIBLE);
        textView.setText(string);
        UIHelper.leftDrawable(
                res, mContext,
                textView);
    }

    class OnItemViewClickListener implements OnClickListener {
        ViewHolder holder;

        public OnItemViewClickListener(ViewHolder holder) {
            super();
            this.holder = holder;
        }

        @Override
        public void onClick(View v) {
            if (!BtnUtils.getInstance().isFastDoubleClick()) {
                return;
            }
            Bundle bundle = new Bundle();
            switch (v.getId()) {
//                case R.id.craftsman_item_rll:
//                    bundle.putString("user_id", mList.get(holder.getPostion())
//                            .getUser_id());
//                    UIHelper.startActivity((Activity) mContext,
//                            CraftsmandetailsActivity.class, bundle);
//                    break;
                case R.id.craftsman_item_attention_btn:// 关注

                    if (TXApplication.login) {
                        attention(mList.get(holder.getPostion()).getUser_id(),
                                holder.craftsman_item_attention_btn);
                    } else {
                        bundle.putInt(LoginActivity.FROM_LOGIN, LoginActivity.FROM_OTHER);
                        UIHelper.startActivity((Activity) mContext, LoginActivity.class, bundle);
                    }
                    break;
            }
        }
    }

    private void attention(String attentid, final TextView btn) {
        RequestParams params = TXApplication.getParams();
        String url = URLs.ATTENTION_USER;
        String uid = TXApplication.filename.getString("uid", "");
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("attentid", attentid);
        TXApplication.post(null, mContext, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        UIHelper.ToastMessage(mContext, R.string.net_work_error);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        MessageResult result = MessageResult
                                .parse(responseInfo.result);
                        if (result == null) {
                            return;
                        }
                        switch (result.getSuccess()) {
                            case 1:
                                UIHelper.ToastLogMessage(mContext,
                                        result.getMessage());
                                btn.setText("已关注");
                                btn.setSelected(false);
                                break;
                            case 2:
                                UIHelper.ToastLogMessage(mContext,
                                        result.getMessage());
                                btn.setText("+关注");
                                btn.setSelected(true);
                                break;
                            default:
                                UIHelper.ToastLogMessage(mContext,
                                        result.getMessage());
                                break;
                        }
                    }
                });
    }

    static class ViewHolder {
        private int postion;
        private ImageView craftsman_item_iv;// 秀客头像
        private TextView craftsman_item_name;// 秀客昵称
        private TextView craftsman_item_age;// 秀客年龄
        private TextView craftsman_item_constellation;// 秀客星座
        private TextView craftsman_item_distance;// 距离
        private TextView craftsman_item_address;// 门店地址
        private RelativeLayout craftsman_item_rll;// 整个item
        private TextView craftsman_item_attention_btn;// 关注按钮

        public int getPostion() {
            return postion;
        }

        public void setPostion(int postion) {
            this.postion = postion;
        }
    }

}
