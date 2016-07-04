package com.in.gaston.appparser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;

import com.in.gaston.bean.CommentBean;
import com.in.gaston.bean.CommonBean;
import com.in.gaston.bean.CreateInterestBean;
import com.in.gaston.bean.InteresCommentBean;
import com.in.gaston.bean.InterestArrayListBean;
import com.in.gaston.bean.InterestBean;
import com.in.gaston.bean.InterestDeatailBean;
import com.in.gaston.bean.ListLikeUnlikeCommentBean;
import com.in.gaston.bean.ListLikeUnlikeCommentObjBean;
import com.in.gaston.bean.NotificationBean;
import com.in.gaston.bean.NotificationMainBean;
import com.in.gaston.bean.ProfileDataBean;
import com.in.gaston.bean.SignUpBean;
import com.in.gaston.bean.SubscribeBean;
import com.in.gaston.constant.AppParserConstant;
import com.in.gaston.constant.AppPreferences;

public class AppParser 
{
	private Context mContext;
	private AppPreferences mAppPreferences;

	public AppParser(Activity context)
	{
		mContext = context;
		mAppPreferences = AppPreferences.getInstance(mContext);
	}

	public SignUpBean parseSignUpData(String response) 
	{
		SignUpBean signUpBean = new SignUpBean();

		if(response!=null)
		{
			try
			{
				JSONObject jsonObject = new JSONObject(response);
				if(jsonObject.has(AppParserConstant.ERROR_CODE_KEY))
				{
					signUpBean.setErrorCode(jsonObject.getInt(AppParserConstant.ERROR_CODE_KEY));
				}
				if(jsonObject.has(AppParserConstant.STATUS_KEY))
				{
					signUpBean.setStatus(jsonObject.getInt(AppParserConstant.STATUS_KEY));
				}
				if(jsonObject.has(AppParserConstant.RESPONSE_STRING_KEY))
				{
					signUpBean.setResponseString(jsonObject.getString(AppParserConstant.RESPONSE_STRING_KEY));
				}

				JSONObject dataJSON =  jsonObject.getJSONObject(AppParserConstant.DATA_KEY);
				JSONObject userDetailJSON = dataJSON.getJSONObject(AppParserConstant.USER_DETAIL_KEY);

				if(userDetailJSON.has(AppParserConstant.USER_ID_KEY))
				{
					signUpBean.setUserId(userDetailJSON.getString(AppParserConstant.USER_ID_KEY));
					mAppPreferences.setUserId(userDetailJSON.getString(AppParserConstant.USER_ID_KEY));

				}
				if(userDetailJSON.has("user_name"))
				{
					signUpBean.setUser_name(userDetailJSON.getString("user_name"));

				}

				if(userDetailJSON.has(AppParserConstant.USER_LAST_NAME_KEY))
				{
					signUpBean.setUserLastName(userDetailJSON.getString(AppParserConstant.USER_LAST_NAME_KEY));

				}

				if(userDetailJSON.has(AppParserConstant.USER_EMAIL_KEY))
				{
					signUpBean.setUserEmailId(userDetailJSON.getString(AppParserConstant.USER_EMAIL_KEY));

				}
				if(userDetailJSON.has(AppParserConstant.USER_GENDER_KEY))
				{
					signUpBean.setUserGender(userDetailJSON.getInt(AppParserConstant.USER_GENDER_KEY));

				}
				if(userDetailJSON.has(AppParserConstant.FB_ID))
				{
					signUpBean.setUserFBId(userDetailJSON.getString(AppParserConstant.FB_ID));

				}
				if(userDetailJSON.has(AppParserConstant.USER_IMAGE_KEY))
				{
					signUpBean.setUserImage(userDetailJSON.getString(AppParserConstant.USER_IMAGE_KEY));
					mAppPreferences.setUserIMG(userDetailJSON.getString(AppParserConstant.USER_IMAGE_KEY));
				}
				if(userDetailJSON.has(AppParserConstant.USER_ACCESS_TOKEN_KEY))
				{
					signUpBean.setUserAccessToken(userDetailJSON.getString(AppParserConstant.USER_ACCESS_TOKEN_KEY));
					mAppPreferences.setUserAccessToken(userDetailJSON.getString(AppParserConstant.USER_ACCESS_TOKEN_KEY));
				}

			}catch(Exception e)
			{
				signUpBean.setStatus(0);
				e.printStackTrace();
			}
		}
		else
		{
			signUpBean.setStatus(0);

		}
		return signUpBean;

	}


