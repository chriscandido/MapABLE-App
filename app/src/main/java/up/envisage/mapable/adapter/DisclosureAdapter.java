package up.envisage.mapable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import up.envisage.mapable.R;

public class DisclosureAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public DisclosureAdapter (Context context) {
        this.context = context;
    }

    // Disclosure icons
    public int[] disclosureImages = {
            R.drawable.ic_disclosure_locationservice,
            R.drawable.ic_disclosure_media
    };

    // Disclosure headings
    public String[] disclosureHeadings = {
            "Allow MASDAN to access your location?",
            "Allow MASDAN to access camera and photo gallery on your device?"
    };

    // Disclosure body
    public String[] disclosureBody = {
            "MASDAN collects location in the background to customize your experience with the app. MASDAN will use this location in the report module to accurately geotag your submitted reports. " +
                    "The map interface will also show your current location.",
            "Reports require attachment of photos to complete. MASDAN will use this data to verify your reports."
    };

    public int getCount(){
        return disclosureImages.length;
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.popup_disclosure_location, viewGroup, false);

        ImageView imageView_disclosureImage = view.findViewById(R.id.imageView_disclosureImage);
        TextView textView_disclosureHeading = view.findViewById(R.id.textView_disclosureHeading);
        TextView textView_disclosureBody = view.findViewById(R.id.textView_disclosureBody);

        imageView_disclosureImage.setImageResource(disclosureImages[position]);
        textView_disclosureHeading.setText(disclosureHeadings[position]);
        textView_disclosureBody.setText(disclosureBody[position]);

        viewGroup.addView(view);

        return view;
    }

    public void destroyItem (ViewGroup viewGroup, int position, Object object){
        viewGroup.removeView((RelativeLayout) object);
    }

}
