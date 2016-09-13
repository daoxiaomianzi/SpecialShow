package com.show.specialshow.utils;


import java.util.HashMap;

import android.os.Handler;
import android.os.Message;

public class MsgCenter {
	
	private HashMap<String, Handler> mHandlerMap = new HashMap<String, Handler>();
	private static MsgCenter mInstance;
	
	private MsgCenter(){
		
	}
	
	public static MsgCenter getInstance(){
		if (mInstance == null) {
			mInstance = new MsgCenter();
		}
		return mInstance;
	}
	
	public void sendMsg(String name,Message msg){
		
		for (String key: mHandlerMap.keySet()) {
			if (key.equals(name)) {
				Handler handler = mHandlerMap.get(key);
				handler.sendMessage(msg);
			}
		}
	}
	
	public void addHandler(String name, Handler handler){
		mHandlerMap.put(name, handler);
	}
	
	public void removeHandler(String name){
		mHandlerMap.remove(name);
	}
	
	public void clearHandler(){
		mHandlerMap.clear();
	}

}








