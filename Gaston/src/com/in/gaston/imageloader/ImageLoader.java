package com.in.gaston.imageloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

/**
 * This is a class which used to load the image to the imageview thorough url.
 * 
 * @author anshika_kala
 * 
 */

public class ImageLoader
{

	private static ImageLoader instance = null;
	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	private ExecutorService executorService;

	// final int stub_id=R.drawable.home_screen_default_pic;

	private ImageLoader(Activity context)
	{
		if (context == null)
			return;
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}

	public static ImageLoader getInstance(Activity context)
	{
		if (instance == null || context == null)
		{
			instance = new ImageLoader(context);
		}
		return instance;
	}

	// final int stub_id1=R.drawable.u_reward;
	public void displayImage(String url, ImageView imageView, boolean isReward)
	{
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else
		{
			queuePhoto(url, imageView);
			if (isReward)
			{
				// imageView.setImageResource(stub_id1);
			}
			else
			{
				// imageView.setImageResource(stub_id);
			}
		}
	}

	private void queuePhoto(String url, ImageView imageView)
	{
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	public Bitmap getBitmap(String url)
	{
		File f = fileCache.getFile(url);
		// from SD cache
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		// from web
		try
		{
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			Utils.CopyStream(is, os);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		}
		catch (Throwable ex)
		{
			//Log.e("image loader", "" + ex.toString());
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError)
				memoryCache.clear();
			return null;
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f)
	{
		try
		{
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 125;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true)
			{
				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		}
		catch (FileNotFoundException e)
		{
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad
	{
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i)
		{
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable
	{
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad)
		{
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run()
		{
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.url);
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad)
	{
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable
	{
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p)
		{
			bitmap = b;
			photoToLoad = p;
		}

		@Override
		public void run()
		{
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
			{
				photoToLoad.imageView.setImageBitmap(bitmap);
			}
			// else
			// photoToLoad.imageView.setImageResource(stub_id);
		}
	}

	public void clearCache()
	{
		memoryCache.clear();
		fileCache.clear();
	}

//	public static Bitmap getBitmapFromURL(String src)
//	{
//		try {
//			URL url = new URL(src);
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			connection.setDoInput(true);
//			connection.connect();
//			InputStream input = connection.getInputStream();
//			Bitmap myBitmap = BitmapFactory.decodeStream(input);
//
//			myBitmap = Bitmap.createScaledBitmap(myBitmap, 300, 300, true);
//			return myBitmap;
//		} catch (IOException e) {
//			// Log exception
//			return null;
//		}
//	}
	
}
