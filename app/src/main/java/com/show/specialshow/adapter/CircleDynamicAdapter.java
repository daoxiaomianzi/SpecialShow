package com.show.specialshow.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.chatuidemo.utils.SmileUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.activity.CircleDynamicDetailActivity;
import com.show.specialshow.activity.DynamicImagePagerActivity;
import com.show.specialshow.activity.LoginActivity;
import com.show.specialshow.activity.ShowerDetailsActivity;
import com.show.specialshow.activity.StoresDetailsActivity;
import com.show.specialshow.model.CircleDynamicItem;
import com.show.specialshow.model.DynamicImage;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.ShopListTagsMess;
import com.show.specialshow.utils.RoundImageView;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.utils.XUtilsImageLoader;
import com.show.specialshow.view.MyGridView;
import com.show.specialshow.view.NoScrollGridView;

public class CircleDynamicAdapter extends BaseAdapter {
	private static final int JUST_TEXT = 0;
	private static final int ONE_PICTURE = 1;
	private static final int TWO_PICTURE = 2;
	private static final int OTHER_PICTURE = 3;
	private static final int TYPE_NUMBER = 4;
	private List<CircleDynamicItem> mList;
	private Context mContext;
	OnItemViewClickListener listener;
	public static ArrayList<String> cache_attention = new ArrayList<String>();

	public CircleDynamicAdapter(Context context, List<CircleDynamicItem> list) {
		this.mContext = context;
		this.mList = list;
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
	public int getViewTypeCount() {
		return TYPE_NUMBER;
	}

	@Override
	public int getItemViewType(int position) {
		CircleDynamicItem item_data = mList.get(position);
		switch (item_data.getStatus_pics().size()) {
		case 0:
			return JUST_TEXT;
		case 1:
			return ONE_PICTURE;
		case 2:
			return TWO_PICTURE;
		default:
			return OTHER_PICTURE;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		HolderJustText holder_just_text = null;
		HolderOnePicture holder_one_picture = null;
		HolderTwoPicture holder_two_picture = null;
		HolderOtherPicture holder_other_picture = null;

		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type) {
			case JUST_TEXT:
				convertView = View.inflate(mContext,
						R.layout.circle_dynamic_just_text_type, null);
				holder_just_text = initJustTextView(convertView, holder,
						position);
				holder_just_text.setPosition(position);
				convertView.setTag(holder_just_text);
				break;
			case ONE_PICTURE:
				convertView = View.inflate(mContext,
						R.layout.circle_dynamic_one_picture_type, null);
				holder_one_picture = initOnePictureView(convertView, holder,
						position);
				holder_one_picture.setPosition(position);
				convertView.setTag(holder_one_picture);
				break;
			case TWO_PICTURE:
				convertView = View.inflate(mContext,
						R.layout.circle_dynamic_two_picture_type, null);
				holder_two_picture = initTwoPictureView(convertView, holder,
						position);
				holder_two_picture.setPosition(position);
				convertView.setTag(holder_two_picture);
				break;
			case OTHER_PICTURE:
				convertView = View.inflate(mContext,
						R.layout.circle_dynamic_other_picture_type, null);
				holder_other_picture = initOtherPictureView(convertView,
						holder, position);
				holder_other_picture.setPosition(position);
				convertView.setTag(holder_other_picture);
				break;
			default:
				break;
			}
		} else {
			switch (type) {
			case JUST_TEXT:
				holder_just_text = (HolderJustText) convertView.getTag();
				holder_just_text.setPosition(position);
				break;
			case ONE_PICTURE:
				holder_one_picture = (HolderOnePicture) convertView.getTag();
				holder_one_picture.setPosition(position);
				break;
			case TWO_PICTURE:
				holder_two_picture = (HolderTwoPicture) convertView.getTag();
				holder_two_picture.setPosition(position);
				break;
			case OTHER_PICTURE:
				holder_other_picture = (HolderOtherPicture) convertView
						.getTag();
				holder_other_picture.setPosition(position);
				break;
			default:
				break;
			}
		}
		bindData(type, holder_just_text, holder_one_picture,
				holder_two_picture, holder_other_picture);

		return convertView;
	}

