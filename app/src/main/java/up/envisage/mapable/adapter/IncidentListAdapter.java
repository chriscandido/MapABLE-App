package up.envisage.mapable.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;
import java.util.Objects;

import up.envisage.mapable.R;

public class IncidentListAdapter extends BaseAdapter {

    Context context;

    public IncidentListAdapter(Context context) {
        this.context = context;
    }

    public int[] logos = {
            R.drawable.ic_incident_algalbloom,
            R.drawable.ic_incident_fishkill,
            R.drawable.ic_incident_waterpollution,
            R.drawable.ic_incident_reclamation,
            R.drawable.ic_incident_waterhyacinth,
            R.drawable.ic_incident_solidwaste,
            R.drawable.ic_incident_ibapa
    };

    public String[] incidentTitle = {
            "Algal Bloom",
            "Fish Kill",
            "Water Pollution",
            "Reclamation",
            "Water Hyacinth",
            "Solid Waste",
            "Iba pa"
    };

    @Override
    public int getCount() {
        return logos.length;
    }


    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolderItem viewHolderItem;
        if (view == null) {
            LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
            view = layoutInflater.inflate(R.layout.cardslayout_incident_list, parent, false);

            viewHolderItem = new ViewHolderItem();
            viewHolderItem.imageView_incident_icon = (ImageView) view.findViewById(R.id.imageView_incident_icon);
            viewHolderItem.textView_incident_title = (TextView) view.findViewById(R.id.textView_incident_title);
            view.setTag(viewHolderItem);
        } else {
            viewHolderItem = (ViewHolderItem) view.getTag();
        }

        viewHolderItem.imageView_incident_icon.setImageResource(logos[position]);
        viewHolderItem.textView_incident_title.setText(incidentTitle[position]);

        return view;

    }

    static class ViewHolderItem {
        ImageView imageView_incident_icon;
        TextView textView_incident_title;
    }
}
