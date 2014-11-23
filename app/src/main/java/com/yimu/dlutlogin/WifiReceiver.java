package com.yimu.dlutlogin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;

/**
 * 网络状态改变时接收广播并启动主服务
 *
 * @author Administrator
 */
public class WifiReceiver extends BroadcastReceiver {

    public static final String WIFI_SSID = "DLUT";

    public void onReceive(Context ctx, Intent intent) {

        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 这个监听wifi的连接状态
            //Log.i("receiver","NETWORK_STATE_CHANGED_ACTION");
            Parcelable parcelableExtra = intent
                    .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                State state = networkInfo.getState();
                if (state == State.CONNECTED) { // 表示wifi状态改变
                    Log.i("receiver", "wifi状态改变");
                    WifiAdmin wifiAdmin = new WifiAdmin(ctx);
                    String ssidName = wifiAdmin.getSSID();
                    Log.i("receiver connect to:", ssidName);
                    // 判断是否连接了DLUT
                    if (ssidName != null && ssidName.equalsIgnoreCase(WIFI_SSID) && MyService.isLogin == false) {
                        ctx.startService(new Intent(ctx, MyService.class));

                    }
                } else if (state == State.DISCONNECTED) {
                    MyService.isLogin = false;
                    ctx.sendBroadcast(new Intent(UiReceiver.ACTION_UPDATE_UI));
                }

            }

        }


    }

}
