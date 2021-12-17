package up.envisage.mapable.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofitInterface.RetrofitInterface;
import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;
import up.envisage.mapable.adapter.IncidentListAdapter;
import up.envisage.mapable.model.Image;
import up.envisage.mapable.model.ReportViewModel;
import up.envisage.mapable.model.UserViewModel;
import up.envisage.mapable.ui.home.ReportIncidentActivity;
import up.envisage.mapable.ui.home.ReportingActivity;
import up.envisage.mapable.ui.home.report.ReportAlgalBloom;
import up.envisage.mapable.ui.home.report.ReportFishKill;
import up.envisage.mapable.ui.home.report.ReportIbaPa;
import up.envisage.mapable.ui.home.report.ReportIllegalReclamation;
import up.envisage.mapable.ui.home.report.ReportPollution;
import up.envisage.mapable.ui.home.report.ReportSolidWaste;
import up.envisage.mapable.ui.home.report.ReportWaterHyacinth;


public class IncidentListFragment extends Fragment {

    private FragmentActivity listener;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://ec2-54-91-89-105.compute-1.amazonaws.com/";
//    private String BASE_URL = "http://10.0.2.2:5000/";

    private ReportViewModel reportViewModel;
    private UserViewModel userViewModel;

    private String userID, dateTime, incidentType, Report, lon, lat, image, imageID2, outPhoto, imageString, outUserId;
    private ImageView imageView_incidentList_back;

    GridView gridView_incidentList;

    public Boolean connection;

