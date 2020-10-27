package up.envisage.mapable.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import up.envisage.mapable.db.table.ReportTable;
import up.envisage.mapable.db.table.UserTable;
import up.envisage.mapable.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private LiveData<List<UserTable>> allUsers;
    private LiveData<UserTable> lastUser;

    public UserViewModel(@NonNull Application application) {
        super(application);

        userRepository = new UserRepository(application);
        allUsers = userRepository.getAllUsers();
        lastUser = userRepository.getLastUser();
    }

    public void insert(UserTable user) {
        userRepository.insertUser(user);
    }

    public LiveData<UserTable> getLastUser() {
        return lastUser;
    }

    public LiveData<List<UserTable>> getUsers() {
        return allUsers;
    }
}
