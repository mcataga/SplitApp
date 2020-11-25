package com.example.splitapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class BillViewAdapter extends FirestoreRecyclerAdapter<BillItem, BillViewAdapter.ViewHolder> {
    private static final String TAG = "BillViewAdapter";
    private BillViewAdapter.OnBillClickListener listener;

    public BillViewAdapter(@NonNull FirestoreRecyclerOptions<BillItem> options) {
        super(options);
    }
//

    @Override
    protected void onBindViewHolder(@NonNull BillViewAdapter.ViewHolder holder, int position, @NonNull BillItem billItem) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.billItemName.setText(billItem.getName());
        holder.billTotalAmount.setText("$"+String.format(String.valueOf(billItem.getTotalPrice())));
    }

    @NonNull
    @Override
    public BillViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_billitem, parent, false);
        BillViewAdapter.ViewHolder holder = new BillViewAdapter.ViewHolder(view);
        return holder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView billItemName;
        TextView billTotalAmount;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            billItemName = itemView.findViewById(R.id.billName);
            billTotalAmount = itemView.findViewById(R.id.billPrice);
            parentLayout = itemView.findViewById(R.id.billRecyclerItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }
    public interface OnBillClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnBillClickListener(BillViewAdapter.OnBillClickListener listener) {
        this.listener=listener;
    }
}
