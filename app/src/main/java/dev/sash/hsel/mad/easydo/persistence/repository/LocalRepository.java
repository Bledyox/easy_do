package dev.sash.hsel.mad.easydo.persistence.repository;

import java.util.List;

import dev.sash.hsel.mad.easydo.model.Todo;

public interface LocalRepository {
    long create(Todo todo);
    Todo read(long id);
    List<Todo> read();
    boolean update(Todo todo);
    boolean delete(Todo todo);
    boolean delete();
    int count();
}
