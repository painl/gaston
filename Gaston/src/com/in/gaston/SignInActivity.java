package com.in.gaston;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.in.gaston.apptypeface.AppTypeFace;
import com.in.gaston.asyntask.SignInAsynTask;
import com.in.gaston.bean.ProfileDataBean;
import com.in.gaston.bean.RequestBean;
import com.in.gaston.bean.SignUpBean;
import com.in.gaston.constant.AppParserConstant;
import com.in.gaston.constant.AppPreferences;
import com.in.gaston.constant.GastonConstant;
import com.in.gaston.network.NetworkStatus;
import com.in.gaston.view.CustomView;
import com.sromku.simple.fb.Permission.Type;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.entities.Profile.Properties;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.sromku.simple.fb.utils.PictureAttributes;
import com.sromku.simple.fb.utils.PictureAttributes.PictureType;

public class SignInActivity extends Activity implements OnClickListener
{
	private Button mLoginBTN;
	private AppPreferences mAppPreferences;
	private TextView mForgetPswdTV,mRegisterTV,mFBLoginTV;
	private EditText mEmailEt,mPswdET;
	private Intent mIntent;
	private int mUserTypeLogin = 0;
	private SimpleFacebook mSimpleFacebook;
	private AppTypeFace mAppTypeFace;
	private LinearLayout mRegisterLinearLayout;
	private String mFbID = "", mEmailID="";
	private ProgressDialog mProgressBar;
	private String mCurrentScore="";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);

		initvariables();
		getDeviceToken();
		initView();

	}


	private void initView() 
	{
		mEmailEt = (EditText) findViewById(R.id.et_email);
		mPswdET = (EditText) findViewById(R.id.et_pswd);
		mLoginBTN = (Button) findViewById(R.id.btn_login);
		mForgetPswdTV = (TextView) findViewById(R.id.tv_forget_pswd);
		mRegisterTV = (TextView) findViewById(R.id.tv_register_here);
		mFBLoginTV = (TextView) findViewById(R.id.btn_fb_login);
		mRegisterLinearLayout = (LinearLayout) findViewById(R.id.ll_register_here);



		//setType face
		mEmailEt.setTypeface(mAppTypeFace.getTypeRobotoLight());		
		mPswdET.setTypeface(mAppTypeFace.getTypeRobotoLight());		
		mForgetPswdTV.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());	
		mRegisterTV.setTypeface(mAppTypeFace.getTypeRobotoLight());	

		mLoginBTN.setTypeface(mAppTypeFace.getTypeRoboto_BOLD());		
		mFBLoginTV.setTypeface(mAppTypeFace.getTypeRoboto_BOLD());		

		mPswdET.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) 
			{
				if(actionId==EditorInfo.IME_ACTION_DONE)
				{

					mUserTypeLogin = 0;
					CustomView.hideSoftKeyboard(SignInActivity.this);
					if(validation(mEmailEt.getText().toString(),mPswdET.getText().toString()))
					{
						mEmailID = mEmailEt.getText().toString();
						hitLoginService();
					}}
				return false;
			}
		});

		//set Listener

		mLoginBTN.setOnClickListener(this);
		mRegisterLinearLayout.setOnClickListener(this);
		mForgetPswdTV.setOnClickListener(this);
		mFBLoginTV.setOnClickListener(this);
	}

	private void initvariables() 
	{
		mAppTypeFace = new AppTypeFace(SignInActivity.this);
		mAppPreferences = AppPreferences.getInstance(SignInActivity.this);
		mSimpleFacebook =  SimpleFacebook.getInstance(SignInActivity.this);
	}

	@Override
	public void onClick(View view) 
	{
		switch (view.getId()) 
		{
		case R.id.btn_login:
			mUserTypeLogin = 0;
			CustomView.hideSoftKeyboard(SignInActivity.this);
			if(validation(mEmailEt.getText().toString(),mPswdET.getText().toString()))
			{
				mEmailID = mEmailEt.getText().toString();
				hitLoginService();
			}
			break;


		case R.id.tv_forget_pswd:
			CustomView.hideSoftKeyboard(SignInActivity.this);
			mIntent = new Intent(SignInActivity.this,ForgotPswdActivity.class);
			startActivityForResult(mIntent, GastonConstant.FORGET_PSWD_INTENT_KEY);
			break;

		case R.id.ll_register_here:
			CustomView.hideSoftKeyboard(SignInActivity.this);
			mIntent = new Intent(SignInActivity.this,SignUpActivity.class);
			mIntent.putExtra(GastonConstant.USER_TYPE_LOGIN_INTENT, 0);
			startActivityForResult(mIntent,GastonConstant.SIGN_UP_INTENT_KEY);
			finish();
			break;

		case R.id.btn_fb_login:
			CustomView.hideSoftKeyboard(SignInActivity.this);
			facebookLogin();//mine account
			break;

		default:
			break;
		}
	}



	private void facebookLogin() 
	{

		if(NetworkStatus.isInternetOn(SignInActivity.this))
		{
			if(mSimpleFacebook.isLogin())
			{
				mProgressBar = new ProgressDialog(SignInActivity.this,R.style.MyTheme);
				mProgressBar.setCancelable(false);
				mProgressBar.show();

				Properties.Builder pBuilder = new Properties.Builder();
				pBuilder.add(Properties.NAME);
				pBuilder.add(Properties.EMAIL);
				PictureAttributes pictureAttributes = PictureAttributes.createPictureAttributes();
				pictureAttributes.setHeight(100);
				pictureAttributes.setWidth(100);
				pictureAttributes.setType(PictureType.SQUARE);
				pBuilder.add(Properties.PICTURE, pictureAttributes);
				pBuilder.add(Properties.ID);

				mSimpleFacebook.getProfile(pBuilder.build(), onProfileListener);

			}
			else
			{

				mSimpleFacebook.login(onLoginListener);

			}
		}
		else
		{
			Toast.makeText(SignInActivity.this,getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();
		}

	}


	OnLoginListener onLoginListener = new OnLoginListener() 
	{

		@Override
		public void onFail(String reason) {

		}

		@Override
		public void onException(Throwable throwable) {

		}

		@Override
		public void onThinking() {

		}

		@Override
		public void onNotAcceptingPermissions(Type type) {

		}

		@Override
		public void onLogin() 
		{

			Properties.Builder pBuilder = new Properties.Builder();
			pBuilder.add(Properties.NAME);
			pBuilder.add(Properties.EMAIL);
			PictureAttributes pictureAttributes = PictureAttributes.createPictureAttributes();
			pictureAttributes.setHeight(100);
			pictureAttributes.setWidth(100);
			pictureAttributes.setType(PictureType.SQUARE);
			pBuilder.add(Properties.PICTURE, pictureAttributes);
			pBuilder.add(Properties.ID);
			pBuilder.add(Properties.HOMETOWN);
			pBuilder.add(Properties.LOCATION);

			mSimpleFacebook.getProfile(pBuilder.build(), onProfileListener);
		}
	};




	OnProfileListener onProfileListener = new OnProfileListener() 
	{
		@Override
		public void onFail(String reason) 
		{
		}

		@Override
		public void onException(Throwable throwable) {
		}

		@Override
		public void onThinking() 
		{

		}

		@Override
		public void onComplete(Profile profile) 
		{

			if(mProgressBar != null && mProgressBar.isShowing())
			{
				mProgressBar.dismiss();
			}

			mFbID = "" + profile.getId();
			String picture =  profile.getPicture();
			mAppPreferences.setFBImageURL(picture);

			mAppPreferences.setFBId(mFbID);
			/*
			 * Login type 1 = FB
			 */
			mUserTypeLogin = 1;
			Log.e("facebook_picture", "->"+picture);
			/*
			 * 
			 */
			try 
			{
				hitLoginService();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}


	};


	private boolean validation(String emailId, String pswd) 
	{
		if(emailId.equalsIgnoreCase("")||emailId.length()<0)
		{
			Toast.makeText(this,R.string.toast_email_id, Toast.LENGTH_SHORT).show();

			return false;
		}
		else if(!Patterns.EMAIL_ADDRESS.matcher(emailId).matches())
		{
			Toast.makeText(SignInActivity.this, R.string.toast_email_id_valid, Toast.LENGTH_SHORT).show();
			mPswdET.setText("");
			return false;
		}
		else if(pswd.equalsIgnoreCase("")||pswd.length()<0)
		{
			Toast.makeText(this,R.string.toast_pswd, Toast.LENGTH_SHORT).show();

			return false;

		}
		else if(pswd.length()<0)
		{
			if(pswd.length()<6)
			{
				Toast.makeText(this,R.string.toast_pswd_len, Toast.LENGTH_SHORT).show();
			}
			return false;

		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);


		if(requestCode==GastonConstant.SIGN_UP_INTENT_KEY)
		{
			Toast.makeText(this,"onACtivity Result of Sign Up Intent", Toast.LENGTH_SHORT).show();
		}
	}


	//get device token
	void getDeviceToken() 
	{
		try {
			callRecursiveForGCMRegistration();
		} 
		catch (UnsupportedOperationException e) {
			Toast.makeText(getApplicationContext(), "Gcm Not supported",
					Toast.LENGTH_SHORT).show();
		}
	}


	/**
	 * call the gcm registration to get the device token after 5 seonds
	 */
	private void callRecursiveForGCMRegistration() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() 
			{
				callForGCMRegistration();
				if (mAppPreferences.getDeviceToken() == null|| mAppPreferences.getDeviceToken().equalsIgnoreCase("")) {
					callRecursiveForGCMRegistration();
				}
				else
				{
					Log.e("device_token",mAppPreferences.getDeviceToken());
				}
			}
		}, 500);
	}

	/**
	 * register the device for getting thr device token
	 */
	private void callForGCMRegistration() 
	{
		GCMRegistrar.checkDevice(getApplicationContext());
		GCMRegistrar.checkManifest(getApplicationContext());
		final String regId = GCMRegistrar
				.getRegistrationId(getApplicationContext());
		if (regId.equals("")) 
		{
			GCMRegistrar.register(getApplicationContext(),
					GastonConstant.PROJECT_ID);
			mAppPreferences.setDeviceToken(regId);



		} else {
			mAppPreferences.setDeviceToken(regId);

		}
	}



	@SuppressLint("NewApi")
	private void hitLoginService() 
	{

		RequestBean requestBean = new RequestBean();
		if(NetworkStatus.isInternetOn(SignInActivity.this))
		{
			try
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(AppParserConstant.SERVICE_ACCESS_KEY,AppParserConstant.SERVICE_ACCESS_KEY_VAL);
				jsonObject.put(AppParserConstant.METHOD_NAME_KEY,AppParserConstant.METHOD_LOGIN);
				jsonObject.put(AppParserConstant.USER_EMAIL_KEY,mEmailID);
				jsonObject.put(AppParserConstant.USER_PSWD_KEY,mPswdET.getText().toString());
				jsonObject.put(AppParserConstant.FB_ID,mFbID);
				jsonObject.put(AppParserConstant.USER_TYPE_LOGIN_KEY,mUserTypeLogin);
				jsonObject.put(AppParserConstant.DEVICE_TOKEN_KEY,mAppPreferences.getDeviceToken());
				jsonObject.put(AppParserConstant.DEVICE_TYPE_KEY,AppParserConstant.DEVICE_TYPE_VAL);

				requestBean.setUrl(AppParserConstant.HIT_URL);
				requestBean.setLoader(true);
				requestBean.setJsonObject(jsonObject);
				requestBean.setActivity(SignInActivity.this);
				SignInAsynTask signInAsynTask = new SignInAsynTask(requestBean);

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
			Toast.makeText(SignInActivity.this,getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();
		}
	}

	public void setSignInResponse(SignUpBean result) 
	{


		if(mUserTypeLogin==0)
		{
			if(result.getStatus()==1)
			{
				mAppPreferences.setisLogin(true);
				mAppPreferences.setUserAccessToken(result.getUserAccessToken());
				mAppPreferences.setUserEmailId(mEmailID);
				mAppPreferences.setFirstName(result.getUserFirstName());
				mAppPreferences.setLastName(result.getUserLastName());
				mAppPreferences.setGender(result.getUserGender()+"");
				
				if(!result.getUser_name().equalsIgnoreCase(""))
				{
					String[] separated = result.getUser_name().split(",");
					mAppPreferences.setFirstName(separated[0]);
					mAppPreferences.setLastName(separated[1]);
				}
				

				Intent intent = new Intent(SignInActivity.this,DashBoardActivity.class);
				startActivity(intent);
				finish();
			}
			else
			{
				mAppPreferences.setisLogin(false);

				Toast.makeText(SignInActivity.this,result.getResponseString(), Toast.LENGTH_SHORT).show();

			}
		}
		else if (mUserTypeLogin==1)
		{
			if(result.getStatus()==0)
			{
				mAppPreferences.setisLogin(false);


				Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
				intent.putExtra(GastonConstant.USER_TYPE_LOGIN_INTENT, 1);
				startActivity(intent);
				finish();
			}
			else if(result.getStatus()==1)
			{
				mAppPreferences.setisLogin(true);
				mAppPreferences.setUserAccessToken(result.getUserAccessToken());

				Intent intent = new Intent(SignInActivity.this,DashBoardActivity.class);
				startActivity(intent);
				finish();
			}

		}
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mCurrentScore = savedInstanceState.getString("anshika");

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
		super.onSaveInstanceState(outState);
		{
			outState.putString("anshika", "anshika");
		}
	}


	@Override
	protected void onStop() 
	{
		super.onStop();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
	}


	@Override
	protected void onStart() {
		super.onStart();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


	public void setProfileResponse(ProfileDataBean result) {
	}
}
