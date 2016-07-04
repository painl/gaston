package com.in.gaston.bean;

import java.util.ArrayList;

public class ListLikeUnlikeCommentBean extends CommentBean
{
	int total_records;
	ArrayList<ListLikeUnlikeCommentObjBean> arrayList ;
	public ArrayList<ListLikeUnlikeCommentObjBean> getArrayList() {
		return arrayList;
	}

	public void setArrayList(ArrayList<ListLikeUnlikeCommentObjBean> arrayList) {
		this.arrayList = arrayList;
	}

	public int getTotal_records() {
		return total_records;
	}

	public void setTotal_records(int total_records) {
		this.total_records = total_records;
	}

}
