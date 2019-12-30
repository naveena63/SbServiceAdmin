package com.ashesha.sbservicesadmin.SideNavAllJobs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashesha.sbservicesadmin.R;
import com.ashesha.sbservicesadmin.Utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class AssignAdapter extends RecyclerView.Adapter {
    PrefManager prefManager;
    Context context;
    List<AssignModel> assignModelList = new ArrayList<>();
TextView employeeNames,custmorName,serviceName,orderId;

    public AssignAdapter(Context context, List<AssignModel> assignModelList) {
        this.context = context;
        this.assignModelList = assignModelList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.assignjob_layout, viewGroup, false);
        AssignJobsViewHolder assignJobsViewHolder = new AssignJobsViewHolder(view);
        prefManager = new PrefManager(context);
        return assignJobsViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        employeeNames.setText(assignModelList.get(position).getEmpName());
        custmorName.setText(assignModelList.get(position).getCustmorName());

    //    serviceName.setText(assignModelList.get(position).getEmpName());
        orderId.setText(assignModelList.get(position).getOrderId());



    }

    @Override
    public int getItemCount() {
        return assignModelList.size();
    }

    private class AssignJobsViewHolder extends RecyclerView.ViewHolder {
        public AssignJobsViewHolder(@NonNull View itemView) {
            super(itemView);

            employeeNames=itemView.findViewById(R.id.employeeNames);
            custmorName=itemView.findViewById(R.id.CustmorName);
        //    serviceName=itemView.findViewById(R.id.serviceName);
            orderId=itemView.findViewById(R.id.order_id);

        }
    }
}
