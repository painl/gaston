package com.in.gaston.fragment;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.in.gaston.DashBoardActivity;
import com.in.gaston.InterestDetailActivity;
import com.in.gaston.R;
import com.in.gaston.adapter.NotificationAdapter;
import com.in.gaston.apptypeface.AppTypeFace;
import com.in.gaston.asyntask.NotificationAsynTask;
import com.in.gaston.bean.NotificationBean;
import com.in.gaston.bean.NotificationMainBean;
import com.in.gaston.bean.RequestBean;
import com.in.gaston.constant.AppParserConstant;
import com.in.gaston.constant.AppPreferences;
import com.in.gaston.constant.GastonConstant;
import com.in.gaston.network.NetworkStatus;

public class NotificationFragment extends Fragment implements OnRefreshListener,OnItemClickListener
{
	private boolean isHit = true;
	private TextView mNoData;
	private AppPreferences mAppPreferences;
	private AppTypeFace mAppTypeFace;
	private int mPerPage=10,mNextPageId = 0;
	private ProgressBar mProgressBar;
	private ListView mListView;
	private SwipeRefreshLayout swipeLayout;
	private NotificationAdapter mNotificationAdapter;
	private ArrayList<NotificationBean> mArrayList ;
	private int mPosition;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_notification, container, false);
		((DashBoardActivity)getActivity()).back_press_val("notification");

		initView(view);
		hit_service();
		return view;
	}
	public void hit_service() 
	{
		if(isHit)
		{
			isHit = false;
			hitNotificationApi();
		}
	}

	private void hitNotificationApi() 
	{
		//service_access_key, method, user_access_token, user_id , 
		//profile_status , view_all , per_page , page_id
		mNoData.setVisibility(View.GONE);
		RequestBean requestBean = new RequestBean();
		if(NetworkStatus.isInternetOn(getActivity()))
		{
			try
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(AppParserConstant.SERVICE_ACCESS_KEY,AppParserConstant.SERVICE_ACCESS_KEY_VAL);
				jsonObject.put(AppParserConstant.METHOD_NAME_KEY,"fetchallnotifications");
				jsonObject.put(AppParserConstant.USER_ACCESS_TOKEN_KEY,mAppPreferences.getUserAccessToken());
				jsonObject.put(AppParserConstant.USER_ID_KEY,mAppPreferences.getUserId());
				jsonObject.put("profile_status",mAppPreferences.getUserProfileStatus());
				jsonObject.put("per_page",mPerPage);
				jsonObject.put(AppParserConstant.PAGE_ID,mNextPageId);

				if(mNextPageId ==0)
				{
					jsonObject.put("view_all",1);
					requestBean.setLoader(true);
					mProgressBar.setVisibility(View.GONE);
				}
				else
				{
					requestBean.setLoader(false);
					mProgressBar.setVisibility(View.VISIBLE);
				}
				requestBean.setUrl(AppParserConstant.HIT_URL__NOTIFICATION);
				requestBean.setJsonObject(jsonObject);
				requestBean.setCallableObect(NotificationFragment.this);
				requestBean.setActivity(getActivity());
				NotificationAsynTask interestDetailAsynTask = new NotificationAsynTask(requestBean);

				if(VERSION.SDK_INT < VERSION_CODES.HONEYCOMB)
				{
					interestDetailAsynTask.execute();
				}else{
					interestDetailAsynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");	
				}
			}
			catch (Exception e) 
			{
				isHit = true;
				e.printStackTrace();
			}
		}
		else
		{
			isHit = true;
			Toast.makeText(getActivity(),getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();
		}
	}

	@SuppressWarnings("deprecation")
	private void initView(View view) 
	{
		mListView =  (ListView)view.findViewById(R.id.ll_notification);
		mArrayList = new ArrayList<NotificationBean>();
		mNotificationAdapter = new NotificationAdapter(getActivity(), NotificationFragment.this,mArrayList);
		
		
		mListView.setAdapter(mNotificationAdapter);
		//
		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright, 
				android.R.color.holo_green_light, 
				android.R.color.holo_orange_light, 
				android.R.color.holo_red_light);	
		
		mAppPreferences = AppPreferences.getInstance(getActivity());
		mAppTypeFace = new AppTypeFace(getActivity());
		mNoData =  (TextView)view.findViewById(R.id.tv_no_data);
		mProgressBar =  (ProgressBar)view.findViewById(R.id.progressbar);
		mListView.setOnItemClickListener(this);
	}

	public void setNotificationList(NotificationMainBean result) 
	{
		if(swipeLayout.isRefreshing())
		{
			swipeLayout.setRefreshing(false);
		}

		isHit = true;
	//	Toast.makeText(getActivity(), result.getResponseString(), Toast.LENGTH_SHORT).show();
		
		if(result!=null)
		{
			
			if(result.getStatus()==1)
			{
				
				if(mNextPageId==0)
				{
					mArrayList.clear();
				}
				mNotificationAdapter.set_total_records(result.getTotal_record());
				mNoData.setVisibility(View.GONE);
				mArrayList.addAll(result.getmArrayList());
				mNotificationAdapter.notifyDataSetChanged();
				mNextPageId++;

			}
			else 
			{
				mNoData.setVisibility(View.VISIBLE);
				mArrayList.clear();
			}

		}
		
	}
	@Override
	public void onRefresh() 
	{
		mNextPageId = 0;
		hit_service();
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) 
	{}
	public void sendIntent(int position)
	{
		mPosition = position;
		Intent intent = new Intent(getActivity(),InterestDetailActivity.class);
		intent.putExtra(GastonConstant.INTRST_ID_KEY,mArrayList.get(position).getInterest_id());
		intent.putExtra(GastonConstant.PROFILE_STATUS_KEY,mAppPreferences.getUserProfileStatus());
		intent.putExtra(GastonConstant.PSWD_PROTECTED_KEY,mArrayList.get(position).getPaasword());
		intent.putExtra(GastonConstant.USER_ID_KEY,mAppPreferences.getUserId());
		startActivityForResult(intent,1);
	}
}
