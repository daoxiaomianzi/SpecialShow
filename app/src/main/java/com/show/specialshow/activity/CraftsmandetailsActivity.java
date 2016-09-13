package com.show.specialshow.activity;

import java.io.Serializable;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.model.CraftsmanInfoMess;
import com.show.specialshow.model.CraftsmanIntroduceMess;
import com.show.specialshow.model.CraftsmanMess;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.ShopComcardStaPicsMess;
import com.show.specialshow.model.ShopPeopleMess;
import com.show.specialshow.model.ShopReviewMess;
import com.show.specialshow.model.ShopServiceMess;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.DensityUtil;
import com.show.specialshow.utils.ImageLoderutils;
import com.show.specialshow.utils.RoundImageView;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.view.MyListView;
import com.show.specialshow.view.NotifyingScrollView;
import com.show.specialshow.view.NotifyingScrollView.OnScrollChangedListener;

@SuppressLint("UseValueOf")
public class CraftsmandetailsActivity extends BaseActivity {
	private String user_id;
	private String shop_id;
	private UserMessage user;
	public static final int SHOP_FROM = 1;// 从商户来的
	public static final int SHOWER_FROM = 2;// 从秀客详情的名片信息来的
	public static final String WHERR_FROM = "whree_from";
	public static final String SERVICE_LIST = "service_list";
	public static final String PEOPLE_DES = "people_des";
	public static final String PEOPLE_LIST = "people_list";
	public static final int STAFF_REVIEW = 7;
	// 数据相关
	private CraftsmanMess craftsmanMess;//
	private CraftsmanInfoMess craftsmanInfoMess;
	private CraftsmanIntroduceMess craftsmanIntroduceMess;
	private List<ShopComcardStaPicsMess> craftsmanPicsMesses;// 作品图片
	private List<ShopReviewMess> craftsmanReviews;// 点评数据
	private List<ShopComcardStaPicsMess> mComcardStaPicsMess;// 点评数据的图片集合
	private int whree_from;
	private List<ShopPeopleMess> shopPeopleMesses;// 手艺人集合数据
	private List<ShopServiceMess> shopServiceMesses;// 服务集合数据
	private ShopPeopleMess shopPeopleMess;// 手艺人信息
	// 控件相关
	private RelativeLayout rll_craftsman_details_all;// 整个视图
	private ImageView crafstman_details_headbackground;// 头像背景
	private TextView crafstman_details_name;// 名字
	private TextView crafstman_details_job;// 职位
	private TextView craftsman_details_shops_located_tv;// 入住店铺
	private TextView craftman_details_moods;// 人气
	private TextView craftman_details_thumbup;// 点赞
	private TextView craftsman_details_working_time_tv;// 从业时间
	private TextView craftsman_details_good_at_tv;// 擅长
	private TextView craftsman_details_brief_introduction_tv;// 简介
	private TextView craftsman_details_head_tv;// TA的个人空间
	private RoundImageView craftssman_details_head_iv;// 个人空间头像
	private TextView craftsman_details_working_tv;// 作品
	private ImageView craftsman_details_working_iv1;// 作品图片1
	private ImageView craftsman_details_working_iv2;// 作品图片2
	private TextView craftsman_details_shower_review;// 秀友点评
	private MyListView craftsman_details_review_lv;//
	private RelativeLayout rll_craftsman_details_headbackground;
	private View craftsman_details_head_include;
	private NotifyingScrollView myScrollView;
	private TextView craftsman_details_clickKlike;// 点赞

	@SuppressWarnings("unchecked")
	@Override
	public void initData() {
		whree_from = getIntent().getExtras().getInt(WHERR_FROM);
		if (1 == whree_from) {
			shopPeopleMesses = (List<ShopPeopleMess>) getIntent()
					.getSerializableExtra(PEOPLE_LIST);
			shopServiceMesses = (List<ShopServiceMess>) getIntent()
					.getSerializableExtra(SERVICE_LIST);
			shopPeopleMess = (ShopPeopleMess) getIntent().getSerializableExtra(
					PEOPLE_DES);
			shop_id = getIntent().getStringExtra("shop_id");
		}
		user_id = getIntent().getStringExtra("user_id");
		user = TXApplication.getUserMess();
	}

