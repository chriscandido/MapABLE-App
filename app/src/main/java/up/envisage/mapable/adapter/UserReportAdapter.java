package up.envisage.mapable.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import up.envisage.mapable.R;
import up.envisage.mapable.model.UserReport;

public class UserReportAdapter extends RecyclerView.Adapter<UserReportAdapter.MyViewHolder> {

    Context context;
    private final List<UserReport> userReportList;
    private final OnReportClickListener mOnReportClickListener;

    public UserReportAdapter(Context context, List<UserReport> userReportList, OnReportClickListener onReportClickListener) {
        this.context = context;
        this.userReportList = userReportList;
        this.mOnReportClickListener = onReportClickListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView type, date, status, priority, closed;
        private ImageView image;

        OnReportClickListener onReportClickListener;

        public MyViewHolder(@NonNull View view, OnReportClickListener onReportClickListener) {
            super(view);
            type = itemView.findViewById(R.id.textView_type);
            date = itemView.findViewById(R.id.textView_date);
            status = itemView.findViewById(R.id.textView_status);
            priority = itemView.findViewById(R.id.textView_priority);
            closed = itemView.findViewById(R.id.textView_closed);
            image = itemView.findViewById(R.id.imageView_userReport_report);

            this.onReportClickListener = onReportClickListener;

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            onReportClickListener.onReportClick(getAdapterPosition());
        }
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_report_item, parent, false);
        return new MyViewHolder(v, mOnReportClickListener);
    }

    @SuppressLint({"LongLogTag", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {

        holder.type.setText(userReportList.get(position).getType());
        holder.date.setText(userReportList.get(position).getDate());
        holder.status.setText(userReportList.get(position).getStatus());
        holder.priority.setText(userReportList.get(position).getPriority());

        switch (userReportList.get(position).getType()) {
            case "Algal Bloom":
                holder.image.setImageResource(R.drawable.ic_incident_algalbloom);
                break;
            case "Fish Kill":
                holder.image.setImageResource(R.drawable.ic_incident_fishkill);
                break;
            case "Water Pollution":
                holder.image.setImageResource(R.drawable.ic_incident_waterpollution);
                break;
            case "Ongoing Reclamation":
                holder.image.setImageResource(R.drawable.ic_incident_reclamation);
                break;
            case "Water Hyacinth":
                holder.image.setImageResource(R.drawable.ic_incident_waterhyacinth);
                break;
            case "Solid Waste":
                holder.image.setImageResource(R.drawable.ic_incident_solidwaste);
                break;
            default:
                holder.image.setImageResource(R.drawable.ic_incident_ibapa);
                break;
        }

        if(userReportList.get(position).getClosed().equals("false")) {
            holder.closed.setVisibility(View.GONE);
        } else if(userReportList.get(position).getClosed().equals("true")) {
            holder.closed.setVisibility(View.VISIBLE);
            holder.closed.setText("Closed");
        }
    }

    @Override
    public int getItemCount() {
        return userReportList.size();
    }

    public interface OnReportClickListener{
        void onReportClick(int position);
    }

}
