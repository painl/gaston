package com.in.gaston.fragment;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.in.gaston.CreateInterestActivity;
import com.in.gaston.DashBoardActivity;
import com.in.gaston.InterestDetailActivity;
import com.in.gaston.R;
import com.in.gaston.adapter.FetchAllInterestAdapter;
import com.in.gaston.adapter.FetchCommentOnInterestAdapter;
import com.in.gaston.adapter.ShowListOfLikeUnlikeCommentAdapter;
import com.in.gaston.apptypeface.AppTypeFace;
import com.in.gaston.asyntask.CommentSendAsyncTask;
import com.in.gaston.asyntask.FetchIntrstAsyn;
import com.in.gaston.asyntask.InterestDetailAsynTask;
import com.in.gaston.asyntask.LikeUnlikeAsynTask;
import com.in.gaston.asyntask.ListLikeUnlikeAsynTask;
import com.in.gaston.bean.CommentBean;
import com.in.gaston.bean.InteresCommentBean;
import com.in.gaston.bean.InterestArrayListBean;
import com.in.gaston.bean.InterestBean;
import com.in.gaston.bean.InterestDeatailBean;
import com.in.gaston.bean.ListLikeUnlikeCommentBean;
import com.in.gaston.bean.ListLikeUnlikeCommentObjBean;
import com.in.gaston.bean.RequestBean;
import com.in.gaston.constant.AppParserConstant;
import com.in.gaston.constant.AppPreferences;
import com.in.gaston.constant.GastonConstant;
import com.in.gaston.network.NetworkStatus;
import com.in.gaston.view.CustomView;

public class HomeFragment extends Fragment implements OnClickListener,android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener
{
	private TextView mInterestTV,mActivityFeedsTV,mPouplarTV,mRecentTV;
	private ListView mHomeLV;
	private View mIntrstSelectorV,mActivityFeedSelectorV;
	private AppPreferences mAppPreferences;
	private int mTopInterest = 0;
	private int mPerPage = 10,mNextPageId = 0;
	private ArrayList<InterestBean> mArrayList;
	private FetchAllInterestAdapter mFetchAllInterestAdapter;
	private FetchCommentOnInterestAdapter mFetchCommentOnInterestAdapter;
	private AppTypeFace mTypeFace;
	private boolean isRecent = true;
	private boolean is_hit=false;
	private ProgressBar mProgressBar;
	private SwipeRefreshLayout swipeLayout;
	private TextView mHeadingTV;
	private ArrayList<ListLikeUnlikeCommentObjBean>mArrayLikedList;
	private String mPicturePath="";
	private EditText mSearchET;
	private ShowListOfLikeUnlikeCommentAdapter mShowListAdapter;
	private ImageView mSerachIMG,mFilterIMG;
	private boolean isSearchVisible = false,isRecentSearch = true,isHitActivityFeeds = false;
	private String mSearchVal="";
	private CustomView mCustomView;
	private ImageView mCrossIMG;
	private LinearLayout mInterestLL,mCategoryLL;
	private TextView mRecentSearchTV;
	private TextView noDataTV;
	protected String mProtectedPswdVal="";
	private boolean isInterest = true,isActivityFeeds = false;
	private	ArrayList<InteresCommentBean> mActivityFeedArraList;
	private RelativeLayout  window;
	private int mPosition;
	private int likePosition;
	private int commentLikeStatus;
	private int mListType;
	private int mPerPageIdLikeUnlikeList;
	private TextView totalCountTV;
	private int fromWhere;
	private FileBody mCommentImageBody;
	private CustomView customView;
	private int all_fetch_type = 0,pswd_protected_fetch_type =2 ,my_created_fetch_type = 1; 
	private int fetch_type = 0;
	private ImageView mFlipIMG;
	private boolean isPswd;
	private String mCommentId;
	private TextView mNoDataCmntTV;
	private TextView mNoDataHome;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{

		View view = inflater.inflate(R.layout.fragment_home,container, false);

		((DashBoardActivity)getActivity()).back_press_val("interest");


		/* window =   (RelativeLayout) view.findViewById(R.id.rl);
		final Activity activity = getActivity();
		final View content = activity.findViewById(android.R.id.content).getRootView();
		if (content.getWidth() > 0) {
			Bitmap image = Blurbackground.blur(content);
			window.setBackgroundDrawable(new BitmapDrawable(activity.getResources(), image));
		} else {
			content.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					Bitmap image = Blurbackground.blur(content);
					window.setBackgroundDrawable(new BitmapDrawable(activity.getResources(), image));
				}
			});
		}
		 */
		initView(view);		
		initVariables();
		hitservice(all_fetch_type);
		defaultSetting();


		return view;
	}

