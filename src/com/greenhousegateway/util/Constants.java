package com.greenhousegateway.util;

/**
 * 保存常量的，比如服务器地址，编码什么的。
 * 
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

	String Server_Address = "http://120.132.70.185:8080/dp/";
	String Login_Url = Server_Address + "gw/login.do?";// 登录地址
	String Upload_Url = Server_Address + "2.0/gw/upload.do?";// 上传数据地址
	String UpdateCriticalSuccess_Url = Server_Address + "2.0/gw/updateCriticalSuccess.do?";//网关确认修改临界值
	String RegDetector_Url = Server_Address+"2.0/gw/regDetector.do?";//注册或者删除一个探头
	String ServerTime_Url = Server_Address+"2.0/gw/time.do?";//获取时间
	String DetectorList_Url = Server_Address+"2.0/gw/detectorList.do?";//获取探头列表

	int TEMP_MAX = 100;
	int BEAM_MAX = 30000;
	int HUMI_MAX = 100;
	int TEMP_MIN = 0;
	int BEAM_MIN = 0;
	int HUMI_MIN = 0;
	int MINUTES = 60 *1000;
	int HOUR = 60*MINUTES;
	int DAY = 24 *HOUR;
	int WEEK = 7*DAY;
	
	//服务器联网相关
	
	String REQUEST_SERVER_RETURN_NULL = "未能从服务器获取到数据，请检查联网！";
}
