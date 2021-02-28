package up.envisage.mapable.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import up.envisage.mapable.db.table.ReportTable;
import up.envisage.mapable.db.table.UserTable;

@Dao
public interface ReportDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReport(ReportTable reportTable);

    @Query ("SELECT * FROM Report_Profile")
    LiveData<List<ReportTable>> getAllReports();

    @Query ("SELECT * FROM Report_Profile ORDER BY report_id DESC LIMIT 1")
    LiveData<ReportTable> getLastReport();

    @Query ("SELECT COUNT(report) FROM Report_Profile")
    LiveData<Integer> getCount();

    @Query ("DELETE FROM Report_Profile")
    void deleteAllReports();

    @Delete
    void deleteReport(ReportTable reportTable);

    @Update
    void updateReport(ReportTable reports);
}
