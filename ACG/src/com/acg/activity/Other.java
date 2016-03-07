package com.acg.activity;

import com.example.acg.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class Other extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Intent intent = this.getIntent();
		String other = (String) intent.getSerializableExtra("other");
		TextView othertext = (TextView) findViewById(R.id.othertext);
		othertext.setText(other);
	}
}
