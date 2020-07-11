package up.envisage.mapable.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

import up.envisage.mapable.db.Database;
import up.envisage.mapable.db.dao.UserDAO;
import up.envisage.mapable.db.table.UserTable;

public class UserRepository {

    private UserDAO userDAO;
    private LiveData<List<UserTable>> allUsers;

    public UserRepository (Application application) {
        Database db = Database.getInstance(application);
        userDAO = db.userDAO();
        allUsers = userDAO.getAllUsers();
    }

    public LiveData<List<UserTable>> getAllUsers() {
        return allUsers;
    }

    public void insertUser(UserTable userTable) {
        new insertUserAsyncTask(userDAO).execute(userTable);
    }

    public static class insertUserAsyncTask extends AsyncTask<UserTable, Void, Void> {

        private UserDAO userDAO;

        private insertUserAsyncTask(UserDAO dao) {
            userDAO = dao;
        }

        protected Void doInBackground(UserTable... users) {
            userDAO.insertUser(users[0]);
            Log.v("[ UserRepository.java ]", "Data Successfully Inserted");
            return null;
        }

    }
}
