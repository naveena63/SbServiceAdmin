package com.ashesha.sbservicesadmin.SideNavAllJobs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.ashesha.sbservicesadmin.MainActivity;
import com.ashesha.sbservicesadmin.R;
import com.ashesha.sbservicesadmin.Utils.PrefManager;

import java.util.ArrayList;

public class CancelledjobAdapter extends RecyclerView.Adapter<CancelledjobAdapter.ViewHolder>  {

PrefManager prefManager;
    private Context context;
    // private PackageListener packageListener;
    private ArrayList<AllJobsModel> cancelledJobModels;
    ViewGroup viewGroup;

    public CancelledjobAdapter(CancelledJobActivity cancelledJobActivity, ArrayList<AllJobsModel> cancelledJobModels) {
        this.cancelledJobModels = cancelledJobModels;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.nav_jobs_custom_layout, viewGroup, false);
        prefManager=new PrefManager(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {


        if (cancelledJobModels.get(i).getName() != null)
            viewHolder.name.setText(cancelledJobModels.get(i).getName());
        else
            viewHolder.name.setText("");


        if (cancelledJobModels.get(i).getCategory_name() != null)
            viewHolder.serviceName.setText(cancelledJobModels.get(i).getCategory_name());
        else
            viewHolder.serviceName.setText("");

        if (cancelledJobModels.get(i).getOrder_id() != null)
            viewHolder.orderId.setText(cancelledJobModels.get(i).getOrder_id());
        else
            viewHolder.orderId.setText("");

        if (cancelledJobModels.get(i).getService_date() != null)
            viewHolder.date.setText(cancelledJobModels.get(i).getService_date());
        else
            viewHolder.date.setText("");

        if (cancelledJobModels.get(i).getTime_slot() != null)
            viewHolder.time.setText(" ,"+cancelledJobModels.get(i).getTime_slot());
        else
            viewHolder.time.setText("");

        viewHolder.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ViewCancelledActivity.class);

                prefManager.setOrderId(cancelledJobModels.get(i).getOrder_id());
                context.startActivity(intent);
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

        return cancelledJobModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        Button viewBtn,call;
        TextView name, serviceName, date,orderId,time;
        ViewHolder(View itemView) {
            super(itemView);
            viewBtn = itemView.findViewById(R.id.viewButn);
            name = itemView.findViewById(R.id.name);
            serviceName = itemView.findViewById(R.id.serviceName);
            date = itemView.findViewById(R.id.date);
            orderId = itemView.findViewById(R.id.orderId);
            call=itemView.findViewById(R.id.call);
            time=itemView.findViewById(R.id.time);

        }
    }
}

