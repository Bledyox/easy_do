package dev.sash.hsel.mad.easydo.persistence.task;

import android.os.AsyncTask;

import java.util.function.Consumer;

import dev.sash.hsel.mad.easydo.persistence.repository.LocalRepository;
import dev.sash.hsel.mad.easydo.persistence.repository.RemoteRepository;

public class CountTask extends AsyncTask<Void, Void, Integer> {

    private LocalRepository local_repository;
    private RemoteRepository remote_repository;
    private Consumer<Integer> consumer;

    public CountTask(LocalRepository local_repository, RemoteRepository remote_repository, Consumer<Integer> consumer) {
        this.local_repository = local_repository;
        this.remote_repository = remote_repository;
        this.consumer = consumer;
    }

    @Override protected Integer doInBackground(Void... params) {
            if (local_repository == null) return remote_repository.count();
            if (remote_repository == null) return local_repository.count();
            int local_count = local_repository.count();
            int remote_count = remote_repository.count();
            return local_count == remote_count ? local_count : -1;
    }

    @Override protected void onPostExecute(Integer param) {
        consumer.accept(param);
    }
}