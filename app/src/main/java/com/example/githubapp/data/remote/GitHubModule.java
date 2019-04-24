package com.example.githubapp.data.remote;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class GitHubModule {
    @Provides
    public GitHubService provideGitHubService(Retrofit retrofit) {
        return retrofit.create(GitHubService.class);
    }
}
