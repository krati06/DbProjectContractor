package com.example.divyansh.dbprojectandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SetLimit extends AppCompatActivity {

    SharedPreferences preferences;
    Button setLimit;
    EditText limit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_limit);
        setLimit = (Button)findViewById(R.id.set_limit_button);
        limit = (EditText) findViewById(R.id.limit);
        // set on click on Image View
        preferences = getSharedPreferences(getString(R.string.MyPREFERENCES), Context.MODE_PRIVATE);
        final String uid = preferences.getString("username", "");
        final String token = preferences.getString("token", "");
        setLimit.setOnClickListener(
                new View.OnClickListener() {
                    //@Override
                    public void onClick(View v) {
                        setLimit async = new setLimit(uid, token);
                        async.execute();
                    }
                }
        );
    }

    class setLimit extends AsyncTask<Void,Void,Boolean> {
        String uid;
        String token;
        Boolean isValid;
        String message;

        setLimit(String uid, String token){
            this.uid = uid;
            this.token = token;
        }

        @Override
        protected Boolean doInBackground(Void... arg) {
            JSONObject Params = new JSONObject();
            String data_sent = "";
            try {
                Params.put("token",token);
                Params.put("username",uid);
                Params.put("limit",limit.getText());
                Log.e("params",Params.toString());
                data_sent += "&" + URLEncoder.encode("username", "UTF-8") + "="
                        + URLEncoder.encode(uid, "UTF-8");
                data_sent += "&" + URLEncoder.encode("token", "UTF-8") + "="
                        + URLEncoder.encode(token, "UTF-8");
                data_sent += "&" + URLEncoder.encode("limit", "UTF-8") + "="
                        + URLEncoder.encode(limit.getText().toString(), "UTF-8");
                String base_url = getString(R.string.base_url);
                URL url = new URL(base_url+"/SetStudentLimit");
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

                writer.write(data_sent);
                writer.flush();

                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                writer.close();
                reader.close();
                Log.e("output", sb.toString());
                JSONObject jsonObject = new JSONObject(sb.toString());
                isValid = (boolean) jsonObject.get("status");
                message = jsonObject.getString("message");
                return true;
            } catch (Exception e) {
                message = e.getMessage();
                return false;
            }
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            Toast.makeText(SetLimit.this,
                    message, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
