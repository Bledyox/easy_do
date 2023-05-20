package dev.sash.hsel.mad.easydo.persistence.task;

import android.os.AsyncTask;

import dev.sash.hsel.mad.easydo.persistence.repository.LocalRepository;
import dev.sash.hsel.mad.easydo.persistence.repository.RemoteRepository;

public class SynchronizeTask extends AsyncTask<Void, Void, Void> {

    private RemoteRepository remote_repository;
    private LocalRepository local_repository;

    public SynchronizeTask(RemoteRepository remote_repository, LocalRepository local_repository) {
        this.remote_repository = remote_repository;
        this.local_repository = local_repository;
    }

    @Override protected Void doInBackground(Void... voids) {
        if (local_repository.count() > 0) {
            remote_repository.delete();
            local_repository.read().forEach(todo -> remote_repository.create(todo));
        } else {
            remote_repository.read().forEach(todo -> local_repository.create(todo));
        }
        return null;
    }

}
