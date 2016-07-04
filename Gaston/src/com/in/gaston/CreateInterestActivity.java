package com.in.gaston;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.in.gaston.apptypeface.AppTypeFace;
import com.in.gaston.asyntask.CreateInteresrAsynTask;
import com.in.gaston.bean.CreateInterestBean;
import com.in.gaston.bean.RequestBean;
import com.in.gaston.constant.AppParserConstant;
import com.in.gaston.constant.AppPreferences;
import com.in.gaston.constant.GastonConstant;
import com.in.gaston.helper.FileUtils;
import com.in.gaston.imageloader.ImageLoader;
import com.in.gaston.network.NetworkStatus;
import com.in.gaston.view.CustomView;
import com.in.gaston.view.RoundImageViewGray;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

@SuppressLint("NewApi")
public class CreateInterestActivity extends Activity implements OnClickListener,ImageChooserListener
{
	private RoundImageViewGray mRealIMG,mFakeIMG,mIntrstIMG;
	private EditText mInterestNameET,mIntsrtDesET,mPswd1ET,mPswd2ET,mPswd3ET,mPswd4ET;
	private ImageView mAllowTextTV,mAllowMusicTV,mAllowPicTV;
	private Button mCreateBTN;
	private int mProfileStatusType=1;
	private AppPreferences mAppPreferences;
	private int mTextTV=0,mPicTV=0,mAudTV=0;
	private String mPicturePath ="";
	private FileBody mIntrstImage;
	private ProgressDialog pbar;
	private ImageChooserManager imageChooserManager;
	private int chooserType;
	private LinearLayout mPswdProtectedLL;
	private Uri selectedImage;
	private Switch mPswdProtected;
	private View mRealV,mFakeV;
	private String mProtectedPswdVal="";
	private AppTypeFace mAppTypeFace;
	private LinearLayout mFakeLL,mRealLL;
	private CustomView mCustomView;
	private boolean isText = false,isIMG = false,isMusic = false;
	private boolean isCreateAppSuccess =false;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_interest);
		initView();
		iniVariables();
		setSelector(1);
	}

	private void initView() 
	{
		mAppTypeFace = new AppTypeFace(CreateInterestActivity.this);

		mPswdProtectedLL =  (LinearLayout) findViewById(R.id.ll_pswd_protected);
		mPswdProtected = (Switch) findViewById(R.id.switch_pass_protected);
		mRealIMG = (RoundImageViewGray) findViewById(R.id.img_real);
		mFakeIMG = (RoundImageViewGray) findViewById(R.id.img_fake);
		mIntrstIMG = (RoundImageViewGray) findViewById(R.id.img_interest);
		mCreateBTN = (Button) findViewById(R.id.btn_create);
		mInterestNameET = (EditText) findViewById(R.id.et_interest_name);
		mIntsrtDesET = (EditText) findViewById(R.id.et_interest_desciption);
		mAllowTextTV = (ImageView) findViewById(R.id.tv_allow_option1);
		mAllowPicTV = (ImageView) findViewById(R.id.tv_allow_option2);
		mAllowMusicTV = (ImageView) findViewById(R.id.tv_allow_option3);
		mPswd1ET =  (EditText) findViewById(R.id.et_password_protected_1);
		mPswd2ET =  (EditText) findViewById(R.id.et_password_protected_2);
		mPswd3ET =  (EditText) findViewById(R.id.et_password_protected_3);
		mPswd4ET =  (EditText) findViewById(R.id.et_password_protected_4);
		mRealV =  findViewById(R.id.view_real);
		mFakeV =  findViewById(R.id.view_fake);
		mRealLL = (LinearLayout) findViewById(R.id.ll_real);
		mFakeLL = (LinearLayout) findViewById(R.id.ll_fake);
		TextView headerTV = (TextView) findViewById(R.id.tv_header);
		TextView passTV = 	(TextView) findViewById(R.id.tv_pass);

		TextView choose_imgTV = (TextView) findViewById(R.id.tv_choose_img);
		TextView allowTV = (TextView) findViewById(R.id.tv_allow);
		TextView digitTV = (TextView) findViewById(R.id.tv_digit);


		/**
		 * typeface
		 */

		headerTV.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());
		choose_imgTV.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());
		mInterestNameET.setTypeface(mAppTypeFace.getTypeRobotoLight());
		mIntsrtDesET.setTypeface(mAppTypeFace.getTypeRobotoLight());
		passTV.setTypeface(mAppTypeFace.getTypeRobotoLight());
		digitTV.setTypeface(mAppTypeFace.getTypeRobotoLight());

		allowTV.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());
		mCreateBTN.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());


		mRealLL.setOnClickListener(this);
		mFakeLL.setOnClickListener(this);

		mCreateBTN.setOnClickListener(this);
		mIntrstIMG.setOnClickListener(this);
		mAllowTextTV.setOnClickListener(this);
		mAllowMusicTV.setOnClickListener(this);
		mAllowPicTV.setOnClickListener(this);
		mRealIMG.setOnClickListener(this);
		mFakeIMG.setOnClickListener(this);
		mPswdProtectedLL.setOnClickListener(this);
		mPswd1ET.setOnClickListener(this);
		mPswd2ET.setOnClickListener(this);
		mPswd3ET.setOnClickListener(this);
		mPswd4ET.setOnClickListener(this);
		/*
		 * 
		 * 
		 */

		mPswd1ET.addTextChangedListener(new TextWatcher() 
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) 
			{
				mPswd2ET.requestFocus();

			}
		});
		mPswd2ET.addTextChangedListener(new TextWatcher() 
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) 
			{
				mPswd3ET.requestFocus();

			}
		});
		mPswd3ET.addTextChangedListener(new TextWatcher() 
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) 
			{
				mPswd4ET.requestFocus();

			}
		});

		mPswd4ET.addTextChangedListener(new TextWatcher() 
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) 
			{
				mPswd1ET.requestFocus();

			}
		});

		/*
		 *
		 */

		mPswdProtected.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				if(isChecked)
				{
					mPswd1ET.requestFocus();
					mPswd1ET.setText("");
					mPswd2ET.setText("");
					mPswd3ET.setText("");
					mPswd4ET.setText("");

					mPswdProtectedLL.setVisibility(View.VISIBLE);

				}
				else
				{
					mPswdProtectedLL.setVisibility(View.GONE);

				}

			}
		});
	}

	private void iniVariables() 
	{
		mAppPreferences = AppPreferences.getInstance(CreateInterestActivity.this);
		ImageLoader.getInstance(CreateInterestActivity.this).displayImage(AppParserConstant.BASE_URL+mAppPreferences.getUserIMG(), mRealIMG,false);
		mCustomView =  new CustomView(CreateInterestActivity.this,CreateInterestActivity.this);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.ll_real:
			mCustomView.hideSoftKeyboard(CreateInterestActivity.this);
			mProfileStatusType = 1;
			setSelector(1);

			break;

		case R.id.ll_fake:
			mCustomView.hideSoftKeyboard(CreateInterestActivity.this);

			mProfileStatusType = 2;
			setSelector(2);
			break;
		case R.id.img_real:
			mCustomView.hideSoftKeyboard(CreateInterestActivity.this);

			mProfileStatusType = 1;
			setSelector(1);

			break;

		case R.id.img_fake:
			mCustomView.hideSoftKeyboard(CreateInterestActivity.this);

			mProfileStatusType = 2;
			setSelector(2);
			break;




		case R.id.btn_create:

			
			CustomView.hideSoftKeyboard(CreateInterestActivity.this);
			
			if(validation(mInterestNameET.getText().toString(),mIntsrtDesET.getText().toString(),mPswdProtected.isChecked()))
			{
				if(mPswdProtected.isChecked())
				{
					mProtectedPswdVal = mPswd1ET.getText().toString()+mPswd2ET.getText().toString()+mPswd3ET.getText().toString()+mPswd4ET.getText().toString();
				}
				else
				{
					mProtectedPswdVal = "";
				}
				hitCreateInterestService();
			}
			break;


		case R.id.tv_allow_option1:
			if(!isText)
			{
				isText = true;
				mTextTV = 1;

				mAllowTextTV.setImageResource(R.drawable.text_active1);
			}
			else
			{

				isText = false;
				mTextTV = 0;

				mAllowTextTV.setImageResource(R.drawable.text);

			}

			break;

		case R.id.tv_allow_option2:


			if(!isIMG)
			{
				isIMG = true;
				mPicTV = 1;

				mAllowPicTV.setImageResource(R.drawable.image_active);
			}
			else
			{

				isIMG = false;
				mPicTV = 0;

				mAllowPicTV.setImageResource(R.drawable.image);

			}
			break;

		case R.id.tv_allow_option3:


			if(!isMusic)
			{
				isMusic = true;
				mAudTV = 1;

				mAllowMusicTV.setImageResource(R.drawable.music_active);
			}
			else
			{

				isMusic = false;
				mAudTV = 0;

				mAllowMusicTV.setImageResource(R.drawable.music);
			}

			break;

		case R.id.img_interest:
			showDialog();
			break;

		default:
			break;
		}
	}
	private void setSelector(int selector) 
	{
		if (selector==1) 
		{
			mRealV.setBackgroundColor(getResources().getColor(R.color.c_login_button_green_0a6f1f));
			mFakeV.setBackgroundColor(getResources().getColor(R.color.c_white));
		}else if (selector==2) 
		{

			mFakeV.setBackgroundColor(getResources().getColor(R.color.c_login_button_green_0a6f1f));
			mRealV.setBackgroundColor(getResources().getColor(R.color.c_white));

		}

	}

	private boolean validation(String name, String des, boolean isPswd) 
	{

		if(name.length()<0||name.equalsIgnoreCase(""))
		{
			Toast.makeText(CreateInterestActivity.this,"Please add interest name",Toast.LENGTH_SHORT).show();
			return false;
		}

		else if (des.length()<0||des.equalsIgnoreCase("")) 
		{
			Toast.makeText(CreateInterestActivity.this,"Please add interest description",Toast.LENGTH_SHORT).show();

			return false;

		}

		else if(isPswd)
		{
			if(mPswd1ET.getText().toString().equalsIgnoreCase("")||mPswd2ET.getText().toString().equalsIgnoreCase("")||mPswd3ET.getText().toString().equalsIgnoreCase("")||mPswd4ET.getText().toString().equalsIgnoreCase(""))
			{
				Toast.makeText(CreateInterestActivity.this,"Password entered should be of 4 digit",Toast.LENGTH_SHORT).show();

				return false;
			}

		}
		return true;
	}

	private void hitCreateInterestService()
	{
		isCreateAppSuccess = false;
		/*
		 * service_access_key,
		 *  method ,user_access_token ,
		 *  user_id ,
		 *  user_profile_status ,
		 *  interest_name ,
		 *  interest_description , 
		 *  password , 
		 *  allow_text , 
		 *  allow_image,
		 *  allow_audio,interest_image
		 */
		/*
		 * user_profile_status 1 for real 2 for fake
		 */

		if(NetworkStatus.isInternetOn(CreateInterestActivity.this))
		{
			RequestBean requestBean = new RequestBean();

			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			try
			{
				StringBody service_access_keySB = new StringBody(AppParserConstant.SERVICE_ACCESS_KEY_VAL);
				StringBody user_access_tokenSB = new StringBody(mAppPreferences.getUserAccessToken());
				StringBody methodSB = new StringBody(AppParserConstant.METHOD_ADD_INTRST);
				StringBody user_idSB = new StringBody(mAppPreferences.getUserId());
				StringBody user_profile_statusSB = new StringBody(String.valueOf(mProfileStatusType));
				StringBody interest_nameSB = new StringBody(mInterestNameET.getText().toString());
				StringBody interest_descriptionSB = new StringBody(mIntsrtDesET.getText().toString());
				StringBody passwordSB = new StringBody(mProtectedPswdVal);
				StringBody allow_textSB = new StringBody(String.valueOf(mTextTV));
				StringBody allow_imageSB = new StringBody(String.valueOf(mPicTV));
				StringBody allow_audioSB = new StringBody(String.valueOf(mAudTV));


				reqEntity.addPart(AppParserConstant.SERVICE_ACCESS_KEY, service_access_keySB);
				reqEntity.addPart(AppParserConstant.USER_ACCESS_TOKEN_KEY, user_access_tokenSB);
				reqEntity.addPart(AppParserConstant.METHOD_NAME_KEY, methodSB);
				reqEntity.addPart(AppParserConstant.USER_ID_KEY, user_idSB);
				reqEntity.addPart(AppParserConstant.USER_PROFILE_STATUS_KEY, user_profile_statusSB);
				reqEntity.addPart(AppParserConstant.INTEREST_NAME_KEY, interest_nameSB);
				reqEntity.addPart(AppParserConstant.INTRST_DES_KEY, interest_descriptionSB);
				reqEntity.addPart(AppParserConstant.PSWD_KEY, passwordSB);
				reqEntity.addPart(AppParserConstant.ALLOW_TEXT_KEY, allow_textSB);
				reqEntity.addPart(AppParserConstant.ALLOW_IMG_KEY, allow_imageSB);
				reqEntity.addPart(AppParserConstant.ALLOW_AUD_KEY, allow_audioSB);



				if (mPicturePath != null && mPicturePath.trim().length() > 0) 
				{
					File profilePicFile = new File(mPicturePath);
					if (mPicturePath.endsWith(".png")) 
					{
						mIntrstImage = new FileBody(profilePicFile, GastonConstant.THUMBNAIL_IMAGE_PNG);
					} else if (mPicturePath.endsWith("jpg")) {
						mIntrstImage = new FileBody(profilePicFile,  GastonConstant.THUMBNAIL_IMAGE_JPG);
					} else if (mPicturePath.endsWith("jpeg")) {
						mIntrstImage = new FileBody(profilePicFile,  GastonConstant.THUMBNAIL_IMAGE_JPEG);
					}
					reqEntity.addPart("interest_image", mIntrstImage);
				}


				requestBean.setUrl(AppParserConstant.HIT_URL_INTRST);
				requestBean.setLoader(true);
				requestBean.setMultipartEntity(reqEntity);
				requestBean.setActivity(CreateInterestActivity.this);
				CreateInteresrAsynTask signUpAsynTask = new CreateInteresrAsynTask(requestBean);

				if(VERSION.SDK_INT < VERSION_CODES.HONEYCOMB){
					signUpAsynTask.execute();
				}else{
					signUpAsynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");	
				}

			}catch(Exception e)
			{
				e.printStackTrace();

			}
		}
		else
		{
			Toast.makeText(CreateInterestActivity.this,getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();

		}
	}


	/*
	 * image capture
	 */
	public void showDialog()
	{
		Button gallery,camera;
		final Dialog dialog = new Dialog(CreateInterestActivity.this,R.style.transparent_background_dialog);
		dialog.setContentView(R.layout.dialog_custom);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialog.getWindow().setAttributes(lp);
		gallery	= (Button) dialog.findViewById(R.id.btn_gallery);
		camera	= (Button) dialog.findViewById(R.id.btn_camera);
		dialog.show();
		TextView text = (TextView) dialog.findViewById(R.id.tv_dialog_text);

		text.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
		gallery.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
		camera.setTypeface(mAppTypeFace.getTypeRoboto_Medium());

		gallery.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				dialog.dismiss();
				chooseImage();
			}
		});

		camera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) 
			{

				dialog.dismiss();
				takePicture();
			}
		});


	}


	private void showProgress(boolean show) {
		if(pbar==null) {
			pbar = new ProgressDialog(CreateInterestActivity.this);
			pbar.setTitle(R.string.app_name);
			pbar.setMessage(getString(R.string.text_please_wait));
		}
		if((!pbar.isShowing()) && show) {
			pbar.show();
		}else {
			pbar.cancel();
		}
	}


	//



	private void chooseImage() {
		chooserType = ChooserType.REQUEST_PICK_PICTURE;
		imageChooserManager = new ImageChooserManager(this,
				ChooserType.REQUEST_PICK_PICTURE, FileUtils.APP_FOLDER_NAME, true);
		imageChooserManager.setImageChooserListener(this);
		try {
			showProgress(true);
			mPicturePath = imageChooserManager.choose();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void takePicture() {
		chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
		imageChooserManager = new ImageChooserManager(this,
				ChooserType.REQUEST_CAPTURE_PICTURE, FileUtils.APP_FOLDER_NAME, true);
		imageChooserManager.setImageChooserListener(this);
		try {
			showProgress(true);
			mPicturePath = imageChooserManager.choose();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK
				&& (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
			if (imageChooserManager == null) 
			{
				reinitializeImageChooser();
			}
			imageChooserManager.submit(requestCode, data);
		} else {
			showProgress(false);
		}
		if(resultCode== RESULT_OK && requestCode == GastonConstant.CROP_PIC_INTENT_KEY){
			try {

				File file = new File(mPicturePath);
				if (file.isFile()) 
				{

					selectedImage = Uri.fromFile(file);
					Bitmap decodeFile = FileUtils.decodeFile(mPicturePath);
					mIntrstIMG.setImageBitmap(decodeFile);
				}
				else
				{
					Toast.makeText(CreateInterestActivity.this,R.string.text_no_resource_found, Toast.LENGTH_SHORT).show();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onImageChosen(final ChosenImage image) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				showProgress(false);
				if (image != null) {
					Log.d("onImageCHoosen", image.getFilePathOriginal());
					String filePathOriginal = image.getFilePathOriginal();
					File file = new File(filePathOriginal);

					selectedImage = Uri.fromFile(file);
					File parentDirectory = file.getParentFile();

					try {
						mPicturePath = parentDirectory.getAbsolutePath()+File.separator+"cropped.jpg";
						if(!performCrop(mPicturePath)) {
							Log.e("OnImageChoosen", "falling back to uncropped version :: no cropper found");
							mIntrstIMG.setImageURI(Uri.parse(new File(image
									.getFileThumbnail()).toString()));	
						}
					} catch (IOException e) {
						Log.e("OnImageChoosen", "falling back to uncropped version ::"+ e.getMessage());
						e.printStackTrace();
						mIntrstIMG.setImageURI(Uri.parse(new File(image
								.getFileThumbnail()).toString()));

					}

					//					imageViewThumbSmall.setImageURI(Uri.parse(new File(image
					//							.getFileThumbnailSmall()).toString()));
				}
			}
		});
	}

	@Override
	public void onError(final String reason) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				showProgress(false);
				Toast.makeText(CreateInterestActivity.this, reason,
						Toast.LENGTH_LONG).show();
			}
		});
	}

	// Should be called if for some reason the ImageChooserManager is null (Due
	// to destroying of activity for low memory situations)
	private void reinitializeImageChooser() {
		imageChooserManager = new ImageChooserManager(this, chooserType,
				FileUtils.APP_FOLDER_NAME, true);
		imageChooserManager.setImageChooserListener(this);
		imageChooserManager.reinitialize(mPicturePath);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("chooser_type", chooserType);
		outState.putString("media_path", mPicturePath);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("chooser_type")) {
				chooserType = savedInstanceState.getInt("chooser_type");
			}

			if (savedInstanceState.containsKey("media_path")) {
				mPicturePath = savedInstanceState.getString("media_path");
			}
		}
		super.onRestoreInstanceState(savedInstanceState);
	}


	private boolean performCrop(String outputPath) throws IOException 
	{
		try {
			//call the standard crop action intent (the user device may not support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP"); 
			//indicate image type and Uri
			cropIntent.setDataAndType(selectedImage, "image/*");
			//set crop properties
			cropIntent.putExtra("crop", "true");
			//indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			//indicate output X and Y
			cropIntent.putExtra("outputX", 400);
			cropIntent.putExtra("outputY", 400);


			File file = new File(outputPath);
			if(file.exists()) {
				file.delete();
			}

			file.createNewFile();

			cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			//retrieve data on return
			cropIntent.putExtra("return-data", true);
			//start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, GastonConstant.CROP_PIC_INTENT_KEY);
			return true;
		}
		// respond to users whose devices do not support the crop action
		catch (ActivityNotFoundException anfe) 
		{
			// display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
			return false;
		}
	}

	public void setCreateInterestResponse(CreateInterestBean result) 
	{
		//Toast.makeText(this, result.getResponseString(), Toast.LENGTH_SHORT).show();

		if(result.getStatus()==1)
		{
			isCreateAppSuccess  = true;
			if(!isAppIsInBackground(CreateInterestActivity.this))
			{
				//Toast.makeText(this,"App in foreground", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(CreateInterestActivity.this,DashBoardActivity.class);
				startActivity(intent);
				finish();
			}
			else
			{
				//Toast.makeText(this,"App in background", Toast.LENGTH_SHORT).show();

			}
		}
	}

	//check app is on background or not


	private boolean isAppIsInBackground(Context context) 
	{
		boolean isInBackground = true;
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) 
		{
			List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
			for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) 
			{
				if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) 
				{
					for (String activeProcess : processInfo.pkgList) 
					{
						if (activeProcess.equals(context.getPackageName())) 
						{
							isInBackground = false;
						}
					}
				}
			}
		} else 
		{
			List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
			ComponentName componentInfo = taskInfo.get(0).topActivity;
			if (componentInfo.getPackageName().equals(context.getPackageName())) 
			{
				isInBackground = false;
			}
		}

		return isInBackground;
	}






	@Override
	public void onBackPressed() {

		Intent intent = new Intent(CreateInterestActivity.this,DashBoardActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onPause() 
	{

		super.onPause();
		//Toast.makeText(CreateInterestActivity.this, "onPause", Toast.LENGTH_SHORT).show();
	}


	@Override
	protected void onStop() {
		super.onStop();
		//Toast.makeText(CreateInterestActivity.this, "onStop", Toast.LENGTH_SHORT).show();

	}
	@Override
	protected void onResume() {
		super.onResume();
		//Toast.makeText(CreateInterestActivity.this, "onResume", Toast.LENGTH_SHORT).show();
		
		if(isCreateAppSuccess)
		{
			Intent intent = new Intent(CreateInterestActivity.this,DashBoardActivity.class);
			startActivity(intent);
			finish();
		}

	}

}
