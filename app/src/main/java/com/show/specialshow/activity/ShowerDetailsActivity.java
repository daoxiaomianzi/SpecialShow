package com.show.specialshow.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chatuidemo.activity.ChatActivity;
import com.easemob.chatuidemo.utils.SmileUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.adapter.ShowerFollerAdapter;
import com.show.specialshow.adapter.TagsMessAdapter;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.model.ShopComcardStaPicsMess;
import com.show.specialshow.model.ShopListTagsMess;
import com.show.specialshow.model.ShowerCardMess;
import com.show.specialshow.model.ShowerCommedMess;
import com.show.specialshow.model.ShowerFollowerCountMess;
import com.show.specialshow.model.ShowerFollowerMess;
import com.show.specialshow.model.ShowerInfoMess;
import com.show.specialshow.model.ShowerMess;
import com.show.specialshow.model.UserMessage;
import com.show.specialshow.utils.BtnUtils;
import com.show.specialshow.utils.DensityUtil;
import com.show.specialshow.utils.ImmersedStatusbarUtils;
import com.show.specialshow.utils.RoundImageView;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.UIHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class ShowerDetailsActivity extends BaseActivity {
    private View shower_details_head;// 头部视图
    private View shower_details_head2;// 头部视图
    public static final String ACTION_NAME = "发送广播";
    public static final String ATTEN_FANS = "atten_fans";
    public static final String TITLE = "title";
    // 相关控件
    private View shower_details_head_include;//导航栏
    private View shower_details_head_include_2;//导航栏2
    private LinearLayout shower_details_content;// 下面视图
    private ListView shower_details_shopcard_lv;
    private TextView shower_details_chat;// 聊天按钮
    private TextView shower_details_sex_age;//年龄性别
    private TextView shower_details_xinzuo;//星座
    private RoundImageView shower_details_roundImageView;// 头像
    private TextView shower_details_attention_btn;// 关注
    private TextView shower_details_nickname_tv;// 昵称
    private TextView shower_details_often_in_tv;// 常居地
    private TextView shower_details_signature_tv;// 个性签名
    private GridView shower_details_interested_sign_gv;// 兴趣标签
    private TextView shower_details_attention_tv;// 关注数
    private TextView shower_details_often_in;
    private LinearLayout shower_details_crafstman_ll;// 手艺人视图
    private TextView shower_details_fans_tv;// 粉丝数
    private GridView shower_details_attention_gv;// 关注头像列表
    private GridView shower_details_fans_gv;// 粉丝头像列表
    private RadioButton shower_details_showcard;// 秀卡
    private RadioButton shower_details_cengcard;// 蹭卡
    private RadioButton shower_details_dynamic;// 动态
    private RadioButton shower_details_showcard_hover;//
    private RadioButton shower_details_cengcard_hover;//
    private RadioButton shower_details_dynamic_hover;//
    private LinearLayout shower_details_rg;//
    private RelativeLayout shower_details_attention_rll;
    private RelativeLayout shower_details_fans_rll;//
    // 此秀客是手艺人的信息
    private RoundImageView shower_details_crafstman_roundImageView;// 头像
    private TextView shower_detaisls_crafstman_nickname_tv;// 昵称
    private TextView shower_details_crafstman_position_tv;// 职位
    private TextView shower_detaisls_crafstman_moods;// 人气
    private TextView shower_detaisls_crafstman_thumbup;// 点赞
    private TextView shower_details_shops_located_tv;// 入住的店铺
    private int user_biaoshi;// 标识
    private int attention;// 是否关注
    // 数据相关
    private ShowerMess showerMess;//
    private ShowerInfoMess showerInfoMess;
    private ShowerCardMess showerCardMess;
    private ShowerFollowerMess showerFollowerMess;
    private List<ShopListTagsMess> mTagsMesses = new ArrayList<ShopListTagsMess>();// 标签数据
    private List<ShowerFollowerCountMess> showerFollowerAttenMesses;// 关注的人
    private List<ShowerFollowerCountMess> showerFollowerFansMesses;// 粉丝
    private static final int SHOW_CARD = 1;
    private static final int CENG_CARD = 2;
    private static final int DYNAMIC = 3;
    private UserMessage user;// 用户对象
    private List<ShowerCommedMess> showerCommedMesses = new ArrayList<ShowerCommedMess>();
    private ShowerCommedAdapter adapter;
    private List<ShopComcardStaPicsMess> mComcardStaPicsMess;
    private String user_id;

    @Override
    public void initData() {
        user_id = getIntent().getExtras().getString("user_id");
        setContentView(R.layout.activity_shower_details);
        shower_details_head = View.inflate(mContext,
                R.layout.activity_shower_details_head, null);
        shower_details_head2 = View.inflate(mContext,
                R.layout.activity_shower_details_head2, null);
        shower_details_shopcard_lv = (ListView) findViewById(R.id.shower_details_shopcard_lv);
        shower_details_shopcard_lv.addHeaderView(shower_details_head);
        shower_details_shopcard_lv.addHeaderView(shower_details_head2);
    }

    @Override
    public void initView() {
        shower_details_head_include = findViewById(R.id.shower_details_head);
        shower_details_head_include_2 = shower_details_head.findViewById(R.id.shower_details_head_2);
        shower_details_head.findViewById(R.id.head_title_tv).setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ImmersedStatusbarUtils.initAfterSetContentView(mContext, shower_details_head_include_2);
        }
        shower_details_head_include_2.getBackground().setAlpha(0);
        shower_details_rg = (LinearLayout) findViewById(R.id.shower_details_rg);
        shower_details_showcard_hover = (RadioButton) findViewById(R.id.shower_details_showcard_hover);
        shower_details_cengcard_hover = (RadioButton) findViewById(R.id.shower_details_cengcard_hover);
        shower_details_dynamic_hover = (RadioButton) findViewById(R.id.shower_details_dynamic_hover);
        shower_details_chat = (TextView) findViewById(R.id.shower_details_chat);
        shower_details_roundImageView = (RoundImageView) shower_details_head
                .findViewById(R.id.shower_details_roundImageView);
        shower_details_sex_age = (TextView) shower_details_head.findViewById(R.id.shower_details_sex_age);
        shower_details_xinzuo = (TextView) shower_details_head.findViewById(R.id.shower_details_xinzuo);
        shower_details_attention_btn = (TextView) shower_details_head
                .findViewById(R.id.shower_details_attention_btn);
        shower_details_nickname_tv = (TextView) shower_details_head
                .findViewById(R.id.shower_details_nickname_tv);
        shower_details_often_in_tv = (TextView) shower_details_head
                .findViewById(R.id.shower_details_often_in_tv);
        shower_details_signature_tv = (TextView) shower_details_head
                .findViewById(R.id.shower_details_signature_tv);
        shower_details_interested_sign_gv = (GridView) shower_details_head
                .findViewById(R.id.shower_details_interested_sign_gv);
        shower_details_crafstman_roundImageView = (RoundImageView) shower_details_head
                .findViewById(R.id.shower_details_crafstman_roundImageView);
        shower_detaisls_crafstman_nickname_tv = (TextView) shower_details_head
                .findViewById(R.id.shower_detaisls_crafstman_nickname_tv);
        shower_details_crafstman_position_tv = (TextView) shower_details_head
                .findViewById(R.id.shower_details_crafstman_position_tv);
        shower_detaisls_crafstman_moods = (TextView) shower_details_head
                .findViewById(R.id.shower_detaisls_crafstman_moods);
        shower_detaisls_crafstman_thumbup = (TextView) shower_details_head
                .findViewById(R.id.shower_detaisls_crafstman_thumbup);
        shower_details_shops_located_tv = (TextView) shower_details_head
                .findViewById(R.id.shower_details_shops_located_tv);
        shower_details_attention_tv = (TextView) shower_details_head
                .findViewById(R.id.shower_details_attention_tv);
        shower_details_fans_tv = (TextView) shower_details_head
                .findViewById(R.id.shower_details_fans_tv);
        shower_details_content = (LinearLayout) shower_details_head
                .findViewById(R.id.shower_details_content);
        shower_details_attention_rll = (RelativeLayout) shower_details_head
                .findViewById(R.id.shower_details_attention_rll);
        shower_details_fans_rll = (RelativeLayout) shower_details_head
                .findViewById(R.id.shower_details_fans_rll);
        shower_details_attention_gv = (GridView) shower_details_head
                .findViewById(R.id.shower_details_attention_gv);
        shower_details_fans_gv = (GridView) shower_details_head
                .findViewById(R.id.shower_details_fans_gv);
        shower_details_showcard = (RadioButton) shower_details_head2
                .findViewById(R.id.shower_details_showcard);
        shower_details_cengcard = (RadioButton) shower_details_head2
                .findViewById(R.id.shower_details_cengcard);
        shower_details_dynamic = (RadioButton) shower_details_head2
                .findViewById(R.id.shower_details_dynamic);
        shower_details_crafstman_ll = (LinearLayout) shower_details_head
                .findViewById(R.id.shower_details_crafstman_ll);
        user = TXApplication.getUserMess();
        shower_details_often_in = (TextView) shower_details_head
                .findViewById(R.id.shower_details_often_in);
    }

    @Override
    public void fillView() {
        goneView();
        getViewData();
    }


    // 隐藏
    private void goneView() {
        head_title_tv.setVisibility(View.GONE);
        shower_details_roundImageView.setVisibility(View.GONE);
        shower_details_nickname_tv.setVisibility(View.GONE);
        shower_details_often_in_tv.setVisibility(View.GONE);
        shower_details_often_in.setVisibility(View.GONE);
        shower_details_attention_btn.setVisibility(View.GONE);
        shower_details_content.setVisibility(View.GONE);
    }

    @Override
    public void setListener() {
        shower_details_attention_gv
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Bundle bundle = new Bundle();
                        bundle.putString("user_id", showerFollowerAttenMesses
                                .get(position).getFav_id());
                        UIHelper.startActivity(mContext,
                                ShowerDetailsActivity.class, bundle);
                    }
                });
        shower_details_fans_gv
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Bundle bundle = new Bundle();
                        bundle.putString("user_id", showerFollowerFansMesses
                                .get(position).getFav_id());
                        UIHelper.startActivity(mContext,
                                ShowerDetailsActivity.class, bundle);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (!BtnUtils.getInstance().isFastDoubleClick()) {
            return;
        }
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.shower_details_showcard:// 秀卡
                chaneTab(SHOW_CARD);
                break;
            case R.id.shower_details_cengcard:// 蹭卡
                chaneTab(CENG_CARD);
                break;
            case R.id.shower_details_dynamic:// 动态
                chaneTab(DYNAMIC);
                break;
            case R.id.shower_details_showcard_hover:// 秀卡
                chaneTab(SHOW_CARD);
                break;
            case R.id.shower_details_cengcard_hover:// 蹭卡
                chaneTab(CENG_CARD);
                break;
            case R.id.shower_details_dynamic_hover:// 动态
                chaneTab(DYNAMIC);
                break;
            case R.id.shower_details_attention_rll:// 关注的人
                bundle.putSerializable(ATTEN_FANS,
                        (Serializable) showerFollowerAttenMesses);
                bundle.putString(TITLE, "关注的人");
                UIHelper.startActivity(mContext, AttenFansPeoActivity.class, bundle);
                break;
            case R.id.shower_details_fans_rll:// TA的粉丝
                bundle.putSerializable(ATTEN_FANS,
                        (Serializable) showerFollowerFansMesses);
                bundle.putString(TITLE, "TA的粉丝");
                UIHelper.startActivity(mContext, AttenFansPeoActivity.class, bundle);
                break;
            case R.id.shower_details_attention_btn:// 关注
                if (TXApplication.login) {
                    attention(user_id, shower_details_attention_btn);
                } else {
                    bundle.putInt(LoginActivity.FROM_LOGIN, LoginActivity.FROM_OTHER);
                    UIHelper.startActivity(mContext, LoginActivity.class, bundle);
                }
                break;
            case R.id.shower_details_crafstman_rll:// 名片信息内容
                bundle.putString("user_id", user_id);
                UIHelper.startActivity(mContext, CraftsmandetailsActivity.class,
                        bundle);
                break;
            case R.id.rll_shower_details_shop:
                bundle.putString("shop_id", showerCardMess.getShop_id());
                UIHelper.startActivity(mContext, StoresDetailsActivity.class,
                        bundle);
                break;
            case R.id.shower_details_chat://聊天
                if (TXApplication.login) {
                    if (user_id.equals(SPUtils.get(mContext, "uid", ""))) {
                        UIHelper.ToastMessage(mContext, "不能和自己聊天");
                    } else {
                        bundle.putString("userId", user_id);
                        UIHelper.startActivity(mContext, ChatActivity.class, bundle);
                    }
                } else {
                    bundle.putInt(LoginActivity.FROM_LOGIN,
                            LoginActivity.FROM_OTHER);
                    UIHelper.startActivity(mContext, LoginActivity.class, bundle);
                }
                break;

            default:
                break;
        }
    }

    private void attention(String attentid, final TextView btn) {
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

    /**
     * 加载数据
     */
    private void updataview() {
        // ImageLoderutils imageLoderutils = new ImageLoderutils(mContext);
        if (showerInfoMess != null) {
            // imageLoderutils.display(shower_details_roundImageView,
            // showerInfoMess.getUser_icon());
            ImageLoader.getInstance().displayImage(
                    showerInfoMess.getUser_icon(),
                    shower_details_roundImageView);
            shower_details_nickname_tv.setText(showerInfoMess.getUser_name());
            shower_details_sex_age.setText(showerInfoMess.getUser_age());
            if(showerInfoMess.getUser_sex()==null&&showerInfoMess.getUser_age()==null){
                shower_details_sex_age.setVisibility(View.INVISIBLE);
            }
            if ("男".equals(showerInfoMess.getUser_sex())) {
                UIHelper.leftDrawable(
                        R.drawable.icon_sex_boy, mContext,
                        shower_details_sex_age);
            } else if ("女".equals(showerInfoMess.getUser_sex())) {
                UIHelper.leftDrawable(
                        R.drawable.icon_sex_girl, mContext,
                        shower_details_sex_age);
            }
            shower_details_xinzuo.setText(showerInfoMess.getUser_constellation());
            shower_details_often_in_tv.setText(showerInfoMess.getUser_where());
            shower_details_roundImageView.setVisibility(View.VISIBLE);
            shower_details_nickname_tv.setVisibility(View.VISIBLE);
            shower_details_often_in_tv.setVisibility(View.VISIBLE);
            shower_details_signature_tv.setText(showerInfoMess
                    .getUser_introduce());
            mTagsMesses = ShopListTagsMess.parse(showerInfoMess.getTags());
            shower_details_interested_sign_gv.setAdapter(new TagsMessAdapter(
                    mTagsMesses, mContext));
            attention = showerInfoMess.getAttention();
            if (1 == attention) {
                shower_details_attention_btn.setText("已关注");
                shower_details_attention_btn.setSelected(true);
                shower_details_attention_btn.setVisibility(View.VISIBLE);
            } else if (2 == attention) {
                shower_details_attention_btn.setText("+关注");
                shower_details_attention_btn.setSelected(false);
                shower_details_attention_btn.setVisibility(View.VISIBLE);
            }
            if (user.getUid().equals(user_id)) {
                shower_details_attention_btn.setVisibility(View.GONE);
            }
        }
        if (showerCardMess != null) {
            user_biaoshi = showerCardMess.getUser_biaoshi();
            if (1 == user_biaoshi) {
                shower_details_crafstman_ll.setVisibility(View.GONE);
            } else if (2 == user_biaoshi) {
                // imageLoderutils.display(
                // shower_details_crafstman_roundImageView,
                // showerCardMess.getUser_icon());
                ImageLoader.getInstance().displayImage(
                        showerCardMess.getUser_icon(),
                        shower_details_crafstman_roundImageView);
                shower_detaisls_crafstman_nickname_tv.setText(showerCardMess
                        .getUser_name());
                shower_details_crafstman_position_tv.setText(showerCardMess
                        .getUser_job());
                shower_detaisls_crafstman_moods.setText(showerCardMess
                        .getUser_hot());
                shower_detaisls_crafstman_thumbup.setText(showerCardMess
                        .getUser_fav());
                shower_details_shops_located_tv.setText(showerCardMess
                        .getUser_shop());
                shower_details_crafstman_ll.setVisibility(View.VISIBLE);
            }
        }
        if (showerFollowerMess != null) {
            showerFollowerAttenMesses = ShowerFollowerCountMess
                    .parse(showerFollowerMess.getAttention_count());
            showerFollowerFansMesses = ShowerFollowerCountMess
                    .parse(showerFollowerMess.getFan_count());
            if (showerFollowerAttenMesses != null) {
                shower_details_attention_tv.setText("关注的人("
                        + showerFollowerAttenMesses.size() + ")");
                // int width = ((TXApplication.WINDOW_WIDTH -
                // DensityUtil.dip2px(
                // mContext, 75)) / 4)
                // * (showerFollowerAttenMesses.size() < 4 ?
                // showerFollowerAttenMesses
                // .size() : 4)
                // + DensityUtil.dip2px(mContext, 15)
                // + ((showerFollowerAttenMesses.size() < 4 ?
                // showerFollowerAttenMesses
                // .size() : 4) - 1)
                // * DensityUtil.dip2px(mContext, 5);
                // shower_details_attention_gv.setLayoutParams(new LayoutParams(
                // width, LayoutParams.WRAP_CONTENT));
                if (0 == showerFollowerAttenMesses.size()) {
                    shower_details_attention_rll.setVisibility(View.GONE);
                }

            } else {
                shower_details_attention_rll.setVisibility(View.GONE);
            }
            if (showerFollowerFansMesses != null) {
                shower_details_fans_tv.setText("TA的粉丝("
                        + showerFollowerFansMesses.size() + ")");
                if (0 == showerFollowerFansMesses.size()) {
                    shower_details_fans_rll.setVisibility(View.GONE);
                }
            } else {
                shower_details_fans_rll.setVisibility(View.GONE);
            }
            shower_details_attention_gv.setAdapter(new ShowerFollerAdapter(
                    mContext, showerFollowerAttenMesses, 1));
            shower_details_fans_gv.setAdapter(new ShowerFollerAdapter(mContext,
                    showerFollowerFansMesses, 1));
        }
        shower_details_often_in.setVisibility(View.VISIBLE);
        shower_details_content.setVisibility(View.VISIBLE);
    }

    /**
     * 秀卡，蹭卡，动态的切换
     */
    private void chaneTab(int index) {
        switch (index) {
            case SHOW_CARD:
                shower_details_showcard.setChecked(true);
                shower_details_cengcard.setChecked(false);
                shower_details_dynamic.setChecked(false);
                getShopCard(SHOW_CARD, "秀卡", false);
                break;
            case CENG_CARD:
                shower_details_showcard.setChecked(false);
                shower_details_cengcard.setChecked(true);
                shower_details_dynamic.setChecked(false);
                getShopCard(CENG_CARD, "蹭卡", false);
                break;
            case DYNAMIC:
                shower_details_showcard.setChecked(false);
                shower_details_cengcard.setChecked(false);
                shower_details_dynamic.setChecked(true);
                getShopCard(DYNAMIC, "动态", false);
                break;

            default:
                break;
        }
    }

    /**
     * 获取头部数据
     */
    private void getViewData() {
        RequestParams params = TXApplication.getParams();
        String url = URLs.USER_INFO;
        params.addBodyParameter("login_uid", user.getUid());
        params.addBodyParameter("uid", user_id);
        TXApplication.post(null, mContext, url, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        loadIng("加载中", false);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        UIHelper.ToastMessage(mContext, R.string.net_work_error);
                        dialog.dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        // dialog.dismiss();
                        MessageResult result = MessageResult
                                .parse(responseInfo.result);
                        if (null == result) {
                            UIHelper.ToastMessage(mContext, "数据加载出错，请重试!");
                            return;
                        }
                        if (1 == result.getSuccess()) {
                            showerMess = ShowerMess.parse(result.getData());
                            if (null != showerMess) {
                                showerInfoMess = ShowerInfoMess
                                        .parse(showerMess.getInfo());
                                showerCardMess = ShowerCardMess
                                        .parse(showerMess.getCard());
                                showerFollowerMess = ShowerFollowerMess
                                        .parse(showerMess.getFollower());
                            }
                            getShopCard(DYNAMIC, "动态", true);
                        }
                    }
                });
    }

    /**
     * 获取秀卡数据
     */
    private void getShopCard(final int index, String type, final boolean isfrist) {
        RequestParams params = TXApplication.getParams();
        String url = URLs.USER_DETAIL;
        params.addBodyParameter("uid", user_id);
        params.addBodyParameter("type", type);
        TXApplication.post(null, mContext, url, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        UIHelper.ToastMessage(mContext, R.string.net_work_error);
                        dialog.dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        dialog.dismiss();
                        MessageResult result = MessageResult
                                .parse(responseInfo.result);
                        if (null == result) {
                            UIHelper.ToastMessage(mContext, "数据加载出错，请重试!");
                            return;
                        }
                        if (1 == result.getSuccess()) {
                            showerCommedMesses = ShowerCommedMess.parse(result
                                    .getData());
                            if (null != showerCommedMesses) {
                                shower_details_showcard.setText("秀卡"
                                        + "("
                                        + showerCommedMesses.get(0)
                                        .getX_count() + ")");
                                shower_details_dynamic.setText("动态"
                                        + "("
                                        + showerCommedMesses.get(0)
                                        .getD_count() + ")");
                                shower_details_cengcard.setText("蹭卡"
                                        + "("
                                        + showerCommedMesses.get(0)
                                        .getC_count() + ")");
                                shower_details_showcard_hover.setText("秀卡"
                                        + "("
                                        + showerCommedMesses.get(0)
                                        .getX_count() + ")");
                                shower_details_dynamic_hover.setText("动态"
                                        + "("
                                        + showerCommedMesses.get(0)
                                        .getD_count() + ")");
                                shower_details_cengcard_hover.setText("蹭卡"
                                        + "("
                                        + showerCommedMesses.get(0)
                                        .getC_count() + ")");
                            }
                            if (isfrist) {
                                adapter = new ShowerCommedAdapter();
                                shower_details_shopcard_lv.setAdapter(adapter);
                                updataview();
                            } else {
                                // shower_details_shopcard_lv.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                            hvoverView();
                        }
                    }
                });

    }

    Dictionary<Integer, Integer> listViewItemHeights = new Hashtable<Integer, Integer>();

    private void hvoverView() {

        shower_details_shopcard_lv.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                View c = view.getChildAt(0); //this is the first visible row
                if (c != null) {
                    int scrollY = -c.getTop();
                    listViewItemHeights.put(view.getFirstVisiblePosition(), c.getHeight());
                    for (int i = 0; i < view.getFirstVisiblePosition(); ++i) {
                        if (listViewItemHeights.get(i) != null) // (this is a sanity check)
                            scrollY += listViewItemHeights.get(i); //add all heights of the views that are gone

                    }
                    Log.i("tag", "the scrollY of the listview is==========" + scrollY);
                    // 滑动改变标题栏的透明度和文字透明度，图标
//					if (crafstman_details_headbackground == null) {
//						return;
//					}
                    if (scrollY < 0) {
                        return;
                    }
                    int lHeight = DensityUtil.dip2px(mContext, 100);
                    if (scrollY <= lHeight) {
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(mContext, 0));

                        shower_details_head_include.setLayoutParams(layoutParams);
                    } else {
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(mContext, 45) + ImmersedStatusbarUtils.getStatusBarHeight(mContext));

                        shower_details_head_include.setLayoutParams(layoutParams);
                    }

                }
                if (firstVisibleItem >= 1) {
                    shower_details_rg.setVisibility(View.VISIBLE);
                    shower_details_showcard_hover
                            .setChecked(shower_details_showcard.isChecked());
                    shower_details_cengcard_hover
                            .setChecked(shower_details_cengcard.isChecked());
                    shower_details_dynamic_hover
                            .setChecked(shower_details_dynamic.isChecked());
                } else {

                    shower_details_rg.setVisibility(View.GONE);
                }
            }
        });
    }

    class ShowerCommedAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return showerCommedMesses == null ? 0 : showerCommedMesses.size();
        }

        @Override
        public Object getItem(int position) {
            return null == showerCommedMesses ? null : showerCommedMesses
                    .get(position);
        }

        @Override
        public long getItemId(int position) {
            return null == showerCommedMesses ? 0 : position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder vh = null;
            if (null == convertView) {
                vh = new ViewHolder();
                convertView = View.inflate(mContext,
                        R.layout.item_shower_commed, null);
                vh.item_shower_details_time = (TextView) convertView
                        .findViewById(R.id.item_shower_details_time);
                vh.item_shower_details_gv = (GridView) convertView
                        .findViewById(R.id.item_shower_details_gv);
                vh.item_shower_details_content_tv = (TextView) convertView
                        .findViewById(R.id.item_shower_details_content_tv);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.item_shower_details_time.setText(showerCommedMesses
                    .get(position).getStatus_createTime());
            vh.item_shower_details_content_tv.setText(SmileUtils.getSmiledText(mContext
                    , showerCommedMesses.get(
                            position).getStatus_content()));
            mComcardStaPicsMess = ShopComcardStaPicsMess
                    .parse(showerCommedMesses.get(position).getStatus_pics());
            if (mComcardStaPicsMess != null) {
                switch (mComcardStaPicsMess.size()) {
                    case 4:
                        vh.item_shower_details_gv.setNumColumns(2);
                        break;
                    case 1:
                        vh.item_shower_details_gv.setNumColumns(1);
                        break;

                    default:
                        vh.item_shower_details_gv.setNumColumns(3);
                        break;
                }
            }
            if (mComcardStaPicsMess != null && mComcardStaPicsMess.size() > 0) {
                vh.item_shower_details_gv.setVisibility(View.VISIBLE);
                vh.item_shower_details_gv.setAdapter(new CommendgvAdapter(
                        mComcardStaPicsMess));
                vh.item_shower_details_gv
                        .setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> arg0,
                                                    View arg1, int arg2, long arg3) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(
                                        ImagePagerActivity.EXTRA_IMAGE_URLS,
                                        (Serializable) ShopComcardStaPicsMess
                                                .parse(showerCommedMesses.get(
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
                vh.item_shower_details_gv.setVisibility(View.GONE);
            }
            return convertView;
        }

        class ViewHolder {
            TextView item_shower_details_time;
            GridView item_shower_details_gv;
            TextView item_shower_details_content_tv;
        }
    }

    /**
     * 秀卡，蹭卡，点评
     */
    class CommendgvAdapter extends BaseAdapter {
        private List<ShopComcardStaPicsMess> mComcardStaPicsMess;

        public CommendgvAdapter(List<ShopComcardStaPicsMess> mComcardStaPicsMess) {
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
            if (mComcardStaPicsMess != null) {
                switch (mComcardStaPicsMess.size()) {
                    case 4:
                        vh.shop_commend_iv.setLayoutParams(new LayoutParams(
                                (TXApplication.WINDOW_WIDTH - DensityUtil.dip2px(
                                        mContext, 120)) / 2,
                                (TXApplication.WINDOW_WIDTH - DensityUtil.dip2px(
                                        mContext, 120)) / 2));
                        break;
                    case 1:
                        vh.shop_commend_iv.setLayoutParams(new LayoutParams(
                                (TXApplication.WINDOW_WIDTH - DensityUtil.dip2px(
                                        mContext, 110)) / 1,
                                (int) ((TXApplication.WINDOW_WIDTH - DensityUtil
                                        .dip2px(mContext, 110)) / 1.5)));
                        break;
                    default:
                        vh.shop_commend_iv.setLayoutParams(new LayoutParams(
                                (TXApplication.WINDOW_WIDTH - DensityUtil.dip2px(
                                        mContext, 130)) / 3,
                                (TXApplication.WINDOW_WIDTH - DensityUtil.dip2px(
                                        mContext, 130)) / 3));
                        break;
                }
            }
            // Log.i("ashhjasjk",
            // mComcardStaPicsMess.get(position).getThumbnail_pic()+position);
            // vh.shop_commend_iv.setTag(mComcardStaPicsMess.get(position).getThumbnail_pic()+position);
            // ImageAware imageAware = new ImageViewAware(vh.shop_commend_iv,
            // false);
            // ImageLoderutils imageLoderutils=new ImageLoderutils(mContext);
            // imageLoderutils.display(vh.shop_commend_iv,
            // mComcardStaPicsMess.get(position).getThumbnail_pic());
            // if(vh.shop_commend_iv.getTag()!=null&&vh.shop_commend_iv.getTag().equals(mComcardStaPicsMess.get(position).getThumbnail_pic()+position)){
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

    public void goBack(View v) {
        switch (v.getId()) {
            case R.id.head_left_tv:
                finish();
                onBack();
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBack();
    }

    private void onBack() {
        Intent mIntent = new Intent(ACTION_NAME);
        mIntent.putExtra("yaner", "发送广播，相当于在这里传送数据");
        // 发送广播
        sendBroadcast(mIntent);
    }

}
