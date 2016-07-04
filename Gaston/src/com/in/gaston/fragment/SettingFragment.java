package com.in.gaston.fragment;

import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.in.gaston.ChangePasswordActivity;
import com.in.gaston.DashBoardActivity;
import com.in.gaston.R;
import com.in.gaston.SignInActivity;
import com.in.gaston.apptypeface.AppTypeFace;
import com.in.gaston.asyntask.ForgetPswdAsynTask;
import com.in.gaston.bean.CommonBean;
import com.in.gaston.bean.RequestBean;
import com.in.gaston.constant.AppParserConstant;
import com.in.gaston.constant.AppPreferences;
import com.in.gaston.network.NetworkStatus;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLogoutListener;

public class SettingFragment extends Fragment implements OnClickListener
{
	private AppPreferences mAppPreferences;
	private AppTypeFace mAppTypeFace;
	private Switch mNotificationSWITCH;
	private ImageView mChangePswdIMG;
	private ImageView mAboutUsIMG;
	private ImageView mPrivacyIMG;
	private ImageView mTnMIMG;
	private TextView mNotificationTV;
	private TextView mChangePswdTV;
	private TextView mAboutUsTV;
	private TextView mTnMTV;
	private TextView mPrivacyTV;
	private ImageView backImGVIEW;
	boolean isHit=true;
	private Button logoutBTN;
	private SimpleFacebook mSimpleFacebook;;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{

		View view = inflater.inflate(R.layout.fragment_setting, container, false);
		((DashBoardActivity)getActivity()).back_press_val("setting");

		initVariables();
		initView(view);
		return view;
	}

	private void initVariables() 
	{
		mSimpleFacebook =  SimpleFacebook.getInstance(getActivity());

		mAppTypeFace = new AppTypeFace(getActivity());		
		mAppPreferences = AppPreferences.getInstance(getActivity());
	}

	private void initView(View view) 
	{

		backImGVIEW =  (ImageView)view.findViewById(R.id.img_back);
		logoutBTN =  (Button)view.findViewById(R.id.btn_logout);
		mNotificationSWITCH = 	(Switch) view.findViewById(R.id.switch_notification);
		mChangePswdIMG =  (ImageView)view.findViewById(R.id.img_change_pawd);
		mAboutUsIMG =  (ImageView)view.findViewById(R.id.img_about_us);
		mTnMIMG =  (ImageView)view.findViewById(R.id.img_tnm);
		mPrivacyIMG =  (ImageView)view.findViewById(R.id.img_privacy_policy);

		//

		mNotificationTV = 	(TextView) view.findViewById(R.id.tv_notification);
		mChangePswdTV =  (TextView)view.findViewById(R.id.tv_change_pswd);
		mAboutUsTV =  (TextView)view.findViewById(R.id.tv_about_us);
		mTnMTV =  (TextView)view.findViewById(R.id.tv_tnm);
		mPrivacyTV =  (TextView)view.findViewById(R.id.tv_privacy_policy);
		//

		mNotificationSWITCH.setOnClickListener(this);
		mChangePswdIMG.setOnClickListener(this);
		mAboutUsIMG.setOnClickListener(this);
		mTnMIMG.setOnClickListener(this);
		mPrivacyIMG.setOnClickListener(this);
		backImGVIEW.setOnClickListener(this);
		logoutBTN.setOnClickListener(this);

		mNotificationTV.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
		mChangePswdTV.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
		mAboutUsTV.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
		mTnMTV.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
		mPrivacyTV.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
		logoutBTN.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
		/*
		 * switch
		 */


		mNotificationSWITCH.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				if(isChecked)
				{
//1 for on
					hit_service(1);				
					}
				else
				{
					//0 for off
					hit_service(0);				

				}

			}
		});
		
		
		backImGVIEW.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				((DashBoardActivity)getActivity()).initialHomeFragment(false);
			}
		});

	}
	private void hit_service(int val) 
	{
		if(isHit)
		{
			isHit = false;
			hitNotificationOnOffApi(val);
		}
	}

	private void hitNotificationOnOffApi(int val) 
	{
/*
 * service_access_key, method, user_access_token, user_id , notification_status
 */

		RequestBean requestBean = new RequestBean();
		if(NetworkStatus.isInternetOn(getActivity()))
		{
			try
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(AppParserConstant.SERVICE_ACCESS_KEY,AppParserConstant.SERVICE_ACCESS_KEY_VAL);
				jsonObject.put(AppParserConstant.METHOD_NAME_KEY,"notificationonandoff");
				jsonObject.put(AppParserConstant.USER_ACCESS_TOKEN_KEY,mAppPreferences.getUserAccessToken());
				jsonObject.put(AppParserConstant.USER_ID_KEY,mAppPreferences.getUserId());
				jsonObject.put("notification_status",val);


				requestBean.setUrl(AppParserConstant.HIT_URL);
				requestBean.setLoader(true);
				requestBean.setJsonObject(jsonObject);
				requestBean.setCallableObect(SettingFragment.this);
				requestBean.setActivity(getActivity());
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
				isHit  =true;
				e.printStackTrace();
			}
		}
		else
		{
			isHit  =true;

			Toast.makeText(getActivity(),getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();
		}
	
	}

	@Override
	public void onClick(View view) 
	{
		switch (view.getId()) 
		{
		case R.id.img_change_pawd:

			Intent intent = new Intent(getActivity(),ChangePasswordActivity.class);
			startActivity(intent);

			break;
		case R.id.img_about_us:
			openWeb();
			break;
		case R.id.img_tnm:
			openWeb();

			break;
		case R.id.img_privacy_policy:
			openWeb();
			break;
			
		case R.id.btn_logout:
			mSimpleFacebook.logout(new OnLogoutListener() 
			{
				
				@Override
				public void onFail(String reason) 
				{
					//Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();

				}
				
				@Override
				public void onException(Throwable throwable) 
				{
				//	Toast.makeText(getActivity(), "Exception", Toast.LENGTH_SHORT).show();

				}
				
				@Override
				public void onThinking() {
					//Toast.makeText(getActivity(), "Thinking", Toast.LENGTH_SHORT).show();

				}
				
				@Override
				public void onLogout() 
				{
					//Toast.makeText(getActivity(), "LogOut", Toast.LENGTH_SHORT).show();
				}
			});
			
			Intent logout_intent = new Intent(getActivity(),SignInActivity.class);
			getActivity().startActivity(logout_intent);
			
			break;
		default:
			break;
		}
	}

	private void openWeb()
	{
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.in/?gfe_rd=cr&ei=rx7LVdrdAurA8gfS-L_IDA&gws_rd=ssl"));
		startActivity(i);	
	}

	public void setNotificationOnOffResponse(CommonBean result) 
	{
		isHit  =true;

	//	Toast.makeText(getActivity(), result.getResponseString()+"",Toast.LENGTH_SHORT).show();
	}

}
