package com.example.ettt;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EmployeeEditActivity extends AppCompatActivity {

    private EditText edtName, edtPosition, edtSalary;
    private Button btnSave;
    private DBHelper dbHelper;
    private int employeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_edit);

        edtName = findViewById(R.id.edtName);
        edtPosition = findViewById(R.id.edtPosition);
        edtSalary = findViewById(R.id.edtSalary);
        btnSave = findViewById(R.id.btnSave);
        dbHelper = new DBHelper(this);

        // Récupérer l'ID de l'employé à partir de l'intent
        Intent intent = getIntent();
        employeeId = intent.getIntExtra("EMPLOYEE_ID", -1);

        if (employeeId != -1) {
            loadEmployeeData(employeeId);
        }

        btnSave.setOnClickListener(v -> saveEmployeeData());
    }

    // Charger les données existantes de l'employé dans les champs
    private void loadEmployeeData(int employeeId) {
        Employee employee = dbHelper.getEmployeeById(employeeId);
        if (employee != null) {
            edtName.setText(employee.getName());
            edtPosition.setText(employee.getPosition());
            edtSalary.setText(String.valueOf(employee.getSalary()));
        }
    }

    // Sauvegarder les nouvelles données de l'employé
    private void saveEmployeeData() {
        String name = edtName.getText().toString();
        String position = edtPosition.getText().toString();
        String salaryString = edtSalary.getText().toString();

        if (name.isEmpty() || position.isEmpty() || salaryString.isEmpty()) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        double salary = Double.parseDouble(salaryString);

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("position", position);
        values.put("salary", salary);

        boolean isUpdated = dbHelper.updateEmployee(employeeId, values);
        if (isUpdated) {
            Toast.makeText(this, "Employee updated successfully", Toast.LENGTH_SHORT).show();
            finish();  // Retour à la liste des employés
        } else {
            Toast.makeText(this, "Failed to update employee", Toast.LENGTH_SHORT).show();
        }
    }
}
