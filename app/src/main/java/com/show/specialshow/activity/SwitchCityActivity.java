package com.show.specialshow.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.show.specialshow.BaseActivity;
import com.show.specialshow.R;
import com.show.specialshow.TXApplication;
import com.show.specialshow.URLs;
import com.show.specialshow.db.DatabaseHelper;
import com.show.specialshow.model.City;
import com.show.specialshow.model.MessageResult;
import com.show.specialshow.utils.PingYinUtil;
import com.show.specialshow.utils.SPUtils;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.view.MyLetterListView;
import com.show.specialshow.view.MyLetterListView.OnTouchingLetterChangedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class SwitchCityActivity extends BaseActivity implements
        OnScrollListener, AMapLocationListener {
    private BaseAdapter adapter;
    private ResultListAdapter resultListAdapter;
    private ListView personList;
    private ListView resultList;
    private TextView overlay; // 对话框首字母textview
    private MyLetterListView letterListView; // A-Z listview
    private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
    private String[] sections;// 存放存在的汉语拼音首字母
    private Handler handler;
    private OverlayThread overlayThread; // 显示首字母对话框
    private ArrayList<City> allCity_lists; // 所有城市列表
    private ArrayList<City> city_lists;// 城市列表
    private ArrayList<City> city_hot;
    private ArrayList<City> city_result;
    private ArrayList<String> city_history = new ArrayList<String>();
    private EditText sh;
    private TextView tv_noresult;
    private boolean isScroll = false;
    private boolean mReady;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption locationOption = null;

    private String currentCity; // 用于保存定位到的城市
    private int locateProcess = 1; // 记录当前定位的状态 正在定位-定位成功-定位失败
    private boolean isNeedFresh;

    private DatabaseHelper helper;
    private WindowManager windowManager;

    @Override
    public void initData() {
        setContentView(R.layout.activity_switch_city);
    }

    @Override
    public void initView() {
        personList = (ListView) findViewById(R.id.list_view);
        allCity_lists = new ArrayList<City>();
        city_hot = new ArrayList<City>();
        city_result = new ArrayList<City>();
        resultList = (ListView) findViewById(R.id.search_result);
        sh = (EditText) findViewById(R.id.sh);
        tv_noresult = (TextView) findViewById(R.id.tv_noresult);
        helper = new DatabaseHelper(this);
        onChange();
        letterListView = (MyLetterListView) findViewById(R.id.MyLetterListView01);
        letterListView
                .setOnTouchingLetterChangedListener(new LetterListViewListener());
        alphaIndexer = new HashMap<String, Integer>();
        handler = new Handler();
        overlayThread = new OverlayThread();
        isNeedFresh = true;
        personList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position >= 4) {
                    Intent data = new Intent();
                    data.putExtra("select_city", allCity_lists.get(position).getName());
                    UIHelper.setResult(mContext, RESULT_OK, data);
                    InsertCity(allCity_lists.get(position).getName());
                    SPUtils.put(mContext, "city", allCity_lists.get(position).getName());
                }
            }
        });
        locateProcess = 1;
        personList.setAdapter(adapter);
        personList.setOnScrollListener(this);
        resultListAdapter = new ResultListAdapter(this, city_result);
        resultList.setAdapter(resultListAdapter);
        resultList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent data = new Intent();
                data.putExtra("select_city", city_result.get(position).getName());
                UIHelper.setResult(mContext, RESULT_OK, data);
                InsertCity(city_result.get(position).getName());
                SPUtils.put(mContext, "city", city_result.get(position).getName());
            }
        });
        initOverlay();
//        cityInit();
        getCityList();
