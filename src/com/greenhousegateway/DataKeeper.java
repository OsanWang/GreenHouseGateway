package com.greenhousegateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.greenhousegateway.databean.DetectorBean;
import com.greenhousegateway.databean.HardwareDataBean;
import com.greenhousegateway.util.Constants;

/**
 * 保存数据的一个dataKeeper
 * 
 * @author RyanHu
 * 
 */
public class DataKeeper
{
	//保存服务器下发的探头列表
	public static ArrayList<DetectorBean> dataKeeper_detectorList = new ArrayList<DetectorBean>();
//	
//	// key是网关id，List是数据
//	public static ArrayList<UploadDataBean> dataKeeper_hour = new ArrayList<UploadDataBean>();
//	
	//保存探头数据
	public static Map<String, List<HardwareDataBean>> detectorDataMap = new HashMap<String, List<HardwareDataBean>>();

//	static {
//		UploadDataBean databean = new UploadDataBean();
//		databean.humidity = 0;
//		databean.temperature = 0;
//		databean.beam = 0;
//		dataKeeper_hour.add(databean);
//	}
}
