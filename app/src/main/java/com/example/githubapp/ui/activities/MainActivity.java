package com.example.githubapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.githubapp.R;
import com.example.githubapp.databinding.ActivityMainBinding;
import com.example.githubapp.data.models.Repo;
import com.example.githubapp.ui.adapters.RepoAdapter;
import com.example.githubapp.viewmodel.RepoViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private RepoViewModel repoViewModel;

    private RepoAdapter adapter;
    private ArrayList<Repo> data = new ArrayList<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadView();
        setClickListeners();
        subscribeToData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    private void loadView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new RepoAdapter(data, new RepoAdapter.Listener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.get(position).getHtmlUrl()));
                startActivity(intent);
            }
        });
        binding.recyclerView.setAdapter(adapter);
    }

    private void setClickListeners() {
        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String org = binding.edit.getText().toString().trim();
                if (!org.isEmpty()) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    repoViewModel.fetchRepos(org);
                    hideKeyboard();
                }
            }
        });
    }

    private void subscribeToData() {
        repoViewModel = ViewModelProviders.of(this).get(RepoViewModel.class);

        repoViewModel.getRepos().observe(this, new Observer<List<Repo>>() {
            @Override
            public void onChanged(List<Repo> repos) {
                data.clear();
                data.addAll(repos);
                adapter.notifyDataSetChanged();
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        repoViewModel.getError().subscribe(new io.reactivex.Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(String s) {
                Toast.makeText(MainActivity.this, R.string.error_text, Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);
                if (!data.isEmpty()) {
                    data.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
