package com.ryancat.greenhousegateway.databean;

import com.ryancat.greenhousegateway.annotation.HttpAnnotation;
import com.ryancat.greenhousegateway.util.Constants;
/**
 * 登陆接口传递的DataBean
 * @author RyanHu
 *
 */
public final class UploadDataBean extends BaseDataBean
{
	//网关唯一ID
	@HttpAnnotation
	public int gwid ;
	
	//网关token
	@HttpAnnotation
	public String gwToken ;
	//温度
	@HttpAnnotation
	public int temperature ;
	//湿度
	@HttpAnnotation
	public int humidity ;
	//光照
	@HttpAnnotation
	public int beam ;
}
