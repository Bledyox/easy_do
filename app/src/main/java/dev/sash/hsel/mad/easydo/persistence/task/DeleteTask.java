package dev.sash.hsel.mad.easydo.persistence.task;

import android.os.AsyncTask;

import java.util.function.Consumer;

import dev.sash.hsel.mad.easydo.model.Todo;
import dev.sash.hsel.mad.easydo.persistence.repository.LocalRepository;
import dev.sash.hsel.mad.easydo.persistence.repository.RemoteRepository;

public class DeleteTask extends AsyncTask<Todo, Void, Long> {

    private LocalRepository local_repository;
    private RemoteRepository remote_repository;
    private Consumer<Long> consumer;

    public DeleteTask(LocalRepository local_repository, RemoteRepository remote_repository, Consumer<Long> consumer) {
        this.local_repository = local_repository;
        this.remote_repository = remote_repository;
        this.consumer = consumer;
    }

    @Override protected Long doInBackground(Todo... params) {
        if (local_repository.delete(params[0]) && remote_repository != null)
            remote_repository.delete(params[0]);
        return params[0].getId();
    }

    @Override protected void onPostExecute(Long param) {
        consumer.accept(param);
    }

}