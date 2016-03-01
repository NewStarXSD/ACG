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
		db.execSQL("CREATE TABLE IF NOT EXISTS pvcOrders" + "(编号 INTEGER,"
				+ "商品名称 VARCHAR(50)," + "出货日期 DATE," + "版式 VARCHAR(50),"
				+ "制造商 VARCHAR(50)," + "供货商 VARCHAR(50)," + "定金 DOUBLE,"
				+ "全款 DOUBLE," + "状态 INTEGER," + "图片 BLOB," + "备注 TEXT);");
		db.execSQL("CREATE TABLE IF NOT EXISTS pvcMakers" + "(编号 INTEGER,"
				+ "制造商 VARCHAR(50));");
		db.execSQL("CREATE TABLE IF NOT EXISTS pvcBuys" + "(编号 INTEGER,"
				+ "供货商 VARCHAR(50));");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		this.onCreate(db);
	}
}
