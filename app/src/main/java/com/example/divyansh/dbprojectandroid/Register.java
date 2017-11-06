package com.example.divyansh.dbprojectandroid;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class Register extends AppCompatActivity {

    RegisterHostelAdapter registerHostelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getActionBar();
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String studentId = preferences.getString("studentId", "");
        ListView listView = (ListView) findViewById(R.id.register_list_view);
        GetHostels getHostels = new GetHostels(studentId);
        registerHostelAdapter = new RegisterHostelAdapter(getApplicationContext());
        listView.setAdapter(registerHostelAdapter);

//        actionBar.setDisplayHomeAsUpEnabled(true);
        if (actionBar != null) {
            actionBar.setTitle("Register");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
          //  return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    class GetHostels extends AsyncTask<Void,Void,Boolean>{
        String server = getResources().getString(R.string.server)+"/GetHostels";
        String studentId;
        ArrayList<Hostel> hostels = new ArrayList<Hostel>();

        GetHostels (String uid){
            studentId = uid;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                String urlParameters = "uid="+studentId;
                URL url = new URL(getResources().getString(R.string.server)+"SeeUserPosts");
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

                writer.write(urlParameters);
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

                for(int i = 0; i < data.length() ; i++){
                    hostels.add(new Hostel(
                            data.getJSONObject(i).getString("hostel_id"),
                            data.getJSONObject(i).getString("waitlist_number")));
                }
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
                registerHostelAdapter.addAll(hostels);
                registerHostelAdapter.notifyDataSetChanged();
            }
        }
    }
}
