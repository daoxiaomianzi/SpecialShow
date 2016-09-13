package com.show.specialshow.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.show.specialshow.model.NearMapMess;

import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {
    private MyHelper helper;

    public DatabaseUtil(Context context) {
        super();
        helper = new MyHelper(context);
    }

    /**
     * 插入数据
     *
     * @param //String
     */
    public boolean Insert(NearMapMess nearMapMess) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "insert into "+MyHelper.TABLE_NAME+
                "(address,title,latitude,longitude,shop_id) values("+
                "'"+nearMapMess.getAddress()+"',"
                +"'"+nearMapMess.getTitle()+"',"
                +"'"+nearMapMess.getLatitude()+"',"
                +"'"+nearMapMess.getLongitude()+"',"
                +"'"+nearMapMess.getShop_id()+"'"+")";
        try {
            db.execSQL(sql);
            return true;
        } catch (SQLException e) {
            Log.e("err", "insert failed");
            return false;
        } finally {
            db.close();
        }

    }


    /**
     * 更新数据
     *
     * @param
     */

    public void Update(NearMapMess nearMapMess, int id) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("address", nearMapMess.getAddress());
        values.put("title", nearMapMess.getTitle());
        values.put("latitude", nearMapMess.getLatitude());
        values.put("longitude", nearMapMess.getLongitude());
        values.put("shop_id", nearMapMess.getShop_id());
        int rows = db.update(MyHelper.TABLE_NAME, values, "_id=?", new String[]{id + ""});

        db.close();
    }

    /**
     * 根据删除数据
     *
     * @param //int id
     */

    public void Delete(int id) {

        SQLiteDatabase db = helper.getWritableDatabase();
        int raw = db.delete(MyHelper.TABLE_NAME, "_id=?", new String[]{id + ""});
        db.close();
    }
    /**
     * 删除所有数据
     */
    public void deleteAll(){
        SQLiteDatabase db=helper.getWritableDatabase();
        db.delete(MyHelper.TABLE_NAME,null,null);
        db.close();
    }
    /**
     * 查询所有数据
     */
    public List<NearMapMess> queryAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<NearMapMess> list = new ArrayList<NearMapMess>();
        Cursor cursor = db.query(MyHelper.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            NearMapMess nearMapMess = new NearMapMess();
            nearMapMess.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            nearMapMess.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            nearMapMess.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            nearMapMess.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
            nearMapMess.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
            nearMapMess.setShop_id(cursor.getString(cursor.getColumnIndex("shop_id")));
            list.add(nearMapMess);
        }
        db.close();
        return list;
    }


    /**
     * 按店铺名字进行查找并排序
     */
    public List<NearMapMess> queryByname(String title) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<NearMapMess> list = new ArrayList<NearMapMess>();
        Cursor cursor = db.query(MyHelper.TABLE_NAME, new String[]{"_id", "address", "title","latitude","longitude","shop_id"}, "title like ? ", new String[]{"%" + title + "%"}, null, null, "title asc");
//		Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
        while (cursor.moveToNext()) {
            NearMapMess nearMapMess = new NearMapMess();
            nearMapMess.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            nearMapMess.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            nearMapMess.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            nearMapMess.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
            nearMapMess.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
            nearMapMess.setShop_id(cursor.getString(cursor.getColumnIndex("shop_id")));
            list.add(nearMapMess);
        }
        db.close();
        return list;
    }


    /**
     * 按id
     * 查询
     */
    public NearMapMess queryByid(int id) {

        SQLiteDatabase db = helper.getReadableDatabase();
        NearMapMess nearMapMess = new NearMapMess();
        Cursor cursor = db.query(MyHelper.TABLE_NAME, new String[]{"name", "sex"}, "_id=?", new String[]{id + ""}, null, null, null);
//		db.delete(table, whereClause, whereArgs)
        while (cursor.moveToNext()) {
            nearMapMess.setId(id);
            nearMapMess.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            nearMapMess.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            nearMapMess.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
            nearMapMess.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
            nearMapMess.setShop_id(cursor.getString(cursor.getColumnIndex("shop_id")));
        }
        db.close();
        return nearMapMess;
    }


}
