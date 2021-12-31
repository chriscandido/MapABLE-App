package up.envisage.mapable.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import up.envisage.mapable.R;
import up.envisage.mapable.db.table.ReportTable;

public class MyReportAdapter extends RecyclerView.Adapter<MyReportAdapter.ViewHolder> {

    Context context;

    private final OnReportClickListener onReportClickListener;
    private final List<ReportTable> reportTable;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView_myReportTitle, textView_myReportDescription, textView_myReportLat, textView_myReportLon;
        ImageView imageView_myReportImage;
        OnReportClickListener onReportClickListener;

        public ViewHolder(@NonNull View view, OnReportClickListener onReportClickListener) {
            super(view);
            this.onReportClickListener = onReportClickListener;
            this.textView_myReportTitle = view.findViewById(R.id.textView_myReportTitle);
            this.textView_myReportDescription = view.findViewById(R.id.textView_myReportDescription);
            this.textView_myReportLat = view.findViewById(R.id.textView_myReportLat);
            this.textView_myReportLon = view.findViewById(R.id.textView_myReportLon);
            this.imageView_myReportImage = view.findViewById(R.id.imageView_myReportImage);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onReportClickListener.onClick(getAdapterPosition());
            Log.v("[MyReportActivity.java]", "Adapter position: " + getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardslayout_my_reports, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, onReportClickListener);
        return viewHolder;
    }

    public MyReportAdapter(Context context,  OnReportClickListener onReportClickListener, List<ReportTable> reportTable) {
        this.context = context;
        this.onReportClickListener = onReportClickListener;
        this.reportTable = reportTable;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(@NonNull MyReportAdapter.ViewHolder holder, int position) {

        holder.textView_myReportTitle.setText(reportTable.get(position).getIncidentType());
        holder.textView_myReportDescription.setText(reportTable.get(position).getDateTime());

        float lat = BigDecimal.valueOf(reportTable.get(position).getLatitude()).setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
        float lon = BigDecimal.valueOf(reportTable.get(position).getLongitude()).setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();

        holder.textView_myReportLat.setText(String.valueOf(lat));
        holder.textView_myReportLon.setText(String.valueOf(lon));

        Log.v("[ MyReportActivity ]", "Number of Reports: " + reportTable.get(position).getIncidentType());
        switch (reportTable.get(position).getIncidentType()) {
            case "Algal Bloom":
                holder.imageView_myReportImage.setImageResource(R.drawable.ic_map_algalbloom);
                break;
            case "Fish Kill":
                holder.imageView_myReportImage.setImageResource(R.drawable.ic_map_fishkill);
                break;
            case "Water Pollution":
                holder.imageView_myReportImage.setImageResource(R.drawable.ic_map_waterpollution);
                break;
            case "Ongoing Reclamation":
                holder.imageView_myReportImage.setImageResource(R.drawable.ic_map_illegalreclamation);
                break;
            case "Water Hyacinth":
                holder.imageView_myReportImage.setImageResource(R.drawable.ic_map_hyacinth);
                break;
            case "Solid Waste":
                holder.imageView_myReportImage.setImageResource(R.drawable.ic_map_solidwaste);
                break;
            case "Iba Pa":
                holder.imageView_myReportImage.setImageResource(R.drawable.ic_map_ibapa);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return reportTable.size();
    }

    public interface OnReportClickListener {
        void onClick(int position);
    }
}
