package com.in.gaston.view;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.in.gaston.R;
import com.in.gaston.SignUpActivity;
import com.in.gaston.constant.GastonConstant;
import com.in.gaston.imageloader.ImageLoader;

public class CustomView 
{
	private Context mContext;
	private static int count = 0;
	public static Uri outputFileUri;
	public Activity mActivity;
	public CustomView(Context context,Activity activity)
	{
		mActivity = activity;
		mContext = context;
	}
	public void showDialog()
	{
		Button gallery,camera;
		final Dialog dialog = new Dialog(mContext);
		dialog.setContentView(R.layout.dialog_custom);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialog.getWindow().setAttributes(lp);
		gallery	= (Button) dialog.findViewById(R.id.btn_gallery);
		camera	= (Button) dialog.findViewById(R.id.btn_camera);
		dialog.show();

		gallery.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				dialog.dismiss();
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intent.setType("image/*");
				((SignUpActivity)mActivity).startActivityForResult(intent,GastonConstant.GALLERY_REQ_CODE_INTENT_KEY);			}
		});

		camera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) 
			{

				dialog.dismiss();
				createFile();
			}
		});

	}

	void createFile()
	{

		final String dir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/gastonFolder/"; 

		File fileDir = new File(dir);
		if(fileDir.exists())
		{
			fileDir.delete();
			fileDir.mkdir();
		}
		else
		{
			fileDir.mkdir();

		}


		final File myFile = new File(fileDir, "anshika_image" + ".jpg");

		if (!myFile.exists()) 
		{    
			try 
			{
				myFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 

		try
		{
			outputFileUri = Uri.fromFile(myFile);

			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
			cameraIntent.putExtra("return-data", true);

			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

			((SignUpActivity)mActivity).startActivityForResult(cameraIntent, GastonConstant.CAMERA_REQ_CODE_INTENT_KEY);
		}catch (ActivityNotFoundException anfe) {
			Toast toast = Toast.makeText(mActivity, "This device doesn't support the crop action!",
					Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	/*
	 * hide keyboard
	 */
	public static  void hideSoftKeyboard(Activity activity) 
	{
		View view = activity.getCurrentFocus();
		if(view!=null)
		{
			InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		}
	}

	/*private void generateKeyHash()
	{
		try {
		PackageInfo info = getPackageInfo("com.in.gaston",        
				PackageManager.GET_SIGNATURES);
		for (Signature signature : info.signatures) {
			MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(signature.toByteArray());
			String sign=Base64.encodeToString(md.digest(), Base64.DEFAULT);
			Log.e("hash_key", sign);
			Toast.makeText(mActivity,sign,Toast.LENGTH_LONG).show();
		}
	} catch (NameNotFoundException e) {
	} catch (NoSuchAlgorithmException e) {
	}}*/



	@SuppressLint("NewApi")
	public static String getPath(Context context, Uri uri) throws UnsupportedEncodingException {
		if( uri == null ) {
			return null;
		}

		String encode = URLDecoder.decode(uri.toString(), "UTF-8");
		uri = Uri.parse(encode);


		String[] projection = { MediaStore.Images.Media.DATA };

		Cursor cursor;
		if(Build.VERSION.SDK_INT >19)
		{
			// Will return "image:x*"
			String wholeID = DocumentsContract.getDocumentId(uri);
			// Split at colon, use second item in the array
			String id = wholeID.split(":")[1];
			// where id is equal to             
			String sel = MediaStore.Images.Media._ID + "=?";

			cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
					projection, sel, new String[]{ id }, null);
		}
		else
		{
			cursor = context.getContentResolver().query(uri, projection, null, null, null);
		}
		String path = null;
		try
		{
			int column_index = cursor
					.getColumnIndex(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			path = cursor.getString(column_index).toString();
			cursor.close();
		}
		catch(NullPointerException e) {

		}
		return path;
	}
/*	static public void zoom_image(Context context, String zoom_image)
	{
		final ImageView cross;
		final ImageView img_zoom;
		final Dialog dialog = new Dialog(context, R.style.transparent_background_dialog);
		dialog.setContentView(R.layout.dialog_zoom_image);
		dialog.setCanceledOnTouchOutside(true);

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialog.getWindow().setAttributes(lp);
		img_zoom	= (ImageView) dialog.findViewById(R.id.img_zoom);

		dialog.show();

		if(zoom_image.equalsIgnoreCase(""))
		{
			img_zoom.setImageResource(R.drawable.profileiconbig);
		}
		else
		{
			ImageLoader.getInstance(((Activity)context)).displayImage(zoom_image, img_zoom, false);
		}

	}*/
	
	
	/*
	 * web view to zoom image
	 * 
	 */
	static public void zoom_image(Context context, String fileUrl) 
	{
	     Dialog d = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
	    d.setCancelable(true);
	    WebView wv = new WebView(context);
	    wv.setLayoutParams(new LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT, android.widget.TableRow.LayoutParams.MATCH_PARENT));
	    wv.loadUrl(fileUrl);
	    wv.getSettings().setBuiltInZoomControls(true);
	    wv.getSettings().setSupportZoom(true);
	    d.setContentView(wv);
	    d.show();
	}
	
	
	
	static public void zoom_in_user_pic(Context context,String fileUrl)
	{
		Dialog dialog = new Dialog(context);
		dialog.getWindow().setBackgroundDrawableResource(R.color.c_transparent);
		dialog.setContentView(R.layout.dialog_zoom_image);
		dialog.setCancelable(true);
		ImageView zoom_in_image = (ImageView)dialog.findViewById(R.id.img_zoom);
		ImageLoader.getInstance((Activity)context).displayImage(fileUrl, zoom_in_image, false);
		dialog.show();
		
	}

}