	public CommonBean parseUserHitNetwork(String response)
	{
		CommonBean commonBean = new CommonBean();

		if(response!=null)
		{
			try
			{
				JSONObject jsonObject = new JSONObject(response);
				commonBean.setErrorCode(jsonObject.getInt(AppParserConstant.ERROR_CODE_KEY));
				commonBean.setStatus(jsonObject.getInt(AppParserConstant.STATUS_KEY));
				commonBean.setResponseString(jsonObject.getString(AppParserConstant.RESPONSE_STRING_KEY));


			}catch(Exception e)
			{
				e.printStackTrace();
				commonBean.setStatus(2);

			}
		}
		else
		{
			commonBean.setStatus(2);

		}
		return commonBean;
	}

	public CreateInterestBean parseCreateInterest(String response) 
	{
		CreateInterestBean createInterestBean = new CreateInterestBean();

		if(response!=null)
		{
			try
			{
				JSONObject jsonObject = new JSONObject(response);
				createInterestBean.setErrorCode(jsonObject.getInt(AppParserConstant.ERROR_CODE_KEY));
				createInterestBean.setStatus(jsonObject.getInt(AppParserConstant.STATUS_KEY));
				createInterestBean.setResponseString(jsonObject.getString(AppParserConstant.RESPONSE_STRING_KEY));

				JSONObject dataJSON =  jsonObject.getJSONObject(AppParserConstant.DATA_KEY);
				//JSONObject userDetailJSON = dataJSON.getJSONObject(AppParserConstant.USER_DETAIL_KEY);



			}catch(Exception e)
			{
				e.printStackTrace();
				createInterestBean.setStatus(2);

			}
		}
		else
		{
			createInterestBean.setStatus(2);

		}


		return createInterestBean;
	}

