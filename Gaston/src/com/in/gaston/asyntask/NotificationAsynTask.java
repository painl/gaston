package com.in.gaston.asyntask;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.in.gaston.R;
import com.in.gaston.appparser.AppParser;
import com.in.gaston.bean.NotificationMainBean;
import com.in.gaston.bean.RequestBean;
import com.in.gaston.fragment.NotificationFragment;
import com.in.gaston.network.NetworkConnection;

/**
 * asynchronous class service being executed in this class: SIGN_IN_SERVICE
 * 
 * @author anshika_kala
 * 
 */

public class NotificationAsynTask extends AsyncTask<String, Object, Object>
{

	private RequestBean mRequestBean;
	private ProgressDialog mProgressDialog;
	private NetworkConnection mNetworkConnection;
	private AppParser mAppParser;

	/**
	 * @constructor
	 * @param requestBean
	 */

	public NotificationAsynTask(RequestBean requestBean)
	{
		this.mRequestBean = requestBean;
		this.mNetworkConnection = new NetworkConnection();
		this.mAppParser = new AppParser(mRequestBean.getActivity());
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		if (mRequestBean.isLoader())
		{
			mProgressDialog = new ProgressDialog(mRequestBean.getActivity(),R.style.MyTheme);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
		}
	}

	@Override
	protected Object doInBackground(String... params)
	{
		String response = mNetworkConnection.networkHit(mRequestBean.getJsonObject(),mRequestBean.getUrl());

		NotificationMainBean commonBean =  mAppParser.parseNotificationData(response);

		return commonBean;
	}

	@Override
	protected void onPostExecute(Object result)
	{
		super.onPostExecute(result);
		try
		{
			if (mProgressDialog != null && mProgressDialog.isShowing()) 
				mProgressDialog.dismiss();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		if(mRequestBean.getCallableObect() instanceof NotificationFragment)
		{
		((NotificationFragment)mRequestBean.getCallableObect()).setNotificationList((NotificationMainBean)result);

		}
	}
}