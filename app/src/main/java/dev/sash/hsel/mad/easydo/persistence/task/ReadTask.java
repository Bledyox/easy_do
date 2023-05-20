package dev.sash.hsel.mad.easydo.persistence.task;

import android.os.AsyncTask;

import java.util.function.Consumer;

import dev.sash.hsel.mad.easydo.model.Todo;
import dev.sash.hsel.mad.easydo.persistence.repository.LocalRepository;

public class ReadTask extends AsyncTask<Long, Void, Todo> {

    private LocalRepository local_repository;
    private Consumer<Todo> consumer;

    public ReadTask(LocalRepository local_repository, Consumer<Todo> consumer) {
        this.local_repository = local_repository;
        this.consumer = consumer;
    }

    @Override protected Todo doInBackground(Long... longs) {
        return local_repository.read(longs[0]);
    }

    @Override protected void onPostExecute(Todo todo) {
        consumer.accept(todo);
    }

}
