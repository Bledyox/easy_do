package dev.sash.hsel.mad.easydo.widget;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import dev.sash.hsel.mad.easydo.R;
import dev.sash.hsel.mad.easydo.databinding.AdapterTodoBinding;
import dev.sash.hsel.mad.easydo.model.Todo;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private TodoAdapter.Listener listener;
    private List<Todo> list;
    private boolean normal_sort;

    public TodoAdapter(TodoAdapter.Listener listener) {
        this.listener = listener;
        this.list = new ArrayList<>();
        this.normal_sort = true;
    }

    @NonNull @Override public TodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        AdapterTodoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_todo, parent, false);
        return new ViewHolder(binding);
    }

    @Override public void onBindViewHolder(@NonNull ViewHolder view_holder, int position) {
        view_holder.bind(list.get(position));
        view_holder.binding.setListener(listener);
    }

    @Override public int getItemCount() {
        return list.size();
    }

    public void sort() {
        if (normal_sort)
            list.sort(Comparator.comparing(Todo::isDone).thenComparing(Todo::getExpiry).thenComparing(Todo::isFavourite, Comparator.reverseOrder()));
        else
            list.sort(Comparator.comparing(Todo::isDone).thenComparing(Todo::isFavourite, Comparator.reverseOrder()).thenComparing(Todo::getExpiry));
        notifyDataSetChanged();
    }

    public void toggleSortAlgorithm() {
        this.normal_sort = !this.normal_sort;
        sort();
    }

    public boolean isNormalSorted() {
        return normal_sort;
    }

    public void add(Todo todo) {
        this.list.add(todo);
        sort();
    }

    public void addAll(List<Todo> todo_list) {
        this.list.addAll(todo_list);
        sort();
    }

    public void set(Todo todo) {
        list.set(list.indexOf(todo), todo);
        sort();
    }

    public void remove(long id) {
        list.removeIf(todo -> todo.getId() == id);
        notifyDataSetChanged();
    }

    public void removeAll() {
        list.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public AdapterTodoBinding binding;

        public ViewHolder(@NonNull AdapterTodoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Todo todo) {
            binding.setTodo(todo);
            binding.executePendingBindings();
        }

    }

    public interface Listener {
        void onClickAdapterItem(long id);
        void onChangeAdapterItem(Todo todo);
    }

}
