package com.ashesha.sbservicesadmin.BalanceSheet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashesha.sbservicesadmin.R;

import java.util.List;


public class BalanceSheetAdapter extends RecyclerView.Adapter<BalanceSheetAdapter.BalanceSheetViewHolder> {

    private Context context;
    private List<BalanceSheetModel> balanceSheetModelArrayList;


    BalanceSheetAdapter(Context context, List<BalanceSheetModel> balanceSheetModelArrayList) {
        this.context = context;
        this.balanceSheetModelArrayList = balanceSheetModelArrayList;
    }

    @NonNull
    @Override
    public BalanceSheetViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.balance_sheet_layout, viewGroup, false);

        return new BalanceSheetViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull BalanceSheetViewHolder viewHolder, int position) {

        if (balanceSheetModelArrayList.get(position).getOrder_id().length() > 0)
            viewHolder.orderId.setText("OrderId :" + balanceSheetModelArrayList.get(position).getOrder_id());
        else
            viewHolder.orderId.setText("OrderId :");

        if (balanceSheetModelArrayList.get(position).getPrevious_balance().length() > 0)
            viewHolder.previousBal.setText("Previous Balance:" + balanceSheetModelArrayList.get(position).getPrevious_balance());
        else
            viewHolder.previousBal.setText("Previous Balance:");

        if (balanceSheetModelArrayList.get(position).getReason().length() > 0)
            viewHolder.reson.setText("Reason :" + balanceSheetModelArrayList.get(position).getReason());
        else
            viewHolder.reson.setText("Reason :");

        if (balanceSheetModelArrayList.get(position).getTotalbalance().length() > 0)
            viewHolder.totalBalance.setText("Total Balance :" + balanceSheetModelArrayList.get(position).getTotalbalance());
        else
            viewHolder.totalBalance.setText("Total Balance :");

    }

    @Override
    public int getItemCount() {
        return balanceSheetModelArrayList.size();
    }

    class BalanceSheetViewHolder extends RecyclerView.ViewHolder {

        TextView previousBal, orderId, reson, totalBalance;

        BalanceSheetViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.textViewOrderId);
            previousBal = itemView.findViewById(R.id.textViewPreviousBal);
            reson = itemView.findViewById(R.id.amountReason);
            totalBalance = itemView.findViewById(R.id.amountTotalBal);


        }
    }
}