package com.greenhousegateway.databean;

import com.greenhousegateway.annotation.HttpAnnotation;
import com.greenhousegateway.util.Constants;

public final class GetServerTimeBean extends BaseDataBean
{
	// 网关ID
	@HttpAnnotation
	public int gwid;
	// 网关合法token
	@HttpAnnotation
	public String gwToken;
	//拿到的时间
	@HttpAnnotation(HttpType = Constants.Http_RESPONSE)
	public TimeBean time;
}