	private void bindData(int type, ViewHolder... holders) {
		CircleDynamicItem item = mList.get(holders[type].getPosition());
		holders[type].user_name.setText(item.getStatus_user().getUser_name());
		holders[type].send_time.setText(item.getStatus_user()
				.getUser_statusCreateTime());
		if(item.getStatus_package()==null){
			holders[type].card_ll.setVisibility(View.GONE);
		}else{
			holders[type].card_ll.setVisibility(View.VISIBLE);
			holders[type].describe.setText(item.getStatus_package().getPackage_title());
			holders[type].label.setText(item.getStatus_package().getPackage_what());
			if("秀卡".equals(item.getStatus_package().getPackage_what())){
				holders[type].user_shop_tv.setText("可用门店:");
			}else if("蹭卡".equals(item.getStatus_package().getPackage_what())){
				holders[type].user_shop_tv.setText("目标门店:");
			}
		}
		if(StringUtils.isEmpty(item.getStatus_content())){
			holders[type].relation_me.setVisibility(View.GONE);
		}else{
			holders[type].relation_me.setVisibility(View.VISIBLE);
			holders[type].relation_me.setText(SmileUtils.getSmiledText(mContext,item.getStatus_content()));
		}
		holders[type].comment_count.setText(item.getStatus_comment()+"");
		holders[type].thumbs_count.setText(item.getStatus_favor()+"");
		if(null==ShopListTagsMess.parse(item.getTags())||ShopListTagsMess.parse(item.getTags()).size()==0){
			holders[type].label_gv.setVisibility(View.GONE);
		}else{
			holders[type].label_gv.setVisibility(View.VISIBLE);
			holders[type].label_gv.setAdapter(new TagsMessAdapter(ShopListTagsMess.parse(item.getTags()), mContext));
		}
		XUtilsImageLoader imageLoader = new XUtilsImageLoader(mContext);
//		imageLoader.display(holders[type].portrait, item.getStatus_user()
//				.getUser_icon());
		ImageLoader.getInstance().displayImage(item.getStatus_user()
			.getUser_icon(), holders[type].portrait);
		listener = new OnItemViewClickListener(holders[type]);
		holders[type].attention.setOnClickListener(listener);
		holders[type].item.setOnClickListener(listener);
		holders[type].thumbs_count_ll.setOnClickListener(listener);
		holders[type].comment_count_ll.setOnClickListener(listener);
		holders[type].portrait.setOnClickListener(listener);
		holders[type].card_ll.setOnClickListener(listener);
		if(null!=item.getStatus_user()){
			if(null!=item.getStatus_user().getUser_id()){
		if (!item.getStatus_user().getUser_id()
				.equals(TXApplication.filename.getString("uid", ""))) {
			holders[type].attention.setVisibility(View.VISIBLE);
			if (item.getStatus_user().getAttention().equals("1")
					|| cache_attention.contains(item.getStatus_user()
							.getUser_id())) {
				holders[type].attention.setText("已关注");
				holders[type].attention.setEnabled(false);
				holders[type].attention.setSelected(true);
			} else {
				holders[type].attention.setText("+关注");
				holders[type].attention.setEnabled(true);
				holders[type].attention.setSelected(false);
			}
		} else {
			holders[type].attention.setVisibility(View.GONE);
		}
			}
		}
		switch (type) {
		case JUST_TEXT:
			break;
		case ONE_PICTURE:
			imageLoader.display(((HolderOnePicture) holders[type]).content_iv,
					item.getStatus_pics().get(0).getThumbnail_pic());
			((HolderOnePicture) holders[type]).content_iv
					.setOnClickListener(listener);
			break;
		case TWO_PICTURE:
			imageLoader.display(
					((HolderTwoPicture) holders[type]).content_one_iv, item
							.getStatus_pics().get(0).getThumbnail_pic());
			imageLoader.display(
					((HolderTwoPicture) holders[type]).content_two_iv, item
							.getStatus_pics().get(1).getThumbnail_pic());
			((HolderTwoPicture) holders[type]).content_one_iv
					.setOnClickListener(listener);
			((HolderTwoPicture) holders[type]).content_two_iv
					.setOnClickListener(listener);
			break;
		case OTHER_PICTURE:
			((HolderOtherPicture) holders[type]).content_gv
					.setSelector(new ColorDrawable(Color.TRANSPARENT));
			((HolderOtherPicture) holders[type]).content_gv
					.setAdapter(new DynamicGridAdapter(mContext, item
							.getStatus_pics()));
			break;
		default:
			break;
		}
	}

