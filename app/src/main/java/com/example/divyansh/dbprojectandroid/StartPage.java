package com.example.divyansh.dbprojectandroid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class StartPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Handler mHandler = new Handler();
    private Handler mHandler_async = new Handler();

    View progressOverlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressOverlay = findViewById(R.id.progress_overlay);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            progressOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
            progressOverlay.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
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
        if (id == R.id.action_Logout) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.warning)
                    .setTitle("Logout Activity")
                    .setMessage("Are you sure you want to Logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.MyPREFERENCES), Context.MODE_PRIVATE);
                            String username = sharedpreferences.getString("username","");
                            LogoutTask mAuthTask = new LogoutTask(username);
                            mAuthTask.execute((Void) null);
                            showProgress(true);
                            dialog.dismiss();
                        }

                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }

                    })
                    .show();
            return true;
        } else if (id == R.id.action_Optout)
        {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.warning)
                    .setTitle("OptOut Activity")
                    .setMessage("Are you sure you want to Opt out?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // call to optout
                            SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.MyPREFERENCES), Context.MODE_PRIVATE);
                            String token = sharedpreferences.getString("token", "");
                            String username = sharedpreferences.getString("username","");
                            OptoutTask mAuthTask = new StartPage.OptoutTask(username,token);
                            mAuthTask.execute((Void) null);
                            showProgress(true);
                            dialog.dismiss();

                        }

                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }

                    })
                    .show();
            return  true;
        }



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_feedback) {
            Intent intent = new Intent(StartPage.this,ShowFeedback.class);
            startActivity(intent);
        } else if (id == R.id.nav_limit) {
            Intent intent = new Intent(StartPage.this,SetLimit.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class LogoutTask extends AsyncTask<Void, Void, Boolean> {

        private final String username;
        URL url;
        LogoutTask(String in1) {
            username = in1;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject Params = new JSONObject();
            String data = "";
            try {
                Params.put("username",username);
                Log.e("params",Params.toString());
                data += "&" + URLEncoder.encode("username", "UTF-8") + "="
                        + URLEncoder.encode(username, "UTF-8");
                String base_url = getString(R.string.base_url);
                url = new URL(base_url + "/LogoutContractor");
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

                    SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.MyPREFERENCES), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("token", "");
                    editor.putString("username", "");
                    editor.commit();
                    return true;
                }

            } catch (ProtocolException e) {
                e.printStackTrace();
                return false;
            }catch (IOException i){
                i.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);
            Intent intent = new Intent(StartPage.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public class OptoutTask extends AsyncTask<Void, Void, Boolean> {

        private final String token;
        private final String username;
        private final String MyPREFERENCES = "pref";
        URL url;
        OptoutTask(String in1, String in2) {
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
                url = new URL(base_url + "/OptOut");
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
                    Log.e("RESPONSE ok FROM SERVER", "1");
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
                        Toast.makeText(getApplicationContext(),"Opted out successfully",Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                }, 2000); // 2 seconds

            } else {

                mHandler_async.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Failed Optout, Try again!",Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                }, 2000); // 2 seconds
            }
        }
    }
}
