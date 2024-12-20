package com.example.ettt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Redirection vers SplashActivity
        Intent intent = new Intent(MainActivity.this, SplashActivity.class);
        startActivity(intent);
        finish(); // Ferme MainActivity pour éviter de revenir ici

        // Initialiser Firebase Auth et Google Sign-In
        firebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Remplacez par votre ID client Web
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        Button googleSignInButton = findViewById(R.id.btnGoogleSignIn);
        googleSignInButton.setOnClickListener(v -> signInWithGoogle());
    }

    // Méthode pour lancer l'activité de connexion Google
    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Gérer la réponse de l'activité de connexion Google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignIn.getSignedInAccountFromIntent(data)
                    .addOnCompleteListener(task -> {
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            firebaseAuthWithGoogle(account.getIdToken());
                        } catch (ApiException e) {
                            Log.w("GoogleSignIn", "Google sign-in failed", e);
                            Toast.makeText(this, "Échec de la connexion Google", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // Authentifier l'utilisateur avec Firebase en utilisant le token Google
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Toast.makeText(MainActivity.this, "Connexion réussie: " + user.getDisplayName(), Toast.LENGTH_LONG).show();
                        // Rediriger l'utilisateur vers la page d'accueil ou autre activité
                        Intent intent = new Intent(MainActivity.this, EmployeeListActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.w("GoogleSignIn", "Échec de la connexion", task.getException());
                        Toast.makeText(MainActivity.this, "Échec de la connexion avec Firebase", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
