package com.acg.access;

import java.io.Serializable;

import android.graphics.Bitmap;

public class Order implements Serializable {
	public String name;
	public String outDate;
	public String kind;
	public String whoMake;
	public String whereBuy;
	public double orderMoney;
	public double allMoney;
	public double needMoney;
	public int state;
	public Bitmap img;
	public int no;
	public String other;

	public Order(int no, String name, String outDate, String kind,
			String whoMake, String whereBuy, double orderMoney,
			double allMoney, double needMoney, int state, Bitmap img,
			String other) {
		this.no = no;
		this.name = name;
		this.outDate = outDate;
		this.kind = kind;
		this.whoMake = whoMake;
		this.whereBuy = whereBuy;
		this.orderMoney = orderMoney;
		this.allMoney = allMoney;
		this.needMoney = needMoney;
		this.state = state;
		this.img = img;
		this.other = other;
	}
}
