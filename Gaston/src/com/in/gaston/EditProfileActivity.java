package com.in.gaston;

import java.io.File;
import java.io.IOException;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.in.gaston.apptypeface.AppTypeFace;
import com.in.gaston.asyntask.SignUpAsynTask;
import com.in.gaston.bean.RequestBean;
import com.in.gaston.bean.SignUpBean;
import com.in.gaston.constant.AppParserConstant;
import com.in.gaston.constant.AppPreferences;
import com.in.gaston.constant.GastonConstant;
import com.in.gaston.helper.FileUtils;
import com.in.gaston.imageloader.ImageLoader;
import com.in.gaston.network.NetworkStatus;
import com.in.gaston.view.CustomView;
import com.in.gaston.view.RoundedImageView;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

public class EditProfileActivity extends Activity implements OnClickListener, ImageChooserListener
{
	private EditText mUserFirstNameET,mUserLastNameET;
	private RoundedImageView mUserIMG;
	private RadioButton mMaleRB,mFemaleRB;
	private TextView mRegisterTV,mEmailTV;
	private int mSelectedRB=1,mImageType = 0,userTypeLogin = 0;
	private AppPreferences mAppPreferences;
	private StringBody facebookIdStringBody;
	private ContentBody mUserImage;
	private AppTypeFace mAppTypeFace;
	private Uri selectedImage;
	private ImageChooserManager imageChooserManager;
	private String mPicturePath="";
	private int chooserType;
	private ProgressDialog pbar;
	private EditText mAddDesc;
	private LinearLayout mAboutYouLL;
	private String mUserImageSTR = "";
	private String mUserFirstNameSTR = "",mUserLastNameSTR="";
	private String mUserGenderSTR;
	private String mUserDescSTR="";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		getDataFromIntent();
		initView();
		initVariables();
	}
	private void getDataFromIntent() 
	{
		Intent intent = 	getIntent();
		mUserImageSTR = 	intent.getStringExtra("user_profile_image");
		mUserFirstNameSTR = intent.getStringExtra("user_first_name");
		mUserLastNameSTR = intent.getStringExtra("user_last_name");
		mUserGenderSTR = intent.getStringExtra("user_gender");
		mUserDescSTR = intent.getStringExtra("user_desc");

	}
	private void initView() 
	{
		mAppPreferences = AppPreferences.getInstance(EditProfileActivity.this);
		mAppTypeFace = new AppTypeFace(EditProfileActivity.this);
		mAboutYouLL =  (LinearLayout)findViewById(R.id.ll_about_you);
		TextView tv_edit_text =  (TextView) findViewById(R.id.tv_edit_text);
		TextView gender  = (TextView) findViewById(R.id.tv_user_gender);
		mAddDesc =  (EditText)findViewById(R.id.et_add_desc);
		mUserIMG = (RoundedImageView) findViewById(R.id.iv_user_image);
		mUserFirstNameET	= (EditText) findViewById(R.id.et_first_name);
		mUserLastNameET = (EditText) findViewById(R.id.et_last_name);
		mEmailTV = (TextView) findViewById(R.id.tv_email);
		mMaleRB = (RadioButton) findViewById(R.id.rb_male);
		mFemaleRB = (RadioButton) findViewById(R.id.rb_female);
		mRegisterTV = (TextView) findViewById(R.id.btn_register);

		// typeface
		tv_edit_text.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());
		mUserFirstNameET.setTypeface(mAppTypeFace.getTypeRobotoLight());
		mUserLastNameET.setTypeface(mAppTypeFace.getTypeRobotoLight());
		mEmailTV.setTypeface(mAppTypeFace.getTypeRobotoLight());
		mAddDesc.setTypeface(mAppTypeFace.getTypeRobotoLight());
		mMaleRB.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());
		mFemaleRB.setTypeface(mAppTypeFace.getTypeRoboto_REGULAR());
		gender.setTypeface(mAppTypeFace.getTypeRobotoLight());
		mRegisterTV.setTypeface(mAppTypeFace.getTypeRoboto_BOLD());

		mUserFirstNameET.setText(mUserFirstNameSTR);
		mUserLastNameET.setText(mUserLastNameSTR);

		mAddDesc.setText(mUserDescSTR);

		if(mUserGenderSTR.equalsIgnoreCase("1"))
		{
			mMaleRB.setChecked(true);
			mFemaleRB.setChecked(false);	
		}
		else
		{
			mMaleRB.setChecked(false);
			mFemaleRB.setChecked(true);	
		}
		mEmailTV.setText(mAppPreferences.getUserEmailId());
		/*
		 */
		findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				Intent output = new Intent();
				output.putExtra("save",0);
				setResult(RESULT_OK, output);
				finish();	
			}
		});


		mRegisterTV.setOnClickListener(this);
		mUserIMG.setOnClickListener(this);
	}

	private void initVariables() 
	{
		if(mUserImageSTR.equalsIgnoreCase(""))
		{
			mUserIMG.setImageResource(R.drawable.profileiconbig);
		}
		else
		{
			ImageLoader.getInstance(EditProfileActivity.this).displayImage(AppParserConstant.BASE_URL+mUserImageSTR, mUserIMG, false);
		}

	}

	@Override
	public void onClick(View view) 
	{
		switch (view.getId()) 
		{
		case R.id.btn_register:

			if(mMaleRB.isChecked())
			{
				mSelectedRB = 1;
			}
			else if (mFemaleRB.isChecked())
			{
				mSelectedRB = 2;
			}
			CustomView.hideSoftKeyboard(EditProfileActivity.this);
			if(validation(mUserFirstNameET.getText().toString(),mUserLastNameET.getText().toString()))
			{
				hitSignUpService();
			}

			break;

		case R.id.iv_user_image:

			showDialog();

			break;


		default:
			break;
		}
	}


	/**
	 * check validation
	 * @param emailId 
	 * @param string 
	 * @param string
	 */

	private boolean validation(String userFirstName, String userLastName) 
	{
		if(userFirstName.equals("")||userFirstName.length()<0)
		{

			Toast.makeText(EditProfileActivity.this, R.string.toast_user_first_name, Toast.LENGTH_SHORT).show();
			return false;

		}

		/*else if (userFirstName.contains(" ")) 
		{
			Toast.makeText(SignUpActivity.this, "No Spaces Allowed", 5000).show();
			return false;
		}*/
		else if (userLastName.equals("")||userLastName.length()<0) 
		{

			Toast.makeText(EditProfileActivity.this, R.string.toast_user_last_name, Toast.LENGTH_SHORT).show();
			return false;

		}

		/*	else if (userLastName.contains(" ")) 
		{
			Toast.makeText(SignUpActivity.this, "No Spaces Allowed", 5000).show();
			return false;
		}
		 */
		return true;
	}

	@SuppressLint("NewApi")
	private void hitSignUpService() 
	{
		/*
		 * user_fname  ,
		 *  user_lname , 
		 *  user_email,
		 *  user_gender , 
		 *  user_password , 
		 *  user_devicetoken , 
		 *  user_devicetype , 
		 *  user_location ,
		 *   user_lon , 
		 *   user_lat ,
		 *   user_image_type 

		 */
		RequestBean requestBean = new RequestBean();
		if(NetworkStatus.isInternetOn(EditProfileActivity.this))
		{
			try
			{

				MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
				try {
					StringBody descStringBody ;
					StringBody serviceAccessKeyStringBody = new StringBody(AppParserConstant.SERVICE_ACCESS_KEY_VAL);
					StringBody userIDStringBody = new StringBody(mAppPreferences.getUserId());
					StringBody methodeStringBody = new StringBody(AppParserConstant.METHOD_SIGN_UP);
					StringBody fisrtNameStringBody = new StringBody(mUserFirstNameET.getText().toString());
					StringBody lastNameStringBody = new StringBody(mUserLastNameET.getText().toString());
					StringBody emailStringBody = new StringBody("");
					StringBody pswdStringBody = new StringBody("");

					descStringBody = new StringBody(mAddDesc.getText().toString());
					StringBody genderStringBody = new StringBody(String.valueOf(mSelectedRB));
					StringBody deviceTokenStringBody = new StringBody(mAppPreferences.getDeviceToken());
					StringBody androidTypeStringBody = new StringBody(AppParserConstant.DEVICE_TYPE_VAL);
					StringBody profileTypeStringBody = new StringBody(mAppPreferences.getUserProfileStatus());

					if(mImageType==0)
					{
						facebookIdStringBody = new StringBody("");
					}
					else if(mImageType==1)
					{
						facebookIdStringBody = new StringBody(mAppPreferences.getFBId());
					}
					StringBody privateImageStringBody = new StringBody("");
					StringBody	imageTypeStringBody = new StringBody(String.valueOf(mImageType));
					if(userTypeLogin==0)
					{
						if (mPicturePath != null && mPicturePath.trim().length() > 0) 
						{
							File profilePicFile = new File(mPicturePath);
							if (mPicturePath.endsWith(".png")) {
								mUserImage = new FileBody(profilePicFile, GastonConstant.THUMBNAIL_IMAGE_PNG);
							} else if (mPicturePath.endsWith("jpg")) {
								mUserImage = new FileBody(profilePicFile,  GastonConstant.THUMBNAIL_IMAGE_JPG);
							} else if (mPicturePath.endsWith("jpeg")) {
								mUserImage = new FileBody(profilePicFile,  GastonConstant.THUMBNAIL_IMAGE_JPEG);
							}
							else{
								mUserImage = new FileBody(profilePicFile,  GastonConstant.THUMBNAIL_IMAGE_JPEG);
							}
							reqEntity.addPart(AppParserConstant.USER_IMAGE_KEY, mUserImage);
						}
					}
					else if (userTypeLogin==1)
					{
						StringBody userImageStringBody = new StringBody(mPicturePath);
						reqEntity.addPart(AppParserConstant.USER_IMAGE_KEY, userImageStringBody);
					}

					reqEntity.addPart("profile_type",profileTypeStringBody);
					reqEntity.addPart("description",descStringBody);
					reqEntity.addPart(AppParserConstant.USER_ID_KEY,userIDStringBody);
					reqEntity.addPart(AppParserConstant.FB_ID, facebookIdStringBody);
					reqEntity.addPart(AppParserConstant.SERVICE_ACCESS_KEY, serviceAccessKeyStringBody);
					reqEntity.addPart(AppParserConstant.METHOD_NAME_KEY, methodeStringBody);
					reqEntity.addPart(AppParserConstant.USER_FIRST_NAME_KEY, fisrtNameStringBody);
					reqEntity.addPart(AppParserConstant.USER_LAST_NAME_KEY, lastNameStringBody);
					reqEntity.addPart(AppParserConstant.USER_EMAIL_KEY, emailStringBody);
					reqEntity.addPart(AppParserConstant.USER_GENDER_KEY, genderStringBody);
					reqEntity.addPart(AppParserConstant.USER_PSWD_KEY, pswdStringBody);
					reqEntity.addPart(AppParserConstant.DEVICE_TOKEN_KEY, deviceTokenStringBody);
					reqEntity.addPart(AppParserConstant.DEVICE_TYPE_KEY, androidTypeStringBody);
					reqEntity.addPart(AppParserConstant.USER_IMAGE_TYPE_KEY, imageTypeStringBody);
					reqEntity.addPart(AppParserConstant.USER_PRIVATE_IMAGE_KEY, privateImageStringBody);

					requestBean.setUrl(AppParserConstant.HIT_URL);
					requestBean.setLoader(true);
					requestBean.setMultipartEntity(reqEntity);
					requestBean.setActivity(EditProfileActivity.this);
					SignUpAsynTask signUpAsynTask = new SignUpAsynTask(requestBean);

					if(VERSION.SDK_INT < VERSION_CODES.HONEYCOMB){
						signUpAsynTask.execute();
					}else{
						signUpAsynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");	
					}
				}

				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}catch(Exception e)
			{

			}

		}
		else
		{
			Toast.makeText(EditProfileActivity.this,getResources().getString(R.string.text_no_network), Toast.LENGTH_SHORT).show();

		}

	}




	public void showDialog()
	{
		final Button gallery;
		final Button camera;
		final Dialog dialog = new Dialog(EditProfileActivity.this, R.style.transparent_background_dialog);
		dialog.setContentView(R.layout.dialog_custom);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialog.getWindow().setAttributes(lp);
		gallery	= (Button) dialog.findViewById(R.id.btn_gallery);
		TextView text = (TextView) dialog.findViewById(R.id.tv_dialog_text);
		camera	= (Button) dialog.findViewById(R.id.btn_camera);
		dialog.show();
		text.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
		gallery.setTypeface(mAppTypeFace.getTypeRoboto_Medium());
		camera.setTypeface(mAppTypeFace.getTypeRoboto_Medium());

		gallery.setOnClickListener(new View.OnClickListener() 
		{

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
			pbar = new ProgressDialog(EditProfileActivity.this);
			pbar.setTitle(R.string.app_name);
			pbar.setMessage(getString(R.string.text_please_wait));
		}
		if((!pbar.isShowing()) && show) {
			pbar.show();
		}else {
			pbar.cancel();
		}
	}



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
					mUserIMG.setImageBitmap(decodeFile);
				}
				else
				{
					Toast.makeText(EditProfileActivity.this,R.string.text_no_resource_found, Toast.LENGTH_SHORT).show();
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
							mUserIMG.setImageURI(Uri.parse(new File(image
									.getFileThumbnail()).toString()));	
						}
					} catch (IOException e) {
						Log.e("OnImageChoosen", "falling back to uncropped version ::"+ e.getMessage());
						e.printStackTrace();
						mUserIMG.setImageURI(Uri.parse(new File(image
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
				Toast.makeText(EditProfileActivity.this, reason,
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
	public void setSignUpResponse(SignUpBean result) 
	{
		if(result!=null)
		{
			//Toast.makeText(EditProfileActivity.this,result.getResponseString(),Toast.LENGTH_SHORT).show();
			if(result.getStatus()==1)
			{

				mAppPreferences.setFirstName(mUserFirstNameET.getText().toString());
				mAppPreferences.setLastName(mUserLastNameET.getText().toString());
				mAppPreferences.setGender(String.valueOf(mSelectedRB));


				Intent output = new Intent();
				output.putExtra("save",1);
				setResult(RESULT_OK, output);
				finish();
			}

			else if(result.getStatus()==0)
			{
				//Toast.makeText(EditProfileActivity.this,result.getResponseString(),Toast.LENGTH_SHORT).show();

			}
		}
	}
	/**
	 * this function does the crop operation.
	 * @throws IOException 
	 */
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
	@Override
	public void onBackPressed() 
	{
		Intent output = new Intent();
		output.putExtra("save",0);
		setResult(RESULT_OK, output);
		finish();	

	}





}
