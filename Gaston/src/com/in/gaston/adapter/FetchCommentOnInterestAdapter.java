package com.in.gaston.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.in.gaston.DashBoardActivity;
import com.in.gaston.InterestDetailActivity;
import com.in.gaston.R;
import com.in.gaston.ShowOtherUserProfileActivity;
import com.in.gaston.apptypeface.AppTypeFace;
import com.in.gaston.asyntask.PlayMediaPlayer;
import com.in.gaston.bean.CommentBean;
import com.in.gaston.bean.InteresCommentBean;
import com.in.gaston.constant.AppParserConstant;
import com.in.gaston.constant.AppPreferences;
import com.in.gaston.fragment.HomeFragment;
import com.in.gaston.imageloader.ImageLoader;
import com.in.gaston.view.CustomTypefaceSpan;
import com.in.gaston.view.CustomView;
import com.in.gaston.view.RoundImageViewGray;

public class FetchCommentOnInterestAdapter extends BaseAdapter implements OnCompletionListener,OnErrorListener
{
	private ArrayList<InteresCommentBean> mArrayList;
	private Context mContext;
	private AppTypeFace mAppTypeFace;
	private AppPreferences mAppPreferences;
	private int mTotalRecord=0;
	private MediaPlayer mMediaPlayer;
	private boolean isMediaPlayerOn = true;
	private View mPlayingView ;
	private Fragment mFragment;
	private boolean isMute=false;
	private String commentType;
	private int progressStatus = 0;
	private Handler handler;
	private PlayMediaPlayer player = null ;
	/** Called when the activity is first created. */
	private int current = 0;
	private boolean   running = true;
	private	int duration = 0;
	private SeekBar mMediaPlayerSeekBar;
	private TextView soundLength; 


	public FetchCommentOnInterestAdapter(Context context,ArrayList<InteresCommentBean> interestDeatailBean)
	{
		mArrayList = interestDeatailBean;
		mContext = context;
		handler = new Handler();
		mAppTypeFace = new AppTypeFace(mContext);
		mAppPreferences = AppPreferences.getInstance(mContext);
	}

	public FetchCommentOnInterestAdapter(Fragment fragment,Context context,ArrayList<InteresCommentBean> interestDeatailBean)
	{
		mFragment = fragment;
		mArrayList = interestDeatailBean;
		handler = new Handler();
		mContext = context;
		mAppTypeFace = new AppTypeFace(mContext);
		mAppPreferences = AppPreferences.getInstance(mContext);
	}

