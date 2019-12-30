package com.ashesha.sbservicesadmin.Invoice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashesha.sbservicesadmin.R;
import com.ashesha.sbservicesadmin.SideNavAllJobs.ViewInvoiceActivity;

import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {


    private Context context;
    private List<InvoiceModel> invoiceModelList;
    public InvoiceAdapter(InvoiceActivity invoiceActivity,List<InvoiceModel> invoiceModels) {
        this.invoiceModelList = invoiceModels;

    }

    public InvoiceAdapter(ViewInvoiceActivity viewInvoiceActivity, List<InvoiceModel> invoiceModels) {
        this.invoiceModelList = invoiceModels;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        View view=LayoutInflater.from(context).inflate(R.layout.invoice_layout,viewGroup,false);


        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,@SuppressLint("RecyclerView") final int i) {

        viewHolder.category_name.setText(invoiceModelList.get(i).getCategory_name());
        viewHolder.cost.setText("Rs."+invoiceModelList.get(i).getPrice());
        viewHolder.qty.setText("(Qty)"+invoiceModelList.get(i).getQty());
        viewHolder.packageName.setText(invoiceModelList.get(i).getPackage_name());


    }

    @Override
    public int getItemCount() {
        return invoiceModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView category_name,cost,qty,packageName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            category_name = itemView.findViewById(R.id.category_name);
            cost = itemView.findViewById(R.id.cost);
            qty=itemView.findViewById(R.id.qty);
            packageName=itemView.findViewById(R.id.package_name);
        }
    }

}