	@Override
	public void initView() {
		setContentView(R.layout.activity_craftsman_details);
		myScrollView = (NotifyingScrollView) findViewById(R.id.myScrollView);
		craftsman_details_head_include = findViewById(R.id.craftsman_details_head_include);
		rll_craftsman_details_headbackground = (RelativeLayout) findViewById(R.id.rll_craftsman_details_headbackground);
		rll_craftsman_details_all = (RelativeLayout) findViewById(R.id.rll_craftsman_details_all);
		crafstman_details_headbackground = (ImageView) findViewById(R.id.crafstman_details_headbackground);
		crafstman_details_name = (TextView) findViewById(R.id.crafstman_details_name);
		crafstman_details_job = (TextView) findViewById(R.id.crafstman_details_job);
		craftsman_details_shops_located_tv = (TextView) findViewById(R.id.craftsman_details_shops_located_tv);
		craftman_details_moods = (TextView) findViewById(R.id.craftman_details_moods);
		craftman_details_thumbup = (TextView) findViewById(R.id.craftman_details_thumbup);
		craftsman_details_working_time_tv = (TextView) findViewById(R.id.craftsman_details_working_time_tv);
		craftsman_details_good_at_tv = (TextView) findViewById(R.id.craftsman_details_good_at_tv);
		craftsman_details_brief_introduction_tv = (TextView) findViewById(R.id.craftsman_details_brief_introduction_tv);
		craftsman_details_head_tv = (TextView) findViewById(R.id.craftsman_details_head_tv);
		craftssman_details_head_iv = (RoundImageView) findViewById(R.id.craftssman_details_head_iv);
		craftsman_details_working_tv = (TextView) findViewById(R.id.craftsman_details_working_tv);
		craftsman_details_working_iv1 = (ImageView) findViewById(R.id.craftsman_details_working_iv1);
		craftsman_details_working_iv2 = (ImageView) findViewById(R.id.craftsman_details_working_iv2);
		craftsman_details_shower_review = (TextView) findViewById(R.id.craftsman_details_shower_review);
		craftsman_details_review_lv = (MyListView) findViewById(R.id.craftsman_details_review_lv);
		craftsman_details_clickKlike = (TextView) findViewById(R.id.craftsman_details_clickKlike);
	}

	@Override
	public void fillView() {
		rll_craftsman_details_all.setVisibility(View.GONE);
		head_title_tv.setVisibility(View.GONE);
		titleChange();
		getDataView();
	}

	@Override
	public void setListener() {

	}

