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

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.ViewHolder> {

    Context context;

    private OnMenuClickListener onMenuClickListener;
    private LayoutInflater layoutInflater;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView_mainTitle, textView_mainContent;
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
        }

    }

    public MainMenuAdapter(Context context, OnMenuClickListener onMenuClickListener) {
        this.context = context;
        this.onMenuClickListener = onMenuClickListener;
        layoutInflater = LayoutInflater.from(context);
    }

    //Title
    public String[] mainMenu_title = {
            "MAY REPORT AKO!",
            "MANILA BAY ALAMIN NATIN!",
            "KILALANIN SI   IM4MANILABAY"
    };

    //Icon
    public int[] mainMenu_icon = {
            R.drawable.ic_mainmenu_report,
            R.drawable.ic_mainmenu_aboutmanilabay,
            R.drawable.ic_mainmenu_about

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
