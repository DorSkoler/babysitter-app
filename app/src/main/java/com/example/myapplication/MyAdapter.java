package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    private MainActivity2 main;
    ArrayList<User> list;


    public MyAdapter(Context context, ArrayList<User> list, MainActivity2 main) {
        this.context = context;
        this.list = list;
        this.main = main;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.profile_card,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        User user = list.get(position);
        holder.username.setText(user.getUsername());
        holder.city.setText(user.getCity());
        holder.type.setText(user.getHelp());
        holder.image = user.getImage();
        if (user.getImage() !=null)
           Picasso.get().load(holder.image).into( holder.profilePic);
        else
            holder.profilePic.setImageResource(R.drawable.ic_profile);
        holder.email = user.getEmail();
        holder.phone = user.getPhone();
        holder.main = main;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView username, city, type;
        ImageView profilePic;
        String email, phone, image;
        MainActivity2 main;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username_card);
            city = itemView.findViewById(R.id.city_card);
            type = itemView.findViewById(R.id.type_card);
            profilePic = itemView.findViewById(R.id.img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupView();
                }
            });
        }

        public void popupView(){
            AlertDialog.Builder dialogBuilder;
            AlertDialog dialog;
            dialogBuilder = new AlertDialog.Builder(main);
            final View contactPopupView = main.getLayoutInflater().inflate(R.layout.popup_profile,  null);
            TextView newcontactpopup_fullname = (TextView) contactPopupView.findViewById(R.id.username_profile);
            TextView newcontactpopup_email = (TextView) contactPopupView.findViewById(R.id.email_profile);
            TextView newcontactpopup_city = (TextView) contactPopupView.findViewById(R.id.adress_profile);
            TextView newcontactpopup_phone = (TextView) contactPopupView.findViewById(R.id.phone_profile);
            TextView newcontactpopup_type = (TextView) contactPopupView.findViewById(R.id.help_profile);
            ImageView newcontactpopup_image = (ImageView) contactPopupView.findViewById(R.id.popup_profile_pic);

            newcontactpopup_fullname.setText(username.getText().toString());
            newcontactpopup_phone.setText(phone);
            newcontactpopup_email.setText(email);
            newcontactpopup_type.setText(type.getText().toString());
            Log.i("email", "uri" + image);
            if (image != null)
                Picasso.get().load(image).into(newcontactpopup_image);
            else
                newcontactpopup_image.setImageResource(R.drawable.ic_profile);
            newcontactpopup_city.setText(city.getText().toString());
            dialogBuilder.setView(contactPopupView);
            dialog = dialogBuilder.create();
            dialog.show();
        }
    }
}
