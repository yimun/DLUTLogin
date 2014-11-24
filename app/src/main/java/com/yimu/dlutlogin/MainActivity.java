package com.yimu.dlutlogin;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

public class MainActivity extends Activity implements OnClickListener {

    private Button btnSave;
    private Button btnManual;
    private TextView tvUsername;
    private TextView tvPassword;
    private TextView tvLoginstatus;
    private ProgressBar pgProgressbar;
    private UiReceiver mUiReceiver;

    private int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSave = (Button) findViewById(R.id.save);
        btnManual = (Button) findViewById(R.id.manual);
        tvUsername = (TextView) findViewById(R.id.mailname);
        tvPassword = (TextView) findViewById(R.id.password);
        tvLoginstatus = (TextView) findViewById(R.id.loginstatus);
        pgProgressbar = (ProgressBar) findViewById(R.id.progressBar);
        btnSave.setOnClickListener(this);
        btnManual.setOnClickListener(this);
        // 注册更新的UI广播接收器
        mUiReceiver = new UiReceiver(this);
        IntentFilter inf = new IntentFilter();
        inf.addAction(UiReceiver.ACTION_UPDATE_UI);
        this.registerReceiver(mUiReceiver, inf);


        // always show overflow
        try {
            ViewConfiguration mconfig = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(mconfig, false);
            }
        } catch (Exception ex) {
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = SpUtil.getSp(this);
        boolean isSaved = sp.getBoolean("isSaved", false);
        if (!isSaved)
            return;
        tvUsername.setText(sp.getString("username", ""));
        tvPassword.setText(sp.getString("password", ""));
        this.changeStatus();

        cnt = 0;
    }

    public void changeStatus() {
        pgProgressbar.setVisibility(View.GONE);
        btnManual.setEnabled(true);
        btnManual.setText(getResources().getString(R.string.manual));
        if (SpUtil.getLoginState(this)) {
            tvLoginstatus.setText(getResources().getString(R.string.loginok));
            tvLoginstatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

        } else {
            tvLoginstatus.setText(getResources().getString(R.string.loginfail));
            tvLoginstatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int i, MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.action_about:
                Toast.makeText(this,getResources().getString(R.string.aboutme),Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_settings:
                if(cnt++ > 3)
                    Toast.makeText(this,"Sorry 设置里其实没东西 hiahiahia",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,SettingsActivity.class));
                break;
        }
        return super.onMenuItemSelected(i, menuItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View arg0) {
        SharedPreferences sp = SpUtil.getSp(this);
        switch (arg0.getId()) {
            case R.id.save: {
                String strUname = tvUsername.getText().toString();
                String strPass = tvPassword.getText().toString();
                if (!strUname.isEmpty() && !strPass.isEmpty()) {
                    Editor ed = sp.edit();
                    ed.putString("username", strUname);
                    ed.putString("password", strPass);
                    ed.putBoolean("isSaved", true);
                    ed.commit();
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "用户名密码不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.manual:
                if (!sp.getBoolean("isSaved", false)) {
                    Toast.makeText(this, "先保存用户名密码方可登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                String ssid = new WifiAdmin(this).getSSID();
                Log.i("MainActivity",ssid);
                if(!ssid.contains(WifiReceiver.WIFI_SSID)){
                    Toast.makeText(this, "先连接至DLUT方可登录", Toast.LENGTH_SHORT).show();
                    return;
                }

                pgProgressbar.setVisibility(View.VISIBLE);
                btnManual.setText("");
                btnManual.setEnabled(false);
                this.startService(new Intent(this, MyService.class));
                break;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mUiReceiver);
    }
}
