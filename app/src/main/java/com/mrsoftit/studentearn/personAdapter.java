package com.mrsoftit.studentearn;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class personAdapter extends FirebaseRecyclerAdapter<
        userModle, personAdapter.personsViewholder> {

    public personAdapter(@NonNull FirebaseRecyclerOptions<userModle> options) {
        super(options);
    }

    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")
    @Override
    protected void
    onBindViewHolder(@NonNull personsViewholder holder, int position, @NonNull userModle model) {

        holder.firstname.setText(model.getName());
        if(model.getImageURL() != null){
            Picasso.get().load(model.getImageURL()).into(holder.profile_image);
        }else {
            Picasso.get().load("https://i.ibb.co/9gdBbLK/pp.png").into(holder.profile_image);

        }

    }

    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public personsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new personAdapter.personsViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class personsViewholder
            extends RecyclerView.ViewHolder {
        TextView firstname, lastname, age;
        CircleImageView  profile_image;

        public personsViewholder(@NonNull View itemView) {
            super(itemView);

            firstname = itemView.findViewById(R.id.userName);
            lastname = itemView.findViewById(R.id.userPoint);
            profile_image = itemView.findViewById(R.id.profile_image);

        }
    }
}