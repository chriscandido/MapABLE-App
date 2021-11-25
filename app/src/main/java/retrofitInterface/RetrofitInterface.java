package retrofitInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import up.envisage.mapable.model.Badges;
import up.envisage.mapable.model.Image;
import up.envisage.mapable.model.StatsResult;
import up.envisage.mapable.model.UserReport;
import up.envisage.mapable.model.userID;
import up.envisage.mapable.ui.home.report.FeedbackResult;
import up.envisage.mapable.ui.home.report.ReportClassResult;
import up.envisage.mapable.ui.registration.LoginResult;

public interface RetrofitInterface {

    @POST("/users/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);
    //you call the java class here

    @GET("/users/getID")
    Call<userID> getUser(@QueryMap Map<String, String> options);

    @GET("/users/stats")
    Call<StatsResult> getStats(@QueryMap Map<String, String> options);

    @POST("/users/signup")
    Call<Void> executeSignup(@Body HashMap<String, String> map);

    @POST("/reports/submit")
    Call<ReportClassResult> executeReportSubmit(@Body HashMap<String, String> map);

    @POST("/feedback/submit")
    Call<FeedbackResult> submitFeedback(@Body HashMap<String, String> map);

    @GET("/reports")
    Call<List> getUserReports(@QueryMap Map<String, String> options);

    @GET("/users/reports")
    Call<List<UserReport>> getUserReportsList(@QueryMap Map<String, String> options);

    @GET("/images/getImage/flat")
    Call<Image> getImage(@QueryMap Map<String, String> options);

    @GET("/users/badges")
    Call<Badges> getBadges(@QueryMap Map<String, String> options);

}
