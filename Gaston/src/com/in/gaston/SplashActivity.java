package com.in.gaston;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.in.gaston.constant.AppPreferences;

public class SplashActivity extends Activity 
{
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		initVariables();
	}
	private void initVariables() 
	{
		final AppPreferences appPreferences = AppPreferences.getInstance(SplashActivity.this);
		mHandler = new Handler();
		mHandler.postDelayed(new Runnable() 
		{
			@Override
			public void run() 
			{
				if(appPreferences.isLogin())
				{
					Intent intent = new Intent(SplashActivity.this,DashBoardActivity.class);
					startActivity(intent);
					finish();
				}
				else
				{	Intent intent = new Intent(SplashActivity.this,SignInActivity.class);
				startActivity(intent);
				finish();
				}
			}
		}, 3000);
	}



}