package com.show.specialshow.view;


import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.os.Build;
import android.widget.GridView;

public class MyGridView extends GridView {
	public MyGridView(android.content.Context context,
			android.util.AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 设置不滚动
	 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
    @SuppressLint("NewApi")
	@Override  
    public int getColumnWidth(){  
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) return super.getColumnWidth();  
        else{  
            try{  
                Field field = GridView.class.getDeclaredField("mColumnWidth");  
                field.setAccessible(true);  
                Integer value = (Integer) field.get(this);  
                field.setAccessible(false);  
                return value.intValue();  
            }catch(NoSuchFieldException e){  
                throw new RuntimeException(e);  
            }catch(IllegalAccessException e){  
                throw new RuntimeException(e);  
            }  
        }  
    }  
}