package dev.sash.hsel.mad.easydo.persistence.task;

import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

import dev.sash.hsel.mad.easydo.persistence.repository.RetrofitRepository;

public class ConnectionTask extends AsyncTask<Void, Void, Boolean> {

    private Consumer<Boolean> consumer;
    private Consumer<Boolean> repository_consumer;

    public ConnectionTask(Consumer<Boolean> consumer, Consumer<Boolean> repository_consumer) {
        this.consumer = consumer;
        this.repository_consumer = repository_consumer;
    }

    @Override protected Boolean doInBackground(Void... params) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(RetrofitRepository.NETWORK_URL).openConnection();
            connection.setReadTimeout(1000);
            connection.setConnectTimeout(1500);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override protected void onPostExecute(Boolean param) {
        repository_consumer.accept(param);
        consumer.accept(param);
    }

}
