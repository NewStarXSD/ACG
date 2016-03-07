package com.acg.alarm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.acg.access.Order;
import com.acg.access.OrderService;
import com.acg.activity.MainActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class LongRunningService extends Service {
	Timer timer;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// 定时更新
				int[] order = getOrder();
				int[] state = getState();
				String[] time = getTime();
				// 发送广播
				for (int i = 0; i < order.length; ++i) {
					int item;
					item = order[i];
					if (state[i] == MainActivity.BUY) {
						long date = 0;
						try {
							date = sdf.parse(time[i]).getTime();
						} catch (ParseException e) {
							e.printStackTrace();
						}
						AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
						Intent it = new Intent(LongRunningService.this,
								MyReceiver.class);
						it.putExtra("no", item);
						PendingIntent pi = PendingIntent.getBroadcast(
								LongRunningService.this, item, it,
								PendingIntent.FLAG_CANCEL_CURRENT);
						Calendar c = Calendar.getInstance();
						c.setTimeInMillis(date);
						c.add(Calendar.HOUR, 8);
						am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
					}
				}

			}
		}, 0, 20 * 1000);
		return super.onStartCommand(intent, flags, startId);
	}

	protected int[] getState() {
		OrderService db = new OrderService(LongRunningService.this);
		List<Order> ol = db.findOrderList(0, db.getCount());
		int[] state = new int[db.getCount()];
		int i = 0;
		for (Order os : ol) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			state[i] = os.state;
			i++;
		}
		return state;
	}

	protected int[] getOrder() {
		OrderService db = new OrderService(LongRunningService.this);
		List<Order> ol = db.findOrderList(0, db.getCount());
		int[] no = new int[db.getCount()];
		int i = 0;
		for (Order os : ol) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			no[i] = os.no;
			i++;
		}
		return no;
	}

	protected String[] getTime() {
		OrderService db = new OrderService(LongRunningService.this);
		List<Order> ol = db.findOrderList(0, db.getCount());
		String[] time = new String[db.getCount()];
		int i = 0;
		for (Order os : ol) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			time[i] = os.outDate;
			i++;
		}
		return time;
	}

	public void onDestroy() {
		// TODO Auto-generated method stub
		stopForeground(true);
		Intent intent = new Intent("com.acg.service.destroy");
		sendBroadcast(intent);
		if (timer != null) {
			timer.cancel();
		}
		super.onDestroy();
	}
}