package dev.sash.hsel.mad.easydo.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import dev.sash.hsel.mad.easydo.R;
import dev.sash.hsel.mad.easydo.databinding.ActivityOverviewBinding;
import dev.sash.hsel.mad.easydo.model.Todo;
import dev.sash.hsel.mad.easydo.persistence.repository.Repository;
import dev.sash.hsel.mad.easydo.utils.Globals;
import dev.sash.hsel.mad.easydo.widget.TodoAdapter;

public class OverviewActivity extends AppCompatActivity implements TodoAdapter.Listener {

    private ActivityOverviewBinding binding;
    private Repository repository;
    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_overview);
        this.repository = ((TodoApplication) getApplication()).getRepository();
        this.adapter = new TodoAdapter(this);
        this.repository.read(todo_list -> adapter.addAll(todo_list));
        this.binding.setActivity(this);
        if (!getIntent().getBooleanExtra("connected", false))
            Snackbar.make(this.binding.getRoot(), R.string.message_no_remote_connection, Snackbar.LENGTH_SHORT).show();
    }

    @Override public void onBackPressed() {
    }

    @Override
    protected void onActivityResult(int request_code, int result_code, @Nullable Intent intent) {
        super.onActivityResult(request_code, result_code, intent);
        if (result_code == RESULT_OK && intent != null) {
            long id = intent.getLongExtra(Globals.EXTRA_ID, 0);
            switch (Objects.requireNonNull(intent.getStringExtra(Globals.EXTRA_ACTION))) {
                case Globals.ACTION_TODO_CREATED:
                    repository.read(id, todo -> adapter.add(todo));
                    Snackbar.make(binding.getRoot(), R.string.message_created_todo, Snackbar.LENGTH_SHORT).show();
                    break;
                case Globals.ACTION_TODO_UPDATED:
                    repository.read(id, todo -> adapter.set(todo));
                    Snackbar.make(binding.getRoot(), R.string.message_updated_todo, Snackbar.LENGTH_SHORT).show();
                    break;
                case Globals.ACTION_TODO_DELETED:
                    adapter.remove(id);
                    Snackbar.make(binding.getRoot(), R.string.message_deleted_todo, Snackbar.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override public void onClickAdapterItem(long id) {
        startActivityForResult(new Intent(this, DetailActivity.class).putExtra(Globals.EXTRA_ACTION, Globals.ACTION_TODO_UPDATE).putExtra(Globals.EXTRA_ID, id), Globals.REQUEST_ACTIVITY_TODO_UPDATE);
    }

    @Override public void onChangeAdapterItem(Todo todo) {
        repository.update(todo, id -> adapter.sort());
    }

    public void onClickAddButton() {
        startActivityForResult(new Intent(this, DetailActivity.class).putExtra(Globals.EXTRA_ACTION, Globals.ACTION_TODO_CREATE), Globals.REQUEST_ACTIVITY_TODO_CREATE);
    }

    public void onClickMenuItem(MenuItem item) {
        switch (item.getAlphabeticShortcut()) {
            case '0':
                adapter.toggleSortAlgorithm();
                String tag = (adapter.isNormalSorted() ? getString(R.string.action_sort_toogle_expiry_favourite) : getString(R.string.action_sort_toogle_favourite_expiry));
                item.setTitle(tag);
                Snackbar.make(binding.getRoot(), getString(R.string.message_sorted_todos) + " " + tag, Snackbar.LENGTH_SHORT).show();
                break;
            case '1':
                repository.countAllLocal(count -> {
                    if (count == 0) {
                        String json = null;
                        try {
                            InputStream stream = getAssets().open("todos.json");
                            int size = stream.available();
                            byte[] buffer = new byte[size];
                            stream.read(buffer);
                            stream.close();
                            json = new String(buffer, StandardCharsets.UTF_8);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<Todo>>() {}.getType();
                        List<Todo> list = gson.fromJson(json, type);
                        for (Todo todo : list) {
                            repository.create(todo, id -> {
                                todo.setId(id);
                                adapter.add(todo);
                            });
                        }
                    }
                });
                break;
            case '2':
                if (repository.isConnected()) {
                    repository.synchronize();
                    repository.read(todo_list -> adapter.addAll(todo_list));
                    Snackbar.make(binding.getRoot(), R.string.message_synchronized_todos, Snackbar.LENGTH_SHORT).show();
                    break;
                } else
                    Snackbar.make(this.binding.getRoot(), R.string.message_no_remote_connection, Snackbar.LENGTH_SHORT).show();
                break;
            case '3':
                new MaterialAlertDialogBuilder(this).setTitle(R.string.dialog_title_delete_local_todos).setMessage(R.string.dialog_message_delete_local_todos)
                        .setPositiveButton(R.string.dialog_action_positive, (dialog, button) -> repository.deleteAllLocal(buffer -> {
                            adapter.removeAll();
                            Snackbar.make(binding.getRoot(), R.string.message_deleted_local_todos, Snackbar.LENGTH_SHORT).show();
                        }))
                        .setNegativeButton(R.string.dialog_action_negative, (dialog, button) -> {
                        }).show();
                break;
            case '4':
                if (repository.isConnected()) {
                    new MaterialAlertDialogBuilder(this).setTitle(R.string.dialog_title_delete_remote_todos).setMessage(R.string.dialog_message_delete_remote_todos)
                            .setPositiveButton(R.string.dialog_action_positive, (dialog, button) -> repository.deleteAllRemote(buffer ->
                                    Snackbar.make(binding.getRoot(), R.string.message_deleted_remote_todos, Snackbar.LENGTH_SHORT).show()))
                            .setNegativeButton(R.string.dialog_action_negative, (dialog, button) -> {
                            }).show();
                } else
                    Snackbar.make(this.binding.getRoot(), R.string.message_no_remote_connection, Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

    public TodoAdapter getAdapter() {
        return adapter;
    }

}