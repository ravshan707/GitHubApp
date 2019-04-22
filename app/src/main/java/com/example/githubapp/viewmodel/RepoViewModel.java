package com.example.githubapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.githubapp.model.models.Repo;
import com.example.githubapp.model.remote.GitHubService;
import com.example.githubapp.model.remote.ServiceFactory;
import java.util.Collections;
import java.util.List;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class RepoViewModel extends ViewModel {
    private MutableLiveData<List<Repo>> repos = new MutableLiveData<>();
    private GitHubService api;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public RepoViewModel() {
        this.api = ServiceFactory.getApi(GitHubService.class);
    }

    public void fetchRepos(String org) {
        api.fetchRepos(org)
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

                    }
                });
    }

    public MutableLiveData<List<Repo>> getRepos() {
        return repos;
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
