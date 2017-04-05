package com.show.specialshow.utils;

import com.umeng.comm.core.share.Shareable;

/**
 * Created by admin on 2016/8/1.
 */
public class ActivityShareServiceFactory {
    private static ActivityShareServiceImpl mShareServiceImpl;

    public ActivityShareServiceFactory() {
    }

    public static Shareable getShareService() {
        if (mShareServiceImpl == null) {
            Class var0 = ShareServiceImpl.class;
            synchronized (ShareServiceImpl.class) {
                if (mShareServiceImpl == null) {
                    mShareServiceImpl = new ActivityShareServiceImpl();
                }
            }
        }

        return mShareServiceImpl;
    }
}
