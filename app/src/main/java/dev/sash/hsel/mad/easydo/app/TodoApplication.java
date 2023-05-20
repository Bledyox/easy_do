package dev.sash.hsel.mad.easydo.app;

import android.app.Application;

import dev.sash.hsel.mad.easydo.persistence.repository.Repository;
import dev.sash.hsel.mad.easydo.persistence.repository.RetrofitRepository;

public class TodoApplication extends Application {

    private Repository repository;

    public Repository getRepository() {
        return repository;
    }

    @Override public void onCreate() {
        super.onCreate();
        this.repository = new Repository(this, RetrofitRepository.NETWORK_URL);
    }

}
