package bw.ac.biust.lovingbirds.Matches;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import bw.ac.biust.lovingbirds.R;

//pupilates data from items_matches with the data we want user to view
public class MatchesAdapter extends RecyclerView.Adapter<MatchesViewHolders>{
    private List<MatchesObject> matchesList;
    private Context context;

    //passes info between matches activity to mathces adapter
    public MatchesAdapter(List<MatchesObject> matchesList, Context context){
        this.matchesList = matchesList;
        this.context = context;
    }

    @Override
    public MatchesViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        //the part that will controll the layout
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches, null, false);

        //ensures correct layout and orientation of elements
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //pass the layout params into layout view
        layoutView.setLayoutParams(lp);
        MatchesViewHolders rcv = new MatchesViewHolders(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(MatchesViewHolders holder, int position) {
         /*
          The holder var will contain the item_matches xml attributes
          The position is the position of the views in the recycler view
         */

        //elements that are destroyed on creation() are then stored here
        //user id
        holder.mMatchId.setText(matchesList.get(position).getUserId());
        //user name
        holder.mMatchName.setText(matchesList.get(position).getName());
        //user profile image
        if(!matchesList.get(position).getProfileImageUrl().equals("default")){
            Glide.with(context).load(matchesList.get(position).getProfileImageUrl()).into(holder.mMatchImage);
        }
    }

    @Override
    public int getItemCount() {
        return this.matchesList.size();
    }
}
