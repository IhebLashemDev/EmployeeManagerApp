package com.example.ettt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "EmployeeDB";
    private static final int DATABASE_VERSION = 2; // Incrémentez la version pour appliquer les modifications
    private static final String TABLE_EMPLOYEES = "employees";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Création de la table 'employees'
        String createTable = "CREATE TABLE " + TABLE_EMPLOYEES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +  // Champ obligatoire pour le nom
                "password TEXT NOT NULL, " + // Champ obligatoire pour le mot de passe
                "position TEXT, " + // Champ pour la position (facultatif)
                "salary REAL DEFAULT 0)";    // Champ facultatif avec une valeur par défaut
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Suppression de l'ancienne table et recréation avec la nouvelle structure
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
        onCreate(db);
    }

    // Insertion d'un employé
    public long insertEmployee(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TABLE_EMPLOYEES, null, values);
    }

    // Mise à jour des informations d'un employé
    public boolean updateEmployee(int id, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.update(TABLE_EMPLOYEES, values, "id=?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }


    public Employee getEmployeeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EMPLOYEES, null, "id=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Employee employee = new Employee(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("position")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("salary")),
                    cursor.getString(cursor.getColumnIndexOrThrow("password"))
            );
            cursor.close();
            return employee;
        }

        if (cursor != null) {
            cursor.close();
        }

        return null;
    }


    // Récupérer tous les employés
    public Cursor getAllEmployees() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EMPLOYEES, null);
    }

    // Supprimer un employé par ID
    public boolean deleteEmployee(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_EMPLOYEES, "id=?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0;
    }

    // Validation de la connexion d'un utilisateur par nom et mot de passe
    public boolean validateLogin(String name, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EMPLOYEES, new String[]{"id"},
                "name = ? AND password = ?", new String[]{name, password},
                null, null, null);

        boolean isValid = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        return isValid;
    }

    // Vérification si le nom existe déjà
    public boolean isNameExist(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EMPLOYEES, new String[]{"id"},
                "name = ?", new String[]{name}, null, null, null);

        boolean exists = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }
}
