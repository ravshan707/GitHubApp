package com.example.githubapp.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.githubapp.R;
import com.example.githubapp.model.models.Repo;

import java.util.ArrayList;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoViewHolder> {

    private ArrayList<Repo> data;
    private Listener listener;

    public RepoAdapter(ArrayList<Repo> data, Listener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_repo, parent, false);

        return new RepoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, final int position) {
        Repo repo = data.get(position);
        holder.name.setText(repo.getName());
        holder.stars.setText(String.valueOf(repo.getStargazersCount()));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface Listener {
        void onClick(int position);
    }

    class RepoViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView stars;
        View view;

        RepoViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.name);
            stars = view.findViewById(R.id.stars);
            this.view = view;
        }
    }
}
