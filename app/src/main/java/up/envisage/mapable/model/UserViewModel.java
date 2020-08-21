package up.envisage.mapable.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import up.envisage.mapable.db.table.UserTable;
import up.envisage.mapable.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private LiveData<List<UserTable>> getAllUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);

        userRepository = new UserRepository(application);
        getAllUsers = userRepository.getAllUsers();
    }

    public void insert(UserTable user) {
        userRepository.insertUser(user);
    }

    public LiveData<List<UserTable>> getUsers() {
        return getAllUsers;
    }
}
