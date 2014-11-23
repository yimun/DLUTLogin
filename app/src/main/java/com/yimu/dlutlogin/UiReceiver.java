package com.yimu.dlutlogin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UiReceiver extends BroadcastReceiver {

    public static final String ACTION_UPDATE_UI = "com.yimu.dlutlogin.ACTION_STATE_CHANGE";
    private MainActivity ui;

    public UiReceiver(){};
    public UiReceiver(MainActivity context) {
        this.ui = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.
        ui.changeStatus();

    }
}
