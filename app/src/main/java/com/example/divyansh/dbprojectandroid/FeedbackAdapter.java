package com.example.divyansh.dbprojectandroid;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Divyansh on 11/24/2017.
 */

public class FeedbackAdapter extends ArrayAdapter<feedback> {
    public Context context;
    public Activity activity;

    public FeedbackAdapter(Context context, Activity activity) {
        super(context, 0);
        this.context = context;
        this.activity = activity;
    }

    public class feedBackHolder {
        TextView Timestamp;
        TextView Text;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final feedBackHolder holder;
        feedback feeback = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.feedback_card, parent, false);
            holder = new feedBackHolder();
            holder.Timestamp = convertView.findViewById(R.id.feedback_timestamp);
            holder.Text = convertView.findViewById(R.id.feedback_text);
            convertView.setTag(holder);
        } else {
            holder = (feedBackHolder) convertView.getTag();
        }

        holder.Timestamp.setText(feeback.getTimestamp());
        holder.Text.setText(feeback.getText());
        convertView.setOnClickListener(
                new View.OnClickListener() {
                    //@Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(activity)
                                .setIcon(R.drawable.warning)
                                .setTitle(holder.Timestamp.getText())
                                .setMessage(holder.Text.getText())
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }

                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }

                                }).show();
                    }
                }
        );
        return convertView;
    }
}

