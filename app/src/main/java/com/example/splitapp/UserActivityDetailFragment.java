package com.example.splitapp;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.splitapp.dummy.ActivityContent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * A fragment representing a single UserActivity detail screen.
 * This fragment is either contained in a {@link UserActivityListActivity}
 * in two-pane mode (on tablets) or a {@link UserActivityDetailActivity}
 * on handsets.
 */
public class UserActivityDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_NAME = "item_name";

    /**
     * The dummy content this fragment is presenting.
     */
    private ActivityContent.ActivityItem mItem;
    private HashMap<String, ActivityContent.ActivityItem> ITEM_MAP;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private String TAG = "UserActivityFragment";
    private EditText activityName;
    public UserActivityDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        activityName = getActivity().findViewById(R.id.editActivityName);
        getActivity().findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = fStore.collection("users").document(fAuth.getCurrentUser().getUid()).collection("activities").document(mItem.id);
                documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Activity has been deleted");
                            getActivity().finish();
                        } else {
                            Log.d(TAG, "error can't delete activity");
                        }
                    }
                });
            }
        });
        getActivity().findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = activityName.getText().toString().trim();
                DocumentReference documentReference = fStore.collection("users").document(fAuth.getCurrentUser().getUid()).collection("activities").document(mItem.id);
                documentReference.update("name", name).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Activity has been updated");
                            getActivity().finish();
                        } else {
                            Log.d(TAG, "error can't update activity");
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle savedInstanceState = this.getArguments();
        Toast.makeText(getContext(), "THIS IS MAP FRAG" + ITEM_MAP.size(), Toast.LENGTH_LONG).show();
            ITEM_MAP = (HashMap<String, ActivityContent.ActivityItem>) savedInstanceState.getSerializable("ITEM_MAP");
            Toast.makeText(getContext(), "THIS IS MAP FRAG" + ITEM_MAP.toString(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "TESTTEST");
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            Toast.makeText(getContext(), "THIS IS ID FRAG" + getArguments().getString(ARG_ITEM_ID), Toast.LENGTH_LONG).show();
            mItem = ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            Toast.makeText(getContext(), mItem.toString(), Toast.LENGTH_LONG).show();
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.name);
            }
            activityName.setText(mItem.name);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.useractivity_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.useractivity_detail)).setText("FILLER TEXT 1");
        }

        return rootView;
    }
}