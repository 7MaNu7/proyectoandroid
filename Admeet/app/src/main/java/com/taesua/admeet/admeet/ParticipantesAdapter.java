package com.taesua.admeet.admeet;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.appspot.ad_meet.conference.model.Profile;

import java.util.List;

/**
 * Created by Hector on 16/05/2015.
 */
public class ParticipantesAdapter extends ArrayAdapter<Profile> {

    private List<Profile> items;
    private int layoutResourceId;
    private Context context;

    public ParticipantesAdapter(Context context, int layoutResourceId, List<Profile> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ProfileHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new ProfileHolder();
        holder.Profile = items.get(position);
        holder.ButtonEchar = (ImageView)row.findViewById(R.id.ButtonEchar);
        holder.ButtonEchar.setTag(holder.Profile);

        holder.nombre = (TextView)row.findViewById(R.id.participante);
        row.setTag(holder);
        setupItem(holder);
        return row;
    }

    private void setupItem(ProfileHolder holder) {
        holder.nombre.setText(holder.Profile.getDisplayName());
    }

    public static class ProfileHolder {
        Profile Profile;
        TextView nombre;
        ImageView ButtonEchar;
    }
}