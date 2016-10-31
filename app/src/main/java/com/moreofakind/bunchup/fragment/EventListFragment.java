package com.moreofakind.bunchup.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.moreofakind.bunchup.EventDetailActivity;
import com.moreofakind.bunchup.R;
import com.moreofakind.bunchup.models.Event;
import com.moreofakind.bunchup.viewholder.EventViewHolder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class EventListFragment extends Fragment {

    private static final String TAG = "EventListFragment";

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<Event, EventViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    public EventListFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_events, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        mRecycler = (RecyclerView) rootView.findViewById(R.id.messages_list);
        mRecycler.setHasFixedSize(true);
        Log.d(TAG,"onCreateView");

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        //mManager.setReverseLayout(true);
        //mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query eventsQuery = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(Event.class, R.layout.item_event,
                EventViewHolder.class, eventsQuery) {
            @Override
            protected void populateViewHolder(final EventViewHolder viewHolder, final Event model, final int position) {
                final DatabaseReference eventRef = getRef(position);

                // Set click listener for the whole event view
                final String eventKey = eventRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch EventDetailActivity
                        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                        intent.putExtra(EventDetailActivity.EXTRA_EVENT_KEY, eventKey);
                        startActivity(intent);
                    }
                });

                // Determine if the current user has liked this event and set UI accordingly

                if (model.stars.containsKey(getUid())) {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_24);
                } else {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);
                }

                // Bind Event to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToEvent(model, getUid(), new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        Map<String, Object> childUpdates = new HashMap<>();
                        if (model.stars.containsKey(getUid())) {
                            model.starCount = model.starCount - 1;
                            model.stars.remove(getUid());
                            childUpdates.put("/user-participations/" + getUid() + "/" + eventRef.getKey(),null);
                            childUpdates.put("/events/" + eventRef.getKey(), model.toMap());
                            childUpdates.put("/user-initiatives/" + model.uid + "/" + eventRef.getKey(), model.toMap());
                            Iterator starit = model.stars.keySet().iterator();
                            while (starit.hasNext()) {
                                String star = starit.next().toString();
                                Log.d("xxx",star.toString());
                                childUpdates.put("/user-participations/" + star + "/" + eventRef.getKey(), model.toMap());
                            }
                        } else {
                            model.starCount = model.starCount + 1;
                            model.stars.put(getUid(), true);
                            childUpdates.put("/events/" + eventRef.getKey(), model.toMap());
                            childUpdates.put("/user-initiatives/" + model.uid + "/" + eventRef.getKey(), model.toMap());
                            Iterator starit = model.stars.keySet().iterator();
                            while (starit.hasNext()) {
                                childUpdates.put("/user-participations/" + starit.next().toString() + "/" + eventRef.getKey(), model.toMap());
                            }
                        }
                        mDatabase.updateChildren(childUpdates);
                    }
                });
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);

}
