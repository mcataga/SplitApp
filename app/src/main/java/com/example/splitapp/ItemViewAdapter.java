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

import org.w3c.dom.Text;

public class ItemViewAdapter extends FirestoreRecyclerAdapter<ItemItem, ItemViewAdapter.ViewHolder> {
    private static final String TAG = "ItemViewAdapter";
    private ItemViewAdapter.OnItemClickListener listener;

    public ItemViewAdapter(@NonNull FirestoreRecyclerOptions<ItemItem> options) {
        super(options);
    }
//

    @Override
    protected void onBindViewHolder(@NonNull ItemViewAdapter.ViewHolder holder, int position, @NonNull ItemItem itemItem) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.itemItemName.setText(itemItem.getName());
        holder.itemPrice.setText("$"+String.format(String.valueOf(itemItem.getPrice())));
        holder.itemSplit.setText("Total Split: $"+ String.format(String.valueOf(itemItem.getSplit())));
    }

    @NonNull
    @Override
    public ItemViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_itemitem, parent, false);
        ItemViewAdapter.ViewHolder holder = new ItemViewAdapter.ViewHolder(view);
        return holder;
    }
    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemItemName;
        TextView itemPrice;
        TextView itemSplit;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            itemItemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemSplit = itemView.findViewById(R.id.totalSplit);
            parentLayout = itemView.findViewById(R.id.ItemRecyclerItem);
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
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(ItemViewAdapter.OnItemClickListener listener) {
        this.listener=listener;
    }
}
