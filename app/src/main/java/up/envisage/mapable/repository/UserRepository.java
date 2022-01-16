package up.envisage.mapable.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import up.envisage.mapable.db.Database;
import up.envisage.mapable.db.dao.UserDAO;
import up.envisage.mapable.db.table.UserTable;

public class UserRepository {

    private final UserDAO userDAO;
    private final LiveData<List<UserTable>> allUsers;
    private final LiveData<UserTable> lastUser;

    public UserRepository (Application application) {
        Database db = Database.getInstance(application);
        userDAO = db.userDAO();
        allUsers = userDAO.getAllUsers();
        lastUser = userDAO.getLastUser();
    }

    public LiveData<List<UserTable>> getAllUsers() {
        return allUsers;
    }

    public LiveData<UserTable> getLastUser() {
        return lastUser;
    }

    public void insertUser(UserTable userTable) {
        new insertUserAsyncTask(userDAO).execute(userTable);
    }

    //----------------------------------------------------------------------------------------------Inserting User Data to local DB
    public static class insertUserAsyncTask extends AsyncTask<UserTable, Void, Void> {

        private final UserDAO userDAO;

        private insertUserAsyncTask(UserDAO dao) {
            userDAO = dao;
        }

        @Override
        protected Void doInBackground(UserTable... users) {
            userDAO.insertUser(users[0]);
            Log.v("[ UserRepository.java ]", "Data Successfully Inserted");
            return null;
        }
    }
}
