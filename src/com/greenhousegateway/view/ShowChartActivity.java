package com.greenhousegateway.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import cn.limc.androidcharts.entity.LineEntity;
import cn.limc.androidcharts.view.MAChart;

import com.greenhousegateway.DataKeeper;
import com.greenhousegateway.GreenHouseApplication;
import com.greenhousegateway.R;
import com.greenhousegateway.controller.TaskConstants;
import com.greenhousegateway.databean.LoginDataBean;
import com.greenhousegateway.databean.HardwareDataBean;
import com.greenhousegateway.service.UploadDataService;
import com.greenhousegateway.util.Constants;
import com.greenhousegateway.util.GreenHouseUtils;
import com.greenhousegateway.util.L;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/***
 * 探头数据线图activity。 本activity应当只有界面逻辑，不牵扯业务逻辑。
 * 
 * @author RyanHu
 * 
 */
public class ShowChartActivity extends BaseActivity
{
	private static final int TYPE_HOUR = 1;
	private static final int TYPE_DAY = 2;
	private static final int TYPE_WEEK = 3;
	private static final int TEMP = -1;
	private static final int HUMI = -2;
	private static final int BEAM = -3;
	MAChart temp_machart;
	MAChart humi_machart;
	MAChart beam_machart;
	TextView temp_tv;
	TextView humi_tv;
	TextView beam_tv;

