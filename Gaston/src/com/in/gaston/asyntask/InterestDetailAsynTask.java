package com.in.gaston.asyntask;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.in.gaston.InterestDetailActivity;
import com.in.gaston.R;
import com.in.gaston.ShowOtherUserProfileActivity;
import com.in.gaston.appparser.AppParser;
import com.in.gaston.bean.InterestDeatailBean;
import com.in.gaston.bean.RequestBean;
import com.in.gaston.fragment.HomeFragment;
import com.in.gaston.fragment.ProfileFragment;
import com.in.gaston.network.NetworkConnection;

/**
 * asynchronous class service being executed in this class: SIGN_IN_SERVICE
 * 
 * @author anshika_kala
 * 
 */

public class InterestDetailAsynTask extends AsyncTask<String, Object, Object>
{

	private RequestBean mRequestBean;
	private AppParser mAppParser;
	private ProgressDialog mProgressDialog;
	private NetworkConnection mNetworkConnection;

	/**
	 * @constructor
	 * @param requestBean
	 */

	public InterestDetailAsynTask(RequestBean requestBean)
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
		String response = mNetworkConnection.networkHit(mRequestBean.getJsonObject(), mRequestBean.getUrl());
		InterestDeatailBean interestDeatailBean=  mAppParser.parseInteresDetailData(response);
		return interestDeatailBean;
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

		if(mRequestBean.getActivity() instanceof InterestDetailActivity)
		{
			((InterestDetailActivity)mRequestBean.getActivity()).setInterestDetailResponse((InterestDeatailBean)result);
		}
		else if (mRequestBean.getCallableObect() instanceof HomeFragment) 
		{
			((HomeFragment)mRequestBean.getCallableObect()).setActvityFeedsResponse((InterestDeatailBean)result);

		}
		else if (mRequestBean.getCallableObect() instanceof ProfileFragment)
		{
			((ProfileFragment)mRequestBean.getCallableObect()).setChkPswdResponse((InterestDeatailBean)result);

		}
		else if (mRequestBean.getActivity() instanceof ShowOtherUserProfileActivity)
		{
			((ShowOtherUserProfileActivity)mRequestBean.getActivity()).setChkPswdResponse((InterestDeatailBean)result);

		}

	}

}