	public InterestArrayListBean parseFetchInterestData(String response) 
	{
		InterestArrayListBean interestArrayListBean = new InterestArrayListBean();
		ArrayList<InterestBean> inArrayList = new  ArrayList<InterestBean>();
		if(response!=null)
		{
			try
			{
				JSONObject jsonObject = new JSONObject(response);
				interestArrayListBean.setErrorCode(jsonObject.getInt(AppParserConstant.ERROR_CODE_KEY));
				interestArrayListBean.setStatus(jsonObject.getInt(AppParserConstant.STATUS_KEY));
				interestArrayListBean.setResponseString(jsonObject.getString(AppParserConstant.RESPONSE_STRING_KEY));


				JSONObject dataJSON  = jsonObject.getJSONObject(AppParserConstant.DATA_KEY);

				if(dataJSON.has(AppParserConstant.TOTAL_RCD_KEY))
				{
					interestArrayListBean.setTotal_record(dataJSON.getInt(AppParserConstant.TOTAL_RCD_KEY));
				}
				JSONArray intrstListJA =  dataJSON.getJSONArray(AppParserConstant.INTRST_LIST_JSON_ARRAY);


				if(intrstListJA.length()>0)
				{
					for(int i=0;i<intrstListJA.length();i++)
					{
						InterestBean interestBean = new InterestBean();
						JSONObject innerJO =  intrstListJA.getJSONObject(i);
						interestBean.setInterest_image(innerJO.getString(AppParserConstant.INTRST_IMG_KEY));
						interestBean.setInterest_id(innerJO.getString(AppParserConstant.INTRST_ID_KEY));
						interestBean.setInterest_name(innerJO.getString(AppParserConstant.INTEREST_NAME_KEY));
						interestBean.setInterest_description(innerJO.getString(AppParserConstant.INTRST_DES_KEY));
						interestBean.setCreated_on(innerJO.getString(AppParserConstant.CREATED_ON_KEY));
						interestBean.setUser_image(innerJO.getString(AppParserConstant.USER_IMAGE_KEY));
						interestBean.setUser_fname(innerJO.getString(AppParserConstant.USER_FIRST_NAME_KEY));
						interestBean.setTotal_subscribed(innerJO.getString(AppParserConstant.TOTAL_SUB_KEY));
						interestBean.setUser_gender(innerJO.getString(AppParserConstant.USER_GENDER_KEY));
						interestBean.setUser_private_name(innerJO.getString(AppParserConstant.USER_PVT_NAME_KEY));
						interestBean.setUser_private_image(innerJO.getString(AppParserConstant.USER_PVT_IMG_KEY));
						interestBean.setPass_protected(innerJO.getString(AppParserConstant.PASS_PROTECTED_KEY));
						interestBean.setPassword(innerJO.getString(AppParserConstant.PSWD_KEY));
						interestBean.setUser_profile_status(innerJO.getString(AppParserConstant.USER_PROFILE_STATUS_KEY));
						interestBean.setUser_id(innerJO.getString(AppParserConstant.USER_ID_KEY));
						if(innerJO.has(AppParserConstant.USER_SUBSCRIBD_STATUS))
						{
							interestBean.setUser_subscribe_status(innerJO.getInt(AppParserConstant.USER_SUBSCRIBD_STATUS));
						}

						//	mAppPreferences.setUserProfileStatus(innerJO.getString(AppParserConstant.USER_PROFILE_STATUS_KEY));

						interestBean.setAllow_audio(innerJO.getString(AppParserConstant.ALLOW_AUD_KEY));
						interestBean.setAllow_image(innerJO.getString(AppParserConstant.ALLOW_IMG_KEY));
						interestBean.setAllow_text(innerJO.getString(AppParserConstant.ALLOW_TEXT_KEY));

						inArrayList.add(interestBean);
					}
				}
				else
				{
					interestArrayListBean.setStatus(0);

				}
				interestArrayListBean.setArrayList(inArrayList);

			}catch(Exception e)
			{
				e.printStackTrace();
				interestArrayListBean.setStatus(2);

			}
		}
		else
		{
			interestArrayListBean.setStatus(2);

		}

		return interestArrayListBean;
	}

