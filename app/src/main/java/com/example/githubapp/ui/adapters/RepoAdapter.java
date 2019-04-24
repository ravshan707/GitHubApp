package com.example.githubapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.githubapp.BR;
import com.example.githubapp.R;
import com.example.githubapp.data.models.Repo;

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
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.view_repo, parent, false);

        return new RepoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, final int position) {
        Repo repo = data.get(position);
        holder.bind(repo, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface Listener {
        void onClick(int position);
    }

    class RepoViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;

        RepoViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Object obj, final int position) {
            binding.setVariable(BR.obj, obj);
            binding.executePendingBindings();
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(position);
                }
            });
        }
    }
}
