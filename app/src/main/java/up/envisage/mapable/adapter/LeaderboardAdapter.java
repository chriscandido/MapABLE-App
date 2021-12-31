package up.envisage.mapable.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import up.envisage.mapable.R;
import up.envisage.mapable.model.Leaderboard;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.MyViewHolder> {

    Context context;
    private final List<Leaderboard> leaderboardList;

    public LeaderboardAdapter(Context context, List<Leaderboard> leaderboardList) {
        this.context = context;
        this.leaderboardList = leaderboardList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView points;
        ImageView badge;

        public MyViewHolder(@NonNull View view) {
            super(view);
            username = itemView.findViewById(R.id.textView4);
            points = itemView.findViewById(R.id.textView3);
            badge = itemView.findViewById(R.id.imageView_userprofile_leaderboardtop);
        }
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View v;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        v = layoutInflater.inflate(R.layout.leaderboard_item, parent, false);

        return new MyViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {

        List<Leaderboard> filteredList = new ArrayList<>();
        for (Leaderboard leaderboard : leaderboardList) {
            if (position < 10) {
                filteredList.add(leaderboard);
            }
        }

        holder.username.setText(filteredList.get(position).getUsername());
        holder.points.setText(filteredList.get(position).getPoints());

        // Set image resource for every position in leaderboard
        if (position == 0) {
            holder.badge.setImageResource(R.drawable.ic_userprofile_leaderoardtop1);
        } else if (position == 1) {
            holder.badge.setImageResource(R.drawable.ic_userprofile_leaderoardtop2);
        } else if (position == 2) {
            holder.badge.setImageResource(R.drawable.ic_userprofile_leaderoardtop3);
        } else if (position == 3) {
            holder.badge.setImageResource(R.drawable.ic_userprofile_leaderboardtop4);
        } else if (position == 4) {
            holder.badge.setImageResource(R.drawable.ic_userprofile_leaderboardtop5);
        } else if (position == 5) {
            holder.badge.setImageResource(R.drawable.ic_userprofile_leaderboardtop6);
        } else if (position == 6) {
            holder.badge.setImageResource(R.drawable.ic_userprofile_leaderboardtop7);
        } else if (position == 7) {
            holder.badge.setImageResource(R.drawable.ic_userprofile_leaderboardtop8);
        } else if (position == 8) {
            holder.badge.setImageResource(R.drawable.ic_userprofile_leaderboardtop9);
        } else {
            holder.badge.setImageResource(R.drawable.ic_userprofile_leaderboardtop10);
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

}