	@Override
	public int getCount() 
	{
		return mArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return mArrayList.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		/*
		 * if comment_like_status = 0 means unliked
		 * 1 means liked
		 * 2 means nothing
		 */
		ViewHolder	viewHolder = null;

		if(convertView==null)
		{
			convertView = 	LayoutInflater.from(mContext).inflate(R.layout.row_comment_view_2, parent, false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder)convertView.getTag();

		}

		if(mFragment instanceof HomeFragment)
		{
			viewHolder.commenter_name.setTextSize(12);
			String commentTypeText = "" ;
			commentType =  mArrayList.get(position).getComment_type();
			if(commentType.equalsIgnoreCase("1"))
			{
				commentTypeText = "text";
			}
			else if(commentType.equalsIgnoreCase("2"))
			{
				commentTypeText = "picture";
			}
			else if(commentType.equalsIgnoreCase("3"))
			{
				commentTypeText = "audio";
			}

			if(mAppPreferences.getUserId().equalsIgnoreCase(mArrayList.get(position).getInterest_user_id()))
			{
				viewHolder.commenter_name.setText(mArrayList.get(position).getUser_first_name()+" posted a "+commentTypeText+" message on your"+" created interest "+"\""+mArrayList.get(position).getInterest_name()+"\"");
				String text = viewHolder.commenter_name.getText().toString();
				int indexOfInterestName =  text.indexOf(mArrayList.get(position).getInterest_name());
				Typeface font = mAppTypeFace.getTypeRobotoLight();
				SpannableString spannableString = new SpannableString(viewHolder.commenter_name.getText().toString());
				spannableString.setSpan((new CustomTypefaceSpan("", font)),mArrayList.get(position).getUser_first_name().length(),indexOfInterestName, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				viewHolder.commenter_name.setText(spannableString);
			}
			else
			{
				viewHolder.commenter_name.setText(mArrayList.get(position).getUser_first_name()+" posted a "+commentTypeText+" message on your"+" subscribed interest "+"\""+mArrayList.get(position).getInterest_name()+"\"");
				String text = viewHolder.commenter_name.getText().toString();
				int indexOfInterestName =  text.indexOf(mArrayList.get(position).getInterest_name());
				Typeface font = mAppTypeFace.getTypeRobotoLight();
				SpannableString spannableString = new SpannableString(viewHolder.commenter_name.getText().toString());
				spannableString.setSpan((new CustomTypefaceSpan("", font)),mArrayList.get(position).getUser_first_name().length(),indexOfInterestName, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				viewHolder.commenter_name.setText(spannableString);
			}
		}
		else
		{
			viewHolder.commenter_name.setTextSize(18);
			viewHolder.commenter_name.setText(mArrayList.get(position).getUser_first_name());
		}
		viewHolder.audioDurateionTV.setText("0:00");
		viewHolder.mMediaPlayerPB.setProgress(0);
		viewHolder.playSound.setImageResource(R.drawable.play);
		viewHolder.comment_date.setText(mArrayList.get(position).getComment_created_on());
		viewHolder.tv_like.setText(mArrayList.get(position).getTotal_like()); 
		viewHolder.tv_deslike.setText(mArrayList.get(position).getTptal_dislike());
		viewHolder.tv_comment.setText(mArrayList.get(position).getTotal_comment());

		if(Integer.parseInt(mArrayList.get(position).getComment_like_status())==0)
		{
			viewHolder.img_deslike.setImageResource(R.drawable.deslikeactive);
		}
		else
		{
			viewHolder.img_deslike.setImageResource(R.drawable.deslikenormal);
		}

		/*
		 * like bg
		 */
		if(Integer.parseInt(mArrayList.get(position).getComment_like_status())==1)
		{
			viewHolder.img_like.setImageResource(R.drawable.likeactive);
		}
		else
		{
			viewHolder.img_like.setImageResource(R.drawable.likenormal);
		}

		if(mFragment!=null&&mFragment instanceof HomeFragment)
		{
			ImageLoader.getInstance((DashBoardActivity)mContext).displayImage(AppParserConstant.BASE_URL+mArrayList.get(position).getUser_image(), viewHolder.commentImage,false);
		}
		else
		{
			ImageLoader.getInstance((InterestDetailActivity)mContext).displayImage(AppParserConstant.BASE_URL+mArrayList.get(position).getUser_image(), viewHolder.commentImage,false);
		}
		//1 = text
		//2 = pic
		//3 = sound
		if(mArrayList.get(position).getComment_type().equalsIgnoreCase("1"))
		{
			viewHolder.comment_data.setVisibility(View.VISIBLE);
			viewHolder.comment_data.setText(mArrayList.get(position).getComment_text());
			viewHolder.img_allow_pic.setVisibility(View.GONE);
			viewHolder.mMEdiaPlayerRL.setVisibility(View.GONE);
		}
		else if (mArrayList.get(position).getComment_type().equalsIgnoreCase("2")) 
		{
			viewHolder.comment_data.setVisibility(View.GONE);
			viewHolder.img_allow_pic.setVisibility(View.VISIBLE);
			viewHolder.mMEdiaPlayerRL.setVisibility(View.GONE);
			if(mFragment!=null&&mFragment instanceof HomeFragment)
			{
				ImageLoader.getInstance((DashBoardActivity)mContext).displayImage(AppParserConstant.BASE_URL+mArrayList.get(position).getComment_image(), viewHolder.img_allow_pic,false);
			}
			else
			{
				ImageLoader.getInstance((InterestDetailActivity)mContext).displayImage(AppParserConstant.BASE_URL+mArrayList.get(position).getComment_image(), viewHolder.img_allow_pic,false);
			}
		}
		else if (mArrayList.get(position).getComment_type().equalsIgnoreCase("3")) 
		{
			viewHolder.comment_data.setVisibility(View.GONE);
			viewHolder.img_allow_pic.setVisibility(View.GONE);
			viewHolder.mMEdiaPlayerRL.setVisibility(View.VISIBLE);
		}


		/*
		 * Media player 
		 */

		//viewHolder.playSound.setTag(viewHolder.mMediaPlayerRL);
		viewHolder.playSound.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(final View v) 
			{
				mPlayingView = (View) v.getParent();
				mMediaPlayerSeekBar = 	(SeekBar) mPlayingView.findViewById(R.id.img_filer);

				if(mMediaPlayer!=null)
				{
					if(mMediaPlayer.isPlaying())
					{
						((ImageView)v).setImageResource(R.drawable.play);
						mMediaPlayer.pause();
					}
					else
					{
						((ImageView)v).setImageResource(R.drawable.pause);
						mMediaPlayer.start();
						handler.postDelayed(onEverySecond, 1000);

					}
				}
				else
				{
					((ImageView)v).setImageResource(R.drawable.pause);

					if(mFragment!=null&&mFragment instanceof HomeFragment)
					{
						player = new PlayMediaPlayer(mFragment);

					}
					else
					{
						player = new PlayMediaPlayer(mContext);
					}
					player.execute(AppParserConstant.BASE_URL+mArrayList.get(position).getComment_audio());
				}
			}
		});
		/*
		 * 
		 */

		viewHolder.volumeIMG.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View view) 
			{
				if(!isMute)
				{
					((ImageView)view).setImageResource(R.drawable.soundoff);
					isMute = true;
					mMediaPlayer.setVolume(0.0f, 0.0f);

				}
				else
				{
					((ImageView)view).setImageResource(R.drawable.soundon);
					isMute = false;
					mMediaPlayer.setVolume(1.0f, 1.0f);


				}
			}
		});

		viewHolder.img_like.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// 1 for like
				if(Integer.parseInt(mArrayList.get(position).getComment_like_status())==1)
				{
					if(mFragment!=null&&mFragment instanceof HomeFragment)
					{
						//0
						((HomeFragment)mFragment).hitLikeUnlikeService(position,mArrayList.get(position).getInterest_id(),mArrayList.get(position).getComment_id(),1);
					}
					else
					{
						((InterestDetailActivity)mContext).hitLikeUnlikeService(position,mArrayList.get(position).getInterest_id(),mArrayList.get(position).getComment_id(),1);
					}
				}
				else
				{
					if(mFragment!=null&&mFragment instanceof HomeFragment)
					{
						((HomeFragment)mFragment).hitLikeUnlikeService(position,mArrayList.get(position).getInterest_id(),mArrayList.get(position).getComment_id(),1);
					}
					else
					{
						((InterestDetailActivity)mContext).hitLikeUnlikeService(position,mArrayList.get(position).getInterest_id(),mArrayList.get(position).getComment_id(),1);
					}
				}
			}
		});
		viewHolder.img_deslike.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// 0 for unlike
				if(Integer.parseInt(mArrayList.get(position).getComment_like_status())==0)
				{

					if(mFragment instanceof HomeFragment&& mFragment!=null)
					{
						((HomeFragment)mFragment).hitLikeUnlikeService(position,mArrayList.get(position).getInterest_id(),mArrayList.get(position).getComment_id(),0);
					}
					else
					{
						((InterestDetailActivity)mContext).hitLikeUnlikeService(position,mArrayList.get(position).getInterest_id(),mArrayList.get(position).getComment_id(),0);
					}
				}
				else
				{
					if(mFragment instanceof HomeFragment&& mFragment!=null)
					{
						((HomeFragment)mFragment).hitLikeUnlikeService(position,mArrayList.get(position).getInterest_id(),mArrayList.get(position).getComment_id(),0);
					}
					else
					{
						((InterestDetailActivity)mContext).hitLikeUnlikeService(position,mArrayList.get(position).getInterest_id(),mArrayList.get(position).getComment_id(),0);
					}
				}
			}
		});
		viewHolder.img_comment.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
			}
		});
		viewHolder.img_allow_pic.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(mFragment!=null&&mFragment instanceof HomeFragment)
				{
					CustomView.zoom_in_user_pic(mContext, AppParserConstant.BASE_URL+mArrayList.get(position).getComment_image());
				}
				else
				{
					CustomView.zoom_in_user_pic(mContext, AppParserConstant.BASE_URL+mArrayList.get(position).getComment_image());
				}
			}
		});
		/*
		 * pagination
		 */
		if (position == (mArrayList.size() - 1)&& position < (mTotalRecord - 1)) 
		{
			if(mFragment!=null&&mFragment instanceof HomeFragment)
			{
				((HomeFragment)mFragment).hitActivityFeedsService();
			}
			else
			{
				((InterestDetailActivity)mContext).hitservice();
			}
		}



		/*
		 * list of like , unlike , comments
		 */
		viewHolder.likeLL.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				int val =	Integer.parseInt(mArrayList.get(position).getTotal_like());
				if(val>0)
				{
					if(mFragment instanceof HomeFragment&& mFragment!=null)
					{
						((HomeFragment)mFragment).show_list_dialog(mArrayList.get(position).getComment_id(),1,"");
					}
					else
					{
						((InterestDetailActivity)mContext).show_list_dialog(mArrayList.get(position).getComment_id(),1);
					}
				}
				else
				{
					if(mFragment instanceof HomeFragment&& mFragment!=null)
					{
					//	Toast.makeText(mContext, "No likes in this comment", Toast.LENGTH_SHORT).show();

					}
					else
					{
						//Toast.makeText(mContext, "No likes in this comment", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		viewHolder.unLikeLL.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{

				int val =	Integer.parseInt(mArrayList.get(position).getTptal_dislike());
				if(val>0)
				{

					if(mFragment instanceof HomeFragment&& mFragment!=null)
					{
						((HomeFragment)mFragment).show_list_dialog(mArrayList.get(position).getComment_id(),2,"");
					}
					else
					{
						((InterestDetailActivity)mContext).show_list_dialog(mArrayList.get(position).getComment_id(),2);
					}
				}
				else
				{
					if(mFragment instanceof HomeFragment&& mFragment!=null)
					{
					//	Toast.makeText(mContext, "No unlikes in this comment", Toast.LENGTH_SHORT).show();

					}
					else
					{
						//Toast.makeText(mContext, "No unlikes in this comment", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

		viewHolder.commentLL.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{


				int val =	Integer.parseInt(mArrayList.get(position).getTotal_comment());

				if(mFragment instanceof HomeFragment&& mFragment!=null)
				{
					((HomeFragment)mFragment).show_list_dialog(mArrayList.get(position).getComment_id(),3,mArrayList.get(position).getInterest_id());
				}
				else
				{
					((InterestDetailActivity)mContext).show_list_dialog(mArrayList.get(position).getComment_id(),3);
				}
				/*else
				{
					if(mFragment instanceof HomeFragment&& mFragment!=null)
					{
						Toast.makeText(mContext, "No Comment", Toast.LENGTH_SHORT).show();
					}
					else
					{
						Toast.makeText(mContext, "No Comment", Toast.LENGTH_SHORT).show();
					}
				}*/
			}
		});


		viewHolder.commentImage.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(mContext,ShowOtherUserProfileActivity.class);
				intent.putExtra("user_id", mArrayList.get(position).getUser_id());
				intent.putExtra("user_name", mArrayList.get(position).getUser_first_name());
				intent.putExtra("user_gender", mArrayList.get(position).getUser_gender());
				mContext.startActivity(intent);

			}
		});

		return convertView;
	}

	class ViewHolder
	{
		private TextView commenter_name,comment_date,comment_data,tv_like,tv_deslike,tv_comment, audioDurateionTV,commentSentTimTV;
		private RoundImageViewGray commentImage;
		private ImageView img_like,img_deslike,img_comment,img_allow_pic,playSound,volumeIMG;
		private RelativeLayout mMEdiaPlayerRL,mMediaPlayerRL;
		private SeekBar mMediaPlayerPB;
		private LinearLayout likeLL,unLikeLL,commentLL;
		public ViewHolder(View view) 
		{
			commenter_name = (TextView) view.findViewById(R.id.tv_commenter_name);
			comment_date = (TextView) view.findViewById(R.id.tv_comment_time);
			comment_data = (TextView) view.findViewById(R.id.tv_comment_desc);
			tv_like = (TextView) view.findViewById(R.id.tv_like);
			tv_deslike = (TextView) view.findViewById(R.id.tv_deslike);
			tv_comment = (TextView) view.findViewById(R.id.tv_comment);
			img_like = (ImageView) view.findViewById(R.id.img_like);
			img_deslike = (ImageView) view.findViewById(R.id.img_deslike);
			img_comment = (ImageView) view.findViewById(R.id.img_comment);
			commentImage = 	(RoundImageViewGray) view.findViewById(R.id.img_comment_image);
			img_allow_pic =  (ImageView) view.findViewById(R.id.img_allow_pic);
			mMEdiaPlayerRL = (RelativeLayout) view.findViewById(R.id.rl_music_player);
			playSound = (ImageView) view.findViewById(R.id.img_play);
			volumeIMG = (ImageView) view.findViewById(R.id.img_volume);
			audioDurateionTV = (TextView) view.findViewById(R.id.tv_soung_length); 
			mMediaPlayerPB = (SeekBar) view.findViewById(R.id.img_filer);
			mMediaPlayerRL =  (RelativeLayout)view.findViewById(R.id.rl_music_player);
			likeLL = (LinearLayout)view.findViewById(R.id.ll_like);
			unLikeLL = (LinearLayout)view.findViewById(R.id.ll_unlike);
			commentLL = (LinearLayout)view.findViewById(R.id.ll_comment);
			/*
			 * set typeface
			 */
			commenter_name.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
			comment_date.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
			comment_data.setTypeface(mAppTypeFace.getTypeRobotoLight());
			tv_like.setTypeface(mAppTypeFace.getTypeRobotoLight());
			tv_deslike.setTypeface(mAppTypeFace.getTypeRobotoLight());
			audioDurateionTV.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
			tv_comment.setTypeface(mAppTypeFace.getTypeRobotoLight());
		}
	}

	public void setTotalRecord(int total_record)
	{
		this.mTotalRecord = total_record;
	}

	public void mediaPlayer(MediaPlayer result)
	{
		//Toast.makeText(mContext,"Inside --> void mediaPlayer()",Toast.LENGTH_SHORT).show();
		mMediaPlayer = result;
		/*
		 * set sound length
		 */
		soundLength =	(TextView) mPlayingView.findViewById(R.id.tv_soung_length);
		soundLength.setText(getTimeString(mMediaPlayer.getDuration()));		


		mMediaPlayerSeekBar.setProgress(0);
		mMediaPlayerSeekBar.setMax(mMediaPlayer.getDuration());

		handler.postDelayed(onEverySecond, 1000);
		mMediaPlayer.setOnCompletionListener(this);
		mMediaPlayer.setOnErrorListener(this);
	}


	public void updateItem(int position,CommentBean commentBean)
	{
		mArrayList.get(position).setTotal_like(commentBean.getTotal_like());
		mArrayList.get(position).setTptal_dislike(commentBean.getTotal_unlike());
	}
	public static float pxFromDp(float dp, Context mContext) 
	{
		return dp * mContext.getResources().getDisplayMetrics().density;
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) 
	{
		//Toast.makeText(mContext,"onError   ------> Media Player ------->"+what,Toast.LENGTH_SHORT).show();
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) 
	{
		//Toast.makeText(mContext,"onCompletion   ------> Media Player ------->"+mp,Toast.LENGTH_SHORT).show();

		ImageView play = (ImageView)mPlayingView.findViewById(R.id.img_play);
		play.setImageResource(R.drawable.play);
		soundLength.setText(getTimeString(mMediaPlayer.getDuration()));
		mMediaPlayerSeekBar.setProgress(mMediaPlayer.getDuration());
		mMediaPlayer.release();
		mMediaPlayer = null;

	}
	private Runnable onEverySecond = new Runnable() 
	{

		@Override
		public void run()
		{
			ImageView playSound = null;
			playSound = (ImageView) mPlayingView.findViewById(R.id.img_play);

			if(mMediaPlayer!=null)
			{
				if(mMediaPlayer.getCurrentPosition()<mMediaPlayer.getDuration()) 
				{
					soundLength.setText(getTimeString(mMediaPlayer.getCurrentPosition()));
					//Toast.makeText(mContext,"current_pos != duration",Toast.LENGTH_SHORT).show();
					mMediaPlayerSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
				}

				if(mMediaPlayer.isPlaying()) 
				{

					playSound.setImageResource(R.drawable.pause);
					handler.postDelayed(onEverySecond, 1000);
				}
				else
				{
					playSound.setImageResource(R.drawable.play);

				}
			}
			else
			{
				playSound.setImageResource(R.drawable.play);
				//Toast.makeText(mContext,"curent_pos == dutation",Toast.LENGTH_SHORT).show();
			}
		}
	};

	/*
	 * sound length in mm:ss form a
	 */

	public static String getTimeString(long duration) 
	{
		int minutes = (int) Math.floor(duration / 1000 / 60);
		int seconds = (int) ((duration / 1000) - (minutes * 60));
		return minutes + ":" + String.format("%02d", seconds);
	}
}
