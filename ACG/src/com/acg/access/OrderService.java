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
public class OrderService {

	private MySql dbOpenHelper;

	public OrderService(Context context) {
		dbOpenHelper = new MySql(context);
	}

	/**
	 * ���Orders
	 * 
	 * @param Order
	 */
	public void addOrder(Order order) {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		order.img.compress(Bitmap.CompressFormat.PNG, 100, os);
		// �Զ���д�����ķ���
		// ��������Ƕ��ε���������ݿⷽ��,���ǵ��õ���ͬһ�����ݿ����,������ķ������������ݵ��ö������õ�ͬһ������
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into pvcOrders(���,��Ʒ����,��������,��ʽ,������,������,����,ȫ��,״̬,ͼƬ,��ע)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?);", new Object[] { order.no,
				order.name, order.outDate, order.kind, order.whoMake,
				order.whereBuy, order.orderMoney, order.allMoney, order.state,
				os.toByteArray(), order.other });

	}

	/**
	 * �޸�Orders
	 * 
	 * @param int
	 */
	public void modifyOrder(int no) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update pvcOrders set ���=? where ���=?", new Object[] { no - 1,
				no });
	}

	public void modifyOrderState(int no, int state) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update pvcOrders set ״̬=? where ���=?", new Object[] { state,
				no });
	}

	public void modify(Order order) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		order.img.compress(Bitmap.CompressFormat.PNG, 100, os);
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL(
				"update pvcOrders set ��Ʒ����=?,��������=?,��ʽ=?,������=?,������=?,����=?,ȫ��=?,״̬=?,ͼƬ=?,��ע=? where ���=?",
				new Object[] { order.name, order.outDate, order.kind,
						order.whoMake, order.whereBuy, order.orderMoney,
						order.allMoney, order.state, os.toByteArray(),
						order.other, order.no });

	}

	/**
	 * ɾ��Orders
	 * 
	 * @param int
	 */
	public void deleteOrder(int no) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from pvcOrders where ���=?", new Object[] { no });
	}

	/**
	 * ����person��Id��ѯPerson����
	 * 
	 * @param id
	 *            Person��ID
	 * @return Person
	 */
	public Order findOrder(int num) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		// Cursor�α��λ��,Ĭ����0,�����ڲ���ʱһ��Ҫ��cursor.moveToFirst()һ��,��λ����һ����¼
		Cursor cursor = db.query("pvcOrders", new String[] { "���", "��Ʒ����", "��������",
				"��ʽ", "������", "������", "����", "ȫ��", "״̬", "ͼƬ","��ע" }, "���=?",
				new String[] { String.valueOf(num) }, null, null, null);
		if (cursor.moveToFirst()) {
			int no = cursor.getInt(cursor.getColumnIndex("���"));
			String name = cursor.getString(cursor.getColumnIndex("��Ʒ����"));
			String outdate = cursor.getString(cursor.getColumnIndex("��������"));
			String kind = cursor.getString(cursor.getColumnIndex("��ʽ"));
			String whoMake = cursor.getString(cursor.getColumnIndex("������"));
			String whereBuy = cursor.getString(cursor.getColumnIndex("������"));
			double orderMoney = cursor.getDouble(cursor.getColumnIndex("����"));
			double allMoney = cursor.getDouble(cursor.getColumnIndex("ȫ��"));
			int state = cursor.getInt(cursor.getColumnIndex("״̬"));
			byte[] imgBlob = cursor.getBlob(cursor.getColumnIndex("ͼƬ"));
			String other = cursor.getString(cursor.getColumnIndex("��ע"));
			Bitmap img = BitmapFactory.decodeByteArray(imgBlob, 0,
					imgBlob.length);
			return new Order(no, name, outdate, kind, whoMake, whereBuy,
					orderMoney, allMoney, allMoney - orderMoney, state, img,
					other);
		}
		return null;
	}

	/**
	 * ����Orders�ļ���
	 * 
	 * @param int,int
	 * 
	 * @return List<Order>
	 * 
	 */
	public List<Order> findOrderList(Integer start, Integer length) {
		List<Order> orders = new ArrayList<Order>();
		// ֻ�Զ��Ĳ����ķ���
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("pvcOrders", null, null, null, null, null, null,
				start + "," + length);
		while (cursor.moveToNext()) {
			int no = cursor.getInt(cursor.getColumnIndex("���"));
			String name = cursor.getString(cursor.getColumnIndex("��Ʒ����"));
			String outdate = cursor.getString(cursor.getColumnIndex("��������"));
			String kind = cursor.getString(cursor.getColumnIndex("��ʽ"));
			String whoMake = cursor.getString(cursor.getColumnIndex("������"));
			String whereBuy = cursor.getString(cursor.getColumnIndex("������"));
			double orderMoney = cursor.getDouble(cursor.getColumnIndex("����"));
			double allMoney = cursor.getDouble(cursor.getColumnIndex("ȫ��"));
			int state = cursor.getInt(cursor.getColumnIndex("״̬"));
			byte[] imgBlob = cursor.getBlob(cursor.getColumnIndex("ͼƬ"));
			String other = cursor.getString(cursor.getColumnIndex("��ע"));
			Bitmap img = BitmapFactory.decodeByteArray(imgBlob, 0,
					imgBlob.length);
			orders.add(new Order(no, name, outdate, kind, whoMake, whereBuy,
					orderMoney, allMoney, allMoney - orderMoney, state, img,
					other));
		}
		return orders;
	}

	/**
	 * ����Orders�ļ�¼�ܸ���
	 * 
	 * @return int
	 */
	public int getCount() {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(0) from pvcOrders;", null);
		// ����ض���һ����¼.���в����ж�,ֱ���Ƶ���һ��.
		cursor.moveToFirst();
		// ����ֻ��һ���ֶ�ʱ�� ����
		return cursor.getInt(0);
	}

}
