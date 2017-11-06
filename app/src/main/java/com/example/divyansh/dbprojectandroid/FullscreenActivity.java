package com.example.divyansh.dbprojectandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    private Handler mHandler = new Handler();
    private Handler mHandler_async = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoginTask mAuthTask = null;
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_fullscreen);

        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.MyPREFERENCES), Context.MODE_PRIVATE);
        String token = sharedpreferences.getString("token", "");
        String username = sharedpreferences.getString("username","");
        if(token.equals("") | username.equals(""))
        {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(FullscreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000); // 2 seconds
        }
        else
        {
            mAuthTask = new LoginTask(username,token);
            mAuthTask.execute((Void) null);

        }
    }

    public class LoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String token;
        private final String username;
        private final String MyPREFERENCES = "pref";
        URL url;
        LoginTask(String in1, String in2) {
            token = in2;
            username = in1;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject Params = new JSONObject();
            String data = "";
            try {
                Params.put("token", token);
                Params.put("username",username);
                Log.e("params",Params.toString());
                data = URLEncoder.encode("token", "UTF-8")
                        + "=" + URLEncoder.encode(token, "UTF-8");
                data += "&" + URLEncoder.encode("username", "UTF-8") + "="
                        + URLEncoder.encode(username, "UTF-8");
                String base_url = getString(R.string.base_url);
                url = new URL(base_url + "/Fullscreen");
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(data);
                writer.flush();
                writer.close();
                os.close();
                int responseCode=conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK){ //This is 200
                    BufferedReader in=new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    Log.e("RESPONSE FROM SERVER", sb.toString());

                    JSONObject json = new JSONObject(sb.toString());
                    boolean isValid = (boolean) json.get("status");
                    if(!isValid)
                        return false;
                    String token = (String) json.get("token");
                    String username = (String) json.get("username");
                    SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.MyPREFERENCES), Context.MODE_PRIVATE);
                    Editor editor = sharedpreferences.edit();
                    editor.putString("token", token);
                    editor.putString("username", username);
                    editor.commit();
                    return isValid;
                }

            } catch (ProtocolException e) {
                e.printStackTrace();
                return false;
            }catch (IOException i){
                i.printStackTrace();
                return false;
            }catch (JSONException j){
                j.printStackTrace();
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {

                mHandler_async.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(FullscreenActivity.this,StartPage.class);
                        startActivity(intent);
                        finish();
                    }
                }, 2000); // 2 seconds

            } else {

                mHandler_async.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(FullscreenActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 2000); // 2 seconds
            }
        }
    }
}
