package com.in.gaston;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.in.gaston.adapter.FetchProfileDataAdapter;
import com.in.gaston.apptypeface.AppTypeFace;
import com.in.gaston.asyntask.InterestDetailAsynTask;
import com.in.gaston.asyntask.ProfileAsycTask;
import com.in.gaston.bean.InterestBean;
import com.in.gaston.bean.InterestDeatailBean;
import com.in.gaston.bean.ProfileDataBean;
import com.in.gaston.bean.RequestBean;
import com.in.gaston.constant.AppParserConstant;
import com.in.gaston.constant.AppPreferences;
import com.in.gaston.constant.GastonConstant;
import com.in.gaston.imageloader.ImageLoader;
import com.in.gaston.network.NetworkStatus;
import com.in.gaston.view.CustomView;
import com.in.gaston.view.RoundedImageView;

public class ShowOtherUserProfileActivity extends Activity implements OnClickListener,OnRefreshListener
{
	private AppPreferences mAppPreferences;
	private AppTypeFace mAppTypeFace;
	private ImageView mBackIMG;
	private TextView mProfileHeadingTV;
	private ArrayList<InterestBean> mArrayList;
	private RoundedImageView mUserIMGVIEW;
	private TextView mUserNameTV;
	private TextView mCreateInterestTV;
	private ListView mListView;
	private String userIdSTR;
	private int mPerPage = 10,mNextPageId = 0;
	private FetchProfileDataAdapter mFetchProfileDataAdapter;
	private int total_record = 0;
	private boolean isHit = true;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private TextView mNoData;
	private String userNameSTR;
	private String mGenderTV="";

