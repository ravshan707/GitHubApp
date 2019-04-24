package com.example.githubapp.di;

import com.example.githubapp.data.remote.GitHubModule;
import com.example.githubapp.data.remote.NetworkModule;
import com.example.githubapp.viewmodel.RepoViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class, GitHubModule.class})
public interface NetworkComponent {

    void inject(RepoViewModel repoViewModel);
}
