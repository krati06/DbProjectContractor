package com.example.divyansh.dbprojectandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Divyansh on 11/4/2017.
 */

public class RegisterHostelAdapter extends ArrayAdapter<Hostel> {

    public Context context;

    public RegisterHostelAdapter(Context context) {
        super(context, 0);
        this.context = context;
    }

    public class Holder
    {
        TextView HostelId;
        TextView WaitListNum;
        Button   Register;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final RegisterHostelAdapter.Holder holder;
        Hostel hostel = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.hostel_card, parent, false);
            holder = new RegisterHostelAdapter.Holder();
            holder.HostelId = (TextView) convertView.findViewById(R.id.hostelName);
            holder.WaitListNum = (TextView) convertView.findViewById(R.id.waitList);
            holder.Register = (Button) convertView.findViewById(R.id.register);
            convertView.setTag(holder);
        } else {
            holder = (RegisterHostelAdapter.Holder) convertView.getTag();
        }

        holder.HostelId.setText(hostel.getHostel_id());
        holder.WaitListNum.setText(hostel.getWaitList());
        return convertView;
    }
}
