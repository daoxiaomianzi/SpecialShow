package com.show.specialshow.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.show.specialshow.R;
import com.show.specialshow.activity.StoresDetailsActivity;
import com.show.specialshow.model.ShopListMess;
import com.show.specialshow.model.ShopListTagsMess;
import com.show.specialshow.utils.ImageLoderutils;
import com.show.specialshow.utils.UIHelper;

public class MyCollectAdapter extends BaseAdapter {
    OnItemViewClickListener listener;
    private List<ShopListMess> mList = new ArrayList<ShopListMess>();
    private List<ShopListTagsMess> mTagsMesses = new ArrayList<ShopListTagsMess>();
    private Context mContext;
    private int flag;


    public MyCollectAdapter(List<ShopListMess> mList, Context mContext, int flag) {
        super();
        this.mList = mList;
        this.mContext = mContext;
        this.flag = flag;
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
            return mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (mList != null) {
            return position;
        }
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.activity_my_collect_item, null);
            vh.show_lang_item_iv = (ImageView) convertView.findViewById(R.id.show_lang_item_iv);
            vh.show_lang_item_name = (TextView) convertView.findViewById(R.id.show_lang_item_name);
            vh.show_lang_item_card_num = (TextView) convertView.findViewById(R.id.show_lang_item_card_num);
            vh.show_lang_item_ceng_card_num = (TextView) convertView.findViewById(R.id.show_lang_item_ceng_card_num);
            vh.show_lang_item_moods_num = (TextView) convertView.findViewById(R.id.show_lang_item_moods_num);
            vh.show_lang_item_label_gv = (GridView) convertView.findViewById(R.id.show_lang_item_label_gv);
            vh.show_lang_item_rll = (RelativeLayout) convertView.findViewById(R.id.show_lang_item_rll);
            vh.show_lang_distance = (TextView) convertView.findViewById(R.id.show_lang_distance);
            vh.setPosition(position);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
            vh.setPosition(position);
        }
        ImageLoderutils imageLoderutils = new ImageLoderutils(mContext);
        imageLoderutils.display(vh.show_lang_item_iv, mList.get(position).getPic_urls());
        vh.show_lang_item_name.setText(mList.get(position).getTitle());
        vh.show_lang_item_card_num.setText(mList.get(position).getShowCard());
        vh.show_lang_item_ceng_card_num.setText(mList.get(position).getNeedCard());
        vh.show_lang_item_moods_num.setText(mList.get(position).getHot());
        switch (flag) {
            case 1:
                vh.show_lang_distance.setText(mList.get(position).getDistance());
                break;
            case 2:
                vh.show_lang_distance.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        mTagsMesses = ShopListTagsMess.parse(mList.get(position).getTags());
        vh.show_lang_item_label_gv.setAdapter(new TagsMessAdapter(mTagsMesses, mContext));
/*			vh.show_lang_item_rll.setOnClickListener(new OnClickListener() {
            @Override
			public void onClick(View v) {
				Bundle bundle=new Bundle();
				bundle.putSerializable("shopList", mList.get(position));
				UIHelper.startActivity(getActivity(), StoresDetailsActivity.class,bundle);
			}
		});*/
        listener = new OnItemViewClickListener(vh);
//		vh.show_lang_item_rll.setOnClickListener(listener);
        return convertView;
    }


    class OnItemViewClickListener implements OnClickListener {
        ViewHolder holder;

        public OnItemViewClickListener(ViewHolder holder) {
            super();
            this.holder = holder;
        }

        @Override
        public void onClick(View v) {
//            Bundle bundle = new Bundle();
//            switch (v.getId()) {
//                case R.id.show_lang_item_rll:
////			bundle.putSerializable("shopList", mList.get(holder.getPosition()));
//                    bundle.putString("shop_id", mList.get(holder.getPosition()).getShop_id());
//                    UIHelper.startActivity((Activity) mContext, StoresDetailsActivity.class, bundle);
//                    break;
//            }
        }
    }

    static class ViewHolder {
        private int position;
        private ImageView show_lang_item_iv;
        private TextView show_lang_item_name;
        private TextView show_lang_item_card_num;
        private TextView show_lang_item_ceng_card_num;
        private TextView show_lang_item_moods_num;
        private GridView show_lang_item_label_gv;
        private TextView show_lang_distance;
        private RelativeLayout show_lang_item_rll;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}
