package com.yimu.dlutlogin;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by linwei on 2014/11/24.
 */
public class SpUtil {

    public static SharedPreferences getSp(Context ui){
        SharedPreferences sp = ui.getSharedPreferences("com.yimu.dlutlogin_preferences",0);
        return sp;
    }

    public static void setLoginState(Context ui,boolean isLogin){
        SharedPreferences sp = getSp(ui);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("isLogin",isLogin);
        ed.commit();
    }

    public static boolean getLoginState(Context ui){
        SharedPreferences sp = getSp(ui);
        return sp.getBoolean("isLogin",false);
    }

}
