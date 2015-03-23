package com.greenhousegateway.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.util.EncodingUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public final class Lw {
	/**
	 * 写入日志
	 * 
	 * @param mContext
	 * @param mStr
	 */
	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh.mm.ss SSS");
	private static String mPath;
	private static boolean isWrite = false;

	private Lw() {

	}

	public static void setWrite(boolean isWrites) {
		isWrite = isWrites;
	}

	public synchronized static void WriteLog(Context mContext, String mStr) {
		if (mContext == null || mStr == null || !isWrite) {
			return;
		}
		FileWriter writer = null;
		try {
			mPath = mContext.getFilesDir() + "/" + ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId() + ".txt";
			writer = new FileWriter(mPath, true);
			writer.write(formatter.format(new Date()) + "  :" + mStr + "\r\n");
			writer.flush();
			writer.close();
			System.out.println("写入日志成功！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {

				}
			}
		}

	}

	/**
	 * 读取日志,输出
	 * 
	 * @param mContext
	 * @return
	 */
	public synchronized static String ReadLog(Context mContext) {
		BufferedReader in = null;
		try {
			mPath = mContext.getFilesDir() + "/" + ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId() + ".txt";
			in = new BufferedReader(new FileReader(mPath));
			StringBuffer sb = new StringBuffer("");
			String str = null;
			while ((str = in.readLine()) != null)
				sb.append(str + "\r\n\r\n");
			String content = EncodingUtils.getString(sb.toString().getBytes(), "UTF-8");
			if (content.length() > 1024 * 1024) {
				content = "超过1Mb了，界面无法写入超过1Mb的数据，导出来自己查看吧！！！";
			}
			in.close();
			return content;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {

				}
			}
		}
		return "没有文件日志~";
	}

	/**
	 * 删除日志
	 * 
	 * @param mContext
	 */
	public synchronized static void ClearLog(Context mContext) {
		mPath = mContext.getFilesDir() + "/" + ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId() + ".txt";
		File file = new File(mPath);
		file.delete();
	}

	/**
	 * 导出日志到SD卡内
	 * 
	 * @param mContext
	 */
	@SuppressWarnings("resource")
	public synchronized static void ExportLog(Context mContext) {
		try {
			mPath = mContext.getFilesDir() + "/" + ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId() + ".txt";
			String mPaths = Environment.getExternalStorageDirectory() + "/" + ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId() + ".txt";
			FileChannel srcChannel = new java.io.FileInputStream(mPath).getChannel();// 直接NIO，少写点代码
			FileChannel dstChannel = new java.io.FileOutputStream(mPaths).getChannel();
			dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
			srcChannel.close();
			dstChannel.close();
			Toast.makeText(mContext, "导出成功！", Toast.LENGTH_SHORT).show();
		} catch (FileNotFoundException e) {
			Toast.makeText(mContext, "请检查SD卡是否正常！", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(mContext, "请检查SD卡是否正常！", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
}
