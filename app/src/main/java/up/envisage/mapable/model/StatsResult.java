package up.envisage.mapable.model;

import com.google.gson.annotations.SerializedName;

public class StatsResult {

    @SerializedName("data")
    private Object data;

    @SerializedName("userID")
    private String userID;

    @SerializedName("algalBloom")
    private Integer algalBloom;

    @SerializedName("fishKill")
    private Integer fishKill;

    @SerializedName("waterPollution")
    private Integer waterPollution;

    @SerializedName("ongoingReclamation")
    private Integer ongoingReclamation;

    @SerializedName("waterHyacinth")
    private Integer waterHyacinth;

    @SerializedName("solidWaste")
    private Integer solidWaste;

    @SerializedName("others")
    private Integer others;

    @SerializedName("verified")
    private Integer verified;

    @SerializedName("unverified")
    private Integer unverified;

    @SerializedName("falsePositive")
    private Integer falsePositive;

    @SerializedName("total")
    private Integer total;

    public Object getData() { return data; }

    public Integer getAlgalBloom() { return algalBloom; }

    public Integer getFishKill() { return fishKill; }

    public Integer getWaterPollution() { return waterPollution; }

    public Integer getOngoingReclamation() { return ongoingReclamation; }

    public Integer getWaterHyacinth() {
        return waterHyacinth;
    }

    public Integer getSolidWaste() {
        return solidWaste;
    }

    public Integer getOthers() {
        return others;
    }

    public Integer getVerified() {
        return verified;
    }

    public Integer getUnverified() {
        return unverified;
    }

    public Integer getFalsePositive() {
        return falsePositive;
    }

    public Integer getTotal() { return total; }


}