	public InterestDeatailBean parseInteresDetailData(String response) 
	{
		InterestDeatailBean interestDeatailBean = new InterestDeatailBean();
		ArrayList<InteresCommentBean> mArrayList = new  ArrayList<InteresCommentBean>();
		if(response!=null)
		{
			try
			{
				JSONObject jsonObject = new JSONObject(response);
				interestDeatailBean.setErrorCode(jsonObject.getInt(AppParserConstant.ERROR_CODE_KEY));
				interestDeatailBean.setStatus(jsonObject.getInt(AppParserConstant.STATUS_KEY));
				interestDeatailBean.setErrorCode(jsonObject.getInt(AppParserConstant.STATUS_KEY));
				interestDeatailBean.setResponseString(jsonObject.getString(AppParserConstant.RESPONSE_STRING_KEY));


				JSONObject dataJSON  = jsonObject.getJSONObject(AppParserConstant.DATA_KEY);

				if(dataJSON.has(AppParserConstant.TOTAL_RCD_KEY))
				{
					interestDeatailBean.setTotal_record(dataJSON.getInt(AppParserConstant.TOTAL_RCD_KEY));
				}



				if(dataJSON.has(AppParserConstant.INTRST_DETAIL_KEY))
				{
					JSONObject interestDetailJSON_OBJ =  dataJSON.getJSONObject(AppParserConstant.INTRST_DETAIL_KEY);


					/*
					 * interest information
					 */
					interestDeatailBean.setInterest_report(interestDetailJSON_OBJ.getInt(AppParserConstant.INTRST_REPORT_KEY));
					interestDeatailBean.setUser_profile_status(interestDetailJSON_OBJ.getInt(AppParserConstant.USER_PROFILE_STATUS_KEY));
					interestDeatailBean.setInterest_id(interestDetailJSON_OBJ.getString(AppParserConstant.INTRST_ID_KEY));
					interestDeatailBean.setInterest_name(interestDetailJSON_OBJ.getString(AppParserConstant.INTEREST_NAME_KEY));
					interestDeatailBean.setInterest_desc(interestDetailJSON_OBJ.getString(AppParserConstant.INTRST_DES_KEY));
					interestDeatailBean.setInterest_image(interestDetailJSON_OBJ.getString(AppParserConstant.INTRST_IMG_KEY));
					interestDeatailBean.setInterest_created_on(interestDetailJSON_OBJ.getString(AppParserConstant.CREATED_ON_KEY));
					interestDeatailBean.setTotal_subscribe(interestDetailJSON_OBJ.getString(AppParserConstant.TOTAL_SUB_KEY));
					interestDeatailBean.setAllow_audio(interestDetailJSON_OBJ.getString(AppParserConstant.ALLOW_AUD_KEY));
					interestDeatailBean.setAllow_picture(interestDetailJSON_OBJ.getString(AppParserConstant.ALLOW_IMG_KEY));
					interestDeatailBean.setAllow_text(interestDetailJSON_OBJ.getString(AppParserConstant.ALLOW_TEXT_KEY));
					interestDeatailBean.setInterestCreateUserIMG(interestDetailJSON_OBJ.getString(AppParserConstant.USER_IMAGE_KEY));
					interestDeatailBean.setUserName(interestDetailJSON_OBJ.getString(AppParserConstant.USER_FIRST_NAME_KEY));
					interestDeatailBean.setUser_subscibed_status(interestDetailJSON_OBJ.getInt(AppParserConstant.USER_SUBSCRIBD_STATUS));

				}

				/*
				 * comment info
				 */
				JSONArray interestCommentJSONARRAY = dataJSON.getJSONArray(AppParserConstant.INTRST_COMMENT_JSON_ARRAY);

				if(interestCommentJSONARRAY.length()>0)
				{
					for(int i=0;i<interestCommentJSONARRAY.length();i++)
					{
						InteresCommentBean interestBean = new InteresCommentBean();
						JSONObject innerJO =  interestCommentJSONARRAY.getJSONObject(i);

						interestBean.setComment_id(innerJO.getString(AppParserConstant.COMMENT_ID));
						interestBean.setUser_id(innerJO.getString(AppParserConstant.USER_ID_KEY));
						interestBean.setUser_profile_status(innerJO.getString(AppParserConstant.USER_PROFILE_STATUS_KEY));
						interestBean.setInterest_id(innerJO.getString(AppParserConstant.INTRST_ID_KEY));
						interestBean.setComment_type(innerJO.getString(AppParserConstant.COMMENT_TYPE));
						interestBean.setComment_text(innerJO.getString(AppParserConstant.COMMENT_TEXT));
						interestBean.setComment_image(innerJO.getString(AppParserConstant.COMMENT_IMG));
						interestBean.setComment_audio(innerJO.getString(AppParserConstant.COMMENT_AUD));
						interestBean.setComment_created_on(innerJO.getString(AppParserConstant.CREATED_ON_KEY));

						interestBean.setUser_first_name(innerJO.getString(AppParserConstant.USER_FIRST_NAME_KEY));
						interestBean.setUser_last_name(innerJO.getString(AppParserConstant.USER_LAST_NAME_KEY));
						interestBean.setUser_image(innerJO.getString(AppParserConstant.USER_IMAGE_KEY));
						interestBean.setUser_private_name(innerJO.getString(AppParserConstant.USER_PVT_NAME_KEY));
						interestBean.setUser_gender(innerJO.getString(AppParserConstant.USER_GENDER_KEY));

						interestBean.setTotal_like(innerJO.getString(AppParserConstant.TOTAL_LIKES_KEY));
						interestBean.setTotal_comment(innerJO.getString(AppParserConstant.TOTAL_COMMENT_KEY));
						interestBean.setTptal_dislike(innerJO.getString(AppParserConstant.TOTAL_UNLIKES_KEY));
						interestBean.setComment_like_status(innerJO.getString(AppParserConstant.COMMENT_LIKE_STATUS_KEY));

						if(innerJO.has(AppParserConstant.INTEREST_USER_ID_KEY))
						{
							interestBean.setInterest_user_id(innerJO.getString(AppParserConstant.INTEREST_USER_ID_KEY));
						}

						if(innerJO.has(AppParserConstant.INTEREST_NAME_KEY))
						{
							interestBean.setInterest_name(innerJO.getString(AppParserConstant.INTEREST_NAME_KEY));
						}
						mArrayList.add(interestBean);
					}
				}
				else
				{
					interestDeatailBean.setStatus(1);
					interestDeatailBean.setErrorCode(2);

				}
				interestDeatailBean.setmArrayList(mArrayList);

			}catch(Exception e)
			{
				e.printStackTrace();
				interestDeatailBean.setStatus(2);

			}
		}
		else
		{
			interestDeatailBean.setStatus(2);

		}
		return interestDeatailBean;
	}

