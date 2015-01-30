package com.greenhousegateway.controller;

public interface TaskConstants
{
	public final static int TASK_SUCCESS = 0;
	public final static int TASK_FAILED = -1;

	public final static int USER_REGISTER_TASK = 10;
	public final static int USER_LOGIN_TASK = 11;
	public final static int ADD_GATEWAY_TASK = 12;
	public final static int DEL_GATEWAY_TASK = 12;
	public final static int SHOW_GATEWAY_TASK = 14;
	public final static int START_MQTT_TASK = 15;
	
	public final static int GATEWAY_LOGIN = 110;
	public final static int GATEWAY_UPLOAD = 111;
	public final static int GATEWAY_READHARDWARE = 112;
}
