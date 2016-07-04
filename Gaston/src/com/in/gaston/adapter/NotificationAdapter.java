package com.in.gaston.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.in.gaston.DashBoardActivity;
import com.in.gaston.R;
import com.in.gaston.apptypeface.AppTypeFace;
import com.in.gaston.bean.NotificationBean;
import com.in.gaston.constant.AppParserConstant;
import com.in.gaston.constant.AppPreferences;
import com.in.gaston.fragment.NotificationFragment;
import com.in.gaston.imageloader.ImageLoader;
import com.in.gaston.view.RoundImageViewGray;

public class NotificationAdapter extends BaseAdapter 
{
	private Context context;
	private Fragment fragment;
	private ArrayList<NotificationBean> arrayList;
	private LayoutInflater inflater;
	private int total_records = 0;
	private AppTypeFace appTypeFace;
	private AppPreferences mAppPreferences;
	public NotificationAdapter(Context context,Fragment fragment,ArrayList<NotificationBean> arrayList)
	{
		this.context = context;
		mAppPreferences = AppPreferences.getInstance(context);
		this.fragment = fragment;
		this.arrayList = arrayList;
		inflater = LayoutInflater.from(context);
		appTypeFace = new AppTypeFace(context);
	}
	@Override
	public int getCount() 
	{
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		ViewHolder viewHolder;		
		if(convertView==null)
		{
			convertView = inflater.inflate(R.layout.row_notification, parent, false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		//set data
		viewHolder.commentSentTimTV.setText(arrayList.get(position).getCreated_on());
		//viewHolder.person_image .setImageDrawable(context.getDrawable(R.drawable.profileiconbig));

		String sender_name = "";
		if(mAppPreferences.getUserProfileStatus().equalsIgnoreCase("1"))
		{
			String userIMG = AppParserConstant.BASE_URL+arrayList.get(position).getSender_image();
			ImageLoader.getInstance((DashBoardActivity)context).displayImage(userIMG, viewHolder.person_image, false);
			sender_name = arrayList.get(position).getSender_first_name();
		}
		else
		{
			viewHolder.person_image.setImageResource(R.drawable.profileiconbig);
			sender_name = arrayList.get(position).getSender_pvt_name();
		}
		/*
           1-> subscribe interest , 
           2 interest message , 
           3 child _message , 
           4 -> like message  

		 */
		String notification_text = "";
		String comment_type = "";

		if(arrayList.get(position).getNotificatoin_type().equalsIgnoreCase("1"))
		{
			viewHolder.person_name.setText(sender_name+" has subscribed your interest "
					+"\""+arrayList.get(position).getInteresr_name()+"\"");
		}
		else if (arrayList.get(position).getNotificatoin_type().equalsIgnoreCase("2")) 
		{
			//2 interest message , 
			if(arrayList.get(position).getParent_comment_type().equalsIgnoreCase("1"))
			{
				comment_type = "text";
			}
			else if (arrayList.get(position).getParent_comment_type().equalsIgnoreCase("2")) 
			{
				comment_type = "image";

			}
			else if (arrayList.get(position).getParent_comment_type().equalsIgnoreCase("3")) 
			{
				comment_type = "audio";

			}
			if(mAppPreferences.getUserId().equalsIgnoreCase(arrayList.get(position).getInterest_user_id()))
			{
				//your
				viewHolder.person_name.setText(sender_name+" sends a "+comment_type+" message on your interest "+"\""+arrayList.get(position).getInteresr_name()+"\"");
			}
			else
			{
				viewHolder.person_name.setText(sender_name+" sends a "+comment_type+" message on your subscribed interest "+"\""+arrayList.get(position).getInteresr_name()+"\"");

			}
		}
		else if(arrayList.get(position).getNotificatoin_type().equalsIgnoreCase("3"))
		{
			//child _message ,
			if(arrayList.get(position).getParent_comment_type().equalsIgnoreCase("1"))
			{
				comment_type = "text";
			}
			else if (arrayList.get(position).getParent_comment_type().equalsIgnoreCase("2")) 
			{
				comment_type = "image";

			}
			else if (arrayList.get(position).getParent_comment_type().equalsIgnoreCase("3")) 
			{
				comment_type = "audio";

			}
			viewHolder.person_name.setText(sender_name+" comment on your "+comment_type+" message");

		}
		else if(arrayList.get(position).getNotificatoin_type().equalsIgnoreCase("4"))
		{
			//like message  ,
			if(arrayList.get(position).getParent_comment_type().equalsIgnoreCase("1"))
			{
				comment_type = "text";
			}
			else if (arrayList.get(position).getParent_comment_type().equalsIgnoreCase("2")) 
			{
				comment_type = "image";

			}
			else if (arrayList.get(position).getParent_comment_type().equalsIgnoreCase("3")) 
			{
				comment_type = "audio";

			}
			viewHolder.person_name.setText(sender_name+"like your "+comment_type+" message");

		}
		
		
		
		convertView.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View view) 
			{
				String profile ="";
				String notification_profile_status = arrayList.get(position).getNotification_profile_status();
				if(notification_profile_status.equalsIgnoreCase("1"))
				{
					profile = "Real Profile";
				}
				else
				{
					profile = "Anonymous Profile";

				}
				if(mAppPreferences.getUserProfileStatus().equalsIgnoreCase(notification_profile_status))
				{
					((NotificationFragment)fragment).sendIntent(position);
					
				}
				else
				{
					Toast.makeText(context,"Please select your "+profile+" to see the notification detail", Toast.LENGTH_SHORT).show();
				}

			}
		});

		//pagination
		if (position == (arrayList.size() - 1)&& position < (total_records - 1)) 
		{
			if(fragment instanceof NotificationFragment && fragment!=null)
			{
				((NotificationFragment)fragment).hit_service();
			}
		}
		return convertView;
	}

	class ViewHolder 
	{
		TextView person_name,commentSentTimTV;
		RoundImageViewGray person_image;
		public ViewHolder(View view)
		{
			person_name =  (TextView) view.findViewById(R.id.tv_person_name);
			person_image =  (RoundImageViewGray) view.findViewById(R.id.img_listing_data_image);
		//	person_name.setTypeface(appTypeFace.getTypeRoboto_Medium());
			commentSentTimTV = (TextView)view.findViewById(R.id.tv_comment_sent_timing);
			commentSentTimTV.setTypeface(appTypeFace.getTypeRoboto_Medium());

		}
	}


	public void set_total_records(int count)
	{
		this.total_records = count;
	}

}
