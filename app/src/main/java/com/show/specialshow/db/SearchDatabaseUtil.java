package com.show.specialshow.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.show.specialshow.model.NearMapMess;
import com.show.specialshow.model.SearchHistoryMess;

import java.util.ArrayList;
import java.util.List;

public class SearchDatabaseUtil {
    private SearchHistoryHelper helper;

    public SearchDatabaseUtil(Context context) {
        super();
        helper = new SearchHistoryHelper(context);
    }

    /**
     * 插入数据
     *
     * @param //String
     */
    public boolean Insert(SearchHistoryMess searchHistoryMess) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "insert into " + SearchHistoryHelper.TABLE_NAME +
                "(search_history) values("
                + "'" + searchHistoryMess.getSearch_history() + "'" + ")";
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

    public void Update(SearchHistoryMess searchHistoryMess, int id) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("search_history", searchHistoryMess.getSearch_history());
        int rows = db.update(SearchHistoryHelper.TABLE_NAME, values, "_id=?", new String[]{id + ""});

        db.close();
    }

    /**
     * 根据删除数据
     *
     * @param //int id
     */

    public void Delete(int id) {

        SQLiteDatabase db = helper.getWritableDatabase();
        int raw = db.delete(SearchHistoryHelper.TABLE_NAME, "_id=?", new String[]{id + ""});
        db.close();
    }

    public void Delete(String search_history) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(SearchHistoryHelper.TABLE_NAME, "search_history=?", new String[]{search_history + ""});
        db.close();
    }

    /**
     * 删除所有数据
     */
    public void deleteAll() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(SearchHistoryHelper.TABLE_NAME, null, null);
        db.close();
    }

    /**
     * 查询所有数据
     */
    public List<SearchHistoryMess> queryAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<SearchHistoryMess> list = new ArrayList<>();
        Cursor cursor = db.query(SearchHistoryHelper.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            SearchHistoryMess searchHistoryMess = new SearchHistoryMess();
            searchHistoryMess.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            searchHistoryMess.setSearch_history(cursor.getString(cursor.getColumnIndex("search_history")));
            list.add(searchHistoryMess);
        }
        db.close();
        return list;
    }


}