	public CommentBean parseCommentResponse(String response) 
	{
		CommentBean commentBean = new CommentBean();
		try
		{
			JSONObject jsonObject = new JSONObject(response);
			commentBean.setErrorCode(jsonObject.getInt(AppParserConstant.ERROR_CODE_KEY));
			commentBean.setStatus(jsonObject.getInt(AppParserConstant.STATUS_KEY));
			commentBean.setResponseString(jsonObject.getString(AppParserConstant.RESPONSE_STRING_KEY));


			JSONObject dataJSON  = jsonObject.getJSONObject(AppParserConstant.DATA_KEY);

			JSONObject commentDetailJO = 	dataJSON.getJSONObject(AppParserConstant.COMMENT_DETAIL_KEY);

			commentBean.setCommentId(commentDetailJO.getString(AppParserConstant.COMMENT_ID));
		}
		catch(Exception e)
		{

		}
		return commentBean;
	}

	public CommentBean parseLikeUnLikeResponse(String response) 
	{
		CommentBean commentBean = new CommentBean();
		try
		{
			JSONObject jsonObject = new JSONObject(response);
			commentBean.setErrorCode(jsonObject.getInt(AppParserConstant.ERROR_CODE_KEY));
			commentBean.setStatus(jsonObject.getInt(AppParserConstant.STATUS_KEY));
			commentBean.setResponseString(jsonObject.getString(AppParserConstant.RESPONSE_STRING_KEY));
			JSONObject dataJSON  = jsonObject.getJSONObject(AppParserConstant.DATA_KEY);
			commentBean.setTotal_like(dataJSON.getString(AppParserConstant.TOTAL_LIKES_KEY));
			commentBean.setTotal_unlike(dataJSON.getString(AppParserConstant.TOTAL_UNLIKES_KEY));
			commentBean.setLike_unlike_status(dataJSON.getString(AppParserConstant.COMMENT_LIKE_STATUS_KEY));

		}
		catch(Exception e)
		{
		}
		return commentBean;
	}

	public SubscribeBean parseSubscribeNetwork(String response) 
	{
		SubscribeBean commonBean = new SubscribeBean();

		if(response!=null)
		{
			try
			{
				JSONObject jsonObject = new JSONObject(response);
				commonBean.setErrorCode(jsonObject.getInt(AppParserConstant.ERROR_CODE_KEY));
				commonBean.setStatus(jsonObject.getInt(AppParserConstant.STATUS_KEY));
				commonBean.setResponseString(jsonObject.getString(AppParserConstant.RESPONSE_STRING_KEY));
				commonBean.setTotal_subscribe(jsonObject.getString(AppParserConstant.DATA_KEY));

			}catch(Exception e)
			{
				e.printStackTrace();
				commonBean.setStatus(2);

			}
		}
		else
		{
			commonBean.setStatus(2);

		}
		return commonBean;
	}

