package com.show.specialshow.utils;

import com.umeng.comm.core.share.Shareable;

/**
 * Created by admin on 2016/8/1.
 */
public class ShareServiceFactory {
    private static ShareServiceImpl mShareServiceImpl;

    public ShareServiceFactory() {
    }
    public static Shareable getShareService() {
        if(mShareServiceImpl == null) {
            Class var0 = ShareServiceImpl.class;
            synchronized(ShareServiceImpl.class) {
                if(mShareServiceImpl == null) {
                    mShareServiceImpl = new ShareServiceImpl();
                }
            }
        }

        return mShareServiceImpl;
    }
}
