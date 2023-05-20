package dev.sash.hsel.mad.easydo.persistence.repository;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.sash.hsel.mad.easydo.model.Credentials;
import dev.sash.hsel.mad.easydo.model.Todo;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class RetrofitRepository implements RemoteRepository {


    public static final String NETWORK_URL = "http://192.168.178.54:8080/";
    public static final String EMULATOR_URL = "http://10.0.2.2:8080/";

    private static final String ID = "id";
    private static final String TODOS_PATH = "api/todos";
    private static final String USERS_PATH = "api/users/auth";

    private WebAPI api;

    public RetrofitRepository(String url) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        api = retrofit.create(WebAPI.class);
    }


    @Override public long create(Todo todo) {
        try {
            return Objects.requireNonNull(api.post(todo).execute().body()).getId();
        } catch (Exception ex) {
            return 0;
        }
    }

    @Override public Todo read(long id) {
        try {
            return api.get(id).execute().body();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override public List<Todo> read() {
        try {
            return api.get().execute().body();
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    @Override public boolean update(Todo todo) {
        try {
            return Objects.requireNonNull(api.put(todo.getId(), todo).execute().body()).getId() == todo.getId();
        } catch (Exception ex) {
            return false;
        }
    }

    @Override public boolean delete(Todo todo) {
        try {
            return api.delete(todo.getId()).execute().body();
        } catch (Exception ex) {
            return false;
        }
    }

    @Override public boolean delete() {
        try {
            return api.delete().execute().body();
        } catch (IOException ex) {
            return false;
        }
    }

    @Override public int count() {
        try {
            return Objects.requireNonNull(api.get().execute().body()).size();
        } catch (IOException ex) {
            return 0;
        }
    }

    @Override public boolean verify(Credentials credentials) {
        try {
            return api.put(credentials).execute().body();
        } catch (IOException e) {
            Log.e("TEST", e.toString());
            return false;
        }
    }

    public interface WebAPI {
        @POST(TODOS_PATH) Call<Todo> post(@Body Todo todo);

        @GET(TODOS_PATH) Call<List<Todo>> get();

        @GET(TODOS_PATH + "/{" + ID + "}") Call<Todo> get(@Path(ID) long id);

        @PUT(TODOS_PATH + "/{" + ID + "}") Call<Todo> put(@Path(ID) long id, @Body Todo todo);

        @DELETE(TODOS_PATH + "/{" + ID + "}") Call<Boolean> delete(@Path(ID) long id);

        @DELETE(TODOS_PATH) Call<Boolean> delete();

        @PUT(USERS_PATH) Call<Boolean> put(@Body Credentials credentials);

    }

}
