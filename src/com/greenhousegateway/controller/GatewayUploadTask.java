package com.greenhousegateway.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.greenhousegateway.GreenHouseApplication;
import com.greenhousegateway.databean.Detector4UploadDataBean;
import com.greenhousegateway.databean.LoginDataBean;
import com.greenhousegateway.databean.UploadBean;
import com.greenhousegateway.databean.HardwareDataBean;
import com.greenhousegateway.databean.UploadListBean;
import com.greenhousegateway.network.HttpManager;
import com.greenhousegateway.util.Constants;
import com.greenhousegateway.util.L;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * 网关上传数据的task
 * 
 * @author RyanHu
 * 
 */
final class GatewayUploadTask extends BaseTask
{

	private static final int TASK_TAG = TaskConstants.GATEWAY_UPLOAD_TASK;
	private List<HardwareDataBean> uploadDataBean;

	public GatewayUploadTask(Handler handler, Context _context, List<HardwareDataBean> _uploadDataBeans)
	{
		super(handler, _context);
		uploadDataBean = _uploadDataBeans;
	}

	@Override
	public void run()
	{
		L.d("即将上传" + uploadDataBean.size() + "条数据");
		UploadBean uploadBean = new UploadBean();
		uploadBean.gwid = GreenHouseApplication.gwid;
		uploadBean.gwToken = GreenHouseApplication.gwToken;


		String json = UploadList2Json();

		// TODO
		// 这里差一个转化，把基本的数据库每条DataBean通过时间作为判断，然后组包，转成JSON
		uploadBean = (UploadBean)httpManager.requestServer(Constants.Upload_Url, uploadBean, true,json);
		if (uploadBean == null)
		{
			L.e("没有从服务器拿到数据");
			sendResultMessage(TASK_TAG, "上传数据失败！", TaskConstants.TASK_SUCCESS, 0);
			return;
		}
		if (uploadBean.status.equals(Constants.Status_Success))
		{
			sendResultMessage(TASK_TAG, "上传数据成功！", TaskConstants.TASK_SUCCESS, 0);
			L.d("上传成功");
		} else
		{
			sendResultMessage(TASK_TAG, "上传数据成功！", TaskConstants.TASK_FAILED, 0);
			L.d("上传失败");
		}
	}

	/**
	 * 把待上传的数据转换为json
	 * 
	 * @return
	 */
	private String UploadList2Json()
	{
		String json = "";
		//封装进JSON的bean
		UploadListBean uploadListBean = new UploadListBean();
		// 即将上传的数据，以时间做区分，装入这个Map
		Map<Long, ArrayList<HardwareDataBean>> data_map = new HashMap<Long, ArrayList<HardwareDataBean>>();
		Iterator<HardwareDataBean> it = uploadDataBean.iterator();// 所有的数据迭代出来
		long data_current_time = 0;// 以时间为记录，封装各个硬件数据到detectorDatas里面
		while (it.hasNext())
		{
			HardwareDataBean uploadDataBean = it.next();
			// 开始转化当前BEAN的时间
			long thisbean_logtime = 0;
			String str_log_time = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date(uploadDataBean.logTime));
			try
			{
				thisbean_logtime = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").parse(str_log_time).getTime();
			} catch (ParseException e)
			{
				L.e("日期转换错误~~");
				e.printStackTrace();
			}
			// 转化时间结束
			ArrayList<HardwareDataBean> t_list = data_map.get(thisbean_logtime);
			if (t_list == null)
			{
				// dataMap里面没有当前时间节点,new一个List
				t_list = new ArrayList<HardwareDataBean>();
			}
			t_list.add(uploadDataBean);
			data_map.put(thisbean_logtime, t_list);
		}
		L.d("封装上传数据进MAP结束~~~~");
		Set<Long> keySet = data_map.keySet();
		uploadListBean.dataList = new Detector4UploadDataBean[keySet.size()];
		Iterator<Long>  it_list = keySet.iterator();
		int count = 0;
		while (it_list.hasNext())
		{
			
			Long logTime  =  it_list.next();
			Detector4UploadDataBean t_Detector4UploadDataBean = new Detector4UploadDataBean();
			t_Detector4UploadDataBean.logTime = logTime;//时间赋值
			t_Detector4UploadDataBean.detectorDatas = new HardwareDataBean[data_map.get(logTime).size()];
			data_map.get(logTime).toArray(t_Detector4UploadDataBean.detectorDatas);
			uploadListBean.dataList[count] =t_Detector4UploadDataBean;
			count ++;
		}
		L.d("封装上传数据进BEAN结束~~~~");
		json = new Gson().toJson(uploadListBean);
		L.d("封装上传数据进GSON结束~~~~");
		return json;
	}
}
