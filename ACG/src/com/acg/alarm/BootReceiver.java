package com.acg.alarm;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("com.acg.service.destroy")) {
			Intent sevice = new Intent(context, LongRunningService.class);
			sevice.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(sevice);
		}

	}

}