package com.greenhousegateway.hardware;

import com.greenhousegateway.R;
import com.greenhousegateway.util.L;
import com.greenhousegateway.util.Lw;

import tw.com.prolific.driver.pl2303.PL2303Driver;
import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SerialPortUSBManager
{
	private static final boolean SHOW_DEBUG = true;
	PL2303Driver mSerial;
	String TAG = "PL2303HXD_APLog";
	private Button btRead;

	private PL2303Driver.BaudRate mBaudrate = PL2303Driver.BaudRate.B9600;

	private static final String ACTION_USB_PERMISSION = "com.prolific.pl2303hxdsimpletest.USB_PERMISSION";
	public int PL2303HXD_BaudRate;
	public String PL2303HXD_BaudRate_str = "B4800";

	private static SerialPortUSBManager sInstance;
	private Context mContext;

	private SerialPortUSBManager()
	{
	}

	public static SerialPortUSBManager getInstance(Context _context)
	{
		if (sInstance == null)
		{
			sInstance = new SerialPortUSBManager();
			sInstance.mContext = _context;
			sInstance.create();
			sInstance.openUsbSerial();
		}

		return sInstance;
	}

	public void create()
	{
		Log.d(TAG, "Enter onCreate");
		mSerial = new PL2303Driver((UsbManager) mContext.getSystemService(Context.USB_SERVICE), mContext, ACTION_USB_PERMISSION);

		if (!mSerial.PL2303USBFeatureSupported())
		{
			Log.d(TAG, "No Support USB host API");
			mSerial = null;
		}

		Log.d(TAG, "Leave create");
		init();
	}

	public void init()
	{
		Log.d(TAG, "Enter init");
		if (!mSerial.isConnected())
		{
			if (SHOW_DEBUG)
			{
				Log.d(TAG, "New instance : " + mSerial);
			}

			if (!mSerial.enumerate())
			{

				Log.d(TAG, "no more devices found" + mSerial);
				return;
			} else
			{
				Log.d(TAG, "init:enumerate succeeded!");
			}
		}
		try
		{
			// 这里必须暂停一下再去Open SerialPort
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		Log.d(TAG, "attached");
		Log.d(TAG, "Leave init");
	}

	public void openUsbSerial()
	{
		Log.d(TAG, "Enter  openUsbSerial");

		if (null == mSerial)
			return;

		if (mSerial.isConnected())
		{
			if (SHOW_DEBUG)
			{
				Log.d(TAG, "openUsbSerial : isConnected ");
			}
			String str = "9600";
			int baudRate = Integer.parseInt(str);
			switch (baudRate)
			{
			case 9600:
				mBaudrate = PL2303Driver.BaudRate.B9600;
				break;
			case 19200:
				mBaudrate = PL2303Driver.BaudRate.B19200;
				break;
			case 115200:
				mBaudrate = PL2303Driver.BaudRate.B115200;
				break;
			default:
				mBaudrate = PL2303Driver.BaudRate.B9600;
				break;
			}
			Log.d(TAG, "baudRate:" + baudRate);
			if (!mSerial.InitByBaudRate(mBaudrate, 700))
			{
				if (!mSerial.PL2303Device_IsHasPermission())
				{
					Log.e(TAG, "cannot open, maybe no permission");

				}

				if (mSerial.PL2303Device_IsHasPermission() && (!mSerial.PL2303Device_IsSupportChip()))
				{
					Log.e(TAG, "cannot open, maybe this chip has no support, please use PL2303HXD / RA / EA chip.");

				}
			} else
			{
				Log.d(TAG, "PL2303 connected");
			}
		} else
		{
			Log.d(TAG, "!!!!!!!!mSerial.isConnected()");
		}
		Log.d(TAG, "Leave openUsbSerial");
	}
//
//	public String readDataFromSerial()
//	{
//		try
//		{
//
//			int len;
//			byte[] rbuf = new byte[128];
//			StringBuffer sbHex = new StringBuffer();
//
//			Log.d(TAG, "Enter readDataFromSerial");
//
//			if (null == mSerial)
//				return "未初始化";
//
//			if (!mSerial.isConnected())
//				return "串口未连接";
//
//			len = mSerial.read(rbuf);
//			if (len < 0)
//			{
//				Log.d(TAG, "Fail to bulkTransfer(read data)");
//				return "读取失败";
//			}
//
//			if (len > 0)
//			{
//				if (SHOW_DEBUG)
//				{
//					Log.d(TAG, "read len : " + len);
//				}
//				for (int j = 0; j < len; j++)
//				{
//					sbHex.append((char) (rbuf[j] & 0x000000FF));
//				}
//				String result = new String(rbuf);
//				L.d("read all result ->" + result);
//				Lw.WriteLog(mContext,"read all result ->" + result);
//				String str[] = result.split("\t");
//				result = str[1].split("\n")[0];
//				return result;
//			} else
//			{
//				if (SHOW_DEBUG)
//				{
//					Log.d(TAG, "read len : 0 ");
//				}
//				return "没有读到数据";
//			}
//		} catch (Exception e)
//		{
//			e.printStackTrace();
//			return "读取数据错误";
//		}
//	}
	
	private static StringBuffer sReceiveBuf = new StringBuffer() ;
	public String readDataFromSerial()
	{
		try
		{

			int len;
			byte[] rbuf = new byte[128];

			Log.d(TAG, "Enter readDataFromSerial");

			if (null == mSerial)
				return "未初始化";

			if (!mSerial.isConnected())
				return "串口未连接";

			len = mSerial.read(rbuf);
			if (len < 0)
			{
				Log.d(TAG, "Fail to bulkTransfer(read data)");
				return "读取失败";
			}

			if (len > 0)
			{
				if (SHOW_DEBUG)
				{
					Log.d(TAG, "read len : " + len);
				}
				String result = "";
				for (int j = 0; j < len; j++)
				{
					if ((char) (rbuf[j] & 0x000000FF) == '\n')
					{
						result = sReceiveBuf.toString();
						sReceiveBuf.delete(0, sReceiveBuf.length()-1);
					}
					else
					{
						sReceiveBuf.append((char) (rbuf[j] & 0x000000FF));
					}
				}				
				L.d("read all result ->" + result);
				Lw.WriteLog(mContext,"read all result ->" + result);
				return result;
			} else
			{
				if (SHOW_DEBUG)
				{
					Log.d(TAG, "read len : 0 ");
					Lw.WriteLog(mContext,"read len : 0 " );
				}
				return "没有读到数据";
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return "读取数据错误";
		}
	}
}