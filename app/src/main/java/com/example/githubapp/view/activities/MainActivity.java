package com.example.githubapp.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.githubapp.R;
import com.example.githubapp.model.models.Repo;
import com.example.githubapp.view.adapters.RepoAdapter;
import com.example.githubapp.viewmodel.RepoViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    EditText editText;
    Button search;

    RecyclerView recyclerView;
    RepoViewModel repoViewModel;

    RepoAdapter adapter;
    ArrayList<Repo> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadView();
        setClickListeners();

        repoViewModel = ViewModelProviders.of(this).get(RepoViewModel.class);
        repoViewModel.getRepos().observe(this, new Observer<List<Repo>>() {
            @Override
            public void onChanged(List<Repo> repos) {
                data.clear();
                data.addAll(repos);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void loadView() {
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.edit);
        recyclerView = findViewById(R.id.recycler_view);
        search = findViewById(R.id.search);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new RepoAdapter(data, new RepoAdapter.Listener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.get(position).getHtmlUrl()));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    void setClickListeners() {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String org = editText.getText().toString().trim();
                if (!org.isEmpty()) {
                    repoViewModel.fetchRepos(org);
                }
            }
        });
    }
}
