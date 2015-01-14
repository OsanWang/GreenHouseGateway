package com.ryancat.greenhousegateway.util;

/**
 * 保存常量的，比如服务器地址，编码什么的。
 * @author RyanHu
 *
 */
public interface Constants
{
	
	
	String ENCODING = "utf-8";
	String HTTP_REQUEST = "Http_Request";
	String Http_RESPONSE = "Http_Response";
	String Status_Success = "success";

	
	
	String Server_Address = "http://yirenna.com:8081";
	String Login_Url =Server_Address+"/dp/gw/login.do?";//登录地址
	String Upload_Url = Server_Address+"/dp/gw/upload.do?";//上传数据地址
}
