package bw.ac.biust.lovingbirds.Profile;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import bw.ac.biust.lovingbirds.R;

public class ProfileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView mUsername, mAboutProfile;
    public ImageView mProfileImage;

    public ProfileViewHolder(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);

        mUsername= itemView.findViewById(R.id.profile_nameTxt);
        mProfileImage = itemView.findViewById(R.id.prifile_viewImg);
        mAboutProfile = itemView.findViewById(R.id.about_profile);

    }


    @Override
    public void onClick(View view) {

    }
}
