package bw.ac.biust.lovingbirds.Matches;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import bw.ac.biust.lovingbirds.Chat.ChatActivity;
import bw.ac.biust.lovingbirds.R;

public class MatchesViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mMatchId, mMatchName;
    public ImageView mMatchImage;
    public MatchesViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMatchId =  itemView.findViewById(R.id.Matchid);
        mMatchName =  itemView.findViewById(R.id.MatchName);

        mMatchImage =  itemView.findViewById(R.id.MatchImage);
    }

    @Override
    //is called when user clicks on the image
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ChatActivity.class);
        //save user id for this user and send to other activity
        Bundle b = new Bundle();
        b.putString("matchId", mMatchId.getText().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }
}
