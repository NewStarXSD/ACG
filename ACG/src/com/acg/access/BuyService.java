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
 * ��Orders�����sql����(��ɾ�Ĳ�)
 * 
 * @author xsd
 * 
 */
public class BuyService {

	private MySql dbOpenHelper;

	public BuyService(Context context) {
		dbOpenHelper = new MySql(context);
	}

	/**
	 * ���Makers
	 * 
	 * @param WhoMake
	 */
	public boolean addBuy(WhereBuy buy) {
		// �Զ���д�����ķ���
		// ��������Ƕ��ε���������ݿⷽ��,���ǵ��õ���ͬһ�����ݿ����,������ķ������������ݵ��ö������õ�ͬһ������
		if (findBuy(buy.name)) {
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			db.execSQL("insert into pvcBuys(���,������)" + " values(?,?);",
					new Object[] { buy.no, buy.name });
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ɾ��Orders
	 * 
	 * @param no
	 *            WhoMake��ID
	 */
	public void deleteBuy(int no) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from pvcBuys where ���=?", new Object[] { no });
	}

	/**
	 * ����WhoMake��Id��ѯWhoMake����
	 * 
	 * @param num
	 *            WhoMake��ID
	 * @return WhoMake
	 */
	public boolean findBuy(String name) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		// Cursor�α��λ��,Ĭ����0,�����ڲ���ʱһ��Ҫ��cursor.moveToFirst()һ��,��λ����һ����¼
		Cursor cursor = db.query("pvcBuys", new String[] { "���", "������" },
				"������=?", new String[] { name }, null, null, null);
		if (cursor.moveToFirst()) {
			return false;
		}
		return true;
	}

	/**
	 * ����Makers�ļ���
	 * 
	 * @param int,int
	 * 
	 * @return List<WhoMake>
	 * 
	 */
	public List<WhereBuy> findBuyList(Integer start, Integer length) {
		List<WhereBuy> buy = new ArrayList<WhereBuy>();
		// ֻ�Զ��Ĳ����ķ���
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("pvcBuys", null, null, null, null, null, null,
				start + "," + length);
		while (cursor.moveToNext()) {
			int no = cursor.getInt(cursor.getColumnIndex("���"));
			String name = cursor.getString(cursor.getColumnIndex("������"));
			buy.add(new WhereBuy(no, name));
		}
		return buy;
	}

	/**
	 * ����Makers�ļ�¼�ܸ���
	 * 
	 * @return int
	 */
	public int getCount() {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(0) from pvcBuys;", null);
		// ����ض���һ����¼.���в����ж�,ֱ���Ƶ���һ��.
		cursor.moveToFirst();
		// ����ֻ��һ���ֶ�ʱ�� ����
		return cursor.getInt(0);
	}

}
