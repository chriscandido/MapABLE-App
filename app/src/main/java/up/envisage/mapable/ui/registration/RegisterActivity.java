package up.envisage.mapable.ui.registration;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;
import up.envisage.mapable.databinding.ActivityRegisterBinding;
import up.envisage.mapable.databinding.Listener;
import up.envisage.mapable.db.table.UserTable;
import up.envisage.mapable.model.UserViewModel;


public class RegisterActivity extends AppCompatActivity implements Listener {

    private UserViewModel registerViewModel;
    private ActivityRegisterBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(RegisterActivity.this, R.layout.activity_register);
        binding.setClickListener((RegisterActivity) this);

        registerViewModel = ViewModelProviders.of(RegisterActivity.this).get(UserViewModel.class);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_submit:
                String name = binding.textInputLayoutRegisterName.getEditText().getText().toString().trim();
                String number = binding.textInputLayoutRegisterMobileNum.getEditText().getText().toString().trim();
                String email = binding.textInputLayoutRegisterEmail.getEditText().getText().toString().trim();
                String username = binding.textInputLayoutRegisterUsername.getEditText().getText().toString().trim();
                String password = binding.textInputLayoutRegisterPassword.getEditText().getText().toString().trim();

                UserTable user = new UserTable();
                if (TextUtils.isEmpty(name)) {
                    binding.textInputLayoutRegisterName.setError("Please Enter Your Name");
                }

                else if (TextUtils.isEmpty(email)) {
                    binding.textInputLayoutRegisterEmail.setError("Please Enter Your Email");
                }

                else if (TextUtils.isEmpty(username)) {
                    binding.textInputLayoutRegisterUsername.setError("Please Enter Username");
                }

                else if (TextUtils.isEmpty(password)) {
                    binding.textInputLayoutRegisterPassword.setError("Please Enter Password");
                }

                else {
                    user.setName(name);
                    user.setNumber(number);
                    user.setEmail(email);
                    user.setUsername(username);
                    user.setPassword(password);
                    registerViewModel.insert(user);
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.textView_registerLogin:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                break;

        }
    }
}
