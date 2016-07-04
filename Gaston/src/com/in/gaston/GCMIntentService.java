package com.in.gaston;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;
import com.in.gaston.constant.AppPreferences;
import com.in.gaston.constant.GastonConstant;
import com.in.gaston.network.NetworkStatus;

/**
 * This class is used to receive push notification and notify the user.
 * 
 * 
 */
public class GCMIntentService extends GCMBaseIntentService 
{
	AppPreferences mAppPrefrences;
	String response;
	String mAlert;
	String mPushType;
	boolean foreground;
	int mPushId;
	private String user_profile_status;
	private String notifications_type;
	private String notification_id;
	private String interest_id;
	private String count;
	private String password ="";

	private static final String TAG = "GCMIntentService";

	public GCMIntentService() {
		super(GastonConstant.PROJECT_ID);
	}
	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) 
	{
		//mAppPrefrences.setDeviceToken(registrationId);
	}
	/**
	 * Method called on device un registred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) 
	{

	}

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) 
	{
		// Log.e("","************************ onMessageReceive *****************************");
		mAppPrefrences = AppPreferences.getInstance(context);

		Bundle bundle = intent.getExtras();
		if(bundle!=null)
		{
			String payload = bundle.getString("payload");
			try {
				JSONObject jsonObject = new JSONObject(payload);
				JSONObject dataJO =  jsonObject.getJSONObject("data");
				user_profile_status = dataJO.getString("profile_status");
				mPushId = dataJO.getInt("notifications_type");
				notification_id = dataJO.getString("notification_id");
				interest_id = dataJO.getString("interest_id");
				
				if(dataJO.has("password"))
				{
					password = dataJO.getString("password");

				}

				/////////
				mAlert = jsonObject.getString("alert");
				count = jsonObject.getString("count");



			} catch (JSONException e) {
				e.printStackTrace();
			}


		}
		GCMIntentService intentService = new GCMIntentService();
		try {
			foreground = intentService.new ForegroundCheckTask().execute(
					context).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}


		/*
		mInterestIDString =  intent.getStringExtra(GastonConstant.INTRST_ID_KEY);
		mProfileStatusString =  intent.getStringExtra(GastonConstant.PROFILE_STATUS_KEY);
		mPasswordSTring =  intent.getStringExtra(GastonConstant.PSWD_PROTECTED_KEY);
		userIdString = intent.getStringExtra(GastonConstant.USER_ID_KEY);
		userSubInterest = intent.getIntExtra(GastonConstant.SUBSCRIBE_STATUS_KEY,0);
		 */
		Intent open_activity_intent = new Intent(context,InterestDetailActivity.class);
		open_activity_intent.putExtra(GastonConstant.INTRST_ID_KEY,interest_id);
		open_activity_intent.putExtra(GastonConstant.PROFILE_STATUS_KEY,user_profile_status);
		open_activity_intent.putExtra(GastonConstant.PSWD_PROTECTED_KEY,"");
		open_activity_intent.putExtra(GastonConstant.USER_ID_KEY,mAppPrefrences.getUserId());
		open_activity_intent.getIntExtra(GastonConstant.SUBSCRIBE_STATUS_KEY,0);
		open_activity_intent.putExtra(GastonConstant.PSWD_PROTECTED_KEY,password);

		open_activity_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		String title = context.getString(R.string.app_name);
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		/**
		 * set intent action when app in foreground
		 */
		PendingIntent pending_intent = PendingIntent.getActivity(context, 0,open_activity_intent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setDefaults(Notification.DEFAULT_SOUND);

		mBuilder.setContentTitle(title).setContentText(mAlert).setSmallIcon(R.drawable.splash).setContentIntent(pending_intent)
		.setAutoCancel(true);

		/**
		 * To Notify the push message only if the Application is in background
		 */

		if (NetworkStatus.isInternetOn(context)) 
		{
			notificationManager.notify(mPushId, mBuilder.build());// here
			// pushTpye
			// is used
			// as a
			// notification
			// id
		} else {
			Toast.makeText(context, "No internet Connection",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Method called on receiving a deleted message
	 * 
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 * 
	 */
	@SuppressLint("NewApi")
	class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Context... params) {
			final Context context = params[0].getApplicationContext();
			return isAppOnForeground(context);
		}

		private boolean isAppOnForeground(Context context) {
			ActivityManager activityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> appProcesses = activityManager
					.getRunningAppProcesses();
			if (appProcesses == null) {
				return false;
			}
			final String packageName = context.getPackageName();
			for (RunningAppProcessInfo appProcess : appProcesses) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
						&& appProcess.processName.equals(packageName)) {
					return true;
				}
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
		}
	}

	public static void CancelNotification(Context ctx, int notifyId) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager nMgr = (NotificationManager) ctx
				.getSystemService(ns);
		nMgr.cancel(notifyId);
	}
}