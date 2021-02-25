package com.example.usersapp.presentation.activities.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.usersapp.R;
import com.example.usersapp.databinding.ActivityProfileBinding;
import com.example.usersapp.presentation.activities.edit.EditActivity;
import com.example.usersapp.presentation.activities.main.MainActivity;
import com.example.usersapp.repository.AppData;
import com.example.usersapp.repository.database.entity.User;

public class ProfileActivity extends AppCompatActivity {
    AppData appData;
    User  localUser;
    ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        appData = AppData.getInstance(getApplicationContext());
        int id = getIntent().getIntExtra(AppData.ID,-1);
        appData.db.userDao().findById(id).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user==null)
                    finish();
                localUser  = user;
                binding.setUser(user);
                appData.loadImage(user.url,binding.avatarView);

            }
        });
        binding.goEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent editActivity = new Intent(ProfileActivity.this, EditActivity.class);
                editActivity.putExtra(AppData.ID, localUser.id);
                startActivity(editActivity);
            }
        });
    }
}