	public ProfileDataBean parseProfileData(String response) 
	{
		ProfileDataBean profileDataBean = new ProfileDataBean();
		ArrayList<InterestBean> mArrayList = new ArrayList<InterestBean>();
		if(response!=null)
		{
			try
			{
				JSONObject jsonObject = new JSONObject(response);
				profileDataBean.setErrorCode(jsonObject.getInt(AppParserConstant.ERROR_CODE_KEY));
				profileDataBean.setStatus(jsonObject.getInt(AppParserConstant.STATUS_KEY));
				profileDataBean.setResponseString(jsonObject.getString(AppParserConstant.RESPONSE_STRING_KEY));

				JSONObject dataJO =  jsonObject.getJSONObject(AppParserConstant.DATA_KEY);
				if(dataJO.has(AppParserConstant.TOTAL_RCD_KEY))
				{
					profileDataBean.setTotal_record(dataJO.getInt(AppParserConstant.TOTAL_RCD_KEY));
				}



				JSONObject userProfileJO =  dataJO.getJSONObject(AppParserConstant.USER_PROFILE_KEY);
				profileDataBean.setUser_fname(userProfileJO.getString(AppParserConstant.USER_FIRST_NAME_KEY));
				profileDataBean.setUser_lname(userProfileJO.getString(AppParserConstant.USER_LAST_NAME_KEY));
				profileDataBean.setUser_email(userProfileJO.getString(AppParserConstant.USER_EMAIL_KEY));
				profileDataBean.setUser_gender(userProfileJO.getString(AppParserConstant.USER_GENDER_KEY));
				profileDataBean.setPrivate_name(userProfileJO.getString(AppParserConstant.USER_PVT_NAME_KEY));
				profileDataBean.setUser_private_image(userProfileJO.getString("user_private_image"));
				profileDataBean.setPrivate_gender(userProfileJO.getString("private_gender"));
				profileDataBean.setPrivate_desc(userProfileJO.getString("private_description"));


				if(userProfileJO.has("description"))
				{
					profileDataBean.setUser_description(userProfileJO.getString("description"));
				}


				if(userProfileJO.has("user_image"))
				{
					profileDataBean.setUser_image(userProfileJO.getString("user_image"));
				}
				JSONArray intrstListJA = dataJO.getJSONArray(AppParserConstant.INTRST_LIST_JSON_ARRAY);


				if(intrstListJA.length()>0)
				{
					for(int i=0;i<intrstListJA.length();i++)
					{
						InterestBean interestBean = new InterestBean();
						JSONObject innerJO =  intrstListJA.getJSONObject(i);
						interestBean.setInterest_image(innerJO.getString(AppParserConstant.INTRST_IMG_KEY));
						interestBean.setInterest_id(innerJO.getString(AppParserConstant.INTRST_ID_KEY));
						interestBean.setInterest_name(innerJO.getString(AppParserConstant.INTEREST_NAME_KEY));
						interestBean.setInterest_description(innerJO.getString(AppParserConstant.INTRST_DES_KEY));
						interestBean.setCreated_on(innerJO.getString(AppParserConstant.CREATED_ON_KEY));
						interestBean.setUser_image(innerJO.getString(AppParserConstant.USER_IMAGE_KEY));
						interestBean.setUser_fname(innerJO.getString(AppParserConstant.USER_FIRST_NAME_KEY));
						interestBean.setTotal_subscribed(innerJO.getString(AppParserConstant.TOTAL_SUB_KEY));
						interestBean.setUser_private_name(innerJO.getString(AppParserConstant.USER_PVT_NAME_KEY));
						interestBean.setUser_private_image(innerJO.getString(AppParserConstant.USER_PVT_IMG_KEY));
						interestBean.setPass_protected(innerJO.getString(AppParserConstant.PASS_PROTECTED_KEY));
						interestBean.setPassword(innerJO.getString(AppParserConstant.PSWD_KEY));
						interestBean.setUser_profile_status(innerJO.getString(AppParserConstant.USER_PROFILE_STATUS_KEY));
						interestBean.setAllow_audio(innerJO.getString(AppParserConstant.ALLOW_AUD_KEY));
						interestBean.setAllow_image(innerJO.getString(AppParserConstant.ALLOW_IMG_KEY));
						interestBean.setAllow_text(innerJO.getString(AppParserConstant.ALLOW_TEXT_KEY));
						interestBean.setUser_id(innerJO.getString(AppParserConstant.USER_ID_KEY));
						if(innerJO.has("subscribed"))
						{
							interestBean.setUser_subscribe_status(innerJO.getInt("subscribed"));
						}

						mArrayList.add(interestBean);
					}
				}
				else
				{
					profileDataBean.setStatus(0);
				}

				profileDataBean.setmArrayList(mArrayList);
			}catch(Exception e)
			{
				e.printStackTrace();
				profileDataBean.setStatus(2);
			}
		}
		else
		{
			profileDataBean.setStatus(2);

		}
		return profileDataBean;
	}

