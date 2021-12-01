package up.envisage.mapable.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Callback;
import up.envisage.mapable.R;
import up.envisage.mapable.model.UserReport;

public class UserReportAdapter extends RecyclerView.Adapter<UserReportAdapter.MyViewHolder> {

    Context context;
    private List<UserReport> userReportList;
    private OnReportClickListener mOnReportClickListener;

    public UserReportAdapter(Context context, List<UserReport> userReportList, OnReportClickListener onReportClickListener) {
        this.context = context;
        this.userReportList = userReportList;
        this.mOnReportClickListener = onReportClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView type, date, city, prov, status, priority, closed, reg;

        OnReportClickListener onReportClickListener;

        public MyViewHolder(@NonNull View view, OnReportClickListener onReportClickListener) {

            super(view);

            type = itemView.findViewById(R.id.textView_type);
            date = itemView.findViewById(R.id.textView_date);
            status = itemView.findViewById(R.id.textView_status);
            priority = itemView.findViewById(R.id.textView_priority);
            closed = itemView.findViewById(R.id.textView_closed);
            city = itemView.findViewById(R.id.textView_lat);
            prov = itemView.findViewById(R.id.textView_lon);
            reg = itemView.findViewById(R.id.textView_reg);

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
//        v = layoutInflater.inflate(R.layout.user_report_item, parent, false);

        return new MyViewHolder(v, mOnReportClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {

        holder.type.setText(userReportList.get(position).getType());
        holder.date.setText(userReportList.get(position).getDate());
        holder.status.setText(userReportList.get(position).getStatus());
        holder.priority.setText(userReportList.get(position).getPriority());
        holder.city.setText(userReportList.get(position).getMuni());
        holder.prov.setText(userReportList.get(position).getProv());
        holder.reg.setText(userReportList.get(position).getReg());

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
