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
public interface UserDAO {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserTable users);

    @Query ("SELECT * FROM User_Profile")
    LiveData<List<UserTable>> getAllUsers();

    @Query ("SELECT * FROM User_Profile ORDER BY id DESC LIMIT 1")
    LiveData<UserTable> getLastUser();

    @Query ("SELECT * FROM User_Profile WHERE username LIKE :username AND password LIKE :password LIMIT 1")
    LiveData<UserTable> verifyUserLogin(String username, String password);

    @Query ("DELETE FROM User_Profile")
    void deleteAllUsers();

    @Delete
    void deleteUser(UserTable usersTable);

    @Update
    void updateUser(UserTable users);

}
