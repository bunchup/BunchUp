package com.moreofakind.bunchup.viewholder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moreofakind.bunchup.R;
import com.moreofakind.bunchup.models.Event;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EventViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView dateView;
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;

    public EventViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.event_title);
        dateView = (TextView) itemView.findViewById(R.id.event_date);
        authorView = (TextView) itemView.findViewById(R.id.event_author);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        bodyView = (TextView) itemView.findViewById(R.id.event_body);
    }

    public void bindToEvent(Event event, String uid, View.OnClickListener starClickListener) {
        titleView.setText(event.title);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        dateView.setText(sdf.format(new Date(event.epoch)) + " - ");
        authorView.setText(event.author);
        numStarsView.setText(String.valueOf(event.starCount));
        bodyView.setText(event.body);

        if (event.uid.equalsIgnoreCase(uid)) {
        } else {
            starView.setOnClickListener(starClickListener);
        }
    }
}
