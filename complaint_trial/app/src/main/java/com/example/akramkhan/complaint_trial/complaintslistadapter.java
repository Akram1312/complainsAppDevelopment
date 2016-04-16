package com.example.akramkhan.complaint_trial;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Akram Khan on 27-03-2016.
 */
public class complaintslistadapter extends BaseAdapter {

    private Context context;
    private List<complaint> complaintlist;

    public complaintslistadapter(Context context,List<complaint> complaintlist){
        this.context=context;
        this.complaintlist=complaintlist;
    }

    @Override
    public int getCount() {
        return complaintlist.size();
    }

    @Override
    public Object getItem(int position) {
        return complaintlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context,R.layout.item_list,null);
        TextView upvotes = (TextView) v.findViewById(R.id.compupvotes);
        TextView downvotes = (TextView) v.findViewById(R.id.compdownvotes);
        TextView title = (TextView) v.findViewById(R.id.comptitle);
        TextView complaintid = (TextView) v.findViewById(R.id.complaintid);
        TextView time = (TextView) v.findViewById(R.id.times);
        View colorcode = v.findViewById(R.id.colorcode);

        title.setText(complaintlist.get(position).getComplaintTitle());
        complaintid.setText(complaintlist.get(position).getComplaintid());
        time.setText(complaintlist.get(position).getTime());
        upvotes.setText(""+complaintlist.get(position).getUpVotes());
        downvotes.setText(""+complaintlist.get(position).getDownVotes());

        switch(complaintlist.get(position).getColorcode()){
            case "0":
                colorcode.setBackgroundColor(Color.parseColor("#ff9966"));
                break;
            case "1":
                colorcode.setBackgroundColor(Color.parseColor("#ffffcc"));
                break;
            case "2":
                colorcode.setBackgroundColor(Color.parseColor("#ccffcc"));
                break;
        }
        v.setTag(complaintlist.get(position).getComplaintid());

        return v;
    }
}
