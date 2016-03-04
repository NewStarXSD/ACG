package com.acg.main;

import com.acg.access.Order;
import com.acg.access.OrderService;
import com.example.acg.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

	private NotificationManager manager;

	public void onReceive(Context context, Intent intent) {
		OrderService db = new OrderService(context);

		int no = (Integer) intent.getSerializableExtra("no");

		Order order = db.findOrder(no);

		Toast.makeText(context, order.name + "需要补款了", Toast.LENGTH_LONG).show();

		manager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		Intent playIntent = new Intent(context, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, no,
				playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context);
		builder.setContentTitle(order.name)
				.setContentText(order.name + "需要补款" + order.needMoney + "元")
				.setSmallIcon(R.drawable.logo).setLargeIcon(order.img)
				.setDefaults(Notification.DEFAULT_VIBRATE)
				.setContentIntent(pendingIntent)
				// .setAutoCancel(true)
				.setSubText("补款截止日期" + order.outDate);
		manager.notify(no, builder.build());

	}

}
