package amap.com.android_path_record;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.IntDef;
import android.support.v7.app.NotificationCompat;

/**
 * Polling service
 * @Author Ryan
 * @Create 2013-7-13 上午10:18:44
 */
public class PollingService extends Service {
	private NotificationManager manger;
	public static final int TYPE_Normal = 1;
	public static final String ACTION = "com.ryantang.service.PollingService";
	private Notification mNotification;
	private NotificationManager mManager;
	private PowerManager.WakeLock mWakelock;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		initNotifiManager();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		new PollingThread().start();
	}

	private void initNotifiManager() {
		manger = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
		PowerManager pm = (PowerManager)getSystemService(POWER_SERVICE);// init powerManager
		mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP|
				PowerManager.SCREEN_DIM_WAKE_LOCK,"target"); // this target for tell OS which app control screen
	}

	private void showNotification() {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
		//Ticker是状态栏显示的提示
		builder.setTicker("上位互动");
		//第一行内容  通常作为通知栏标题
		builder.setContentTitle("你收到一条新的消息");
		//第二行内容 通常是通知正文
		builder.setContentText("你该打酱油了！！！");
		//第三行内容 通常是内容摘要什么的 在低版本机器上不一定显示
		//builder.setSubText("这里显示的是通知第三行内容！");
		//ContentInfo 在通知的右侧 时间的下面 用来展示一些其他信息
		//builder.setContentInfo("2");
		builder.setAutoCancel(true);
		//builder.setNumber(2);
		builder.setSmallIcon(R.mipmap.ic_launcher);
		Intent intent = new Intent(getApplicationContext(),MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(),1,intent,0);
		builder.setContentIntent(pIntent);
		//设置震动
		//long[] vibrate = {100,200,100,200};
		//builder.setVibrate(vibrate);
		builder.setDefaults(NotificationCompat.DEFAULT_ALL);
		Notification notification = builder.build();
		manger.notify(TYPE_Normal,notification);
	}

	/**
	 * Polling thread
	 * @Author Ryan
	 * @Create 2013-7-13 上午10:18:34
	 */
	int count = 0;
	class PollingThread extends Thread {
		@Override
		public void run() {
			System.out.println("Polling...");
			count ++;
			if (count % 2 == 0) {
				showNotification();
				System.out.println("New message!");
				mWakelock.acquire();
				mWakelock.release();
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("Service:onDestroy");
	}

}
