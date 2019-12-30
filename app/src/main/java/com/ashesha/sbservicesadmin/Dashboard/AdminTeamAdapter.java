package com.ashesha.sbservicesadmin.Dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashesha.sbservicesadmin.R;

import java.util.ArrayList;

public class AdminTeamAdapter extends RecyclerView.Adapter<AdminTeamAdapter.ViewHolder>  {


    private Context context;
    // private PackageListener packageListener;
    private ArrayList<AdminteamModel> adminteamModels;
    ViewGroup viewGroup;

    public AdminTeamAdapter(AdminteamActivity navDashboardActivity, ArrayList<AdminteamModel> adminteamModels) {
        this.adminteamModels = adminteamModels;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.admin_team_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {


        if (adminteamModels.get(i).getEmpName() != null)
            viewHolder.empName.setText(adminteamModels.get(i).getEmpName());
        else
            viewHolder.empName.setText("");


        if (adminteamModels.get(i).getEmpid() != null)
            viewHolder.empId.setText(adminteamModels.get(i).getEmpid());
        else
            viewHolder.empId.setText("");

        if (adminteamModels.get(i).getEmpAccountMoney() != null)
            viewHolder.amount.setText(adminteamModels.get(i).getEmpAccountMoney());
        else
            viewHolder.amount.setText("");






    }
    @Override
    public int getItemCount() {

        return adminteamModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView empName, empId, amount;

        ViewHolder(View itemView) {
            super(itemView);

            empName = itemView.findViewById(R.id.empName);
            empId = itemView.findViewById(R.id.empId);
            amount = itemView.findViewById(R.id.amount);

        }
    }
}
