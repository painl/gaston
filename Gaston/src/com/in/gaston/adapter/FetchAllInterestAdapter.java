package com.in.gaston.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.in.gaston.constant.AppPreferences;
import com.in.gaston.fragment.HomeFragment;
import com.in.gaston.imageloader.ImageLoader;
import com.in.gaston.view.RoundImageViewGray;

public class FetchAllInterestAdapter extends BaseAdapter 
{
	private Context mContext;
	private ArrayList<InterestBean> mArrayList;
	private Activity mActivity;
	private LayoutInflater mLayoutInflater;
	private ViewHolder viewHolder;
	private AppTypeFace mAppTypeFace;
	private int mTotalRecord ;
	private HomeFragment mHomeFragment;
	private AppPreferences mAppPreferences;
	private int filter_val = 0;;
	public FetchAllInterestAdapter(HomeFragment homeFragment, Context context,ArrayList<InterestBean> arrList,int total_record)
	{
		mArrayList = arrList;
		mContext = context;
		mHomeFragment = homeFragment;
		mActivity = (DashBoardActivity)context;
		mLayoutInflater = LayoutInflater.from(mContext);
		mAppTypeFace = new AppTypeFace(mActivity); 
		mTotalRecord = total_record;
		mAppPreferences= AppPreferences.getInstance(mContext);
	}
	@Override
	public int getCount() {
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
			convertView = mLayoutInflater.inflate(R.layout.row_home,parent,false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder =  (ViewHolder) convertView.getTag();
		}

		setPassIconVisibility(position);
		setAllowViewVisibility(position);
		setPassIconV(position);


		if(mAppPreferences.getUserId().equalsIgnoreCase(mArrayList.get(position).getUser_id()))
		{
			viewHolder.subscribeBTN.setVisibility(View.GONE);
			viewHolder.totalSub.setVisibility(View.GONE);
		}
		else
		{
			viewHolder.subscribeBTN.setVisibility(View.VISIBLE);
			viewHolder.totalSub.setVisibility(View.VISIBLE);

		}

		String intrstIMG = AppParserConstant.BASE_URL+mArrayList.get(position).getInterest_image();


		if(mArrayList.get(position).getUser_subscribe_status()==0)
		{
			// 0 unsub

			viewHolder.subscribeBTN.setText("Subscribe");
		}
		else
		{
			viewHolder.subscribeBTN.setText("Unsubscribe");

		}

		if(mArrayList.get(position).getUser_profile_status().equalsIgnoreCase("2"))
		{
			viewHolder.userIMG.setVisibility(View.GONE);
			viewHolder.userName.setVisibility(View.GONE);
			viewHolder.maskIMg.setVisibility(View.VISIBLE);

		}
		else if(mArrayList.get(position).getUser_profile_status().equalsIgnoreCase("1"))
		{
			viewHolder.userIMG.setVisibility(View.VISIBLE);
			viewHolder.userName.setVisibility(View.VISIBLE);
			viewHolder.userName.setText(mArrayList.get(position).getUser_fname());
			viewHolder.maskIMg.setVisibility(View.GONE);
			viewHolder.userIMG.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.profileiconsmall));


			if(mArrayList.get(position).getUser_image().equalsIgnoreCase(""))
			{
				viewHolder.userIMG.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.profileiconsmall));

			}
			else
			{
				String userIMG = AppParserConstant.BASE_URL+mArrayList.get(position).getUser_image();

				ImageLoader.getInstance(mActivity).displayImage(userIMG, viewHolder.userIMG,false);

			}


		}

		if(mArrayList.get(position).getInterest_image().equalsIgnoreCase(""))
		{
			viewHolder.intrstIMG.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.profileiconbig));

		}
		else
		{
			ImageLoader.getInstance(mActivity).displayImage(intrstIMG, viewHolder.intrstIMG,false);

		}
		viewHolder.intrstName.setText(mArrayList.get(position).getInterest_name());
		viewHolder.createdOnDate.setText("Created at "+mArrayList.get(position).getCreated_on());
		viewHolder.totalSub.setText(mArrayList.get(position).getTotal_subscribed());


		if (position == (mArrayList.size() - 1)&& position < (mTotalRecord - 1)) 
		{
			mHomeFragment.hitservice(filter_val);
		}
		else
		{
		}


		viewHolder.subscribeBTN.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				//Toast.makeText(mActivity,"Under Development",Toast.LENGTH_SHORT).show();

			}
		});


		convertView.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				if(mArrayList.get(position).getPass_protected().equalsIgnoreCase("1"))
				{
					mHomeFragment.openPasswordDialog(position);
				}
				else
				{
					mHomeFragment.sendIntent(position);

				}
			}
		});

		viewHolder.userIMG.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(mActivity,ShowOtherUserProfileActivity.class);
				intent.putExtra("user_id", mArrayList.get(position).getUser_id());
				intent.putExtra("user_name", mArrayList.get(position).getUser_fname());
				intent.putExtra("user_gender", mArrayList.get(position).getUser_gender());

				mActivity.startActivity(intent);
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
		private TextView intrstName,userName,totalSub,createdOnDate;
		private RoundImageViewGray intrstIMG,userIMG;
		private ImageView textIMG,audioIMG,pictureIMG,passProtectedIMG,maskIMg;
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
			passProtectedIMG = (ImageView) view.findViewById(R.id.img_pswd_protected);

			intrstName.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
			subscribeBTN.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
			totalSub.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
			createdOnDate.setTypeface(mAppTypeFace.getTypeRobotoLight());
			userName.setTypeface(mAppTypeFace.getTypeRoboto_Medium());


		}

	}


	public void setTotalRecord(int total_record)
	{
		this.mTotalRecord = total_record;
	}
	
	public void setfilter_type(int filter_val)
	{
		this.filter_val  = filter_val;
	}


}