	public ListLikeUnlikeCommentBean parseListLikeUnLikeResponse(String response) 
	{

		ListLikeUnlikeCommentBean listLikeUnlikeCommentBean = new ListLikeUnlikeCommentBean();
		ArrayList<ListLikeUnlikeCommentObjBean> arrayList = new ArrayList<ListLikeUnlikeCommentObjBean>();

		if(response!=null)
		{
			try
			{
				JSONObject jsonObject = new JSONObject(response);
				listLikeUnlikeCommentBean.setErrorCode(jsonObject.getInt(AppParserConstant.ERROR_CODE_KEY));
				listLikeUnlikeCommentBean.setStatus(jsonObject.getInt(AppParserConstant.STATUS_KEY));
				listLikeUnlikeCommentBean.setResponseString(jsonObject.getString(AppParserConstant.RESPONSE_STRING_KEY));


				JSONObject dataJO =  jsonObject.getJSONObject(AppParserConstant.DATA_KEY);
				int count = Integer.parseInt(dataJO.getString(AppParserConstant.COUNT_KEY));
				listLikeUnlikeCommentBean.setTotal_records(count);

				JSONArray listJA = dataJO.getJSONArray(AppParserConstant.LIST_KEY);

				for(int i=0;i<listJA.length();i++)
				{
					JSONObject inner_json_obj=  listJA.getJSONObject(i);

					ListLikeUnlikeCommentObjBean likeUnlikeCommentObjBean = new ListLikeUnlikeCommentObjBean();
					likeUnlikeCommentObjBean.setUser_first_name(inner_json_obj.getString(AppParserConstant.USER_FIRST_NAME_KEY));
					likeUnlikeCommentObjBean.setUser_id(inner_json_obj.getString(AppParserConstant.USER_ID_KEY));
					likeUnlikeCommentObjBean.setUser_image(inner_json_obj.getString(AppParserConstant.USER_IMAGE_KEY));
					likeUnlikeCommentObjBean.setUser_private_name(inner_json_obj.getString(AppParserConstant.USER_PVT_NAME_KEY));

					if(inner_json_obj.has(AppParserConstant.COMMENT_TEXT_KEY))
					{
						likeUnlikeCommentObjBean.setComment_text(inner_json_obj.getString(AppParserConstant.COMMENT_TEXT_KEY));
					}
					if(inner_json_obj.has(AppParserConstant.CREATED_ON_KEY))
					{
						likeUnlikeCommentObjBean.setCreated_on(inner_json_obj.getString(AppParserConstant.CREATED_ON_KEY));
					}
					arrayList.add(likeUnlikeCommentObjBean);
				}
				listLikeUnlikeCommentBean.setArrayList(arrayList);

			}
			catch(Exception e)
			{
				e.printStackTrace();
				listLikeUnlikeCommentBean.setStatus(2);

			}
		}
		else
		{
			listLikeUnlikeCommentBean.setStatus(2);

		}
		return listLikeUnlikeCommentBean;

	}

