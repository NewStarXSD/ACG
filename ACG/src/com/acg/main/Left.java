package com.acg.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.acg.R;

public class Left extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.left_bar);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		TextView back = (TextView) findViewById(R.id.alpha);
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});

	}
}
