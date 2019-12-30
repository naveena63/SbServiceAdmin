
package com.ashesha.sbservicesadmin.CancelReason;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ashesha.sbservicesadmin.R;
import com.ashesha.sbservicesadmin.Utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class CancelReasonAdapter extends RecyclerView.Adapter {
    PrefManager prefManager;
    Context context;
    List<CancelReasonModel> cancelReasonModelList=new ArrayList<>();
    CheckBox checkbox;

    public CancelReasonAdapter(Context context, List<CancelReasonModel> cancelReasonModelList) {
        this.context = context;
        this.cancelReasonModelList = cancelReasonModelList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view= layoutInflater.inflate(R.layout.cancel_reasoncustom_layout,viewGroup,false);
        CancelReasonViewHolder cancelReasonViewHolder=new CancelReasonViewHolder(view);
        prefManager = new PrefManager(context);
       return cancelReasonViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        checkbox.setText(cancelReasonModelList.get(position).getDescription());
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  //  ((CancelReasonActivity)context).saveCancelReason();
                    prefManager.setReson_id(cancelReasonModelList.get(position).getId());

                Log.e("hhdhdh","dhdhd"+cancelReasonModelList.get(position).getId());


            }
        });

    }


    @Override
    public int getItemCount() {
        return cancelReasonModelList.size();
    }

    private class CancelReasonViewHolder extends RecyclerView.ViewHolder {
        public CancelReasonViewHolder(@NonNull View itemView) {
            super(itemView);

            checkbox=itemView.findViewById(R.id.rb);


        }

    }
}
