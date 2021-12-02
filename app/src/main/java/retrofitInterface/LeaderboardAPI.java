package retrofitInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import up.envisage.mapable.model.Leaderboard;

public interface LeaderboardAPI {

    @GET("users/leaderboard")

    Call<List<Leaderboard>> getLeaderboard();

}
