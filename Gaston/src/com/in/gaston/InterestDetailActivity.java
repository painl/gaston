package com.in.gaston;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.in.gaston.adapter.FetchCommentOnInterestAdapter;
import com.in.gaston.adapter.ShowListOfLikeUnlikeCommentAdapter;
import com.in.gaston.apptypeface.AppTypeFace;
import com.in.gaston.asyntask.CommentSendAsyncTask;
import com.in.gaston.asyntask.ForgetPswdAsynTask;
import com.in.gaston.asyntask.InterestDetailAsynTask;
import com.in.gaston.asyntask.LikeUnlikeAsynTask;
import com.in.gaston.asyntask.ListLikeUnlikeAsynTask;
import com.in.gaston.asyntask.SubscribeAsynTask;
import com.in.gaston.bean.CommentBean;
import com.in.gaston.bean.CommonBean;
import com.in.gaston.bean.InteresCommentBean;
import com.in.gaston.bean.InterestDeatailBean;
import com.in.gaston.bean.ListLikeUnlikeCommentBean;
import com.in.gaston.bean.ListLikeUnlikeCommentObjBean;
import com.in.gaston.bean.RequestBean;
import com.in.gaston.bean.SubscribeBean;
import com.in.gaston.constant.AppParserConstant;
import com.in.gaston.constant.AppPreferences;
import com.in.gaston.constant.GastonConstant;
import com.in.gaston.helper.FileUtils;
import com.in.gaston.imageloader.ImageLoader;
import com.in.gaston.network.NetworkStatus;
import com.in.gaston.view.AppUtils;
import com.in.gaston.view.CustomView;
import com.in.gaston.view.RoundImageViewGray;
import com.in.gaston.view.RoundedImageView;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

public class InterestDetailActivity extends Activity implements OnClickListener,android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener,ImageChooserListener
{
	private AppPreferences mAppPreferences;
	private AppTypeFace mAppTypeFace;
	private TextView mIntrstNameTV,mReportTV;
	private RoundedImageView mIntsrtIMG;
	private TextView mSubscribeCountTV,NoDataTV;
	private Button mSubscribeBTN;
	private TextView mIntrstCreatedOnTV;
	private RoundImageViewGray mUserIMG;
	private TextView mUserNameTV;
	private TextView mIntrstDescTV;
	private ListView mListView;
	private boolean is_hit=false;
	private CustomView customView ;

