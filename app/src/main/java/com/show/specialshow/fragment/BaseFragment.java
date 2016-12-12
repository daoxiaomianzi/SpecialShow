package com.show.specialshow.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ScrollView;

import com.show.specialshow.view.CustomProgressDialog;

public abstract class BaseFragment extends Fragment {
    protected ScrollView content_sv;
    public Context mContext;
    protected boolean isVisible;
    protected Dialog affirmDialog;
    protected CustomProgressDialog dialog;

    //对话框样式（一个确定）区别
    public int dialogStyle;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
        setListener();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        //判断当前fragment是否为正在显示
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 隐藏处理的事情
     */
    private void onInvisible() {

    }

    public void loadIng(String tips, boolean canBack) {
        if (null != getActivity().getParent()) {
            if (getActivity().getParent().isFinishing()) {
                return;
            }
            if (dialog != null) {
                dialog.dismiss();
            }
            dialog = new CustomProgressDialog(getActivity().getParent(), tips);
        } else {
            if (getActivity().isFinishing()) {
                return;
            }
            if (dialog != null) {
                dialog.dismiss();
            }
            dialog = new CustomProgressDialog(mContext, tips);
        }
        dialog.setCancelable(canBack);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    /**
     * 显示处理的事情
     */
    private void onVisible() {
        fillView();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && null != content_sv)// hidden为true时，fragment变得不可见，为false时变得可见
            content_sv.smoothScrollTo(0, 0);
    }

    public abstract void initData();

    public abstract void initView();

    public abstract void setListener();

    public abstract void fillView();

    public View findViewById(int id) {
        return getActivity().findViewById(id);
    }

    public abstract void onClick(View v);
}