	private String mProtectedPswdVal = "";
	private String userGenderSTR;
	private int mPosition;
	private boolean isPswd;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_user_profile_view);
		setVariables();
		setAdapter();
		setView();
		mUserNameTV.setText("");
		hitService();
	}
	private void setAdapter() 
	{
		mArrayList = new ArrayList<InterestBean>();
		mFetchProfileDataAdapter = new FetchProfileDataAdapter(null,ShowOtherUserProfileActivity.this, mArrayList, total_record);
	}
	private void setVariables() 
	{
		userIdSTR =  getIntent().getStringExtra("user_id");
		userNameSTR =  getIntent().getStringExtra("user_name");
		userGenderSTR =  getIntent().getStringExtra("user_gender");

		mAppTypeFace = new AppTypeFace(ShowOtherUserProfileActivity.this);
		mAppPreferences = AppPreferences.getInstance(ShowOtherUserProfileActivity.this);
	}
	@SuppressWarnings("deprecation")
	private void setView() 
	{
		mNoData = (TextView)findViewById(R.id.tv_no_data);
		mBackIMG =  (ImageView)findViewById(R.id.img_back);
		mProfileHeadingTV =  (TextView)findViewById(R.id.tv_heading);
		mUserIMGVIEW =  (RoundedImageView)findViewById(R.id.img_intrst_image);
		mUserNameTV =  (TextView)findViewById(R.id.user_name);
		mCreateInterestTV =  (TextView)findViewById(R.id.tv_create_interest);
		mListView = 	(ListView)findViewById(R.id.ll_interest);
		mSwipeRefreshLayout =  (SwipeRefreshLayout)findViewById(R.id.swipe_container);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright, 
				android.R.color.holo_green_light, 
				android.R.color.holo_orange_light, 
				android.R.color.holo_red_light);
		mProfileHeadingTV.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());
		mUserNameTV.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
		mCreateInterestTV.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
		mListView.setAdapter(mFetchProfileDataAdapter);
		//set Listener
		mBackIMG.setOnClickListener(this);
	}


	@Override
	public void onClick(View view) 
	{
		switch (view.getId()) 
		{
		case R.id.img_back:
			finish();

			break;

		default:
			break;
		}
	}


	/*
	 * service hit
	 */

	public void hitService() 
	{
		if(isHit)
		{
			isHit = false;
			hitUserProfileService();
		}
	}

	private void hitUserProfileService() 
	{
		/*
		 * service_access_key, 
		 * method, 
		 * user_access_token, 
		 * user_id , 
		 * profile_status, 
		 * type, 
		 * other_user_id
		 */
		RequestBean requestBean = new RequestBean();
		if(NetworkStatus.isInternetOn(ShowOtherUserProfileActivity.this))
		{
			try
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(AppParserConstant.SERVICE_ACCESS_KEY,AppParserConstant.SERVICE_ACCESS_KEY_VAL);
				jsonObject.put(AppParserConstant.METHOD_NAME_KEY,AppParserConstant.METHOD_PROFILE);
				jsonObject.put(AppParserConstant.USER_ACCESS_TOKEN_KEY,mAppPreferences.getUserAccessToken());
				jsonObject.put(AppParserConstant.USER_ID_KEY,mAppPreferences.getUserId());
				jsonObject.put(AppParserConstant.PROFILE_STATUS_KEY,mAppPreferences.getUserProfileStatus());
				jsonObject.put(AppParserConstant.TYPE,"");
				jsonObject.put(AppParserConstant.OTHER_USER_ID,userIdSTR);
				jsonObject.put(AppParserConstant.PAGE_ID,mNextPageId);
				jsonObject.put(AppParserConstant.PER_PAGE,mPerPage);

				if(mNextPageId==0)
				{
					requestBean.setLoader(true);
				}
				else
				{
					requestBean.setLoader(false);

				}
				requestBean.setUrl(AppParserConstant.HIT_URL_USER);
				requestBean.setLoader(true);
				requestBean.setJsonObject(jsonObject);
				requestBean.setActivity(ShowOtherUserProfileActivity.this);
				ProfileAsycTask signInAsynTask = new ProfileAsycTask(requestBean);

				if(VERSION.SDK_INT < VERSION_CODES.HONEYCOMB)
				{
					signInAsynTask.execute();
				}else{
					signInAsynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");	
				}

			}
			catch (Exception e) 
			{
				isHit = true;;

				e.printStackTrace();
			}
		}
		else
		{
			isHit = true;;
			Toast.makeText(ShowOtherUserProfileActivity.this,getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();
		}

	}
	public void setProfileResponse(ProfileDataBean result) 
	{
		isHit = true;
		//Toast.makeText(ShowOtherUserProfileActivity.this,result.getResponseString(), Toast.LENGTH_SHORT).show();
		if(userGenderSTR.equalsIgnoreCase("1"))
		{
			mGenderTV = "M";
		}
		else
		{
			mGenderTV = "F";
		}
		if(mAppPreferences.getUserProfileStatus().equalsIgnoreCase("1"))
		{
			if(!result.getUser_image().equalsIgnoreCase(""))
			{
				ImageLoader.getInstance(ShowOtherUserProfileActivity.this).displayImage(AppParserConstant.BASE_URL+result.getUser_image(), mUserIMGVIEW, false);
			}
			else
			{
				mUserIMGVIEW.setImageResource(R.drawable.profileiconbig);
			}
			
			Resources res = getResources();
			String text = String.format(res.getString(R.string.text_user_name_profile),userNameSTR, mGenderTV);
			mUserNameTV.setText(text);
			mProfileHeadingTV.setText("Real Profile");
		}
		else
		{
			mUserNameTV.setVisibility(View.VISIBLE);
			mUserIMGVIEW.setImageResource(R.drawable.profileiconbig);
			Resources res = getResources();
			String text = String.format(res.getString(R.string.text_user_name_profile),result.getPrivate_name(), mGenderTV);
			mUserNameTV.setText(text);
			mProfileHeadingTV.setText("Anonymous Profile");
		}
	
		//
		if(mSwipeRefreshLayout.isRefreshing())
		{
			mSwipeRefreshLayout.setRefreshing(false);
		}
		if(result.getStatus()==1)
		{

			if(mNextPageId==0)
			{
				mArrayList.clear();
			}
			mNoData.setVisibility(View.GONE);
			mNextPageId++;
			mFetchProfileDataAdapter.setTotalRecord(result.getTotal_record(),0);
			mArrayList.addAll(result.getmArrayList());
			mFetchProfileDataAdapter.notifyDataSetChanged();
		}
		else if(result.getStatus()==0)
		{

			if(result.getTotal_record()==0)
			{
				mArrayList.clear();
				mFetchProfileDataAdapter.notifyDataSetChanged();
				mNoData.setVisibility(View.VISIBLE);
			}
			else
			{


			}
		}
	}
	@Override
	public void onRefresh() 
	{

		mNextPageId = 0;
		hitService();

	}





