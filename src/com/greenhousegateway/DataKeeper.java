package com.greenhousegateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.greenhousegateway.databean.UploadDataBean;
import com.greenhousegateway.util.Constants;

/**
 * 保存数据的一个dataKeeper
 * 
 * @author RyanHu
 * 
 */
public class DataKeeper
{
	// key是网关id，List是数据
	public static ArrayList<UploadDataBean> dataKeeper_hour = new ArrayList<UploadDataBean>();

	static {
		UploadDataBean databean = new UploadDataBean();
		databean.humidity = 0;
		databean.temperature = 0;
		databean.beam = 0;
		
		dataKeeper_hour.add(databean);
	}
}
