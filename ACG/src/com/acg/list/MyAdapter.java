package com.acg.list;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.acg.activity.Other;
import com.example.acg.R;

public class MyAdapter extends SimpleAdapter {

	private Context context;
	private List<HashMap<String, Object>> data;
	private int resource;
	private LayoutInflater inflater;

	public MyAdapter(Context context, List<HashMap<String, Object>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		this.data = data;
		this.resource = resource;
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(resource, null);
		}
		final HashMap<String, Object> item = data.get(position);

		ImageView other = (ImageView) convertView.findViewById(R.id.other);
		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView state = (TextView) convertView.findViewById(R.id.state);
		ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
		TextView outdate = (TextView) convertView.findViewById(R.id.outdate);
		TextView kind = (TextView) convertView.findViewById(R.id.kind);
		TextView whomake = (TextView) convertView.findViewById(R.id.whomake);
		TextView wherebuy = (TextView) convertView.findViewById(R.id.wherebuy);
		TextView ordermoney = (TextView) convertView.findViewById(R.id.ordermoney);
		TextView allmoney = (TextView) convertView.findViewById(R.id.allmoney);
		TextView needmoney = (TextView) convertView.findViewById(R.id.needmoney);
		
		name.setText((String)item.get("��Ʒ����"));
		state.setText((String)item.get("״̬"));
		icon.setImageBitmap((Bitmap)item.get("ͼƬ"));
		outdate.setText((String)item.get("��������"));
		kind.setText((String)item.get("��ʽ"));
		whomake.setText((String)item.get("������"));
		wherebuy.setText((String)item.get("������"));
		ordermoney.setText(((Double)item.get("����")).toString());
		allmoney.setText(((Double)item.get("ȫ��")).toString());
		needmoney.setText(((Double)item.get("β��")).toString());
		
		other.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent toother = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("other", (String) item.get("��ע"));
				toother.putExtras(bundle);
				toother.setClass(context, Other.class);
				context.startActivity(toother);
			}
		});
		return convertView;
	}
}
