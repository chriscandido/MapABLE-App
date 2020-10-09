package up.envisage.mapable.ui.registration;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);
//    you call the java class here

    @POST("/signup")
    Call<Void> executeSignup(@Body HashMap<String, String> map);
}
