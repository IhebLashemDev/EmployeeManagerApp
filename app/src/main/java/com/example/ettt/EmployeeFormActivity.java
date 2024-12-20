package com.example.ettt;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EmployeeFormActivity extends AppCompatActivity {

    private EditText etName, etPosition, etSalary;
    private Button btnSave;

    private DBHelper dbHelper;
    private boolean isEditMode = false;
    private int employeeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_form);

        etName = findViewById(R.id.etName);
        etPosition = findViewById(R.id.etPosition);
        etSalary = findViewById(R.id.etSalary);
        btnSave = findViewById(R.id.btnSave);

        dbHelper = new DBHelper(this);

        // Check if it's an edit operation
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            isEditMode = true;
            employeeId = intent.getIntExtra("id", -1);
            loadEmployeeData(employeeId);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEmployee();
            }
        });
    }

    private void loadEmployeeData(int id) {
        Employee employee = dbHelper.getEmployeeById(id);
        if (employee != null) {
            etName.setText(employee.getName());
            etPosition.setText(employee.getPosition());
            etSalary.setText(String.valueOf(employee.getSalary()));
        } else {
            Toast.makeText(this, "Erreur de chargement des données de l'employé", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveEmployee() {
        String name = etName.getText().toString().trim();
        String position = etPosition.getText().toString().trim();
        String salaryStr = etSalary.getText().toString().trim();

        // Ajoutez un champ de mot de passe pour l'insertion
        String password = "default_password"; // Remplacez ceci par un champ password si nécessaire.

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(position) || TextUtils.isEmpty(salaryStr)) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        float salary;
        try {
            salary = Float.parseFloat(salaryStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Veuillez entrer un salaire valide", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("position", position);
        values.put("salary", salary);
        values.put("password", password); // Ajout du mot de passe

        if (isEditMode) {
            // Mise à jour de l'employé existant
            if (dbHelper.updateEmployee(employeeId, values)) {
                Toast.makeText(this, "Employé mis à jour avec succès", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erreur lors de la mise à jour de l'employé", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Ajout d'un nouvel employé
            if (dbHelper.insertEmployee(values) > 0) {
                Toast.makeText(this, "Employé ajouté avec succès", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erreur lors de l'ajout de l'employé", Toast.LENGTH_SHORT).show();
            }
        }

        finish(); // Ferme le formulaire et retourne à la liste des employés
    }

}
//