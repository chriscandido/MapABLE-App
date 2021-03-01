package up.envisage.mapable.ui.registration;

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

    @SerializedName("pollution")
    private Integer pollution;

    @SerializedName("illegalReclamation")
    private Integer illegalReclamation;

    @SerializedName("total")
    private Integer total;

    public Object getData() { return data; }

    public Integer getAlgalBloom() { return algalBloom; }

    public Integer getFishKill() { return fishKill; }

    public Integer getPollution() { return pollution; }

    public Integer getIllegalReclamation() { return illegalReclamation; }

    public Integer getTotal() { return total; }


}
