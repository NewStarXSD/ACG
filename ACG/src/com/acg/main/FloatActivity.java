package com.acg.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acg.access.Order;
import com.acg.access.OrderService;
import com.acg.date.DateTimePickDialogUtil;
import com.acg.dropedit.DropEditText;
import com.acg.image.ImageTools;
import com.acg.lineedit.LineEditText;
import com.example.acg.R;

public class FloatActivity extends Activity {

	private final int REQUEST_IMAGE = 1;
	private static final int SCALE = 5;

	private int flag = 0;

	private TextView DateTime;
	private String initDateTime = new SimpleDateFormat("yyyy-MM-dd")
			.format(Calendar.getInstance().getTime());
	private DropEditText makeby;
	private DropEditText buyby;
	private List<String> mList1 = new ArrayList<String>();
	private List<String> mList2 = new ArrayList<String>();

	private ImageView photo = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.additem);

		DateTime = (TextView) findViewById(R.id.addgoodsdate);
		DateTime.setText(initDateTime);
		DateTime.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
						FloatActivity.this, initDateTime);
				dateTimePicKDialog.dateTimePicKDialog(DateTime);

			}
		});

		makeby = (DropEditText) findViewById(R.id.addgoodsmake);
		final BaseAdapter adapter1 = new BaseAdapter() {

			public int getCount() {
				return mList1.size();
			}

			public Object getItem(int position) {
				return mList1.get(position);
			}

			public long getItemId(int position) {
				return position;
			}

			public View getView(int position, View convertView, ViewGroup parent) {
				TextView tv = new TextView(FloatActivity.this);
				tv.setText(mList1.get(position));
				return tv;
			}
		};
		ImageView make = (ImageView) findViewById(R.id.addmakeimage);
		make.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				String whoMake = makeby.getText();
				mList1.add(whoMake);
			}
		});
		makeby.setAdapter(adapter1);

		buyby = (DropEditText) findViewById(R.id.addgoodsbuy);
		final BaseAdapter adapter2 = new BaseAdapter() {

			public int getCount() {
				return mList2.size();
			}

			public Object getItem(int position) {
				return mList2.get(position);
			}

			public long getItemId(int position) {
				return position;
			}

			public View getView(int position, View convertView, ViewGroup parent) {
				TextView tv = new TextView(FloatActivity.this);
				tv.setText(mList2.get(position));
				return tv;
			}
		};
		ImageView buy = (ImageView) findViewById(R.id.addbuyimage);
		buy.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				String whoMake = buyby.getText();
				mList2.add(whoMake);
			}
		});
		buyby.setAdapter(adapter2);

		Button cancel = (Button) findViewById(R.id.addcancel);
		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}
		});
		photo = (ImageView) findViewById(R.id.photo);
		photo.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setType("image/*");
				startActivityForResult(intent, REQUEST_IMAGE);
			}
		});

		final Button states = (Button) findViewById(R.id.addstatekind);
		states.setOnClickListener(new OnClickListener() {

			int item = 0;

			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						FloatActivity.this);
				builder.setTitle("请选择状态");
				final String[] st = { "已预订", "已补款", "已完成" };
				builder.setSingleChoiceItems(st, 0,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								item = which;
							}
						});
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								states.setText(st[item]);
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								return;
							}
						});
				builder.show();
			}
		});

		Button ok = (Button) findViewById(R.id.addok);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {

				LineEditText et1 = (LineEditText) findViewById(R.id.addordermoney);
				LineEditText et2 = (LineEditText) findViewById(R.id.addallmoney);
				Button et3 = (Button) findViewById(R.id.addstatekind);
				LineEditText et4 = (LineEditText) findViewById(R.id.addgoodsname);
				LineEditText et5 = (LineEditText) findViewById(R.id.addgoodskind);
				TextView et6 = (TextView) findViewById(R.id.addgoodsdate);
				DropEditText et7 = (DropEditText) findViewById(R.id.addgoodsmake);
				DropEditText et8 = (DropEditText) findViewById(R.id.addgoodsbuy);
				EditText et9 = (EditText) findViewById(R.id.addgoodsothers);

				ImageView imageView = (ImageView) findViewById(R.id.photo);
				Bitmap img = ((BitmapDrawable) imageView.getDrawable())
						.getBitmap();

				if (flag == 0) {
					img = BitmapFactory.decodeResource(getResources(),
							R.drawable.logo);
				}

				String str1 = et4.getText().toString();
				String str2 = et6.getText().toString();
				String str3 = et5.getText().toString();
				String str4 = et7.getText().toString();
				String str5 = et8.getText().toString();
				String str6 = et1.getText().toString();
				String str7 = et2.getText().toString();
				if (str6.equals("")) {
					return;
				}
				if (str7.equals("")) {
					return;
				}
				double dou6 = Double.parseDouble(et1.getText().toString());
				double dou7 = Double.parseDouble(et2.getText().toString());
				double dou8 = dou7 - dou6;
				String str9 = et9.getText().toString();
				String statestr = et3.getText().toString();
				int state = 4;
				if (statestr.equals("已预订")) {
					state = 0;
				} else if (statestr.equals("已补款")) {
					state = 1;
				} else if (statestr.equals("已完成")) {
					state = 2;
				}

				OrderService o1 = new OrderService(FloatActivity.this);

				Order order = new Order(o1.getCount(), str1, str2, str3, str4,
						str5, dou6, dou7, dou8, state, img, str9);

				o1.addOrder(order);
				Toast.makeText(FloatActivity.this, "添加成功", Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent();
				setResult(1, intent);
				finish();
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_IMAGE:
				ContentResolver resolver = getContentResolver();
				Uri originalUri = data.getData();
				try {
					Bitmap phototmp = MediaStore.Images.Media.getBitmap(
							resolver, originalUri);
					if (phototmp != null) {
						Bitmap smallBitmap = ImageTools.zoomBitmap(phototmp,
								phototmp.getWidth() / SCALE,
								phototmp.getHeight() / SCALE);
						phototmp.recycle();
						flag = 1;
						photo.setImageBitmap(smallBitmap);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		}
	}
}
