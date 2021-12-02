package up.envisage.mapable.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.List;

import up.envisage.mapable.R;
import up.envisage.mapable.model.Leaderboard;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.MyViewHolder> {

    Context context;
    private List<Leaderboard> leaderboardList;

    public LeaderboardAdapter(Context context, List<Leaderboard> leaderboardList) {
        this.context = context;
        this.leaderboardList = leaderboardList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView points;

        public MyViewHolder(@NonNull View view) {

            super(view);

            username = itemView.findViewById(R.id.textView4);
            points = itemView.findViewById(R.id.textView3);

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

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {

        holder.username.setText(leaderboardList.get(position).getUsername());
        holder.points.setText(leaderboardList.get(position).getPoints());

    }

    @Override
    public int getItemCount() {
        return leaderboardList.size();
    }



}
