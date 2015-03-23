package com.greenhousegateway.databean;
import com.greenhousegateway.annotation.HttpAnnotation;


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
	public double temperature ;
	//湿度
	@HttpAnnotation
	public double humidity ;
	//光照
	@HttpAnnotation
	public double beam ;
	//上传日志时间
	@HttpAnnotation
	public long logTime;
}