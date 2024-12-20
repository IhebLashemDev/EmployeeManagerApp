package com.example.ettt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {
    private final ArrayList<String> employeeList;
    private final Context context;

    public EmployeeAdapter(Context context, ArrayList<String> employeeList) {
        this.context = context;
        this.employeeList = employeeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String employee = employeeList.get(position);
        holder.textView.setText(employee);

        holder.itemView.setOnClickListener(v -> {
            int employeeId = Integer.parseInt(employee.split(":")[0]);
            Intent intent = new Intent(context, EmployeeFormActivity.class);
            intent.putExtra("EMPLOYEE_ID", employeeId);
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            int employeeId = Integer.parseInt(employee.split(":")[0]);
            new AlertDialog.Builder(context)
                    .setTitle("Delete Employee")
                    .setMessage("Are you sure you want to delete this employee?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        DBHelper dbHelper = new DBHelper(context);
                        boolean result = dbHelper.deleteEmployee(employeeId);
                        if (result) {
                            Toast.makeText(context, "Employee deleted", Toast.LENGTH_SHORT).show();
                            employeeList.remove(position);
                            notifyItemRemoved(position);
                        } else {
                            Toast.makeText(context, "Failed to delete employee", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
//