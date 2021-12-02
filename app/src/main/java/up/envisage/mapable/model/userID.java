package up.envisage.mapable.model;


import com.google.gson.annotations.SerializedName;

public class userID {

    @SerializedName("_id")
    private String _id;

    @SerializedName("name")
    private String name;

    @SerializedName("number")
    private String number;

    @SerializedName("email")
    private String email;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("numOfReports")
    private Integer numOfReports;

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Integer getNumOfReports() { return numOfReports; }

}
