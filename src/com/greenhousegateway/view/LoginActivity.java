package com.greenhousegateway.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.limc.androidcharts.entity.LineEntity;
import cn.limc.androidcharts.view.MAChart;

import com.greenhousegateway.DataKeeper;
import com.greenhousegateway.GreenHouseApplication;
import com.greenhousegateway.R;
import com.greenhousegateway.controller.TaskConstants;
import com.greenhousegateway.databean.LoginDataBean;
import com.greenhousegateway.databean.UploadDataBean;
import com.greenhousegateway.service.UploadDataService;
import com.greenhousegateway.util.Constants;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
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
 * 探头数据网格activity。
 * 本activity应当只有界面逻辑，不牵扯业务逻辑。
 * @author RyanHu
 * 
 */
public class LoginActivity extends BaseActivity 
{
	/**--------------------**/

	private Button register_btn;
	private Button showDeviceInfo_btn;
	private Button showApkPathBtn;
	private Button showGwIdQrCodeBtn;
	private static boolean CanClick =true;
	/**--------------------**/
	private static final String DEFAULT_DECETOR_NAME = "TEST_DETECTOR_1";
	private TextView decetorNameTV;
	private TextView tempTV;
	private TextView humiTV;
	private TextView beamTV;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initViews()
	{
		
		showView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.detector_activity, null);
		decetorNameTV = (TextView)showView.findViewById(R.id.rl_ll_ll_tv_decetorname1);
		tempTV = (TextView)showView.findViewById(R.id.rl_ll_ll_tv_temp1);
		humiTV = (TextView)showView.findViewById(R.id.rl_ll_ll_tv_humi1);
		beamTV = (TextView)showView.findViewById(R.id.rl_ll_ll_tv_beam1);
		View decetorView1 = (View)showView.findViewById(R.id.rl_ll_ll_ver1);
		View decetorView2 = (View)showView.findViewById(R.id.rl_ll_ll_ver2);
		View decetorView3 = (View)showView.findViewById(R.id.rl_ll_ll_ver3);
		View decetorView4 = (View)showView.findViewById(R.id.rl_ll_ll_ver4);
		decetorView1.setOnClickListener(this);
		decetorView2.setOnClickListener(this);
		decetorView3.setOnClickListener(this);
		decetorView4.setOnClickListener(this);
		/**--------------------**/
		register_btn = (Button) showView.findViewById(R.id.login_register_btn);
		showDeviceInfo_btn = (Button)showView.findViewById(R.id.show_device_info);
		showDeviceInfo_btn.setOnClickListener(this);
		showApkPathBtn = (Button)showView.findViewById(R.id.show_client_apk_qrcode_btn);
		showApkPathBtn.setOnClickListener(this);
		showGwIdQrCodeBtn = (Button)showView.findViewById(R.id.show_gwid_qrcode_btn);
		showGwIdQrCodeBtn.setOnClickListener(this);
		register_btn.setOnClickListener(this);
		/**--------------------**/
		updataTextView();
	}
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.login_register_btn:
			
			if(CanClick)
			{
				
//			GreenHouseApplication.UploadTime =Integer.parseInt( mUploadTimeET.getText().toString());
			if(!(GreenHouseApplication.UploadTime>=1 && GreenHouseApplication.UploadTime<=5))
			{
				Toast.makeText(this, "上传数据时间间隔必须在1分钟到5分钟之间", 1).show();
			}
			Intent intent = new Intent(this,UploadDataService.class);
			startService(intent);
			CanClick = false ;
			}
			else
			{
				Toast.makeText(this, "已经开启上传数据服务，请不要重复开启！", 1).show();
			}
			break;
		case R.id.show_client_apk_qrcode_btn:
			if(GreenHouseApplication.apkPath!=null)
			{
				Intent i = new Intent(this,QRCodeActivity.class);
				i.putExtra(Constants.QRCODE_TYPE, Constants.CLENT_APKPATH_QRCODE);
				startActivity(i);
			}
			else
			{
				Toast.makeText(this, "网关未登录服务器，请先登录~", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.show_gwid_qrcode_btn:
			if(GreenHouseApplication.gwid!=0)
			{
				Intent i = new Intent(this,QRCodeActivity.class);
				i.putExtra(Constants.QRCODE_TYPE, Constants.GWID_QRCODE);
				startActivity(i);
			}
			else
			{
				Toast.makeText(this, "网关未登录服务器，请先登录~", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.show_device_info:
			StringBuffer sb = new StringBuffer();
			sb.append("网关设备信息：\n");
			sb.append("IMEI："+GreenHouseApplication.IMEI+"\n");
			sb.append("MAC："+GreenHouseApplication.Mac+"\n");
			sb.append("网关ID："+GreenHouseApplication.gwid+"\n");
			AlertDialog dialog =new  AlertDialog.Builder(this).create();
			dialog.setMessage(sb);
			dialog.show();
//			deviceInfoTV.setText(sb.toString());
			break;
		case R.id.rl_ll_ll_ver1:
			if(GreenHouseApplication.gwid!=0)
			{
				Intent i = new Intent(this,DetectorActivity.class);
				startActivity(i);
			}
			else
			{
				Toast.makeText(this, "网关未登录服务器，请先登录~", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.rl_ll_ll_ver2:
			Toast.makeText(this, "还未添加该探头！", Toast.LENGTH_LONG).show();
			break;
		case R.id.rl_ll_ll_ver3:
			Toast.makeText(this, "还未添加该探头！", Toast.LENGTH_LONG).show();
			break;

		case R.id.rl_ll_ll_ver4:
			Toast.makeText(this, "还未添加该探头！", Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
	}

	@Override
	protected void progressLogic()
	{
	}

	@Override
	protected void  setTaskHandler()
	{
		taskHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case TaskConstants.GATEWAY_LOGIN:
					if(msg.arg1 == TaskConstants.TASK_SUCCESS)
					{
						LoginDataBean bean = (LoginDataBean) msg.obj;
						Toast.makeText(mApp, "登录成功："+bean.toString(), Toast.LENGTH_LONG).show();
					}
					break;
				case Constants.LOGIN_SUCCESS:
					if(GreenHouseApplication.gwToken!=null)
					{
						mNetworkStatusTV.setText("已登录到服务器");
					}
					else
					{
						mNetworkStatusTV.setText("未登录到服务器");
					}
					break;
				case TaskConstants.GATEWAY_READHARDWARE:
					//读取硬件成功，准备刷新界面
					updataTextView();
					break;
				default:
					break;
				}
			}
			
		};
		handler =taskHandler;
	}
	
	public void updataTextView ()
	{
		List<UploadDataBean> dataBeanlist = DataKeeper.dataKeeper_hour;
		UploadDataBean currentBean = dataBeanlist.get(dataBeanlist.size()-1);
		decetorNameTV.setText("探测器:"+DEFAULT_DECETOR_NAME);
		tempTV.setText("温度:"+String.valueOf(currentBean.temperature));
		humiTV.setText("湿度:"+String.valueOf(currentBean.humidity));
		beamTV.setText("光照:"+String.valueOf(currentBean.beam));
	}
	public static Handler handler ;
	public static Handler getHandler()
	{
		return handler;
	}
}
