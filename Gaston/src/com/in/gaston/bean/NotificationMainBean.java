package com.in.gaston.bean;

import java.util.ArrayList;

public class NotificationMainBean extends CommonBean
{
	private ArrayList<NotificationBean> mArrayList;
	private int total_record;
	public int getTotal_record() {
		return total_record;
	}
	public void setTotal_record(int total_record) {
		this.total_record = total_record;
	}
	public ArrayList<NotificationBean> getmArrayList() {
		return mArrayList;
	}
	public void setmArrayList(ArrayList<NotificationBean> mArrayList) {
		this.mArrayList = mArrayList;
	}

}
