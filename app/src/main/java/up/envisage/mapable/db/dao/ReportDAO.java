package up.envisage.mapable.db.dao;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import up.envisage.mapable.db.table.ReportTable;
import up.envisage.mapable.db.table.UserTable;

public interface ReportDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(ReportTable reportTable);
}
