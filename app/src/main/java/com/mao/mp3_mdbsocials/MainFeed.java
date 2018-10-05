package com.mao.mp3_mdbsocials;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainFeed extends AppCompatActivity {
    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager;

    final int REQUEST_CODE = 123;

    final ArrayList<Social> socials = new ArrayList<>();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("/Socials");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);

        mRecyclerView = findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(new SocialsAdapter(this, socials));

        //Make a new event
        findViewById(R.id.newButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainFeed.this, CreateEvent.class));
            }
        });

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // TODO: order socials by newest to oldest
                myRef.orderByChild("timestamp");

                // This method is called once with the initial value and again whenever data at this location is updated.
                socials.clear(); //keeps ArrayList and clears rather than make new one

                for (DataSnapshot childsnap : dataSnapshot.getChildren()) {
                    socials.add(childsnap.getValue(Social.class)); //cannot do this if class not Serializable
                }
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }
}
