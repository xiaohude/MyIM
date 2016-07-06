package com.smarttiger.utils;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Vibrator;

public class MyVoiceVibrator {
	
	/**
	 * 可根据用户设置来自动响铃或震动,,,
	 * @param context
	 */
	public static void play(Context context){
		boolean isVoice = true;
		boolean isVibrate = false;
		play(context, isVoice, isVibrate);
	}

	/**
	 * 开始响铃或震动
	 * @param context
	 * @param isVoice 是否响铃
	 * @param isVibrate 是否震动
	 */
	public static void play(Context context, boolean isVoice, boolean isVibrate) {
		
		//设置震动
		if(isVibrate)
		{
			Vibrator mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			//停顿100ms，震动100ms，停顿100ms，震动10ms 后面参数-1代表只震动一次。
			mVibrator.vibrate(new long[] { 	100, 100, 100, 100,
											100, 10,  100, 10,
											100, 100, 120, 10,
											100, 100, 100, 100}, -1);
			
			//SOS
//			mVibrator.vibrate(new long[] { 	100, 100, 100, 10, 
//											100, 10,   
//											100, 100, 100, 100,
//											100, 100, 100, 100, 
//											100, 100, 100, 100,
//											100, 10,   100, 10,
//											100, 100, 100, 100}, -1);
		}
		
		if(isVoice)
			try {
				Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
				MediaPlayer mMediaPlayer = new MediaPlayer();
				mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					public void onCompletion(MediaPlayer mp) {
						mp.release();
					}
				});
				mMediaPlayer.setDataSource(context, alert);
				final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
				if (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) != 0) {
					mMediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
					mMediaPlayer.setLooping(false);
					mMediaPlayer.prepare();
					mMediaPlayer.start();
				}
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