	private HolderOtherPicture initOtherPictureView(View convertView,
			ViewHolder holder, int position) {
		holder = new HolderOtherPicture();
		holder.portrait = (RoundImageView) convertView
				.findViewById(R.id.dynamic_type_other_picture_portrait_riv);
		holder.user_name = (TextView) convertView
				.findViewById(R.id.dynamic_type_other_picture_user_name_tv);
		holder.send_time = (TextView) convertView
				.findViewById(R.id.dynamic_type_other_picture_send_time_tv);
		holder.attention = (TextView) convertView
				.findViewById(R.id.dynamic_type_other_picture_attention_btn);
		holder.label = (TextView) convertView
				.findViewById(R.id.dynamic_type_other_picture_label_tv);
		holder.describe = (TextView) convertView
				.findViewById(R.id.dynamic_type_other_picture_describe_tv);
		holder.relation_me = (TextView) convertView
				.findViewById(R.id.dynamic_type_other_picture_relation_me_tv);
		holder.thumbs_count = (TextView) convertView
				.findViewById(R.id.dynamic_type_other_picture_thumbs_count_tv);
		holder.comment_count = (TextView) convertView
				.findViewById(R.id.dynamic_type_other_picture_comment_count_tv);
		((HolderOtherPicture) holder).content_gv = (NoScrollGridView) convertView
				.findViewById(R.id.dynamic_type_other_picture_content_gv);
		holder.item = (LinearLayout) convertView
				.findViewById(R.id.dynamic_item);
		holder.card_ll=(LinearLayout) convertView.findViewById(R.id.dynamic_type_other_picture_card_ll);
		holder.label_gv=(MyGridView) convertView.findViewById(R.id.dynamic_type_other_picture_label_gv);
		holder.thumbs_count_ll=(LinearLayout) convertView.findViewById(R.id.dynamic_type_other_picture_thumbs_count_ll);
		holder.user_shop_tv=(TextView) convertView.findViewById(R.id.dynamic_type_other_picture_user_shop_tv);
		holder.comment_count_ll=(LinearLayout) convertView.findViewById(R.id.dynamic_type_other_picture_comment_count_ll);
		return (HolderOtherPicture) holder;
	}

	private HolderTwoPicture initTwoPictureView(View convertView,
			ViewHolder holder, int position) {
		holder = new HolderTwoPicture();
		holder.portrait = (RoundImageView) convertView
				.findViewById(R.id.dynamic_type_two_picture_portrait_riv);
		holder.user_name = (TextView) convertView
				.findViewById(R.id.dynamic_type_two_picture_user_name_tv);
		holder.send_time = (TextView) convertView
				.findViewById(R.id.dynamic_type_two_picture_send_time_tv);
		holder.attention = (TextView) convertView
				.findViewById(R.id.dynamic_type_two_picture_attention_btn);
		holder.label = (TextView) convertView
				.findViewById(R.id.dynamic_type_two_picture_label_tv);
		holder.describe = (TextView) convertView
				.findViewById(R.id.dynamic_type_two_picture_describe_tv);
		holder.relation_me = (TextView) convertView
				.findViewById(R.id.dynamic_type_two_picture_relation_me_tv);
		holder.thumbs_count = (TextView) convertView
				.findViewById(R.id.dynamic_type_two_picture_thumbs_count_tv);
		holder.comment_count = (TextView) convertView
				.findViewById(R.id.dynamic_type_two_picture_comment_count_tv);
		((HolderTwoPicture) holder).content_one_iv = (ImageView) convertView
				.findViewById(R.id.dynamic_type_two_picture_content_one_iv);
		((HolderTwoPicture) holder).content_two_iv = (ImageView) convertView
				.findViewById(R.id.dynamic_type_two_picture_content_two_iv);
		holder.item = (LinearLayout) convertView
				.findViewById(R.id.dynamic_item);
		holder.card_ll=(LinearLayout) convertView.findViewById(R.id.dynamic_type_two_picture_card_ll);
		holder.label_gv=(MyGridView) convertView.findViewById(R.id.dynamic_type_two_picture_label_gv);
		holder.thumbs_count_ll=(LinearLayout) convertView.findViewById(R.id.dynamic_type_two_picture_thumbs_count_ll);
		holder.user_shop_tv=(TextView) convertView.findViewById(R.id.dynamic_type_two_picture_user_shop_tv);
		holder.comment_count_ll=(LinearLayout) convertView.findViewById(R.id.dynamic_type_two_picture_comment_count_ll);
		return (HolderTwoPicture) holder;
	}

