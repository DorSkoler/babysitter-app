package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMain2Binding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private FirebaseDatabase db;
    private DatabaseReference mData;
    private FirebaseUser currentUser;
    HashMap<String, String> userMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        ActivityMain2Binding binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_profile, R.id.navigation_explore, R.id.navigation_chat, R.id.navigation_history)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        mData = db.getReference().child("Users");
    }

    public void signOutUser(View view) {
        Navigation.findNavController(view).navigate(R.id.action_navigation_profile_to_splashActivity);
        finish();
    }

    public void createPopUpEdit() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.fragment_edit_profile,  null);
        EditText newcontactpopup_fullname = (EditText) contactPopupView.findViewById(R.id.edit_profile_name);
        EditText newcontactpopup_city = (EditText) contactPopupView.findViewById(R.id.edit_profile_city);
        EditText newcontactpopup_mobile = (EditText) contactPopupView.findViewById(R.id.edit_profile_mobile);
        RadioGroup radioGroup = (RadioGroup)  contactPopupView.findViewById(R.id.radio_group_edit);
        CardView newcontactpopup_save = (CardView) contactPopupView.findViewById(R.id.save_profile_btn);
        CardView newcontactpopup_cancel = (CardView) contactPopupView.findViewById(R.id.cancel_profile_btn);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        newcontactpopup_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentUser = mAuth.getCurrentUser();


                if(!newcontactpopup_fullname.getText().toString().isEmpty())
                    mData.child(currentUser.getUid()).child("username").setValue(newcontactpopup_fullname.getText().toString());

                if(!newcontactpopup_city.getText().toString().isEmpty())
                    mData.child(currentUser.getUid()).child("city").setValue(newcontactpopup_city.getText().toString());

                if(!newcontactpopup_mobile.getText().toString().isEmpty())
                    mData.child(currentUser.getUid()).child("phone").setValue(newcontactpopup_mobile.getText().toString());

                if (radioGroup.getCheckedRadioButtonId() != -1) {
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    View radioButton = radioGroup.findViewById(radioButtonID);
                    int idx = radioGroup.indexOfChild(radioButton);
                    RadioButton r = (RadioButton) radioGroup.getChildAt(idx);
                    mData.child(currentUser.getUid()).child("type").setValue(r.getText().toString());
                }

                dialog.dismiss();
            }
        });

        newcontactpopup_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}