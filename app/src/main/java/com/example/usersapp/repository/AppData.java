package com.example.usersapp.repository;

import android.content.Context;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.usersapp.R;
import com.example.usersapp.repository.database.AppDatabase;

public class AppData{
    public static final String ID = "id";

    private  static AppData instance;

    public static AppData getInstance(Context context) {
        if(instance==null)
            instance = new AppData(context);
        return instance;
    }
    public AppDatabase db;

    RequestManager glide;
    private AppData(Context context) {
         db = Room.databaseBuilder(context,
                AppDatabase.class, "database-name")
                 .allowMainThreadQueries().build();
        glide = Glide.with(context);
    }
    public  void loadImage(String url, ImageView imageView)
    {
        glide.load(url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView);
    }
    public  String getValueFromEditText(EditText editText)
    {
        return  editText.getText().toString();
    }

}