	private HolderOnePicture initOnePictureView(View convertView,
			ViewHolder holder, int position) {
		holder = new HolderOnePicture();
		holder.portrait = (RoundImageView) convertView
				.findViewById(R.id.dynamic_type_one_picture_portrait_riv);
		holder.user_name = (TextView) convertView
				.findViewById(R.id.dynamic_type_one_picture_user_name_tv);
		holder.send_time = (TextView) convertView
				.findViewById(R.id.dynamic_type_one_picture_send_time_tv);
		holder.attention = (TextView) convertView
				.findViewById(R.id.dynamic_type_one_picture_attention_btn);
		holder.label = (TextView) convertView
				.findViewById(R.id.dynamic_type_one_picture_label_tv);
		holder.describe = (TextView) convertView
				.findViewById(R.id.dynamic_type_one_picture_describe_tv);
		holder.relation_me = (TextView) convertView
				.findViewById(R.id.dynamic_type_one_picture_relation_me_tv);
		holder.thumbs_count = (TextView) convertView
				.findViewById(R.id.dynamic_type_one_picture_thumbs_count_tv);
		holder.comment_count = (TextView) convertView
				.findViewById(R.id.dynamic_type_one_picture_comment_count_tv);
		((HolderOnePicture) holder).content_iv = (ImageView) convertView
				.findViewById(R.id.dynamic_type_one_picture_content_iv);
		holder.item = (LinearLayout) convertView
				.findViewById(R.id.dynamic_item);
		holder.card_ll=(LinearLayout) convertView.findViewById(R.id.dynamic_type_one_picture_card_ll);
		holder.label_gv=(MyGridView) convertView.findViewById(R.id.dynamic_type_one_picture_label_gv);
		holder.thumbs_count_ll=(LinearLayout) convertView.findViewById(R.id.dynamic_type_one_picture_thumbs_count_ll);
		holder.user_shop_tv=(TextView) convertView.findViewById(R.id.dynamic_type_one_picture_user_shop_tv);
		holder.comment_count_ll=(LinearLayout) convertView.findViewById(R.id.dynamic_type_one_picture_comment_count_ll);
		return (HolderOnePicture) holder;
	}

	private HolderJustText initJustTextView(View convertView,
			ViewHolder holder, int position) {
		holder = new HolderJustText();
		holder.portrait = (RoundImageView) convertView
				.findViewById(R.id.dynamic_type_just_text_portrait_riv);
		holder.user_name = (TextView) convertView
				.findViewById(R.id.dynamic_type_just_text_user_name_tv);
		holder.send_time = (TextView) convertView
				.findViewById(R.id.dynamic_type_just_text_send_time_tv);
		holder.attention = (TextView) convertView
				.findViewById(R.id.dynamic_type_just_text_attention_btn);
		holder.label = (TextView) convertView
				.findViewById(R.id.dynamic_type_just_text_label_tv);
		holder.describe = (TextView) convertView
				.findViewById(R.id.dynamic_type_just_text_describe_tv);
		holder.relation_me = (TextView) convertView
				.findViewById(R.id.dynamic_type_just_text_relation_me_tv);
		holder.thumbs_count = (TextView) convertView
				.findViewById(R.id.dynamic_type_just_text_thumbs_count_tv);
		holder.comment_count = (TextView) convertView
				.findViewById(R.id.dynamic_type_just_text_comment_count_tv);
		holder.item = (LinearLayout) convertView
				.findViewById(R.id.dynamic_item);
		holder.card_ll=(LinearLayout) convertView.findViewById(R.id.dynamic_type_just_text_card_ll);
		holder.label_gv=(MyGridView) convertView.findViewById(R.id.dynamic_type_just_text_label_gv);
		holder.thumbs_count_ll=(LinearLayout) convertView.findViewById(R.id.dynamic_type_just_text_thumbs_count_ll);
		holder.comment_count_ll=(LinearLayout) convertView.findViewById(R.id.dynamic_type_just_text_comment_count_ll);
		holder.user_shop_tv=(TextView) convertView.findViewById(R.id.dynamic_type_just_text_user_shop_tv);
		return (HolderJustText) holder;
	}

