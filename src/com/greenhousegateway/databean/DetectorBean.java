package com.greenhousegateway.databean;

import com.greenhousegateway.annotation.HttpAnnotation;

/**
 * 最基本的探头Bean
 * @author RyanHu
 *
 */
public class DetectorBean
{
	//mac
	@HttpAnnotation
	public String mac;
	//温度
	@HttpAnnotation
	public double temperature ;
	//湿度
	@HttpAnnotation
	public double humidity ;
	//光照
	@HttpAnnotation
	public double beam ;
}
