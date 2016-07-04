package com.in.gaston.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.in.gaston.DashBoardActivity;
import com.in.gaston.InterestDetailActivity;
import com.in.gaston.R;
import com.in.gaston.apptypeface.AppTypeFace;
import com.in.gaston.bean.ListLikeUnlikeCommentObjBean;
import com.in.gaston.constant.AppParserConstant;
import com.in.gaston.fragment.HomeFragment;
import com.in.gaston.imageloader.ImageLoader;
import com.in.gaston.view.RoundImageViewGray;

public class ShowListOfLikeUnlikeCommentAdapter extends BaseAdapter 
{
	private Context context;
	private Fragment fragment;
	private ArrayList<ListLikeUnlikeCommentObjBean> arrayList;
	private LayoutInflater inflater;
	private int total_records = 0;
	private String comment_id;
	private int list_type;
	private AppTypeFace appTypeFace;
	public ShowListOfLikeUnlikeCommentAdapter(Context context,Fragment fragment,ArrayList<ListLikeUnlikeCommentObjBean> arrayList)
	{
		this.context = context;
		this.fragment = fragment;
		this.arrayList = arrayList;
		inflater = LayoutInflater.from(context);
		appTypeFace = new AppTypeFace(context);
	}
	public ShowListOfLikeUnlikeCommentAdapter(Context applicationContext,ArrayList<ListLikeUnlikeCommentObjBean> arrayList) {
		this.context = applicationContext;
		this.arrayList = arrayList;
		inflater = LayoutInflater.from(context);
		appTypeFace = new AppTypeFace(context);



	}
	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder viewHolder;		
		if(convertView==null)
		{
			convertView = inflater.inflate(R.layout.row_list_like_unlike, parent, false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (position == (arrayList.size() - 1)&& position < (total_records - 1)) 
		{
			//	Toast.makeText(context, "true", Toast.LENGTH_SHORT).show();

			Activity activity =  (Activity)context;
			((InterestDetailActivity)activity).hit_service(comment_id, list_type);
		}
		else
		{
			//	Toast.makeText(context, "false", Toast.LENGTH_SHORT).show();
		}

		viewHolder.person_name.setText(arrayList.get(position).getUser_first_name());
		viewHolder.person_image.setImageResource(R.drawable.profileiconbig);

		if(list_type == 3)
		{

			if(fragment instanceof HomeFragment && fragment!=null)
			{
				ImageLoader.getInstance(((DashBoardActivity)context)).displayImage(AppParserConstant.BASE_URL+arrayList.get(position).getUser_image(),viewHolder.person_image,false);

			}
			else
			{
				ImageLoader.getInstance(((InterestDetailActivity)context)).displayImage(AppParserConstant.BASE_URL+arrayList.get(position).getUser_image(),viewHolder.person_image,false);

			}
			viewHolder.commentDescTV.setVisibility(View.VISIBLE);
			viewHolder.commentSentTimTV.setVisibility(View.VISIBLE);

			viewHolder.commentDescTV.setText(arrayList.get(position).getComment_text());
			viewHolder.commentSentTimTV.setText(arrayList.get(position).getCreated_on());
		}
		else
		{

			if(fragment instanceof HomeFragment && fragment!=null)
			{
				ImageLoader.getInstance(((DashBoardActivity)context)).displayImage(AppParserConstant.BASE_URL+arrayList.get(position).getUser_image(),viewHolder.person_image,false);

			}
			else
			{
				ImageLoader.getInstance(((InterestDetailActivity)context)).displayImage(AppParserConstant.BASE_URL+arrayList.get(position).getUser_image(),viewHolder.person_image,false);

			}
			viewHolder.commentDescTV.setVisibility(View.GONE);
			viewHolder.commentSentTimTV.setVisibility(View.GONE);
		}



		return convertView;
	}

	class ViewHolder 
	{
		TextView person_name,commentDescTV,commentSentTimTV;
		RoundImageViewGray person_image;
		public ViewHolder(View view)
		{
			person_name =  (TextView) view.findViewById(R.id.tv_person_name);
			person_image =  (RoundImageViewGray) view.findViewById(R.id.img_listing_data_image);
			person_name.setTypeface(appTypeFace.getTypeRoboto_Medium());
			commentSentTimTV = (TextView)view.findViewById(R.id.tv_comment_sent_timing);

			commentDescTV = (TextView)view.findViewById(R.id.tv_comment_desc);
			commentDescTV.setTypeface(appTypeFace.getTypeRoboto_REGULAR());
			commentSentTimTV.setTypeface(appTypeFace.getTypeRoboto_Medium());

		}
	}


	public void set_total_records(int count)
	{
		this.total_records = count;
	}
	public void setdata(String comment_id, int list_type)
	{
		this.comment_id = comment_id;
		this.list_type = list_type;
	}
}
