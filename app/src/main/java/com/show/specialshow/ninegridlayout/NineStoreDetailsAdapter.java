package com.show.specialshow.ninegridlayout;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.show.specialshow.R;
import com.show.specialshow.model.ShopComcardStaPicsMess;
import com.show.specialshow.model.ShopComcardStaUserMess;
import com.show.specialshow.model.ShopCommendcardStatusMess;
import com.show.specialshow.utils.ImageLoderutils;
import com.show.specialshow.utils.RoundImageView;

/**
 * Created by Pan_ on 2015/2/3.
 */
public class NineStoreDetailsAdapter extends BaseAdapter {
	private Context context;
	private List<ShopCommendcardStatusMess> mCommendcardStatusMesses;// 秀卡动态数据
	private int index;

	public NineStoreDetailsAdapter(Context context,
			List<ShopCommendcardStatusMess> mCommendcardStatusMesses, int index) {
		this.context = context;
		this.mCommendcardStatusMesses = mCommendcardStatusMesses;
		this.index = index;
	}

	@Override
	public int getCount() {
		return mCommendcardStatusMesses == null ? 0 : mCommendcardStatusMesses
				.size();
	}

	@Override
	public Object getItem(int position) {
		return mCommendcardStatusMesses == null ? null
				: mCommendcardStatusMesses.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mCommendcardStatusMesses == null ? 0 : position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		List<ShopComcardStaPicsMess> itemList = ShopComcardStaPicsMess
				.parse(mCommendcardStatusMesses.get(position).getStatus_pics());
//		Log.i("dddd", itemList.size() + "");
		if (convertView == null) {
			switch (index) {
			case StoresDetailsNineActivity.REVIEW:
				
				break;

			default:
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_comemdcard_ninegridlayout, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.ivMore = (NineGridlayout) convertView
						.findViewById(R.id.iv_ngrid_layout);
				viewHolder.ivOne = (CustomImageView) convertView
						.findViewById(R.id.iv_oneimage);
				viewHolder.roundImage_commend=(RoundImageView) convertView.findViewById(R.id.roundImage_shop_comcard);
				viewHolder.shop_commend_name=(TextView) convertView.findViewById(R.id.shop_comcard_name);
				viewHolder.shop_commend_createtime=(TextView) convertView.findViewById(R.id.shop_comcard_createtime);
				viewHolder.shop_commend_chat=(TextView) convertView.findViewById(R.id.shop_comcard_chat);
				viewHolder.shop_commend_content=(TextView) convertView.findViewById(R.id.shop_comcard_content);
				convertView.setTag(viewHolder);
				break;
			}
			
		} else {
			switch (index) {
			case StoresDetailsNineActivity.REVIEW:
				viewHolder = (ViewHolder) convertView.getTag();
				break;
			default:
				viewHolder = (ViewHolder) convertView.getTag();
				break;
			}
		}
		switch (index) {
		case StoresDetailsNineActivity.REVIEW:
			
			break;
		default:
			ShopComcardStaUserMess mComcardStaUserMess=ShopComcardStaUserMess.parse(mCommendcardStatusMesses.get(position).getStatus_user());
			ImageLoderutils imageLoderutils=new ImageLoderutils(context);
			imageLoderutils.display(viewHolder.roundImage_commend,mComcardStaUserMess.getUser_icon());
			viewHolder.shop_commend_name.setText(mComcardStaUserMess.getUser_name());
			viewHolder.shop_commend_createtime.setText(mComcardStaUserMess.getUser_statusCreateTime());
			viewHolder.shop_commend_content.setText(mCommendcardStatusMesses.get(position).getStatus_content());
			break;
		}
		if (itemList == null || itemList.isEmpty()) {
			viewHolder.ivMore.setVisibility(View.GONE);
			viewHolder.ivOne.setVisibility(View.GONE);
		} else if (itemList.size() == 1) {
			viewHolder.ivMore.setVisibility(View.GONE);
			viewHolder.ivOne.setVisibility(View.VISIBLE);

			handlerOneImage(viewHolder, itemList.get(0).getThumbnail_pic());
		} else {
			viewHolder.ivMore.setVisibility(View.VISIBLE);
			viewHolder.ivOne.setVisibility(View.GONE);

			viewHolder.ivMore.setImagesData(itemList,60);
		}

		return convertView;
	}

	private void handlerOneImage(ViewHolder viewHolder, String url) {

	/*	int totalWidth;
		int imageWidth;
		int imageHeight;
		ScreenTools screentools = ScreenTools.instance(context);
		totalWidth = screentools.getScreenWidth() - screentools.dip2px(80);
		imageWidth = screentools.dip2px(image.getWidth());
		imageHeight = screentools.dip2px(image.getHeight());
		if (image.getWidth() <= image.getHeight()) {
			if (imageHeight > totalWidth) {
				imageHeight = totalWidth;
				imageWidth = (imageHeight * image.getWidth())
						/ image.getHeight();
			}
		} else {
			if (imageWidth > totalWidth) {
				imageWidth = totalWidth;
				imageHeight = (imageWidth * image.getHeight())
						/ image.getWidth();
			}
		}*/
		/*ViewGroup.LayoutParams layoutparams = viewHolder.ivOne
				.getLayoutParams();
		layoutparams.height = ViewGroup.LayoutParams.WRAP_CONTENT;;
		layoutparams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
		viewHolder.ivOne.setLayoutParams(layoutparams);*/

		viewHolder.ivOne.setClickable(true);
		viewHolder.ivOne
				.setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
//		viewHolder.ivOne.setImageUrl(url);
		
		 ImageLoderutils xUtilsImageLoader = new ImageLoderutils(context);
		 xUtilsImageLoader.setChange(true);
		 xUtilsImageLoader.setLeftAndRight(60);
		 xUtilsImageLoader.display(viewHolder.ivOne, url);
		 

	}

	class ViewHolder {
		public NineGridlayout ivMore;
		public CustomImageView ivOne;
		RoundImageView roundImage_commend;
		TextView shop_commend_name;
		TextView shop_commend_createtime;
		TextView shop_commend_chat;
		TextView shop_commend_content;
	}
}
