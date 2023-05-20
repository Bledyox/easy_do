package dev.sash.hsel.mad.easydo.persistence.task;

import android.os.AsyncTask;

import java.util.function.Consumer;

import dev.sash.hsel.mad.easydo.model.Credentials;
import dev.sash.hsel.mad.easydo.persistence.repository.RemoteRepository;

public class VerifyTask extends AsyncTask<Void, Void, Boolean> {

    private final String TAG = VerifyTask.class.getSimpleName();

    private RemoteRepository remote_repository;
    private Credentials credentials;
    private Consumer<Boolean> consumer;

    public VerifyTask(RemoteRepository remote_repository, Credentials credentials, Consumer<Boolean> consumer) {
        this.remote_repository = remote_repository;
        this.credentials = credentials;
        this.consumer = consumer;
    }

    @Override protected Boolean doInBackground(Void... params) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
        }
        return remote_repository.verify(credentials);
    }

    @Override protected void onPostExecute(Boolean param) {
        consumer.accept(param);
    }

}
