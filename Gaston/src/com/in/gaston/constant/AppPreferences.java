package com.in.gaston.constant;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences 
{
	private static AppPreferences mPreferences = null;
	private SharedPreferences sharedPreferences;
	private Editor editor;



	private AppPreferences(Context nContext) 
	{
		this.sharedPreferences = nContext.getSharedPreferences(GastonConstant.SHARED_PREFS, Context.MODE_PRIVATE);
		this.editor = sharedPreferences.edit();
	}

	public static synchronized AppPreferences getInstance(Context nContext) {
		if (mPreferences == null) {
			mPreferences = new AppPreferences(nContext);
		}
		return mPreferences;
	}

	public Editor getEditor() {
		return editor;
	}

	public void commitEditor() {
		editor.commit();

	}

	/*
	 * device token
	 */

	public String getDeviceToken()
	{
		return sharedPreferences.getString("device_token", "");
	}

	public void setDeviceToken(String device_token)
	{
		editor.putString("device_token", device_token);
		editor.commit();
	}

	/*
	 * user id
	 */


	public String getUserId()
	{
		return sharedPreferences.getString("user_id", "");
	}

	public void setUserId(String user_id)
	{
		editor.putString("user_id", user_id);
		editor.commit();
	}

	/*
	 * user access token
	 */


	public String getUserAccessToken()
	{
		return sharedPreferences.getString("usser_access_token", "");
	}

	public void setUserAccessToken(String usser_access_token)
	{
		editor.putString("usser_access_token", usser_access_token);
		editor.commit();
	}

	//fb image
	public String getFBImageURL()
	{
		return sharedPreferences.getString("fb_img_url", "");
	}

	public void setFBImageURL(String fb_img_url)
	{
		editor.putString("fb_img_url", fb_img_url);
		editor.commit();
	}
	//fb id
	public String getFBId()
	{
		return sharedPreferences.getString("fb_id", "");
	}

	public void setFBId(String fb_id)
	{
		editor.putString("fb_id", fb_id);
		editor.commit();
	}	

	//isLogin
	public boolean isLogin()
	{
		return sharedPreferences.getBoolean("isLogin",false);
	}

	public void setisLogin(Boolean isLogin)
	{
		editor.putBoolean("isLogin", isLogin);
		editor.commit();
	}	
	//user img
	public String getUserIMG()
	{
		return sharedPreferences.getString("user_img", "");
	}

	public void setUserIMG(String user_img)
	{
		editor.putString("user_img", user_img);
		editor.commit();
	}	
	//user_profile_status
	//user img
	public String getUserProfileStatus()
	{
		return sharedPreferences.getString("userProfileStatus", "1");
	}

	public void setUserProfileStatus(String userProfileStatus)
	{
		editor.putString("userProfileStatus", userProfileStatus);
		editor.commit();
	}
	
	
	public int getAudDuration()
	{
		return sharedPreferences.getInt("aud_duration",0);
	}

	public void setAudDuration(int aud_duration)
	{
		editor.putInt("aud_duration", aud_duration);
		editor.commit();
	}	
	//
	public String getUserEmailId()
	{
		return sharedPreferences.getString("email", "");
	}

	public void setUserEmailId(String email)
	{
		editor.putString("email", email);
		editor.commit();
	}
	
	//isReal
	public boolean isReal()
	{
		return sharedPreferences.getBoolean("isReal",true);
	}

	public void setIsReal(Boolean isReal)
	{
		editor.putBoolean("isReal", isReal);
		editor.commit();
	}	
	
	
	//first name
	//
	public String getFirstName()
	{
		return sharedPreferences.getString("fn", "");
	}

	public void setFirstName(String fn)
	{
		editor.putString("fn", fn);
		editor.commit();
	}
	
	//last name
		//
		public String getLastName()
		{
			return sharedPreferences.getString("ln", "");
		}

		public void setLastName(String ln)
		{
			editor.putString("ln", ln);
			editor.commit();
		}
		
		//gender name
				//
				public String getGender()
				{
					return sharedPreferences.getString("gender", "");
				}

				public void setGender(String gender)
				{
					editor.putString("gender", gender);
					editor.commit();
				}
		
}
