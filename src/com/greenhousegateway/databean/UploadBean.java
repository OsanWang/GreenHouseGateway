package com.greenhousegateway.databean;

import com.greenhousegateway.annotation.HttpAnnotation;

public class UploadBean extends BaseDataBean
{
	@HttpAnnotation
	public int gwid;
	@HttpAnnotation
	public String gwToken;
}