//        hotCityInit();
        hisCityInit();
        setAdapter(allCity_lists, city_hot, city_history);

        mLocationClient = new AMapLocationClient(this.getApplicationContext());
        permissionLocation();
        mLocationClient.startLocation();
    }

    private void permissionLocation() {
        Acp.getInstance(mContext).request(new AcpOptions.Builder()
                        .setPermissions(android.Manifest.permission.ACCESS_COARSE_LOCATION
                        )
//                /*以下为自定义提示语、按钮文字
                        .setRationalMessage("定位功能需要您授权，否则App将不能正常使用")
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        InitLocation();
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        UIHelper.ToastMessage(mContext, "定位功能取消授权");
                    }
                });
    }

    @Override
    public void fillView() {
        head_title_tv.setText("切换城市");
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {

    }

    public void InsertCity(String name) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from recentcity where name = '"
                + name + "'", null);
        if (cursor.getCount() > 0) { //
            db.delete("recentcity", "name = ?", new String[]{name});
        }
        db.execSQL("insert into recentcity(name, date) values('" + name + "', "
                + System.currentTimeMillis() + ")");
        db.close();
    }

    // 初始化汉语拼音首字母弹出提示框
    private void initOverlay() {
        mReady = true;
        LayoutInflater inflater = LayoutInflater.from(this);
        overlay = (TextView) inflater.inflate(R.layout.overlay, null);
        overlay.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        try {
            windowManager.addView(overlay, lp);

        } catch (Exception e) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (windowManager != null) {
            windowManager.removeView(overlay);
        }
        if (null != mLocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mLocationClient.onDestroy();
            mLocationClient = null;
            locationOption = null;
        }
    }

    private void cityInit() {
        City city = new City("定位", "0"); // 当前定位城市
        allCity_lists.add(city);
        city = new City("最近", "1"); // 最近访问的城市
        allCity_lists.add(city);
//        city = new City("热门", "2"); // 热门城市
//        allCity_lists.add(city);
//        city = new City("全部", "3"); // 全部城市
        city = new City("全部", "2"); // 全部城市
        allCity_lists.add(city);
//        city_lists = getCityList();
        allCity_lists.addAll(city_lists);
    }

    /**
     * 热门城市
     */
    public void hotCityInit() {
        City city = new City("上海", "2");
        city_hot.add(city);
        city = new City("北京", "2");
        city_hot.add(city);
        city = new City("广州", "2");
        city_hot.add(city);
        city = new City("深圳", "2");
        city_hot.add(city);
        city = new City("武汉", "2");
        city_hot.add(city);
        city = new City("天津", "2");
        city_hot.add(city);
        city = new City("西安", "2");
        city_hot.add(city);
        city = new City("南京", "2");
        city_hot.add(city);
        city = new City("杭州", "2");
        city_hot.add(city);
        city = new City("成都", "2");
        city_hot.add(city);
        city = new City("重庆", "2");
        city_hot.add(city);
    }

    @SuppressWarnings("unchecked")
    private ArrayList<City> getCityList() {
//        DBHelper dbHelper = new DBHelper(this);
//        try {
//            dbHelper.createDataBase();
//            SQLiteDatabase db = dbHelper.getWritableDatabase();
//            Cursor cursor = db.rawQuery("select * from city", null);
//            City city;
//            while (cursor.moveToNext()) {
//                city = new City(cursor.getString(1), cursor.getString(2));
//                list.add(city);
//            }
//            cursor.close();
//            db.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        RequestParams params = TXApplication.getParams();
        String url = URLs.CITY_CITYLIST;
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                loadIng("加载中...", true);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                MessageResult result = MessageResult.parse(responseInfo.result);
                if (null == result) {
                    return;
                }
                if (1 == result.getSuccess()) {
                    city_lists = (ArrayList<City>) City.parse(result.getData());
                    if (city_lists != null) {
                        Collections.sort(city_lists, comparator);
                        cityInit();
                    }

                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                UIHelper.ToastMessage(mContext, R.string.net_work_error);
            }
        });
        return city_lists;
    }

    /**
     * 最近访问的城市
     */
    private void hisCityInit() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from recentcity order by date desc limit 0, 3", null);
        while (cursor.moveToNext()) {
            city_history.add(cursor.getString(1));
        }
        cursor.close();
        db.close();
    }

    private void setAdapter(List<City> list, List<City> hotList,
                            List<String> hisCity) {
        adapter = new ListAdapter(this, list, hotList, hisCity);
        personList.setAdapter(adapter);
    }

    private void onChange() {
        sh.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString() == null || "".equals(s.toString())) {
                    letterListView.setVisibility(View.VISIBLE);
                    personList.setVisibility(View.VISIBLE);
                    resultList.setVisibility(View.GONE);
                    tv_noresult.setVisibility(View.GONE);
                } else {
                    city_result.clear();
                    letterListView.setVisibility(View.GONE);
                    personList.setVisibility(View.GONE);
                    getResultCityList(s.toString());
//                    if (city_result.size() <= 0) {
//                        tv_noresult.setVisibility(View.VISIBLE);
//                        resultList.setVisibility(View.GONE);
//                    } else {
//                        tv_noresult.setVisibility(View.GONE);
//                        resultList.setVisibility(View.VISIBLE);
//                        resultListAdapter.notifyDataSetChanged();
//                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @SuppressWarnings("unchecked")
    private void getResultCityList(String keyword) {

//        DBHelper dbHelper = new DBHelper(this);
//        try {
//            dbHelper.createDataBase();
//            SQLiteDatabase db = dbHelper.getWritableDatabase();
//            Cursor cursor = db.rawQuery(
//                    "select * from city where name like \"%" + keyword
//                            + "%\" or pinyin like \"%" + keyword + "%\"", null);
//            City city;
////			Log.e("info", "length = " + cursor.getCount());
//            while (cursor.moveToNext()) {
//                city = new City(cursor.getString(1), cursor.getString(2));
//                city_result.add(city);
//            }
//            cursor.close();
//            db.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Collections.sort(city_result, comparator);
        RequestParams params = TXApplication.getParams();
        params.addBodyParameter("key", keyword);
        String url = URLs.CITY_CITYLIST;
        TXApplication.post(null, mContext, url, params, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                loadIng("加载中", true);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                MessageResult result = MessageResult.parse(responseInfo.result);
                if (null == result) {
                    return;
                }

                if (1 == result.getSuccess()) {
                    if (result.getData() == null) {
                        tv_noresult.setVisibility(View.VISIBLE);
                        resultList.setVisibility(View.GONE);
                    } else {
                        ArrayList city_result1 = (ArrayList<City>) City.parse(result.getData());
                        if (city_result1 != null) {
                            Collections.sort(city_result1, comparator);
                            if (city_result1.size() <= 0) {
                                tv_noresult.setVisibility(View.VISIBLE);
                                resultList.setVisibility(View.GONE);
                            } else {
                                tv_noresult.setVisibility(View.GONE);
                                resultList.setVisibility(View.VISIBLE);
                                city_result.addAll(city_result1);
                                resultListAdapter.notifyDataSetChanged();
                            }
                        }
                    }


                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                UIHelper.ToastMessage(mContext, R.string.net_work_error);
            }
        });
    }

    /**
     * a-z排序
     */
    @SuppressWarnings("rawtypes")
    Comparator comparator = new Comparator<City>() {
        @Override
        public int compare(City lhs, City rhs) {
            String a = lhs.getPinyi().substring(0, 1);
            String b = rhs.getPinyi().substring(0, 1);
            int flag = a.compareTo(b);
            if (flag == 0) {
                return a.compareTo(b);
            } else {
                return flag;
            }
        }
    };

    /**
     * 高德地图定位回调
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        // Log.e("info", "city = " + arg0.getCity());
        if (!isNeedFresh) {
            return;
        }
        isNeedFresh = false;
        if (aMapLocation.getCity() == null) {
            locateProcess = 3; // 定位失败
            personList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            return;
        }
        currentCity = aMapLocation.getCity().substring(0,
                aMapLocation.getCity().length() - 1);
        locateProcess = 2; // 定位成功
        personList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    // 设置overlay不可见
    private class OverlayThread implements Runnable {
        @Override
        public void run() {
            overlay.setVisibility(View.GONE);
        }
    }

    // 获得汉语拼音首字母
    private String getAlpha(String str) {
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }
        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else if (str.equals("0")) {
            return "定位";
        } else if (str.equals("1")) {
            return "最近";
        } else if (str.equals("2")) {
            return "热门";
        } else if (str.equals("3")) {
            return "全部";
        } else {
            return "#";
        }
    }

    private class LetterListViewListener implements
            OnTouchingLetterChangedListener {

        @Override
        public void onTouchingLetterChanged(final String s) {
            isScroll = false;
            if (alphaIndexer.get(s) != null) {
                int position = alphaIndexer.get(s);
                personList.setSelection(position);
                overlay.setText(s);
                overlay.setVisibility(View.VISIBLE);
                handler.removeCallbacks(overlayThread);
                // 延迟一秒后执行，让overlay为不可见
                handler.postDelayed(overlayThread, 1000);
            }
        }
    }

    class ResultListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<City> results = new ArrayList<City>();

        public ResultListAdapter(Context context, ArrayList<City> results) {
            inflater = LayoutInflater.from(context);
            this.results = results;
        }

        @Override
        public int getCount() {
            return results.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView
                        .findViewById(R.id.name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.name.setText(results.get(position).getName());
            return convertView;
        }

        class ViewHolder {
            TextView name;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (!isScroll) {
            return;
        }

        if (mReady) {
            String text;
            String name = allCity_lists.get(firstVisibleItem).getName();
            String pinyin = allCity_lists.get(firstVisibleItem).getPinyi();
            if (firstVisibleItem < 4) {
                text = name;
            } else {
                text = PingYinUtil.converterToFirstSpell(pinyin)
                        .substring(0, 1).toUpperCase();
            }
            overlay.setText(text);
            overlay.setVisibility(View.VISIBLE);
            handler.removeCallbacks(overlayThread);
            // 延迟一秒后执行，让overlay为不可见
            handler.postDelayed(overlayThread, 1000);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_TOUCH_SCROLL
                || scrollState == SCROLL_STATE_FLING) {
            isScroll = true;
        }
    }

    public class ListAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<City> list;
        private List<City> hotList;
        private List<String> hisCity;
        //        final int VIEW_TYPE = 5;
        final int VIEW_TYPE = 4;

        public ListAdapter(Context context, List<City> list,
                           List<City> hotList, List<String> hisCity) {
            this.inflater = LayoutInflater.from(context);
            this.list = list;
            this.context = context;
            this.hotList = hotList;
            this.hisCity = hisCity;
            alphaIndexer = new HashMap<String, Integer>();
            sections = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                // 当前汉语拼音首字母
                String currentStr = getAlpha(list.get(i).getPinyi());
                // 上一个汉语拼音首字母，如果不存在为" "
                String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1)
                        .getPinyi()) : " ";
                if (!previewStr.equals(currentStr)) {
                    String name = getAlpha(list.get(i).getPinyi());
                    alphaIndexer.put(name, i);
                    sections[i] = name;
                }
            }
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE;
        }

        @Override
        public int getItemViewType(int position) {
            return position < 3 ? position : 3;
//            return position < 4 ? position : 4;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        ViewHolder holder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final TextView city;
            int viewType = getItemViewType(position);
            if (viewType == 0) { // 定位
                convertView = inflater.inflate(R.layout.frist_list_item, null);
                TextView locateHint = (TextView) convertView
                        .findViewById(R.id.locateHint);
                city = (TextView) convertView.findViewById(R.id.lng_city);
                city.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (locateProcess == 2) {
                            Intent data = new Intent();
                            data.putExtra("select_city", city.getText().toString());
                            UIHelper.setResult(mContext, RESULT_OK, data);
                            InsertCity(city.getText().toString());
                            SPUtils.put(mContext, "city", city.getText().toString());
                        } else if (locateProcess == 3) {
                            locateProcess = 1;
                            personList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            mLocationClient.stopLocation();
                            isNeedFresh = true;
                            InitLocation();
                            currentCity = "";
                            mLocationClient.startLocation();
                        }
                    }
                });
                ProgressBar pbLocate = (ProgressBar) convertView
                        .findViewById(R.id.pbLocate);
                if (locateProcess == 1) { // 正在定位
                    locateHint.setText("正在定位");
                    city.setVisibility(View.GONE);
                    pbLocate.setVisibility(View.VISIBLE);
                } else if (locateProcess == 2) { // 定位成功
                    locateHint.setText("当前定位城市");
                    city.setVisibility(View.VISIBLE);
                    city.setText(currentCity);
                    mLocationClient.stopLocation();
                    pbLocate.setVisibility(View.GONE);
                } else if (locateProcess == 3) {
                    locateHint.setText("未定位到城市,请选择");
                    city.setVisibility(View.VISIBLE);
                    city.setText("重新选择");
                    pbLocate.setVisibility(View.GONE);
                }
            } else if (viewType == 1) { // 最近访问城市
                convertView = inflater.inflate(R.layout.recent_city, null);
                GridView rencentCity = (GridView) convertView
                        .findViewById(R.id.recent_city);
                rencentCity
                        .setAdapter(new HitCityAdapter(context, this.hisCity));
                rencentCity.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent data = new Intent();
                        data.putExtra("select_city", city_history.get(position));
                        UIHelper.setResult(mContext, RESULT_OK, data);
                        InsertCity(city_history.get(position));
                        SPUtils.put(mContext, "city", city_history.get(position));
                    }
                });
                TextView recentHint = (TextView) convertView
                        .findViewById(R.id.recentHint);
                recentHint.setText("最近访问的城市");
//            } else if (viewType == 2) {
//                convertView = inflater.inflate(R.layout.recent_city, null);
//                GridView hotCity = (GridView) convertView
//                        .findViewById(R.id.recent_city);
//                hotCity.setOnItemClickListener(new OnItemClickListener() {
//
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view,
//                                            int position, long id) {
//                        Intent data = new Intent();
//                        data.putExtra("select_city", city_hot.get(position).getName());
//                        UIHelper.setResult(mContext, RESULT_OK, data);
//                        InsertCity(city_hot.get(position).getName());
//                        SPUtils.put(mContext, "city", city_hot.get(position).getName());
//                    }
//                });
//                hotCity.setAdapter(new HotCityAdapter(context, this.hotList));
//                TextView hotHint = (TextView) convertView
//                        .findViewById(R.id.recentHint);
//                hotHint.setText("热门城市");
//            } else if (viewType == 3) {
            } else if (viewType == 2) {
                convertView = inflater.inflate(R.layout.total_item, null);
            } else {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.list_item, null);
                    holder = new ViewHolder();
                    holder.alpha = (TextView) convertView
                            .findViewById(R.id.alpha);
                    holder.name = (TextView) convertView
                            .findViewById(R.id.name);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                if (position >= 1) {
                    holder.name.setText(list.get(position).getName());
                    String currentStr = getAlpha(list.get(position).getPinyi());
                    String previewStr = (position - 1) >= 0 ? getAlpha(list
                            .get(position - 1).getPinyi()) : " ";
                    if (!previewStr.equals(currentStr)) {
                        holder.alpha.setVisibility(View.VISIBLE);
                        holder.alpha.setText(currentStr);
                    } else {
                        holder.alpha.setVisibility(View.GONE);
                    }
                }
            }
            return convertView;
        }

        private class ViewHolder {
            TextView alpha; // 首字母标题
            TextView name; // 城市名字
        }

    }

    @Override
    protected void onStop() {
        mLocationClient.stopLocation();
        super.onStop();
    }

    //热门城市
    class HotCityAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<City> hotCitys;

        public HotCityAdapter(Context context, List<City> hotCitys) {
            this.context = context;
            inflater = LayoutInflater.from(this.context);
            this.hotCitys = hotCitys;
        }

        @Override
        public int getCount() {
            return hotCitys.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.item_city, null);
            TextView city = (TextView) convertView.findViewById(R.id.city);
            city.setText(hotCitys.get(position).getName());
            return convertView;
        }
    }

    //	最近城市
    class HitCityAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<String> hotCitys;

        public HitCityAdapter(Context context, List<String> hotCitys) {
            this.context = context;
            inflater = LayoutInflater.from(this.context);
            this.hotCitys = hotCitys;
        }

        @Override
        public int getCount() {
            return hotCitys.size();
        }

        @Override
        public Object getItem(int position) {
            return hotCitys.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.item_city, null);
            TextView city = (TextView) convertView.findViewById(R.id.city);
            city.setText(hotCitys.get(position));
            return convertView;
        }
    }

    /**
     * 初始化地图定位
     *
     * @param
     */
    private void InitLocation() {
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        // 设置定位监听
        mLocationClient.setLocationListener(this);
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(true);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(true);
        mLocationClient.setLocationOption(locationOption);
    }

}

 

