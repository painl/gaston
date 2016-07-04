package com.in.gaston.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.Toast;

public class AppUtils 
{
	private Context mContext;
	public  AppUtils(Context context)
	{
		mContext = context;
	}
	
	
	
	//get file path from audio uri when delected from gallery on Api >=19 lolipop
	
	@SuppressLint("NewApi")
	public String get_file_from_uri_lolipop(Uri uri_data)
	{
		String wholeID = DocumentsContract.getDocumentId(uri_data);
		// Split at colon, use second item in the array
		String id = wholeID.split(":")[1];
		String[] column = { MediaStore.Audio.Media.DATA };     
		// where id is equal to             
		String sel = MediaStore.Audio.Media._ID + "=?";

		Cursor cursor = mContext. getContentResolver().
				query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, 
						column, sel, new String[]{ id }, null);
		String filePath = "";

		int columnIndex = cursor.getColumnIndex(column[0]);


		if(cursor.getCount()>0)
		{
			if (cursor.moveToFirst()) 
			{
				Toast.makeText(mContext,"Cursor Count --> "+cursor.getCount(), Toast.LENGTH_SHORT).show();
				filePath = cursor.getString(columnIndex);
			}   
		}
		else
		{
			Toast.makeText(mContext,"Cursor Count --> "+cursor.getCount(), Toast.LENGTH_SHORT).show();
		}
		cursor.close();
		return filePath;
	}


	
	
	//get file path from audio uri when delected from gallery on kiykat
	public String getRealPathFromURI1(Uri uri) 
	{
		int column_index = 0;
		String[] projection = { MediaStore.Audio.Media.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = mContext. getContentResolver().query(uri, projection, null, null, null);
		try
		{
			column_index = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
		}
		catch(IllegalArgumentException e)
		{
			Toast.makeText(mContext, e.getMessage()+"", Toast.LENGTH_LONG).show();
		}
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private String getRealPathFromURI(Uri contentURI) 
	{
		String result = null;
		Cursor cursor = mContext. getContentResolver().query(contentURI, null, null, null, null);
		((Activity) mContext).startManagingCursor(cursor);
		if (cursor == null) 
		{ 
			if(cursor.moveToNext())
			{

				result = contentURI.getPath();
			}
		} else 
		{ 

			if(cursor!=null)
			{
				cursor.moveToFirst(); 
				int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
				result = cursor.getString(idx);
				cursor.close();
			}
		}
		return result;
	}


}