	public NotificationMainBean parseNotificationData(String response) 
	{
		NotificationMainBean notificationtDeatailBean = new NotificationMainBean();
		ArrayList<NotificationBean> mArrayList = new  ArrayList<NotificationBean>();
		if(response!=null)
		{
			try
			{
				JSONObject jsonObject = new JSONObject(response);
				notificationtDeatailBean.setErrorCode(jsonObject.getInt(AppParserConstant.ERROR_CODE_KEY));
				notificationtDeatailBean.setStatus(jsonObject.getInt(AppParserConstant.STATUS_KEY));
				notificationtDeatailBean.setResponseString(jsonObject.getString(AppParserConstant.RESPONSE_STRING_KEY));


				JSONObject dataJSON  = jsonObject.getJSONObject(AppParserConstant.DATA_KEY);

				if(dataJSON.has(AppParserConstant.TOTAL_RCD_KEY))
				{
					int total_record =  Integer.parseInt(dataJSON.getString(AppParserConstant.TOTAL_RCD_KEY));
					notificationtDeatailBean.setTotal_record(total_record);
				}
				/*
				 * comment info
				 */
				JSONArray notificationJA = dataJSON.getJSONArray(AppParserConstant.LIST_KEY);

				if(notificationJA.length()>0)
				{
					for(int i=0;i<notificationJA.length();i++)
					{
						NotificationBean notificationBean = new NotificationBean();

						JSONObject innerJO =  notificationJA.getJSONObject(i);
						notificationBean.setNotificatoin_type(innerJO.getString(AppParserConstant.NOTIFACATION_TYPE));
						notificationBean.setInterest_id(innerJO.getString(AppParserConstant.INTRST_ID_KEY));
						notificationBean.setComment_id(innerJO.getString(AppParserConstant.COMMENT_ID));
						notificationBean.setParent_comment_id(innerJO.getString(AppParserConstant.PARENT_COMMENT_ID));
						notificationBean.setCreated_on(innerJO.getString(AppParserConstant.CREATED_ON_KEY));
						notificationBean.setNotification_profile_status(innerJO.getString("notification_profile_status"));

						//sender
						JSONObject senderJO = 	innerJO.getJSONObject("sender");
						notificationBean.setSender_first_name(senderJO.getString("first_name"));
						notificationBean.setSender_last_name(senderJO.getString("last_name"));
						notificationBean.setSender_image(senderJO.getString("user_image"));
						notificationBean.setSender_pvt_name(senderJO.getString("user_private_name"));


						//interest
						JSONObject interestJO = 	innerJO.getJSONObject("interest");
						notificationBean.setInteresr_name(interestJO.getString("interest_name"));
						notificationBean.setInterest_user_id(interestJO.getString("interest_user_id"));

						notificationBean.setPaasword(interestJO.getString("password"));

						//parent
						JSONObject parentJO = 	innerJO.getJSONObject("parent");
						notificationBean.setParent_comment_type(parentJO.getString("comment_type"));
						notificationBean.setParent_comment_user_id(parentJO.getString("comment_user_id"));


						//child					
						JSONObject childJO = 	innerJO.getJSONObject("child");
						notificationBean.setChild_comment_type(childJO.getString("comment_type"));
						notificationBean.setChile_comment_user_id(childJO.getString("comment_user_id"));


						mArrayList.add(notificationBean);
					}

					notificationtDeatailBean.setmArrayList(mArrayList);
				}
				else
				{
					notificationtDeatailBean.setStatus(0);

				}
			}catch(Exception e)
			{
				e.printStackTrace();
				notificationtDeatailBean.setStatus(2);

			}
		}
		else
		{
			notificationtDeatailBean.setStatus(2);

		}
		return notificationtDeatailBean;
	}


}
