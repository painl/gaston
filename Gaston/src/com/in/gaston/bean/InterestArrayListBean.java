package com.in.gaston.bean;

import java.util.ArrayList;

public class InterestArrayListBean extends CommonBean 
{
	private ArrayList<InterestBean> arrayList;
	private int total_record;

	public int getTotal_record() {
		return total_record;
	}

	public void setTotal_record(int total_record) {
		this.total_record = total_record;
	}

	public ArrayList<InterestBean> getArrayList() {
		return arrayList;
	}

	public void setArrayList(ArrayList<InterestBean> arrayList) {
		this.arrayList = arrayList;
	}


}
