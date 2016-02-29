package com.acg.main;

import com.example.acg.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Other extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other);

		Intent intent = this.getIntent();
		String other = (String) intent.getSerializableExtra("other");
		TextView othertext = (TextView) findViewById(R.id.othertext);
		othertext.setText(other);
	}
}
