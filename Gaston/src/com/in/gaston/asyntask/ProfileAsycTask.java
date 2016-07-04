package com.in.gaston.asyntask;



import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.in.gaston.R;
import com.in.gaston.ShowOtherUserProfileActivity;
import com.in.gaston.appparser.AppParser;
import com.in.gaston.bean.ProfileDataBean;
import com.in.gaston.bean.RequestBean;
import com.in.gaston.fragment.ProfileFragment;
import com.in.gaston.network.NetworkConnection;

/**
 * asynchronous class service being executed in this class: SIGN_IN_SERVICE
 * 
 * @author anshika_kala
 * 
 */

public class ProfileAsycTask extends AsyncTask<String, Object, Object>
{

	private RequestBean mRequestBean;
	private AppParser mAppParser;
	private ProgressDialog mProgressDialog;
	private NetworkConnection mNetworkConnection;

	/**
	 * @constructor
	 * @param requestBean
	 */

	public ProfileAsycTask(RequestBean requestBean)
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
		else
		{
			mProgressDialog.dismiss();
		}
	}

	@Override
	protected Object doInBackground(String... params)
	{
		String response = mNetworkConnection.networkHit(mRequestBean.getJsonObject(), mRequestBean.getUrl());
		ProfileDataBean profileDataBean=  mAppParser.parseProfileData(response);
		return profileDataBean;
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

		if(mRequestBean.getCallableObect() instanceof ProfileFragment)
		{
			((ProfileFragment)mRequestBean.getCallableObect()).setProfileResponse((ProfileDataBean)result);
		}
		else if (mRequestBean.getActivity() instanceof ShowOtherUserProfileActivity) 
		{
			((ShowOtherUserProfileActivity)mRequestBean.getActivity()).setProfileResponse((ProfileDataBean)result);

		}

	}

}