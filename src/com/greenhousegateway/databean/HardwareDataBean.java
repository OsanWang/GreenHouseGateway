package com.greenhousegateway.databean;

import com.greenhousegateway.annotation.Coloumn;
import com.greenhousegateway.annotation.HttpAnnotation;

/**
 * 上传数据时用到的DataBean,从硬件读数据也是封装成这个
 * 
 * @author RyanHu
 * 
 */
public final class HardwareDataBean
{
	// 网关唯一ID
	@HttpAnnotation
	public int gwid;
	@Coloumn
	@HttpAnnotation
	public int did;
	@Coloumn
	@HttpAnnotation
	public String dmac;
	// 网关token
	@HttpAnnotation
	public String gwToken;
	// 温度
	@Coloumn
	@HttpAnnotation
	public double temperature;
	// 湿度
	@Coloumn
	@HttpAnnotation
	public double humidity;
	// 光照
	@Coloumn
	@HttpAnnotation
	public double beam;
	// 电量
	@Coloumn
	@HttpAnnotation
	public double power;
	// 是否已经送达
	@Coloumn
	public int delivered;
	
	@Coloumn
	public long logTime;
	@Override
	public String toString()
	{
		return "UploadDataBean [gwid=" + gwid + ", dmac=" + dmac + ", did=" + did + ", gwToken=" + gwToken + ", temperature=" + temperature + ", humidity=" + humidity + ", beam=" + beam + ", power=" + power + ", delivered=" + delivered + "]";
	}
	
	
}