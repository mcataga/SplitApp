package com.example.splitapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.splitapp.dummy.ActivityContent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link UserActivityDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class UserActivityListActivity extends AppCompatActivity {


    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private ArrayList<ActivityContent.ActivityItem> ITEMS = new ArrayList<ActivityContent.ActivityItem>();
    private String TAG = "UserActivityListClass";
    private boolean mTwoPane;
    private Boolean elementAdded = false;
    private HashMap<String, ActivityContent.ActivityItem> ITEM_MAP  = new HashMap<String, ActivityContent.ActivityItem>();
    private RecyclerView recyclerView;
    SimpleItemRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useractivity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        setContentView(R.layout.activity_useractivity_list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                startActivity(intent);
            }
        });

        if (findViewById(R.id.useractivity_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


        assert recyclerView != null;
        recyclerView = findViewById(R.id.useractivity_list);
        setupRecyclerView(recyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "Error filling lists from db!", Toast.LENGTH_LONG).show();
            FirebaseFirestore fStore = FirebaseFirestore.getInstance();
            FirebaseAuth fAuth = FirebaseAuth.getInstance();
            ITEMS.clear();
            ITEM_MAP.clear();
            fStore.collection("users").document(fAuth.getCurrentUser().getUid()).collection("activities").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.d(TAG, "Data query failed");
                        return;
                    }
                    if (value != null) {
                        for (QueryDocumentSnapshot document : value) {
                            ITEM_MAP.put(document.getId(), new ActivityContent.ActivityItem(document.getId(), document.get("name").toString()));
                            ITEMS.add(new ActivityContent.ActivityItem(document.getId(), document.get("name").toString()));
                            Toast.makeText(getApplicationContext(), ITEMS.toString(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setupRecyclerView(recyclerView);


    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter = new SimpleItemRecyclerViewAdapter(this, ITEMS, mTwoPane);
        recyclerView.setAdapter(adapter);

    }
    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final UserActivityListActivity mParentActivity;
        private final List<ActivityContent.ActivityItem> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityContent.ActivityItem item = (ActivityContent.ActivityItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(UserActivityDetailFragment.ARG_ITEM_ID, item.id);
                    arguments.putString(UserActivityDetailFragment.ARG_ITEM_NAME, item.name);
                    arguments.putSerializable("ITEM_MAP",ITEM_MAP);
                    UserActivityDetailFragment fragment = new UserActivityDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.useractivity_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, UserActivityDetailActivity.class);
                    intent.putExtra(UserActivityDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(UserActivityListActivity parent,
                                      List<ActivityContent.ActivityItem> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.useractivity_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).name);
            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            if (mValues == null) {return 0;}
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}