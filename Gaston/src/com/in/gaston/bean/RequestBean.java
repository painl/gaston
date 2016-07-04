package com.in.gaston.bean;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONObject;

import android.app.Activity;

/**
 * Use this class for wrapping all request parameter
 * 
 * @param mJsonObject
 *            (complete patch data)
 * @return Context for sending local broadcast
 */
public class RequestBean
{

	private MultipartEntity multipartEntity;
	public MultipartEntity getMultipartEntity() {
		return multipartEntity;
	}

	public void setMultipartEntity(MultipartEntity multipartEntity) {
		this.multipartEntity = multipartEntity;
	}

	private List<NameValuePair> params;
	private Object callableObect;
	private JSONObject jsonObject;
	public JSONObject getJsonObject()
	{
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject)
	{
		this.jsonObject = jsonObject;
	}

	public Object getCallableObect()
	{
		return callableObect;
	}

	public void setCallableObect(Object callableObect)
	{
		this.callableObect = callableObect;
	}

	private boolean Loader;

	public List<NameValuePair> getParams()
	{
		return params;
	}

	public void setParams(List<NameValuePair> params)
	{
		this.params = params;
	}

	private Activity activity;
	private String url;

	public boolean isLoader()
	{
		return Loader;
	}

	public void setLoader(boolean loader)
	{
		Loader = loader;
	}

	public Activity getActivity()
	{
		return activity;
	}

	public void setActivity(Activity activity)
	{
		this.activity = activity;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

}
