package com.greenhousegateway.databean;

import com.greenhousegateway.annotation.HttpAnnotation;
import com.greenhousegateway.util.Constants;

public final class GetDetectorListBean extends BaseDataBean
{
	/**
	 * 网关ID
	 */
	@HttpAnnotation
	public int gwid;

	/**
	 * 本次登录拿到的token
	 */
	@HttpAnnotation
	public String gwToken;

	/**
	 * 返回的探头列表
	 */
	@HttpAnnotation(HttpType = Constants.Http_RESPONSE)
	public DetectorBean[] detectorList;
}