	private ImageView mTextIMG;
	private ImageView mPicIMG;
	private ImageView mAudioIMG;
	private String mInterestIDString;
	private String mProfileStatusString;
	private int mNestPageId = 0;
	private String mPasswordSTring="";
	private int mPerPage=10;
	private LinearLayout mCreatoeNameIMGLL;
	private boolean isText = false,isPic = false,isAud = false;
	private FetchCommentOnInterestAdapter mFetchCommentOnInterestAdapter;
	private ShowListOfLikeUnlikeCommentAdapter mShowListAdapter;
	private ArrayList<InteresCommentBean> mArrayList;
	private ArrayList<ListLikeUnlikeCommentObjBean>mArrayLikedList;
	//private FetchCommentOnInterestAdapter 	
	private SwipeRefreshLayout swipeLayout;
	private LinearLayout commentTypeLL,rl_interest_detail,ll_view1;
	private LinearLayout doCommentLL;
	private EditText commentTextET;
	private ImageView mSendCommentText,mPicIMG1;
	private String mPicturePath="",userIMG="",user_name="";
	private int chooserType;
	private TextView totalCountTV,mUserNameTV1;
	private ProgressDialog pbar;
	private Uri selectedImage;
	private ImageChooserManager imageChooserManager;
	private FileBody mCommentImageBody;
	private int mPosition,mSubscribeStatus=0;
	private int mComment_like_status;
	private boolean isSubscribe = true;
	private String userIdString,interesrId_Of_Created_Interest="";
	private MediaRecorder mRecorder;
	private String intrstIMG = "",mFileName;
	private CountDownTimer mCountDownTimer;
	private TextView timerTV ;
	private boolean isRecord = false;
	private int mCountDownTime = 0;
	private int userSubInterest,mPerPageIdLikeUnlikeList = 0;
	private int mListType,audio_selection_rc = 11;
	private int fromWhere;
	private boolean isSenOptionTextClicked =false;
	//	private String mProfileIMG;
	private boolean isAudioRecord;
	private  EditText commentET;
	private MediaPlayer mMediaPlayer;
	private String mCommentId;
	private TextView mNoDataCmntTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interest_detail);
		initVariables();
		getIntentData();
		initView();
		setAdapter();
		hitservice();
	}
	public void hitservice()
	{
		if(!is_hit)
		{
			is_hit = true;
			hitInterestDetailService();
		}
	}
	private void setAdapter() 
	{
		mListView.setAdapter(mFetchCommentOnInterestAdapter);
	}


	private void getIntentData() 
	{
		////////////////////
		Intent intent = getIntent();
		mInterestIDString =  intent.getStringExtra(GastonConstant.INTRST_ID_KEY);
		mProfileStatusString =  intent.getStringExtra(GastonConstant.PROFILE_STATUS_KEY);
		mPasswordSTring =  intent.getStringExtra(GastonConstant.PSWD_PROTECTED_KEY);
		userIdString = intent.getStringExtra(GastonConstant.USER_ID_KEY);
		userSubInterest = intent.getIntExtra(GastonConstant.SUBSCRIBE_STATUS_KEY,0);
	}
	private void initVariables() 
	{

		customView = new CustomView(InterestDetailActivity.this, InterestDetailActivity.this);
		mArrayList = new ArrayList<InteresCommentBean>();
		mArrayLikedList = new ArrayList<ListLikeUnlikeCommentObjBean>();
		mAppPreferences  = AppPreferences.getInstance(getApplicationContext());
		mFetchCommentOnInterestAdapter = new FetchCommentOnInterestAdapter(InterestDetailActivity.this, mArrayList);
		mShowListAdapter = new ShowListOfLikeUnlikeCommentAdapter(InterestDetailActivity.this,mArrayLikedList);
		mAppTypeFace = new AppTypeFace(getApplicationContext());
	}
	private void initView() 
	{

		ll_view1 =  (LinearLayout)findViewById(R.id.ll_view1);

		rl_interest_detail =  (LinearLayout)findViewById(R.id.rl_interest_detail);
		mPicIMG1  =	(ImageView)findViewById(R.id.img_user1);
		mUserNameTV1 = (TextView)findViewById(R.id.tv_user_name1);	
		mSendCommentText  = (ImageView)findViewById(R.id.img_send_comment);
		mCreatoeNameIMGLL =  (LinearLayout)findViewById(R.id.ll_view);
		NoDataTV = (TextView) findViewById(R.id.tv_no_data);
		commentTypeLL = (LinearLayout)findViewById(R.id.ll_comment_type);
		doCommentLL = (LinearLayout)findViewById(R.id.ll_comment_text);
		commentTextET =    (EditText) findViewById(R.id.et_comment_text);
		mIntrstNameTV = (TextView)findViewById(R.id.tv_interest_name);
		mReportTV = (TextView)findViewById(R.id.tv_report);	
		mIntsrtIMG = (RoundedImageView)findViewById(R.id.img_intrst_image);	
		mSubscribeCountTV = (TextView)findViewById(R.id.tv_subscribe_count);	
		mSubscribeBTN = (Button)findViewById(R.id.btn_subscribe);	
		mIntrstCreatedOnTV = (TextView)findViewById(R.id.tv_intrst_created_on);	
		mUserIMG = (com.in.gaston.view.RoundImageViewGray)findViewById(R.id.img_user);	
		mUserNameTV = (TextView)findViewById(R.id.tv_user_name);	
		mIntrstDescTV = (TextView)findViewById(R.id.tv_intrst_desc);
		mListView = (ListView)findViewById(R.id.ll_interest);	
		mTextIMG = (ImageView)findViewById(R.id.img_text);	
		mPicIMG = (ImageView)findViewById(R.id.img_pic);	
		mAudioIMG = (ImageView)findViewById(R.id.img_aud);
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		if(mAppPreferences.getUserId().equalsIgnoreCase(userIdString))
		{
			mSubscribeBTN.setVisibility(View.GONE);
			mSubscribeCountTV.setVisibility(View.GONE);
		}
		else
		{
			mSubscribeBTN.setVisibility(View.VISIBLE);
			mSubscribeCountTV.setVisibility(View.VISIBLE);
		}


		if(userSubInterest==1)
		{
			mIntrstDescTV.setVisibility(View.GONE);
		}
		else
		{
			mIntrstDescTV.setVisibility(View.VISIBLE);

		}

		TextView messageTV = (TextView) findViewById(R.id.tv_message);
		messageTV.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
		mSubscribeBTN.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());
		mIntrstNameTV.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
		mReportTV.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());
		mIntrstCreatedOnTV.setTypeface(mAppTypeFace.getTypeRobotoLight());
		mUserNameTV.setTypeface(mAppTypeFace.getTypeRoboto_BOLD());
		mIntrstDescTV.setTypeface(mAppTypeFace.getTypeRobotoLight());
		mSubscribeCountTV.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
		swipeLayout.setOnRefreshListener(this);
		mTextIMG.setOnClickListener(this);
		mPicIMG.setOnClickListener(this);
		mAudioIMG.setOnClickListener(this);
		mReportTV.setOnClickListener(this);
		mSubscribeBTN.setOnClickListener(this);
		mSendCommentText.setOnClickListener(this);
		mIntsrtIMG.setOnClickListener(this);


		//
	}

	@Override
	public void onClick(View view) 
	{
		switch (view.getId()) 
		{

		case R.id.img_text:


			isSenOptionTextClicked = true;
			if(isText)
			{
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
				commentTextET.setCursorVisible(true);
				commentTextET.requestFocus();
				commentTextET.setText("");
				commentTypeLL.setVisibility(View.GONE);
				doCommentLL.setVisibility(View.VISIBLE);
			}
			break;

		case R.id.img_send_comment:
			CustomView.hideSoftKeyboard(InterestDetailActivity.this);
			isSenOptionTextClicked = false;

			if(commentTextET.getText().toString().equalsIgnoreCase(""))
			{
				Toast.makeText(InterestDetailActivity.this,"Please enter text", Toast.LENGTH_SHORT).show();
			}
			else
			{
				fromWhere = 1;
				hitsentCommentTextService(1,"",commentTextET.getText().toString());
			}
			break;

		case R.id.img_pic:
			isSenOptionTextClicked = false;


			if(isPic)
			{
				showDialog("");
			}

			break;
		case R.id.img_aud:
			isSenOptionTextClicked = false;

			if(isAud)
			{
				//hitsentCommentTextService(3);

				/*
				 * open recorder dialog
				 */

				showDialog("audio_record");

			}

			break;
		case R.id.btn_subscribe:


			/*
			 * status 1 subscribe, 0 unsubscribe
			 */
			if(isSubscribe)
			{
				mSubscribeStatus = 1;
				hitSubsCribeService();	
			}
			else
			{
				mSubscribeStatus = 0;
				hitSubsCribeService();	
			}
			break;
		case R.id.tv_report:
			hitReportApi();

			break;

		case R.id.img_intrst_image :


			CustomView.zoom_in_user_pic(InterestDetailActivity.this, intrstIMG);


			break;

		default:
			break;
		}
	}


	private void hitReportApi() 
	{

		//service_access_key, method, user_access_token, user_id , interest_id

		RequestBean requestBean = new RequestBean();
		if(NetworkStatus.isInternetOn(InterestDetailActivity.this))
		{
			try
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(AppParserConstant.SERVICE_ACCESS_KEY,AppParserConstant.SERVICE_ACCESS_KEY_VAL);
				jsonObject.put(AppParserConstant.METHOD_NAME_KEY,"reportinterest");
				jsonObject.put(AppParserConstant.USER_ACCESS_TOKEN_KEY,mAppPreferences.getUserAccessToken());
				jsonObject.put(AppParserConstant.USER_ID_KEY,mAppPreferences.getUserId());
				jsonObject.put(AppParserConstant.INTRST_ID_KEY,mInterestIDString);
				requestBean.setLoader(true);
				requestBean.setUrl(AppParserConstant.HIT_URL_INTRST);
				requestBean.setJsonObject(jsonObject);
				requestBean.setActivity(InterestDetailActivity.this);
				ForgetPswdAsynTask interestDetailAsynTask = new ForgetPswdAsynTask(requestBean);

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
			is_hit=false;
			Toast.makeText(InterestDetailActivity.this,getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();
		}


	}
	private void openRecorderDialog() 
	{
		mCountDownTime = 0;
		final Button gallery;
		final Button camera;
		final Dialog dialog = new Dialog(InterestDetailActivity.this, R.style.transparent_background_dialog);
		dialog.setContentView(R.layout.dialog_record_sound);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialog.getWindow().setAttributes(lp);


		timerTV = (TextView) dialog.findViewById(R.id.tv_timer);
		TextView tapToRecordTV = (TextView) dialog.findViewById(R.id.tv_timer_text);
		Button recordIMG =  (Button) dialog.findViewById(R.id.img_record);
		tapToRecordTV.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());
		timerTV.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());

		dialog.show();
		recordIMG.setOnTouchListener(new OnTouchListener() 
		{

			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				if(event.getAction() == MotionEvent.ACTION_DOWN) 
				{

					if(!isRecord)
					{
						isRecord = true;
						//Toast.makeText(InterestDetailActivity.this,"recorder start", Toast.LENGTH_SHORT).show();

						startRecorder();
						startCountDown();
						//Toast.makeText(InterestDetailActivity.this, "touch", Toast.LENGTH_SHORT).show();
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP) 
				{
					isRecord = false;
					stopRecorder();
					mCountDownTimer.cancel();
					//Toast.makeText(InterestDetailActivity.this,"recorder stop", Toast.LENGTH_SHORT).show();
					//	Toast.makeText(InterestDetailActivity.this,"release touch", Toast.LENGTH_SHORT).show();
					timerTV.setText("00:00");
					dialog.dismiss();
					hitsentCommentTextService(3,"","");
				}
				return false;
			}

		});
	}


	private void startCountDown() 
	{
		mCountDownTimer = new CountDownTimer(62000, 1000) 
		{

			@Override
			public void onTick(long millisUntilFinished) 
			{
				long millisUntilF1inished = 62000-millisUntilFinished;
				timerTV.setText("0"+mCountDownTime+":"+millisUntilF1inished / 1000);
			}
			@Override
			public void onFinish() 
			{
				mCountDownTime ++;
				mCountDownTimer.start();
			}
		};

		mCountDownTimer.start();

	}

	private void startRecorder() 
	{
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mRecorder.setOutputFile(mFileName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e("record", "prepare() failed");
		}

		mRecorder.start();

	}


	//stop recorder

	private void stopRecorder() 
	{
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
	}


	private void hitSubsCribeService() 
	{

		/*
		 * service_access_key, method, user_access_token, user_id, interest_id, status
		 */
		RequestBean requestBean = new RequestBean();
		if(NetworkStatus.isInternetOn(InterestDetailActivity.this))
		{
			try
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(AppParserConstant.SERVICE_ACCESS_KEY,AppParserConstant.SERVICE_ACCESS_KEY_VAL);
				jsonObject.put(AppParserConstant.METHOD_NAME_KEY,AppParserConstant.METHOD_SUBSCRIBE);
				jsonObject.put(AppParserConstant.USER_ACCESS_TOKEN_KEY,mAppPreferences.getUserAccessToken());
				jsonObject.put(AppParserConstant.USER_ID_KEY,mAppPreferences.getUserId());
				jsonObject.put(AppParserConstant.INTRST_ID_KEY,mInterestIDString);
				jsonObject.put(AppParserConstant.STATUS_KEY,mSubscribeStatus);

				requestBean.setUrl(AppParserConstant.HIT_URL_INTRST);
				requestBean.setLoader(true);
				requestBean.setJsonObject(jsonObject);
				requestBean.setActivity(InterestDetailActivity.this);
				SubscribeAsynTask signInAsynTask = new SubscribeAsynTask(requestBean);

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
			Toast.makeText(InterestDetailActivity.this,getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();
		}

	}

	private void hitsentCommentTextService(int commentType, String comment_id, String comment_text) 
	{
		/*
		 * service_access_key, 
		 * method, 
		 * user_access_token, 
		 * comment_id , 
		 * user_id , 
		 * user_profile_status , 
		 * interest_id , 
		 * parent_comment_id , 
		 * comment_type ,
		 * comment_text , 
		 * comment_image , 
		 * comment_audio,
		 */
		RequestBean requestBean = new RequestBean();
		if(NetworkStatus.isInternetOn(InterestDetailActivity.this))
		{
			try
			{
				MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
				try {
					StringBody commentTextBody = null,commentImageBody=null,commentSoundBody=null;
					StringBody serviceAccessKeyStringBody = new StringBody(AppParserConstant.SERVICE_ACCESS_KEY_VAL);
					StringBody methodeStringBody = new StringBody(AppParserConstant.METHOD_ADD_COMMENT);
					StringBody userAccessTokenBody = new StringBody(mAppPreferences.getUserAccessToken());
					StringBody commentIdBody = new StringBody("");
					StringBody userIdBody = new StringBody(mAppPreferences.getUserId());
					StringBody userProfileStatusBody = new StringBody(mProfileStatusString);
					StringBody interestIDBody = new StringBody(mInterestIDString);
					StringBody parentCommentIdBody = new StringBody(comment_id);
					StringBody commentTypeBody = new StringBody(String.valueOf(commentType));

					if(commentType==1)
					{
						//text


						commentTextBody = new StringBody(comment_text);

						commentImageBody = new StringBody("");
						commentSoundBody = new StringBody("");
					}
					else if (commentType==2) 
					{
						//image
						commentTextBody = new StringBody("");
						commentImageBody = new StringBody("");
						commentSoundBody = new StringBody("");


						if (mPicturePath != null && mPicturePath.trim().length() > 0) 
						{
							File profilePicFile = new File(mPicturePath);
							if (mPicturePath.endsWith(".png")) {
								mCommentImageBody = new FileBody(profilePicFile, GastonConstant.THUMBNAIL_IMAGE_PNG);
							} else if (mPicturePath.endsWith("jpg")) {
								mCommentImageBody = new FileBody(profilePicFile,  GastonConstant.THUMBNAIL_IMAGE_JPG);
							} else if (mPicturePath.endsWith("jpeg")) {
								mCommentImageBody = new FileBody(profilePicFile,  GastonConstant.THUMBNAIL_IMAGE_JPEG);
							}

							else{
								mCommentImageBody = new FileBody(profilePicFile,  GastonConstant.THUMBNAIL_IMAGE_JPEG);

							}
						}
					}
					else if (commentType==3) 
					{
						//sound
						commentTextBody = new StringBody("");
						commentImageBody = new StringBody("");
						File file  =  new File(mFileName);

						if(file.exists())
						{
							//Toast.makeText(InterestDetailActivity.this,"file exist", Toast.LENGTH_SHORT).show();
							reqEntity.addPart(AppParserConstant.COMMENT_AUD, new FileBody(file));  

						}
						else
						{
							//Toast.makeText(InterestDetailActivity.this,"file does not exist", Toast.LENGTH_SHORT).show();
							commentSoundBody = new StringBody("");
							reqEntity.addPart(AppParserConstant.COMMENT_AUD, commentSoundBody);
						}

					}

					/*
					 * add
					 */
					reqEntity.addPart(AppParserConstant.SERVICE_ACCESS_KEY, serviceAccessKeyStringBody);
					reqEntity.addPart(AppParserConstant.METHOD_NAME_KEY, methodeStringBody);
					reqEntity.addPart(AppParserConstant.USER_ACCESS_TOKEN_KEY, userAccessTokenBody);
					reqEntity.addPart(AppParserConstant.COMMENT_ID, commentIdBody);
					reqEntity.addPart(AppParserConstant.USER_ID_KEY, userIdBody);
					reqEntity.addPart(AppParserConstant.USER_PROFILE_STATUS_KEY, userProfileStatusBody);
					reqEntity.addPart(AppParserConstant.INTRST_ID_KEY, interestIDBody);
					reqEntity.addPart(AppParserConstant.PARENT_COMMENT_ID_KEY, parentCommentIdBody);
					reqEntity.addPart(AppParserConstant.COMMENT_TYPE, commentTypeBody);
					reqEntity.addPart(AppParserConstant.COMMENT_TEXT, commentTextBody);
					if(commentType==2)
					{
						reqEntity.addPart(AppParserConstant.COMMENT_IMG, mCommentImageBody);
					}
					else
					{
						reqEntity.addPart(AppParserConstant.COMMENT_IMG, commentImageBody);
					}
					requestBean.setUrl(AppParserConstant.HIT_URL_INTRST);
					requestBean.setLoader(true);
					requestBean.setMultipartEntity(reqEntity);
					requestBean.setActivity(InterestDetailActivity.this);
					CommentSendAsyncTask signUpAsynTask = new CommentSendAsyncTask(requestBean);

					if(VERSION.SDK_INT < VERSION_CODES.HONEYCOMB){
						signUpAsynTask.execute();
					}else{
						signUpAsynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");	
					}
				}

				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}catch(Exception e)
			{

			}

		}
		else
		{
			Toast.makeText(InterestDetailActivity.this,getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();

		}

	}

	private void hitInterestDetailService() 
	{
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
		if(NetworkStatus.isInternetOn(InterestDetailActivity.this))
		{
			try
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(AppParserConstant.SERVICE_ACCESS_KEY,AppParserConstant.SERVICE_ACCESS_KEY_VAL);
				jsonObject.put(AppParserConstant.METHOD_NAME_KEY,AppParserConstant.METHOD_INTRST_DETAIL);
				jsonObject.put(AppParserConstant.USER_ACCESS_TOKEN_KEY,mAppPreferences.getUserAccessToken());
				jsonObject.put(AppParserConstant.INTRST_ID_KEY,mInterestIDString);
				jsonObject.put(AppParserConstant.PROFILE_STATUS_KEY,mProfileStatusString);
				jsonObject.put(AppParserConstant.PSWD_KEY,mPasswordSTring);
				jsonObject.put(AppParserConstant.PAGE_ID,mNestPageId);
				jsonObject.put(AppParserConstant.PER_PAGE,mPerPage);
				jsonObject.put(AppParserConstant.USER_ID_KEY,mAppPreferences.getUserId());
				requestBean.setLoader(true);
				requestBean.setUrl(AppParserConstant.HIT_URL_INTRST);
				requestBean.setJsonObject(jsonObject);
				requestBean.setActivity(InterestDetailActivity.this);
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
			is_hit=false;
			Toast.makeText(InterestDetailActivity.this,getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();
		}

	}


	public void setInterestDetailResponse(InterestDeatailBean result) 
	{

		is_hit=false;
		if(swipeLayout.isRefreshing())
		{
			swipeLayout.setRefreshing(false);
		}

		if(result!=null)
		{

			if(result.getUser_profile_status()!=1)
			{
				mCreatoeNameIMGLL.setVisibility(View.GONE);
			}
			else
			{
				mCreatoeNameIMGLL.setVisibility(View.VISIBLE);
			}

			if(result.getUser_subscibed_status()==1)
			{
				mIntrstDescTV.setVisibility(View.GONE);
			}
			else
			{
				mIntrstDescTV.setVisibility(View.VISIBLE);

			}
			if(result.getErrorCode()==1)
			{
				if(result.getInterest_report()==0)
				{
					mReportTV.setVisibility(View.VISIBLE);
				}
				else
				{
					mReportTV.setVisibility(View.GONE);

				}
				interesrId_Of_Created_Interest = result.getInterest_id();
				NoDataTV.setVisibility(View.GONE);
				intrstIMG = AppParserConstant.BASE_URL+result.getInterest_image();
				userIMG = AppParserConstant.BASE_URL+result.getInterestCreateUserIMG();
				user_name	=  result.getUserName();
				/*
				 * subscribes status check
				 */
				if(result.getUser_subscibed_status()==0)
				{
					//i.e unubscribed
					mSubscribeBTN.setText("Subscribe");
					isSubscribe = true;

					ll_view1.setVisibility(View.GONE);
					rl_interest_detail.setVisibility(View.VISIBLE);
					mUserNameTV.setText(result.getUserName());
					ImageLoader.getInstance(InterestDetailActivity.this).displayImage(userIMG, mUserIMG, false);


				}
				else if (result.getUser_subscibed_status()==1) 
				{
					//i.e subscribed
					mSubscribeBTN.setText("Unsubscribe");
					isSubscribe = false;
					rl_interest_detail.setVisibility(View.GONE);
					ll_view1.setVisibility(View.VISIBLE);
					mUserNameTV1.setText(result.getUserName());
					ImageLoader.getInstance(InterestDetailActivity.this).displayImage(userIMG, mPicIMG1, false);
				}
				mIntrstNameTV.setText(result.getInterest_name());
				mIntrstCreatedOnTV.setText("Created at "+result.getInterest_created_on());
				mSubscribeCountTV.setText(result.getTotal_subscribe());
				//mUserNameTV.setText(result.getUserName());
				mIntrstDescTV.setText(result.getInterest_desc());

				ImageLoader.getInstance(InterestDetailActivity.this).displayImage(intrstIMG, mIntsrtIMG, false);
				//	ImageLoader.getInstance(InterestDetailActivity.this).displayImage(userIMG, mUserIMG, false);

				if(result.getAllow_text().equalsIgnoreCase("1"))
				{
					isText = true;
					mTextIMG.setImageResource(R.drawable.text_active);
				}
				else
				{
					isText = false;
					mTextIMG.setImageResource(R.drawable.text_normal);

				}
				/*
				 * 
				 */
				if (result.getAllow_picture().equalsIgnoreCase("1")) 
				{
					isPic = true;
					mPicIMG.setImageResource(R.drawable.picture_active);
				}
				else
				{
					isPic = false;

					mPicIMG.setImageResource(R.drawable.picture_normal);	
				}
				/*
				 * 
				 */
				if (result.getAllow_audio().equalsIgnoreCase("1")) 
				{
					isAud = true;
					mAudioIMG.setImageResource(R.drawable.sound_active);
				}
				else
				{
					isAud = false;
					mAudioIMG.setImageResource(R.drawable.sound_normal);	
				}

				/*
				 * set comment list
				 */
				if(mNestPageId==0)
				{
					mArrayList.clear();
				}

				mFetchCommentOnInterestAdapter.setTotalRecord(result.getTotalRecord());
				mArrayList.addAll(result.getmArrayList());
				mFetchCommentOnInterestAdapter.notifyDataSetChanged();
				mNestPageId++;

			}
			else if(result.getErrorCode()==0)
			{

				if(result.getInterest_report()==0)
				{
					mReportTV.setVisibility(View.VISIBLE);
				}
				else
				{
					mReportTV.setVisibility(View.GONE);

				}
				String intrstIMG = AppParserConstant.BASE_URL+result.getInterest_image();
				userIMG = AppParserConstant.BASE_URL+result.getInterestCreateUserIMG();
				user_name	=  result.getUserName();
				if(result.getUser_subscibed_status()==0)
				{
					//i.e unubscribed
					mSubscribeBTN.setText("Subscribe");
					isSubscribe = true;
					ll_view1.setVisibility(View.GONE);
					rl_interest_detail.setVisibility(View.VISIBLE);
					mUserNameTV.setText(result.getUserName());
					ImageLoader.getInstance(InterestDetailActivity.this).displayImage(userIMG, mUserIMG, false);

				}
				else if (result.getUser_subscibed_status()==1) 
				{
					//i.e subscribed
					mSubscribeBTN.setText("Unsubscribe");
					isSubscribe = false;
					rl_interest_detail.setVisibility(View.GONE);
					ll_view1.setVisibility(View.VISIBLE);
					mUserNameTV1.setText(result.getUserName());
					ImageLoader.getInstance(InterestDetailActivity.this).displayImage(userIMG, mPicIMG1, false);

				}
				mIntrstNameTV.setText(result.getInterest_name());
				mIntrstCreatedOnTV.setText("Created at "+result.getInterest_created_on());
				mSubscribeCountTV.setText(result.getTotal_subscribe());
				mUserNameTV.setText(result.getUserName());
				mIntrstDescTV.setText(result.getInterest_desc());

				ImageLoader.getInstance(InterestDetailActivity.this).displayImage(intrstIMG, mIntsrtIMG, false);
				ImageLoader.getInstance(InterestDetailActivity.this).displayImage(userIMG, mUserIMG, false);

				if(result.getAllow_text().equalsIgnoreCase("1"))
				{
					isText = true;
					mTextIMG.setImageResource(R.drawable.text_active);
				}
				else
				{
					isText = false;
					mTextIMG.setImageResource(R.drawable.text_normal);

				}
				/*
				 * 
				 */
				if (result.getAllow_picture().equalsIgnoreCase("1")) 
				{
					isPic = true;
					mPicIMG.setImageResource(R.drawable.picture_active);
				}
				else
				{
					isPic = false;

					mPicIMG.setImageResource(R.drawable.picture_normal);	
				}
				/*
				 * 
				 */
				if (result.getAllow_audio().equalsIgnoreCase("1")) 
				{
					isAud = true;
					mAudioIMG.setImageResource(R.drawable.sound_active);
				}
				else
				{
					isAud = false;
					mAudioIMG.setImageResource(R.drawable.sound_normal);	
				}

				NoDataTV.setVisibility(View.VISIBLE);
			}
			else
			{

				if(result.getInterest_report()==0)
				{
					mReportTV.setVisibility(View.VISIBLE);
				}
				else
				{
					mReportTV.setVisibility(View.GONE);

				}
				String intrstIMG = AppParserConstant.BASE_URL+result.getInterest_image();
				userIMG = AppParserConstant.BASE_URL+result.getInterestCreateUserIMG();
				user_name	=  result.getUserName();
				if(result.getUser_subscibed_status()==0)
				{
					//i.e unubscribed
					mSubscribeBTN.setText("Subscribe");
					isSubscribe = true;
					ll_view1.setVisibility(View.GONE);
					rl_interest_detail.setVisibility(View.VISIBLE);
					mUserNameTV.setText(result.getUserName());
					ImageLoader.getInstance(InterestDetailActivity.this).displayImage(userIMG, mUserIMG, false);

				}
				else if (result.getUser_subscibed_status()==1) 
				{
					//i.e subscribed
					mSubscribeBTN.setText("Unsubscribe");
					isSubscribe = false;
					rl_interest_detail.setVisibility(View.GONE);
					ll_view1.setVisibility(View.VISIBLE);
					mUserNameTV1.setText(result.getUserName());
					ImageLoader.getInstance(InterestDetailActivity.this).displayImage(userIMG, mPicIMG1, false);

				}
				mIntrstNameTV.setText(result.getInterest_name());
				mIntrstCreatedOnTV.setText("Created at "+result.getInterest_created_on());
				mSubscribeCountTV.setText(result.getTotal_subscribe());
				mUserNameTV.setText(result.getUserName());
				mIntrstDescTV.setText(result.getInterest_desc());

				ImageLoader.getInstance(InterestDetailActivity.this).displayImage(intrstIMG, mIntsrtIMG, false);
				ImageLoader.getInstance(InterestDetailActivity.this).displayImage(userIMG, mUserIMG, false);

				if(result.getAllow_text().equalsIgnoreCase("1"))
				{
					isText = true;
					mTextIMG.setImageResource(R.drawable.text_active);
				}
				else
				{
					isText = false;
					mTextIMG.setImageResource(R.drawable.text_normal);

				}
				/*
				 * 
				 */
				if (result.getAllow_picture().equalsIgnoreCase("1")) 
				{
					isPic = true;
					mPicIMG.setImageResource(R.drawable.picture_active);
				}
				else
				{
					isPic = false;

					mPicIMG.setImageResource(R.drawable.picture_normal);	
				}
				/*
				 * 
				 */
				if (result.getAllow_audio().equalsIgnoreCase("1")) 
				{
					isAud = true;
					mAudioIMG.setImageResource(R.drawable.sound_active);
				}
				else
				{
					isAud = false;
					mAudioIMG.setImageResource(R.drawable.sound_normal);	
				}

				NoDataTV.setVisibility(View.VISIBLE);
			}


		}
	}

	@Override
	public void onRefresh() 
	{
		if(swipeLayout.isRefreshing())
		{
			mNestPageId = 0;
			hitservice();

		}

	}

	public void setMediControl(MediaPlayer result) 
	{
		mMediaPlayer = result;
		mFetchCommentOnInterestAdapter.mediaPlayer(result);
	}

	public void showDialog(String audio_recording)
	{
		final Button gallery;
		final Button camera;
		final Dialog dialog = new Dialog(InterestDetailActivity.this, R.style.transparent_background_dialog);
		dialog.setContentView(R.layout.dialog_custom);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialog.getWindow().setAttributes(lp);
		gallery	= (Button) dialog.findViewById(R.id.btn_gallery);
		TextView text = (TextView) dialog.findViewById(R.id.tv_dialog_text);
		camera	= (Button) dialog.findViewById(R.id.btn_camera);
		dialog.show();
		text.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
		gallery.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
		camera.setTypeface(mAppTypeFace.getTypeRoboto_Medium());

		if(audio_recording.equalsIgnoreCase("audio_record"))
		{
			isAudioRecord = true;
			camera.setText("Record Now");
		}
		else
		{
			isAudioRecord=false;
			camera.setText("Camera");
		}
		gallery.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{

				dialog.dismiss();
				if(!isAudioRecord)
				{
					chooseImage();
				}
				else
				{
					Intent intent = new Intent();
					intent.setType("audio/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(Intent.createChooser(intent, "Open Audio (mp3) file"), audio_selection_rc);
				}
			}
		});

		camera.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				dialog.dismiss();
				if(!isAudioRecord)
				{
					takePicture();
				}
				else
				{
					mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
					mFileName += "/audiorecordtest.3gp";
					openRecorderDialog();
				}
			}
		});
	}
	private void showProgress(boolean show) {
		if(pbar==null) {
			pbar = new ProgressDialog(InterestDetailActivity.this);
			pbar.setTitle(R.string.app_name);
			pbar.setMessage(getString(R.string.text_please_wait));
		}
		if((!pbar.isShowing()) && show) {
			pbar.show();
		}else {
			pbar.cancel();
		}
	}



	private void chooseImage() {
		chooserType = ChooserType.REQUEST_PICK_PICTURE;
		imageChooserManager = new ImageChooserManager(this,
				ChooserType.REQUEST_PICK_PICTURE, FileUtils.APP_FOLDER_NAME, true);
		imageChooserManager.setImageChooserListener(this);
		try {
			showProgress(true);
			mPicturePath = imageChooserManager.choose();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void takePicture() {
		chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
		imageChooserManager = new ImageChooserManager(this,
				ChooserType.REQUEST_CAPTURE_PICTURE, FileUtils.APP_FOLDER_NAME, true);
		imageChooserManager.setImageChooserListener(this);
		try {
			showProgress(true);
			mPicturePath = imageChooserManager.choose();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (resultCode == RESULT_OK
				&& (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
			if (imageChooserManager == null) 
			{
				reinitializeImageChooser();
			}
			imageChooserManager.submit(requestCode, data);
		} else {
			showProgress(false);
		}
		if(resultCode== RESULT_OK && requestCode == GastonConstant.CROP_PIC_INTENT_KEY)
		{
			try {

				File file = new File(mPicturePath);
				if (file.isFile()) 
				{

					selectedImage = Uri.fromFile(file);
					Bitmap decodeFile = FileUtils.decodeFile(mPicturePath);
					//mUserIMG.setImageBitmap(decodeFile);
					hitsentCommentTextService(2,"","");
				}
				else
				{
					Toast.makeText(InterestDetailActivity.this,R.string.text_no_resource_found, Toast.LENGTH_SHORT).show();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		if(resultCode==RESULT_OK&&requestCode==audio_selection_rc)
		{
			String file_path = "";
			Uri audioFileUri = data.getData();
			AppUtils appUtils = new AppUtils(InterestDetailActivity.this);


			file_path =	appUtils.getRealPathFromURI1(audioFileUri);

			/*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
			{
				Toast.makeText(InterestDetailActivity.this, "Build Version-->"+Build.VERSION.SDK_INT+"", Toast.LENGTH_SHORT).show();
				file_path =appUtils.get_file_from_uri_lolipop(audioFileUri);	
			}
			else
			{
				file_path =	appUtils.getRealPathFromURI1(audioFileUri);
			}*/
			//Toast.makeText(InterestDetailActivity.this,"Audio file path-- >"+file_path, Toast.LENGTH_LONG).show();
			mFileName = file_path;
			hitsentCommentTextService(3,"","");
		}

	}

	@Override
	public void onImageChosen(final ChosenImage image) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				showProgress(false);
				if (image != null) {
					Log.d("onImageCHoosen", image.getFilePathOriginal());
					String filePathOriginal = image.getFilePathOriginal();
					File file = new File(filePathOriginal);

					selectedImage = Uri.fromFile(file);
					File parentDirectory = file.getParentFile();

					try {
						mPicturePath = parentDirectory.getAbsolutePath()+File.separator+"cropped.jpg";
						if(!performCrop(mPicturePath)) {
							Log.e("OnImageChoosen", "falling back to uncropped version :: no cropper found");
							mUserIMG.setImageURI(Uri.parse(new File(image
									.getFileThumbnail()).toString()));	
						}
					} catch (IOException e) {
						Log.e("OnImageChoosen", "falling back to uncropped version ::"+ e.getMessage());
						e.printStackTrace();
						mUserIMG.setImageURI(Uri.parse(new File(image
								.getFileThumbnail()).toString()));

					}

					//					imageViewThumbSmall.setImageURI(Uri.parse(new File(image
					//							.getFileThumbnailSmall()).toString()));
				}
			}
		});
	}

	@Override
	public void onError(final String reason) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				showProgress(false);
				/*Toast.makeText(InterestDetailActivity.this, reason,
						Toast.LENGTH_LONG).show();*/
			}
		});
	}

	// Should be called if for some reason the ImageChooserManager is null (Due
	// to destroying of activity for low memory situations)
	private void reinitializeImageChooser() {
		imageChooserManager = new ImageChooserManager(this, chooserType,
				FileUtils.APP_FOLDER_NAME, true);
		imageChooserManager.setImageChooserListener(this);
		imageChooserManager.reinitialize(mPicturePath);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("chooser_type", chooserType);
		outState.putString("media_path", mPicturePath);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("chooser_type")) {
				chooserType = savedInstanceState.getInt("chooser_type");
			}

			if (savedInstanceState.containsKey("media_path")) {
				mPicturePath = savedInstanceState.getString("media_path");
			}
		}
		super.onRestoreInstanceState(savedInstanceState);
	}
	/**
	 * this function does the crop operation.
	 * @throws IOException 
	 */
	private boolean performCrop(String outputPath) throws IOException 
	{
		try {
			//call the standard crop action intent (the user device may not support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP"); 
			//indicate image type and Uri
			cropIntent.setDataAndType(selectedImage, "image/*");
			//set crop properties
			cropIntent.putExtra("crop", "true");
			//indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			//indicate output X and Y
			cropIntent.putExtra("outputX", 400);
			cropIntent.putExtra("outputY", 400);



			File file = new File(outputPath);
			if(file.exists()) {
				file.delete();
			}

			file.createNewFile();

			cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			//retrieve data on return
			cropIntent.putExtra("return-data", true);
			//start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, GastonConstant.CROP_PIC_INTENT_KEY);
			return true;
		}
		// respond to users whose devices do not support the crop action
		catch (ActivityNotFoundException anfe) 
		{
			// display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
			return false;
		}
	}

	public void setCommentSendResponse(CommentBean result) 
	{
		CustomView.hideSoftKeyboard(InterestDetailActivity.this);
		//	Toast.makeText(InterestDetailActivity.this,result.getResponseString(),Toast.LENGTH_SHORT).show();
		if(result.getStatus()==1)
		{
			//fromWhere == 1 i.e comes from interest detail comment
			//fromWhere == 2 i.e comes from dialog of interest detail
			if(fromWhere==1)
			{

				mNestPageId = 0;
				doCommentLL.setVisibility(View.GONE);
				commentTypeLL.setVisibility(View.VISIBLE);
				hitservice();
			}
			else
			{

				//mNestPageId = 0;
				doCommentLL.setVisibility(View.GONE);
				commentTypeLL.setVisibility(View.VISIBLE);
				mPerPageIdLikeUnlikeList = 0;
				hit_show_like_unlike_comment_Api(mCommentId,3);
				//hitservice();

			}
		}
	}

	/*
	 * like unlike
	 */

	public void hitLikeUnlikeService(int position, String interestId, String comment_id, int comment_like_status)
	{
		mPosition = position;
		mComment_like_status = comment_like_status;
		/*
		 * service_access_key, method, user_access_token, user_id , interest_id , comment_id ,like_status
		 */
		RequestBean requestBean = new RequestBean();
		if(NetworkStatus.isInternetOn(InterestDetailActivity.this))
		{
			try
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(AppParserConstant.SERVICE_ACCESS_KEY,AppParserConstant.SERVICE_ACCESS_KEY_VAL);
				jsonObject.put(AppParserConstant.METHOD_NAME_KEY,AppParserConstant.METHOD_LIKE_UNLIKE);
				jsonObject.put(AppParserConstant.USER_ACCESS_TOKEN_KEY,mAppPreferences.getUserAccessToken());
				jsonObject.put(AppParserConstant.USER_ID_KEY,mAppPreferences.getUserId());
				jsonObject.put(AppParserConstant.INTRST_ID_KEY,interesrId_Of_Created_Interest);
				jsonObject.put(AppParserConstant.COMMENT_ID,comment_id);
				jsonObject.put(AppParserConstant.LIKE_STATUS_KEY,comment_like_status);


				requestBean.setUrl(AppParserConstant.HIT_URL_INTRST);
				requestBean.setLoader(true);
				requestBean.setJsonObject(jsonObject);
				requestBean.setActivity(InterestDetailActivity.this);
				LikeUnlikeAsynTask signInAsynTask = new LikeUnlikeAsynTask(requestBean);

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
			Toast.makeText(InterestDetailActivity.this,getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();
		}
	}

	public void setLikeUnlikeResponse(CommentBean result) 
	{
		/*
		 * 0 = unlike
		 * 1 = like
		 * 2 
		 */
		//Toast.makeText(InterestDetailActivity.this,result.getResponseString(), Toast.LENGTH_SHORT).show();

		if(result.getStatus()==1)
		{
			//mFetchCommentOnInterestAdapter.updateItem(mPosition, result);

			mArrayList.get(mPosition).setTotal_like(result.getTotal_like());
			mArrayList.get(mPosition).setTptal_dislike(result.getTotal_unlike());
			mArrayList.get(mPosition).setComment_like_status(result.getLike_unlike_status());

			//mFetchCommentOnInterestAdapter.updateItem(mPosition,mComment_like_status);

			mFetchCommentOnInterestAdapter.notifyDataSetChanged();
		}
	}

	public void setsubscibedStatusResponse(SubscribeBean result) 
	{
		if(result!=null)
		{
			if(result.getStatus()==1)
			{
				mSubscribeCountTV.setText(result.getTotal_subscribe());

				if(isSubscribe)
				{
					isSubscribe = false;
					mIntrstDescTV.setVisibility(View.GONE);
					mSubscribeBTN.setText("Unsubscribe");
					ll_view1.setVisibility(View.VISIBLE);
					rl_interest_detail.setVisibility(View.GONE);
					mUserNameTV1.setText(user_name);

					ImageLoader.getInstance(InterestDetailActivity.this).displayImage(userIMG, mPicIMG1, false);
				}
				else
				{
					isSubscribe = true;
					mIntrstDescTV.setVisibility(View.VISIBLE);
					mSubscribeBTN.setText("Subscribe");

					ll_view1.setVisibility(View.GONE);
					rl_interest_detail.setVisibility(View.VISIBLE);
					mUserNameTV.setText(user_name);
					ImageLoader.getInstance(InterestDetailActivity.this).displayImage(userIMG, mUserIMG, false);

				}
			}
		}
	}


	/*@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event)  
	{  
		//Toast.makeText(InterestDetailActivity.this, "onKeyDown", Toast.LENGTH_SHORT).show();
		//replaces the default 'Back' button action  
		if(keyCode==KeyEvent.KEYCODE_BACK)  
		{

		}  
		return true;  
	}  
	 */


	public void show_list_dialog(final String comment_id, int list_type)
	{
		mArrayLikedList.clear();
		mListType = list_type;
		mPerPageIdLikeUnlikeList = 0;
		mShowListAdapter.setdata(comment_id, list_type);
		ImageView crossIMG;
		final Dialog dialog = new Dialog(InterestDetailActivity.this, R.style.transparent_background_dialog);
		dialog.setContentView(R.layout.dialog_like_unlike_comment);
		mNoDataCmntTV = 	(TextView)dialog.findViewById(R.id.tv_no_data);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialog.getWindow().setAttributes(lp);
		totalCountTV = (TextView) dialog.findViewById(R.id.tv_total_count);
		crossIMG = (ImageView) dialog.findViewById(R.id.img_cross);
		ListView mListListView = (ListView) dialog.findViewById(R.id.ll_list_data);
		mListListView.setAdapter(mShowListAdapter);
		dialog.show();
		totalCountTV.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
		RelativeLayout commentRL =  (RelativeLayout)dialog.findViewById(R.id.ll_comment_text);
		commentET= (EditText)dialog.findViewById(R.id.et_comment_text);


		if(list_type==3)
		{
			mCommentId = comment_id;
			commentRL.setVisibility(View.VISIBLE);
		}
		else
		{
			commentRL.setVisibility(View.GONE);
		}

		dialog.findViewById(R.id.img_send_comment).setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				if(commentET.getText().toString().equalsIgnoreCase(""))
				{
					Toast.makeText(InterestDetailActivity.this,"Please enter text", Toast.LENGTH_SHORT).show();
				}
				else
				{
					CustomView.hideSoftKeyboard(InterestDetailActivity.this);
					fromWhere = 2;
					hitsentCommentTextService(1,comment_id,commentET.getText().toString());
					//	dialog.dismiss();

				}
			}
		});



		hit_show_like_unlike_comment_Api(comment_id, list_type);

		crossIMG.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				CustomView.hideSoftKeyboard(InterestDetailActivity.this);	
						dialog.dismiss();
				//	hitservice();

			}
		});
	}



	public void hit_service(String comment_id, int list_type)
	{
		if(!is_hit)
		{
			hit_show_like_unlike_comment_Api(comment_id,list_type);
			is_hit = true;
		}
	}

	public void hit_show_like_unlike_comment_Api(String comment_id, int list_type)
	{
		/*
		 * service_access_key, 
		 * method, 
		 * user_access_token, 
		 * user_id ,
		 * comment_id, 
		 * list_type, 
		 * per_page , 
		 * page_id
		 */
		/*
		 * list_type = 1 like list, 2= unlike list
		 */

		/*
		 * comment list
		 * service_access_key, method, user_access_token, user_id , comment_id, per_page , page_id
		 */
		RequestBean requestBean = new RequestBean();
		if(NetworkStatus.isInternetOn(InterestDetailActivity.this))
		{
			try
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(AppParserConstant.SERVICE_ACCESS_KEY,AppParserConstant.SERVICE_ACCESS_KEY_VAL);
				jsonObject.put(AppParserConstant.USER_ACCESS_TOKEN_KEY,mAppPreferences.getUserAccessToken());
				jsonObject.put(AppParserConstant.USER_ID_KEY,mAppPreferences.getUserId());
				jsonObject.put(AppParserConstant.COMMENT_ID,comment_id);
				jsonObject.put(AppParserConstant.PER_PAGE,mPerPage);
				jsonObject.put(AppParserConstant.PAGE_ID,mPerPageIdLikeUnlikeList);

				if(list_type==3)
				{
					jsonObject.put(AppParserConstant.METHOD_NAME_KEY,AppParserConstant.METHOD_LIST_CHILD_COMMENT);
				}
				else
				{
					jsonObject.put(AppParserConstant.LIST_TYPE_KEY,list_type);
					jsonObject.put(AppParserConstant.METHOD_NAME_KEY,AppParserConstant.METHOD_LIST_LIKE_UNLIKE);
				}

				requestBean.setUrl(AppParserConstant.HIT_URL_INTRST);
				requestBean.setLoader(true);
				requestBean.setJsonObject(jsonObject);
				requestBean.setActivity(InterestDetailActivity.this);
				ListLikeUnlikeAsynTask listLikeUnlikeAsynTask = new ListLikeUnlikeAsynTask(requestBean);

				if(VERSION.SDK_INT < VERSION_CODES.HONEYCOMB)
				{
					listLikeUnlikeAsynTask.execute();
				}
				else
				{
					listLikeUnlikeAsynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");	
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			Toast.makeText(InterestDetailActivity.this,getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();
		}	
	}

	public void setListLikeUnlikeResponse(ListLikeUnlikeCommentBean result) 
	{
		is_hit = false;
		if(result!=null)
		{
			if(result.getStatus()==1)
			{
				if(mListType== 1)
				{
					totalCountTV.setText("Total likes : "+String.valueOf(result.getTotal_records()));
				}
				else if (mListType== 2) 
				{
					totalCountTV.setText("Total Unlikes : "+String.valueOf(result.getTotal_records()));
				}
				else if (mListType== 3) 
				{


					totalCountTV.setText("Total Comment : "+String.valueOf(result.getTotal_records()));
				}
				mShowListAdapter.set_total_records(result.getTotal_records());
				mArrayLikedList.addAll(result.getArrayList());
				mShowListAdapter.notifyDataSetChanged();
				mPerPageIdLikeUnlikeList++;
			}
			else
			{
				if(mListType==3)
				{
					mArrayLikedList.clear();
					mNoDataCmntTV.setVisibility(View.VISIBLE);
				}
				else
				{
					mNoDataCmntTV.setVisibility(View.GONE);

				}

			}
		}
	}
	public void setReportInterestResponse(CommonBean result) 
	{
		if(result!=null)
		{
			if(result.getStatus()==1)
			{
				mReportTV.setVisibility(View.GONE);
			}
			else
			{
				//Toast.makeText(InterestDetailActivity.this, result.getResponseString(), Toast.LENGTH_SHORT).show();
			}

		}
	}



	@Override
	public void onBackPressed() 
	{

		if(mMediaPlayer!=null)
		{
			if(mMediaPlayer.isPlaying())
			{
				mMediaPlayer.pause();
			}
		}
		if(isSenOptionTextClicked)
		{

			commentTextET.setText("");
			commentTypeLL.setVisibility(View.VISIBLE);
			doCommentLL.setVisibility(View.GONE);
			isSenOptionTextClicked= false;

		}
		else
		{

			Intent intent = new Intent();
			intent.putExtra("sub_count",mSubscribeCountTV.getText().toString());
			intent.putExtra("sub_button_text",mSubscribeBTN.getText().toString());
			setResult(RESULT_OK, intent);
			finish();
		}

	}

}
