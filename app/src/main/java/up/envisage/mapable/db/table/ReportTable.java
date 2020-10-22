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

    @ColumnInfo (name = "latitude")
    private long latitude;

    @ColumnInfo (name = "longitude")
    private long longitude;

    @ColumnInfo (name = "report")
    private String report;

    @ColumnInfo (name = "photo")
    private int photo;


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

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
    
    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
