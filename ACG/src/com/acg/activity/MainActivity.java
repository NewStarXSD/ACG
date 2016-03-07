package com.acg.activity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acg.access.Order;
import com.acg.access.OrderService;
import com.acg.alarm.LongRunningService;
import com.acg.image.MyViewBinder;
import com.acg.list.MyAdapter;
import com.acg.swipemenulistview.SwipeMenu;
import com.acg.swipemenulistview.SwipeMenuCreator;
import com.acg.swipemenulistview.SwipeMenuItem;
import com.acg.swipemenulistview.SwipeMenuListView;
import com.acg.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.acg.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.example.acg.R;

public class MainActivity extends Activity {

	private double totaldouble = 0;
	private double orderdouble = 0;
	private double alldouble = 0;

	public final static int BUY = 0;
	public final static int PAY = 1;
	public final static int OVER = 2;

	private final static int INSERT = 1;
	private final static int MODIFY = 2;

	private MyAdapter adapter;
	private SwipeMenuListView listView;
	private List<HashMap<String, Object>> data;
	private OrderService db = new OrderService(MainActivity.this);
	private String strmoney;
	private String ordermoney;
	private String allmoney;

	private Order o2;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Resources res = getResources();
		//
		// Random rand = new Random();
		// int s = rand.nextInt(3);
		// Bitmap img = BitmapFactory.decodeResource(res, R.drawable.head);
		// Order otmp = new Order(db.getCount(), "123", "2016-02-12", "锟秸帮拷",
		// "GSC",
		// "猫锟斤拷", 20, 1000, 1000 - 20, s, img);
		// db.addOrder(otmp);

		init();

		Intent intent = new Intent();
		intent.setClass(MainActivity.this, LongRunningService.class);
		startService(intent);

		totaldouble = alldouble - orderdouble;

		final NumberFormat totalmoney = new DecimalFormat("#,###,##0.00");

		strmoney = totalmoney.format(totaldouble);
		ordermoney = totalmoney.format(orderdouble);
		allmoney = totalmoney.format(alldouble);

		final TextView moneys = (TextView) findViewById(R.id.moneys);
		final TextView order = (TextView) findViewById(R.id.ordernumber);
		final TextView all = (TextView) findViewById(R.id.allnumber);
		final TextView cnt = (TextView) findViewById(R.id.cnt);
		moneys.setText(strmoney);
		order.setText(ordermoney);
		all.setText(allmoney);
		cnt.setText(String.valueOf(db.getCount()));

		adapter = new MyAdapter(this, data, R.layout.item, new String[] {
				"商品名称", "状态", "出货日期", "图片", "定金", "全款", "尾款", "版式", "制造商",
				"供货商" }, new int[] { R.id.name, R.id.state, R.id.outdate,
				R.id.icon, R.id.ordermoney, R.id.allmoney, R.id.needmoney,
				R.id.kind, R.id.whomake, R.id.wherebuy });

