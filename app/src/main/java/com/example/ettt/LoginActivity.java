package com.example.ettt;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DBHelper(this);

        EditText nameInput = findViewById(R.id.nameInput); // Remplacer emailInput par nameInput
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button loginButton = findViewById(R.id.loginButton);
        TextView createAccountLink = findViewById(R.id.createAccountLink);

        loginButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String password = passwordInput.getText().toString();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Vérifier les identifiants
            if (dbHelper.validateLogin(name, password)) {
                Toast.makeText(LoginActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, EmployeeListActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Nom ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
            }
        });

        createAccountLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
