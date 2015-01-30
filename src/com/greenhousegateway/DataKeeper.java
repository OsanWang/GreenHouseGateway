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
//	public static Map<Integer, ArrayList<UploadDataBean>> dataKeeper_day = new HashMap<Integer, ArrayList<UploadDataBean>>();
//	public static Map<Integer, ArrayList<UploadDataBean>> dataKeeper_week = new HashMap<Integer, ArrayList<UploadDataBean>>();

	static {
		UploadDataBean databean = new UploadDataBean();
		databean.humidity = 0;
		databean.temperature = 0;
		databean.beam = 0;
		
		dataKeeper_hour.add(databean);
	}
//	static
//	{
//		dataKeeper_day.put(0, new ArrayList<UploadDataBean>());
//		dataKeeper_day.put(1, new ArrayList<UploadDataBean>());
//		dataKeeper_day.put(2, new ArrayList<UploadDataBean>());
//		dataKeeper_day.put(3, new ArrayList<UploadDataBean>());
//		dataKeeper_day.put(4, new ArrayList<UploadDataBean>());
//		dataKeeper_day.put(5, new ArrayList<UploadDataBean>());
//		dataKeeper_day.put(6, new ArrayList<UploadDataBean>());
//		dataKeeper_day.put(7, new ArrayList<UploadDataBean>());
//		dataKeeper_day.put(8, new ArrayList<UploadDataBean>());
//		dataKeeper_day.put(9, new ArrayList<UploadDataBean>());
//		dataKeeper_day.put(10, new ArrayList<UploadDataBean>());
//		dataKeeper_day.put(11, new ArrayList<UploadDataBean>());
//		dataKeeper_day.put(12, new ArrayList<UploadDataBean>());
//		dataKeeper_day.put(13, new ArrayList<UploadDataBean>());
//		dataKeeper_day.put(14, new ArrayList<UploadDataBean>());
//		dataKeeper_day.put(15, new ArrayList<UploadDataBean>());
//		dataKeeper_day.put(16, new ArrayList<UploadDataBean>());
//		dataKeeper_day.put(17, new ArrayList<UploadDataBean>());
//		dataKeeper_day.put(18, new ArrayList<UploadDataBean>());
//		dataKeeper_day.put(19, new ArrayList<UploadDataBean>());
//
//		for (int i = 0; i < dataKeeper_day.size(); i++)
//		{
//			ArrayList<UploadDataBean> list = dataKeeper_day.get(i);
//			for (int j = 0; j < 24; j++)
//			{
//				if (list != null)
//				{
//					list.add(getUploadDataBean(i));
//
//				}
//			}
//		}
//
//		dataKeeper_week.put(0, new ArrayList<UploadDataBean>());
//		dataKeeper_week.put(1, new ArrayList<UploadDataBean>());
//		dataKeeper_week.put(2, new ArrayList<UploadDataBean>());
//		dataKeeper_week.put(3, new ArrayList<UploadDataBean>());
//		dataKeeper_week.put(4, new ArrayList<UploadDataBean>());
//		dataKeeper_week.put(5, new ArrayList<UploadDataBean>());
//		dataKeeper_week.put(6, new ArrayList<UploadDataBean>());
//		dataKeeper_week.put(7, new ArrayList<UploadDataBean>());
//		dataKeeper_week.put(8, new ArrayList<UploadDataBean>());
//		dataKeeper_week.put(9, new ArrayList<UploadDataBean>());
//		dataKeeper_week.put(10, new ArrayList<UploadDataBean>());
//		dataKeeper_week.put(11, new ArrayList<UploadDataBean>());
//		dataKeeper_week.put(12, new ArrayList<UploadDataBean>());
//		dataKeeper_week.put(13, new ArrayList<UploadDataBean>());
//		dataKeeper_week.put(14, new ArrayList<UploadDataBean>());
//		dataKeeper_week.put(15, new ArrayList<UploadDataBean>());
//		dataKeeper_week.put(16, new ArrayList<UploadDataBean>());
//		dataKeeper_week.put(17, new ArrayList<UploadDataBean>());
//		dataKeeper_week.put(18, new ArrayList<UploadDataBean>());
//		dataKeeper_week.put(19, new ArrayList<UploadDataBean>());
//
//		for (int i = 0; i < dataKeeper_week.size(); i++)
//		{
//			ArrayList<UploadDataBean> list = dataKeeper_week.get(i);
//			for (int j = 0; j < 7; j++)
//			{
//				if(list!=null)
//				{
//					list.add(getUploadDataBean(i));
//				}
//			}
//		}
//		dataKeeper_hour.put(0, new ArrayList<UploadDataBean>());
//		dataKeeper_hour.put(1, new ArrayList<UploadDataBean>());
//		dataKeeper_hour.put(2, new ArrayList<UploadDataBean>());
//		dataKeeper_hour.put(3, new ArrayList<UploadDataBean>());
//		dataKeeper_hour.put(4, new ArrayList<UploadDataBean>());
//		dataKeeper_hour.put(5, new ArrayList<UploadDataBean>());
//		dataKeeper_hour.put(6, new ArrayList<UploadDataBean>());
//		dataKeeper_hour.put(7, new ArrayList<UploadDataBean>());
//		dataKeeper_hour.put(8, new ArrayList<UploadDataBean>());
//		dataKeeper_hour.put(9, new ArrayList<UploadDataBean>());
//		dataKeeper_hour.put(10, new ArrayList<UploadDataBean>());
//		dataKeeper_hour.put(11, new ArrayList<UploadDataBean>());
//		dataKeeper_hour.put(12, new ArrayList<UploadDataBean>());
//		dataKeeper_hour.put(13, new ArrayList<UploadDataBean>());
//		dataKeeper_hour.put(14, new ArrayList<UploadDataBean>());
//		dataKeeper_hour.put(15, new ArrayList<UploadDataBean>());
//		dataKeeper_hour.put(16, new ArrayList<UploadDataBean>());
//		dataKeeper_hour.put(17, new ArrayList<UploadDataBean>());
//		dataKeeper_hour.put(18, new ArrayList<UploadDataBean>());
//		dataKeeper_hour.put(19, new ArrayList<UploadDataBean>());
//
//		for (int i = 0; i < dataKeeper_hour.size(); i++)
//		{
//			ArrayList<UploadDataBean> list = dataKeeper_hour.get(i);
//			for (int j = 0; j < 11; j++)
//			{
//				if(list!=null)
//				{
//					list.add(getUploadDataBean(i));
//				}
//			}
//		}
//	}
//
//	private static UploadDataBean getUploadDataBean(int gwid)
//	{
//		UploadDataBean bean = new UploadDataBean();
//		bean.gwid = gwid;
//		bean.beam = new Random().nextInt(Constants.BEAM_MAX);
//		bean.temperature = new Random().nextInt(Constants.TEMP_MAX);
//		bean.humidity = new Random().nextInt(Constants.HUMI_MAX);
//		return bean;
//	}
}