	@SuppressWarnings("deprecation")
	private void initView(View view) 
	{
		mTypeFace =new AppTypeFace(getActivity());
		mNoDataHome =  (TextView)view.findViewById(R.id.tv_no_data1);

		mFlipIMG = 	(ImageView)view.findViewById(R.id.img_flip);
		noDataTV	= (TextView)view.findViewById(R.id.tv_no_data);
		mInterestLL	= (LinearLayout) view.findViewById(R.id.rl_heading);
		mCategoryLL	= (LinearLayout) view.findViewById(R.id.ll_category);
		mRecentSearchTV =    (TextView) view.findViewById(R.id.tv_recent_search);
		mInterestTV = 	(TextView) view.findViewById(R.id.tv_interest);
		mIntrstSelectorV = 	view.findViewById(R.id.view_interest_selector);
		mActivityFeedsTV	=  (TextView) view.findViewById(R.id.tv_activity_feeds);
		mActivityFeedSelectorV = 	view.findViewById(R.id.view_activity_feeds_selector);
		mRecentTV = (TextView) view.findViewById(R.id.tv_recent_tab);
		mPouplarTV =  (TextView) view.findViewById(R.id.tv_popular_tab);
		mHomeLV =  (ListView) view.findViewById(R.id.ll_interest);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
		mHeadingTV =  (TextView)view.findViewById(R.id.tv_heading);
		mSearchET =  (EditText) view.findViewById(R.id.et_search);
		mSerachIMG = (ImageView) view.findViewById(R.id.img_search);
		mCrossIMG = (ImageView) view.findViewById(R.id.img_cross);
		mFilterIMG =  (ImageView) view.findViewById(R.id.img_filter);

		/**
		 * 
		 */
		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright, 
				android.R.color.holo_green_light, 
				android.R.color.holo_orange_light, 
				android.R.color.holo_red_light);


		noDataTV.setTypeface(mTypeFace.getTypeRoboto_REGULAR());
		mSearchET.setTypeface(mTypeFace.getTypeRoboto_REGULAR());
		mInterestTV.setTypeface(mTypeFace.getTypeRoboto_REGULAR());
		mActivityFeedsTV.setTypeface(mTypeFace.getTypeRoboto_REGULAR());
		mRecentTV.setTypeface(mTypeFace.getTypeRoboto_REGULAR());
		mPouplarTV.setTypeface(mTypeFace.getTypeRoboto_REGULAR());
		mHeadingTV.setTypeface(mTypeFace.getTypeRoboto_REGULAR());
		mSearchET.setTypeface(mTypeFace.getTypeRobotoLight());
		mRecentSearchTV.setTypeface(mTypeFace.getTypeRoboto_BOLD());

