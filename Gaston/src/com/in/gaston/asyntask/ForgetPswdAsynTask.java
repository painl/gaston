package com.in.gaston.asyntask;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.in.gaston.ChangePasswordActivity;
import com.in.gaston.ForgotPswdActivity;
import com.in.gaston.InterestDetailActivity;
import com.in.gaston.PassCodeActivity;
import com.in.gaston.R;
import com.in.gaston.appparser.AppParser;
import com.in.gaston.bean.CommonBean;
import com.in.gaston.bean.RequestBean;
import com.in.gaston.fragment.ProfileFragment;
import com.in.gaston.fragment.SettingFragment;
import com.in.gaston.network.NetworkConnection;

/**
 * asynchronous class service being executed in this class: SIGN_IN_SERVICE
 * 
 * @author anshika_kala
 * 
 */

public class ForgetPswdAsynTask extends AsyncTask<String, Object, Object>
{

	private RequestBean mRequestBean;
	private ProgressDialog mProgressDialog;
	private NetworkConnection mNetworkConnection;
	private AppParser mAppParser;

	/**
	 * @constructor
	 * @param requestBean
	 */

	public ForgetPswdAsynTask(RequestBean requestBean)
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
	
	CommonBean commonBean =  mAppParser.parseUserHitNetwork(response);
	
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

		if(mRequestBean.getActivity() instanceof ForgotPswdActivity)
		{
			((ForgotPswdActivity)mRequestBean.getActivity()).setForgotPswdResponse((CommonBean)result);
		}
		else if (mRequestBean.getActivity() instanceof PassCodeActivity) 
		{
			((PassCodeActivity)mRequestBean.getActivity()).setResetPswdResponse((CommonBean)result);

		}
		else if (mRequestBean.getCallableObect() instanceof ProfileFragment) 
		{
			((ProfileFragment)mRequestBean.getCallableObect()).setDeleteInterestResponse((CommonBean)result);

		}
		else if (mRequestBean.getActivity() instanceof InterestDetailActivity) 
		{
			((InterestDetailActivity)mRequestBean.getActivity()).setReportInterestResponse((CommonBean)result);
		}
		else if (mRequestBean.getActivity() instanceof ChangePasswordActivity) 
		{
			((ChangePasswordActivity)mRequestBean.getActivity()).setChangePasswordActivityResponse((CommonBean)result);
		}
		else if (mRequestBean.getCallableObect() instanceof SettingFragment) 
		{
			((SettingFragment)mRequestBean.getCallableObect()).setNotificationOnOffResponse((CommonBean)result);
		}
	}

}