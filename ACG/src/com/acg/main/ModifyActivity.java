package com.acg.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acg.access.BuyService;
import com.acg.access.MakeService;
import com.acg.access.Order;
import com.acg.access.OrderService;
import com.acg.access.WhereBuy;
import com.acg.access.WhoMake;
import com.acg.date.DateTimePickDialogUtil;
import com.acg.dropedit.DropEditText;
import com.acg.image.ImageTools;
import com.acg.lineedit.LineEditText;
import com.example.acg.R;

@SuppressLint({ "CutPasteId", "SimpleDateFormat" })
public class ModifyActivity extends Activity {

	private final int REQUEST_IMAGE = 1;
	private static final int SCALE = 5;

	private OrderService db = new OrderService(ModifyActivity.this);
	private int no;
	private TextView DateTime;
	private String initDateTime = new SimpleDateFormat("yyyy-MM-dd")
			.format(Calendar.getInstance().getTime());
	private DropEditText makeby;
	private DropEditText buyby;
	private List<String> mList1 = new ArrayList<String>();
	private List<String> mList2 = new ArrayList<String>();

	private ImageView imageView;

	private MakeService db1 = new MakeService(ModifyActivity.this);
	private BuyService db2 = new BuyService(ModifyActivity.this);

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.additem);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		init();

		Intent intent = this.getIntent();
		no = (Integer) intent.getSerializableExtra("dataorder");

		final LineEditText et1 = (LineEditText) findViewById(R.id.addordermoney);
		final LineEditText et2 = (LineEditText) findViewById(R.id.addallmoney);
		final Button et3 = (Button) findViewById(R.id.addstatekind);
		final LineEditText et4 = (LineEditText) findViewById(R.id.addgoodsname);
		final LineEditText et5 = (LineEditText) findViewById(R.id.addgoodskind);
		final TextView et6 = (TextView) findViewById(R.id.addgoodsdate);
		final DropEditText et7 = (DropEditText) findViewById(R.id.addgoodsmake);
		final DropEditText et8 = (DropEditText) findViewById(R.id.addgoodsbuy);
		final EditText et9 = (EditText) findViewById(R.id.addgoodsothers);

		imageView = (ImageView) findViewById(R.id.photo);

		final Order order = db.findOrder(no);

		et1.setText(String.valueOf(order.orderMoney));
		et2.setText(String.valueOf(order.allMoney));
		switch (order.state) {
		case 0:
			et3.setText("已预订");
			break;
		case 1:
			et3.setText("已补款");
			break;
		case 2:
			et3.setText("已完成");
			break;
		}
		et4.setText(order.name);
		et5.setText(order.kind);
		et6.setText(order.outDate);
		et7.setText(order.whoMake);
		et8.setText(order.whereBuy);
		et9.setText(order.other);
		imageView.setImageBitmap(order.img);

		DateTime = (TextView) findViewById(R.id.addgoodsdate);
		DateTime.setText(order.outDate);
		DateTime.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
						ModifyActivity.this, initDateTime);
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
				TextView tv = new TextView(ModifyActivity.this);
				tv.setText(mList1.get(position));
				return tv;
			}
		};
		ImageView make = (ImageView) findViewById(R.id.addmakeimage);
		make.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				String whoMake = makeby.getText();
				if (!db1.addMaker(new WhoMake(db2.getCount(), whoMake))) {
					Toast.makeText(ModifyActivity.this, "该制造商已存在",
							Toast.LENGTH_LONG).show();
				} else {
					mList2.add(whoMake);
				}
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
				TextView tv = new TextView(ModifyActivity.this);
				tv.setText(mList2.get(position));
				return tv;
			}
		};
		ImageView buy = (ImageView) findViewById(R.id.addbuyimage);
		buy.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				String whereBuy = buyby.getText();
				if (!db2.addBuy(new WhereBuy(db2.getCount(), whereBuy))) {
					Toast.makeText(ModifyActivity.this, "该供货商已存在",
							Toast.LENGTH_LONG).show();
				} else {
					mList2.add(whereBuy);
				}
			}
		});
		buyby.setAdapter(adapter2);

		Button cancel = (Button) findViewById(R.id.addcancel);
		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}
		});

		imageView.setOnClickListener(new OnClickListener() {

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
						ModifyActivity.this);
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
				Bitmap img = ((BitmapDrawable) imageView.getDrawable())
						.getBitmap();

				String str1 = et4.getText().toString();
				String str2 = et6.getText().toString();
				String str3 = et5.getText().toString();
				String str4 = et7.getText().toString();
				String str5 = et8.getText().toString();
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

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				long time = 0;
				try {
					time = sdf.parse(str2).getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (order.state == 0) {
					setReminder(false, order.no, time);
				}

				OrderService o1 = new OrderService(ModifyActivity.this);

				Order order = new Order(no, str1, str2, str3, str4, str5, dou6,
						dou7, dou8, state, img, str9);

				o1.modify(order);
				if (statestr.equals("已预订")) {
					setReminder(true, order.no, time);
				} else {
					setReminder(false, order.no, time);
				}
				Toast.makeText(ModifyActivity.this, "修改成功", Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent();
				setResult(2, intent);
				finish();
			}
		});
	}

	private void init() {
		List<WhoMake> ml = db1.findMakerList(0, db1.getCount());
		for (WhoMake ms : ml) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("编号", ms.no);
			item.put("制造商", ms.name);
			mList1.add(ms.name);
		}
		List<WhereBuy> bl = db2.findBuyList(0, db2.getCount());
		for (WhereBuy bs : bl) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("编号", bs.no);
			item.put("供货商", bs.name);
			mList2.add(bs.name);
		}
	}

	public void setReminder(boolean b, int no, long time) {
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent it = new Intent(ModifyActivity.this, MyReceiver.class);
		it.putExtra("no", no);
		PendingIntent pi = PendingIntent.getBroadcast(ModifyActivity.this, no,
				it, PendingIntent.FLAG_UPDATE_CURRENT);

		if (b) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(time);
			c.add(Calendar.HOUR, 8);
			am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
		} else {
			am.cancel(pi);
		}
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

						imageView.setImageBitmap(smallBitmap);
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
