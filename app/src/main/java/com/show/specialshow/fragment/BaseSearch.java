package com.show.specialshow.fragment;

import android.widget.BaseAdapter;

import com.show.specialshow.contstant.ConstantValue;
import com.show.specialshow.utils.UIHelper;
import com.show.specialshow.xlistview.XListView;
import com.show.specialshow.xlistview.XListView.IXListViewListener;


public abstract class BaseSearch extends BaseFragment {
	public static String BID_TYPE = "bid_type";
	protected int pageIndex = 1;
	protected int totalRecord;
	protected int localRecord = 0;
	protected XListView search_result_lv;
	protected BaseAdapter adapter;

	protected void initListView() {
		search_result_lv.setDividerHeight(10);
		search_result_lv.setPullLoadEnable(true);
		search_result_lv.setPullRefreshEnable(true);
		search_result_lv.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				pageIndex = 1;
				getData();
			}

			@Override
			public void onLoadMore() {
				pageIndex++;
				getData();
			}

			@Override
			public void onInit() {
				getData();
				
			}
		});
		search_result_lv.setAdapter(adapter);
	}

	protected void changeListView(int size) {
		stopLoad();
		if (size < ConstantValue.PAGE_SIZE || localRecord == totalRecord) {
			search_result_lv.loadFull();
		}
		if(size==0&&localRecord==0){
			search_result_lv.loadEmpty();
		}
		if (search_result_lv.getState() == XListView.LOAD_INIT)
			search_result_lv.setSelectionFromTop(0, 0);
		adapter.notifyDataSetChanged();
	}

	protected void onError(String prompt) {
		UIHelper.ToastMessage(mContext, prompt);
		stopLoad();
		search_result_lv.onError();
	}

	protected void stopLoad() {
		switch (search_result_lv.getState()) {
		case XListView.LOAD_INIT:
			search_result_lv.stopInit();
			break;
		case XListView.LOAD_REFRESH:
			search_result_lv.stopRefresh();
			break;
		case XListView.LOAD_MORE:
			search_result_lv.stopLoadMore();
			break;
		}
	}

	protected abstract void getData();
}
