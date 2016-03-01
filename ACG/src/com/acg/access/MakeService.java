package com.acg.access;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.example.acg.R;

/**
 * 对Orders对象的sql操作(增删改查)
 * 
 * @author xsd
 * 
 */
public class MakeService {

	private MySql dbOpenHelper;

	public MakeService(Context context) {
		dbOpenHelper = new MySql(context);
	}

	/**
	 * 添加Makers
	 * 
	 * @param WhoMake
	 */
	public boolean addMaker(WhoMake maker) {
		// 对读和写操作的方法
		// 如果当我们二次调用这个数据库方法,他们调用的是同一个数据库对象,在这里的方法创建的数据调用对象是用的同一个对象
		if (findMaker(maker.name)) {
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			db.execSQL("insert into pvcMakers(编号,制造商)" + " values(?,?);",
					new Object[] { maker.no, maker.name });
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 删除Orders
	 * 
	 * @param no
	 *            WhoMake的ID
	 */
	public void deleteOrder(int no) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from pvcMakers where 编号=?", new Object[] { no });
	}

	/**
	 * 根据WhoMake的Id查询WhoMake对象
	 * 
	 * @param num
	 *            WhoMake的ID
	 * @return WhoMake
	 */
	public boolean findMaker(String name) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		// Cursor游标的位置,默认是0,所有在操作时一定要先cursor.moveToFirst()一下,定位到第一条记录
		Cursor cursor = db.query("pvcMakers", new String[] { "编号", "制造商" },
				"制造商=?", new String[] { name }, null, null, null);
		if (cursor.moveToFirst()) {
			return false;
		}
		return true;
	}

	/**
	 * 返回Makers的集合
	 * 
	 * @param int,int
	 * 
	 * @return List<WhoMake>
	 * 
	 */
	public List<WhoMake> findMakerList(Integer start, Integer length) {
		List<WhoMake> makers = new ArrayList<WhoMake>();
		// 只对读的操作的方法
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("pvcMakers", null, null, null, null, null,
				null, start + "," + length);
		while (cursor.moveToNext()) {
			int no = cursor.getInt(cursor.getColumnIndex("编号"));
			String name = cursor.getString(cursor.getColumnIndex("制造商"));
			makers.add(new WhoMake(no, name));
		}
		return makers;
	}

	/**
	 * 返回Makers的记录总个数
	 * 
	 * @return int
	 */
	public int getCount() {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(0) from pvcMakers;", null);
		// 这里必定有一条记录.所有不用判断,直接移到第一条.
		cursor.moveToFirst();
		// 这里只有一个字段时候 返回
		return cursor.getInt(0);
	}

}