    private Dialog dialog;

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.listener = (FragmentActivity) context;
        }
    }

    @SuppressLint("LongLogTag")
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Initiating report class
        reportViewModel = ViewModelProviders.of(listener).get(ReportViewModel.class);
        userViewModel = ViewModelProviders.of(listener).get(UserViewModel.class);

        userViewModel.getLastUser().observe(listener, UserTable -> {
            outUserId = UserTable.getUniqueId();
            Log.v("[ReportingActivity.java userID from local]:", "UserID: " + outUserId);
        });

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        //Set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        //Add your other interceptors …
        httpClient.callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(60, TimeUnit.SECONDS) // connect timeout
                .writeTimeout(60, TimeUnit.SECONDS) // write timeout
                .readTimeout(60, TimeUnit.SECONDS); // read timeout

        //Add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Intent reportAct = getActivity().getIntent();

        userID = reportAct.getStringExtra("userID");
        dateTime = reportAct.getStringExtra("Date and Time");
        incidentType = reportAct.getStringExtra("Incident Type");
        Report = reportAct.getStringExtra("Report");
        lon = reportAct.getStringExtra("Longitude");
        lat = reportAct.getStringExtra("Latitude");
        image = reportAct.getStringExtra("image");

    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState){
        return layoutInflater.inflate(R.layout.fragment_incident_list, container, false);
    }

    @SuppressLint("LongLogTag")
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gridView_incidentList = (GridView) view.findViewById(R.id.gridView_reportIncident);
        // Create an object of CustomAdapter and set Adapter to GirdView
        gridView_incidentList.setAdapter(new IncidentListAdapter(view.getContext()));
        // implement setOnItemClickListener event on GridView
        gridView_incidentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent algalBloomOk = new Intent(listener, ReportAlgalBloom.class);
                        algalBloomOk.putExtra("userID", userID);
                        algalBloomOk.putExtra("Date and time", dateTime);
                        algalBloomOk.putExtra("Incident Type", incidentType);
                        algalBloomOk.putExtra("Report", Report);
                        algalBloomOk.putExtra("Longitude", lon);
                        algalBloomOk.putExtra("Latitude", lat);
                        algalBloomOk.putExtra("image", image);
                        startActivity(algalBloomOk);
                        break;
                    case 1:
                        Intent fishKillOk = new Intent(listener, ReportFishKill.class);
                        fishKillOk.putExtra("userID", userID);
                        fishKillOk.putExtra("Date and time", dateTime);
                        fishKillOk.putExtra("Incident Type", incidentType);
                        fishKillOk.putExtra("Report", Report);
                        fishKillOk.putExtra("Longitude", lon);
                        fishKillOk.putExtra("Latitude", lat);
                        fishKillOk.putExtra("image", image);
                        startActivity(fishKillOk);
                        break;
                    case 2:
                        Intent pollutionOk = new Intent(listener, ReportPollution.class);
                        pollutionOk.putExtra("userID", userID);
                        pollutionOk.putExtra("Date and time", dateTime);
                        pollutionOk.putExtra("Incident Type", incidentType);
                        pollutionOk.putExtra("Report", Report);
                        pollutionOk.putExtra("Longitude", lon);
                        pollutionOk.putExtra("Latitude", lat);
                        pollutionOk.putExtra("image", image);
                        startActivity(pollutionOk);
                        break;
                    case 3:
                        Intent illegalReclamationOk = new Intent(listener, ReportIllegalReclamation.class);
                        illegalReclamationOk.putExtra("userID", userID);
                        illegalReclamationOk.putExtra("Date and time", dateTime);
                        illegalReclamationOk.putExtra("Incident Type", incidentType);
                        illegalReclamationOk.putExtra("Report", Report);
                        illegalReclamationOk.putExtra("Longitude", lon);
                        illegalReclamationOk.putExtra("Latitude", lat);
                        illegalReclamationOk.putExtra("image", image);
                        startActivity(illegalReclamationOk);
                        break;
                    case 4:
                        Intent waterHyacinthOk = new Intent(listener, ReportWaterHyacinth.class);
                        waterHyacinthOk.putExtra("userID", userID);
                        waterHyacinthOk.putExtra("Date and time", dateTime);
                        waterHyacinthOk.putExtra("Incident Type", incidentType);
                        waterHyacinthOk.putExtra("Report", Report);
                        waterHyacinthOk.putExtra("Longitude", lon);
                        waterHyacinthOk.putExtra("Latitude", lat);
                        waterHyacinthOk.putExtra("image", image);
                        startActivity(waterHyacinthOk);
                        break;
                    case 5:
                        Intent solidWasteOk = new Intent(listener, ReportSolidWaste.class);
                        solidWasteOk.putExtra("userID", userID);
                        solidWasteOk.putExtra("Date and time", dateTime);
                        solidWasteOk.putExtra("Incident Type", incidentType);
                        solidWasteOk.putExtra("Report", Report);
                        solidWasteOk.putExtra("Longitude", lon);
                        solidWasteOk.putExtra("Latitude", lat);
                        solidWasteOk.putExtra("image", image);
                        startActivity(solidWasteOk);
                        break;
                    case 6:
                        Intent ibaPaOk = new Intent(listener, ReportIbaPa.class);
                        ibaPaOk.putExtra("userID", userID);
                        ibaPaOk.putExtra("Date and time", dateTime);
                        ibaPaOk.putExtra("Incident Type", incidentType);
                        ibaPaOk.putExtra("Report", Report);
                        ibaPaOk.putExtra("Longitude", lon);
                        ibaPaOk.putExtra("Latitude", lat);
                        ibaPaOk.putExtra("image", image);
                        startActivity(ibaPaOk);
                        break;
                }
            }
        });

        imageView_incidentList_back = view.findViewById(R.id.imageView_incidentlist_back);
        imageView_incidentList_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(listener, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //----------------------------------------------------------------------------------------------convert from bitmap to byte array
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    //----------------------------------------------------------------------------------------------convert image to string
    public String imageConvertToString(String image) {
        Uri selectedImage = Uri.parse(image);
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();

        Bitmap galleryPhoto = BitmapFactory.decodeFile(filePath);

        //Get the base 64 string
        String img = Base64.encodeToString(getBytesFromBitmap(galleryPhoto),
                Base64.NO_WRAP);
        return img;
    }

}
