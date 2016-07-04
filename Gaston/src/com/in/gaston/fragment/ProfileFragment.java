package com.in.gaston.fragment;

import java.util.ArrayList;

import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.in.gaston.DashBoardActivity;
import com.in.gaston.EditProfileActivity;
import com.in.gaston.FlipAnimation;
import com.in.gaston.InterestDetailActivity;
import com.in.gaston.R;
import com.in.gaston.adapter.FetchProfileDataAdapter;
import com.in.gaston.apptypeface.AppTypeFace;
import com.in.gaston.asyntask.ForgetPswdAsynTask;
import com.in.gaston.asyntask.InterestDetailAsynTask;
import com.in.gaston.asyntask.ProfileAsycTask;
import com.in.gaston.bean.CommonBean;
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
import com.kbeanie.imagechooser.api.ImageChooserManager;

public class ProfileFragment extends Fragment implements OnClickListener,OnRefreshListener,AnimationListener
{

	private boolean showingBack;
	private Context context;
	private Handler handler;
	private FlipAnimation flipAnimation;
	private AppPreferences mAppPreferences;
	private AppTypeFace mAppTypeFace;
	private TextView mProfileHeadingTV,mSubscribeTV,mMyCreatedTV,mUserNameTV;
	private RoundedImageView mUserProfileIMG;
	private ImageView mEditProfileIMG,mFlipIMG;
	private View mSubscribeSelectorV,mMyCreatedSelectorV;
	private boolean isHit = true;
	private int mProfileStatus=1;
	private boolean isReal;
	private int mType = 1;
	private FetchProfileDataAdapter mFetchProfileDataAdapter;
	private ListView mListView;
	private int mPerPage = 10,mNextPageId = 0;
	private ArrayList<InterestBean> mArrayList;
	private RelativeLayout mRelativeLayout;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private String mGenderTV="";

	private ProgressBar mProgressBar;
	private View mNoData;
	private String mProtectedPswdVal;
	private boolean isSub = true,isCreated = true;
	private RelativeLayout mMainRL;
	private FlipAnimation backFlip;
	private Animation animZoomOut;
	private String mProfileIMG="";

