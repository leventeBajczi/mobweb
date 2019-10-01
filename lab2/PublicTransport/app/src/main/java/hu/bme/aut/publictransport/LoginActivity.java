package hu.bme.aut.publictransport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etEmailAddress = findViewById(R.id.etEmailAddress);
        final EditText etPassword = findViewById(R.id.etPassword);
        final Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new MyLoginListener(etEmailAddress, etPassword));
    }

    private class MyLoginListener implements View.OnClickListener {
        private final EditText etEmailAddress;
        private final EditText etPassword;

        private MyLoginListener(EditText etEmailAddress, EditText etPassword) {
            this.etEmailAddress = etEmailAddress;
            this.etPassword = etPassword;
        }

        @Override
        public void onClick(View view) {
            if (etEmailAddress.getText().toString().isEmpty()) {
                etEmailAddress.requestFocus();
                etEmailAddress.setError("Please enter your email address");
                return;
            }

            if (etPassword.getText().toString().isEmpty()) {
                etPassword.requestFocus();
                etPassword.setError("Please enter your password");
                return;
            }

            //TODO login

            Intent intent = new Intent(LoginActivity.this, ListActivity.class);
            startActivity(intent);
        }
    }
}
