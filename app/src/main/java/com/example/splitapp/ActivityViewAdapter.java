package com.example.splitapp;

import android.content.Context;
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

import java.util.ArrayList;

public class ActivityViewAdapter extends FirestoreRecyclerAdapter<ActivityItem, ActivityViewAdapter.ViewHolder> {
    private static final String TAG = "ActivityViewAdapter";
    private OnItemClickListener listener;

    public ActivityViewAdapter(@NonNull FirestoreRecyclerOptions<ActivityItem> options) {
        super(options);
    }
//

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ActivityItem activityItem) {
              Log.d(TAG, "onBindViewHolder: called");
      holder.activityItemName.setText(activityItem.getName());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView activityItemName;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            activityItemName = itemView.findViewById(R.id.activityItemName);
            parentLayout = itemView.findViewById(R.id.parent_layout);

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
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener=listener;
    }
}
