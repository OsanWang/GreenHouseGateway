package com.greenhousegateway.databean;

import com.greenhousegateway.annotation.HttpAnnotation;

/**
 * 最基本的探头Bean，用在解析服务器下发探头列表时使用
 * 
 * @author RyanHu
 * 
 */
public final class DetectorBean
{
	// 探头id
	@HttpAnnotation
	public int did;
	// 探头mac
	@HttpAnnotation
	public String dmac;
	// 温度最大临界值
	@HttpAnnotation
	public double maxTemperature;
	// 温度最小临界值
	@HttpAnnotation
	public double minTemperature;
	// 湿度最大临界值
	@HttpAnnotation
	public double maxHumidity;
	// 湿度最小临界值
	@HttpAnnotation
	public double minHumidity;
	// 光照最大值
	@HttpAnnotation
	public double maxBeam;
	// 光照最小值
	@HttpAnnotation
	public double minBeam;
}
