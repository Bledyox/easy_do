package dev.sash.hsel.mad.easydo.persistence.task;

import android.os.AsyncTask;

import java.util.function.Consumer;

import dev.sash.hsel.mad.easydo.persistence.repository.LocalRepository;
import dev.sash.hsel.mad.easydo.persistence.repository.RemoteRepository;

public class DeleteAllTask extends AsyncTask<Void, Void, Boolean> {

    private LocalRepository local_repository;
    private RemoteRepository remote_repository;
    private Consumer<Boolean> consumer;

    public DeleteAllTask(LocalRepository local_repository, RemoteRepository remote_repository, Consumer<Boolean> consumer) {
        this.local_repository = local_repository;
        this.remote_repository = remote_repository;
        this.consumer = consumer;
    }

    @Override protected Boolean doInBackground(Void... params) {
        if (local_repository == null) return remote_repository.delete();
        if (remote_repository == null) return local_repository.delete();
        return (local_repository.delete() && remote_repository.delete());
    }

    @Override protected void onPostExecute(Boolean param) {
        consumer.accept(param);
    }

}
