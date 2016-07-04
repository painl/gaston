package com.in.gaston.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkStatus
{

	/**
	 * this method check that is internet connection availabele or not
	 * 
	 * @param nothing
	 * @return boolean
	 */
	public static boolean isInternetOn(Context context)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null)
		{
			NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected())
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isNetworkConnected(Context context)
	{
		boolean val = false;
		ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// ARE WE CONNECTED TO THE NET
		try
		{
			if (connec.getNetworkInfo(0) == null)
			{
				WifiManager wman = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wi = wman.getConnectionInfo();
				if (wi.getNetworkId() == -1)
				{
					return false;
				} else
				{
					return true;
				}
			} else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED || connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING
					|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING || connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED)
			{
				val = true;
			} else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED || connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED)
			{
				val = false;
			}
		} catch (Exception e)
		{

		}
		return val;

	}
}