	private String dmac;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initViews()
	{
		showView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.charts_activity, null);
		temp_machart = (MAChart) showView.findViewById(R.id.temp_machart);
		humi_machart = (MAChart) showView.findViewById(R.id.humi_machart);
		beam_machart = (MAChart) showView.findViewById(R.id.beam_machart);
		temp_tv = (TextView) showView.findViewById(R.id.temp_tv);
		humi_tv = (TextView) showView.findViewById(R.id.humi_tv);
		beam_tv = (TextView) showView.findViewById(R.id.beam_tv);
	}

	@Override
	public void onClick(View v)
	{
	}

	@Override
	protected void progressLogic()
	{
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		dmac = getIntent().getExtras().getString("dmac");
		L.d("当前探头MAC --->" + dmac);
		initMACharts(temp_machart, TYPE_HOUR, TEMP);
		initMACharts(humi_machart, TYPE_HOUR, HUMI);
		initMACharts(beam_machart, TYPE_HOUR, BEAM);
		updataTextView();
	}

	@Override
	protected void setTaskHandler()
	{
		taskHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case TaskConstants.GATEWAY_LOGIN_TASK:
					if (msg.arg1 == TaskConstants.TASK_SUCCESS)
					{
						LoginDataBean bean = (LoginDataBean) msg.obj;
						Toast.makeText(mApp, "登录成功：" + bean.toString(), Toast.LENGTH_LONG).show();
					}
					break;
				case Constants.LOGIN_SUCCESS:
					if (GreenHouseApplication.gwToken != null)
					{
						mNetworkStatusTV.setText("已登录到服务器");
					} else
					{
						mNetworkStatusTV.setText("未登录到服务器");
					}
					break;
				case TaskConstants.GATEWAY_READHARDWARE_TASK:
					// 读取硬件成功，准备刷新界面
					HardwareDataBean bean = (HardwareDataBean) msg.obj;
					if (msg.arg1 == TaskConstants.TASK_SUCCESS && dmac.equals(bean.dmac))
					{
						initMACharts(temp_machart, TYPE_HOUR, TEMP);
						initMACharts(humi_machart, TYPE_HOUR, HUMI);
						initMACharts(beam_machart, TYPE_HOUR, BEAM);
						temp_machart.invalidate();
						humi_machart.invalidate();
						beam_machart.invalidate();
						updataTextView();
					}
					break;
				default:
					break;
				}
			}

		};
		handler = taskHandler;
	}

	public void updataTextView()
	{
		List<HardwareDataBean> dataBeanlist = DataKeeper.detectorDataMap.get(dmac);
		if (dataBeanlist.size() > 0)
		{
			HardwareDataBean currentBean = dataBeanlist.get(dataBeanlist.size() - 1);
			temp_tv.setText(String.valueOf(currentBean.temperature));
			humi_tv.setText(String.valueOf(currentBean.humidity));
			beam_tv.setText(String.valueOf(currentBean.beam));
		}

	}

	public static Handler handler;

	public static Handler getHandler()
	{
		return handler;
	}

	public void initMACharts(MAChart machart, int TIME_TYPE, int dataTYPE)
	{
		ArrayList<HardwareDataBean> listDataBean = (ArrayList<HardwareDataBean>) DataKeeper.detectorDataMap.get(dmac);
		if (listDataBean == null || listDataBean.size() == 0)
		{// 防止界面无数据崩溃
			listDataBean = new ArrayList<HardwareDataBean>();
			HardwareDataBean _HardwareDataBean = new HardwareDataBean();
			_HardwareDataBean.logTime = new Date().getTime() + GreenHouseApplication.SERVER_TIME;
			listDataBean.add(_HardwareDataBean);
		}
		List<String> ytitle = new ArrayList<String>();
		ytitle.add("0");
		ytitle.add("25");
		ytitle.add("50");
		ytitle.add("75");
		ytitle.add("100");
		List<String> xtitle = new ArrayList<String>();

		switch (TIME_TYPE)
		{
		case TYPE_HOUR:
			int pointCount = 60;
			if(listDataBean.size()>1)
			{
				pointCount = (int) (Constants.HOUR / (listDataBean.get(1).logTime - listDataBean.get(0).logTime));
				L.d("时间采样点共计---->"+pointCount);
			}
			while (listDataBean.size() > 60)
			{
				listDataBean.remove(0);
			}
			xtitle.add(getHHmmLogTime(listDataBean.get(0).logTime));
			xtitle.add(getHHmmLogTime(listDataBean.get(0).logTime + 5 * Constants.MINUTES));
			xtitle.add(getHHmmLogTime(listDataBean.get(0).logTime + 10 * Constants.MINUTES));
			xtitle.add(getHHmmLogTime(listDataBean.get(0).logTime + 15 * Constants.MINUTES));
			xtitle.add(getHHmmLogTime(listDataBean.get(0).logTime + 20 * Constants.MINUTES));
			xtitle.add(getHHmmLogTime(listDataBean.get(0).logTime + 25 * Constants.MINUTES));
			xtitle.add(getHHmmLogTime(listDataBean.get(0).logTime + 30 * Constants.MINUTES));
			xtitle.add(getHHmmLogTime(listDataBean.get(0).logTime + 35 * Constants.MINUTES));
			xtitle.add(getHHmmLogTime(listDataBean.get(0).logTime + 40 * Constants.MINUTES));
			xtitle.add(getHHmmLogTime(listDataBean.get(0).logTime + 45 * Constants.MINUTES));
			xtitle.add(getHHmmLogTime(listDataBean.get(0).logTime + 50 * Constants.MINUTES));
			xtitle.add(getHHmmLogTime(listDataBean.get(0).logTime + 55 * Constants.MINUTES));
			xtitle.add(getHHmmLogTime(listDataBean.get(0).logTime + 60 * Constants.MINUTES));
			machart.setMaxPointNum(pointCount);
			break;
		case TYPE_DAY:
			xtitle.add("0时");
			xtitle.add("1");
			xtitle.add("2");
			xtitle.add("3");
			xtitle.add("4");
			xtitle.add("5");
			xtitle.add("6");
			xtitle.add("7");
			xtitle.add("8");
			xtitle.add("9");
			xtitle.add("10");
			xtitle.add("11");
			xtitle.add("12");
			xtitle.add("13");
			xtitle.add("14");
			xtitle.add("15");
			xtitle.add("16");
			xtitle.add("17");
			xtitle.add("18");
			xtitle.add("19");
			xtitle.add("20");
			xtitle.add("21");
			xtitle.add("22");
			xtitle.add("23");
			xtitle.add("24");
			machart.setMaxPointNum(24);
			while (listDataBean.size() > 24)
			{
				listDataBean.remove(0);
			}
			break;
		case TYPE_WEEK:
			xtitle.add("周一");
			xtitle.add("周二");
			xtitle.add("周三");
			xtitle.add("周四");
			xtitle.add("周五");
			xtitle.add("周六");
			xtitle.add("周日");
			machart.setMaxPointNum(7);
			while (listDataBean.size() > 7)
			{
				listDataBean.remove(0);

			}
			break;
		default:
			break;
		}
		List<Float> data = new ArrayList<Float>();
		switch (dataTYPE)
		{
		case TEMP:
		{
			Iterator<HardwareDataBean> iterator = listDataBean.iterator();
			while (iterator.hasNext())
			{
				HardwareDataBean greenHouseBaseDataBean = (HardwareDataBean) iterator.next();
				data.add((float) greenHouseBaseDataBean.temperature);
			}
			machart.setMaxValue(100);
			machart.setAxisYTitles(ytitle);
		}
			break;

		case HUMI:
		{
			Iterator<HardwareDataBean> iterator = listDataBean.iterator();
			while (iterator.hasNext())
			{
				HardwareDataBean greenHouseBaseDataBean = (HardwareDataBean) iterator.next();
				data.add((float) greenHouseBaseDataBean.humidity);
			}
			machart.setMaxValue(100);
			machart.setAxisYTitles(ytitle);

		}
			break;

		case BEAM:
		{
			Iterator<HardwareDataBean> iterator = listDataBean.iterator();
			while (iterator.hasNext())
			{
				HardwareDataBean greenHouseBaseDataBean = (HardwareDataBean) iterator.next();
				data.add((float) greenHouseBaseDataBean.beam);
			}
			machart.setMaxValue(300);
			ytitle = new ArrayList<String>();
			ytitle.add("0");
			ytitle.add("75");
			ytitle.add("150");
			ytitle.add("225");
			ytitle.add("300");
			machart.setAxisYTitles(ytitle);
		}
			break;

		default:
			break;
		}

		List<LineEntity> lines = new ArrayList<LineEntity>();

		LineEntity MA = new LineEntity();
		MA.setTitle("Title");
		MA.setLineColor(Color.BLACK);
		MA.setLineData(data);
		lines.add(MA);
		machart.setBackgroudColor(Color.WHITE);
		machart.setDisplayCrossXOnTouch(false);
		machart.setDisplayCrossYOnTouch(false);
		machart.setAxisXColor(Color.BLACK);
		machart.setAxisYColor(Color.BLACK);
		machart.setBorderColor(Color.BLACK);
		machart.setAxisMarginTop(10);
		machart.setAxisMarginLeft(20);
		machart.setAxisXTitles(xtitle);
		machart.setLongtitudeFontSize(20);
		machart.setLongtitudeFontColor(Color.BLACK);
		machart.setLatitudeColor(Color.BLACK);
		machart.setLatitudeFontColor(Color.BLACK);
		machart.setLongitudeColor(Color.BLACK);

		machart.setMinValue(0);
		machart.setLatitudeFontSize(20);
		machart.setLongtitudeFontSize(10);
		machart.setDisplayAxisXTitle(true);
		machart.setDisplayAxisYTitle(true);
		machart.setDisplayLatitude(true);
		machart.setDisplayLongitude(true);
		// 为chart1增加均线
		machart.setLineData(lines);
		// machart.addNotify(new ITouchEventResponse()
	}

	private String getHHmmLogTime(long logTime)
	{
		String result = new SimpleDateFormat("HH:mm").format(logTime);
		return result;
	}
}
