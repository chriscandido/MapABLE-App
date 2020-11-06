package up.envisage.mapable.ui.registration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import up.envisage.mapable.ui.home.report.ReportClassResult;
import up.envisage.mapable.ui.home.report.ReportResult;

public interface RetrofitInterface {

    @POST("/users/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);
    //you call the java class here

    @GET("/users/getID")
    Call<userID> getUser(@QueryMap Map<String, String> options);

    @POST("/users/signup")
    Call<Void> executeSignup(@Body HashMap<String, String> map);

    @POST("/reports/submit")
    Call<ReportClassResult> executeReportSubmit(@Body HashMap<String, String> map);

    @POST("/images/upload")
    Call<Void> executeImageUpload(@Body HashMap<String, String> map);
}
