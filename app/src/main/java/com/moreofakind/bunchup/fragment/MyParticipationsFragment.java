package com.moreofakind.bunchup.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyParticipationsFragment extends EventListFragment {

    public MyParticipationsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START my_top_posts_query]
        // My top posts by number of stars
        //String myUserId = getUid();
        //Query myTopPostsQuery = databaseReference.child("events").child.("stars").child(myUserId)
        //        .orderByChild("starCount");
        //return myTopPostsQuery;
        return databaseReference.child("user-participations").child(getUid()).orderByChild("epoch").startAt(System.currentTimeMillis()).limitToFirst(100);
        // [END my_top_posts_query]


    }
}
