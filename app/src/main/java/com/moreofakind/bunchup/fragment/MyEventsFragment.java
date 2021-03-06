package com.moreofakind.bunchup.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyEventsFragment extends EventListFragment {

    public MyEventsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // All my posts
        return databaseReference.child("user-initiatives").child(getUid()).orderByChild("epoch").startAt(System.currentTimeMillis()).limitToFirst(100);
    }
}
