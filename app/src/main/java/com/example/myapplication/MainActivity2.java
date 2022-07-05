package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.toolbox.HttpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMain2Binding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private FirebaseDatabase db;
    private DatabaseReference mData;
    private FirebaseUser currentUser;
    private Uri imageUri;
    private ImageView newcontactpopup_image;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    HashMap<String, String> userMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        currentUser = mAuth.getCurrentUser();
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
        EditText newcontactpopup_help = (EditText) contactPopupView.findViewById(R.id.edit_profile_help);
        RadioGroup radioGroup = (RadioGroup)  contactPopupView.findViewById(R.id.radio_group_edit);
        CardView newcontactpopup_save = (CardView) contactPopupView.findViewById(R.id.save_profile_btn);
        CardView newcontactpopup_cancel = (CardView) contactPopupView.findViewById(R.id.cancel_profile_btn);
        CardView newcontactpopup_clear = (CardView) contactPopupView.findViewById(R.id.clear_profile_btn);
        newcontactpopup_image = (ImageView)  contactPopupView.findViewById(R.id.edit_profile_pic);

        DatabaseReference ref_help = mData.child(currentUser.getUid()).child("help");
        ref_help.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String help_user = dataSnapshot.getValue(String.class);
                newcontactpopup_help.setText(help_user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        newcontactpopup_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newcontactpopup_mobile.setText("");
                newcontactpopup_help.setText("");
                newcontactpopup_city.setText("");
                newcontactpopup_fullname.setText("");
            }
        });

        newcontactpopup_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        newcontactpopup_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!newcontactpopup_fullname.getText().toString().isEmpty())
                    mData.child(currentUser.getUid()).child("username").setValue(newcontactpopup_fullname.getText().toString());

                if(!newcontactpopup_city.getText().toString().isEmpty())
                    mData.child(currentUser.getUid()).child("city").setValue(newcontactpopup_city.getText().toString());

                if(!newcontactpopup_mobile.getText().toString().isEmpty())
                    mData.child(currentUser.getUid()).child("phone").setValue(newcontactpopup_mobile.getText().toString());

                if(!newcontactpopup_help.getText().toString().isEmpty())
                    mData.child(currentUser.getUid()).child("help").setValue(newcontactpopup_help.getText().toString());

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

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            newcontactpopup_image.setImageURI(imageUri);
            uploadePicture();
        }
    }

    private void uploadePicture() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image ...");
        pd.show();

        StorageReference ref = storageReference.child("images/" + currentUser.getUid());

        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Snackbar.make(findViewById(android.R.id.content),"Image uploaded",Snackbar.LENGTH_LONG).show();
                        mData.child(currentUser.getUid()).child("image").setValue(uri.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(),"Failed To Upload", Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Progress: " + (int) progress + "%");
            }
        });
    }

    public Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("1", "Error getting bitmap", e);
        }
        return bm;
    }
}