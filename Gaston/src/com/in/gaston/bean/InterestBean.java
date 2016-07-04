package com.in.gaston.bean;

public class InterestBean extends CommonBean 
{
	public String interest_id="",interest_image,interest_name,interest_description,user_id,user_profile_status,password,pass_protected,allow_audio,allow_image,allow_text,created_on,user_lname,user_fname,user_gender,user_image,user_private_name,user_private_image,total_subscribed;
	public int total_record;
	private int user_subscribe_status;
	public int getUser_subscribe_status() {
		return user_subscribe_status;
	}

	public void setUser_subscribe_status(int user_subscribe_status) {
		this.user_subscribe_status = user_subscribe_status;
	}

	public int getTotal_record() {
		return total_record;
	}

	public void setTotal_record(int total_record) {
		this.total_record = total_record;
	}

	public String getInterest_id() {
		return interest_id;
	}

	public void setInterest_id(String interest_id) {
		this.interest_id = interest_id;
	}

	public String getInterest_image() {
		return interest_image;
	}

	public void setInterest_image(String interest_image) {
		this.interest_image = interest_image;
	}

	public String getInterest_name() {
		return interest_name;
	}

	public void setInterest_name(String interest_name) {
		this.interest_name = interest_name;
	}

	public String getInterest_description() {
		return interest_description;
	}

	public void setInterest_description(String interest_description) {
		this.interest_description = interest_description;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_profile_status() {
		return user_profile_status;
	}

	public void setUser_profile_status(String user_profile_status) {
		this.user_profile_status = user_profile_status;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPass_protected() {
		return pass_protected;
	}

	public void setPass_protected(String pass_protected) {
		this.pass_protected = pass_protected;
	}

	public String getAllow_audio() {
		return allow_audio;
	}

	public void setAllow_audio(String allow_audio) {
		this.allow_audio = allow_audio;
	}

	public String getAllow_image() {
		return allow_image;
	}

	public void setAllow_image(String allow_image) {
		this.allow_image = allow_image;
	}

	public String getAllow_text() {
		return allow_text;
	}

	public void setAllow_text(String allow_text) {
		this.allow_text = allow_text;
	}

	public String getCreated_on() {
		return created_on;
	}

	public void setCreated_on(String created_on) {
		this.created_on = created_on;
	}

	public String getUser_lname() {
		return user_lname;
	}

	public void setUser_lname(String user_lname) {
		this.user_lname = user_lname;
	}

	public String getUser_fname() {
		return user_fname;
	}

	public void setUser_fname(String user_fname) {
		this.user_fname = user_fname;
	}

	public String getUser_gender() {
		return user_gender;
	}

	public void setUser_gender(String user_gender) {
		this.user_gender = user_gender;
	}

	public String getUser_image() {
		return user_image;
	}

	public void setUser_image(String user_image) {
		this.user_image = user_image;
	}

	public String getUser_private_name() {
		return user_private_name;
	}

	public void setUser_private_name(String user_private_name) {
		this.user_private_name = user_private_name;
	}

	public String getUser_private_image() {
		return user_private_image;
	}

	public void setUser_private_image(String user_private_image) {
		this.user_private_image = user_private_image;
	}

	public String getTotal_subscribed() {
		return total_subscribed;
	}

	public void setTotal_subscribed(String total_subscribed) {
		this.total_subscribed = total_subscribed;
	}

}
