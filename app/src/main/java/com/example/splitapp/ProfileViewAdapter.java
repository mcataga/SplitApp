package com.example.splitapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitapp.db.Profile;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
        holder.bind(profileItem, listener);

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

        public ViewHolder(View itemView) {
            super(itemView);
            profileName = itemView.findViewById(R.id.profileName);
            profileOwed = itemView.findViewById(R.id.amountOwed);
            profileSplit = itemView.findViewById(R.id.totalSplitProfile);
            isVisible = itemView.findViewById(R.id.checkMark);
            parentLayout = itemView.findViewById(R.id.profileLayout);
        }
        public void bind(final ProfileItem item, final OnProfileClickListener listener) {
            Log.d(TAG, "onBindViewHolder: called XD" );
            profileName.setText(item.getName());
            Locale locale = new Locale("en", "US");
            NumberFormat format = NumberFormat.getCurrencyInstance(locale);
            profileOwed.setText(format.format(item.getAmountOwed()));
            profileSplit.setText("Items Split: "+ String.format(String.valueOf(item.getTotalSplit())));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "was inside!!");
                    item.setIsVisible(!item.getIsVisible());
                    isVisible.setVisibility(item.getIsVisible() ? View.VISIBLE : View.GONE);
                    int position = getAdapterPosition();
                    listener.onItemClick(item, getSnapshots().getSnapshot(position));
                }
            });
        }
    }
        public interface OnProfileClickListener {
            void onItemClick(ProfileItem item, DocumentSnapshot documentSnapshot);
        }
    public void setOnProfileClickListener(ProfileViewAdapter.OnProfileClickListener listener) {
        this.listener=listener;
    }

}
