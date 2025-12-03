package com.example.d308_app.UI;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d308_app.R;
import com.example.d308_app.database.VacationDatabaseBuilder;
import com.example.d308_app.entities.User;

public class CreateAccount extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private EditText confirmInput;
    private VacationDatabaseBuilder db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        usernameInput = findViewById(R.id.registerUsername);
        passwordInput = findViewById(R.id.registerPassword);
        confirmInput = findViewById(R.id.registerConfirm);
        Button createButton = findViewById(R.id.registerCreateButton);

        db = VacationDatabaseBuilder.getDatabase(getApplicationContext());

        createButton.setOnClickListener(v -> createAccount());
    }

    private void createAccount() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        String confirm = confirmInput.getText().toString();

        if(username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show();
            return;
        }

        if(!password.equals(confirm)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
            return;
        }

        if(!PasswordUtils.isValidPassword(password)){
            Toast.makeText(this, "Password must meet specified requirements", Toast.LENGTH_LONG).show();
            return;
        }

        if(db.userDAO().getUserByUsername(username) != null) {
            Toast.makeText(this, "User already exists", Toast.LENGTH_LONG).show();
            return;
        }

        String hash = PasswordUtils.hashPassword(password);
        User user = new User(0, username, hash);
        db.userDAO().insert(user);

        Toast.makeText(this, "Account created!", Toast.LENGTH_LONG).show();
        finish();
    }
}
