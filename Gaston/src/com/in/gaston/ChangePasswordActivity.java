package com.in.gaston;

import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ChangePasswordActivity extends Activity 
{
	private EditText mOldPswd;
	private EditText mNewPswd;
	private EditText mConfirmPswd;
	private Button mSaveChanges;
	private ImageView mBackBTN;
	private AppPreferences mAppPreferences;
	private AppTypeFace mAppTypeFace;
	private TextView mHeading;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);

		mAppTypeFace = new AppTypeFace(ChangePasswordActivity.this);		
		mAppPreferences = AppPreferences.getInstance(ChangePasswordActivity.this);

		mHeading =  (TextView)findViewById(R.id.tv_forget_pswd);
		mOldPswd =  (EditText)findViewById(R.id.et_old_pswd);
		mNewPswd =  (EditText)findViewById(R.id.et_new_pswd);
		mConfirmPswd =  (EditText)findViewById(R.id.et_confirm_pswd);
		mSaveChanges =  (Button)findViewById(R.id.btn_ok);
		mBackBTN =  (ImageView)findViewById(R.id.img_back);

		
		mHeading.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());
		mOldPswd.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());
		mNewPswd.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());
		mConfirmPswd.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());

		

		mSaveChanges.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{

				CustomView.hideSoftKeyboard(ChangePasswordActivity.this);

				if(validation(mOldPswd.getText().toString(),mNewPswd.getText().toString(),mConfirmPswd.getText().toString()))
				{
					hitChangePswdService();
				}
			}


		});


		mBackBTN.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				finish();
			}
		});
	}

	private void hitChangePswdService() 
	{
		/*
		 * service_access_key, method, user_access_token, user_id , user_old_password , user_new_password
		 */
		RequestBean requestBean = new RequestBean();
		if(NetworkStatus.isInternetOn(ChangePasswordActivity.this))
		{
			try
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(AppParserConstant.SERVICE_ACCESS_KEY,AppParserConstant.SERVICE_ACCESS_KEY_VAL);
				jsonObject.put(AppParserConstant.METHOD_NAME_KEY,"changepassword");
				jsonObject.put(AppParserConstant.USER_ACCESS_TOKEN_KEY,mAppPreferences.getUserAccessToken());
				jsonObject.put(AppParserConstant.USER_ID_KEY,mAppPreferences.getUserId());
				jsonObject.put("user_old_password",mOldPswd.getText().toString());
				jsonObject.put("user_new_password",mNewPswd.getText().toString());

				requestBean.setUrl(AppParserConstant.HIT_URL);
				requestBean.setLoader(true);
				requestBean.setJsonObject(jsonObject);
				requestBean.setActivity(ChangePasswordActivity.this);
				ForgetPswdAsynTask signInAsynTask = new ForgetPswdAsynTask(requestBean);

				if(VERSION.SDK_INT < VERSION_CODES.HONEYCOMB)
				{
					signInAsynTask.execute();
				}else{
					signInAsynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");	
				}

			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			Toast.makeText(ChangePasswordActivity.this,getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();
		}

	}
	private boolean validation(String old_pswd, String newPswd,String confirmPswd) 
	{
		if(old_pswd.equalsIgnoreCase("")||old_pswd.length()<0)
		{
			Toast.makeText(ChangePasswordActivity.this, R.string.toast_passcode, Toast.LENGTH_SHORT).show();

			return false;
		}

		else if (newPswd.equalsIgnoreCase("")||newPswd.length()<0) 
		{
			Toast.makeText(ChangePasswordActivity.this, R.string.toast_new_pswd, Toast.LENGTH_SHORT).show();

			return false;

		}
		else if(newPswd.length()<6) 
		{
			Toast.makeText(ChangePasswordActivity.this, R.string.toast_pswd_len, Toast.LENGTH_SHORT).show();

			return false;
		}

		else if (confirmPswd.equalsIgnoreCase("")||confirmPswd.length()<0) 
		{
			Toast.makeText(ChangePasswordActivity.this, R.string.toast_confirm_pswd, Toast.LENGTH_SHORT).show();

			return false;

		}

		else if(!newPswd.equals(confirmPswd))
		{
			Toast.makeText(ChangePasswordActivity.this, R.string.text_match_pswd, Toast.LENGTH_SHORT).show();
			return false;
		}


		return true;
	}

	public void setChangePasswordActivityResponse(CommonBean result) 
	{


		Toast.makeText(ChangePasswordActivity.this,result.getResponseString(),Toast.LENGTH_SHORT).show();
		if(result!=null)
		{
			if(result.getStatus()==1)
			{
				finish();
			}
		}

	}
}
