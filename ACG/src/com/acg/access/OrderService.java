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
public class OrderService {

	private MySql dbOpenHelper;

	public OrderService(Context context) {
		dbOpenHelper = new MySql(context);
	}

	/**
	 * 添加Orders
	 * 
	 * @param Order
	 */
	public void addOrder(Order order) {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		order.img.compress(Bitmap.CompressFormat.PNG, 100, os);
		// 对读和写操作的方法
		// 如果当我们二次调用这个数据库方法,他们调用的是同一个数据库对象,在这里的方法创建的数据调用对象是用的同一个对象
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into pvcOrders(编号,商品名称,出货日期,版式,制造商,供货商,定金,全款,状态,图片,备注)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?);", new Object[] { order.no,
				order.name, order.outDate, order.kind, order.whoMake,
				order.whereBuy, order.orderMoney, order.allMoney, order.state,
				os.toByteArray(), order.other });

	}

	/**
	 * 修改Orders
	 * 
	 * @param int
	 */
	public void modifyOrder(int no) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update pvcOrders set 编号=? where 编号=?", new Object[] { no - 1,
				no });
	}

	public void modifyOrderState(int no, int state) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update pvcOrders set 状态=? where 编号=?", new Object[] { state,
				no });
	}

	public void modify(Order order) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		order.img.compress(Bitmap.CompressFormat.PNG, 100, os);
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL(
				"update pvcOrders set 商品名称=?,出货日期=?,版式=?,制造商=?,供货商=?,定金=?,全款=?,状态=?,图片=?,备注=? where 编号=?",
				new Object[] { order.name, order.outDate, order.kind,
						order.whoMake, order.whereBuy, order.orderMoney,
						order.allMoney, order.state, os.toByteArray(),
						order.other, order.no });

	}

	/**
	 * 删除Orders
	 * 
	 * @param int
	 */
	public void deleteOrder(int no) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from pvcOrders where 编号=?", new Object[] { no });
	}

	/**
	 * 根据person的Id查询Person对象
	 * 
	 * @param id
	 *            Person的ID
	 * @return Person
	 */
	public Order findOrder(int num) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		// Cursor游标的位置,默认是0,所有在操作时一定要先cursor.moveToFirst()一下,定位到第一条记录
		Cursor cursor = db.query("pvcOrders", new String[] { "编号", "商品名称", "出货日期",
				"版式", "制造商", "供货商", "定金", "全款", "状态", "图片","备注" }, "编号=?",
				new String[] { String.valueOf(num) }, null, null, null);
		if (cursor.moveToFirst()) {
			int no = cursor.getInt(cursor.getColumnIndex("编号"));
			String name = cursor.getString(cursor.getColumnIndex("商品名称"));
			String outdate = cursor.getString(cursor.getColumnIndex("出货日期"));
			String kind = cursor.getString(cursor.getColumnIndex("版式"));
			String whoMake = cursor.getString(cursor.getColumnIndex("制造商"));
			String whereBuy = cursor.getString(cursor.getColumnIndex("供货商"));
			double orderMoney = cursor.getDouble(cursor.getColumnIndex("定金"));
			double allMoney = cursor.getDouble(cursor.getColumnIndex("全款"));
			int state = cursor.getInt(cursor.getColumnIndex("状态"));
			byte[] imgBlob = cursor.getBlob(cursor.getColumnIndex("图片"));
			String other = cursor.getString(cursor.getColumnIndex("备注"));
			Bitmap img = BitmapFactory.decodeByteArray(imgBlob, 0,
					imgBlob.length);
			return new Order(no, name, outdate, kind, whoMake, whereBuy,
					orderMoney, allMoney, allMoney - orderMoney, state, img,
					other);
		}
		return null;
	}

	/**
	 * 返回Orders的集合
	 * 
	 * @param int,int
	 * 
	 * @return List<Order>
	 * 
	 */
	public List<Order> findOrderList(Integer start, Integer length) {
		List<Order> orders = new ArrayList<Order>();
		// 只对读的操作的方法
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("pvcOrders", null, null, null, null, null, null,
				start + "," + length);
		while (cursor.moveToNext()) {
			int no = cursor.getInt(cursor.getColumnIndex("编号"));
			String name = cursor.getString(cursor.getColumnIndex("商品名称"));
			String outdate = cursor.getString(cursor.getColumnIndex("出货日期"));
			String kind = cursor.getString(cursor.getColumnIndex("版式"));
			String whoMake = cursor.getString(cursor.getColumnIndex("制造商"));
			String whereBuy = cursor.getString(cursor.getColumnIndex("供货商"));
			double orderMoney = cursor.getDouble(cursor.getColumnIndex("定金"));
			double allMoney = cursor.getDouble(cursor.getColumnIndex("全款"));
			int state = cursor.getInt(cursor.getColumnIndex("状态"));
			byte[] imgBlob = cursor.getBlob(cursor.getColumnIndex("图片"));
			String other = cursor.getString(cursor.getColumnIndex("备注"));
			Bitmap img = BitmapFactory.decodeByteArray(imgBlob, 0,
					imgBlob.length);
			orders.add(new Order(no, name, outdate, kind, whoMake, whereBuy,
					orderMoney, allMoney, allMoney - orderMoney, state, img,
					other));
		}
		return orders;
	}

	/**
	 * 返回Orders的记录总个数
	 * 
	 * @return int
	 */
	public int getCount() {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(0) from pvcOrders;", null);
		// 这里必定有一条记录.所有不用判断,直接移到第一条.
		cursor.moveToFirst();
		// 这里只有一个字段时候 返回
		return cursor.getInt(0);
	}

}
