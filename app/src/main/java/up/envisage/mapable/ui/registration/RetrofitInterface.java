package up.envisage.mapable.ui.registration;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import up.envisage.mapable.ui.home.report.ReportClassResult;
import up.envisage.mapable.ui.home.report.ReportResult;

public interface RetrofitInterface {

    @POST("/users/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);
    //you call the java class here

    @POST("/users/signup")
    Call<Void> executeSignup(@Body HashMap<String, String> map);

    @POST("/reports/submit")
    Call<ReportClassResult> executeReportSubmit(@Body HashMap<String, String> map);
}
