package com.in.gaston.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.net.ParseException;

public class NetworkConnection {

	public static String NetworkHitWithGET(String url_) 
	{
		String mResponce = null;
		String combinedParams = "";
		try 
		{
			// combinedParams += "/get?";
			//Log.e("Get Url", url_ + "" + combinedParams);
			HttpGet request = new HttpGet(url_ + combinedParams);
			HttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(request);
			//int responseCode = httpResponse.getStatusLine().getStatusCode();
			//String message = httpResponse.getStatusLine().getReasonPhrase();
			HttpEntity entity = httpResponse.getEntity();
			mResponce = EntityUtils.toString(entity);
		} 
		catch (Throwable e) 
		{
			e.printStackTrace();
			return null;
		}
		return mResponce;
	}

	public static String NetworkHitWithGET(String url_,List<NameValuePair> params) 
	{
		String mResponce = null;
		String combinedParams = "";
		try {
			if (!params.isEmpty()) {
				combinedParams += "/get?";
				for (NameValuePair p : params) {
					String paramString;
					paramString = p.getName() + "="+ URLEncoder.encode(p.getValue(), "UTF-8");
					if (combinedParams.length() > 1) {
						combinedParams += "&" + paramString;
					} else {
						combinedParams += paramString;
					}
				}
		//		Log.e("Get Url", url_ + "" + combinedParams);
				HttpGet request = new HttpGet(url_ + combinedParams);
				HttpClient client = new DefaultHttpClient();
				HttpResponse httpResponse = client.execute(request);
				//int responseCode = httpResponse.getStatusLine().getStatusCode();
				//String message = httpResponse.getStatusLine().getReasonPhrase();
				HttpEntity entity = httpResponse.getEntity();

				mResponce = EntityUtils.toString(entity);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
		return mResponce;
	}
	
/*	
 * 
 *  Delete Action With JSON Data
 *  
 * public static String networkHitDelete( String url, String token) {
		try {
			Logger.error("url is: "+url);
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
			DefaultHttpClient client = new DefaultHttpClient(httpParameters);
			HttpDelete post = new HttpDelete(url);

			post.setHeader("Accept", "application/json");
			post.setHeader("X-API-TOKEN", token);
			post.setHeader(HTTP.CONTENT_TYPE,
					"application/json");
			

			HttpResponse response = client.execute(post);
			// Checking response
			if (response != null) {
				InputStream inputStream = response.getEntity().getContent(); // Get
																				// the
																				// data
																				// in
																				// the
																				// entity
				String response_string = generateString1(inputStream);
				return response_string;
			} else {
				return null;
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}*/

	public static String NetworkHitWithGETLocation(String url_,List<NameValuePair> params) 
	{
		String mResponce = null;
		String combinedParams = "";
		try 
		{
			if (!params.isEmpty()) 
			{
				combinedParams += "/get?";
				for (NameValuePair p : params) {
					String paramString;
					paramString = p.getName() + "="
							+ URLEncoder.encode(p.getValue(), "UTF-8");

					if (combinedParams.length() > 1) {
						combinedParams += "&" + paramString;
					} else {
						combinedParams += paramString;
					}
				}
			//	Log.e("Get Url", url_ + "" + combinedParams);
				HttpGet request = new HttpGet(url_ + combinedParams);

				HttpClient client = new DefaultHttpClient();
				HttpResponse httpResponse = client.execute(request);
				//int responseCode = httpResponse.getStatusLine().getStatusCode();
				//String message = httpResponse.getStatusLine().getReasonPhrase();
				HttpEntity entity = httpResponse.getEntity();
				mResponce = EntityUtils.toString(entity);
			}
		} catch (Throwable e) 
		{
			e.printStackTrace();
			return null;
		}
		return mResponce;
	}

	public static String NetworkHitWithDelete(String url_,List<NameValuePair> params) {
		String mResponce = null;
		String combinedParams = "";
		try {
			if (!params.isEmpty()) {
				combinedParams += "?";
				for (NameValuePair p : params) {
					String paramString;
					paramString = p.getName() + "="+ URLEncoder.encode(p.getValue(), "UTF-8");

					if (combinedParams.length() > 1) {
						combinedParams += "&" + paramString;
					} else {
						combinedParams += paramString;
					}
				}
	//			Log.e("Get Url", url_ + "" + combinedParams);
				HttpGet request = new HttpGet(url_ + combinedParams);

				HttpClient client = new DefaultHttpClient();
				HttpResponse httpResponse = client.execute(request);
				//int responseCode = httpResponse.getStatusLine().getStatusCode();
				//String message = httpResponse.getStatusLine().getReasonPhrase();
				HttpEntity entity = httpResponse.getEntity();
				mResponce = EntityUtils.toString(entity);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
		return mResponce;
	}

	public String networkHit(List<NameValuePair> pairs, String url) 
	{
		String responseEntity = null;
		try 
		{
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
			DefaultHttpClient client = new DefaultHttpClient(httpParameters);
			HttpPost post = new HttpPost(url);
			//post.setHeader(BjpAppConstant.AUTH_KEY, BjpAppConstant.AUTH_KEY_VALUE);
			post.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
			HttpResponse response = client.execute(post);
			responseEntity = EntityUtils.toString(response.getEntity());
		} catch (Throwable e) {
			responseEntity = null;
			e.printStackTrace();
		}
		return responseEntity;
	}

	public String networkHitPut(JSONObject json, String url) 
	{
		try {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
			DefaultHttpClient client = new DefaultHttpClient(httpParameters);
			// HttpPost post= new HttpPost(url);
			HttpPut put = new HttpPut(url);
			StringEntity entity = new StringEntity(json.toString());
			BasicHeader basicHeader = 	new BasicHeader(HTTP.CONTENT_TYPE,"application/json");
			entity.setContentType(basicHeader);
			put.setEntity(entity);
			HttpResponse response = client.execute(put);
			// Checking response
			if (response != null) 
			{
				InputStream inputStream = response.getEntity().getContent();
				String response_string = generateString1(inputStream);
				return response_string;
			} 
			else 
			{
				return null;
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	public String networkHit(String url) {
		try {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
			DefaultHttpClient client = new DefaultHttpClient(httpParameters);
			HttpPost post = new HttpPost(url);
			StringEntity entity = new StringEntity("");
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			String jsonText = EntityUtils.toString(entity, HTTP.UTF_8);
			entity = new StringEntity(jsonText);
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			// Checking response
			if (response != null) {
				InputStream inputStream = response.getEntity().getContent();
				String response_string = generateString1(inputStream);
				return response_string;
			} else {
				return null;
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	public String networkHit(JSONObject json, String url) 
	{
		try {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
			DefaultHttpClient client = new DefaultHttpClient(httpParameters);
			HttpPost post = new HttpPost(url);
			StringEntity entity = new StringEntity(json.toString());
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
			String jsonText = EntityUtils.toString(entity, HTTP.UTF_8);
			entity = new StringEntity(jsonText);
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			// Checking response
			if (response != null) {
				InputStream inputStream = response.getEntity().getContent();
				String response_string = generateString1(inputStream);
				return response_string;
			} else {
				return null;
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String generateString1(InputStream stream) {
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader buffer = new BufferedReader(reader);
		StringBuilder sb = new StringBuilder();
		try {
			String cur;
			while ((cur = buffer.readLine()) != null) {
				sb.append(cur + "");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	
	
	public String hitNetwork(String url, MultipartEntity multipart) 
	{
		String responseEntity = null;
		url = url.trim();
		url = url.replace(" ", "%20");
		try {
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 30000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = 20000;
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
	}
	/*public String signup(String url, boolean edit, String imagepath,
			String first_name, String last_name, String email, String password,
			String address, String phone, String age, String type, String doc1,
			String doc2, String doc3, String doc4, String doc5,
			String deviceTokenString, String deviceGCM,String landline) {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		HttpPost post = new HttpPost(url);
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			ContentBody First_Name = new StringBody(first_name);
			ContentBody Last_Name = new StringBody(last_name);
			ContentBody Email = new StringBody(email);
			ContentBody Password = new StringBody(password);
			ContentBody Address = new StringBody(address);
			ContentBody Phone = new StringBody(phone);
			ContentBody Age = new StringBody(age);
			ContentBody Type = new StringBody(type);
			ContentBody DeviceToken = new StringBody(deviceTokenString);
			ContentBody security_key = new StringBody(NetworkConstant.SIGNUP_OAUTHKEY);
			ContentBody DeviceType = new StringBody(NetworkConstant.DEVICE_TYPE);
			ContentBody DeviceGCM = new StringBody(deviceGCM);
			
			ContentBody Landline = new StringBody(landline);

			reqEntity.addPart(NetworkConstant.FirstNameKey, First_Name);
			reqEntity.addPart(NetworkConstant.LastNameKey, Last_Name);
			reqEntity.addPart(NetworkConstant.EmailKey, Email);
			reqEntity.addPart(NetworkConstant.PasswordKey, Password);
			reqEntity.addPart(NetworkConstant.AddressKey, Address);
			reqEntity.addPart(NetworkConstant.ContactKey, Phone);
			reqEntity.addPart(NetworkConstant.AgeKey, Age);
			reqEntity.addPart(NetworkConstant.TypeKey, Type);
			reqEntity.addPart(NetworkConstant.DeviceKey, DeviceToken);
			reqEntity.addPart(NetworkConstant.OAuthKey, security_key);
			reqEntity.addPart(NetworkConstant.LandlineKey, Landline);
			
			if (!edit) {
				reqEntity.addPart("device_type", DeviceType);
				reqEntity.addPart("device_token", DeviceGCM);
			}
			if (!(imagepath.equals(null)) && imagepath.length() > 0) {
				if (imagepath.contains(".png") || imagepath.contains(".PNG")) {
					ContentBody user_pic = new FileBody(new File(imagepath),
							"image/png");
					reqEntity.addPart(NetworkConstant.UserImageKey, user_pic);
				} else {
					ContentBody user_pic = new FileBody(new File(imagepath),
							"image/jpg");
					reqEntity.addPart(NetworkConstant.UserImageKey, user_pic);
				}
			}

			if (!(doc1.equals(null)) && doc1.length() > 0) {
				if (doc1.contains(".png") || doc1.contains(".PNG")) {
					ContentBody user_pic = new FileBody(new File(doc1),
							"image/png");
					reqEntity.addPart(NetworkConstant.DOC1KEY, user_pic);
				} else {
					ContentBody user_pic = new FileBody(new File(doc1),
							"image/jpg");
					reqEntity.addPart(NetworkConstant.DOC1KEY, user_pic);
				}
			}

			if (!(doc2.equals(null)) && doc2.length() > 0) {
				if (doc2.contains(".png") || doc2.contains(".PNG")) {
					ContentBody user_pic = new FileBody(new File(doc2),
							"image/png");
					reqEntity.addPart(NetworkConstant.DOC2KEY, user_pic);
				} else {
					ContentBody user_pic = new FileBody(new File(doc2),
							"image/jpg");
					reqEntity.addPart(NetworkConstant.DOC2KEY, user_pic);
				}
			}

			if (!(doc3.equals(null)) && doc3.length() > 0) {
				if (doc3.contains(".png") || doc3.contains(".PNG")) {
					ContentBody user_pic = new FileBody(new File(doc3),
							"image/png");
					reqEntity.addPart(NetworkConstant.DOC3KEY, user_pic);
				} else {
					ContentBody user_pic = new FileBody(new File(doc3),
							"image/jpg");
					reqEntity.addPart(NetworkConstant.DOC3KEY, user_pic);
				}
			}

			if (!(doc4.equals(null)) && doc4.length() > 0) {
				if (doc4.contains(".png") || doc4.contains(".PNG")) {
					ContentBody user_pic = new FileBody(new File(doc4),
							"image/png");
					reqEntity.addPart(NetworkConstant.DOC4KEY, user_pic);
				} else {
					ContentBody user_pic = new FileBody(new File(doc4),
							"image/jpg");
					reqEntity.addPart(NetworkConstant.DOC4KEY, user_pic);
				}
			}

			if (!(doc5.equals(null)) && doc5.length() > 0) {
				if (doc5.contains(".png") || doc5.contains(".PNG")) {
					ContentBody user_pic = new FileBody(new File(doc5),
							"image/png");
					reqEntity.addPart(NetworkConstant.DOC5KEY, user_pic);
				} else {
					ContentBody user_pic = new FileBody(new File(doc5),
							"image/jpg");
					reqEntity.addPart(NetworkConstant.DOC5KEY, user_pic);
				}
			}
			
			 * 
			 * else if (imagepath.contains(".jpg")|| imagepath.contains(".JPG"))
			 * { ContentBody user_pic = new FileBody(new File(imagepath),
			 * "image/jpg"); reqEntity.addPart(NetworkConstant.UserImageKey,
			 * user_pic); }
			 
//			java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream((int) reqEntity.getContentLength());
//			reqEntity.writeTo(out);
//			String entityContentAsString = new String(out.toByteArray());
//			Log.e("multipartEntitty::::  ", "" + entityContentAsString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		post.setEntity(reqEntity);
		HttpResponse response;
		String response_edit = "";
		try {
			response = client.execute(post);
			HttpEntity resEntity = response.getEntity();
			response_edit = EntityUtils.toString(resEntity);
			return response_edit;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
*/
	/*public String updateProfile(String url, String imagepath, String userid,
			String first_name, String last_name, String address, String phone,
			String type, String role, String deviceTokenString, String Oauth,String landline) {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		HttpPost post = new HttpPost(url);
		MultipartEntity reqEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			ContentBody UserID = new StringBody(userid);
			ContentBody First_Name = new StringBody(first_name);
			ContentBody Last_Name = new StringBody(last_name);
			ContentBody Address = new StringBody(address);
			ContentBody Phone = new StringBody(phone);
			ContentBody Type = new StringBody(type);
			ContentBody DeviceToken = new StringBody(deviceTokenString);
			ContentBody oAuthkey = new StringBody(Oauth);
			ContentBody Role = new StringBody(role);
			
			ContentBody Landline = new StringBody(landline);

			reqEntity.addPart(NetworkConstant.UserIDKey, UserID);
			reqEntity.addPart(NetworkConstant.FirstNameKey, First_Name);
			reqEntity.addPart(NetworkConstant.LastNameKey, Last_Name);
			reqEntity.addPart(NetworkConstant.AddressKey, Address);
			reqEntity.addPart(NetworkConstant.ContactKey, Phone);
			reqEntity.addPart(NetworkConstant.TypeKey, Type);
			reqEntity.addPart(NetworkConstant.DeviceKey, DeviceToken);
			reqEntity.addPart(NetworkConstant.OAuthKey, oAuthkey);
			reqEntity.addPart("role", Role);
			reqEntity.addPart(NetworkConstant.LandlineKey, Landline);

			if (!(imagepath.equals(null)) && imagepath.length() > 0) {
				if (imagepath.contains(".png") || imagepath.contains(".PNG")) {
					ContentBody user_pic = new FileBody(new File(imagepath),
							"image/png");
					reqEntity.addPart(NetworkConstant.UserImageKey, user_pic);
				} else {
					ContentBody user_pic = new FileBody(new File(imagepath),
							"image/jpg");
					reqEntity.addPart(NetworkConstant.UserImageKey, user_pic);
				}
			}

			
			 * else if (imagepath.contains(".jpg")|| imagepath.contains(".JPG"))
			 * { ContentBody user_pic = new FileBody(new File(imagepath),
			 * "image/jpg"); reqEntity.addPart(NetworkConstant.UserImageKey,
			 * user_pic); }
			 
//			java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream(
//					(int) reqEntity.getContentLength());
//			reqEntity.writeTo(out);
//			String entityContentAsString = new String(out.toByteArray());
//			Log.e("multipartEntitty::::  ", "" + entityContentAsString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		post.setEntity(reqEntity);
		HttpResponse response;
		String response_edit = "";
		try {
			response = client.execute(post);
			HttpEntity resEntity = response.getEntity();
			response_edit = EntityUtils.toString(resEntity);
			return response_edit;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}*/

	/*public String AddDocImage(String url, String imagepath, String userid,
			String role, String deviceTokenString, String Oauth) {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		HttpPost post = new HttpPost(url);
		MultipartEntity reqEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			ContentBody UserId = new StringBody(userid);
			ContentBody DeviceToken = new StringBody(deviceTokenString);
			ContentBody oAuthkey = new StringBody(Oauth);
			ContentBody Role = new StringBody(role);

			reqEntity.addPart(NetworkConstant.UserIDKey, UserId);
			reqEntity.addPart(NetworkConstant.DeviceKey, DeviceToken);
			reqEntity.addPart(NetworkConstant.OAuthKey, oAuthkey);
			reqEntity.addPart("role", Role);

			if (!(imagepath.equals(null)) && imagepath.length() > 0) {
				if (imagepath.contains(".png") || imagepath.contains(".PNG")) {
					ContentBody user_pic = new FileBody(new File(imagepath),
							"image/png");
					reqEntity.addPart("prop_img", user_pic);
				} else {
					ContentBody user_pic = new FileBody(new File(imagepath),
							"image/jpg");
					reqEntity.addPart("prop_img", user_pic);
				}
			}
*/
			/*
			 * else if (imagepath.contains(".jpg")|| imagepath.contains(".JPG"))
			 * { ContentBody user_pic = new FileBody(new File(imagepath),
			 * "image/jpg"); reqEntity.addPart(NetworkConstant.UserImageKey,
			 * user_pic); }
			 */
//			java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream(
//					(int) reqEntity.getContentLength());
//			reqEntity.writeTo(out);
//			String entityContentAsString = new String(out.toByteArray());
//			Log.e("multipartEntitty::::  ", "" + entityContentAsString);
		/*} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		post.setEntity(reqEntity);
		HttpResponse response;
		String response_edit = "";
		try {
			response = client.execute(post);
			HttpEntity resEntity = response.getEntity();
			response_edit = EntityUtils.toString(resEntity);
			return response_edit;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
*/
	/*public String sendChatMessage(String url,
			String userid,
			String Oauth,
			String deviceTokenString,
			String msg_id,
			String body,
			String msg_type,
			String media, 
			String video_thumb,
			String file_name) {

		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		HttpPost post = new HttpPost(url);
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			ContentBody UserID = new StringBody(userid);
			ContentBody oAuthkey = new StringBody(Oauth);
			ContentBody DeviceToken = new StringBody(deviceTokenString);
			ContentBody Msg_ID 	= new StringBody(msg_id);
			ContentBody Message = new StringBody(body);
			ContentBody Msg_Type = new StringBody(msg_type);
			ContentBody File_Name = new StringBody(file_name);

			reqEntity.addPart(NetworkConstant.UserIDKey, UserID);
			reqEntity.addPart(NetworkConstant.DeviceKey, DeviceToken);
			reqEntity.addPart(NetworkConstant.OAuthKey, oAuthkey);
			reqEntity.addPart(NetworkConstant.MessageIDKey, Msg_ID);
			reqEntity.addPart("file_text", File_Name);
			reqEntity.addPart("body", Message);
			reqEntity.addPart("message_type", Msg_Type);

			// 1->text,2->image,3->doc,4->pdf,5->video
			if(msg_type.equalsIgnoreCase("2")) // IMAGE
			{
				if (!(media.equals(null)) && media.length() > 0) {
					if (media.contains(".png") || media.contains(".PNG")) {
						ContentBody user_pic = new FileBody(new File(media),"image/png");
						reqEntity.addPart("message_image", user_pic);
					} else {
						ContentBody user_pic = new FileBody(new File(media),"image/jpg");
						reqEntity.addPart("message_image", user_pic);
					}
				}
			}
			else if(msg_type.equalsIgnoreCase("3")) // DOC
			{
				if (!(media.equals(null)) && media.length() > 0) {
					File file = new File(media);
					ContentBody image = new FileBody(file, "application/*");
					reqEntity.addPart("message_image", image);
				}
			}
			else if(msg_type.equalsIgnoreCase("4")) // PDF
			{
				if (!(media.equals(null)) && media.length() > 0) {
					File file = new File(media);
					ContentBody image = new FileBody(file, "application/*");
					reqEntity.addPart("message_image", image);
				}
			}
			else if(msg_type.equalsIgnoreCase("5")) // VIDEO
			{
				if (!(media.equals(null)) && media.length() > 0) {
					if (media.contains(".mp4") || media.contains(".MP4")) {
						ContentBody user_pic = new FileBody( new File(media), "video/mp4");
						reqEntity.addPart("message_image", user_pic);
					} else {
						ContentBody user_pic = new FileBody( new File(media), "video/mp4");
						reqEntity.addPart("message_image", user_pic);
					}
				}

				if (!(video_thumb.equals(null)) && video_thumb.length() > 0) {
					if (media.contains(".png") || media.contains(".PNG")) {
						ContentBody user_pic = new FileBody(new File(video_thumb),"image/png");
						reqEntity.addPart("video_thumb", user_pic);
					} else {
						ContentBody user_pic = new FileBody(new File(video_thumb),"image/jpg");
						reqEntity.addPart("video_thumb", user_pic);
					}
				}
			}

			
			 * else if (imagepath.contains(".jpg")|| imagepath.contains(".JPG"))
			 * { ContentBody user_pic = new FileBody(new File(imagepath),
			 * "image/jpg"); reqEntity.addPart(NetworkConstant.UserImageKey,
			 * user_pic); }
			 

			java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream(
			(int) reqEntity.getContentLength());
			reqEntity.writeTo(out);
			String entityContentAsString = new String(out.toByteArray());
			Log.e("multipartEntitty::::  ", "" + entityContentAsString);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		post.setEntity(reqEntity);
		HttpResponse response;
		String response_edit = "";
		try {
			response = client.execute(post);
			HttpEntity resEntity = response.getEntity();
			response_edit = EntityUtils.toString(resEntity);
			return response_edit;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}*/
}