	private void attention(String attentid) {
		final String attent_user_id = attentid;
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
							cache_attention.add(attent_user_id);
							notifyDataSetChanged();
							break;

						default:
							UIHelper.ToastLogMessage(mContext,
									result.getMessage());
							break;
						}
					}
				});
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
			List<DynamicImage> list_pic = new ArrayList<DynamicImage>();
			switch (v.getId()) {
			case R.id.dynamic_type_just_text_attention_btn:
			case R.id.dynamic_type_one_picture_attention_btn:
			case R.id.dynamic_type_two_picture_attention_btn:
			case R.id.dynamic_type_other_picture_attention_btn:
				String userName = mList.get(holder.getPosition())
						.getStatus_user().getUser_name();
//				UIHelper.ToastLogMessage(mContext, "user_name" + userName);
				if(!TXApplication.login){
					bundle.putInt(LoginActivity.FROM_LOGIN, LoginActivity.FROM_OTHER);
					UIHelper.startActivity((Activity) mContext, LoginActivity.class,bundle);
				}else{
				attention(mList.get(holder.getPosition()).getStatus_user()
						.getUser_id());
				}
				break;
			case R.id.dynamic_type_one_picture_content_iv:
				bundle.putSerializable(
						DynamicImagePagerActivity.EXTRA_IMAGE_URLS,
						(Serializable) mList.get(holder.getPosition())
								.getStatus_pics());
				bundle.putInt(DynamicImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
				UIHelper.startActivity((Activity) mContext,
						DynamicImagePagerActivity.class, bundle);
				break;
			case R.id.dynamic_type_two_picture_content_one_iv:
				bundle.putSerializable(
						DynamicImagePagerActivity.EXTRA_IMAGE_URLS,
						(Serializable) mList.get(holder.getPosition())
								.getStatus_pics());
				bundle.putInt(DynamicImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
				UIHelper.startActivity((Activity) mContext,
						DynamicImagePagerActivity.class, bundle);
				break;
			case R.id.dynamic_type_two_picture_content_two_iv:
				bundle.putSerializable(
						DynamicImagePagerActivity.EXTRA_IMAGE_URLS,
						(Serializable) mList.get(holder.getPosition())
								.getStatus_pics());
				bundle.putInt(DynamicImagePagerActivity.EXTRA_IMAGE_INDEX, 1);
				UIHelper.startActivity((Activity) mContext,
						DynamicImagePagerActivity.class, bundle);
				break;
			case R.id.dynamic_item:
			case R.id.dynamic_type_just_text_thumbs_count_ll:
			case R.id.dynamic_type_just_text_comment_count_ll:
			case R.id.dynamic_type_one_picture_thumbs_count_ll:
			case R.id.dynamic_type_one_picture_comment_count_ll:
			case R.id.dynamic_type_two_picture_thumbs_count_ll:
			case R.id.dynamic_type_two_picture_comment_count_ll:
			case R.id.dynamic_type_other_picture_thumbs_count_ll:
			case R.id.dynamic_type_other_picture_comment_count_ll:
				toActivity(bundle);
				break;
			case R.id.dynamic_type_just_text_portrait_riv:
			case R.id.dynamic_type_one_picture_portrait_riv:
			case R.id.dynamic_type_two_picture_portrait_riv:
			case R.id.dynamic_type_other_picture_portrait_riv:
				bundle.putString("user_id", mList.get(holder.getPosition()).getStatus_user().getUser_id());
				UIHelper.startActivity((Activity) mContext, ShowerDetailsActivity.class,bundle);
				break;
			case R.id.dynamic_type_just_text_card_ll:
			case R.id.dynamic_type_one_picture_card_ll:
			case R.id.dynamic_type_two_picture_card_ll:
			case R.id.dynamic_type_other_picture_card_ll:
				bundle.putString("shop_id", mList.get(holder.getPosition()).getStatus_package().getShop_id());
				UIHelper.startActivity((Activity) mContext, StoresDetailsActivity.class,bundle);
				break;
			default:
				break;
			}
		}
		/**
		 * 界面跳转
		 * @param bundle
		 */
		private void toActivity(Bundle bundle) {
			CircleDynamicItem item = mList.get(holder.getPosition());
//			if(cache_attention.contains(item.getStatus_user()
//					.getUser_id())){
//				item.getStatus_user().setAttention("1");
//			}
			bundle.putString("idStr", item.getIdStr());
			UIHelper.startActivity((Activity) mContext,
					CircleDynamicDetailActivity.class, bundle);
		}
	}

	public class ViewHolder {
		protected RoundImageView portrait;
		protected TextView user_name;
		protected TextView send_time;
		protected TextView attention;
		protected TextView label;
		protected TextView describe;
		protected TextView relation_me;
		protected TextView thumbs_count;
		protected TextView comment_count;
		protected TextView user_shop_tv;
		protected LinearLayout item;
		protected LinearLayout thumbs_count_ll;
		protected LinearLayout comment_count_ll;
		protected LinearLayout card_ll;
		protected MyGridView label_gv;

		private int position;

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}
	}

	public class HolderJustText extends ViewHolder {

	}

	public class HolderOnePicture extends ViewHolder {
		ImageView content_iv;
	}

	public class HolderTwoPicture extends ViewHolder {
		ImageView content_one_iv;
		ImageView content_two_iv;
	}

	public class HolderOtherPicture extends ViewHolder {
		NoScrollGridView content_gv;
	}

}
