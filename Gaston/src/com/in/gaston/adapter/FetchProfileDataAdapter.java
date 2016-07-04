package com.in.gaston.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.in.gaston.DashBoardActivity;
import com.in.gaston.R;
import com.in.gaston.ShowOtherUserProfileActivity;
import com.in.gaston.apptypeface.AppTypeFace;
import com.in.gaston.bean.InterestBean;
import com.in.gaston.constant.AppParserConstant;
import com.in.gaston.fragment.ProfileFragment;
import com.in.gaston.imageloader.ImageLoader;
import com.in.gaston.view.RoundImageViewGray;

public class FetchProfileDataAdapter extends BaseAdapter 
{
	private Context mContext;
	private ArrayList<InterestBean> mArrayList;
	private LayoutInflater mLayoutInflater;
	private ViewHolder viewHolder;
	private AppTypeFace mAppTypeFace;
	private int mTotalRecord ,mType = 1;
	private ProfileFragment mFragment;
	public FetchProfileDataAdapter(Fragment fragment, Context context,ArrayList<InterestBean> arrList,int total_record)
	{
		mArrayList = arrList;
		mContext = context;
		mFragment = (ProfileFragment)fragment;
		//mActivity = (DashBoardActivity)context;
		mLayoutInflater = LayoutInflater.from(mContext);
		mAppTypeFace = new AppTypeFace(mContext); 
		mTotalRecord = total_record;
	}
	public FetchProfileDataAdapter(Context context,ArrayList<InterestBean> arrList,int total_record)
	{
		mArrayList = arrList;
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
		mAppTypeFace = new AppTypeFace(context); 
		mTotalRecord = total_record;
	}

	@Override
	public int getCount() 
	{
		return mArrayList.size();
	}