	@Override
	public void onClick(View v) {
		if(!BtnUtils.getInstance().isFastDoubleClick()){
			return;
		}
		Bundle bundle = new Bundle();
		switch (v.getId()) {
		case R.id.rll_craftsman_details_head:// 个人空间
			bundle.putString("user_id", user_id);
			UIHelper.startActivity(mContext, ShowerDetailsActivity.class,
					bundle);
			break;
		case R.id.rll_craftsman_details_shop:// 入住店铺
			bundle.putString("shop_id",
					craftsmanIntroduceMess.getCratsman_introduce_shopId());
			UIHelper.startActivity(mContext, StoresDetailsActivity.class,
					bundle);
			break;
		case R.id.rll_craftsman_details_working:// 作品
			bundle.putString("nick_name",
					craftsmanIntroduceMess.getCratsman_introduce_name());
			bundle.putSerializable("work_pics",
					(Serializable) craftsmanPicsMesses);
			UIHelper.startActivity(mContext, WorkActivity.class, bundle);
			break;
		case R.id.craftsman_details_working_iv1:
			bundle.putString(OneImageShowActivity.ONE_IMAGE_URL,
					craftsmanPicsMesses.get(0).getThumbnail_pic());
			UIHelper.startActivity(mContext, OneImageShowActivity.class, bundle);
			break;
		case R.id.craftsman_details_working_iv2:
			bundle.putString(OneImageShowActivity.ONE_IMAGE_URL,
					craftsmanPicsMesses.get(1).getThumbnail_pic());
			UIHelper.startActivity(mContext, OneImageShowActivity.class, bundle);
			break;
		case R.id.craftsman_details_clickKlike:// 点赞
			clickLike();
			break;
		case R.id.craftsman_deyails_appoinment:// 预约
			if (1 == whree_from) {
				bundle.putSerializable(PEOPLE_LIST,
						(Serializable) shopPeopleMesses);
				bundle.putInt(WHERR_FROM, SHOP_FROM);
				bundle.putSerializable(SERVICE_LIST,
						(Serializable) shopServiceMesses);
				bundle.putSerializable(PEOPLE_DES, shopPeopleMess);
				bundle.putString("shop_id", shop_id);
				UIHelper.startActivity(mContext, OrderActivity.class, bundle);
			} else {
				bundle.putSerializable(PEOPLE_DES, craftsmanIntroduceMess);
				bundle.putSerializable(SERVICE_LIST,
						(Serializable) shopServiceMesses);
				bundle.putInt(WHERR_FROM, SHOWER_FROM);
				UIHelper.startActivity(mContext, OrderActivity.class, bundle);
			}
			break;
		case R.id.craftsman_details_review:// 点评
			bundle.putInt("send_type", STAFF_REVIEW);
			bundle.putString("user_id", user_id);
			UIHelper.startActivity(mContext, SendCardActivity.class, bundle);
			break;
		default:
			break;
		}

	}

