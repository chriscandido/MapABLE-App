package up.envisage.mapable.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import up.envisage.mapable.R;

public class MyQuestAdapter extends RecyclerView.Adapter<MyQuestAdapter.ViewHolder> {

    Context context;

    private LayoutInflater layoutInflater;
    private OnQuestClickListener onQuestClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView_myquest_title;
        ImageView imageView_myquest_badge;
        OnQuestClickListener onQuestClickListener;

        public ViewHolder(@NonNull View view, OnQuestClickListener onQuestClickListener) {
            super(view);
            this.textView_myquest_title = view.findViewById(R.id.textView_myquest_title);
            this.imageView_myquest_badge = view.findViewById(R.id.imageView_myquest_badge);
            this.onQuestClickListener = onQuestClickListener;
        }

        @Override
        public void onClick(View v) {

            onQuestClickListener.onClick(getAdapterPosition());
            Log.v("[MyQuestAdapter.java]:", "Adapter Position: " + getAdapterPosition());

        }
    }

    public MyQuestAdapter(Context context, OnQuestClickListener onQuestClickListener) {
        this.context = context;
        this.onQuestClickListener = onQuestClickListener;
        layoutInflater = LayoutInflater.from(context);
    }

    public String[] myQuest_title = {
            "Completed App Tutorial",
            "Completed First Report",
            "First Verified Report"
    };

    public int[] myQuest_badge = {
            R.drawable.ic_badge_completetutorial,
            R.drawable.ic_badge_firstreport,
            R.drawable.ic_badge_firstverified
    };

    @NonNull
    @Override
    public MyQuestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardslayout_myquest, parent, false);

        MyQuestAdapter.ViewHolder viewHolder = new MyQuestAdapter.ViewHolder(view, onQuestClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView_myquest_title.setText(myQuest_title[position]);
        holder.imageView_myquest_badge.setImageResource(myQuest_badge[position]);
    }

    @Override
    public int getItemCount() {
        return myQuest_badge.length;
    }

    public interface OnQuestClickListener {
        void onClick(int position);
    }

}
