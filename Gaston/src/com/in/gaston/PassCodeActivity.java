package com.in.gaston;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
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
import com.in.gaston.constant.AppPreferences;
import com.in.gaston.network.NetworkStatus;
import com.in.gaston.view.CustomView;

public class PassCodeActivity extends Activity
{
	private EditText mPasscodeET,mCofirmPswdET,mNewPswdET;
	private Button mOkBTN;
	private AppPreferences mAppPreferences;
	private String mEmailId;
	private TextView mFogrotTV,mDefaultTV;

	private AppTypeFace mAppTypeFace;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_passcode);
		initVariables();
		initView();
	}

	private void initView() 
	{
		mDefaultTV = (TextView) findViewById(R.id.tv_default);
		mFogrotTV =  (TextView) findViewById(R.id.tv_forget_pswd);
		mPasscodeET	 = (EditText) findViewById(R.id.et_passcode);
		mNewPswdET	 = (EditText) findViewById(R.id.et_new_pswd);
		mCofirmPswdET	 = (EditText) findViewById(R.id.et_confirm_pswd);
		mOkBTN	 = (Button) findViewById(R.id.btn_ok);


		//setType face

		mDefaultTV.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());
		mFogrotTV.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());
		mPasscodeET.setTypeface(mAppTypeFace.getTypeRobotoLight());
		mNewPswdET.setTypeface(mAppTypeFace.getTypeRobotoLight());
		mCofirmPswdET.setTypeface(mAppTypeFace.getTypeRobotoLight());
		mOkBTN.setTypeface(mAppTypeFace.getTypeRoboto_BOLD());






		mOkBTN.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) 
			{					
				CustomView.hideSoftKeyboard(PassCodeActivity.this);

				if(validation(mPasscodeET.getText().toString(),mNewPswdET.getText().toString(),mCofirmPswdET.getText().toString()))
				{
					hitReSetPswdService();
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
				CustomView.hideSoftKeyboard(PassCodeActivity.this);

				finish();
			}
		});

	}

	private void initVariables() 
	{

		mAppTypeFace = new AppTypeFace(PassCodeActivity.this);
		mAppPreferences = AppPreferences.getInstance(PassCodeActivity.this);
		mEmailId = 	getIntent().getStringExtra(AppParserConstant.USER_EMAIL_KEY);
	}


	private boolean validation(String passcode, String newPswd,String confirmPswd) 
	{
		if(passcode.equalsIgnoreCase("")||passcode.length()<0)
		{
			Toast.makeText(PassCodeActivity.this, R.string.toast_passcode, Toast.LENGTH_SHORT).show();

			return false;
		}

		else if (newPswd.equalsIgnoreCase("")||newPswd.length()<0) 
		{
			Toast.makeText(PassCodeActivity.this, R.string.toast_new_pswd, Toast.LENGTH_SHORT).show();

			return false;

		}
		else if(newPswd.length()<6) 
		{
			Toast.makeText(PassCodeActivity.this, R.string.toast_pswd_len, Toast.LENGTH_SHORT).show();

			return false;
		}

		else if (confirmPswd.equalsIgnoreCase("")||confirmPswd.length()<0) 
		{
			Toast.makeText(PassCodeActivity.this, R.string.toast_confirm_pswd, Toast.LENGTH_SHORT).show();

			return false;

		}

		else if(!newPswd.equals(confirmPswd))
		{
			Toast.makeText(PassCodeActivity.this, R.string.text_match_pswd, Toast.LENGTH_SHORT).show();
			return false;
		}


		return true;
	}

	@SuppressLint("NewApi")
	private void hitReSetPswdService() 
	{

		RequestBean requestBean = new RequestBean();
		if(NetworkStatus.isInternetOn(PassCodeActivity.this))
		{
			try
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(AppParserConstant.SERVICE_ACCESS_KEY,AppParserConstant.SERVICE_ACCESS_KEY_VAL);
				jsonObject.put(AppParserConstant.METHOD_NAME_KEY,AppParserConstant.METHOD_RESET_PSWD);
				jsonObject.put(AppParserConstant.USER_EMAIL_KEY,mEmailId);
				jsonObject.put(AppParserConstant.USER_PASSCODE_KEY,mPasscodeET.getText().toString());
				jsonObject.put(AppParserConstant.USER_NEW_PSWD_KEY,mNewPswdET.getText().toString());

				requestBean.setUrl(AppParserConstant.HIT_URL);
				requestBean.setLoader(true);
				requestBean.setJsonObject(jsonObject);
				requestBean.setActivity(PassCodeActivity.this);
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
			Toast.makeText(PassCodeActivity.this,getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();

		}

	}

	public void setResetPswdResponse(CommonBean result) 
	{
		if(result!=null)
		{
			Toast.makeText(PassCodeActivity.this, result.getResponseString(), Toast.LENGTH_SHORT).show();
			if(result.getStatus()==1)
			{
				Intent intent = new Intent(PassCodeActivity.this,SignInActivity.class);
				startActivity(intent);
				finish();
			}
			else
			{
				mPasscodeET.setText("");
				mCofirmPswdET.setText("");
				mNewPswdET.setText("");
			}
		}
	}


}
