package com.example.myapplication.ui.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.myapplication.MainActivity;
import com.example.myapplication.MainActivity2;
import com.example.myapplication.R;
import com.example.myapplication.User;
import com.example.myapplication.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference mData;
    private FirebaseUser currentUser;
    private User userInfo;
    String data;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel dashboardViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        mData = db.getReference().child("Users");
        currentUser = mAuth.getCurrentUser();

        //hooks
        CardView logOutBtn = root.findViewById(R.id.logoutbtn);
        CardView editBtn = root.findViewById(R.id.edit_profile_btn);
        TextView address = root.findViewById(R.id.adress_profile);
        TextView email = root.findViewById(R.id.email_profile);
        TextView phone = root.findViewById(R.id.phone_profile);
        TextView username = root.findViewById(R.id.username_profile);
        TextView type = root.findViewById(R.id.type_profile);

        email.setText(currentUser.getEmail());

        DatabaseReference ref_type = mData.child(currentUser.getUid()).child("type");
        ref_type.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String type_user = dataSnapshot.getValue(String.class);
                type.setText(type_user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference ref_username = mData.child(currentUser.getUid()).child("username");
        ref_username.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username_user = dataSnapshot.getValue(String.class);
                username.setText(username_user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference ref_phone = mData.child(currentUser.getUid()).child("phone");
        ref_phone.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String phone_user = dataSnapshot.getValue(String.class);
                phone.setText(phone_user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference ref_address = mData.child(currentUser.getUid()).child("city");
        ref_address.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String address_user = dataSnapshot.getValue(String.class);
                address.setText(address_user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity2 mainActivity2 = (MainActivity2) getActivity();
                mainActivity2.createPopUpEdit();
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                MainActivity2 mainActivity2 = (MainActivity2) getActivity();
                mainActivity2.signOutUser(root);
            }
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}