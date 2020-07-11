package up.envisage.mapable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import up.envisage.mapable.R;

public class Login extends AppCompatActivity {

    TextInputLayout textInputLayout_username, textInputLayout_password;
    TextView textView_signup;
    Button button_login;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    public void initViews() {
        textInputLayout_username = findViewById(R.id.textInputLayout_loginUsername);
        textInputLayout_password = findViewById(R.id.textInputLayout_loginPassword);

        button_login = findViewById(R.id.button_login);

        textView_signup = findViewById(R.id.textView_loginSignUp);

    }

    public void goToSignup(View view) {
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
    }

}
