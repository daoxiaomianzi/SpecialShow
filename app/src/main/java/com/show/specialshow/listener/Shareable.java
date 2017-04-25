//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.show.specialshow.listener;

import android.app.Activity;
import android.content.Intent;

import com.umeng.comm.core.beans.ShareContent;

public interface Shareable {
    void share(Activity var1, ShareContent var2);

    void share(String id, Activity var1, ShareContent var2);

    void onActivityResult(Activity var1, int var2, int var3, Intent var4);
}
