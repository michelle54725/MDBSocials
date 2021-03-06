package com.mao.mp3_mdbsocials;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SocialsAdapter extends RecyclerView.Adapter<SocialsAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<Social> data;
    Social social;

    SocialsAdapter(Context context, ArrayList<Social> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, int position) {
        final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

        social = data.get(position);
        holder.nameView.setText(social.getName());
        holder.interestView.setText(String.valueOf(social.getInterested()));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            holder.emailView.setText(user.getEmail());
        } else {
            holder.emailView.setText("N/A");
        }

        mStorageRef.child(social.key).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                RequestOptions myOptions = new RequestOptions()
                .error(R.drawable.ic_android_black_24dp)
                        .override(100, 100);
                Glide.with(context)
                        .load(url)
                        .apply(myOptions).into(holder.picView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * A card displayed in the RecyclerView
     */
    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        TextView emailView;
        TextView interestView;
        ImageView picView;


        public CustomViewHolder(View view) {
            super(view);
            this.nameView = view.findViewById(R.id.eventName);
            this.emailView = view.findViewById(R.id.eventEmail);
            this.interestView = view.findViewById(R.id.eventInterested);
            this.picView = view.findViewById(R.id.eventPicture);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detailIntent = new Intent(context, DetailActivity.class);
                    detailIntent.putExtra("socialKey", social.key);
                    context.startActivity(detailIntent);
                }
            });
        }

    }
}