/*
 * 
 */
	
	public void openPasswordDialog(final int position)
	{
		mPosition = position;
		Button gallery,camera;
		final Dialog dialog = new Dialog(ShowOtherUserProfileActivity.this,R.style.transparent_background_dialog);
		dialog.setContentView(R.layout.dialog_password);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialog.getWindow().setAttributes(lp);
		gallery	= (Button) dialog.findViewById(R.id.btn_gallery);
		camera	= (Button) dialog.findViewById(R.id.btn_camera);
		dialog.show();
		final EditText Pswd1ET = (EditText) dialog.findViewById(R.id.et_password_protected_1);
		final EditText Pswd2ET =  (EditText) dialog.findViewById(R.id.et_password_protected_2);
		final EditText Pswd3ET =  (EditText) dialog.findViewById(R.id.et_password_protected_3);
		final EditText Pswd4ET =  (EditText) dialog.findViewById(R.id.et_password_protected_4);
		Button okBTN =  (Button) dialog.findViewById(R.id.btn_ok);
		Pswd1ET.addTextChangedListener(new TextWatcher() 
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) 
			{
				Pswd2ET.requestFocus();
			}
		});
		Pswd2ET.addTextChangedListener(new TextWatcher() 
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) 
			{
				Pswd3ET.requestFocus();
			}
		});

		Pswd3ET.addTextChangedListener(new TextWatcher() 
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) 
			{
				Pswd4ET.requestFocus();
			}
		});

		okBTN.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{

				if(Pswd1ET.getText().toString().equalsIgnoreCase("")||Pswd2ET.getText().toString().equalsIgnoreCase("")||Pswd3ET.getText().toString().equalsIgnoreCase("")||Pswd4ET.getText().toString().equalsIgnoreCase(""))
				{
					Toast.makeText(ShowOtherUserProfileActivity.this,"Password entered should be of 4 digit",Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				}
				else
				{
					dialog.dismiss();
					mProtectedPswdVal = Pswd1ET.getText().toString()+Pswd2ET.getText().toString()+Pswd3ET.getText().toString()+Pswd4ET.getText().toString();
					chek_for_valid_pswd(position);
				}
			}
		});

	}
	
	
	
	
	
	private void chek_for_valid_pswd(int position) 
	{

		mPosition = position;
		isPswd = true;
		/*
		 * service_access_key
		 *  method
		 *  user_access_token
		 *  interest_id 
		 *  profile_status 
		 *  password  
		 *  per_page 
		 *  page_id
		 */


		/*
		 * comment type =1 means text
		 * 2=image
		 * 3=audio
		 */
		RequestBean requestBean = new RequestBean();
		if(NetworkStatus.isInternetOn(ShowOtherUserProfileActivity.this))
		{
			try
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(AppParserConstant.SERVICE_ACCESS_KEY,AppParserConstant.SERVICE_ACCESS_KEY_VAL);
				jsonObject.put(AppParserConstant.METHOD_NAME_KEY,AppParserConstant.METHOD_INTRST_DETAIL);
				jsonObject.put(AppParserConstant.USER_ACCESS_TOKEN_KEY,mAppPreferences.getUserAccessToken());
				jsonObject.put(AppParserConstant.INTRST_ID_KEY,mArrayList.get(position).getInterest_id());
				jsonObject.put(AppParserConstant.PROFILE_STATUS_KEY,mAppPreferences.getUserProfileStatus());
				jsonObject.put(AppParserConstant.PSWD_KEY,mProtectedPswdVal);
				jsonObject.put(AppParserConstant.PAGE_ID,0);
				jsonObject.put(AppParserConstant.PER_PAGE,mPerPage);
				jsonObject.put(AppParserConstant.USER_ID_KEY,mAppPreferences.getUserId());
				requestBean.setLoader(true);
				requestBean.setUrl(AppParserConstant.HIT_URL_INTRST);
				requestBean.setJsonObject(jsonObject);
				requestBean.setActivity(ShowOtherUserProfileActivity.this);
				InterestDetailAsynTask interestDetailAsynTask = new InterestDetailAsynTask(requestBean);

				if(VERSION.SDK_INT < VERSION_CODES.HONEYCOMB)
				{
					interestDetailAsynTask.execute();
				}else{
					interestDetailAsynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");	
				}

			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			isHit=false;
			Toast.makeText(ShowOtherUserProfileActivity.this,getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();
		}

	}
	
	
	public void sendIntent(int position)
	{
		int mPosition = position;
		Intent intent = new Intent(ShowOtherUserProfileActivity.this,InterestDetailActivity.class);
		intent.putExtra(GastonConstant.INTRST_ID_KEY,mArrayList.get(position).getInterest_id());
		intent.putExtra(GastonConstant.PROFILE_STATUS_KEY,mAppPreferences.getUserProfileStatus());
		intent.putExtra(GastonConstant.PSWD_PROTECTED_KEY,mProtectedPswdVal);
		intent.putExtra(GastonConstant.USER_ID_KEY,mArrayList.get(position).getUser_id());
		intent.putExtra(GastonConstant.SUBSCRIBE_STATUS_KEY,1);


		intent.putExtra("POSITION",position);
		startActivityForResult(intent,1);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode==1)
		{
			if(resultCode==-1)
			{
				mNextPageId = 0;
					hitService();
			}
		}
	}
	public void setChkPswdResponse(InterestDeatailBean result) 
	{
				CustomView.hideSoftKeyboard(ShowOtherUserProfileActivity.this);

		if(result.getStatus()==1)
		{
			sendIntent(mPosition);
		}
		else
		{
			Toast.makeText(ShowOtherUserProfileActivity.this,result.getResponseString(),Toast.LENGTH_SHORT).show();
		}
	}

}
