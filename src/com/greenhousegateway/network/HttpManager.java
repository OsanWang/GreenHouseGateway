package com.greenhousegateway.network;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.greenhousegateway.annotation.HttpAnnotation;
import com.greenhousegateway.databean.BaseDataBean;
import com.greenhousegateway.util.Constants;
import com.greenhousegateway.util.L;

public class HttpManager
{
	private HttpAdapter adapter;
	private static HttpManager sInstance = new HttpManager();

	private HttpManager()
	{
		adapter = new HttpAdapter();
	}

	public static HttpManager getInstance()
	{
		return sInstance;
	}

	/**
	 * 请求服务器
	 * @param url 地址
	 * @param requestDataBean 请求的dataBean
	 * @param isPost 是否为post
	 * @return
	 */
	public BaseDataBean requestServer(String url, BaseDataBean requestDataBean, boolean isPost)
	{
		Class clazz = requestDataBean.getClass();
		BaseDataBean resultDataBean = null;
		try
		{
			Map<String, Object> requestMap = new HashMap<String, Object>();
			Field[] fields = clazz.getFields();
			for (int i = 0; i < fields.length; i++)
			{
				fields[i].setAccessible(true);
				if ((fields[i].getAnnotation(HttpAnnotation.class) != null) && fields[i].getAnnotation(HttpAnnotation.class).HttpType().equals(Constants.HTTP_REQUEST))
				{
					requestMap.put(fields[i].getName(), fields[i].get(requestDataBean));
				}
			}
			String response = "";
			if (isPost)
			{
				response= httpPost(url, requestMap);
			} else
			{
				response = httpGet(url, requestMap);

			}
			L.d("rsp -->"+response);

			//这里不去new而是使用原有的dataBean减少开销
			resultDataBean = (BaseDataBean) clazz.newInstance();
			//在这里封装为gson
			Gson gson = new Gson();
			resultDataBean = gson.fromJson(response, clazz);
		}catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		if(resultDataBean!=null)
		{
			System.out.println(resultDataBean.toString());
		}
		return resultDataBean;
	}

	private String httpGet(String url, Map<String, Object> param)
	{
		return adapter.get(url, param);
	}

	private String httpPost(String url, Map<String, Object> param)
	{
		return adapter.post(url, param);
	}
}
