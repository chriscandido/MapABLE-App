package up.envisage.mapable.db;

import android.content.Context;
import android.provider.ContactsContract;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import up.envisage.mapable.db.dao.ReportDAO;
import up.envisage.mapable.db.dao.UserDAO;
import up.envisage.mapable.db.table.ReportTable;
import up.envisage.mapable.db.table.UserTable;

@androidx.room.Database(entities =  {UserTable.class, ReportTable.class},
        version = 1,
        exportSchema = false
)
public abstract class Database extends RoomDatabase {

    //This is abstract method implemented by the Room framework
    public abstract UserDAO userDAO();
    public abstract ReportDAO reportDAO();

    private static Database INSTANCE;

    public static Database getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class, "userDB")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