	/**
	 * 点赞
	 */
	private void clickLike() {
		RequestParams params = new RequestParams();
		String url = URLs.ATTENTION_STAFFHIT;
		params.addBodyParameter("hit", craftsmanIntroduceMess.getHit() + "");
		params.addBodyParameter("uid", user.getUid());
		params.addBodyParameter("staff_id", user_id);
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
						if (1 == result.getSuccess()) {
							switch (craftsmanIntroduceMess.getHit()) {
							case 1:
								craftsmanIntroduceMess.setHit(0);
								UIHelper.leftDrawable(R.drawable.icon_dot_no,mContext,craftsman_details_clickKlike);
								break;
							case 0:
								craftsmanIntroduceMess.setHit(1);
								UIHelper.leftDrawable(R.drawable.icon_dot_ok,mContext,craftsman_details_clickKlike);
								break;

							default:
								break;
							}
							UIHelper.ToastMessage(mContext, result.getMessage());
						} else {
							UIHelper.ToastMessage(mContext, result.getMessage());
						}
					}
				});
	}

	/**
	 * 获取数据
	 */
	private void getDataView() {
		RequestParams params = TXApplication.getParams();
		String url = URLs.APPOINTMENT_STAFF;
		params.addBodyParameter("staff_id", user_id);
		params.addBodyParameter("uid", user.getUid());
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
						if (null == result) {
							UIHelper.ToastMessage(mContext, "数据加载错误，请重试");
							return;
						}
						if (1 == result.getSuccess()) {
							craftsmanMess = CraftsmanMess.parse(result
									.getData());
							if (null != craftsmanMess) {
								craftsmanInfoMess = CraftsmanInfoMess
										.parse(craftsmanMess.getCratsman_info());
								craftsmanIntroduceMess = CraftsmanIntroduceMess
										.parse(craftsmanMess
												.getCratsman_introduce());
								shopServiceMesses = ShopServiceMess
										.parse(craftsmanMess.getService_info());
								craftsmanPicsMesses = ShopComcardStaPicsMess
										.parse(craftsmanMess.getCratsman_pics());
							}
							getShowerReview();
						} else {
							UIHelper.ToastMessage(mContext, "数据加载错误，请重试");
						}
					}
				});

	}

	/**
	 * 秀友点评数据
	 */
	private void getShowerReview() {
		RequestParams params = TXApplication.getParams();
		String url = URLs.APPOINMENT_LISTS;
		params.addBodyParameter("staff_id", user_id);
		TXApplication.post(null, mContext, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String mag) {
						UIHelper.ToastMessage(mContext, R.string.net_work_error);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						MessageResult result = MessageResult
								.parse(responseInfo.result);
						if (null == result) {
							UIHelper.ToastMessage(mContext, "数据加载错误，请重试!");
							return;
						}
						if (1 == result.getSuccess()) {
							if (null == result.getData()) {
							} else {
								craftsmanReviews = ShopReviewMess.parse(result
										.getData());
							}
							upDataView();
						} else {
							UIHelper.ToastMessage(mContext, "数据加载错误，请重试!");
							return;
						}
					}
				});
	}

	class ShowerReviewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return craftsmanReviews == null ? 0 : craftsmanReviews.size();
		}

		@Override
		public Object getItem(int position) {
			return null == craftsmanReviews ? null : craftsmanReviews
					.get(position);
		}

		@Override
		public long getItemId(int position) {
			return null == craftsmanReviews ? 0 : position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder vh = null;
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = View.inflate(mContext,
						R.layout.activity_craftsman_details_item, null);
				vh.craftsman_details_comment_roundiv = (RoundImageView) convertView
						.findViewById(R.id.craftsman_details_comment_roundiv);
				vh.craftsman_details_comment_time = (TextView) convertView
						.findViewById(R.id.craftsman_details_comment_time);
				vh.craftsman_details_comment_name = (TextView) convertView
						.findViewById(R.id.craftsman_details_comment_name);
				vh.craftsman_details_comment_picgv = (GridView) convertView
						.findViewById(R.id.craftsman_details_comment_picgv);
				vh.craftsman_details_comment_content = (TextView) convertView
						.findViewById(R.id.craftsman_details_comment_content);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			// ImageLoderutils imageLoderutils=new ImageLoderutils(mContext);
			// imageLoderutils.display(vh.craftsman_details_comment_roundiv,
			// craftsmanReviews.get(position).getComment_icon());
			ImageLoader.getInstance().displayImage(
					craftsmanReviews.get(position).getComment_icon(),
					vh.craftsman_details_comment_roundiv);
			vh.craftsman_details_comment_time.setText(craftsmanReviews.get(
					position).getComment_time());
			vh.craftsman_details_comment_name.setText(craftsmanReviews.get(
					position).getComment_name());
			vh.craftsman_details_comment_content.setText(craftsmanReviews.get(
					position).getComment_total());
			mComcardStaPicsMess = ShopComcardStaPicsMess.parse(craftsmanReviews
					.get(position).getStatus_pics());
			if (mComcardStaPicsMess != null && 4 == mComcardStaPicsMess.size()) {
				vh.craftsman_details_comment_picgv.setNumColumns(2);
			} else {
				vh.craftsman_details_comment_picgv.setNumColumns(3);
			}
			if (mComcardStaPicsMess != null && mComcardStaPicsMess.size() > 0) {
				vh.craftsman_details_comment_picgv.setVisibility(View.VISIBLE);
				vh.craftsman_details_comment_picgv
						.setAdapter(new CommmetgvAdapter(mComcardStaPicsMess));
				vh.craftsman_details_comment_picgv
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								Bundle bundle = new Bundle();
								bundle.putSerializable(
										ImagePagerActivity.EXTRA_IMAGE_URLS,
										(Serializable) ShopComcardStaPicsMess
												.parse(craftsmanReviews.get(
														position)
														.getStatus_pics()));
								bundle.putInt(
										ImagePagerActivity.EXTRA_IMAGE_INDEX,
										arg2);
								UIHelper.startActivity(mContext,
										ImagePagerActivity.class, bundle);
							}
						});
			} else {
				vh.craftsman_details_comment_picgv.setVisibility(View.GONE);
			}
			return convertView;

		}

		class ViewHolder {
			RoundImageView craftsman_details_comment_roundiv;// 头像
			TextView craftsman_details_comment_time;// 时间
			GridView craftsman_details_comment_picgv;// 图片
			TextView craftsman_details_comment_name;// 姓名
			TextView craftsman_details_comment_content;// 内容
		}
	}

	class CommmetgvAdapter extends BaseAdapter {
		private List<ShopComcardStaPicsMess> mComcardStaPicsMess;// 点评数据的图片集合

		public CommmetgvAdapter(List<ShopComcardStaPicsMess> mComcardStaPicsMess) {
			super();
			this.mComcardStaPicsMess = mComcardStaPicsMess;
		}

		@Override
		public int getCount() {
			return null == mComcardStaPicsMess ? 0 : mComcardStaPicsMess.size();
		}

		@Override
		public Object getItem(int position) {
			return null == mComcardStaPicsMess ? null : mComcardStaPicsMess
					.get(position);
		}

		@Override
		public long getItemId(int position) {
			return null == mComcardStaPicsMess ? 0 : position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			if (null == convertView) {
				vh = new ViewHolder();
				convertView = View.inflate(mContext,
						R.layout.activity_my_work_item, null);
				vh.shop_commend_iv = (ImageView) convertView
						.findViewById(R.id.my_work_item_iv);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			if (mComcardStaPicsMess != null && 4 == mComcardStaPicsMess.size()) {
				vh.shop_commend_iv.setLayoutParams(new LayoutParams(
						(TXApplication.WINDOW_WIDTH - DensityUtil.dip2px(
								mContext, 100)) / 2,
						(TXApplication.WINDOW_WIDTH - DensityUtil.dip2px(
								mContext, 100)) / 2));
			} else {
				vh.shop_commend_iv.setLayoutParams(new LayoutParams(
						(TXApplication.WINDOW_WIDTH - DensityUtil.dip2px(
								mContext, 110)) / 3,
						(TXApplication.WINDOW_WIDTH - DensityUtil.dip2px(
								mContext, 110)) / 3));
			}
			ImageLoader.getInstance().displayImage(
					mComcardStaPicsMess.get(position).getThumbnail_pic(),
					vh.shop_commend_iv);
			// }
			return convertView;
		}

		class ViewHolder {
			ImageView shop_commend_iv;
		}

	}

	/**
	 * 加载数据到视图
	 */
	private void upDataView() {
		if (null != craftsmanInfoMess) {
			ImageLoader.getInstance().displayImage(
					craftsmanInfoMess.getCratsman_info_icon(),
					craftssman_details_head_iv);
			craftsman_details_good_at_tv.setText(craftsmanInfoMess
					.getCratsman_info_good());
			craftsman_details_brief_introduction_tv.setText(craftsmanInfoMess
					.getCratsman_info_about());
			craftsman_details_head_tv.setText(craftsmanInfoMess
					.getCratsman_info_space());
			craftsman_details_working_time_tv.setText(craftsmanInfoMess
					.getCratsman_info_work_time());
		} else {
			craftsman_details_good_at_tv.setVisibility(View.GONE);
			craftsman_details_brief_introduction_tv.setVisibility(View.GONE);
			craftsman_details_head_tv.setVisibility(View.GONE);
			craftsman_details_working_time_tv.setVisibility(View.GONE);
		}
		if (null != craftsmanIntroduceMess) {
			craftman_details_moods.setText("人气 "+craftsmanIntroduceMess
					.getCratsman_introduce_hot());
			craftman_details_thumbup.setText("点赞 "+craftsmanIntroduceMess
					.getCratsman_introduce_fav());
			craftsman_details_shops_located_tv.setText(craftsmanIntroduceMess
					.getCratsman_introduce_shop());
			crafstman_details_job.setText(craftsmanIntroduceMess
					.getCratsman_introduce_job());
			ImageLoader.getInstance().displayImage(
					craftsmanIntroduceMess.getCratsman_introduce_bigIcon(),
					crafstman_details_headbackground);
			crafstman_details_name.setText(craftsmanIntroduceMess
					.getCratsman_introduce_name());
			if (0 == craftsmanIntroduceMess.getHit()) {
				UIHelper.leftDrawable(R.drawable.icon_dot_no,mContext,craftsman_details_clickKlike);
			} else if (1 == craftsmanIntroduceMess.getHit()) {
				UIHelper.leftDrawable(R.drawable.icon_dot_ok,mContext,craftsman_details_clickKlike);
			}
		} else {
			craftman_details_moods.setVisibility(View.GONE);
			craftman_details_thumbup.setVisibility(View.GONE);
			craftsman_details_shops_located_tv.setVisibility(View.GONE);
			crafstman_details_job.setVisibility(View.GONE);
			crafstman_details_name.setVisibility(View.GONE);
		}
		if (null == craftsmanPicsMesses || 0 == craftsmanPicsMesses.size()) {
			craftsman_details_working_iv1.setVisibility(View.GONE);
			craftsman_details_working_iv2.setVisibility(View.GONE);
		} else if (1 == craftsmanPicsMesses.size()) {
			craftsman_details_working_tv.setText("作品 （"
					+ craftsmanPicsMesses.size() + "）");
			craftsman_details_working_iv2.setVisibility(View.GONE);
			craftsman_details_working_iv1.setLayoutParams(new LayoutParams(
					(TXApplication.WINDOW_WIDTH - DensityUtil.dip2px(mContext,
							75)) / 2, (TXApplication.WINDOW_WIDTH - DensityUtil
							.dip2px(mContext, 75)) / 2));
			ImageLoader.getInstance().displayImage(
					craftsmanPicsMesses.get(0).getThumbnail_pic(),
					craftsman_details_working_iv1);
		} else {
			craftsman_details_working_tv.setText("作品（"
					+ craftsmanPicsMesses.size() + "）");
			craftsman_details_working_iv1.setLayoutParams(new LayoutParams(
					(TXApplication.WINDOW_WIDTH - DensityUtil.dip2px(mContext,
							75)) / 2, (TXApplication.WINDOW_WIDTH - DensityUtil
							.dip2px(mContext, 75)) / 2));
			craftsman_details_working_iv2.setLayoutParams(new LayoutParams(
					(TXApplication.WINDOW_WIDTH - DensityUtil.dip2px(mContext,
							75)) / 2, (TXApplication.WINDOW_WIDTH - DensityUtil
							.dip2px(mContext, 75)) / 2));
			ImageLoader.getInstance().displayImage(
					craftsmanPicsMesses.get(0).getThumbnail_pic(),
					craftsman_details_working_iv1);
			ImageLoader.getInstance().displayImage(
					craftsmanPicsMesses.get(1).getThumbnail_pic(),
					craftsman_details_working_iv2);
		}
		if (craftsmanReviews != null) {
			craftsman_details_shower_review.setText("秀友点评（"
					+ craftsmanReviews.size() + "）");
		}
		craftsman_details_review_lv.setAdapter(new ShowerReviewAdapter());
		rll_craftsman_details_all.setVisibility(View.VISIBLE);
	}

	/**
	 * 标题头的透明度变化
	 */
	private void titleChange() {
		craftsman_details_head_include.getBackground().setAlpha(0);
		myScrollView.setOnScrollChangedListener(new OnScrollChangedListener() {

			@SuppressLint("UseValueOf")
			@Override
			public void onScrollChanged(ScrollView who, int l, int t, int oldl,
					int oldt) {
				// 滑动改变标题栏的透明度和文字透明度，图标
				if (crafstman_details_headbackground == null) {
					return;
				}
				if (t < 0) {
					return;
				}
				int lHeight = rll_craftsman_details_headbackground.getHeight();
				if (t <= lHeight) {
					int progress = (int) (new Float(t) / new Float(lHeight) * 255);
					craftsman_details_head_include.getBackground().setAlpha(
							progress);
				} else {
					craftsman_details_head_include.getBackground()
							.setAlpha(255);
				}
			}
		});
	}

}
