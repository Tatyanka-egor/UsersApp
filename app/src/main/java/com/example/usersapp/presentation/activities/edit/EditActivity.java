package com.example.usersapp.presentation.activities.edit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.example.usersapp.R;
import com.example.usersapp.databinding.ActivityEditBinding;
import com.example.usersapp.repository.AppData;
import com.example.usersapp.repository.database.entity.User;

public class EditActivity extends AppCompatActivity {

    User  localUser;
    AppData appData;
    ActivityEditBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        appData = AppData.getInstance(getApplicationContext());




        appData.loadImage("",binding.avatarView);


        int id = getIntent().getIntExtra(AppData.ID,-1);
        if(id==-1)
        {
            localUser = new User();
        }
        else
        {
            binding.deleteButton.setVisibility(View.VISIBLE);
            binding.buttonEditOrAdd.setText("Редактировать");
            appData.db.userDao().findById(id).observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if(user==null)
                        finish();
                    localUser = user;
                    binding.nameView.setText(user.firstName);
                    appData.loadImage(user.url,binding.avatarView);
                    binding.urlView.setText(user.url);

                }
            });
            binding.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteMessage();
                }
            });
        }

        binding.urlView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence value, int start, int before, int count) {
//                Glide.with(EditActivity.this).
//                        load(binding.urlView.getText().toString()).into(binding.avatarView);
                appData.loadImage(binding.urlView.getText().toString(),binding.avatarView);
            }

            @Override
            public void afterTextChanged(Editable value) {
            }
        });
        binding.buttonEditOrAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(localUser!=null) {
                    localUser.firstName = binding.nameView.getText().toString();
                    localUser.url = binding.urlView.getText().toString();
                    appData.db.userDao().insert(localUser);
                    finish();
                }
            }
        });
    }

    private void showDeleteMessage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Вы уверены?")
        .setTitle("Удаление пользователя")
        .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                appData.db.userDao().delete(localUser);
                finish();
            }
        }).setNegativeButton("Отмена",null);
        builder.show();

    }
}