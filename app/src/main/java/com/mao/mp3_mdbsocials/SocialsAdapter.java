package com.mao.mp3_mdbsocials;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class SocialsAdapter extends RecyclerView.Adapter<SocialsAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<Social> data;

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
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Social social = data.get(position);
        holder.nameView.setText(social.name);

        //TODO: use Glide to set img; get image URL via key?
        RequestOptions myOptions = new RequestOptions()
//                .error(R.drawable.ic_error_black_24dp)
                .override(100, 100);
        Glide.with(context)
                .load(social.key)
                .apply(myOptions).into(holder.picView);
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
        EditText interestView;
        ImageView picView;


        public CustomViewHolder(View view) {
            super(view);
            this.nameView = view.findViewById(R.id.eventName);
            this.emailView = view.findViewById(R.id.eventEmail);
            this.interestView = view.findViewById(R.id.eventInterested);
            this.picView = view.findViewById(R.id.eventPicture);
        }

    }
}
