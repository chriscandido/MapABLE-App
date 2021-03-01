package up.envisage.mapable.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import up.envisage.mapable.R;

@RequiresApi(api = Build.VERSION_CODES.N)
public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.ViewHolder>{

    Context context;

    private final OnInformationClickListener onInformationClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView;
        OnInformationClickListener onInformationClickListener;
        Context context;

        public ViewHolder(@NonNull View view, OnInformationClickListener onInformationClickListener) {
            super(view);
            this.imageView = view.findViewById(R.id.imageView_informationBackground);
            this.textView = view.findViewById(R.id.textView_informationTitle);
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
        LayoutInflater layoutInflater = LayoutInflater.from(context);
    }

    //Information Title
    public String[] informationTitle = {
            "Kilalanin ang Manila Bay . . .",
            "Pormal na idineklara ang BATTLE OF . . .",
            "Ang G.R. Nos. 171947-48, o mas kilala . . .",
            "Nilagdaan ni Pangulong Rodrigo Duterte . . .",
            "Ang Philippine Clean Water Act . . .",
            "Ano ang Ecological Solid Waste Management Act . . .",
            "Alam niyo ba kung anong batas ang nagsasaad . . ."
    };

    //Information Images
    public int[] img_informationBackground = {
            R.drawable.img_kilalanin_manilabay,
            R.drawable.img_policy_battleofmanilabay,
            R.drawable.img_policy_manilabaymandamus,
            R.drawable.img_policy_manilabaytaskforce,
            R.drawable.img_policy_cleanwateract,
            R.drawable.img_policy_solidwaste,
            R.drawable.img_policy_fisheriescode
    };

    @NonNull
    @Override
    public InformationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardslayout_information, parent, false);
        return new ViewHolder(view, onInformationClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull InformationAdapter.ViewHolder viewHolder, int position) {
        viewHolder.imageView.setImageResource(img_informationBackground[position]);
        viewHolder.textView.setText(informationTitle[position]);
    }

    @Override
    public int getItemCount() {
        return img_informationBackground.length;
    }

    public interface OnInformationClickListener {
        void onClick(int position);
    }
}
