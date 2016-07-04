package com.in.gaston.bean;

import java.util.ArrayList;

public class InterestDeatailBean extends CommonBean 
{

	private int totalRecord,interest_report ;
	
	public int getInterest_report() {
		return interest_report;
	}
	public void setInterest_report(int interest_report) {
		this.interest_report = interest_report;
	}
	private String interest_id,interest_name,interest_desc,interest_created_on,interest_image,total_subscribe,allow_text,allow_audio,allow_picture;
	private String InterestCreateUserIMG;
	private int user_profile_status;
	public int getUser_profile_status() {
		return user_profile_status;
	}
	public void setUser_profile_status(int user_profile_status) {
		this.user_profile_status = user_profile_status;
	}
	private String UserName;
	private int user_subscibed_status;
	public int getUser_subscibed_status() {
		return user_subscibed_status;
	}
	public void setUser_subscibed_status(int user_subscibed_status) {
		this.user_subscibed_status = user_subscibed_status;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getInterestCreateUserIMG() {
		return InterestCreateUserIMG;
	}
	public void setInterestCreateUserIMG(String interestCreateUserIMG) {
		InterestCreateUserIMG = interestCreateUserIMG;
	}
	public String getAllow_text() {
		return allow_text;
	}
	public void setAllow_text(String allow_text) {
		this.allow_text = allow_text;
	}
	public String getAllow_audio() {
		return allow_audio;
	}
	public void setAllow_audio(String allow_audio) {
		this.allow_audio = allow_audio;
	}
	public String getAllow_picture() {
		return allow_picture;
	}
	public void setAllow_picture(String allow_picture) {
		this.allow_picture = allow_picture;
	}
	public String getInterest_created_on() {
		return interest_created_on;
	}
	public void setInterest_created_on(String interest_created_on) {
		this.interest_created_on = interest_created_on;
	}
	public String getInterest_image() {
		return interest_image;
	}
	public void setInterest_image(String interest_image) {
		this.interest_image = interest_image;
	}
	public String getInterest_id() {
		return interest_id;
	}
	public void setInterest_id(String interest_id) {
		this.interest_id = interest_id;
	}
	public int getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	public String getInterest_name() {
		return interest_name;
	}
	public void setInterest_name(String interest_name) {
		this.interest_name = interest_name;
	}
	public String getInterest_desc() {
		return interest_desc;
	}
	public void setInterest_desc(String interest_desc) {
		this.interest_desc = interest_desc;
	}

	public String getTotal_subscribe() {
		return total_subscribe;
	}
	public void setTotal_subscribe(String total_subscribe) {
		this.total_subscribe = total_subscribe;
	}
	private ArrayList<InteresCommentBean> mArrayList ;
	public int getTotal_record() {
		return totalRecord;
	}
	public void setTotal_record(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	public ArrayList<InteresCommentBean> getmArrayList() {
		return mArrayList;
	}
	public void setmArrayList(ArrayList<InteresCommentBean> mArrayList) {
		this.mArrayList = mArrayList;
	}


}
