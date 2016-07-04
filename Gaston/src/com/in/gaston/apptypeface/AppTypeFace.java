package com.in.gaston.apptypeface;

import android.content.Context;
import android.graphics.Typeface;

/**
 * @author Appstudioz This is singleton class for handling fonts (Typefaces) of
 *         app
 */
public  class AppTypeFace
{
	private Context mContext;
	private Typeface Roboto_Light,Roboto_REGULAR,Roboto_BOLD,Roboto_MED;

	/**
	 * method for context initialization
	 * 
	 * @param context
	 *            to create typefaces
	 */
	public AppTypeFace(Context context)
	{
		this.mContext = context;
	}

	public Typeface getTypeRobotoLight()
	{
		if (Roboto_Light == null)
		{
			Roboto_Light = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Light.ttf");
		}
		return Roboto_Light;
	}
	public Typeface getTypeRoboto_REGULAR()
	{
		if (Roboto_REGULAR == null)
		{
			Roboto_REGULAR = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Regular.ttf");
		}
		return Roboto_REGULAR;
	}
	public Typeface getTypeRoboto_BOLD()
	{
		if (Roboto_BOLD == null)
		{
			Roboto_BOLD = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Bold.ttf");
		}
		return Roboto_BOLD;
	}
	public Typeface getTypeRoboto_Medium()
	{
		if (Roboto_MED == null)
		{
			Roboto_MED = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Medium.ttf");
		}
		return Roboto_MED;
	}
	


}
