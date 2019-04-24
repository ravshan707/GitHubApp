package com.example.githubapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.githubapp.di.DaggerNetworkComponent;
import com.example.githubapp.data.models.Repo;
import com.example.githubapp.data.remote.GitHubService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;


public class RepoViewModel extends ViewModel {
    @Inject
    GitHubService gitHubApi;

    private MutableLiveData<List<Repo>> repos = new MutableLiveData<>();
    private PublishSubject<String> error = PublishSubject.create();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public RepoViewModel() {
        DaggerNetworkComponent.create().inject(this);
    }

    public void fetchRepos(String org) {
        gitHubApi.fetchRepos(org)
                .map(new Function<List<Repo>, List<Repo>>() {
                    @Override
                    public List<Repo> apply(List<Repo> repos) throws Exception {
                        Collections.sort(repos, Collections.<Repo>reverseOrder());
                        return repos.subList(0, 3);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Repo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<Repo> repos) {
                        update(repos);
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.onNext(e.getMessage());
                        update(new ArrayList<Repo>());
                    }
                });
    }

    public MutableLiveData<List<Repo>> getRepos() {
        return repos;
    }

    public PublishSubject<String> getError() {
        return error;
    }

    private void update(List<Repo> newRepos) {
        repos.postValue(newRepos);

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
