package up.envisage.mapable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import up.envisage.mapable.R;

public class ReportIncidentAdapter extends RecyclerView.Adapter<ReportIncidentAdapter.ViewHolder> {

    Context context;
    private OnIncidentClickListener onIncidentClickListener;
    private LayoutInflater layoutInflater;


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView_incidentTitle, textView_incidentContent;
        ImageView imageView_incidentImage;
        OnIncidentClickListener onIncidentClickListener;

        public ViewHolder(@NonNull View view, OnIncidentClickListener onIncidentClickListener){
            super(view);
            this.textView_incidentTitle = view.findViewById(R.id.textView_incidentTitle);
            this.textView_incidentContent = view.findViewById(R.id.textView_incidentContent);
            this.imageView_incidentImage = view.findViewById(R.id.imageView_incidentImage);
            this.onIncidentClickListener = onIncidentClickListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onIncidentClickListener.onClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ReportIncidentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardslayout_incident_report, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, onIncidentClickListener);
        return viewHolder;
    }

    public ReportIncidentAdapter(Context context, OnIncidentClickListener onIncidentClickListener){
        this.context = context;
        this.onIncidentClickListener = onIncidentClickListener;
        layoutInflater = LayoutInflater.from(context);
    }

    public String[] incidentReport_Title = {
            "Algal Bloom",
            "Fish Kill",
            "Pollution",
            "Illegal Fishing",
            "Illegal Reclamation"
    };

    public int[] incidentReport_Image = {
            R.drawable.img_algal_bloom,
            R.drawable.img_fish_kill,
            R.drawable.img_pollution,
            R.drawable.img_illegal_fishing,
            R.drawable.img_illegal_reclamation
    };

    @Override
    public void onBindViewHolder(@NonNull ReportIncidentAdapter.ViewHolder holder, int position) {
        holder.textView_incidentTitle.setText(incidentReport_Title[position]);
        holder.imageView_incidentImage.setImageResource(incidentReport_Image[position]);
    }

    @Override
    public int getItemCount() {
        return incidentReport_Title.length;
    }

    public interface OnIncidentClickListener {
        void onClick(int position);
    }
}
