package com.in.gaston.helper;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

public class FileUtils {
	
		public static String getApplicationFolder()
		{
			String path = "";
			if (isSDCardAvailable())
			{
				path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+APP_FOLDER_NAME; 
			} else
			{
				path = Environment.getDataDirectory().getAbsolutePath()+File.separator+APP_FOLDER_NAME; 
			}
	
			File file = new File(path);
			if (!file.isDirectory())
			{
				file.mkdirs();
			}
	
			return path;
		}
	
		public static final String APP_FOLDER_NAME = "Gaston";
	
		public static boolean isSDCardAvailable()
		{
			return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? true : false;
		}
	
		
		
		 public static Bitmap decodeFile(String path) {
	
	         int orientation;
	
	            try {
	
	                if(path==null){
	
	                    return null;
	                }
	                // decode image size
	                BitmapFactory.Options o = new BitmapFactory.Options();
	                o.inJustDecodeBounds = true;
	
	                // Find the correct scale value. It should be the power of 2.
	                final int REQUIRED_SIZE = 70;
	                int width_tmp = o.outWidth, height_tmp = o.outHeight;
	                int scale = 4;
	                while (true) {
	                    if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
	                        break;
	                    width_tmp /= 2;
	                    height_tmp /= 2;
	                    scale++;
	                }
	                // decode with inSampleSize
	                BitmapFactory.Options o2 = new BitmapFactory.Options();
	                o2.inSampleSize=scale;
	                Bitmap bm = BitmapFactory.decodeFile(path,o2);
	
	
	                Bitmap bitmap = bm;
	
	                ExifInterface exif = new ExifInterface(path);
	                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
	                Log.e("orientation",""+orientation);
	                Matrix m=new Matrix();
	
	                if((orientation==3)){
	
	                m.postRotate(180);
	                m.postScale(bm.getWidth(), bm.getHeight());
	
	//              if(m.preRotate(90)){
	                Log.e("in orientation",""+orientation);
	
	                bitmap = Bitmap.createBitmap(bm, 0, 0,bm.getWidth(),bm.getHeight(), m, true);
	                return  bitmap;
	                }
	                else if(orientation==6){
	
	                 m.postRotate(90);
	
	                 Log.e("in orientation",""+orientation);
	
	                 bitmap = Bitmap.createBitmap(bm, 0, 0,bm.getWidth(),bm.getHeight(), m, true);
	                    return  bitmap;
	                }
	
	                else if(orientation==8){
	
	                 m.postRotate(270);
	
	                 Log.e("in orientation",""+orientation);
	
	                 bitmap = Bitmap.createBitmap(bm, 0, 0,bm.getWidth(),bm.getHeight(), m, true);
	                    return  bitmap;
	                }
	                return bitmap;
	            }
	            catch (Exception e) {
	            }
	            return null;
	        }
		
}

