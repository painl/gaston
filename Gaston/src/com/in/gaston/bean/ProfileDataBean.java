package com.in.gaston.bean;

import java.util.ArrayList;

public class ProfileDataBean extends CommonBean 
{
	private String user_image,user_private_image,private_gender,private_desc;
	public String getPrivate_gender() {
		return private_gender;
	}
	public void setPrivate_gender(String private_gender) {
		this.private_gender = private_gender;
	}
	public String getPrivate_desc() {
		return private_desc;
	}
	public void setPrivate_desc(String private_desc) {
		this.private_desc = private_desc;
	}
	public String getUser_private_image() {
		return user_private_image;
	}
	public void setUser_private_image(String user_private_image) {
		this.user_private_image = user_private_image;
	}
	private String user_description;
	public String getUser_description() {
		return user_description;
	}
	public void setUser_description(String user_description) {
		this.user_description = user_description;
	}
	public String getUser_image() {
		return user_image;
	}
	public void setUser_image(String user_image) {
		this.user_image = user_image;
	}
	private String user_fname,private_name,user_id,user_lname,user_email,user_gender,user_contact,user_fb_id,visibility_status,notification_status,created_on;
public String getPrivate_name() {
		return private_name;
	}
	public void setPrivate_name(String private_name) {
		this.private_name = private_name;
	}
private int total_record;
	public int getTotal_record() {
	return total_record;
}
public void setTotal_record(int total_record) {
	this.total_record = total_record;
}
	public String getUser_fname() {
		return user_fname;
	}
	public void setUser_fname(String user_fname) {
		this.user_fname = user_fname;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_lname() {
		return user_lname;
	}
	public void setUser_lname(String user_lname) {
		this.user_lname = user_lname;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public String getUser_gender() {
		return user_gender;
	}
	public void setUser_gender(String user_gender) {
		this.user_gender = user_gender;
	}
	public String getUser_contact() {
		return user_contact;
	}
	public void setUser_contact(String user_contact) {
		this.user_contact = user_contact;
	}
	public String getUser_fb_id() {
		return user_fb_id;
	}
	public void setUser_fb_id(String user_fb_id) {
		this.user_fb_id = user_fb_id;
	}
	public String getVisibility_status() {
		return visibility_status;
	}
	public void setVisibility_status(String visibility_status) {
		this.visibility_status = visibility_status;
	}
	public String getNotification_status() {
		return notification_status;
	}
	public void setNotification_status(String notification_status) {
		this.notification_status = notification_status;
	}
	public String getCreated_on() {
		return created_on;
	}
	public void setCreated_on(String created_on) {
		this.created_on = created_on;
	}
	public ArrayList<InterestBean> getmArrayList() {
		return mArrayList;
	}
	public void setmArrayList(ArrayList<InterestBean> mArrayList) {
		this.mArrayList = mArrayList;
	}
	private ArrayList<InterestBean> mArrayList;
}
