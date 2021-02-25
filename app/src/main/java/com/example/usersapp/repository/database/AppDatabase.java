package com.example.usersapp.repository.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.usersapp.repository.database.dao.UserDao;
import com.example.usersapp.repository.database.entity.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
