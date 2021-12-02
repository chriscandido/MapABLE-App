package up.envisage.mapable.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofitInterface.RetrofitInterface;
import up.envisage.mapable.R;
import up.envisage.mapable.model.Image;

public class ViewReportActivity extends AppCompatActivity {

    String report, type, region, province, city, date, status, priority, closed, remarks, imageID, userID, description;

    String[] reportArr;

    TextView textView_report, textView_type, textView_region, textView_province, textView_city, textView_date, textView_status, textView_priority, textView_closed, textView_remarks;

    ImageView imageView_report;

    Retrofit retrofit;
    String BASE_URL = "http://ec2-54-91-89-105.compute-1.amazonaws.com/";
//    private String BASE_URL = "http://10.0.2.2:5000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        imageView_report = (ImageView)findViewById(R.id.imageView_report);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);

        httpClient.addInterceptor(logging);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        Intent intent = getIntent();

        report = intent.getStringExtra("report");
        type = intent.getStringExtra("type");
        status = intent.getStringExtra("status");
        priority = intent.getStringExtra("priority");
        closed = intent.getStringExtra("closed");
        remarks = intent.getStringExtra("remarks");
        date = intent.getStringExtra("date");
        region = intent.getStringExtra("region");
        province = intent.getStringExtra("province");
        city = intent.getStringExtra("city");
        imageID = intent.getStringExtra("imageID");
        userID = intent.getStringExtra("userID");

        reportArr = report.split("\\|");

        if(type.equals("Algal Bloom")) {

            description = reportArr[1] + " nang naoobserbahan. " + (reportArr[2].equals("Mayroon") ? " Mayroong " : " Walang ") +
                    "kakaibang amoy ang tubig. " + (reportArr[3].equals("Oo") ? "Mayroong" : "Walang") +
                    " patay na isdang namataan." + "\n\n\nIba pang detalye: \n\n" + reportArr[0] + "\n" +
                    (reportArr.length == 5 ? reportArr[4] : ' ') ;

        } else if (type.equals("Fish Kill")) {

            description = reportArr[1] + " nang naoobserbahan. " + (reportArr[2].equals("Mayroon") ? " Mayroong " : " Walang ") +
                    "kakaibang amoy ang tubig. " + reportArr[6] +
                    " ang dami ng isdang namatay na." + "\n\n\nIba pang detalye: \n\n" + reportArr[0] + "\nTubig: " +
                    reportArr[3] + "\nApektadong Isda: " + (reportArr.length == 6 ? reportArr[5] : " ")  ;

        } else if (type.equals("Water Pollution")) {

            description = "Naobserbahan sa " + reportArr[1] + " ang " +reportArr[2] +" "+ reportArr[3]+" na ang nakaraan. "+
                    (reportArr[4].equals("Mayroon") ? "Mayroong " : "Walang ") + "kakaibang amoy ang tubig. "+ (reportArr[6].equals("Oo") ? "Mayroong " : "Walang ") +
                    "ibang isyung naidulot." + "\n\n\nIba pang detalye: \n\n" + reportArr[0] + "\nTubig: " +
                    reportArr[5] + "\nPinagmumulan ng polusyon: " + (reportArr.length == 8 ? reportArr[7] : " ");

        } else if (type.equals("Ongoing Reclamation")) {

            description = "Unang naobserbahan ang pagtatambak ng "+reportArr[2]+" "+reportArr[1]+". Ang lawak ay "+reportArr[3]+" at "+
                    (reportArr[4].equals("Mayroon") ? "Mayroong ": "Walang ")+"istrukturang nakatayo sa lugar. "+(reportArr[5].equals("Mayroon")? "Mayroong ": "Walang ")+
                    "namamalagi/nagbabantay sa lugar na tinambakan." + "\n\n\nIba pang detalye: \n\n" + reportArr[0] + "\nMay-ari/nagbabantay sa tinambakang lugar: " +
                    (reportArr.length == 7 ? reportArr[6] : " ");

        } else if (type.equals("Water Hyacinth")) {

            description = reportArr[1]+" nang naoobserbahan ang "+reportArr[2]+" na water hyacinth. Ang lawak ay "+reportArr[3]+" at ito ay "+
                    (reportArr[4].equals("Oo")? "gumagalaw ": "hindi gumagalaw ")+"kasabay ng agos ng tubig. Ito ay "+(reportArr[5].equals("Oo") ? "nakakaharang ":"hindi nakakaharang ")+
                    "sa daluyan ng tubig." + "\n\n\nIba pang detalye: \n\n" + reportArr[0] + "\n"+
                    (reportArr[6].equals("Oo") ? "Naipaalam " : "hindi pa naipaalam ") + "sa lokal na awtoridad.";

        } else if (type.equals("Solid Waste")) {

            description = "Namataan ang "+reportArr[1]+" sa "+reportArr[2]+" "+reportArr[5]+" na ang nakakaraan. "+reportArr[3]+" ang "+reportArr[4]+" basurang nakatambak. "+
                    (reportArr[6].equals("Mayroon")?"Mayroong ": "Walang ")+"mga insekto at hayop na namumugad o nagkakalkal ng basura. " + "\n\n\nIba pang detalye: \n\n" + reportArr[0] + "\n"+
                    (reportArr[8].equals("Oo ngunit wala pang aksyon") ? "Naipaalam " : "hindi pa naipaalam ") + "sa lokal na awtoridad.";

        } else if (type.equals("Iba Pa")) {

//            description = report;
            description = "Isyu: "+(reportArr.length > 1 ? reportArr[1] : " ") + "\n\n\nDetalye: \n\n" + (reportArr.length > 1 ? reportArr[0] : " ") + "\n"+ (reportArr.length == 3 ? reportArr[2] : " ");

        }

