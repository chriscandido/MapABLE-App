package up.envisage.mapable.db;

import android.content.Context;
import android.provider.ContactsContract;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import up.envisage.mapable.db.dao.UserDAO;
import up.envisage.mapable.db.table.UserTable;

@androidx.room.Database(entities =  {UserTable.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

    public abstract UserDAO userDAO();

    private static Database INSTANCE;

    public static Database getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class, "userDB").build();
                }
            }
        }
        return INSTANCE;
    }
}
