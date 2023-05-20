package dev.sash.hsel.mad.easydo.persistence.repository;

import android.content.Context;

import java.util.List;
import java.util.function.Consumer;

import dev.sash.hsel.mad.easydo.model.Credentials;
import dev.sash.hsel.mad.easydo.model.Todo;
import dev.sash.hsel.mad.easydo.persistence.task.ConnectionTask;
import dev.sash.hsel.mad.easydo.persistence.task.CountTask;
import dev.sash.hsel.mad.easydo.persistence.task.CreateTask;
import dev.sash.hsel.mad.easydo.persistence.task.DeleteAllTask;
import dev.sash.hsel.mad.easydo.persistence.task.DeleteTask;
import dev.sash.hsel.mad.easydo.persistence.task.ReadAllTask;
import dev.sash.hsel.mad.easydo.persistence.task.ReadTask;
import dev.sash.hsel.mad.easydo.persistence.task.SynchronizeTask;
import dev.sash.hsel.mad.easydo.persistence.task.UpdateTask;
import dev.sash.hsel.mad.easydo.persistence.task.VerifyTask;

public class Repository {

    private LocalRepository local_repository;
    private RemoteRepository remote_repository;

    private boolean connected;

    public Repository(Context context, String url) {
        this.local_repository = new RoomRepository(context);
        this.remote_repository = new RetrofitRepository(url);
    }

    public void synchronize() {
        SynchronizeTask task = new SynchronizeTask(remote_repository, local_repository);
        task.execute();
    }

    public void verify(Credentials credentials, Consumer<Boolean> consumer) {
        VerifyTask task = new VerifyTask(remote_repository, credentials, consumer);
        task.execute();
    }

    public void connection(Consumer<Boolean> consumer) {
        ConnectionTask task = new ConnectionTask(consumer, buffer -> connected = buffer);
        task.execute();
    }

    public void create(Todo todo, Consumer<Long> consumer) {
        CreateTask task = new CreateTask(local_repository, connected ? remote_repository : null, consumer);
        task.execute(todo);
    }

    public void read(Consumer<List<Todo>> consumer) {
        ReadAllTask task = new ReadAllTask(local_repository, consumer);
        task.execute();
    }

    public void read(Long id, Consumer<Todo> consumer) {
        ReadTask task = new ReadTask(local_repository, consumer);
        task.execute(id);
    }

    public void update(Todo todo, Consumer<Long> consumer) {
        UpdateTask task = new UpdateTask(local_repository, connected ? remote_repository : null, consumer);
        task.execute(todo);
    }

    public void delete(Todo todo, Consumer<Long> consumer) {
        DeleteTask task = new DeleteTask(local_repository, connected ? remote_repository : null, consumer);
        task.execute(todo);
    }

    public void deleteAllLocal(Consumer<Boolean> consumer) {
        DeleteAllTask task = new DeleteAllTask(local_repository, null, consumer);
        task.execute();
    }

    public void deleteAllRemote(Consumer<Boolean> consumer) {
        DeleteAllTask task = new DeleteAllTask(null, remote_repository, consumer);
        task.execute();
    }

    public void countAllLocal(Consumer<Integer> consumer) {
        CountTask task = new CountTask(local_repository, null, consumer);
        task.execute();
    }

    public void countAllRemote(Consumer<Integer> consumer) {
        CountTask task = new CountTask(null, remote_repository, consumer);
        task.execute();
    }

    public boolean isConnected() {
        return connected;
    }

}
