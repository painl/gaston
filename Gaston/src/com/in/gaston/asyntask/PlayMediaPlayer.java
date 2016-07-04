package com.in.gaston.asyntask;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.in.gaston.DashBoardActivity;
import com.in.gaston.InterestDetailActivity;
import com.in.gaston.R;
import com.in.gaston.constant.AppPreferences;
import com.in.gaston.fragment.HomeFragment;

public class PlayMediaPlayer extends AsyncTask<String, Void, MediaPlayer>   implements OnPreparedListener
{

	private Context mContext;
	private Fragment mFragment;
	private AppPreferences mAppPreferences;
	MediaPlayer mediaPlayer;
	private ProgressDialog mProgressDialog;
	public PlayMediaPlayer(Context context)
	{
		mContext = context;
		mProgressDialog = new ProgressDialog(mContext, R.style.MyTheme);
		mAppPreferences = AppPreferences.getInstance(mContext);
	}
	public PlayMediaPlayer(Fragment fragment) 
	{
		this.mFragment = fragment;
		mContext =  fragment.getActivity();
		mProgressDialog = new ProgressDialog(mContext, R.style.MyTheme);
		mAppPreferences = AppPreferences.getInstance(mContext);

	}

	@Override
	protected void onPreExecute() 
	{
		super.onPreExecute();
		if(mProgressDialog!=null)
			mProgressDialog.show();
	}
	@Override
	protected MediaPlayer doInBackground(String... params) 
	{
		String url = params[0];
		mediaPlayer = new MediaPlayer();

		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try
		{
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepare(); // might take long! (for buffering, etc)//call back to onPrepared() when done buffering
			mAppPreferences.setAudDuration(mediaPlayer.getDuration());
		}catch(Exception e)
		{

		}
		return mediaPlayer;
	}
	@Override
	protected void onPostExecute(MediaPlayer result) 
	{
		super.onPostExecute(result);

		if(mContext instanceof InterestDetailActivity)
		{

			//Toast.makeText(mContext,"onPostExecute-->Asun Task",Toast.LENGTH_SHORT).show();

			((InterestDetailActivity)mContext).setMediControl(result);
		}
		else if (mContext instanceof DashBoardActivity) 
		{
			((HomeFragment)mFragment).setMediaControl(result);

		}
	}
	@Override
	public void onPrepared(MediaPlayer mp) 
	{
		//	Toast.makeText(mContext,"onPrepared-->Asun Task",Toast.LENGTH_SHORT).show();

		if(mProgressDialog!=null&&mProgressDialog.isShowing())
		{
			mProgressDialog.dismiss();
		}
		mp.start();
	}

}
