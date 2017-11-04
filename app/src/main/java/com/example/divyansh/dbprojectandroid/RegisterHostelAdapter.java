package com.example.divyansh.dbprojectandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Divyansh on 11/4/2017.
 */

public class RegisterHostelAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<Hostel> HostelList;

    public RegisterHostelAdapter(Context context, ArrayList<Hostel> HostelList) {
        super();
        this.context = context;
        this.HostelList = HostelList;
    }

    public class Holder
    {
        TextView HostelId;
        TextView WaitListNum;
        Button   Register;
    }


    @Override
    public int getCount() {
        return HostelList.size();
    }

    @Override
    public Object getItem(int position) {
        return HostelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final RegisterHostelAdapter.Holder holder;
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

        holder.HostelId.setText(HostelList.get(position).getUid());
        holder.WaitListNum.setText(HostelList.get(position).getWaitList());
        return convertView;
    }
}
