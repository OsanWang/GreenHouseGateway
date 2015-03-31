package com.greenhousegateway.view;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.greenhousegateway.R;
import com.greenhousegateway.controller.TaskConstants;
import com.zxing.camera.CameraManager;
import com.zxing.decoding.InactivityTimer;
import com.zxing.decoding.ScannerActivityHandler;
import com.zxing.view.ViewfinderView;

/**
 * 扫描二维码的Activity
 * 
 * @author RyanHu
 * 
 */
public class ScannerActivity extends BaseActivity implements Callback
{

	private ScannerActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	// private Button cancelScanButton;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// ViewUtil.addTopView(getApplicationContext(), this,
		// R.string.scan_card);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume()
	{
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface)
		{
			initCamera(surfaceHolder);
		} else
		{
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL)
		{
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

	}

	@Override
	protected void onPause()
	{
		super.onPause();
		if (handler != null)
		{
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy()
	{
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	public void handleDecode(Result result, Bitmap barcode)
	{
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		if (resultString.equals(""))
		{
			Toast.makeText(ScannerActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
		} else
		{
			// System.out.println("Result:"+resultString);
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("result", resultString);
			resultIntent.putExtras(bundle);
			Toast.makeText(getApplicationContext(), "result is " + result, 1).show();
		}
		try
		{
			controller.addDetector(taskHandler, result.getText());
		} catch (Exception e)
		{
			// 转换错误
			e.printStackTrace();
		}
	}

	private void initCamera(SurfaceHolder surfaceHolder)
	{
		try
		{
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe)
		{
			return;
		} catch (RuntimeException e)
		{
			return;
		}
		if (handler == null)
		{
			handler = new ScannerActivityHandler(this, decodeFormats, characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		if (!hasSurface)
		{
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView()
	{
		return viewfinderView;
	}

	public Handler getHandler()
	{
		return handler;
	}

	public void drawViewfinder()
	{
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound()
	{
		if (playBeep && mediaPlayer == null)
		{
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try
			{
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e)
			{
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate()
	{
		if (playBeep && mediaPlayer != null)
		{
			mediaPlayer.start();
		}
		if (vibrate)
		{
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener()
	{
		public void onCompletion(MediaPlayer mediaPlayer)
		{
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public void onClick(View v)
	{

	}

	@Override
	protected void initViews()
	{
		showView = (ViewGroup) getLayoutInflater().inflate(R.layout.scanner_activity, null);
		viewfinderView = (ViewfinderView) showView.findViewById(R.id.viewfinder_view);
		CameraManager.init(this);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected void progressLogic()
	{

	}

	@Override
	protected void setTaskHandler()
	{
		taskHandler = new Handler()
		{
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case TaskConstants.ADD_DETECTOR_TASK:
					if (msg.arg1 == TaskConstants.TASK_SUCCESS)
					{
						startActivityByName(GatewayLoginActivity.class);
						ScannerActivity.this.finish();
						Toast.makeText(mApp, "添加探头成功", Toast.LENGTH_LONG).show();
					} else
					{
						Toast.makeText(mApp, "添加探头失败", Toast.LENGTH_LONG).show();

					}
					break;
				default:
					break;
				}
			}
		};
	}
}