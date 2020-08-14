package bw.ac.biust.lovingbirds;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    private ImageView mProfileImage;
    private EditText mNameField, mPhoneField, mAgeField, mLocationField, mAboutField;
    private Button mConfirm;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDb;

    private  String userId, name, phone , profileImageUrl, userSex, mAge, about, loc;

    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mProfileImage = (ImageView) findViewById(R.id.profile_img);
        mNameField = (EditText) findViewById(R.id.name_profile);
        mPhoneField = (EditText) findViewById(R.id.phone);
        mConfirm = (Button) findViewById(R.id.confirm_btn);
        mAgeField = (EditText) findViewById(R.id.age);
        mLocationField = (EditText) findViewById(R.id.location);
        mAboutField = (EditText) findViewById(R.id.description);

        progressBar = (ProgressBar) findViewById(R.id.progress_load);

        //get user is reference from database
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        mUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        //display user info
        getUserInfo();

        //get profile image from phone internal memory
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //allows app to go outside the app ie(the galery)
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                // the one is the number attached to the intent
                //it capture the number and goes with the result
                startActivityForResult(intent,1);
            }
        });
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getUserInfo() {
        //create a listner to check fo current user info
        mUserDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();

                    //grab info
                    //put data from database
                    if (map.get("Name") !=null){
                        //assign user name from database
                        name = map.get("Name").toString();
                        //print the value to textfield
                        mNameField.setText(name);
                    }
                    //get phone num
                    if (map.get("Phone") !=null){
                        //assign user name from database
                        phone = map.get("Phone").toString();
                        //print the value to textfield
                        mPhoneField.setText(phone);
                    }
                    //get user age
                    if (map.get("Age") !=null){
                        //assign user name from database
                        mAge = map.get("Age").toString();
                        //print the value to textfield
                        mAgeField.setText(mAge);
                    }
                    //get about info
                    if (map.get("About") !=null){
                        //assign user name from database
                        about = map.get("About").toString();
                        //print the value to textfield
                        mAboutField.setText(about);
                    }
                    //get user location
                    if (map.get("Location") !=null){
                        //assighn user name from database
                        loc = map.get("Location").toString();
                        //print the value to textfield
                        mLocationField.setText(loc);
                    }
                    //get user gender
                    if (map.get("Gender") !=null){
                        //assign user name from database
                        userSex = map.get("Gender").toString();
                    }


                    Glide.clear(mProfileImage);
                    if (map.get("profileImageUrl") !=null){
                        //assighn user name from database
                        profileImageUrl = map.get("profileImageUrl").toString();
                        switch (profileImageUrl){
                            case "default":
                                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(mProfileImage);
                                break;

                            default:
                                Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    //saves the info to database
    private void saveUserInfo() {
        progressBar.setVisibility(View.VISIBLE);
        //get value from fields and convert them to strings
        name = mNameField.getText().toString();
        phone = mPhoneField.getText().toString();
        mAge = mAgeField.getText().toString();
        about=mAboutField.getText().toString();
        loc= mLocationField.getText().toString();

        Map userInfo = new HashMap();
        //write where to put values in database
        userInfo.put("Name",name);
        userInfo.put("Phone",phone);
        userInfo.put("Age",mAge);
        userInfo.put("About",about);
        userInfo.put("Location",loc);


        //save to database
        mUserDb.updateChildren(userInfo);
        //checks if image has changed
        if (resultUri != null){
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            Bitmap bitmap = null;

            //pass image we got from image uri
            //create a safety net for any unacounted errors
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //compress image to small size
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20,baos);
            byte[] data = baos.toByteArray();

            //try to upload file
            //try on fail to catch error uploads
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });

            //do the other case where image upload is successfull
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //grap url from the profile image from the database
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    Uri downLoadUrl = uriTask.getResult();

                    Map userInfo = new HashMap();
                    //write where to put values in database
                    userInfo.put("profileImageUrl",downLoadUrl.toString());
                    mUserDb.updateChildren(userInfo);

                    return;
                }
            });

        }else{
            Toast.makeText(SettingsActivity.this,"Image load ++", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //checks if app managed to pull up an image
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){

            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mProfileImage.setImageURI(resultUri);
        }
    }

}