package com.example.githubapp.data.remote;

import com.example.githubapp.data.models.Repo;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubService {

    @GET("orgs/{org}/repos")
    Single<List<Repo>> fetchRepos(@Path("org") String org);
}
