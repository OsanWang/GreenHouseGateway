package com.greenhousegateway.databean;

import com.greenhousegateway.annotation.HttpAnnotation;
import com.greenhousegateway.util.Constants;

/**
 * 
 * @author RyanHu
 * @Description: 添加、删除探头用的bean
 * @date Mar 29, 2015 6:26:45 PM
 */
public final class RegDetectorBean extends BaseDataBean
{

	@HttpAnnotation
	public int gwid;
	@HttpAnnotation
	public String gwToken;
	@HttpAnnotation
	public String dmac;
	@HttpAnnotation
	public int action;
	@HttpAnnotation(HttpType = Constants.Http_RESPONSE)
	public int did;

}
