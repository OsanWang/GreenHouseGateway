package com.greenhousegateway.hardware;

import java.util.ArrayList;
import java.util.List;

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
		}

		return sInstance;
	}

	public boolean create()
	{
		L.d( "Enter onCreate");
		mSerial = new PL2303Driver((UsbManager) mContext.getSystemService(Context.USB_SERVICE), mContext, ACTION_USB_PERMISSION);

		if (!mSerial.PL2303USBFeatureSupported())
		{
			L.d(  "No Support USB host API");
			mSerial = null;
		}

		L.d(  "Leave create");
		return init();
	}

	public boolean init()
	{
		L.d( "Enter init");
		if (!mSerial.isConnected())
		{
			if (SHOW_DEBUG)
			{
				L.d( "New instance : " + mSerial);
			}
			if (!mSerial.enumerate())
			{

				L.d( "no more devices found" + mSerial);
				return false;
			} else
			{
				L.d( "init:enumerate succeeded!");
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

		L.d(  "attached");
		L.d( "Leave init");
		return true;
	}

	public boolean openUsbSerial()
	{
		L.d(  "Enter  openUsbSerial");

		if (null == mSerial)
			return false;

		if (mSerial.isConnected())
		{
			if (SHOW_DEBUG)
			{
				L.d(  "openUsbSerial : isConnected ");
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
			L.d( "baudRate:" + baudRate);
			if (!mSerial.InitByBaudRate(mBaudrate, 700))
			{
				if (!mSerial.PL2303Device_IsHasPermission())
				{
					L.d(  "cannot open, maybe no permission");
					return false;
				}

				if (mSerial.PL2303Device_IsHasPermission() && (!mSerial.PL2303Device_IsSupportChip()))
				{
					L.d( "cannot open, maybe this chip has no support, please use PL2303HXD / RA / EA chip.");
					return false;
				}
			} else
			{
				L.d( "PL2303 connected");
				return true;
			}
		} else
		{
			L.d(  "!!!!!!!!mSerial.isConnected()");
			return false;
		}
		L.d(  "Leave openUsbSerial");
		return false;
	}

	private static String LastString = "";

	public List<String> readDataFromSerial()
	{
		try
		{

			int len;
			byte[] rbuf = new byte[PL2303Driver.READBUF_SIZE];

			L.d( "Enter readDataFromSerial");

			if (null == mSerial)
				return null;

			if (!mSerial.isConnected())
				return null;
			
			len = mSerial.read(rbuf);
			if (len < 0)
			{
				L.d(  "Fail to bulkTransfer(read data)");
				return null;
			}
			
			
			if (len > 0)
			{
				if (SHOW_DEBUG)
				{
					L.d(  "read len : " + len);
				}
				StringBuffer sbHex = new StringBuffer();
				List<String> result = new ArrayList<String>();
				for (int j = 0; j < len; j++)
				{
					if(rbuf[j] != '\t')
					{
						sbHex.append((char) (rbuf[j] & 0x000000FF));
					}
					if(rbuf[j] == '\n')
					{
						result.add(LastString + new String (sbHex));
						sbHex = new StringBuffer();
						LastString = "";
					}
				}
				LastString  = LastString+ (new String (sbHex));
				return result;
			} else
			{
				if (SHOW_DEBUG)
				{
					L.d( "read len : 0 ");
				}
				return null;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}


}