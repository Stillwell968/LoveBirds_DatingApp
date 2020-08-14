package bw.ac.biust.lovingbirds.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import bw.ac.biust.lovingbirds.R;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileViewHolder> {
    private List<ProfileObject> profileList;
    private Context context;

    public ProfileAdapter(List<ProfileObject> profileList, Context context){
        this.profileList = profileList;
        this.context =context;

    }

    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        ProfileViewHolder profileViewHolder = new ProfileViewHolder(layoutView);

        return profileViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        holder.mUsername.setText(profileList.get(position).getUserName());
        holder.mAboutProfile.setText(profileList.get(position).getAbout());
        if (!profileList.get(position).getProfileImg().equals("default")){
            Glide.with(context).load(profileList.get(position).getProfileImg()).into(holder.mProfileImage);
        }


    }

    @Override
    public int getItemCount() {
        return this.profileList.size();
    }

}
