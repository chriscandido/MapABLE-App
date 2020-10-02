package up.envisage.mapable.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import up.envisage.mapable.db.table.ReportTable;
import up.envisage.mapable.db.table.UserTable;

@Dao
public interface ReportDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(ReportTable reportTable);
}
