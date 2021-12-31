package up.envisage.mapable.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import up.envisage.mapable.R;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.ViewHolder> {

    Context context;

    private final OnMenuClickListener onMenuClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView_mainTitle;
        ImageView imageView_mainIcon;
        OnMenuClickListener onMenuClickListener;

        public ViewHolder(@NonNull View view, OnMenuClickListener onMenuClickListener) {
            super(view);
            this.textView_mainTitle = view.findViewById(R.id.textView_mainTitle);
            this.imageView_mainIcon = view.findViewById(R.id.imageView_mainIcon);
            this.onMenuClickListener = onMenuClickListener;

            view.setOnClickListener(this);
        }

        public void onClick(View view) {

            onMenuClickListener.onClick(getAdapterPosition());
            Log.v("[MainMenuAdapter.java]:", "Adapter Position: " + getAdapterPosition());
        }

    }

    public MainMenuAdapter(Context context, OnMenuClickListener onMenuClickListener) {
        this.context = context;
        this.onMenuClickListener = onMenuClickListener;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
    }

    // Maine menu title
    public String[] mainMenu_title = {
            "Pormal na idineklara and BATTLE FOR ...",
            "Ang G.R. No. 171947-48, o mas kilala ...",
            "Nilagdaan ni Pangulong Rodrigo Duterte ...",
            "Ang Philippine Clean Water Act ...",
            "Ano ang Ecological Solid Waste Management Act ...",
            "Alam niyo ba kung anong batas ang nagsasaad ..."
    };

    // Main menu icon
    public int[] mainMenu_icon = {
            R.drawable.img_policy_battleofmanilabay,
            R.drawable.img_policy_manilabaymandamus,
            R.drawable.img_policy_manilabaytaskforce,
            R.drawable.img_policy_cleanwateract,
            R.drawable.img_policy_solidwaste,
            R.drawable.img_policy_fisheriescode
    };

    public MainMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardslayout_main, parent, false);

        ViewHolder viewHolder = new ViewHolder(view, onMenuClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView_mainTitle.setText(mainMenu_title[position]);
        holder.imageView_mainIcon.setImageResource(mainMenu_icon[position]);

    }

    @Override
    public int getItemCount() {
        return mainMenu_title.length;
    }

    public interface OnMenuClickListener {
        void onClick(int position);
    }
}
