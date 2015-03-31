package com.greenhousegateway.controller;

public interface TaskConstants
{
	public final static int TASK_START = 1;
	public final static int TASK_PROCESS = 2;
	public final static int TASK_FINISH = 3;
	
	public final static int TASK_SUCCESS = 0;
	public final static int TASK_FAILED = -1;
	public final static int CREATE_SERIAL_FAILED = -11;
	public final static int OPEN_SERIAL_FAILED = -12;

	public final static int USER_REGISTER_TASK = 10;
	public final static int USER_LOGIN_TASK = 11;
	public final static int ADD_GATEWAY_TASK = 12;
	public final static int DEL_GATEWAY_TASK = 13;
	public final static int SHOW_GATEWAY_TASK = 14;
	public final static int START_MQTT_TASK = 15;
	public final static int REQUEST_DETECTORLIST_TASK =16;
	
	public final static int GATEWAY_LOGIN_TASK = 110;
	public final static int GATEWAY_UPLOAD_TASK = 111;
	public final static int GATEWAY_READHARDWARE_TASK = 112;
	public final static int GET_SERVER_TIME_TASK = 113;
	public final static int GET_DETECTORLIST_TASK = 114;
	public final static int ADD_DETECTOR_TASK= 115;
}
