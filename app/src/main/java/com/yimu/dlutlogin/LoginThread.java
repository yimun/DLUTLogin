package com.yimu.dlutlogin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class LoginThread extends Thread implements Runnable {
	
	private Context context;
    private String loginUrl = "http://w.dlut.edu.cn/cgi-bin/srun_portal";
	public LoginThread(Context con){
		this.context = con;
	}
	
	@Override
	public void run() {
		super.run();
		HttpPost httpRequest = new HttpPost(loginUrl);
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(buildParams(), HTTP.UTF_8)); 
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(httpResponse.getEntity());
				Message msg = new Message();
				msg.obj = result;
				((MyService)context).handler.sendMessage(msg);
				Log.i("LoginThread",result);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private List<NameValuePair> buildParams(){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		SharedPreferences sp = context.getSharedPreferences("dlutlogin_pref", 0);
		boolean isSaved = sp.getBoolean("isSaved", false);
		if(!isSaved)
			return null;
		params.add(new BasicNameValuePair("username", sp.getString("username", "")));
		params.add(new BasicNameValuePair("password", sp.getString("password", "")));
		params.add(new BasicNameValuePair("is_debug", "1"));
		params.add(new BasicNameValuePair("uid", "-1"));
		params.add(new BasicNameValuePair("is_pad", "1"));
		params.add(new BasicNameValuePair("force", "1"));
		params.add(new BasicNameValuePair("type", "1"));
		params.add(new BasicNameValuePair("ac_id", "1"));
		params.add(new BasicNameValuePair("pop", "0"));
		params.add(new BasicNameValuePair("ac_type", "h3c"));
		params.add(new BasicNameValuePair("gateway_auth", "0"));
		params.add(new BasicNameValuePair("local_auth", "1"));
		params.add(new BasicNameValuePair("is_ldap", "0"));
		return params;
	}
	
	

}
