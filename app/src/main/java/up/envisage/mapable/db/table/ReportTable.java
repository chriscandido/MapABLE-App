package up.envisage.mapable.db.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import up.envisage.mapable.util.LatLng;

@Entity(tableName = "Report_Profile")
public class ReportTable {

    @PrimaryKey
    @ColumnInfo (name = "uniqueId")
    private String uniqueId;

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "id")
    private int id;

    @ColumnInfo (name = "name")
    private String name;

    @ColumnInfo (name = "dateTime")
    private long dateTime;

    @ColumnInfo (name = "incidentType")
    private String incidentType;

    @ColumnInfo (name = "location")
    private LatLng location;

    @ColumnInfo (name = "answer")
    private String answer;

    @ColumnInfo (name = "photo")
    private int photo;


}
