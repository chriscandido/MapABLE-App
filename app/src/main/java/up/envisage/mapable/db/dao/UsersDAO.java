package up.envisage.mapable.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import up.envisage.mapable.db.table.UsersTable;

@Dao
public interface UsersDAO {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UsersTable users);

    @Query ("SELECT * FROM User_Profile")
    LiveData<List<UsersTable>> getAllUsers();

    @Query ("SELECT * FROM User_Profile WHERE username LIKE :username LIMIT 1")
    LiveData<UsersTable> findByUsername(String username);

    @Query ("DELETE FROM User_Profile")
    void deleteAllUsers();

}
