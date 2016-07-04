package com.in.gaston.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * this class is use to hit web service on behalf of method calling it choose
 * the way data will send to the server
 * 
 * @author anshika_kala
 */

public class NetworkCall {

	private static NetworkCall networkCall;

	public static NetworkCall getInstance(Context context) {
		if (networkCall == null) 
		{
			networkCall = new NetworkCall(context);
		}
		return networkCall;
	}

	@SuppressLint("CommitPrefEdits")
	private NetworkCall(Context context) 
	{

	}

	/**
	 * this method is use to hit web service
	 * 
	 * @param url
	 * @param jsonObject
	 * 
	 *            return String
	 */

	public String hitNetwork(String url, JSONObject jsonObject) {
		String responseEntity = null;
		url = url.trim();
		url = url.replace(" ", "%20");
		HttpClient client = new DefaultHttpClient();
		int timeoutConnection = 15000;
		HttpConnectionParams.setConnectionTimeout(client.getParams(), timeoutConnection);
		int timeoutSocket = 15000;
		HttpConnectionParams.setSoTimeout(client.getParams(), timeoutSocket);
		HttpResponse response;
		try {
			HttpPost post = new HttpPost(url);
			StringEntity entity = new StringEntity(jsonObject.toString());
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			post.setEntity(entity);
			response = client.execute(post);
			if (response != null) {
				InputStream inputStream = response.getEntity().getContent();
				/**
				 * Get the data in the entity
				 */
				String response_string = convertStreamToString(inputStream);
				return response_string;
			} else {
				return null;
			}

		} catch (UnsupportedEncodingException e) {
			responseEntity = "UnsupportedEncodingException";
			e.printStackTrace();
		} catch (ClientProtocolException e1) {
			responseEntity = "ClientProtocolException";
			e1.printStackTrace();
		} catch (IOException e) {
			responseEntity = "IOException";
			e.printStackTrace();
		} catch (ParseException e) {
			responseEntity = "ParseException";
			e.printStackTrace();
		}
		return responseEntity;
	}

	/**
	 * method for hitting webservice.
	 * 
	 * @param url
	 * @return response of webservice.
	 */

	public String hitNetwork(String url) {
		String responseEntity = null;
		url = url.trim();
		//Log.e("url", url);
		url = url.replace(" ", "%20");
		HttpClient client = new DefaultHttpClient();
		int timeoutConnection = 20000;
		HttpConnectionParams.setConnectionTimeout(client.getParams(), timeoutConnection);
		int timeoutSocket = 20000;
		HttpConnectionParams.setSoTimeout(client.getParams(), timeoutSocket); // Timeout
		// Limit
		HttpResponse response;
		try {
			HttpGet get = new HttpGet(url);
			response = client.execute(get);
			if (response != null) {
				InputStream inputStream = response.getEntity().getContent(); // Get
				// the
				// data
				// in
				// the
				// entity
				String response_string = convertStreamToString(inputStream);
				return response_string;
			} else {
				return null;
			}

		} catch (UnsupportedEncodingException e) {
			responseEntity = "UnsupportedEncodingException";
			e.printStackTrace();
		} catch (ClientProtocolException e1) {
			responseEntity = "ClientProtocolException";
			e1.printStackTrace();
		} catch (IOException e) {
			responseEntity = "IOException";
			e.printStackTrace();
		} catch (ParseException e) {
			responseEntity = "ParseException";
			e.printStackTrace();
		}
		return responseEntity;
	}

	/**
	 * this method is use to hit web service
	 * 
	 * @param url
	 * @param multipart
	 * 
	 *            return String
	 */

/*	public String hitNetwork(String url, MultipartEntity multipart) {
		String responseEntity = null;
		url = url.trim();
		Log.e("url", url);

		url = url.replace(" ", "%20");
		try {
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 30000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = 8000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			DefaultHttpClient client = new DefaultHttpClient(httpParameters);
			HttpPost post = new HttpPost(url);
			post.setEntity(multipart);

			HttpResponse response;
			response = client.execute(post);
			responseEntity = EntityUtils.toString(response.getEntity());

		} catch (UnsupportedEncodingException e) {
			responseEntity = "UnsupportedEncodingException";
			e.printStackTrace();
		} catch (ClientProtocolException e1) {
			responseEntity = "ClientProtocolException";
			e1.printStackTrace();
		} catch (IOException e) {
			responseEntity = "IOException";
			e.printStackTrace();
		} catch (ParseException e) {
			responseEntity = "ParseException";
			e.printStackTrace();
		}
		return responseEntity;
	}*/

	public static String networkHit(List<NameValuePair> pairs, String url) {
		String responseEntity = null;
		url = url.trim();
		url = url.replace(" ", "%20");
		try {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
			DefaultHttpClient client = new DefaultHttpClient(httpParameters);
			HttpPost post = new HttpPost(url);
			post.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
		//	Log.e("SERVER RESPONSE", "http post : " + post);
			HttpResponse response = client.execute(post);
			responseEntity = EntityUtils.toString(response.getEntity());
			responseEntity = responseEntity.replace("&amp;", "&").replace("<pre>", "").replace("&quot;", "").replace("<br>", "\n").replace("null", "0");

			//Log.e("SERVER RESPONSE", "SERVER RESPONSE : " + responseEntity);
		} catch (Throwable e) {
			responseEntity = "error";
			e.printStackTrace();
		}
		return responseEntity;
	}

	private String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			return "IOException";
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return sb.toString();
	}
}