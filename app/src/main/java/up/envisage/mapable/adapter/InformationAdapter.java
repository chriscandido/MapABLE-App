package up.envisage.mapable.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import up.envisage.mapable.R;

@RequiresApi(api = Build.VERSION_CODES.N)
public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.ViewHolder>{

    Context context;

    private OnInformationClickListener onInformationClickListener;
    private LayoutInflater layoutInflater;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout linearLayout;
        private TextView textView_information_title, textView_information_desc;
        private CardView cardView;
        OnInformationClickListener onInformationClickListener;
        Context context;

        public ViewHolder(@NonNull View view, OnInformationClickListener onInformationClickListener) {
            super(view);
            this.textView_information_title = view.findViewById(R.id.textView_information_title);
            this.textView_information_desc = view.findViewById(R.id.textView_information_desc);
            this.linearLayout = view.findViewById(R.id.linearLayout_informationBody);
            this.cardView = view.findViewById(R.id.cardView_information);
            this.onInformationClickListener = onInformationClickListener;

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onInformationClickListener.onClick(getAdapterPosition());
        }
    }

    public InformationAdapter (Context context, OnInformationClickListener onInformationClickListener) {
        this.context = context;
        this.onInformationClickListener = onInformationClickListener;
        layoutInflater = LayoutInflater.from(context);
    }

    //Title
    public String[] information_title = {
            "Manila Bay Rehabilitation",
            "Manila Bay Mandamus ",
            "Manila Bay Task Force",
            "Philippine Clean Water Act of 2004",
            "Water Quality Guidelines and General Effluent Standards of 2016 ",
            "Ecological Solid Waste Management Act of 2000"
    };

    //Desc
    public int[] information_desc = {
            R.string.desc_manilaBayRehabilitation,
            R.string.desc_manilaBayMandamus,
            R.string.desc_manilaBayTaskForce,
            R.string.desc_cleanWaterAct,
            R.string.desc_waterQualityGuidelines,
            R.string.desc_ecologicalSolidWaste
    };

    @NonNull
    @Override
    public InformationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardslayout_information, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, onInformationClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InformationAdapter.ViewHolder viewHolder, int position) {
        viewHolder.textView_information_title.setText(information_title[position]);
        viewHolder.textView_information_desc.setText(information_desc[position]);

        viewHolder.textView_information_title.setOnClickListener(v -> {
            if (viewHolder.linearLayout.getVisibility()==View.GONE) {
                TransitionManager.beginDelayedTransition(viewHolder.cardView, new AutoTransition());
                viewHolder.linearLayout.setVisibility(View.VISIBLE);
            } else {
                TransitionManager.beginDelayedTransition(viewHolder.cardView, new AutoTransition());
                viewHolder.linearLayout.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return information_title.length;
    }

    public interface OnInformationClickListener {
        void onClick(int position);
    }
}
