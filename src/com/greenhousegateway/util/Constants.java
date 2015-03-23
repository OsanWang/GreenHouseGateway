package com.greenhousegateway.util;

/**
 * 保存常量的，比如服务器地址，编码什么的。
 * @author RyanHu
 */
public interface Constants
{
	String QRCODE_TYPE = "qrcode_type";
	int CLENT_APKPATH_QRCODE = 1;
	int GWID_QRCODE = 2;
	int LOGIN_SUCCESS = 99;
	
	String ENCODING = "utf-8";
	String HTTP_REQUEST = "Http_Request";
	String Http_RESPONSE = "Http_Response";
	String Status_Success = "success";

	
	String Server_Address = "http://120.132.70.185:8080";
	String Login_Url =Server_Address+"/dp/gw/login.do?";//登录地址
	String Upload_Url = Server_Address+"/dp/gw/upload.do?";//上传数据地址

	int TEMP_MAX =100;
	int BEAM_MAX =100;
	int HUMI_MAX =100;
	int TEMP_MIN = 0;
	int BEAM_MIN = 0;
	int HUMI_MIN = 0;
}
