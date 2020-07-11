package up.envisage.mapable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import up.envisage.mapable.MainActivity;
import up.envisage.mapable.R;
import up.envisage.mapable.databinding.ActivityRegisterBinding;
import up.envisage.mapable.databinding.Listener;
import up.envisage.mapable.db.table.UserTable;
import up.envisage.mapable.model.RegisterViewModel;


public class Register extends AppCompatActivity implements Listener {

    TextInputLayout textInputLayout_registerName, textInputLayout_registerEmail,
            textInputLayout_registerUsername, textInputLayout_registerPassword;

    Button button_submit;

    TextView textView_registerLogin;

    private RegisterViewModel registerViewModel;
    private ActivityRegisterBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(Register.this, R.layout.activity_register);
        binding.setClickListener((Register) this);

        registerViewModel = ViewModelProviders.of(Register.this).get(RegisterViewModel.class);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit:
                String name = binding.textInputLayoutRegisterName.getEditText().getText().toString().trim();
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
                    user.setEmail(email);
                    user.setUsername(username);
                    user.setPassword(password);
                    registerViewModel.insert(user);
                    Intent intent = new Intent(Register.this, MainActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.textView_registerLogin:
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                break;

        }
    }
}
