package com.example.splitapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProfileViewAdapter extends FirestoreRecyclerAdapter<ProfileItem, ProfileViewAdapter.ViewHolder> {
    private static final String TAG = "ItemViewAdapter";

    private ProfileViewAdapter.OnProfileClickListener listener;

    public ProfileViewAdapter(@NonNull FirestoreRecyclerOptions<ProfileItem> options) {
        super(options);

    }
//

    @Override
    protected void onBindViewHolder(@NonNull ProfileViewAdapter.ViewHolder holder, int position, @NonNull ProfileItem profileItem) {
        Log.d(TAG, "onBindViewHolder: called XD" );
        holder.profileName.setText(profileItem.getName());
        holder.profileOwed.setText("$"+String.format(String.valueOf(profileItem.getAmountOwed())));
        holder.profileSplit.setText("Items Split: "+ String.format(String.valueOf(profileItem.getTotalSplit())));
        holder.isVisible.setVisibility(View.GONE);

    }

    @NonNull
    @Override
    public ProfileViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_profileitem, parent, false);
        ProfileViewAdapter.ViewHolder holder = new ProfileViewAdapter.ViewHolder(view);
        return holder;
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView profileName;
        TextView profileOwed;
        TextView profileSplit;
        FloatingActionButton isVisible;
        RelativeLayout parentLayout;
        List<ProfileItem> selectedProfiles;
        public ViewHolder(View itemView) {
            super(itemView);
            profileName = itemView.findViewById(R.id.profileName);
            profileOwed = itemView.findViewById(R.id.amountOwed);
            profileSplit = itemView.findViewById(R.id.totalSplitProfile);
            isVisible = itemView.findViewById(R.id.checkMark);
            parentLayout = itemView.findViewById(R.id.profileLayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        if (isVisible.getVisibility() == View.VISIBLE) {
                            isVisible.setVisibility(View.GONE);
                        }
                        else {
                            if (isVisible.getVisibility() == View.GONE) {
                                    isVisible.setVisibility(View.VISIBLE);
                            }
                        }
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnProfileClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnProfileClickListener(ProfileViewAdapter.OnProfileClickListener listener) {
        this.listener=listener;
    }
}
