package com.show.specialshow.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.easemob.chatuidemo.adapter.ExpressionAdapter;
import com.easemob.chatuidemo.adapter.ExpressionPagerAdapter;
import com.easemob.chatuidemo.utils.SmileUtils;
import com.easemob.chatuidemo.widget.ExpandGridView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.adapter.DynamicGridAdapter;
import com.show.specialshow.adapter.ShowerFollerAdapter;
import com.show.specialshow.adapter.TagsMessAdapter;
import com.show.specialshow.model.CircleDynamicItem;
import com.show.specialshow.model.CommentMess;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.ShopListTagsMess;
import com.show.specialshow.model.ShopReviewMess;
import com.show.specialshow.model.ShowerFollowerCountMess;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.ImageLoderutils;
import com.show.specialshow.utils.RoundImageView;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.utils.XUtilsImageLoader;
import com.show.specialshow.view.MyGridView;
import com.show.specialshow.view.MyListView;
import com.show.specialshow.view.NoScrollGridView;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class  CircleDynamicDetailActivity extends BaseActivity {
    private MyListView detail_lv;
    private List<ShopReviewMess> mlist_test;// 评论数据
    private BaseAdapter mAdapter;
    private ScrollView detail_sv;
    private LinearLayout commentLinear;
    private LinearLayout bottomLinear;
    private EditText commentEdit;
    private CircleDynamicItem mCircleDynamic;

    private RoundImageView dynamic_type_portrait_riv;
    private TextView dynamic_type_user_name_tv;
    private TextView dynamic_type_send_time_tv;
    private TextView dynamic_type_attention_btn;
    private TextView dynamic_type_label_tv;
    private TextView dynamic_type_describe_tv;
    private TextView dynamic_type_relation_me_tv;
    private TextView dynamic_type_url_tv;
    private TextView dynamic_type_thumbs_count_tv;
    private TextView dynamic_type_comment_count_tv;
    private TextView dynamic_type_user_shop_tv;
    private LinearLayout circle_dynamic_type_include;
    private LinearLayout dynamic_type_card_ll;// 门店行
    private MyGridView dynamic_type_label_gv;// 标签
    private ImageView content_iv;// 一张图片
    private ImageView content_one_iv;
    private ImageView content_two_iv;
    private NoScrollGridView content_gv;
    private MyGridView dynamic_type_fav_gv;
    private TextView circle_dynamic_favs;// 赞数
    private TextView circle_dynamic_comments;// 评论数
    private RelativeLayout dynamic_type_fav_rll;//
    private View circle_dynamic_vi;//
    private Button commentButton;// 评论按钮
    //表情相关
    private ImageView iv_emoticons_normal;
    private ImageView iv_emoticons_checked;
    private LinearLayout ll_face_container;//表情vp
    private ViewPager vPager;
    private List<String> reslist;
    //接受的idStr
    private String idStr;
    private boolean isFrist = true;
    private boolean isFavs = true;//是否点击的是点赞
    private boolean isComments = true;//是否点击的是评论
    public static final String ACTION_NAME = "发送广播到动态页";
    public static final String ACTION_NAME_DETAIL = "发送广播到动态详情页";

    // 只有文本布局

    private InputMethodManager manager;

    @Override
    public void initData() {
        // for (int i = 0; i < 20; i++) {
        // mlist_test.add("评论测试" + i);
        // }
        mAdapter = new CommentDetailAdapter();
//		mCircleDynamic = (CircleDynamicItem) getIntent().getExtras()
//				.getSerializable("dynamic");
        idStr = getIntent().getExtras().getString("idStr");
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_circle_dynamic_detail);
        iv_emoticons_normal= (ImageView) findViewById(R.id.iv_emoticons_normal);
        iv_emoticons_checked= (ImageView) findViewById(R.id.iv_emoticons_checked);
        ll_face_container= (LinearLayout) findViewById(R.id.ll_face_container);
        vPager= (ViewPager) findViewById(R.id.vPager);
        detail_lv = (MyListView) findViewById(R.id.detail_lv);
        detail_sv = (ScrollView) findViewById(R.id.detail_sv);
        commentLinear = (LinearLayout) findViewById(R.id.commentLinear);
        bottomLinear = (LinearLayout) findViewById(R.id.bottomLinear);
        commentEdit = (EditText) findViewById(R.id.commentEdit);
        dynamic_type_fav_gv = (MyGridView) findViewById(R.id.shower_details_attention_gv);
        circle_dynamic_favs = (TextView) findViewById(R.id.circle_dynamic_favs);
        dynamic_type_fav_rll = (RelativeLayout) findViewById(R.id.shower_details_attention_rll);
        circle_dynamic_comments = (TextView) findViewById(R.id.circle_dynamic_comments);
        circle_dynamic_vi = findViewById(R.id.circle_dynamic_vi);
        commentButton = (Button) findViewById(R.id.commentButton);
        commentEdit = (EditText) findViewById(R.id.commentEdit);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // 表情list
        reslist = getExpressionRes(99);
        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        View gv3 = getGridChildView(3);
        View gv4 = getGridChildView(4);
        View gv5 = getGridChildView(5);
        views.add(gv1);
        views.add(gv2);
        views.add(gv3);
        views.add(gv4);
        views.add(gv5);
        vPager.setAdapter(new ExpressionPagerAdapter(views));
        isCanSend();
    }

    /**
     * 添加表情
     */
    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "ee_" + x;

            reslist.add(filename);

        }
        return reslist;

    }
    /**
     * 获取表情的gridview的子view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 1) {
            List<String> list1 = reslist.subList(0, 20);
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(reslist.subList(20, 40));
        }else if(i==3){

            list.addAll(reslist.subList(40, 60));
        }else if(i==4){
            list.addAll(reslist.subList(60, 80));

        }else if(i==5){
            list.addAll(reslist.subList(80,reslist.size()));

        }
        list.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this,
                1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    // 文字输入框可见时，才可输入表情
                    // 按住说话可见，不让输入表情

                        if (filename != "delete_expression") { // 不是删除键，显示表情
                            // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                            Class clz = Class
                                    .forName("com.easemob.chatuidemo.utils.SmileUtils");
                            Field field = clz.getField(filename);
                            commentEdit.append(SmileUtils.getSmiledText(
                                    mContext, (String) field.get(null)));
                        } else { // 删除文字或者表情
                            if (!TextUtils.isEmpty(commentEdit.getText())) {

                                int selectionStart = commentEdit
                                        .getSelectionStart();// 获取光标的位置
                                if (selectionStart > 0) {
                                    String body = commentEdit.getText()
                                            .toString();
                                    String tempStr = body.substring(0,
                                            selectionStart);
                                    int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                    if (i != -1) {
                                        CharSequence cs = tempStr.substring(i,
                                                selectionStart);
                                        if (SmileUtils.containsKey(cs
                                                .toString()))
                                            commentEdit.getEditableText()
                                                    .delete(i, selectionStart);
                                        else
                                            commentEdit.getEditableText()
                                                    .delete(selectionStart - 1,
                                                            selectionStart);
                                    } else {
                                        commentEdit.getEditableText()
                                                .delete(selectionStart - 1,
                                                        selectionStart);
                                    }
                                }
                            }

                        }
                } catch (Exception e) {
                }

            }
        });
        return view;
    }

    private void initTypeDynamic() {
        int type = mCircleDynamic.getStatus_pics().size();
        switch (type) {
            case 0:
                initJustText();
                break;
            case 1:
                initOnePicture();
                break;
            case 2:
                initTwoPicture();
                break;
            default:
                initOtherPicture();
                break;
        }
    }

    private void initOtherPicture() {
        dynamic_type_portrait_riv = (RoundImageView) findViewById(R.id.dynamic_type_other_picture_portrait_riv);
        dynamic_type_user_name_tv = (TextView) findViewById(R.id.dynamic_type_other_picture_user_name_tv);
        dynamic_type_send_time_tv = (TextView) findViewById(R.id.dynamic_type_other_picture_send_time_tv);
        dynamic_type_attention_btn = (TextView) findViewById(R.id.dynamic_type_other_picture_attention_btn);
        dynamic_type_label_tv = (TextView) findViewById(R.id.dynamic_type_other_picture_label_tv);
        dynamic_type_describe_tv = (TextView) findViewById(R.id.dynamic_type_other_picture_describe_tv);
        dynamic_type_relation_me_tv = (TextView) findViewById(R.id.dynamic_type_other_picture_relation_me_tv);
        dynamic_type_url_tv= (TextView) findViewById(R.id.dynamic_type_other_picture_url_tv);
        dynamic_type_thumbs_count_tv = (TextView) findViewById(R.id.dynamic_type_other_picture_thumbs_count_tv);
        dynamic_type_comment_count_tv = (TextView) findViewById(R.id.dynamic_type_other_picture_comment_count_tv);
        dynamic_type_card_ll = (LinearLayout) findViewById(R.id.dynamic_type_other_picture_card_ll);
        circle_dynamic_type_include = (LinearLayout) findViewById(R.id.circle_dynamic_other_picture_type_include);
        content_gv = (NoScrollGridView) findViewById(R.id.dynamic_type_other_picture_content_gv);
        dynamic_type_label_gv = (MyGridView) findViewById(R.id.dynamic_type_other_picture_label_gv);
        dynamic_type_user_shop_tv = (TextView) findViewById(R.id.dynamic_type_other_picture_user_shop_tv);
    }

    private void initTwoPicture() {
        dynamic_type_portrait_riv = (RoundImageView) findViewById(R.id.dynamic_type_two_picture_portrait_riv);
        dynamic_type_user_name_tv = (TextView) findViewById(R.id.dynamic_type_two_picture_user_name_tv);
        dynamic_type_send_time_tv = (TextView) findViewById(R.id.dynamic_type_two_picture_send_time_tv);
        dynamic_type_attention_btn = (TextView) findViewById(R.id.dynamic_type_two_picture_attention_btn);
        dynamic_type_label_tv = (TextView) findViewById(R.id.dynamic_type_two_picture_label_tv);
        dynamic_type_describe_tv = (TextView) findViewById(R.id.dynamic_type_two_picture_describe_tv);
        dynamic_type_relation_me_tv = (TextView) findViewById(R.id.dynamic_type_two_picture_relation_me_tv);
        dynamic_type_url_tv= (TextView) findViewById(R.id.dynamic_type_two_picture_url_tv);

        dynamic_type_thumbs_count_tv = (TextView) findViewById(R.id.dynamic_type_two_picture_thumbs_count_tv);
        dynamic_type_comment_count_tv = (TextView) findViewById(R.id.dynamic_type_two_picture_comment_count_tv);
        dynamic_type_card_ll = (LinearLayout) findViewById(R.id.dynamic_type_two_picture_card_ll);
        dynamic_type_label_gv = (MyGridView) findViewById(R.id.dynamic_type_two_picture_label_gv);
        content_one_iv = (ImageView) findViewById(R.id.dynamic_type_two_picture_content_one_iv);
        content_two_iv = (ImageView) findViewById(R.id.dynamic_type_two_picture_content_two_iv);
        dynamic_type_user_shop_tv = (TextView) findViewById(R.id.dynamic_type_two_picture_user_shop_tv);
        circle_dynamic_type_include = (LinearLayout) findViewById(R.id.circle_dynamic_two_picture_type_include);
    }

    private void initOnePicture() {
        dynamic_type_portrait_riv = (RoundImageView) findViewById(R.id.dynamic_type_one_picture_portrait_riv);
        dynamic_type_user_name_tv = (TextView) findViewById(R.id.dynamic_type_one_picture_user_name_tv);
        dynamic_type_send_time_tv = (TextView) findViewById(R.id.dynamic_type_one_picture_send_time_tv);
        dynamic_type_attention_btn = (TextView) findViewById(R.id.dynamic_type_one_picture_attention_btn);
        dynamic_type_label_tv = (TextView) findViewById(R.id.dynamic_type_one_picture_label_tv);
        dynamic_type_describe_tv = (TextView) findViewById(R.id.dynamic_type_one_picture_describe_tv);
        dynamic_type_relation_me_tv = (TextView) findViewById(R.id.dynamic_type_one_picture_relation_me_tv);
        dynamic_type_url_tv= (TextView) findViewById(R.id.dynamic_type_one_picture_url_tv);
        dynamic_type_thumbs_count_tv = (TextView) findViewById(R.id.dynamic_type_one_picture_thumbs_count_tv);
        dynamic_type_comment_count_tv = (TextView) findViewById(R.id.dynamic_type_one_picture_comment_count_tv);
        dynamic_type_card_ll = (LinearLayout) findViewById(R.id.dynamic_type_one_picture_card_ll);
        dynamic_type_label_gv = (MyGridView) findViewById(R.id.dynamic_type_one_picture_label_gv);
        content_iv = (ImageView) findViewById(R.id.dynamic_type_one_picture_content_iv);
        dynamic_type_user_shop_tv = (TextView) findViewById(R.id.dynamic_type_one_picture_user_shop_tv);
        circle_dynamic_type_include = (LinearLayout) findViewById(R.id.circle_dynamic_one_picture_type_include);
    }

    private void initJustText() {
        dynamic_type_portrait_riv = (RoundImageView) findViewById(R.id.dynamic_type_just_text_portrait_riv);
        dynamic_type_user_name_tv = (TextView) findViewById(R.id.dynamic_type_just_text_user_name_tv);
        dynamic_type_send_time_tv = (TextView) findViewById(R.id.dynamic_type_just_text_send_time_tv);
        dynamic_type_attention_btn = (TextView) findViewById(R.id.dynamic_type_just_text_attention_btn);
        dynamic_type_label_tv = (TextView) findViewById(R.id.dynamic_type_just_text_label_tv);
        dynamic_type_describe_tv = (TextView) findViewById(R.id.dynamic_type_just_text_describe_tv);
        dynamic_type_relation_me_tv = (TextView) findViewById(R.id.dynamic_type_just_text_relation_me_tv);
        dynamic_type_url_tv= (TextView) findViewById(R.id.dynamic_type_just_text_url_tv);
        dynamic_type_thumbs_count_tv = (TextView) findViewById(R.id.dynamic_type_just_text_thumbs_count_tv);
        dynamic_type_comment_count_tv = (TextView) findViewById(R.id.dynamic_type_just_text_comment_count_tv);
        dynamic_type_card_ll = (LinearLayout) findViewById(R.id.dynamic_type_just_text_card_ll);
        dynamic_type_label_gv = (MyGridView) findViewById(R.id.dynamic_type_just_text_label_gv);
        dynamic_type_user_shop_tv = (TextView) findViewById(R.id.dynamic_type_just_text_user_shop_tv);
        circle_dynamic_type_include = (LinearLayout) findViewById(R.id.circle_dynamic_just_text_type_include);
    }

    @Override
    public void fillView() {
        getData();
        detail_lv.setAdapter(mAdapter);
        detail_sv.smoothScrollTo(0, 0);
    }

    private void updateView() {
        XUtilsImageLoader imageLoader = new XUtilsImageLoader(mContext);
//		imageLoader.display(dynamic_type_portrait_riv, mCircleDynamic
//				.getStatus_user().getUser_icon());
        ImageLoader.getInstance().displayImage(mCircleDynamic
                .getStatus_user().getUser_icon(), dynamic_type_portrait_riv);
        dynamic_type_user_name_tv.setText(mCircleDynamic.getStatus_user()
                .getUser_name());
        dynamic_type_send_time_tv.setText(mCircleDynamic.getStatus_user()
                .getUser_statusCreateTime());
        dynamic_type_attention_btn
                .setVisibility(TXApplication.getUserMess().getUid()
                        .equals(mCircleDynamic.getStatus_user().getUser_id()) ? View.GONE
                        : View.VISIBLE);
        dynamic_type_attention_btn.setText(mCircleDynamic.getStatus_user()
                .getAttention().equals("1") ? "已关注" : "+关注");
        dynamic_type_attention_btn.setSelected(mCircleDynamic.getStatus_user()
                .getAttention().equals("1") ? true : false);
        if (mCircleDynamic.getStatus_package() == null) {
            dynamic_type_card_ll.setVisibility(View.GONE);
        } else {
            dynamic_type_card_ll.setVisibility(View.VISIBLE);
            dynamic_type_describe_tv.setText(mCircleDynamic.getStatus_package()
                    .getPackage_title());
            dynamic_type_label_tv.setText(mCircleDynamic.getStatus_package()
                    .getPackage_what());
            if ("秀卡".equals(mCircleDynamic.getStatus_package()
                    .getPackage_what())) {
                dynamic_type_user_shop_tv.setText("可用门店:");
            } else if ("蹭卡".equals(mCircleDynamic.getStatus_package()
                    .getPackage_what())) {
                dynamic_type_user_shop_tv.setText("目标门店:");
            }
        }
        if (StringUtils.isEmpty(mCircleDynamic.getStatus_content())) {
            dynamic_type_relation_me_tv.setVisibility(View.GONE);
        } else {
            dynamic_type_relation_me_tv.setVisibility(View.VISIBLE);
            dynamic_type_relation_me_tv.setText(SmileUtils.getSmiledText(mContext
            ,mCircleDynamic.getStatus_content()));
        }
        if (null == ShopListTagsMess.parse(mCircleDynamic.getTags())
                || ShopListTagsMess.parse(mCircleDynamic.getTags()).size() == 0) {
            dynamic_type_label_gv.setVisibility(View.GONE);
        } else {
            dynamic_type_label_gv.setVisibility(View.VISIBLE);
            dynamic_type_label_gv
                    .setAdapter(new TagsMessAdapter(ShopListTagsMess
                            .parse(mCircleDynamic.getTags()), mContext));
        }
        upDataImageView(imageLoader);
        dynamic_type_thumbs_count_tv.setText(mCircleDynamic.getStatus_favor()
                + "");
        switch (mCircleDynamic.getHit()) {
            case 1:
                UIHelper.leftDrawable(R.drawable.icon_like_ok, mContext, dynamic_type_thumbs_count_tv);
                break;
            case 2:
                UIHelper.leftDrawable(R.drawable.icon_like, mContext, dynamic_type_thumbs_count_tv);
                break;
            default:
                break;
        }
        dynamic_type_comment_count_tv.setText(mCircleDynamic
                .getStatus_comment() + "");
        circle_dynamic_type_include.setVisibility(View.VISIBLE);
    }

    /**
     * 加载图片
     *
     * @param imageLoader
     */
    private void upDataImageView(XUtilsImageLoader imageLoader) {
        final Bundle bundle = new Bundle();
        int type = mCircleDynamic.getStatus_pics().size();
        switch (type) {
            case 0:
                break;
            case 1:
                imageLoader.display(content_iv, mCircleDynamic.getStatus_pics()
                        .get(0).getThumbnail_pic());
                content_iv.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (!BtnUtils.getInstance().isFastDoubleClick()) {
                            return;
                        }
                        bundle.putSerializable(
                                DynamicImagePagerActivity.EXTRA_IMAGE_URLS,
                                (Serializable) mCircleDynamic.getStatus_pics());
                        bundle.putInt(DynamicImagePagerActivity.EXTRA_IMAGE_INDEX,
                                0);
                        UIHelper.startActivity((Activity) mContext,
                                DynamicImagePagerActivity.class, bundle);
                    }
                });
                break;
            case 2:
                imageLoader.display(content_one_iv, mCircleDynamic.getStatus_pics()
                        .get(0).getThumbnail_pic());
                imageLoader.display(content_two_iv, mCircleDynamic.getStatus_pics()
                        .get(1).getThumbnail_pic());
                content_one_iv.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (!BtnUtils.getInstance().isFastDoubleClick()) {
                            return;
                        }
                        bundle.putSerializable(
                                DynamicImagePagerActivity.EXTRA_IMAGE_URLS,
                                (Serializable) mCircleDynamic.getStatus_pics());
                        bundle.putInt(DynamicImagePagerActivity.EXTRA_IMAGE_INDEX,
                                0);
                        UIHelper.startActivity((Activity) mContext,
                                DynamicImagePagerActivity.class, bundle);
                    }
                });
                content_two_iv.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (!BtnUtils.getInstance().isFastDoubleClick()) {
                            return;
                        }
                        bundle.putSerializable(
                                DynamicImagePagerActivity.EXTRA_IMAGE_URLS,
                                (Serializable) mCircleDynamic.getStatus_pics());
                        bundle.putInt(DynamicImagePagerActivity.EXTRA_IMAGE_INDEX,
                                1);
                        UIHelper.startActivity((Activity) mContext,
                                DynamicImagePagerActivity.class, bundle);
                    }
                });
                break;
            default:
                content_gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
                content_gv.setAdapter(new DynamicGridAdapter(mContext,
                        mCircleDynamic.getStatus_pics()));
                break;
        }
    }

    @Override
    public void setListener() {
    }

    private void setOnclick() {
        dynamic_type_url_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BtnUtils.getInstance().isFastDoubleClick()) {
                    return;
                }
                UIHelper.ToastMessage(mContext,"这是一个链接");
            }
        });
        dynamic_type_portrait_riv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BtnUtils.getInstance().isFastDoubleClick()) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("user_id", mCircleDynamic.getStatus_user().getUser_id());
                UIHelper.startActivity(mContext, ShowerDetailsActivity.class, bundle);
            }
        });
        dynamic_type_card_ll.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!BtnUtils.getInstance().isFastDoubleClick()) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("shop_id", mCircleDynamic.getStatus_package().getShop_id());
                UIHelper.startActivity(mContext, StoresDetailsActivity.class, bundle);
            }
        });
        dynamic_type_attention_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!BtnUtils.getInstance().isFastDoubleClick()) {
                    return;
                }
                if (TXApplication.login) {
                    attention(mCircleDynamic.getStatus_user().getUser_id(), dynamic_type_attention_btn);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt(LoginActivity.FROM_LOGIN, LoginActivity.FROM_OTHER);
                    UIHelper.startActivity(mContext, LoginActivity.class, bundle);
                }
            }
        });
    }

    private void attention(String attentid, final TextView btn) {
        UserMessage user = TXApplication.getUserMess();
        RequestParams params = TXApplication.getParams();
        String url = URLs.ATTENTION_USER;
        String uid = user.getUid();
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
                                btn.setSelected(true);
                                break;
                            case 2:
                                UIHelper.ToastLogMessage(mContext,
                                        result.getMessage());
                                btn.setText("+关注");
                                btn.setSelected(false);
                                break;
                            default:
                                UIHelper.ToastLogMessage(mContext,
                                        result.getMessage());
                                break;
                        }
                    }
                });
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        if (!BtnUtils.getInstance().isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.input_common_tv:
                commentLinear.setVisibility(View.VISIBLE);
                bottomLinear.setVisibility(View.GONE);
                onFocusChange(true);
                break;
            case R.id.dynamic_type_just_text_thumbs_count_ll:// 点赞
                thumbUp();
                break;
            case R.id.dynamic_type_just_text_comment_count_ll:// 评论
                onFocusChange(true);
                break;
            case R.id.dynamic_type_one_picture_thumbs_count_ll:// 点赞
                thumbUp();
                break;
            case R.id.dynamic_type_one_picture_comment_count_ll:// 评论
                onFocusChange(true);
                break;
            case R.id.dynamic_type_two_picture_thumbs_count_ll:// 点赞
                thumbUp();
                break;
            case R.id.dynamic_type_two_picture_comment_count_ll:// 评论
                onFocusChange(true);
                break;
            case R.id.dynamic_type_other_picture_thumbs_count_ll:// 点赞
                thumbUp();
                break;
            case R.id.dynamic_type_other_picture_comment_count_ll:// 评论
                onFocusChange(true);
                break;
            case R.id.commentButton://评论发送
                comment();
                break;
            case R.id.iv_emoticons_normal://表情按钮
                ll_face_container.setVisibility(View.VISIBLE);
                iv_emoticons_normal.setVisibility(View.GONE);
                iv_emoticons_checked.setVisibility(View.VISIBLE);
                hideKeyboard();
                break;
            case R.id.iv_emoticons_checked://表情按钮
                ll_face_container.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.GONE);
                break;
            case R.id.commentEdit://输入框
                ll_face_container.setVisibility(View.GONE);
                iv_emoticons_checked.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }

    }
    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 评论
     */
    private void comment() {
        if (!TXApplication.login) {
            Bundle bundle = new Bundle();
            bundle.putInt(LoginActivity.FROM_LOGIN, LoginActivity.FROM_OTHER);
            UIHelper.startActivity(mContext, LoginActivity.class, bundle);
        } else {
            RequestParams params = TXApplication.getParams();
            String idStr = mCircleDynamic.getIdStr();
            String uid = TXApplication.getUserMess().getUid();
            String content = commentEdit.getText().toString().trim();
            String url = URLs.ATTENTION_COMMENT;
            params.addBodyParameter("idStr", idStr);
            params.addBodyParameter("uid", uid);
            params.addBodyParameter("content", content);
            TXApplication.post(null, mContext, url, params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onStart() {
                            super.onStart();
                            loadIng("发送中", true);
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            UIHelper.ToastMessage(mContext,
                                    R.string.net_work_error);
                            dialog.dismiss();
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            MessageResult result = MessageResult
                                    .parse(responseInfo.result);
                            if (null == result) {
                                dialog.dismiss();
                                return;
                            }
                            if (1 == result.getSuccess()) {
                                dialog.dismiss();
                                UIHelper.ToastMessage(mContext,
                                        result.getMessage());
                                ll_face_container.setVisibility(View.GONE);
                                iv_emoticons_normal.setVisibility(View.VISIBLE);
                                iv_emoticons_checked.setVisibility(View.GONE);
                                detail_sv.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        detail_sv.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                });
                                commentEdit.setText("");
                                isFrist = false;
                                isComments = true;
                                isFavs = false;
                                getData();
                            }

                        }
                    });
        }
    }

    /**
     * 点赞
     */
    private void thumbUp() {
        if (!TXApplication.login) {
            Bundle bundle = new Bundle();
            bundle.putInt(LoginActivity.FROM_LOGIN, LoginActivity.FROM_OTHER);
            UIHelper.startActivity(mContext, LoginActivity.class, bundle);
        } else {
            RequestParams params = TXApplication.getParams();
            String idStr = mCircleDynamic.getIdStr();
            String uid = TXApplication.getUserMess().getUid();
            final int hit = mCircleDynamic.getHit();
            String url = URLs.ATTENTION_HIT;
            params.addBodyParameter("idStr", idStr);
            params.addBodyParameter("uid", uid);
            params.addBodyParameter("hit", hit + "");
            TXApplication.post(null, mContext, url, params,
                    new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            UIHelper.ToastMessage(mContext,
                                    R.string.net_work_error);
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            MessageResult result = MessageResult
                                    .parse(responseInfo.result);
                            if (null == result) {
                                return;
                            }
                            if (1 == result.getSuccess()) {
                                thumbUpSuccess(hit);
                            }

                        }
                    });
        }
    }

    /**
     * 点赞操作成功
     *
     * @param hit
     */
    private void thumbUpSuccess(int hit) {
        switch (hit) {
            case 1:
                isFavs = true;
                isComments = false;
                isFrist = false;
                UIHelper.leftDrawable(R.drawable.icon_like, mContext, dynamic_type_thumbs_count_tv);
                getData();
                mCircleDynamic.setHit(2);
                break;
            case 2:
                isFavs = true;
                isComments = false;
                isFrist = false;
                UIHelper.leftDrawable(R.drawable.icon_like_ok, mContext, dynamic_type_thumbs_count_tv);
                getData();
                mCircleDynamic.setHit(1);
                break;

            default:
                break;
        }
    }

    protected void getData() {
        RequestParams params = TXApplication.getParams();
//		String idStr = mCircleDynamic.getIdStr();
        String url = URLs.GET_DYNAMIC_COMMENT;
        params.addBodyParameter("idStr", idStr);
        String uid = TXApplication.filename.getString("uid", "");
        params.addBodyParameter("login_uid", uid);
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
                                String info = result.getData();
                                if (info != null) {
                                    CommentMess comMess = CommentMess.parse(info);// 评论，点赞
                                    mCircleDynamic = CircleDynamicItem.parse(comMess.getUserInfo());
                                    initTypeDynamic();
                                    getDataSuccess(info, comMess);
                                    if (isFrist) {
                                        updateView();
                                        setOnclick();
                                    }
                                }

                                break;
                            default:
                                UIHelper.ToastLogMessage(mContext,
                                        result.getMessage());
                                break;
                        }
                    }
                });
    }

    /**
     * 获取数据成功
     *
     * @param info
     * @param comMess
     */
    private void getDataSuccess(String info, CommentMess comMess) {

        if (null != comMess) {
            if (isFavs) {
                List<ShowerFollowerCountMess> favList = ShowerFollowerCountMess
                        .parse(comMess.getStatus_fav());
                ShowerFollerAdapter sFollerAdapter = new ShowerFollerAdapter(
                        mContext, favList, 3);
                dynamic_type_fav_gv.setAdapter(sFollerAdapter);
                if (favList == null || favList.isEmpty()) {
                    dynamic_type_fav_rll.setVisibility(View.GONE);
//				circle_dynamic_favs.setVisibility(View.GONE);
                    dynamic_type_thumbs_count_tv.setText("0");
                } else {
                    circle_dynamic_vi.setVisibility(View.VISIBLE);
                    dynamic_type_fav_rll.setVisibility(View.VISIBLE);
//				circle_dynamic_favs.setVisibility(View.VISIBLE);
//				circle_dynamic_favs.setText("赞" + favList.size());
                    dynamic_type_thumbs_count_tv.setText(favList.size() + "");
                }
            }
            if (isComments) {
                mlist_test = ShopReviewMess.parse(comMess.getStatus_comment());
                mAdapter.notifyDataSetChanged();
                if (mlist_test == null || mlist_test.isEmpty()) {
                    circle_dynamic_vi.setVisibility(View.GONE);
//				circle_dynamic_comments.setVisibility(View.GONE);
                    // circle_dynamic_comments.setText("评论0");;
                    dynamic_type_comment_count_tv.setText("0");
                } else {
//				circle_dynamic_comments.setVisibility(View.VISIBLE);
//				circle_dynamic_comments.setText("评论" + mlist_test.size());
                    dynamic_type_comment_count_tv.setText(mlist_test.size() + "");
                }
            }
        } else {
            List<ShowerFollowerCountMess> favList = null;
            ShowerFollerAdapter sFollerAdapter = new ShowerFollerAdapter(
                    mContext, favList, 3);
            dynamic_type_fav_gv.setAdapter(sFollerAdapter);
            dynamic_type_fav_rll.setVisibility(View.GONE);
            circle_dynamic_favs.setVisibility(View.GONE);
            dynamic_type_thumbs_count_tv.setText("0");
            dynamic_type_comment_count_tv.setText("0");
        }
    }

    /**
     * 判断是否可以发送
     */
    private void isCanSend() {
        commentEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    commentButton.setEnabled(false);
                } else {
                    commentButton.setEnabled(true);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    public void goBack(View v) {
        switch (v.getId()) {
            case R.id.circle_dynamic_details_back:
                hideKeyboard();
                finish();
                onBack();
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(ll_face_container.getVisibility()==View.VISIBLE){
            ll_face_container.setVisibility(View.GONE);
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
            onBack();
        }
    }

    private void onBack() {
        Intent mIntent = new Intent(ACTION_NAME_DETAIL);
        // 发送广播
        sendBroadcast(mIntent);
    }

    /**
     * 显示或隐藏输入法
     */
    private void onFocusChange(boolean hasFocus) {
        final boolean isFocus = hasFocus;
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager) commentEdit
                        .getContext().getSystemService(INPUT_METHOD_SERVICE);
                if (isFocus) {
                    // 显示输入法
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    // 隐藏输入法
                    imm.hideSoftInputFromWindow(commentEdit.getWindowToken(), 0);
                }
            }
        }, 100);
    }

    class CommentDetailAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mlist_test == null) {
                return 0;
            }
            return mlist_test.size();
        }

        @Override
        public Object getItem(int position) {
            if (mlist_test == null) {
                return null;
            }
            return mlist_test.get(position);
        }

        @Override
        public long getItemId(int position) {
            if (null == mlist_test) {
                return 0;
            }
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = View.inflate(mContext,
                        R.layout.view_dynamic_detail_comment_item, null);
                vh.craftsman_details_comment_roundiv = (RoundImageView) convertView
                        .findViewById(R.id.craftsman_details_comment_roundiv);
                vh.craftsman_details_comment_time = (TextView) convertView
                        .findViewById(R.id.craftsman_details_comment_time);
                vh.craftsman_details_comment_name = (TextView) convertView
                        .findViewById(R.id.craftsman_details_comment_name);
                vh.craftsman_details_comment_content = (TextView) convertView
                        .findViewById(R.id.craftsman_details_comment_content);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            ImageLoderutils imageLoderutils = new ImageLoderutils(mContext);
            imageLoderutils.display(vh.craftsman_details_comment_roundiv,
                    mlist_test.get(position).getComment_icon());
            vh.craftsman_details_comment_time.setText(mlist_test.get(position)
                    .getComment_time());
            vh.craftsman_details_comment_name.setText(mlist_test.get(position)
                    .getComment_name() + ":");
            Spannable spannable = SmileUtils.getSmiledText(mContext,mlist_test.get(
                    position).getComment_total());
            vh.craftsman_details_comment_content.setText(spannable);
            return convertView;

        }

        class ViewHolder {
            RoundImageView craftsman_details_comment_roundiv;// 头像
            TextView craftsman_details_comment_time;// 时间
            TextView craftsman_details_comment_name;// 姓名
            TextView craftsman_details_comment_content;// 内容
        }
    }

}
