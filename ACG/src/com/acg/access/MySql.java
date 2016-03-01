package com.acg.access;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySql extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "OrdersOfPvc.db";
	private static final int DATABASE_VERSION = 1;

	public MySql(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS pvcOrders" + "(��� INTEGER,"
				+ "��Ʒ���� VARCHAR(50)," + "�������� DATE," + "��ʽ VARCHAR(50),"
				+ "������ VARCHAR(50)," + "������ VARCHAR(50)," + "���� DOUBLE,"
				+ "ȫ�� DOUBLE," + "״̬ INTEGER," + "ͼƬ BLOB," + "��ע TEXT);");
		db.execSQL("CREATE TABLE IF NOT EXISTS pvcMakers" + "(��� INTEGER,"
				+ "������ VARCHAR(50));");
		db.execSQL("CREATE TABLE IF NOT EXISTS pvcBuys" + "(��� INTEGER,"
				+ "������ VARCHAR(50));");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		this.onCreate(db);
	}
}
