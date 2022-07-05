package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase db;
    private DatabaseReference mData;
    private FirebaseUser currentUser;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
    }

    boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        db = FirebaseDatabase.getInstance();
        mData = db.getReference().child("Users");
    }

    public void loginFunc(View view) {

        String email = ((EditText) findViewById(R.id.email_login)).getText().toString().trim();
        String password = ((EditText) findViewById(R.id.password_login)).getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_LONG).show();
                            Navigation.findNavController(view).navigate(R.id.action_blankFragment_to_mainActivity2);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed to login", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    public void registerFunc(View view) {

        String email = ((EditText) findViewById(R.id.email_register)).getText().toString().trim();
        String password = ((EditText) findViewById(R.id.password_register)).getText().toString().trim();
        String phone = ((EditText) findViewById(R.id.phone_register)).getText().toString().trim();
        String username = ((EditText) findViewById(R.id.username_register)).getText().toString().trim();
        mData.push().get();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Registered Successful", Toast.LENGTH_LONG).show();
                            Navigation.findNavController(view).navigate(R.id.action_blankFragment2_to_mainActivity2);
                            mAuth.signInWithEmailAndPassword(email, password);
                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("username", username);
                            userMap.put("email", email);
                            userMap.put("phone", phone);
                            currentUser = mAuth.getCurrentUser();
                            System.out.println(currentUser.getUid());
                            mData.child(currentUser.getUid()).setValue(userMap);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed to register", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    public String getUserName() {
        return Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
    }

}