		mCrossIMG.setOnClickListener(this);
		mSerachIMG.setOnClickListener(this);
		mSearchET.setOnClickListener(this);
		mInterestTV.setOnClickListener(this);  
		mActivityFeedsTV.setOnClickListener(this);
		mRecentTV.setOnClickListener(this);
		mPouplarTV.setOnClickListener(this);
		mFilterIMG.setOnClickListener(this);
		noDataTV.setOnClickListener(this);
		mFlipIMG.setOnClickListener(this);
	}

	private void defaultSetting() 
	{
		setSelector(1);
		setSelector(3);
		fetch_type = all_fetch_type;
		//	hitFetchAllInterestService(all_fetch_type);

	}

	private void initVariables() 
	{
		customView = new CustomView(getActivity(),getActivity());

		mActivityFeedArraList = new  ArrayList<InteresCommentBean>();
		mArrayLikedList = new ArrayList<ListLikeUnlikeCommentObjBean>();
		mCustomView = new CustomView(getActivity(),getActivity());
		mAppPreferences = AppPreferences.getInstance(getActivity());

		Log.e("device token", mAppPreferences.getDeviceToken());
		mArrayList = new ArrayList<InterestBean>();
		mFetchAllInterestAdapter = new FetchAllInterestAdapter(HomeFragment.this,getActivity(),mArrayList,0);
		mShowListAdapter = new ShowListOfLikeUnlikeCommentAdapter(getActivity(),HomeFragment.this,mArrayLikedList);

		mFetchCommentOnInterestAdapter = new FetchCommentOnInterestAdapter(HomeFragment.this, getActivity(), mActivityFeedArraList);


		mHomeLV.setAdapter(mFetchAllInterestAdapter);
		//mHomeLV.setAdapter(mFetchCommentOnInterestAdapter);

		mSearchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) 
				{
					mCustomView.hideSoftKeyboard(getActivity());

					if(!mSearchET.getText().toString().equalsIgnoreCase(""))
					{
						mNextPageId = 0;

						if(mTopInterest==0)
						{
							isRecent = true;

						}
						else if(mTopInterest==1)
						{
							isRecent = false;
						}
						mSearchVal =mSearchET.getText().toString();
						fetch_type = all_fetch_type;
						hitFetchAllInterestService(all_fetch_type);
						//Toast.makeText(getActivity(),"search",Toast.LENGTH_SHORT).show();

					}
					else
					{
						//Toast.makeText(getActivity(),"Plz enter keyword",Toast.LENGTH_SHORT).show();
						mSearchVal= "";
					}
					return true;
				}
				return false;
			}
		});

	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.tv_interest :
			setSelector(1);
			mNoDataHome.setVisibility(View.GONE);
			mCategoryLL.setVisibility(View.VISIBLE);

			/*
			 * set adapter
			 */
			mHomeLV.setAdapter(mFetchAllInterestAdapter);


			if(!isInterest)
			{


				((DashBoardActivity)getActivity()).back_press_val("interest");

				if(mTopInterest ==0)
				{
					isRecent = true;	
				}
				else
				{
					isRecent  =false;
				}
				mNextPageId = 0;
				isInterest = true;
				isActivityFeeds = false;
				hitservice(all_fetch_type);
			}
			else
			{
			}
			break;

		case R.id.tv_activity_feeds:
			setSelector(2);
			mNoDataHome.setVisibility(View.GONE);
			mCategoryLL.setVisibility(View.GONE);

			/*
			 * set adapter
			 */
			mHomeLV.setAdapter(mFetchCommentOnInterestAdapter);
			if(!isActivityFeeds)
			{

				((DashBoardActivity)getActivity()).back_press_val("activity_feeds");

				mNextPageId = 0;
				isInterest = false;
				isActivityFeeds = true;
				hitActivityFeedsService();
			}
			else
			{

			}


			break;
		case R.id.tv_recent_tab:
			setSelector(3);
			mNextPageId = 0;
			isRecent = true;
			/*
			 * mTopIntrst =  0  => Recent Tab
			 * mTopIntrst = 1 => Populat Tab
			 */
			if(mTopInterest==1)
			{
				mTopInterest = 0;
				hitFetchAllInterestService(all_fetch_type);
				fetch_type = all_fetch_type;
			}
			else
			{

			}
			break;

		case R.id.tv_popular_tab:
			mNextPageId = 0;
			isRecent = false;

			setSelector(4);

			if(mTopInterest==0)
			{
				mTopInterest = 1;
				hitFetchAllInterestService(all_fetch_type);
			}
			break;


		case R.id.img_cross:
			/*
			 * isRecentSearch = true=> user can search
			 */
			isRecentSearch = true;
			mArrayList.clear();
			mFlipIMG.setVisibility(View.VISIBLE);

			mCustomView.hideSoftKeyboard(getActivity());
			mNextPageId = 0;
			mSearchVal = "";
			mInterestLL.setVisibility(View.VISIBLE);
			mCategoryLL.setVisibility(View.VISIBLE);
			mSearchET.setVisibility(View.GONE);
			mFilterIMG .setVisibility(View.VISIBLE);
			mRecentSearchTV.setVisibility(View.GONE);
			mHeadingTV.setVisibility(View.VISIBLE);
			mCrossIMG.setVisibility(View.GONE);

			mSearchET.setText("");
			if(isInterest)
			{
				mCategoryLL.setVisibility(View.VISIBLE);

				if(mTopInterest==0)
				{
					isRecent = true;
				}
				else if(mTopInterest==1)
				{
					isRecent = false;
				}


				hitFetchAllInterestService(all_fetch_type);
				fetch_type = all_fetch_type;
			}
			else
			{
				mCategoryLL.setVisibility(View.GONE);
				mHomeLV.setAdapter(mFetchCommentOnInterestAdapter);
				hitActivityFeedsService();
			}

			break;

		case R.id.img_search:

			mFlipIMG.setVisibility(View.GONE);

			mNextPageId = 0;
			mCustomView.hideSoftKeyboard(getActivity());

			if(isInterest)
			{
				if(mTopInterest==0)
				{
					//Recent tab is opened
					isRecent = true;

				}
				else if(mTopInterest==1)
				{
					// if topInterest is 1 that means popular is opened
					isRecent = false;
				}
			}
			/*
			 * visibility
			 */
			mHeadingTV.setVisibility(View.INVISIBLE);
			mInterestLL.setVisibility(View.GONE);
			mCategoryLL.setVisibility(View.GONE);
			mFilterIMG .setVisibility(View.GONE);
			mSearchET.setVisibility(View.VISIBLE);
			mCrossIMG.setVisibility(View.VISIBLE);			
			mRecentSearchTV.setVisibility(View.VISIBLE);

			if(isRecentSearch)
			{

				isRecentSearch = false;
				hitRecentSearchService();
			}
			else
			{
			}
			break;

		case R.id.img_filter:
			show_filter_dialog();
			break;

		case R.id.tv_no_data:
			Intent intent = new Intent(getActivity(),CreateInterestActivity.class);
			startActivity(intent);
			break;

		case R.id.img_flip:
			mSearchVal= "";
			if(isInterest)
			{
				if(mAppPreferences.isReal())
				{
					mAppPreferences.setIsReal(false);
					mNextPageId= 0;
					hitservice(all_fetch_type);
				}
				else
				{
					mAppPreferences.setIsReal(true);
					mNextPageId= 0;
					hitservice(all_fetch_type);
				}
			}
			else
			{
				if(mAppPreferences.isReal())
				{
					((DashBoardActivity)getActivity()).back_press_val("activity_feeds");
					mAppPreferences.setIsReal(false);
				}
				else
				{
					mAppPreferences.setIsReal(true);
				}
				mNextPageId = 0;
				isInterest = false;
				isActivityFeeds = true;
				hitActivityFeedsService();
			}



			break;

		default:
			break;
		}
	}

	public void hitActivityFeedsService() 
	{
		if(!isHitActivityFeeds)
		{
			isHitActivityFeeds = true;
			hitActivityFeedsDataService();
		}

	}

	private void hitActivityFeedsDataService() 
	{
		/*
		 * service_access_key, 
		 * method, 
		 * user_access_token, 
		 * user_id ,
		 * profile_status , 
		 * filter_type , 
		 * per_page ,
		 *  page_id
		 */

		mNoDataHome.setVisibility(View.GONE);
		RequestBean requestBean = new RequestBean();
		if(NetworkStatus.isInternetOn(getActivity()))
		{
			try
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(AppParserConstant.SERVICE_ACCESS_KEY,AppParserConstant.SERVICE_ACCESS_KEY_VAL);
				jsonObject.put(AppParserConstant.METHOD_NAME_KEY,AppParserConstant.METHOD_ACTIVITY_FEEDS);
				jsonObject.put(AppParserConstant.USER_ACCESS_TOKEN_KEY,mAppPreferences.getUserAccessToken());
				jsonObject.put(AppParserConstant.USER_ID_KEY,mAppPreferences.getUserId());
				jsonObject.put(AppParserConstant.FILTER_TYPE_KEY,"");
				jsonObject.put(AppParserConstant.PER_PAGE,mPerPage);
				jsonObject.put(AppParserConstant.PAGE_ID,mNextPageId);
				
				if(mAppPreferences.isReal())
				{
					//real

					jsonObject.put(AppParserConstant.PROFILE_STATUS_KEY,1);
					mAppPreferences.setUserProfileStatus("1");
				}
				else
				{
					//fake
					jsonObject.put(AppParserConstant.PROFILE_STATUS_KEY,2);
					mAppPreferences.setUserProfileStatus("2");
				}

				if(mNextPageId ==0)
				{
					requestBean.setLoader(true);

					mProgressBar.setVisibility(View.GONE);
				}
				else
				{
					mProgressBar.setVisibility(View.VISIBLE);
				}
				requestBean.setUrl(AppParserConstant.HIT_URL_INTRST);
				requestBean.setJsonObject(jsonObject);
				requestBean.setCallableObect(HomeFragment.this);
				requestBean.setActivity(getActivity());
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
			isHitActivityFeeds = false;
			Toast.makeText(getActivity(),getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();
		}




	}

	private void setSelector(int selectorVal) 
	{
		if(selectorVal==1)
		{
			mInterestTV.setTextColor(getResources().getColor(R.color.c_green_selector_0b6f20));
			mActivityFeedsTV.setTextColor(getResources().getColor(R.color.c_forgot_pswd_heading_3a3a3a));
			//view
			mIntrstSelectorV.setBackgroundColor(getResources().getColor(R.color.c_green_selector_0b6f20));
			mActivityFeedSelectorV.setBackgroundColor(getResources().getColor(R.color.c_white));

		}
		else if (selectorVal==2)
		{
			mInterestTV.setTextColor(getResources().getColor(R.color.c_forgot_pswd_heading_3a3a3a));
			mActivityFeedsTV.setTextColor(getResources().getColor(R.color.c_green_selector_0b6f20));
			//view
			mIntrstSelectorV.setBackgroundColor(getResources().getColor(R.color.c_white));
			mActivityFeedSelectorV.setBackgroundColor(getResources().getColor(R.color.c_green_selector_0b6f20));


		}
		else if (selectorVal==3)
		{
			mRecentTV.setTextColor(getResources().getColor(R.color.c_white));
			mPouplarTV.setTextColor(getResources().getColor(R.color.c_forgot_pswd_heading_3a3a3a));

			mRecentTV.setBackgroundResource(R.drawable.intereststabgreen);
			mPouplarTV.setBackgroundResource(R.drawable.intereststabgray);

		}
		else if (selectorVal==4)
		{
			mRecentTV.setTextColor(getResources().getColor(R.color.c_forgot_pswd_heading_3a3a3a));
			mPouplarTV.setTextColor(getResources().getColor(R.color.c_white));

			mRecentTV.setBackgroundResource(R.drawable.intereststabgrayleft);
			mPouplarTV.setBackgroundResource(R.drawable.intereststabgreenright);

		}
	}


	@SuppressLint("NewApi")
	private void hitFetchAllInterestService(int filter_type)
	{
		noDataTV.setVisibility(View.GONE);
		/**
		 * service_access_key, 
		 * method, 
		 * user_access_token,
		 * user_id,
		 * top_interest, 
		 * profile_status, 
		 * filter_type,
		 *  search_interest,
		 *   per_page, 
		 *   page_id
		 */

		/*
		 * top_interest = 1 for most subscribed , 
		 * 0 for latest, 
		 * profile_status =  1 for real 2 for fake, 
		 * filter_type = 1 my created 2 for password protected
		 */

		RequestBean requestBean = new RequestBean();
		if(NetworkStatus.isInternetOn(getActivity()))
		{
			try
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(AppParserConstant.SERVICE_ACCESS_KEY,AppParserConstant.SERVICE_ACCESS_KEY_VAL);
				jsonObject.put(AppParserConstant.METHOD_NAME_KEY,AppParserConstant.METHOD_FETCH_ALL_DATA);
				jsonObject.put(AppParserConstant.USER_ACCESS_TOKEN_KEY,mAppPreferences.getUserAccessToken());
				jsonObject.put(AppParserConstant.USER_ID_KEY,mAppPreferences.getUserId());
				jsonObject.put(AppParserConstant.TOP_INTRST_KEY,mTopInterest);
				jsonObject.put(AppParserConstant.FILTER_TYPE_KEY,filter_type);
				jsonObject.put(AppParserConstant.SEARCH_INTRST,mSearchVal);
				jsonObject.put(AppParserConstant.PER_PAGE,mPerPage);
				jsonObject.put(AppParserConstant.PAGE_ID,mNextPageId);

				if(mAppPreferences.isReal())
				{
					//real
					jsonObject.put(AppParserConstant.PROFILE_STATUS_KEY,1);
					mAppPreferences.setUserProfileStatus("1");
				}
				else
				{
					//fake
					jsonObject.put(AppParserConstant.PROFILE_STATUS_KEY,2);
					mAppPreferences.setUserProfileStatus("2");
				}

				if(mNextPageId ==0)
				{
					requestBean.setLoader(true);

					mProgressBar.setVisibility(View.GONE);
				}
				else
				{
					mProgressBar.setVisibility(View.VISIBLE);
				}
				requestBean.setUrl(AppParserConstant.HIT_URL_INTRST);
				requestBean.setJsonObject(jsonObject);
				requestBean.setCallableObect(HomeFragment.this);
				requestBean.setActivity(getActivity());
				FetchIntrstAsyn fetchIntrstAsyn = new FetchIntrstAsyn(requestBean);

				if(VERSION.SDK_INT < VERSION_CODES.HONEYCOMB)
				{
					fetchIntrstAsyn.execute();
				}else{
					fetchIntrstAsyn.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");	
				}

			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			is_hit = false;
			Toast.makeText(getActivity(),getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();
		}


	}

	public void setFetchInterestResponse(InterestArrayListBean result) 
	{

		//Toast.makeText(getActivity(),result.getResponseString(), Toast.LENGTH_SHORT).show();
		mProgressBar.setVisibility(View.GONE);
		//mHomeLV.onRefreshComplete();

		if(swipeLayout.isRefreshing())
		{
			swipeLayout.setRefreshing(false);
		}

		is_hit = false;
		if(result.getStatus()==1)
		{
			mNoDataHome.setVisibility(View.GONE);
			noDataTV.setVisibility(View.GONE);


			if(mNextPageId==0)
			{
				mArrayList.clear();
			}
			mNextPageId++;
			if(isInterest)
			{
				if(mTopInterest==0)
				{
					if(isRecent)
					{
						mArrayList.clear();
						isRecent = false;
					}
				}
				else if(mTopInterest==1)
				{
					if(!isRecent)
					{
						mArrayList.clear();
						isRecent = true;

					}
				}
			}
			else
			{
				mHomeLV.setAdapter(mFetchAllInterestAdapter);
				mArrayList.clear();
			}
			mFetchAllInterestAdapter.setfilter_type(fetch_type);
			mFetchAllInterestAdapter.setTotalRecord(result.getTotal_record());
			mArrayList.addAll(result.getArrayList());
			mFetchAllInterestAdapter.notifyDataSetChanged();
		}
		else if(result.getStatus()==0)
		{
			mNoDataHome.setVisibility(View.GONE);
			mArrayList.clear();
			mFetchAllInterestAdapter.setfilter_type(fetch_type);
			mFetchAllInterestAdapter.notifyDataSetChanged();
			noDataTV.setVisibility(View.VISIBLE);
		}
		else if(result.getStatus()==2)
		{
			mArrayList.clear();
			mNoDataHome.setVisibility(View.VISIBLE);
		}
	}
	public void hitservice(int fetch_type)
	{
		if(!is_hit)
		{
			fetch_type = fetch_type;
			is_hit = true;
			hitFetchAllInterestService(fetch_type);
		}
	}
	@Override
	public void onRefresh() 
	{

		mNextPageId =0;

		if(mTopInterest==0)
		{
			isRecent = true;

		}
		else if(mTopInterest==1)
		{
			isRecent = false;
		}

		if(isRecentSearch)
		{
			if(isInterest)
			{

				hitservice(all_fetch_type);
				fetch_type = all_fetch_type;
			}
			else if (isActivityFeeds) 
			{
				hitActivityFeedsService();
			}
		}
		else
		{
			if(swipeLayout.isRefreshing())
			{
				swipeLayout.setRefreshing(false);
			}
			//hitRecentSearchService();
		}
	}



	public void hitRecentSearchService()
	{
		/*(
		 * service_access_key, method, user_access_token, user_id 
		 */
		RequestBean requestBean = new RequestBean();
		if(NetworkStatus.isInternetOn(getActivity()))
		{
			try
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(AppParserConstant.SERVICE_ACCESS_KEY,AppParserConstant.SERVICE_ACCESS_KEY_VAL);
				jsonObject.put(AppParserConstant.METHOD_NAME_KEY,AppParserConstant.METHOD_RECENT_SEARCH);
				jsonObject.put(AppParserConstant.USER_ACCESS_TOKEN_KEY,mAppPreferences.getUserAccessToken());
				jsonObject.put(AppParserConstant.USER_ID_KEY,mAppPreferences.getUserId());

				if(mNextPageId ==0)
				{
					requestBean.setLoader(true);

					mProgressBar.setVisibility(View.GONE);
				}
				else
				{
					mProgressBar.setVisibility(View.VISIBLE);
				}
				requestBean.setUrl(AppParserConstant.HIT_URL_INTRST);
				requestBean.setJsonObject(jsonObject);
				requestBean.setCallableObect(HomeFragment.this);
				requestBean.setActivity(getActivity());
				FetchIntrstAsyn fetchIntrstAsyn = new FetchIntrstAsyn(requestBean);

				if(VERSION.SDK_INT < VERSION_CODES.HONEYCOMB)
				{
					fetchIntrstAsyn.execute();
				}else{
					fetchIntrstAsyn.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");	
				}

			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			is_hit = false;
			Toast.makeText(getActivity(),getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();
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
				requestBean.setCallableObect(HomeFragment.this);
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
			Toast.makeText(getActivity(),getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();
		}

	}

	public void sendIntent(int position)
	{
		mPosition = position;
		Intent intent = new Intent(getActivity(),InterestDetailActivity.class);
		intent.putExtra(GastonConstant.INTRST_ID_KEY,mArrayList.get(position).getInterest_id());
		intent.putExtra(GastonConstant.PROFILE_STATUS_KEY,mAppPreferences.getUserProfileStatus());
		intent.putExtra(GastonConstant.PSWD_PROTECTED_KEY,mProtectedPswdVal);
		intent.putExtra(GastonConstant.USER_ID_KEY,mArrayList.get(position).getUser_id());
		intent.putExtra(GastonConstant.SUBSCRIBE_STATUS_KEY,mArrayList.get(position).getUser_subscribe_status());


		intent.putExtra("POSITION",position);
		startActivityForResult(intent,1);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		//	Toast.makeText(getActivity(), "onActivityResult=>outside", Toast.LENGTH_SHORT).show();

		if(requestCode==1)
		{
			if(resultCode==-1)
			{
				//	Toast.makeText(getActivity(), "onActivityResult=>inside", Toast.LENGTH_SHORT).show();

				String sub_count =  data.getStringExtra("sub_count");
				String sub_button_text =  data.getStringExtra("sub_button_text");
				mArrayList.get(mPosition).setTotal_subscribed(sub_count);


				if(sub_button_text.equalsIgnoreCase("Unsubscribe"))
				{
					mArrayList.get(mPosition).setUser_subscribe_status(1);

				}
				else if(sub_button_text.equalsIgnoreCase("Subscribe"))
				{
					mArrayList.get(mPosition).setUser_subscribe_status(0);

				}

				mFetchAllInterestAdapter.notifyDataSetChanged();


			}
		}
	}

	public void setActvityFeedsResponse(InterestDeatailBean result) 
	{
		isHitActivityFeeds = false;
		if(result.getStatus()==1)
		{
			if(swipeLayout.isRefreshing())
			{
				swipeLayout.setRefreshing(false);
			}

			if(isPswd)
			{
				sendIntent(mPosition);
				isPswd = false;
			}
			else
			{
				mProgressBar.setVisibility(View.GONE);
				noDataTV.setVisibility(View.GONE);

				if(result!=null)
				{
					if(result.getStatus()==1)
					{
						mNoDataHome.setVisibility(View.GONE);

						if(mNextPageId == 0)
						{
							mActivityFeedArraList.clear();
						}
						mActivityFeedArraList.addAll(result.getmArrayList());
						mFetchCommentOnInterestAdapter.setTotalRecord(result.getTotal_record());
						mFetchCommentOnInterestAdapter.notifyDataSetChanged();
						mNextPageId++;

					}
					else
					{
						mNoDataHome.setVisibility(View.VISIBLE);
					}
				}
			}
		}
		else
		{
			if(isPswd)
			{
				Toast.makeText(getActivity(),result.getResponseString(),Toast.LENGTH_SHORT).show();
				isPswd = false;
				if(mActivityFeedArraList!=null)
				{
					mActivityFeedArraList.clear();				
					mFetchCommentOnInterestAdapter.notifyDataSetChanged();
				}
			}
			else
			{
				if(mActivityFeedArraList!=null)
				{
					mActivityFeedArraList.clear();				
					mFetchCommentOnInterestAdapter.notifyDataSetChanged();
				}
				mNoDataHome.setVisibility(View.VISIBLE);

			}
		}
	}

	public void setMediaControl(MediaPlayer result) 
	{
		mFetchCommentOnInterestAdapter.mediaPlayer(result);

	}


	/*
	 * hit like unlike service
	 */

	/*
	 * like unlike
	 */

	public void hitLikeUnlikeService(int position, String interestId, String comment_id, int comment_like_status)
	{

		likePosition = position;
		commentLikeStatus = comment_like_status;

		/*
		 * service_access_key, method, user_access_token, user_id , interest_id , comment_id ,like_status
		 */
		RequestBean requestBean = new RequestBean();
		if(NetworkStatus.isInternetOn(getActivity()))
		{
			try
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(AppParserConstant.SERVICE_ACCESS_KEY,AppParserConstant.SERVICE_ACCESS_KEY_VAL);
				jsonObject.put(AppParserConstant.METHOD_NAME_KEY,AppParserConstant.METHOD_LIKE_UNLIKE);
				jsonObject.put(AppParserConstant.USER_ACCESS_TOKEN_KEY,mAppPreferences.getUserAccessToken());
				jsonObject.put(AppParserConstant.USER_ID_KEY,mAppPreferences.getUserId());
				jsonObject.put(AppParserConstant.INTRST_ID_KEY,interestId);
				jsonObject.put(AppParserConstant.COMMENT_ID,comment_id);
				jsonObject.put(AppParserConstant.LIKE_STATUS_KEY,comment_like_status);

				requestBean.setUrl(AppParserConstant.HIT_URL_INTRST);
				requestBean.setLoader(true);
				requestBean.setJsonObject(jsonObject);
				requestBean.setActivity(getActivity());
				requestBean.setCallableObect(HomeFragment.this);
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
			Toast.makeText(getActivity(),getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();
		}
	}





	public void setLikeUnLikeResponse(CommentBean result) 
	{

		/*
		 * 0 = unlike
		 * 1 = like
		 * 2 
		 */

		if(result.getStatus()==1)
		{
			//mFetchCommentOnInterestAdapter.updateItem(mPosition, result);

			mActivityFeedArraList.get(likePosition).setTotal_like(result.getTotal_like());
			mActivityFeedArraList.get(likePosition).setTptal_dislike(result.getTotal_unlike());
			mActivityFeedArraList.get(likePosition).setComment_like_status(result.getLike_unlike_status());
			//mFetchCommentOnInterestAdapter.updateItem(mPosition,mComment_like_status);
			mFetchCommentOnInterestAdapter.notifyDataSetChanged();
		}
		else
		{

		}

	}

	public void show_list_dialog(final String comment_id, int list_type, final String interest_id)
	{
		mArrayLikedList.clear();
		mListType = list_type;
		mPerPageIdLikeUnlikeList = 0;
		mShowListAdapter.setdata(comment_id, list_type);

		ImageView crossIMG;
		final Dialog dialog = new Dialog(getActivity(), R.style.transparent_background_dialog);
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
		totalCountTV.setTypeface(mTypeFace.getTypeRoboto_Medium());
		RelativeLayout commentRL =  (RelativeLayout)dialog.findViewById(R.id.ll_comment_text);
		final EditText commentET= (EditText)dialog.findViewById(R.id.et_comment_text);


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
					Toast.makeText(getActivity(),"Please enter text", Toast.LENGTH_SHORT).show();
				}
				else
				{
					CustomView.hideSoftKeyboard(getActivity());
					fromWhere = 2;
					hitsentCommentTextService(1,comment_id,commentET.getText().toString(),interest_id);
					//dialog.dismiss();

				}
			}
		});



		hit_show_like_unlike_comment_Api(comment_id, list_type);

		crossIMG.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				CustomView.hideSoftKeyboard(getActivity());
				dialog.dismiss();
				//	hitservice();

			}
		});
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
		RequestBean requestBean = new RequestBean();
		if(NetworkStatus.isInternetOn(getActivity()))
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
				requestBean.setActivity(getActivity());
				requestBean.setCallableObect(HomeFragment.this);

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
			Toast.makeText(getActivity(),getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();
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

	private void hitsentCommentTextService(int commentType, String comment_id, String comment_text, String interest_id) 
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
		if(NetworkStatus.isInternetOn(getActivity()))
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
					StringBody userProfileStatusBody = new StringBody(mAppPreferences.getUserProfileStatus());
					StringBody interestIDBody = new StringBody(interest_id);
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

						String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
						mFileName += "/audiorecordtest.3gp";

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
					requestBean.setActivity(getActivity());
					requestBean.setCallableObect(HomeFragment.this);
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
			Toast.makeText(getActivity(),getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();

		}

	}

	public void setCommentSendResponse(CommentBean result) 
	{
		customView.hideSoftKeyboard(getActivity());

		if(result.getStatus()==1)
		{/*
			mNextPageId = 0;
			isInterest = false;
			isActivityFeeds = true;
			hitActivityFeedsService();*/
			mPerPageIdLikeUnlikeList = 0;
			hit_show_like_unlike_comment_Api(mCommentId,3);
		}
	}


	public void show_filter_dialog()
	{
		final Dialog dialog = new Dialog(getActivity(), R.style.transparent_background_dialog);
		dialog.setContentView(R.layout.dialog_filter);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialog.getWindow().setAttributes(lp);
		dialog.setCancelable(true);
		dialog.show();
		//
		TextView all = 	(TextView) dialog.findViewById(R.id.tv_all);
		TextView password_protected = 	(TextView) dialog.findViewById(R.id.tv_pswd_protected);
		TextView my_created = 	(TextView) dialog.findViewById(R.id.tv_my_created);
		all.setTypeface(mTypeFace.getTypeRoboto_REGULAR());
		password_protected.setTypeface(mTypeFace.getTypeRoboto_REGULAR());
		my_created.setTypeface(mTypeFace.getTypeRoboto_REGULAR());


		all.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				isRecent = true;
				mTopInterest = 0;
				fetch_type = all_fetch_type;
				mFetchAllInterestAdapter.setfilter_type(fetch_type);
				setSelector(1);
				setSelector(3);
				mNextPageId = 0;

				hitservice(all_fetch_type);

				dialog.dismiss();
			}
		});
		password_protected.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				isRecent = true;
				mTopInterest = 0;
				fetch_type = pswd_protected_fetch_type;
				mFetchAllInterestAdapter.setfilter_type(fetch_type);

				setSelector(1);
				setSelector(3);
				mNextPageId = 0;
				hitservice(pswd_protected_fetch_type);

				dialog.dismiss();


			}
		});
		my_created.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				fetch_type = my_created_fetch_type;
				mFetchAllInterestAdapter.setfilter_type(fetch_type);
				isRecent = true;
				mTopInterest = 0;

				setSelector(1);
				setSelector(3);
				mNextPageId = 0;
				hitservice(my_created_fetch_type);
				dialog.dismiss();

			}
		});

	}


	public void disable_listview_scroll()
	{
		mHomeLV.setSaveEnabled(false);
	}

}
