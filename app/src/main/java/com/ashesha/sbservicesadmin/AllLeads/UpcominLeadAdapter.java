package com.ashesha.sbservicesadmin.AllLeads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ashesha.sbservicesadmin.MainActivity;
import com.ashesha.sbservicesadmin.R;
import com.ashesha.sbservicesadmin.Utils.PrefManager;


import java.util.ArrayList;

public class UpcominLeadAdapter  extends RecyclerView.Adapter<UpcominLeadAdapter.ViewHolder>  {


    private Context context;
    // private PackageListener packageListener;
    PrefManager prefManager;
    private ArrayList<AllLeadModel> upcomingLeadModels;
    ViewGroup viewGroup;

    public UpcominLeadAdapter(FragmentActivity activity, ArrayList<AllLeadModel> upcomingLeadModels) {

        this.upcomingLeadModels = upcomingLeadModels;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        context = viewGroup.getContext();
        prefManager = new PrefManager(context);
        View view = LayoutInflater.from(context).inflate(R.layout.all_leads_custom_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {



        if (upcomingLeadModels.get(i).getName() != null)
            viewHolder.name.setText(upcomingLeadModels.get(i).getName());
        else
            viewHolder.name.setText("");


        if (upcomingLeadModels.get(i).getCategory_name() != null)
            viewHolder.serviceName.setText(upcomingLeadModels.get(i).getCategory_name());
        else
            viewHolder.serviceName.setText("");

        if (upcomingLeadModels.get(i).getOrder_id() != null)
            viewHolder.orderId.setText(upcomingLeadModels.get(i).getOrder_id());
        else
            viewHolder.orderId.setText("");

        if (upcomingLeadModels.get(i).getTime_slot() != null)
            viewHolder.time.setText(","+upcomingLeadModels.get(i).getTime_slot());
        else
            viewHolder.time.setText("");

        if (upcomingLeadModels.get(i).getService_date() != null)
            viewHolder.date.setText(""+upcomingLeadModels.get(i).getService_date());
        else
            viewHolder.date.setText("");
        viewHolder.addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,MainActivity.class);
                context.startActivity(intent);
                prefManager.setOrderId(upcomingLeadModels.get(i).getOrder_id());
                Log.e("dhhdhd","dhhdhd"+upcomingLeadModels.get(i).getOrder_id());
            }
        });


        viewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:9514222226"));
                context.startActivity(callIntent);
            }
        });
    }
    @Override
    public int getItemCount() {

        return upcomingLeadModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        Button addbtn,call;
        TextView name, serviceName, date,orderId,time;


        ViewHolder(View itemView) {
            super(itemView);
            addbtn = itemView.findViewById(R.id.viewButn);
            name = itemView.findViewById(R.id.name);
            serviceName = itemView.findViewById(R.id.serviceName);
            date = itemView.findViewById(R.id.date);
            time=itemView.findViewById(R.id.time);
            orderId = itemView.findViewById(R.id.orderId);
            call = itemView.findViewById(R.id.call);
        }
    }
}


