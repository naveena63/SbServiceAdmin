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

public class TodayLeadAdapter extends RecyclerView.Adapter<TodayLeadAdapter.ViewHolder>  {


    private Context context;
    PrefManager prefManager;
    private ArrayList<AllLeadModel> todayLeadModels;
     ViewGroup viewGroup;

    public TodayLeadAdapter(Context context, ArrayList<AllLeadModel> todayLeadModels) {
        this.context = context;
        this.todayLeadModels = todayLeadModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.all_leads_custom_layout, viewGroup, false);
        prefManager = new PrefManager(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {


         if(todayLeadModels.get(i).getName()!=null)

             viewHolder.name.setText(todayLeadModels.get(i).getName());
         else
             viewHolder.name.setText("");

         if(todayLeadModels.get(i).getCategory_name()!=null)
             viewHolder.serviceName.setText(todayLeadModels.get(i).getCategory_name());
         else
             viewHolder.serviceName.setText("");
         if(todayLeadModels.get(i).getService_date()!=null)
             viewHolder.date.setText(todayLeadModels.get(i).getService_date());
         else
             viewHolder.date.setText("");
        if(todayLeadModels.get(i).getOrder_id()!=null)
            viewHolder.orderId.setText(todayLeadModels.get(i).getOrder_id());
        else
            viewHolder.orderId.setText("");

        if(todayLeadModels.get(i).getTime_slot()!=null)
            viewHolder.time.setText(" ,"+todayLeadModels.get(i).getTime_slot());
        else
            viewHolder.time.setText("");


        viewHolder.addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,MainActivity.class);
                context.startActivity(intent);
                prefManager.setOrderId(todayLeadModels.get(i).getOrder_id());
                Log.e("dhhdhd","dhhdhd"+todayLeadModels.get(i).getOrder_id());
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

        return todayLeadModels.size();
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
            orderId = itemView.findViewById(R.id.orderId);
            time = itemView.findViewById(R.id.time);
            call = itemView.findViewById(R.id.call);
        }
    }
}

