package com.example.ettt;

import android.content.ContentValues;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DBHelper(this);

        EditText nameInput = findViewById(R.id.nameInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        EditText confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String password = passwordInput.getText().toString();
            String confirmPassword = confirmPasswordInput.getText().toString();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
                Toast.makeText(RegisterActivity.this, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                return;
            }

            // Vérification si le nom existe déjà
            if (dbHelper.isNameExist(name)) {
                Toast.makeText(RegisterActivity.this, "Ce nom est déjà utilisé", Toast.LENGTH_SHORT).show();
                return;
            }

            // Enregistrer les données dans la base de données
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("password", password); // Champ mot de passe
            values.put("salary", 0);  // Valeur par défaut

            long result = dbHelper.insertEmployee(values);

            if (result != -1) {
                Log.d("RegisterActivity", "Insertion réussie");
                Toast.makeText(RegisterActivity.this, "Compte créé avec succès", Toast.LENGTH_SHORT).show();
                finish(); // Retour à l'écran de connexion
            } else {
                Log.d("RegisterActivity", "Échec de l'enregistrement");
                Toast.makeText(RegisterActivity.this, "Échec de l'enregistrement", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