        HashMap<String, String> map = new HashMap<>();
        map.put("imageID", imageID);

         RetrofitInterface GetImageAPI = retrofit.create(RetrofitInterface.class);

         Call<Image> call = GetImageAPI.getImage(map);

         call.enqueue(new Callback<Image>() {
             @RequiresApi(api = Build.VERSION_CODES.O)
             @Override
             public void onResponse(Call<Image> call, Response<Image> response) {
                 if(response.code() == 200) {

                    byte[] imageBytes = Base64.getDecoder().decode(response.body().getImage().toString());
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    Bitmap resizedImage = Bitmap.createScaledBitmap(decodedImage,(int)(decodedImage.getWidth()*5), (int)(decodedImage.getHeight()*5),true);
                    imageView_report.setImageBitmap(resizedImage);

                    Log.v("ViewReportActivity", response.body().getImage().toString());
                 }

             }

             @Override
             public void onFailure(Call<Image> call, Throwable t) {

             }
         });

        textView_report = findViewById(R.id.textView_report);
        textView_type = findViewById(R.id.textView_type2);
        textView_status = findViewById(R.id.textView_status2);
        textView_priority = findViewById(R.id.textView_priority2);
        textView_closed = findViewById(R.id.textView_closed2);
        textView_remarks = findViewById(R.id.textView_remarks);
        textView_date = findViewById(R.id.textView_date2);
        textView_region = findViewById(R.id.textView_region2);
        textView_province = findViewById(R.id.textView_province2);
        textView_city = findViewById(R.id.textView_city2);

        textView_report.setText(description);
        textView_type.setText(type);
        textView_status.setText(status);
        textView_priority.setText(priority);
        textView_remarks.setText(remarks);
        textView_date.setText(date);
        textView_region.setText(region);
        textView_province.setText(province);
        textView_city.setText(city);

        if(closed.equals("false")) {
            textView_closed.setVisibility(View.GONE);
        }
        else if(closed.equals("true")) {
            textView_closed.setVisibility(View.VISIBLE);
            textView_closed.setText("REPORT IS CLOSED!");
        };

        TextView textView_reportBack = findViewById(R.id.textView_report_back);
        textView_reportBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


}
