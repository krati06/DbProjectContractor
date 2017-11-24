package com.example.divyansh.dbprojectandroid;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
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

public class ShowFeedback extends AppCompatActivity {

    FeedbackAdapter feedbackadapter;
    SharedPreferences preferences;
    String studentId;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_feedback);
        ActionBar actionBar = getActionBar();
        preferences = getSharedPreferences(getString(R.string.MyPREFERENCES), Context.MODE_PRIVATE);
        studentId = preferences.getString("username", "");
        token = preferences.getString("token", "");
        ListView listView = (ListView) findViewById(R.id.feedback_list);
        GetFeedback getFeedback = new GetFeedback(studentId, token);
        feedbackadapter = new FeedbackAdapter(getApplicationContext(), this);
        listView.setAdapter(feedbackadapter);
        getFeedback.execute();

//        actionBar.setDisplayHomeAsUpEnabled(true);
        if (actionBar != null) {
            actionBar.setTitle("Register");
        }
    }


    class GetFeedback extends AsyncTask<Void,Void,Boolean> {
        String server = getResources().getString(R.string.base_url);
        String token;
        String uid;
        String message;
        ArrayList<feedback> feedbacks = new ArrayList<feedback>();

        GetFeedback(String uid, String token){
            this.token = token;
            this.uid = uid;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject Params = new JSONObject();
            String data_sent = "";
            try {
                Params.put("token",token);
                Params.put("username", uid);
                Log.e("params",Params.toString());
                data_sent += "&" + URLEncoder.encode("username", "UTF-8") + "="
                        + URLEncoder.encode(uid, "UTF-8");
                data_sent += "&" + URLEncoder.encode("token", "UTF-8") + "="
                        + URLEncoder.encode(token, "UTF-8");
                URL url = new URL(server+"/ShowFeedback");
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

                JSONObject jsonObject = new JSONObject(sb.toString());
                JSONArray data = jsonObject.getJSONArray("data");

                boolean isValid = (boolean) jsonObject.get("status");
                if(!isValid) {
                   message = jsonObject.getString("message");
                   return false;
                }

                for(int i = 0; i < data.length() ; i++){
                    feedbacks.add(new feedback(
                            data.getJSONObject(i).getString("time"),
                            data.getJSONObject(i).getString("text")));
                }
                Log.d("output", feedbacks.toString());
                return true;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            if(success){
                feedbackadapter.addAll(feedbacks);
                feedbackadapter.notifyDataSetChanged();
            }
            else
                Toast.makeText(ShowFeedback.this, message, Toast.LENGTH_LONG).show();
        }
    }

}
