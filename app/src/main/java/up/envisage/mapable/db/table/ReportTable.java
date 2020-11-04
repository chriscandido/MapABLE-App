package up.envisage.mapable.db.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Report_Profile")
public class ReportTable {

    @ColumnInfo (name = "unique_id")
    private String uniqueId;

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "report_id")
    private int reportId;

    @ColumnInfo (name = "dateTime")
    private String dateTime;

    @ColumnInfo (name = "incident_type")
    private String incidentType;

    @ColumnInfo (name = "latitude")
    private double latitude;

    @ColumnInfo (name = "longitude")
    private double longitude;

    @ColumnInfo (name = "report")
    private String report;

    @ColumnInfo (name = "photo")
    private String photo;


    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
    
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
