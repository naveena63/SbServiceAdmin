package com.ashesha.sbservicesadmin.SideNavAllJobs;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ashesha.sbservicesadmin.BalanceSheet.BalanceSheetActivity;
import com.ashesha.sbservicesadmin.BalanceSheet.BalanceSheetadminActivity;
import com.ashesha.sbservicesadmin.R;
import com.ashesha.sbservicesadmin.Utils.PrefManager;

import java.util.List;

public class AllEmployeesAdapter extends RecyclerView.Adapter<AllEmployeesAdapter.ViewHolder> {

    List<AllEmployeesModel> allEmployeesModelList;
    Context context;
    PrefManager prefManager;
    public AllEmployeesAdapter(AllEmployeesActivity allEmployeesActivity, List<AllEmployeesModel> allEmployeesModelList) {
        this.allEmployeesModelList = allEmployeesModelList;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.all_emp_layout, viewGroup, false);
        prefManager = new PrefManager(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.empId.setText(allEmployeesModelList.get(i).getAdminEmpId());
        viewHolder.empName.setText(allEmployeesModelList.get(i).getEmpName());
        viewHolder.empCompanuNum.setText(allEmployeesModelList.get(i).getEmpCompnyNum());
        viewHolder.empAddress.setText(allEmployeesModelList.get(i).getEmpAddress());
        viewHolder.empFatherName.setText(allEmployeesModelList.get(i).getEmFName());
        viewHolder.empPersonalNum.setText(allEmployeesModelList.get(i).getEmpPersnlNum());
        viewHolder.empRating.setText(allEmployeesModelList.get(i).getEmpRatng());
        viewHolder.emapAdhar.setText(allEmployeesModelList.get(i).getEmpAdhar());
        viewHolder.empPan.setText(allEmployeesModelList.get(i).getEmpPan());
        viewHolder.empAcountMoney.setText(allEmployeesModelList.get(i).getEmpAccntMony());
        viewHolder.empBank.setText(allEmployeesModelList.get(i).getBank());


        viewHolder.buttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BalanceSheetadminActivity.class);
                context.startActivity(intent);
                prefManager.setAdminEmpId(allEmployeesModelList.get(i).getAdminEmpId());
            }
        });
        viewHolder.updateButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return allEmployeesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView empId, empName, empCompanuNum,empPersonalNum,empFatherName,empRating,emapAdhar,empPan,empBank,empAcountMoney,empAddress;
        Button buttn,updateButn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            empId = itemView.findViewById(R.id.empid);
            empName = itemView.findViewById(R.id.empName);
            empCompanuNum = itemView.findViewById(R.id.empCmpNum);
            empPersonalNum = itemView.findViewById(R.id.EmpPersonalNum);
            empFatherName = itemView.findViewById(R.id.fatherName);
            empRating = itemView.findViewById(R.id.rating);
            emapAdhar = itemView.findViewById(R.id.AdharCrad);
            empPan = itemView.findViewById(R.id.panCardNum);
            empBank = itemView.findViewById(R.id.bank);
            empAcountMoney = itemView.findViewById(R.id.EmpAccountMoney);
            empAddress = itemView.findViewById(R.id.empAddres);
            buttn = itemView.findViewById(R.id.viewButn);
            updateButn = itemView.findViewById(R.id.updateButn);

        }
    }
}
