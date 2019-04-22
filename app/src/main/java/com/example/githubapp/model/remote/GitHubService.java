package com.example.githubapp.model.remote;

import com.example.githubapp.model.models.Repo;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubService {

    @GET("orgs/{org}/repos")
    Single<List<Repo>> fetchRepos(@Path("org") String org);
}
