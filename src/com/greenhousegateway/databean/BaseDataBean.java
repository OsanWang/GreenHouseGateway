package com.greenhousegateway.databean;

import java.lang.reflect.Field;

import com.greenhousegateway.GreenHouseApplication;
import com.greenhousegateway.annotation.HttpAnnotation;
import com.greenhousegateway.util.Constants;

/**
 * 网络Bean的基类，应该包含有各种的透传参数
 * 具体的透传参数表应当参照接口定义文档
 * @author RyanHu
 *
 */
public class BaseDataBean 
{
	public BaseDataBean()
	{
		this.imei = GreenHouseApplication.IMEI;
		this.mac = GreenHouseApplication.Mac;
		this.ua = GreenHouseApplication.Ua;
		this.ver = GreenHouseApplication.Ver;
		this.sysVer = GreenHouseApplication.SysVer;
		this.ap = GreenHouseApplication.Ap;
	}
	
	//设备的IMEI唯一标识符
	@HttpAnnotation
	public String imei ;
	//设备的MAC地址，以WIFI MAC为主
	@HttpAnnotation
	public String mac ;
	//设备机型
	@HttpAnnotation
	public String ua;
	//设备接入点
	@HttpAnnotation
	public String ap;
	//客户端版本号
	@HttpAnnotation
	public String ver;
	//系统版本号
	@HttpAnnotation
	public String sysVer;
	//Http相应状态
	@HttpAnnotation(HttpType = Constants.Http_RESPONSE)
	public String status;
	
	@Override
	public String toString()
	{
		Field[] fields = this.getClass().getFields();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < fields.length; i++)
		{
			try
			{
				sb.append("**"+fields[i].getName()+"-->"+fields[i].get(this));
			} catch (IllegalAccessException e)
			{
				e.printStackTrace();
			} catch (IllegalArgumentException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
