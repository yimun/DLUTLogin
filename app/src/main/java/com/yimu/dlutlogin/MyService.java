package com.yimu.dlutlogin;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {


	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message arg0) {
			// TODO Auto-generated method stub
			super.handleMessage(arg0);
			String result = (String) arg0.obj;
            if(result != null && result.contains("登录成功")) {
                SpUtil.setLoginState(MyService.this,true);
                if(SpUtil.getSp(MyService.this).getBoolean("cb_isNotify",false)) {
                    showNotif();
                }else {
                    Toast.makeText(MyService.this, "DLUT:登录成功,开始上网", Toast.LENGTH_LONG).show();
                }
            }else if(result != null && result.contains("登录失败")){
                Toast.makeText(MyService.this, "DLUT:用户名或密码有错误", Toast.LENGTH_SHORT).show();
            }else{
				Toast.makeText(MyService.this, "DLUT:尝试登录失败", Toast.LENGTH_SHORT).show();
			}
            sendBroadcast(new Intent(UiReceiver.ACTION_UPDATE_UI));
            stopSelf();

		}

	};

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
        SpUtil.setLoginState(MyService.this,false); // 重置登录的状态
		Log.i("MyService", "onCreate");
        new LoginThread(this).start();
	}



	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("MyService", "onDestory");

	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.i("MyService", "onStart");

	}

	public void showNotif() {

        String uname = SpUtil.getSp(this).getString("username", "");
		// 定义通知栏展现的内容信息
		CharSequence title, contentTitle, contentText;
		title = "登录成功";
		contentTitle = "登录成功";
		contentText = "已成功使用 " + uname + " 登录到DLUT";

		// 消息通知栏
		// 定义NotificationManager
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		int icon = R.drawable.ic_launcher;

		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, title, when);
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// 定义下拉通知栏时要展现的内容信息
		Intent in = new Intent(this, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, in, 0);
		notification.setLatestEventInfo(this, contentTitle, contentText,
				contentIntent);

		// 用mNotificationManager的notify方法通知用户生成标题栏消息通知
		mNotificationManager.notify(1, notification);

	}

}
