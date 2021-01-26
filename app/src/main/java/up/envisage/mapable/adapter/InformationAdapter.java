package up.envisage.mapable.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import up.envisage.mapable.R;

@RequiresApi(api = Build.VERSION_CODES.N)
public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.ViewHolder>{

    Context context;

    private OnInformationClickListener onInformationClickListener;
    private LayoutInflater layoutInflater;

    private int[] img_policyAndLaws;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        OnInformationClickListener onInformationClickListener;
        Context context;

        public ViewHolder(@NonNull View view, OnInformationClickListener onInformationClickListener) {
            super(view);
            this.imageView = view.findViewById(R.id.imageView_policyAndLaw);
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

    //Policies and Laws Images
    public int[] img_policiesAndLaws = {
            R.drawable.img_policy_battleogmanilabay,
            R.drawable.img_policy_manilabaymandamus,
            R.drawable.img_policy_manilabaytaskforce
    };

    @NonNull
    @Override
    public InformationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_policyandlaw, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, onInformationClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InformationAdapter.ViewHolder viewHolder, int position) {
        viewHolder.imageView.setImageResource(img_policiesAndLaws[position]);
    }

    @Override
    public int getItemCount() {
        return img_policiesAndLaws.length;
    }

    public interface OnInformationClickListener {
        void onClick(int position);
    }
}
