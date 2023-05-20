package dev.sash.hsel.mad.easydo.persistence.task;

import android.os.AsyncTask;

import java.util.List;
import java.util.function.Consumer;

import dev.sash.hsel.mad.easydo.model.Todo;
import dev.sash.hsel.mad.easydo.persistence.repository.LocalRepository;

public class ReadAllTask extends AsyncTask<Void, Void, List<Todo>> {

    private LocalRepository local_repository;
    private Consumer<List<Todo>> consumer;

    public ReadAllTask(LocalRepository local_repository, Consumer<List<Todo>> consumer) {
        this.local_repository = local_repository;
        this.consumer = consumer;
    }

    @Override protected List<Todo> doInBackground(Void... voids) {
        return local_repository.read();
    }

    @Override protected void onPostExecute(List<Todo> todos) {
        consumer.accept(todos);
    }

}
