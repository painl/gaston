package com.in.gaston.view;

import java.io.File;

import android.os.Environment;

public class FileUtils
{

	private static final String APP_FOLDER_NAME = "/gaston/";

	public static boolean isSDCardAvailable()
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? true : false;
	}

	public static String getApplicationFolder()
	{
		String path = "";
		if (isSDCardAvailable())
		{
			path = Environment.getExternalStorageDirectory() + APP_FOLDER_NAME;
		} else
		{
			path = Environment.DIRECTORY_PICTURES + APP_FOLDER_NAME;
		}

		File file = new File(path);
		if (!file.isDirectory())
		{
			file.mkdirs();
		}

		return path;
	}

}
