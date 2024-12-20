package com.example.ettt;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EmployeeListActivity extends AppCompatActivity {

    private ListView listView;
    private Button btnAddEmployee;
    private DBHelper dbHelper;
    private ArrayList<String> employeeList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        listView = findViewById(R.id.listView);
        btnAddEmployee = findViewById(R.id.btnAddEmployee);
        dbHelper = new DBHelper(this);
        employeeList = new ArrayList<>();

        loadEmployees();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, employeeList);
        listView.setAdapter(adapter);

        btnAddEmployee.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeListActivity.this, EmployeeFormActivity.class);
            startActivity(intent);
        });

        // Set the item click listener for the ListView
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedEmployee = employeeList.get(position);
            String[] parts = selectedEmployee.split(":");
            int employeeId = Integer.parseInt(parts[0].trim());

            // Show a dialog with options to modify or delete
            new AlertDialog.Builder(EmployeeListActivity.this)
                    .setTitle("Choose Action")
                    .setMessage("Do you want to modify or delete this employee?")
                    .setPositiveButton("Modify", (dialog, which) -> {
                        // Lancer l'activité de modification avec l'ID de l'employé
                        Intent intent = new Intent(EmployeeListActivity.this, EmployeeEditActivity.class);
                        intent.putExtra("EMPLOYEE_ID", employeeId);
                        startActivity(intent);
                    })
                    .setNegativeButton("Delete", (dialog, which) -> {
                        // Show confirmation dialog before deleting
                        new AlertDialog.Builder(EmployeeListActivity.this)
                                .setMessage("Are you sure you want to delete this employee?")
                                .setPositiveButton("Yes", (dialog1, which1) -> {
                                    // Lancer l'action de suppression
                                    deleteEmployee(employeeId);
                                })
                                .setNegativeButton("No", null)
                                .show();
                    })
                    .setNeutralButton("Cancel", null)
                    .show();
        });
    }

    private void loadEmployees() {
        employeeList.clear();
        Cursor cursor = dbHelper.getAllEmployees();

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                        String position = cursor.getString(cursor.getColumnIndexOrThrow("position"));
                        double salary = cursor.getDouble(cursor.getColumnIndexOrThrow("salary"));
                        employeeList.add(id + ": " + name + " - " + position + " ($" + salary + ")");
                    } while (cursor.moveToNext());
                }
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, "Invalid column name: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                cursor.close();
            }
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void deleteEmployee(int employeeId) {
        boolean isDeleted = dbHelper.deleteEmployee(employeeId);
        if (isDeleted) {
            Toast.makeText(this, "Employee deleted", Toast.LENGTH_SHORT).show();
            loadEmployees();  // Reload the employee list after deletion
        } else {
            Toast.makeText(this, "Failed to delete employee", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEmployees();  // Reload the list after returning from the Add Employee screen
    }
}