		listView = (SwipeMenuListView) this.findViewById(R.id.listView);
		adapter.setViewBinder(new MyViewBinder());
		listView.setAdapter(adapter);

		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);

				// create "update" item
				SwipeMenuItem beenorder = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				beenorder.setBackground(new ColorDrawable(Color.rgb(0xff, 0x89,
						0x15)));
				// set item width
				beenorder.setWidth(dp2px(90));
				// set item title
				beenorder.setTitle("已预订");
				// set item title fontsize
				beenorder.setTitleSize(18);
				// set item title font color
				beenorder.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(beenorder);

				// create "update" item
				SwipeMenuItem beenpay = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				beenpay.setBackground(new ColorDrawable(Color.rgb(0x66, 0xB3,
						0xFF)));
				// set item width
				beenpay.setWidth(dp2px(90));
				// set item title
				beenpay.setTitle("已补款");
				// set item title fontsize
				beenpay.setTitleSize(18);
				// set item title font color
				beenpay.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(beenpay);

				// create "update" item
				SwipeMenuItem beenok = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				beenok.setBackground(new ColorDrawable(Color.rgb(0x13, 0x22,
						0x34)));
				// set item width
				beenok.setWidth(dp2px(90));
				// set item title
				beenok.setTitle("已完成");
				// set item title fontsize
				beenok.setTitleSize(18);
				// set item title font color
				beenok.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(beenok);
			}
		};
		// set creator
		listView.setMenuCreator(creator);

		listView.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});

		listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				switch (index) {
				case 0:
					// delete
					HashMap<String, Object> item = data.get(position);
					delete(position);
					totaldouble = alldouble - orderdouble;
					strmoney = totalmoney.format(totaldouble);
					ordermoney = totalmoney.format(orderdouble);
					allmoney = totalmoney.format(alldouble);
					moneys.setText(strmoney);
					order.setText(ordermoney);
					all.setText(allmoney);
					cnt.setText(String.valueOf(db.getCount()));
					break;

				case 1:
					item = data.get(position);
					int no = (Integer) item.get("编号");				
					db.modifyOrderState(no, BUY);
					List<HashMap<String, Object>> newlist = null;
					newlist = mod(newlist);
					data.clear();
					data.addAll(newlist);
					adapter.notifyDataSetChanged();
					break;
				case 2:
					item = data.get(position);
					no = (Integer) item.get("编号");
					db.modifyOrderState(no, PAY);
					newlist = null;
					newlist = mod(newlist);
					data.clear();
					data.addAll(newlist);
					adapter.notifyDataSetChanged();
					break;
				case 3:
					item = data.get(position);
					no = (Integer) item.get("编号");
					db.modifyOrderState(no, OVER);
					newlist = null;
					newlist = mod(newlist);
					data.clear();
					data.addAll(newlist);
					adapter.notifyDataSetChanged();
					break;
				}
			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, Object> item = data.get(position);
				Intent tomodify = new Intent();
				tomodify.setClass(MainActivity.this, ModifyActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("dataorder", (Integer) item.get("编号"));
				tomodify.putExtras(bundle);
				startActivityForResult(tomodify, 2);
				return false;
			}
		});

		ImageView toLeft = (ImageView) findViewById(R.id.outleft);
		toLeft.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent toinsert = new Intent();
				toinsert.setClass(MainActivity.this, LeftActivity.class);
				startActivityForResult(toinsert, 1);
				// MainActivity.this.overridePendingTransition(R.anim.left_in,0);
			}
		});

		ImageView add = (ImageView) findViewById(R.id.add);
		add.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent toinsert = new Intent();
				toinsert.setClass(MainActivity.this, FloatActivity.class);
				startActivityForResult(toinsert, 1);
				MainActivity.this.overridePendingTransition(
						R.anim.push_bottom_in, 0);
			}
		});

	}

	private void delete(int position) {
		HashMap<String, Object> item = data.get(position);
		int no = (Integer) item.get("编号");
		db.deleteOrder(no);
		for (int i = no + 1; i < db.getCount() + 1; i++) {
			db.modifyOrder(i);
			Order o = db.findOrder(i);

		}

		List<HashMap<String, Object>> newlist = null;
		newlist = mod(newlist);
		data.clear();
		data.addAll(newlist);
		adapter.notifyDataSetChanged();
	}

	private List<HashMap<String, Object>> mod(
			List<HashMap<String, Object>> newlist) {
		orderdouble = 0;
		alldouble = 0;
		newlist = new ArrayList<HashMap<String, Object>>();
		List<Order> ol = db.findOrderList(0, db.getCount());
		for (Order os : ol) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			String state = null;
			switch (os.state) {
			case BUY:
				state = "已预订";
				break;
			case PAY:
				state = "已补款";
				break;
			case OVER:
				state = "已完成";
				break;
			default:
				state = "出错";
			}
			item.put("编号", os.no);
			item.put("商品名称", os.name);
			item.put("出货日期", os.outDate);
			item.put("版式", os.kind);
			item.put("制造商", os.whoMake);
			item.put("供货商", os.whereBuy);
			item.put("定金", os.orderMoney);
			item.put("全款", os.allMoney);
			item.put("尾款", os.needMoney);
			item.put("状态", state);
			item.put("图片", os.img);
			item.put("备注", os.other);
			orderdouble += os.orderMoney;
			alldouble += os.allMoney;
			newlist.add(item);
		}
		return newlist;
	}

	private void init() {
		data = new ArrayList<HashMap<String, Object>>();
		List<Order> ol = db.findOrderList(0, db.getCount());
		for (Order os : ol) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			String state = null;
			switch (os.state) {
			case BUY:
				state = "已预订";
				break;
			case PAY:
				state = "已补款";
				break;
			case OVER:
				state = "已完成";
				break;
			default:
				state = "出错";
			}
			item.put("编号", os.no);
			item.put("商品名称", os.name);
			item.put("出货日期", os.outDate);
			item.put("版式", os.kind);
			item.put("制造商", os.whoMake);
			item.put("供货商", os.whereBuy);
			item.put("定金", os.orderMoney);
			item.put("全款", os.allMoney);
			item.put("尾款", os.needMoney);
			item.put("状态", state);
			item.put("图片", os.img);
			item.put("备注", os.other);

			orderdouble += os.orderMoney;
			alldouble += os.allMoney;
			data.add(item);
		}

	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent da) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, da);
		if (requestCode == INSERT) {
			if (resultCode == 1) {
				List<HashMap<String, Object>> newlist = null;
				newlist = mod(newlist);
				data.clear();
				data.addAll(newlist);
				NumberFormat totalmoney = new DecimalFormat("#,###,##0.00");
				TextView moneys = (TextView) findViewById(R.id.moneys);
				TextView order = (TextView) findViewById(R.id.ordernumber);
				TextView all = (TextView) findViewById(R.id.allnumber);
				TextView cnt = (TextView) findViewById(R.id.cnt);
				totaldouble = alldouble - orderdouble;
				strmoney = totalmoney.format(totaldouble);
				ordermoney = totalmoney.format(orderdouble);
				allmoney = totalmoney.format(alldouble);
				moneys.setText(strmoney);
				order.setText(ordermoney);
				all.setText(allmoney);
				cnt.setText(String.valueOf(db.getCount()));
				adapter.notifyDataSetChanged();
			}
		}
		if (requestCode == MODIFY) {
			if (resultCode == 2) {
				List<HashMap<String, Object>> newlist = null;
				newlist = mod(newlist);
				data.clear();
				data.addAll(newlist);
				NumberFormat totalmoney = new DecimalFormat("#,###,##0.00");
				TextView moneys = (TextView) findViewById(R.id.moneys);
				TextView order = (TextView) findViewById(R.id.ordernumber);
				TextView all = (TextView) findViewById(R.id.allnumber);
				TextView cnt = (TextView) findViewById(R.id.cnt);
				totaldouble = alldouble - orderdouble;
				strmoney = totalmoney.format(totaldouble);
				ordermoney = totalmoney.format(orderdouble);
				allmoney = totalmoney.format(alldouble);
				moneys.setText(strmoney);
				order.setText(ordermoney);
				all.setText(allmoney);
				cnt.setText(String.valueOf(db.getCount()));
				adapter.notifyDataSetChanged();
			}
		}
	}

}
