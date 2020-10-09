package up.envisage.mapable.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import up.envisage.mapable.db.table.ReportTable;
import up.envisage.mapable.db.table.UserTable;

@Dao
public interface ReportDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(ReportTable reportTable);

    @Query ("SELECT * FROM Report_Profile")
    LiveData<List<ReportTable>> getAllReports();
}
