package com.greenhousegateway.databean;

import com.greenhousegateway.annotation.HttpAnnotation;

/**
 * 上传数据时用到的DataBean,从硬件读数据也是封装成这个
 * 
 * @author RyanHu
 * 
 */
public final class UploadDataBean
{
	// 网关唯一ID
	@HttpAnnotation
	public int gwid;
	
	@HttpAnnotation
	public String dmac;

	@HttpAnnotation
	public int did;
	// 网关token
	@HttpAnnotation
	public String gwToken;
	// 温度
	@HttpAnnotation
	public double temperature;
	// 湿度
	@HttpAnnotation
	public double humidity;
	// 光照
	@HttpAnnotation
	public double beam;
	// 电量
	@HttpAnnotation
	public double power;
	// 是否已经送达
	public int delivered;
	@Override
	public String toString()
	{
		return "UploadDataBean [gwid=" + gwid + ", dmac=" + dmac + ", did=" + did + ", gwToken=" + gwToken + ", temperature=" + temperature + ", humidity=" + humidity + ", beam=" + beam + ", power=" + power + ", delivered=" + delivered + "]";
	}
	
	
}