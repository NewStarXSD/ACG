package com.acg.dropedit;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class WrapListView extends ListView {
	private int mWidth = 0;

	public WrapListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public WrapListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int height = getMeasuredHeight();
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		for (int i = 0; i < getChildCount(); i++) {
			int childWidth = getChildAt(i).getMeasuredWidth();
			mWidth = Math.max(mWidth, childWidth);
		}

		setMeasuredDimension(mWidth, height);
	}

	protected void setListWidth(int width) {
		mWidth = width;
	}
}