	private ContentBody mUserImage;
	private Uri selectedImage;
	private ImageChooserManager imageChooserManager;
	private String mPicturePath="";
	private int chooserType;
	private StringBody facebookIdStringBody;
	private int mSelectedRB=1,mImageType = 0,userTypeLogin = 0;
	private RadioButton mMaleRB,mFemaleRB;
	private EditText mUserFirstNameET,mUserLastNameET,mUserEmailIdET,mPswdET;
	private RoundedImageView mUserIMG;
	private TextView mAddDescTV;
	private String mUserProfileSTR="";
	private String mUserName="";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_profile, container, false);
		((DashBoardActivity)getActivity()).back_press_val("profile");

		initVariables();
		initView(view);
		if(mAppPreferences.getUserProfileStatus().equalsIgnoreCase("1"))
		{
			mAppPreferences.setIsReal(true);
			isReal = false;
		}
		else
		{
			mAppPreferences.setIsReal(false);
			isReal = true;


		}
		mUserNameTV.setText("");
		hitService();
		setSelector(1);
		/*
		 * first time real will open
		 */
		return view;
	}

	private void initVariables() 
	{
		mArrayList = new ArrayList<InterestBean>();
		mAppPreferences = AppPreferences.getInstance(getActivity());
		mAppTypeFace = new AppTypeFace(getActivity());
		mFetchProfileDataAdapter = new FetchProfileDataAdapter(ProfileFragment.this, getActivity(), mArrayList, 0);
	}
	private void initView(View view) 
	{

		mRelativeLayout = 		(RelativeLayout) view.findViewById(R.id.rl_main_view);


		mAddDescTV = 	(TextView)view.findViewById(R.id.tv_user_desc);
		mProfileHeadingTV =  (TextView) view.findViewById(R.id.tv_heading);
		mUserProfileIMG =	(RoundedImageView) view.findViewById(R.id.img_intrst_image);
		mEditProfileIMG =  (ImageView) view.findViewById(R.id.img_edit_profile);
		mUserNameTV =  (TextView) view.findViewById(R.id.user_name);
		mFlipIMG =  (ImageView) view.findViewById(R.id.img_flip);
		mListView = 	(ListView)view.findViewById(R.id.ll_interest);
		mSwipeRefreshLayout =  (SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
		mProgressBar =  (ProgressBar)view.findViewById(R.id.progressbar);
		mSubscribeTV = 	(TextView) view.findViewById(R.id.tv_subscribe);
		mSubscribeSelectorV = 	view.findViewById(R.id.view_suscribe_selector);
		mMyCreatedTV	=  (TextView) view.findViewById(R.id.tv_my_created);
		mMyCreatedSelectorV = 	view.findViewById(R.id.view_my_created_selector);
		mMainRL =  (RelativeLayout)view.findViewById(R.id.rl_main_view);

		mNoData = view.findViewById(R.id.tv_no_data);
		mProfileHeadingTV.setText("Real Profile");

		mProfileHeadingTV.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());
		mUserNameTV.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());
		mSubscribeTV.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());
		mMyCreatedTV.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());


		mListView.setAdapter(mFetchProfileDataAdapter);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright, 
				android.R.color.holo_green_light, 
				android.R.color.holo_orange_light, 
				android.R.color.holo_red_light);
		mSubscribeTV.setOnClickListener(this);
		mMyCreatedTV.setOnClickListener(this);
		mEditProfileIMG.setOnClickListener(this);
		mUserProfileIMG.setOnClickListener(this);
		mFlipIMG.setOnClickListener(this);
	}

	public void hitService() 
	{
		if(isHit)
		{
			isHit = false;
			hitUserProfileService(mType);
		}
	}
	@Override
	public void onClick(View view) 
	{
		switch (view.getId()) 
		{
		case R.id.tv_subscribe:
			mType = 1;
			mNextPageId=0;
			setSelector(1);
			hitService();

			break;
		case R.id.tv_my_created:
			mNextPageId=0;
			mType = 2;
			setSelector(2);
			hitService();


			break;
		case R.id.img_flip:
			/*
			 * flip animation
			 */
			handler = new Handler();
			flipAnimation = new FlipAnimation(mRelativeLayout, mRelativeLayout);
			backFlip = new FlipAnimation(mRelativeLayout, mRelativeLayout);
			handler.removeCallbacks(rotate);
			handler.postDelayed(rotate, 100);
			getActivity().overridePendingTransition(R.anim.from_middle, R.anim.from_middle);
			setSelector(1);

			/*Animation animRotate = AnimationUtils.loadAnimation(getActivity(),
					R.anim.rotate_animation);  

			animRotate.setAnimationListener(this);
			mMainRL.startAnimation(animRotate);*/


			if(isReal)
			{
				mUserNameTV.setText("");
				mEditProfileIMG.setVisibility(View.VISIBLE);
				mAppPreferences.setUserProfileStatus("1");
				//mProfileStatus = 1;
				isReal = false;
				mType = 1;
				isSub = true;
				isCreated = true;
				mNextPageId=0;
				hitService();
			}
			else
			{
				mUserNameTV.setText("");
				mUserProfileIMG.setImageResource(R.drawable.profileiconbig);
				mAppPreferences.setUserProfileStatus("2");
				mNextPageId = 0;
				//mProfileStatus = 2;
				isReal = true;
				isSub = true;
				isCreated = true;
				mType = 1;
				hitService();
			}

			break;
		case R.id.img_edit_profile:
			Intent intent = new Intent(getActivity(),EditProfileActivity.class);
			intent.putExtra("user_profile_image", mUserProfileSTR);
			intent.putExtra("user_first_name", mUserFirstName);
			intent.putExtra("user_last_name", mUserLastName);
			intent.putExtra("user_gender", mUserGender);
			intent.putExtra("user_desc", mUserDesc);
			startActivityForResult(intent,20);


			break;
		case R.id.img_intrst_image:
			CustomView.zoom_in_user_pic(getActivity(),mProfileIMG);
			break;

		default:
			break;
		}
	}
	private Runnable rotate = new Runnable() 
	{

		@Override
		public void run() {
			if (!showingBack) {
				mRelativeLayout.startAnimation(flipAnimation);
				mRelativeLayout.startAnimation(flipAnimation);
				//Toast.makeText(getActivity(), "flip", Toast.LENGTH_LONG).show();
				showingBack = true;
			} else {
				showingBack = false;
				backFlip.reverse();
				//	Toast.makeText(getActivity(), "backflip", Toast.LENGTH_LONG).show();
				mRelativeLayout.startAnimation(backFlip);
				mRelativeLayout.startAnimation(backFlip);

			}
		}
	};
	private int mPosition;
	private boolean isPswd;
	private String mUserGender="";
	private String mUserDesc;
	private String mUserFirstName;
	private String mUserLastName;


	private void hitUserProfileService(int type) 
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
		if(NetworkStatus.isInternetOn(getActivity()))
		{
			try
			{
				//mProfileStatus
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(AppParserConstant.SERVICE_ACCESS_KEY,AppParserConstant.SERVICE_ACCESS_KEY_VAL);
				jsonObject.put(AppParserConstant.METHOD_NAME_KEY,AppParserConstant.METHOD_PROFILE);
				jsonObject.put(AppParserConstant.USER_ACCESS_TOKEN_KEY,mAppPreferences.getUserAccessToken());
				jsonObject.put(AppParserConstant.USER_ID_KEY,mAppPreferences.getUserId());
				jsonObject.put(AppParserConstant.PROFILE_STATUS_KEY,mAppPreferences.getUserProfileStatus());
				jsonObject.put(AppParserConstant.TYPE,type);
				jsonObject.put(AppParserConstant.OTHER_USER_ID,"");
				jsonObject.put(AppParserConstant.PAGE_ID,mNextPageId);
				jsonObject.put(AppParserConstant.PER_PAGE,mPerPage);

				if(mNextPageId ==0)
				{
					requestBean.setLoader(true);

					mProgressBar.setVisibility(View.GONE);
				}
				else
				{
					mProgressBar.setVisibility(View.VISIBLE);
				}
				requestBean.setUrl(AppParserConstant.HIT_URL_USER);
				requestBean.setLoader(true);
				requestBean.setJsonObject(jsonObject);
				requestBean.setActivity(getActivity());
				requestBean.setCallableObect(ProfileFragment.this);
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
			Toast.makeText(getActivity(),getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();
		}
	}

	public void setProfileResponse(ProfileDataBean result) 
	{
		mProgressBar.setVisibility(View.GONE);
		mProfileIMG  = "";
		isHit = true;
		//	Toast.makeText(getActivity(),result.getResponseString(), Toast.LENGTH_SHORT).show();
		//set data of profile
		if(mSwipeRefreshLayout.isRefreshing())
		{
			mSwipeRefreshLayout.setRefreshing(false);
		}
		if(result.getStatus()==1)
		{
			set_profile_data(result);
			//
			mNoData.setVisibility(View.GONE);
			mNextPageId++;

			if(mType==1)
			{
				isCreated = true;
				//sub
				if(isSub)
				{
					isSub = false;
					mArrayList.clear();
				}
			}
			else if (mType==2) 
			{
				isSub = true;
				//my created
				if(isCreated)
				{
					isCreated = false;
					mArrayList.clear();
				}
			}
			mFetchProfileDataAdapter.setTotalRecord(result.getTotal_record(),mType);
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

				set_profile_data(result);
			}
			else
			{


			}
		}
	}
	private void set_profile_data(ProfileDataBean result) 
	{

		if(result!=null)
		{
			if(mAppPreferences.getUserProfileStatus().equalsIgnoreCase("1"))
			{
				mAppPreferences.setIsReal(true);

				if(result.getUser_gender().equalsIgnoreCase("1"))
				{
					mGenderTV = "M";
				}
				else
				{
					mGenderTV = "F";
				}


				if(!result.getUser_description().equals("null")||result.getUser_description().equals(""))
				{
					mAddDescTV.setText(result.getUser_description());
				}

				//sent data on edit profile
				mUserProfileSTR = result.getUser_image();
				mUserFirstName =  result.getUser_fname();
				mUserLastName =  result.getUser_lname();
				mUserGender = result.getUser_gender();
				mUserDesc =  result.getUser_description();

				//				
				mProfileIMG = AppParserConstant.BASE_URL+result.getUser_image();
				if(result.getUser_image().equalsIgnoreCase(""))
				{
					mProfileIMG = "";	
					mUserProfileIMG.setImageResource(R.drawable.profileiconbig);
				}
				else
				{
					ImageLoader.getInstance(getActivity()).displayImage(mProfileIMG, mUserProfileIMG, false);
				}

				Resources res = getResources();

				if(result.getUser_fname().equalsIgnoreCase(""))
				{

					if(result.getUser_gender().equalsIgnoreCase(""))
					{
						if(mAppPreferences.getGender().equalsIgnoreCase("1"))
						{
							mGenderTV = "M";
						}
						else
						{
							mGenderTV = "F";
						}
					}
					mUserFirstName =  mAppPreferences.getFirstName();
					mUserLastName=  mAppPreferences.getLastName();

					String text = String.format(res.getString(R.string.text_user_name_profile), mAppPreferences.getFirstName(), mGenderTV);
					mUserNameTV.setText(text);
				}
				else
				{
					String text = String.format(res.getString(R.string.text_user_name_profile), result.getUser_fname(), mGenderTV);
					mUserNameTV.setText(text);
				}
				mProfileHeadingTV.setText("Real Profile");
			}


			else
			{

				mAppPreferences.setIsReal(false);

				if(result.getPrivate_gender().equalsIgnoreCase("1"))
				{
					mGenderTV = "M";
				}
				else
				{
					mGenderTV = "F";
				}
				mProfileIMG = "";
				mUserNameTV.setVisibility(View.VISIBLE);
				//mUserProfileIMG.setImageResource(R.drawable.profileiconbig);
				//sent data on edit profile
				mUserProfileSTR = result.getUser_private_image();
				mUserGender = result.getPrivate_gender();
				mUserDesc =  result.getPrivate_desc();

				if(!result.getPrivate_desc().equals("null")||result.getPrivate_desc().equals(""))
				{
					mAddDescTV.setText(result.getPrivate_desc());
				}



				if(!result.getPrivate_name().equalsIgnoreCase("")&&result.getPrivate_name().contains(","))
				{
					String[] separated = result.getPrivate_name().split(",");
					mUserFirstName =  separated[0]; 
					mUserLastName = separated[1]; 
				}
				else
				{
					mUserFirstName =  "";
					mUserLastName =  "";
				}
				mProfileIMG = AppParserConstant.BASE_URL+result.getUser_private_image();
				if(result.getUser_private_image().equalsIgnoreCase(""))
				{
					mProfileIMG = "";	
					mUserProfileIMG.setImageResource(R.drawable.profileiconbig);
				}
				else
				{
					ImageLoader.getInstance(getActivity()).displayImage(mProfileIMG, mUserProfileIMG, false);
				}

				Resources res = getResources();

				if(result.getPrivate_name().equalsIgnoreCase(""))
				{

					//String text = String.format(res.getString(R.string.text_user_name_profile),mAppPreferences.getFirstName(), mGenderTV);
					mUserNameTV.setText("");
				}
				else
				{
					String text = String.format(res.getString(R.string.text_user_name_profile),mUserFirstName, mGenderTV);
					mUserNameTV.setText(text);
				}

				mProfileHeadingTV.setText("Anonymous Profile");
			}
		}
	}

	public void openPasswordDialog(final int position)
	{

		mPosition = position;
		Button gallery,camera;
		final Dialog dialog = new Dialog(getActivity(),R.style.transparent_background_dialog);
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
				mProtectedPswdVal = "";
				if(Pswd1ET.getText().toString().equalsIgnoreCase("")||Pswd2ET.getText().toString().equalsIgnoreCase("")||Pswd3ET.getText().toString().equalsIgnoreCase("")||Pswd4ET.getText().toString().equalsIgnoreCase(""))
				{
					Toast.makeText(getActivity(),"Password entered should be of 4 digit",Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				}
				else
				{
					dialog.dismiss();
					CustomView.hideSoftKeyboard(getActivity());
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
		if(NetworkStatus.isInternetOn(getActivity()))
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
				requestBean.setActivity(getActivity());
				requestBean.setCallableObect(ProfileFragment.this);
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
			Toast.makeText(getActivity(),getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();
		}

	}



	public void sendIntent(int position)
	{
		int mPosition = position;
		Intent intent = new Intent(getActivity(),InterestDetailActivity.class);
		intent.putExtra(GastonConstant.INTRST_ID_KEY,mArrayList.get(position).getInterest_id());
		intent.putExtra(GastonConstant.PROFILE_STATUS_KEY,mAppPreferences.getUserProfileStatus());
		intent.putExtra(GastonConstant.PSWD_PROTECTED_KEY,mProtectedPswdVal);
		intent.putExtra(GastonConstant.USER_ID_KEY,mArrayList.get(position).getUser_id());
		intent.putExtra(GastonConstant.SUBSCRIBE_STATUS_KEY,1);


		intent.putExtra("POSITION",position);
		startActivityForResult(intent,1);
	}

	@Override
	public void onRefresh() 
	{
		isSub = true;
		isCreated = true;
		mNextPageId = 0;
		hitService();
	}

	private void setSelector(int selectorVal) 
	{
		if(selectorVal==1)
		{
			mSubscribeTV.setTextColor(getResources().getColor(R.color.c_green_selector_0b6f20));
			mMyCreatedTV.setTextColor(getResources().getColor(R.color.c_forgot_pswd_heading_3a3a3a));
			//view
			mSubscribeSelectorV.setBackgroundColor(getResources().getColor(R.color.c_green_selector_0b6f20));
			mMyCreatedSelectorV.setBackgroundColor(getResources().getColor(R.color.c_white));

		}
		else if (selectorVal==2)
		{
			mSubscribeTV.setTextColor(getResources().getColor(R.color.c_forgot_pswd_heading_3a3a3a));
			mMyCreatedTV.setTextColor(getResources().getColor(R.color.c_green_selector_0b6f20));
			//view
			mSubscribeSelectorV.setBackgroundColor(getResources().getColor(R.color.c_white));
			mMyCreatedSelectorV.setBackgroundColor(getResources().getColor(R.color.c_green_selector_0b6f20));


		}

	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode==1)
		{
			if(resultCode==-1)
			{
				mNextPageId = 0;
				isSub = true;
				isCreated = true;
				if(mType==1)
				{
					hitService();
				}
			}
		}
		else if (requestCode==20) 
		{

			int val =	data.getIntExtra("save", 0);

			if(val==1)
			{
				hitService();

			}
		}
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}

	@Override
	public void onAnimationEnd(Animation animation) {
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}



	/*
	 * delete service
	 */

	public void hitDeleteDataApi(String interest_id)
	{
		/*
		 * service_access_key, method, user_access_token, user_id , interest_id
		 */
		RequestBean requestBean = new RequestBean();
		if(NetworkStatus.isInternetOn(getActivity()))
		{
			try
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(AppParserConstant.SERVICE_ACCESS_KEY,AppParserConstant.SERVICE_ACCESS_KEY_VAL);
				jsonObject.put(AppParserConstant.METHOD_NAME_KEY,AppParserConstant.METHOD_DELETE_DATA);
				jsonObject.put(AppParserConstant.USER_ACCESS_TOKEN_KEY,mAppPreferences.getUserAccessToken());
				jsonObject.put(AppParserConstant.USER_ID_KEY,mAppPreferences.getUserId());
				jsonObject.put(AppParserConstant.INTRST_ID_KEY,interest_id);

				requestBean.setLoader(true);
				mProgressBar.setVisibility(View.GONE);
				requestBean.setUrl(AppParserConstant.HIT_URL_INTRST);
				requestBean.setLoader(true);
				requestBean.setJsonObject(jsonObject);
				requestBean.setActivity(getActivity());
				requestBean.setCallableObect(ProfileFragment.this);
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
				isHit = true;;

				e.printStackTrace();
			}
		}
		else
		{
			isHit = true;;
			Toast.makeText(getActivity(),getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();
		}


	}


	public void hit_Delete(String interest_id)
	{
		if(isHit)
		{
			isHit = false;
			hitDeleteDataApi(interest_id);
		}
	}

	public void setDeleteInterestResponse(CommonBean result) 
	{
		isHit = true;

		//	Toast.makeText(getActivity(),result.getResponseString(),Toast.LENGTH_SHORT).show();
		if(result!=null)
		{
			if(result.getStatus()==1)
			{
				isCreated = true;
				mNextPageId=0;
				mType = 2;
				hitService();
			}
		}
	}

	public void setChkPswdResponse(InterestDeatailBean result) 
	{
		if(result.getStatus()==1)
		{
			sendIntent(mPosition);
		}
		else
		{
			Toast.makeText(getActivity(),result.getResponseString(),Toast.LENGTH_SHORT).show();
		}
	}
}
