package com.in.gaston.constant;

public class AppParserConstant 
{
	//common

	public static String ERROR_CODE_KEY = "error_code";
	public static String RESPONSE_STRING_KEY = "response_string";
	public static String STATUS_KEY = "status";
	public static String DATA_KEY = "data";
	public static final String INTRST_LIST_JSON_ARRAY = "interest_list";
	public static final String COMMENT_DETAIL_KEY = "comment_detail";
	public static final String USER_PROFILE_KEY = "user_profile";
	public static final String LIST_KEY = "list";
	//Base Url

	public static String BASE_URL = "http://51.255.160.135";
	//URL http://51.255.160.135/service/user/
	public static final String HIT_URL  = BASE_URL+ "/service/user/";
	public static final String HIT_URL_INTRST  = BASE_URL + "/service/interest/";
	public static final String HIT_URL_USER = BASE_URL +"/service/user/";
	public static final String HIT_URL__NOTIFICATION = BASE_URL + "/service/notification/";
	//Network hit key

	public static String SERVICE_ACCESS_KEY = "service_access_key";
	public static String METHOD_NAME_KEY = "method";

	//Method names
	public static String METHOD_LOGIN = "login";

	public static String METHOD_SIGN_UP = "signup";
	public static String METHOD_PASSCODE = "passcode";
	public static String METHOD_RESET_PSWD = "resetpassword";
	public static final String METHOD_ADD_INTRST = "addinterest";
	public static final String METHOD_FETCH_ALL_DATA = "fetchallinterest";
	public static final String METHOD_RECENT_SEARCH = "recentsearchinterest";
	public static String METHOD_INTRST_DETAIL="interestdetail";
	public static final String METHOD_ADD_COMMENT = "addeditcomment";
	public static final String METHOD_LIKE_UNLIKE = "interestcommentlike";
	public static final String METHOD_SUBSCRIBE = "subscribeinterest";
	public static final String METHOD_PROFILE = "userprofile";
	public static final String METHOD_ACTIVITY_FEEDS = "activityfeeds";


	//Network key value
	public static String SERVICE_ACCESS_KEY_VAL = "aaznyZDQPLLPeAiblq6Mitk6uchEpvEx1ZiorfgAKmA=";
	public static final String DEVICE_TYPE_VAL = "Android";


	//Login
	public static String USER_TYPE_LOGIN_KEY = "user_typelogin";



	//SignUp Key
	public static String USER_DETAIL_KEY = "user_detail";
	public static final String USER_FIRST_NAME_KEY = "user_fname";
	public static final String USER_LAST_NAME_KEY = "user_lname";
	public static final String USER_EMAIL_KEY = "user_email";
	public static final String USER_GENDER_KEY = "user_gender";
	public static final String USER_PSWD_KEY = "user_password";
	public static final String DEVICE_TOKEN_KEY = "user_devicetoken";
	public static final String DEVICE_TYPE_KEY = "user_devicetype";
	public static final String USER_LOCATION_KEY = "user_location";
	public static final String LOCATION_LON_KEY = "user_lon";
	public static final String LOCATION_LAT_KEY = "user_lat";
	public static final String USER_IMAGE_TYPE_KEY = "user_image_type";
	public static final String USER_PRIVATE_IMAGE_KEY = "user_private_image";
	public static final String USER_IMAGE_KEY = "user_image";
	public static final String USER_ID_KEY = "user_id";
	public static final String FB_ID = "user_fbid";
	public static final String USER_ACCESS_TOKEN_KEY = "user_access_token";

	//Reset Pswd

	public static final String USER_NEW_PSWD_KEY = "user_new_password";
	public static final String USER_PASSCODE_KEY = "user_passcode";


	//Crete intrest
	public static final String TOP_INTRST_KEY = "top_interest";
	public static final String PROFILE_STATUS_KEY = "profile_status";
	public static final String FILTER_TYPE_KEY = "filter_type";
	public static final String USER_PROFILE_STATUS_KEY = "user_profile_status";
	public static final String INTEREST_NAME_KEY = "interest_name";
	public static final String INTRST_DES_KEY = "interest_description";
	public static final String PSWD_KEY ="password";
	public static final String ALLOW_TEXT_KEY = "allow_text";
	public static final String ALLOW_IMG_KEY = "allow_image";
	public static final String ALLOW_AUD_KEY = "allow_audio";
	public static final String SEARCH_INTRST = "search_interest";
	public static final String PER_PAGE = "per_page";
	public static final String PAGE_ID = "page_id";


	//fetch interest


	public static final String INTRST_ID_KEY = "interest_id";
	public static final String INTRST_IMG_KEY = "interest_image";
	public static final String PASS_PROTECTED_KEY = "pass_protected";
	public static final String CREATED_ON_KEY = "created_on";
	public static final String USER_PVT_IMG_KEY = "user_private_image";
	public static final String USER_PVT_NAME_KEY = "user_private_name";
	public static final String TOTAL_SUB_KEY = "total_subscribed";
	public static final String TOTAL_RCD_KEY = "total_count";

	//interest detail
	public static final String INTRST_REPORT_KEY = "interest_report";
	public static String INTRST_DETAIL_KEY = "interest_detail";
	public static String INTRST_COMMENT_JSON_ARRAY ="interest_comment";
	public static String COMMENT_ID = "comment_id";
	public static String COMMENT_TYPE ="comment_type";
	public static final String COMMENT_TEXT = "comment_text";
	public static final String COMMENT_IMG = "comment_image";
	public static final String COMMENT_AUD = "comment_audio";
	public static final String TOTAL_LIKES_KEY = "total_likes";
	public static final String TOTAL_UNLIKES_KEY = "total_unlikes";
	public static final String TOTAL_COMMENT_KEY = "total_comments";
	public static String PARENT_COMMENT_ID_KEY = "parent_comment_id";
	public static String COMMENT_LIKE_STATUS_KEY = "comment_like_status";
	public static final String LIKE_STATUS_KEY = "like_status";
	public static final String USER_SUBSCRIBD_STATUS = "user_subscribed";

	//profile
	public static final String TYPE = "type";
	public static final String OTHER_USER_ID = "other_user_id";

	//Activity Feeds
	public static String INTEREST_USER_ID_KEY = "interest_user_id";
	
	//listing og like unlike comments
	public static final String METHOD_LIST_LIKE_UNLIKE = "listlikeunlike";
	public static final String LIST_TYPE_KEY = "list_type";
	public static final String COUNT_KEY = "count";
	public static final String METHOD_LIST_CHILD_COMMENT = "listchildcomment";
	public static final String COMMENT_TEXT_KEY = "comment_text";

	//
	public static final String METHOD_DELETE_DATA = "deleteinterest";
	

	//notification typw
	public static String NOTIFACATION_TYPE = "notification_type";
	public static final String PARENT_COMMENT_ID = "parent_comment_id";
}
