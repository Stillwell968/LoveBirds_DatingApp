package bw.ac.biust.lovingbirds.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import bw.ac.biust.lovingbirds.Cards.arrayAdapter;
import bw.ac.biust.lovingbirds.Cards.cards;
import bw.ac.biust.lovingbirds.LoginActivity;
import bw.ac.biust.lovingbirds.MainActivity;
import bw.ac.biust.lovingbirds.Matches.MatchesActivity;
import bw.ac.biust.lovingbirds.R;
import bw.ac.biust.lovingbirds.SettingsActivity;

public class HomeFragment extends Fragment {

    private Button bLogout;

    private cards cards_data[];
    private bw.ac.biust.lovingbirds.Cards.arrayAdapter arrayAdapter;
    private int i;

    private FirebaseAuth mAuth;

    private String currentUId;

    private DatabaseReference usersDb;

    ListView listView;
    List<cards> rowItems;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_frag, container, false);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();

        bLogout = (Button) view.findViewById(R.id.logout_btn);

        checkUserSex();

        rowItems = new ArrayList<cards>();

        //code change here
        arrayAdapter = new arrayAdapter(getActivity(), R.layout.item, rowItems );

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) view.findViewById(R.id.frame_frag);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                //this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            //create a function that is called when a user swipes left
            @Override
            public void onLeftCardExit(Object dataObject) {
                //get cards information
                //create a card object
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                //create a field that registers the swipes in database
                // usersDb.child(userId).child("connections").child("nope").child(currentUId).setValue(true);
                usersDb.child(userId).child("connections").child("Dislikes").child(currentUId).setValue(true);

                //Toast.makeText(HomeFragment.this, "Like", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Like", Toast.LENGTH_SHORT).show();
            }

            //right swipe means the user likes that profile
            @Override
            public void onRightCardExit(Object dataObject) {
                //get cards information
                //create a card object
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                // usersDb.child(userId).child("connections").child("yeps").child(currentUId).setValue(true);
                usersDb.child(userId).child("connections").child("Likes").child(currentUId).setValue(true);
                //call the function that will create a match if the user swipes right on my profile
                isConnectionMatch(userId);
              //  Toast.makeText(MainActivity.this, "Right", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                //Toast.makeText(MainActivity.this, "Item Clicked", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Item Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        //when logout button is clicked
        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }

    private void isConnectionMatch(String userId) {
        //checks for the right swipe that the current user made
        //we save the id in a key called snapshot
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("connections").child("Likes").child(userId);
        //when this listener is called the user id is lost when app is running
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //check if child exists in database
                if (dataSnapshot.exists()){
                    //Toast.makeText(MainActivity.this, "new Connection", Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(), "new Connection", Toast.LENGTH_LONG).show();
                    //save to database
                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();
                    usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUId).child("ChatId").setValue(key);
                    usersDb.child(currentUId).child("connections").child("matches").child(dataSnapshot.getKey()).child("ChatId").setValue(key);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //matching algorithm
    private String userSex;
    private String oppositeUserSex;
    public void checkUserSex(){
        //get user id
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb = usersDb.child(user.getUid());
        //is called every time there is a change in the database
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("Preferred Gender").getValue() != null){
                        userSex = dataSnapshot.child("Preferred Gender").getValue().toString();
                        switch (userSex){
                            case "Male":
                                oppositeUserSex = "Male";
                                break;
                            case "Female":
                                oppositeUserSex = "Female";
                                break;
                        }
                        getOppositeSexUsers();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getOppositeSexUsers(){
        //is called every time there is a change in the database
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //statement placed to prevent app crash
                if (dataSnapshot.child("Preferred Gender").getValue() != null) {
                    //if the id matches one at database to ensure that we do not want to see the profile again
                    if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("Dislikes").hasChild(currentUId) && !dataSnapshot.child("connections").child("Likes").hasChild(currentUId) && dataSnapshot.child("Gender").getValue().toString().equals(oppositeUserSex)) {
                        String profileImageUrl = "default";
                        if (!dataSnapshot.child("profileImageUrl").getValue().equals("default")) {
                              /*in the case where user does not have a profile image
                       create if statement to check
                     */
                            profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                        }

                        //get username ,id and about info
                        cards item = new cards(dataSnapshot.getKey(), dataSnapshot.child("Name").getValue().toString(), profileImageUrl , dataSnapshot.child("About").getValue().toString());

                        //add the profile with this sex to array adapter
                        rowItems.add(item);
                        //save changes to the adapter
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}