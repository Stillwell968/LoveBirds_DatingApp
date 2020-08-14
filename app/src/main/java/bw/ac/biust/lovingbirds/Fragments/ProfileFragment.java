package bw.ac.biust.lovingbirds.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bw.ac.biust.lovingbirds.Profile.ProfileAdapter;
import bw.ac.biust.lovingbirds.Profile.ProfileObject;
import bw.ac.biust.lovingbirds.R;

public class ProfileFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private String currentUserId, userId, name, profileImageUrl, aboutInfo;
    private DatabaseReference mUserDb;
    private FirebaseAuth mAuth;


    private Uri resultUri;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_frag, container, false);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerView = view.findViewById(R.id.profile_recycleView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);


        layoutManager= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProfileAdapter(getDatasetProfile(), getContext());
        recyclerView.setAdapter(adapter);

        //get user is reference from database
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        mUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        //display user info
        getUserInfo();

        return view;
    }

    private void getUserInfo() {
        mUserDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    //get the id and the name of the user
                    name = "";
                    profileImageUrl = "";
                    aboutInfo ="";


                    if (snapshot.child("Name").getValue().toString() != null) {
                        name = snapshot.child("Name").getValue().toString();
                    }
                    if (snapshot.child("About").getValue().toString() != null) {
                        aboutInfo = snapshot.child("About").getValue().toString();
                    }
                    if (snapshot.child("profileImageUrl").getValue().toString() != null) {
                        profileImageUrl = snapshot.child("profileImageUrl").getValue().toString();
                    }

                    ProfileObject obj = new ProfileObject(name, profileImageUrl, aboutInfo);
                    resultProfile.add(obj);

                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private ArrayList<ProfileObject> resultProfile = new ArrayList<>();
    private List<ProfileObject> getDatasetProfile() {
        return resultProfile;
    }
}
