package com.greenhousegateway.view;

import com.google.zxing.WriterException;
import com.greenhousegateway.GreenHouseApplication;
import com.greenhousegateway.R;
import com.greenhousegateway.util.Constants;
import com.zxing.encoding.EncodingHandler;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class QRCodeActivity extends BaseActivity
{
	private ImageView mQRCodeImageView ;
	private TextView mQrcodeTextView;
	private int qrcodeSize = 300;
	@Override
	public void onClick(View v)
	{
		
	}

	@Override
	protected void initViews()
	{
		showView = (ViewGroup) getLayoutInflater().inflate(R.layout.qrcode_activity, null);
		mQRCodeImageView = (ImageView) showView.findViewById(R.id.create_code_imageview);
		mQrcodeTextView = (TextView)showView.findViewById(R.id.qrcode_text_info);
	
	}

	@Override
	protected void progressLogic()
	{
		Intent intent = getIntent();
		int type = intent.getIntExtra(Constants.QRCODE_TYPE, Constants.GWID_QRCODE);
		if(type == Constants.GWID_QRCODE)
		{
			mQrcodeTextView.setText("请扫描网关二维码！");
			try
			{
				mQRCodeImageView.setImageBitmap(EncodingHandler.createQRCode(GreenHouseApplication.gwid+"", qrcodeSize));
			} catch (WriterException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			mQrcodeTextView.setText("请扫描客户端下载地址！");
			try
			{
				mQRCodeImageView.setImageBitmap(EncodingHandler.createQRCode(GreenHouseApplication.apkPath, qrcodeSize));
			} catch (WriterException e)
			{
				e.printStackTrace();
			}

		}
		
	}

	@Override
	protected void setTaskHandler()
	{
		
	}

}
