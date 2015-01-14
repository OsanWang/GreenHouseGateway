package com.ryancat.greenhousegateway.databean;

import com.ryancat.greenhousegateway.annotation.HttpAnnotation;
import com.ryancat.greenhousegateway.util.Constants;
/**
 * 登陆接口传递的DataBean
 * @author RyanHu
 *
 */
public final class LoginDataBean extends BaseDataBean
{
	//网关唯一ID
	@HttpAnnotation
	public int gwid ;
	
	//客户端当前合法token
	@HttpAnnotation(HttpType = Constants.Http_RESPONSE)
	public String gwToken ; 
	//下发客户端apk地址
	@HttpAnnotation(HttpType = Constants.Http_RESPONSE)
	public String clientApkUrl;
}