	@Override
	public InterestBean getItem(int position) {
		return mArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{

		if(convertView==null)
		{
			
			if(mType!=0)
			{
			convertView = mLayoutInflater.inflate(R.layout.row_profile,parent,false);
			}
			else if (mType==0) 
			{
				convertView = mLayoutInflater.inflate(R.layout.activity_other_user,parent,false);
			}
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder =  (ViewHolder) convertView.getTag();
		}

		if(mType==1)
		{
			viewHolder.deleteIMG.setVisibility(View.GONE);
			viewHolder.userName.setVisibility(View.VISIBLE);
			viewHolder.userIMG.setVisibility(View.VISIBLE);
			viewHolder.totalSub.setVisibility(View.VISIBLE);
			viewHolder.totalSub.setVisibility(View.VISIBLE);
			viewHolder.subscribeBTN.setVisibility(View.VISIBLE);
			viewHolder.intrstDescTV.setVisibility(View.GONE);
		}
		else if (mType==2) 
		{

			viewHolder.deleteIMG.setVisibility(View.VISIBLE);
			viewHolder.userName.setVisibility(View.GONE);
			viewHolder.userIMG.setVisibility(View.GONE);
			viewHolder.totalSub.setVisibility(View.GONE);
			viewHolder.totalSub.setVisibility(View.GONE);
			viewHolder.subscribeBTN.setVisibility(View.GONE);
			viewHolder.intrstDescTV.setVisibility(View.VISIBLE);

		}
		else if (mType==0) 
		{


			viewHolder.deleteIMG.setVisibility(View.GONE);
			viewHolder.userName.setVisibility(View.GONE);
			viewHolder.userIMG.setVisibility(View.GONE);
			viewHolder.totalSub.setVisibility(View.GONE);
			viewHolder.subscribeBTN.setVisibility(View.VISIBLE);
			viewHolder.intrstDescTV.setVisibility(View.VISIBLE);


		}

		if(mType!=0)
		{
			setPassIconVisibility(position);
			setPassIconV(position);
		}
		else
		{
			setPassIconVisibility(position);

		}
		setAllowViewVisibility(position);

		viewHolder.intrstDescTV.setText(mArrayList.get(position).getInterest_description());


		String intrstIMG = AppParserConstant.BASE_URL+mArrayList.get(position).getInterest_image();

		if(mArrayList.get(position).getUser_profile_status().equalsIgnoreCase("2"))
		{
			viewHolder.userIMG.setVisibility(View.GONE);
			viewHolder.userName.setVisibility(View.GONE);
			viewHolder.maskIMg.setVisibility(View.VISIBLE);

		}
		else if(mArrayList.get(position).getUser_profile_status().equalsIgnoreCase("1"))
		{

			if(mType==2)
			{
				viewHolder.userIMG.setVisibility(View.GONE);
				viewHolder.userName.setVisibility(View.GONE);
			}
			else if (mType==1) 
			{
				viewHolder.userIMG.setVisibility(View.VISIBLE);
				viewHolder.userName.setVisibility(View.VISIBLE);
				viewHolder.userName.setText(mArrayList.get(position).getUser_fname());
				viewHolder.userIMG.setImageDrawable(((DashBoardActivity)mContext).getResources().getDrawable(R.drawable.profileiconsmall));
			}
			else
			{
				viewHolder.userIMG.setVisibility(View.GONE);
				viewHolder.userName.setVisibility(View.GONE);
				viewHolder.userName.setText(mArrayList.get(position).getUser_fname());
				
				if(mType!=0)
				{
				
				viewHolder.userIMG.setImageDrawable(((DashBoardActivity)mContext).getResources().getDrawable(R.drawable.profileiconsmall));
				}
				else if(mType==1)
				{
					viewHolder.userIMG.setImageDrawable(((ShowOtherUserProfileActivity)mContext).getResources().getDrawable(R.drawable.profileiconsmall));
				}
			}
			viewHolder.maskIMg.setVisibility(View.GONE);
			if(mArrayList.get(position).getUser_image().equalsIgnoreCase(""))
			{
				if(mType==1)
				{
					viewHolder.userIMG.setImageDrawable(((DashBoardActivity)mContext).getResources().getDrawable(R.drawable.profileiconsmall));
				}
				else
				{
					viewHolder.userIMG.setVisibility(View.GONE);

				}
			}
			else
			{
				if(mType!=0)
				{
					String userIMG = AppParserConstant.BASE_URL+mArrayList.get(position).getUser_image();

					ImageLoader.getInstance((DashBoardActivity)mContext).displayImage(userIMG, viewHolder.userIMG,false);
				}
			}
		}

		if(mArrayList.get(position).getInterest_image().equalsIgnoreCase(""))
		{
			if(mType!=0)
			{
			viewHolder.intrstIMG.setImageDrawable(((DashBoardActivity)mContext).getResources().getDrawable(R.drawable.profileiconbig));
			}
			else
			{
				viewHolder.intrstIMG.setImageDrawable(((ShowOtherUserProfileActivity)mContext).getResources().getDrawable(R.drawable.profileiconbig));

			}

		}
		else
		{
			
			if(mType!=0)
			{
			ImageLoader.getInstance(((DashBoardActivity)mContext)).displayImage(intrstIMG, viewHolder.intrstIMG,false);
			}
			else
			{
				ImageLoader.getInstance(((ShowOtherUserProfileActivity)mContext)).displayImage(intrstIMG, viewHolder.intrstIMG,false);

			}

		}
		viewHolder.intrstName.setText(mArrayList.get(position).getInterest_name());
		viewHolder.createdOnDate.setText("Created at "+mArrayList.get(position).getCreated_on());
		viewHolder.totalSub.setText(mArrayList.get(position).getTotal_subscribed());


		if (position == (mArrayList.size() - 1)&& position < (mTotalRecord - 1)) 
		{
			//Toast.makeText(mContext,"Pagination_Done"+"Array list size :-"+""+mArrayList.size()+"position=>"+position+1+"",Toast.LENGTH_SHORT).show();

			if(mFragment!=null)
			{

				mFragment.hitService();
			}
			else
			{
				((ShowOtherUserProfileActivity)mContext).hitService();
			}
		}
		else
		{
			//Toast.makeText(mContext,"Pagination",Toast.LENGTH_SHORT).show();
		}


		viewHolder.subscribeBTN.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				//	Toast.makeText(mActivity,"Under Development",Toast.LENGTH_SHORT).show();

			}
		});


		convertView.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				if(mType!=0)
				{					

					if(mArrayList.get(position).getPass_protected().equalsIgnoreCase("1"))
					{
						mFragment.openPasswordDialog(position);
					}
					else
					{
						mFragment.sendIntent(position);

					}
				}
				
				else
				{
					if(mArrayList.get(position).getPass_protected().equalsIgnoreCase("1"))
					{
						((ShowOtherUserProfileActivity)mContext).openPasswordDialog(position);
					}
					else
					{
						((ShowOtherUserProfileActivity)mContext).sendIntent(position);

					}
				}
			}
		});
		
		
		
		viewHolder.deleteIMG.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View view) 
			{
				if(mFragment!=null&&mFragment instanceof ProfileFragment)
				{
					mFragment.hit_Delete(mArrayList.get(position).getInterest_id());
				}
			}
		});

		return convertView;
	}



	private void setPassIconV(int position) 
	{
		/*
		 * 1 = pasword protected
		 * 2= no pswd
		 */
		if(mArrayList.get(position).getPass_protected().equalsIgnoreCase("1"))
		{
			viewHolder.passProtectedIMG.setVisibility(View.VISIBLE);
		}
		else
		{
			viewHolder.passProtectedIMG.setVisibility(View.GONE);

		}
	}
	public void setPassIconVisibility(int position) 
	{
		if(mArrayList.get(position).getPass_protected().equalsIgnoreCase("1"))
		{
			// 1 means it has password and password proteced
			viewHolder.passProtectedIMG.setVisibility(View.VISIBLE);

		}
		else 
		{
			viewHolder.passProtectedIMG.setVisibility(View.GONE);

		}
		
		
		if(mArrayList.get(position).getUser_subscribe_status()==0)
		{
			// 0 unsub

			viewHolder.subscribeBTN.setText("Subscribe");
		}
		else
		{
			viewHolder.subscribeBTN.setText("Unsubscribe");

		}
	}


	private void setAllowViewVisibility(int position)
	{
		if(mArrayList.get(position).getAllow_text().equalsIgnoreCase("1"))
		{
			//1 = allow ,0 = not allow
			viewHolder.textIMG.setVisibility(View.VISIBLE);
		}
		else
		{
			viewHolder.textIMG.setVisibility(View.GONE);

		}
		if(mArrayList.get(position).getAllow_image().equalsIgnoreCase("1"))
		{
			//1 = allow ,0 = not allow
			viewHolder.pictureIMG.setVisibility(View.VISIBLE);
		}
		else
		{
			viewHolder.pictureIMG.setVisibility(View.GONE);

		}
		if(mArrayList.get(position).getAllow_audio().equalsIgnoreCase("1"))
		{
			//1 = allow ,0 = not allow
			viewHolder.audioIMG.setVisibility(View.VISIBLE);
		}
		else
		{
			viewHolder.audioIMG.setVisibility(View.GONE);
		}
	}



	class ViewHolder
	{
		private TextView intrstName,userName,totalSub,createdOnDate,intrstDescTV;
		private RoundImageViewGray intrstIMG,userIMG;
		private ImageView textIMG,audioIMG,pictureIMG,passProtectedIMG,maskIMg,deleteIMG;
		private Button subscribeBTN;

		public ViewHolder(View view) 
		{
			intrstName = (TextView) view.findViewById(R.id.tv_interest_name);
			createdOnDate = (TextView) view.findViewById(R.id.tv_interest_created_date_time);
			userName = (TextView) view.findViewById(R.id.tv_user_name);
			totalSub = (TextView) view.findViewById(R.id.tv_subscribe_count);
			intrstIMG = (RoundImageViewGray) view.findViewById(R.id.img_interest_view);
			userIMG = (RoundImageViewGray) view.findViewById(R.id.img_user);
			subscribeBTN = (Button) view.findViewById(R.id.btn_subscribe);
			textIMG = (ImageView) view.findViewById(R.id.img_text);
			pictureIMG = (ImageView) view.findViewById(R.id.img_pic);
			audioIMG = (ImageView) view.findViewById(R.id.img_aud);
			maskIMg = (ImageView) view.findViewById(R.id.img_mask_fake);
			deleteIMG = (ImageView) view.findViewById(R.id.img_delete);
			intrstDescTV = 	(TextView) view.findViewById(R.id.tv_desc);


			passProtectedIMG = (ImageView) view.findViewById(R.id.img_pswd_protected);

			intrstName.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
			subscribeBTN.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
			totalSub.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
			createdOnDate.setTypeface(mAppTypeFace.getTypeRobotoLight());
			userName.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
			intrstDescTV.setTypeface(mAppTypeFace.getTypeRobotoLight());


		}

	}


	public void setTotalRecord(int total_record, int type)
	{
		this.mTotalRecord = total_record;
		this.mType = type;
	}



}
