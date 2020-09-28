package up.envisage.mapable.db.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import up.envisage.mapable.util.LatLng;

@Entity(tableName = "Report_Profile",
        foreignKeys = {
        @ForeignKey(entity = UserTable.class, parentColumns = "unique_id", childColumns = "unique_id")
        })
public class ReportTable {

    @ColumnInfo (name = "unique_id")
    private String uniqueId;

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "report_id")
    private int reportId;

    @ColumnInfo (name = "dateTime")
    private long dateTime;

    @ColumnInfo (name = "incident_type")
    private String incidentType;

    @ColumnInfo (name = "location")
    private LatLng location;

    @ColumnInfo (name = "answer")
    private String answer;

    @ColumnInfo (name = "photo")
    private int photo;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public int getRid() {
        return reportId;
    }

    public void setRid(int rid) {
        this.reportId = reportId;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
