package com.example.d308_app.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d308_app.R;
import com.example.d308_app.database.VacationDatabaseBuilder;
import com.example.d308_app.entities.User;

public class MainActivity extends AppCompatActivity {
    public static int numAlert;
    private EditText usernameInput;
    private EditText passwordInput;
    private VacationDatabaseBuilder db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        Button button = findViewById(R.id.loginButton);
        Button button2 = findViewById(R.id.createAccount);

        db = VacationDatabaseBuilder.getDatabase(getApplicationContext());
        if(isLoggedIn()){
            startVacaList();
            return;
        }

        button.setOnClickListener(v -> attemptLogin());
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateAccount.class);
                startActivity(intent);
            }
        });
    }
    private void attemptLogin() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString();

        User user = db.userDAO().getUserByUsername(username);

        if (user == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            return;
        }

        if (PasswordUtils.verifyPassword(password, user.getPasswordHash())) {
            saveLoginState(username);
            startVacaList();
        } else {
            Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveLoginState(String username) {
        getSharedPreferences("login", MODE_PRIVATE)
                .edit()
                .putString("username", username)
                .apply();
    }

    private boolean isLoggedIn() {
        return getSharedPreferences("login", MODE_PRIVATE)
                .contains("username");
    }

    private void startVacaList() {
        startActivity(new Intent(MainActivity.this, VacationList.class));
        finish();
    }
}