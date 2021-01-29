package up.envisage.mapable.ui.home.report;

import com.google.gson.annotations.SerializedName;

public class FeedbackResult {

    @SerializedName("_id")
    private String _id;

    @SerializedName("userID")
    private String userID;

    @SerializedName("model")
    private String model;

    @SerializedName("feedback")
    private String feedback;

    public String get_id() {
        return _id;
    }

    public String getUsrID() {
        return userID;
    }

    public String getDeviceModel() {
        return model;
    }

    public String getFeedback() {
        return feedback;
    }

}
