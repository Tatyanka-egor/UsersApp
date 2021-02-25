package com.example.usersapp.presentation.activities.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.usersapp.R;
import com.example.usersapp.databinding.ActivityMainBinding;
import com.example.usersapp.databinding.ItemUserBinding;
import com.example.usersapp.presentation.activities.edit.EditActivity;
import com.example.usersapp.presentation.activities.profile.ProfileActivity;
import com.example.usersapp.repository.AppData;
import com.example.usersapp.repository.database.entity.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    AppData appData;
    List<User> localUsers = new ArrayList<>();
    UsersAdapter adapter;
    ActivityMainBinding binding;
    LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = getLayoutInflater();
        binding = ActivityMainBinding.inflate(inflater);
        setContentView(binding.getRoot());

        appData = AppData.getInstance(getApplicationContext());
        initUsersAdapter();
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editActivity = new Intent(MainActivity.this, EditActivity.class);
                startActivity(editActivity);
            }
        });

    }

    private void initUsersAdapter() {
        adapter = new UsersAdapter();
        binding.usersView.setAdapter(adapter);

        appData.db.userDao().getAll().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                localUsers = users;
                adapter.notifyDataSetChanged();
            }
        });
    }


    private class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
        int left = 0, right = 1;
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(ItemUserBinding.inflate(inflater,parent,false));
        }

        @Override
        public int getItemViewType(int position) {
            return  position%2;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            final User itemUser = localUsers.get(position);
            holder.userBinding.nameView.setText(itemUser.firstName);
            appData.loadImage(itemUser.url,holder.userBinding.avatarView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent profileActivity = new Intent(MainActivity.this, ProfileActivity.class);
                    profileActivity.putExtra(AppData.ID, itemUser.id);

                    ActivityOptions options = ActivityOptions
                            .makeSceneTransitionAnimation(MainActivity.this,
                                    holder.userBinding.avatarView,
                                    getResources().getString(R.string.trans));

                    startActivity(profileActivity, options.toBundle());
                }
            });

        }

        @Override
        public int getItemCount() {
            return localUsers.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ItemUserBinding userBinding;
            public ViewHolder(@NonNull ItemUserBinding userBinding) {
                super(userBinding.getRoot());
                this.userBinding = userBinding;
            }
        }
    }
}