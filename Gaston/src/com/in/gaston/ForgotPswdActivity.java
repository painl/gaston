package com.in.gaston;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.in.gaston.apptypeface.AppTypeFace;
import com.in.gaston.asyntask.ForgetPswdAsynTask;
import com.in.gaston.bean.CommonBean;
import com.in.gaston.bean.RequestBean;
import com.in.gaston.constant.AppParserConstant;
import com.in.gaston.network.NetworkStatus;
import com.in.gaston.view.CustomView;

public class ForgotPswdActivity extends Activity 
{
	private Button mSendBTN;
	private TextView mFogrotTV,mDefaultTV;
	private EditText mUserEmailId;
	private AppTypeFace mAppTypeFace;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_pswd);
		initVariables();
		iniView();
	}

	private void iniView() 
	{

		mDefaultTV = (TextView) findViewById(R.id.tv_default);
		mFogrotTV =  (TextView) findViewById(R.id.tv_forget_pswd);
		mSendBTN = (Button) findViewById(R.id.btn_send);
		mUserEmailId =  (EditText) findViewById(R.id.et_email_id);


		//type face

		mFogrotTV.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());
		mDefaultTV.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());
		mUserEmailId.setTypeface(mAppTypeFace.getTypeRobotoLight());
		mSendBTN.setTypeface(mAppTypeFace.getTypeRoboto_BOLD());




		mSendBTN.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				CustomView.hideSoftKeyboard(ForgotPswdActivity.this);

				if(validation(mUserEmailId.getText().toString()))
				{
					hitForgetPsedService();
				}
			}
		});


		/*
		 * 
		 */

		findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				CustomView.hideSoftKeyboard(ForgotPswdActivity.this);

				finish();
			}
		});
	}

	private void initVariables() 
	{
		mAppTypeFace = new AppTypeFace(ForgotPswdActivity.this);
	}
	@SuppressLint("NewApi")
	private void hitForgetPsedService() 
	{

		RequestBean requestBean = new RequestBean();
		if(NetworkStatus.isInternetOn(ForgotPswdActivity.this))
		{
			try
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(AppParserConstant.SERVICE_ACCESS_KEY,AppParserConstant.SERVICE_ACCESS_KEY_VAL);
				jsonObject.put(AppParserConstant.METHOD_NAME_KEY,AppParserConstant.METHOD_PASSCODE);
				jsonObject.put(AppParserConstant.USER_EMAIL_KEY,mUserEmailId.getText().toString());


				requestBean.setUrl(AppParserConstant.HIT_URL);
				requestBean.setLoader(true);
				requestBean.setJsonObject(jsonObject);
				requestBean.setActivity(ForgotPswdActivity.this);
				ForgetPswdAsynTask forgetPswdAsynTask = new ForgetPswdAsynTask(requestBean);

				if(VERSION.SDK_INT < VERSION_CODES.HONEYCOMB)
				{
					forgetPswdAsynTask.execute();
				}else{
					forgetPswdAsynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");	
				}

			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			Toast.makeText(ForgotPswdActivity.this,getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();

		}
	}

	public void setForgotPswdResponse(CommonBean result) 
	{
		Toast.makeText(ForgotPswdActivity.this,result.getResponseString(), Toast.LENGTH_SHORT).show();
		if(result.getStatus()==1)
		{
			Intent intent = new Intent(ForgotPswdActivity.this,PassCodeActivity.class);
			intent.putExtra(AppParserConstant.USER_EMAIL_KEY, mUserEmailId.getText().toString());
			startActivityForResult(intent,1);
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1)
		{
			if(resultCode==RESULT_OK)
			{
				finish();
			}
		}
	}


	private boolean validation(String emailId) 
	{

		if(emailId.equalsIgnoreCase("")||emailId.length()<0)
		{
			Toast.makeText(this,R.string.toast_email_id, Toast.LENGTH_SHORT).show();

			return false;
		}
		if(emailId.length()>0)
		{
			if(!Patterns.EMAIL_ADDRESS.matcher(emailId).matches())
			{
				Toast.makeText(ForgotPswdActivity.this, R.string.toast_email_id_valid, Toast.LENGTH_SHORT).show();
				return false;
			}
		}

		return true;
	}
}
