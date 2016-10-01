package com.moreofakind.bunchup.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// [START event_class]
@IgnoreExtraProperties
public class Event {

    public String uid;
    public String author;
    public String title;
    public long epoch;
    public String body;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Event() {
        // Default constructor required for calls to DataSnapshot.getValue(Event.class)
    }

    public Event(String uid, String author, String title, Long epoch, String body) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.epoch = epoch;
        this.body = body;
    }

    // [START event_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("epoch", epoch);
        result.put("body", body);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }
    // [END event_to_map]

}
// [END event_class]
